package nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo;

import net.sf.openrocket.document.Simulation;
import net.sf.openrocket.rocketcomponent.DeploymentConfiguration;
import net.sf.openrocket.rocketcomponent.Parachute;
import net.sf.openrocket.rocketcomponent.Rocket;
import net.sf.openrocket.simulation.SimulationOptions;
import net.sf.openrocket.simulation.SimulationStatus;
import net.sf.openrocket.simulation.exception.SimulationException;
import net.sf.openrocket.simulation.listeners.AbstractSimulationListener;
import net.sf.openrocket.util.Quaternion;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter.ParameterType;
import nz.ac.vuw.ecs.ormontecarlosimplugin.pid.Controller;
import nz.ac.vuw.ecs.ormontecarlosimplugin.pid.OrientationUtils;
import nz.ac.vuw.ecs.ormontecarlosimplugin.extend.OrSimulationExtension;
import nz.ac.vuw.ecs.ormontecarlosimplugin.util.OpenRocketUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.sf.openrocket.rocketcomponent.DeploymentConfiguration.DeployEvent.NEVER;

/**
 * A singular simulation within the monte carlo simulation, leading to one unique data point.
 * This data point is a LandingSite.
 */
public class LandingSiteSimulation extends AbstractSimulationListener {
    private Simulation simulation;

    // simulation parameter inputs
    private Map<String, Double> inputs;

    private double thrustMultiplier;

    private double motorDirection;
    private WindAltitudeModel windModel;

    private WindParameters windParams;

    // pid
    private boolean pidEnabled = false;
    private Controller controllerX = null;
    private Controller controllerY = null;

    private volatile SimulationStatus finalStatus;

    /**
     * The color for the resultant landing site.
     */
    private LandingSite.LandingColour landingSiteColor;

    /**
     * The parachute defaults for simulations where the parachute should not fail.
     */
    private Map<String, DeploymentConfiguration.DeployEvent> parachuteDefaults;

    /**
     * Create a duplicate simulation with different inputs and seed and run.
     *
     * @param simulation           a simulation to copy
     * @param seed                 a random seed for generating different wind speeds each time
     * @param inputs               a random set of monte carlo inputs
     * @param windParameters       params for a model for wind speeds at different altitudes
     * @param additionalParameters params for additional things like the PID controller values
     * @throws SimulationException SimulationException can be thrown when creating a LandingSiteSimulation.
     */
    public LandingSiteSimulation(Simulation simulation, int seed, Map<String, Double> inputs, WindParameters windParameters,
                                 Map<String, Double> additionalParameters) {
        this.simulation = simulation.copy();
        this.inputs = inputs;

        // colour of landing site
        boolean parachuteFailed = inputs.get(ParameterType.PARACHUTE_FAIL.getName()) < additionalParameters.get(ParameterType.PARACHUTE_FAIL.getName());
        this.landingSiteColor = parachuteFailed ? LandingSite.LandingColour.PARACHUTE_FAILED : LandingSite.LandingColour.PARACHUTE_DEPLOYED;

        // setup wind parameters
        windParams = windParameters;
        this.windModel = new WindAltitudeModel(windParams);

        // setup pid
        if (additionalParameters.get("usePID") == 1) {
            pidEnabled = true;

            double p = additionalParameters.get(ParameterType.P.getName());
            double i = additionalParameters.get(ParameterType.I.getName());
            double d = additionalParameters.get(ParameterType.D.getName());
            double targetAngleX = additionalParameters.get(ParameterType.TARGET_ANGLE_X.getName());
            double targetAngleZ = additionalParameters.get(ParameterType.TARGET_ANGLE_Y.getName());

            controllerX = new Controller(p, i, d, Math.toRadians(targetAngleX));
            controllerY = new Controller(p, i, d, Math.toRadians(targetAngleZ));
        }


        // remove the monte carlo extension to avoid a recursive call.
        this.simulation.getSimulationExtensions().removeIf(extension -> extension instanceof OrSimulationExtension);

        // set random seed
        SimulationOptions options = this.simulation.getOptions();
        if (options == null) {
            throw new MonteCarloException("Null simulation options");
        }
        options.setRandomSeed(seed);
        options.setLaunchIntoWind(false);

        double launchAngle = inputs.get(ParameterType.LAUNCH_ANGLE.getName());
        options.setLaunchRodAngle(launchAngle);

        double launchDir = inputs.get(ParameterType.LAUNCH_DIRECTION.getName());
        options.setLaunchRodDirection(launchDir);

        double windDir = inputs.get(ParameterType.WIND_DIRECTION.getName());
        options.setWindDirection(windDir);

        double pressure = inputs.get(ParameterType.PRESSURE.getName());
        options.setLaunchPressure(pressure);

        double temperature = inputs.get(ParameterType.TEMPERATURE.getName());
        options.setLaunchTemperature(temperature);

        double timeToOpenParachute = inputs.get(ParameterType.PARACHUTE_TIME.getName());
        thrustMultiplier = inputs.get(ParameterType.MOTOR_PERFORMANCE.getName());
        motorDirection = inputs.get(ParameterType.MOTOR_DIRECTION.getName());

        Rocket rocket = this.simulation.getRocket();

        //Save parachute default to reset if this is a parachute failed sim
        List<Parachute> parachutes = new ArrayList<>();
        OpenRocketUtil.getParachutes(rocket, parachutes);
        parachuteDefaults = new HashMap<>();
        for (Parachute parachute : parachutes) {
            parachuteDefaults.put(parachute.getID(), parachute.getDeploymentConfiguration().getDefault().getDeployEvent());
        }

        // set parachute deploy delay
        for (Parachute parachute : parachutes) {
            DeploymentConfiguration config = parachute.getDeploymentConfiguration().getDefault();

            double newDelay = config.getDeployDelay() + timeToOpenParachute;
            if (newDelay < 0) {
                newDelay = 0.0; //Avoid negative delay time
            }

            if (parachuteFailed) {
                config.setDeployEvent(NEVER);
            } else {
                String id = parachute.getID();
                config.setDeployEvent(parachuteDefaults.get(id));
            }

            config.setDeployDelay(newDelay);
        }

        // run the simulation
        try {
            this.simulation.simulate(new LandingSiteListener(this));
        } catch (SimulationException e) {
            throw new MonteCarloException(e);
        }

        //Reset parachute default deploy event
        for (Parachute parachute : parachutes) {
            DeploymentConfiguration config = parachute.getDeploymentConfiguration().getDefault();

            String id = parachute.getID();
            config.setDeployEvent(parachuteDefaults.get(id));
        }
    }

    /**
     * Return the landing site. Determined by this simulation.
     *
     * @return LandingSite as object.
     * @throws MonteCarloException monteCarloException
     */
    public LandingSite getLandingSite() throws MonteCarloException {
        if (finalStatus == null) {
            throw new MonteCarloException("Final status not determined.");
        }

        return new LandingSite(inputs, windParams, finalStatus, landingSiteColor);
    }

    /**
     * Listener. Note that listeners are cloned which is why this has to be a separate class.
     */
    private class LandingSiteListener extends AbstractSimulationListener {
        private final LandingSiteSimulation simulation;

        public LandingSiteListener(LandingSiteSimulation simulation) {
            this.simulation = simulation;
        }

        /**
         * Vary the motor direction based on the motor direction Std dev.
         *
         * @param status SimulationStatus of the simulation
         * @return true
         * @throws SimulationException exception
         * @author Harrison, Lily
         */
        public boolean preStep(SimulationStatus status) throws SimulationException {

            status.getSimulationConditions().setWindModel(windModel);

            // Change the position of the rocket to emulate the motor's direction being offset.
            double distVar = simulation.motorDirection;
            Quaternion currentOrientation = status.getRocketOrientationQuaternion();

            // Calculate the new orientation after rotating x and y axis
            Quaternion afterXAxisRotation = OrientationUtils.quaternionFromAxisAngle(1, 0, 0, Math.toRadians(distVar)).multiplyRight(currentOrientation);
            Quaternion afterRotationXy = OrientationUtils.quaternionFromAxisAngle(0, 1, 0, Math.toRadians(distVar)).multiplyRight(afterXAxisRotation);
            Quaternion afterRotationXyz = OrientationUtils.quaternionFromAxisAngle(0, 0, 1, Math.toRadians(distVar)).multiplyRight(afterRotationXy);

            // DO PID STUFF
            if (!pidEnabled) {
                return true;
            }

            // get the axis angle representation of the rocket orientation
            double rotationX = controllerX.pid(afterRotationXyz.getX());
            double rotationY = controllerY.pid(afterRotationXyz.getY());

            // Create an orientation quaternion from the PID output and rotate the rocket
            Quaternion afterPidX = OrientationUtils.quaternionFromAxisAngle(1, 0, 0, rotationX).multiplyRight(afterRotationXyz);
            Quaternion afterPidY = OrientationUtils.quaternionFromAxisAngle(0, 1, 0, rotationY).multiplyRight(afterPidX);
            status.setRocketOrientationQuaternion(afterPidY);

            return super.preStep(status);
        }

        /**
         * At the end of the simulation, set the "final status" of the simulation. This allows us to determine the final world coordinate of the rocket.
         *
         * @param status status of the simulation
         * @param exception exception if it exists
         */
        public void endSimulation(SimulationStatus status, SimulationException exception) {
            simulation.finalStatus = status;
        }

        /**
         * Multiply the thrust by the motor performance parameter.
         *
         * @param status current status of the simulation
         * @param thrust original thrust
         * @return updated thrust value
         */
        public double postSimpleThrustCalculation(SimulationStatus status, double thrust) {
            double multiplier = simulation.thrustMultiplier / 100.0;
            if (multiplier < 0.2) {
                multiplier = 0.2; // If the multiplier is too low then the simulation usually crashes
            }
            return thrust * multiplier;
        }
    }
}
