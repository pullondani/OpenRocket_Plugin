
package nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo;

import net.sf.openrocket.document.Simulation;
import net.sf.openrocket.simulation.SimulationOptions;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter.GaussianParameter;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter.ParameterType;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter.RandomParameter;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter.UniformParameter;
import nz.ac.vuw.ecs.ormontecarlosimplugin.extend.OrSimulationExtension;

import java.util.HashMap;
import java.util.Map;

/**
 * Gets information from the parameters set in the extension to create some random parameters.
 */
public class OpenRocketMonteCarloParameters {
    private final Simulation sim;
    private final OrSimulationExtension extension;


    /**
     * Constructor of the parameters. Takes the simulation and an extension.
     *
     * @param sim open rocket simulation to run
     * @param extension  the plugin extension
     */
    public OpenRocketMonteCarloParameters(Simulation sim, OrSimulationExtension extension) {
        this.sim = sim;
        this.extension = extension;
    }

    /**
     * The simulation to run, should be copied.
     *
     * @return Simulation as object.
     */
    public Simulation getSim() {
        return sim;
    }

    /**
     * Number of simulations to run.
     *
     * @return Number of sims as an int.
     */
    public int getNumSimulations() {
        return extension.getNumSims();
    }

    /**
     * Get a map of parameters.
     *
     * @return Parameters as a map.
     * @author Lily
     */
    public Map<String, RandomParameter> getParameters() {
        Map<String, RandomParameter> parameters = new HashMap<>();

        SimulationOptions simConfig = sim.getOptions();
        double launchAngleMean = simConfig.getLaunchRodAngle();
        double launchDirectionMean = simConfig.getLaunchRodDirection();
        double windDirectionMean = simConfig.getWindDirection();
        double pressureMean = simConfig.getLaunchPressure();
        double temperatureMean = simConfig.getLaunchTemperature();

        // mean is in radians, std dev is in degrees, change to radians
        GaussianParameter launchAngle = new GaussianParameter(launchAngleMean, Math.toRadians(extension.getLaunchAngleStdDev()));
        GaussianParameter launchDir = new GaussianParameter(launchDirectionMean, Math.toRadians(extension.getLaunchDirectionStdDev()));
        GaussianParameter windDir = new GaussianParameter(windDirectionMean, Math.toRadians(extension.getWindDirectionStdDev()));

        // mean is in Pa, std dev is in Pa
        GaussianParameter pressure = new GaussianParameter(pressureMean, extension.getPressureStdDev());

        // kelvin
        GaussianParameter temperature = new GaussianParameter(temperatureMean, extension.getTemperatureStdDev());

        // seconds
        GaussianParameter parachute = new GaussianParameter(0.0, extension.getTimeToOpenParachuteStdDev());

        // percentage
        GaussianParameter motor = new GaussianParameter(extension.getMotorPerformanceMean(), extension.getMotorPerformanceStdDev());

        // Degrees
        GaussianParameter motorDirection = new GaussianParameter(0.0, extension.getMotorDirectionStdDev());

        parameters.put(ParameterType.LAUNCH_ANGLE.getName(), launchAngle);
        parameters.put(ParameterType.LAUNCH_DIRECTION.getName(), launchDir);
        parameters.put(ParameterType.WIND_DIRECTION.getName(), windDir);
        parameters.put(ParameterType.PRESSURE.getName(), pressure);
        parameters.put(ParameterType.TEMPERATURE.getName(), temperature);
        parameters.put(ParameterType.PARACHUTE_TIME.getName(), parachute);
        parameters.put(ParameterType.MOTOR_PERFORMANCE.getName(), motor);
        parameters.put(ParameterType.MOTOR_DIRECTION.getName(), motorDirection);
        parameters.put(ParameterType.PARACHUTE_FAIL.getName(), new UniformParameter(0, 1));


        return parameters;
    }

    /**
     * Get the wind altitude model for the simulation.
     *
     * @return
     */
    public WindParameters getWindParameters() {
        return new WindParameters(extension.getWindSpeedAltitudes(), extension.getWindDirectionAltitudes(),
                sim.getOptions().getWindSpeedDeviation(), extension.getWindDirectionStdDev(), extension.getWindAltitudeStep());
    }

    /**
     * Additional non-randomized parameters.
     *
     * @return
     */
    public Map<String, Double> getAdditionalParameters() {
        Map<String, Double> additionalParameters = new HashMap<>();

        additionalParameters.put("usePID", extension.getUsePid() ? 1.0 : 0.0);
        additionalParameters.put(ParameterType.P.getName(), extension.getP());
        additionalParameters.put(ParameterType.I.getName(), extension.getI());
        additionalParameters.put(ParameterType.D.getName(), extension.getD());
        additionalParameters.put(ParameterType.TARGET_ANGLE_X.getName(), extension.getTargetAngleX());
        additionalParameters.put(ParameterType.TARGET_ANGLE_Y.getName(), extension.getTargetAngleY());
        additionalParameters.put(ParameterType.PARACHUTE_FAIL.getName(), extension.getParachuteFailRate());

        return additionalParameters;


    }
}