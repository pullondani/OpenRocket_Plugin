package nz.ac.vuw.ecs.ormontecarlosimplugin.extend;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import net.sf.openrocket.document.Simulation;
import net.sf.openrocket.simulation.SimulationStatus;
import net.sf.openrocket.simulation.exception.SimulationException;
import net.sf.openrocket.simulation.listeners.AbstractSimulationListener;
import nz.ac.vuw.ecs.ormontecarlosimplugin.idealparameter.LaunchVectorHillClimb;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.LandingSite;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.MonteCarloResult;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.OpenRocketMonteCarloParameters;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.MonteCarloException;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.OpenRocketMonteCarloSimulation;
import nz.ac.vuw.ecs.ormontecarlosimplugin.gui.SimulationOutputGui;

/**
 * The plugin listener is responsible for listening to the start of the simulation and running our adjusted monte carlo simulation of open rocket.
 */
public class PluginListener extends AbstractSimulationListener {

    private final OrSimulationExtension extension;

    /**
     * Create new plugin listener.
     *
     * @param extension the extension (to get variables from)
     */
    public PluginListener(OrSimulationExtension extension) {
        this.extension = extension;
    }

    /**
     * When open rocket's simulation begins. Start our own monte carlo simulation.
     *
     * @param status begin status of the simulation
     * @throws SimulationException an exception if the start of the simulation failed. This will throw if there are exceptions within the monte carlo simulation
     */
    public void startSimulation(SimulationStatus status) throws SimulationException {
        try {
            Simulation sim = status.getSimulationConditions().getSimulation();

            // Find ideal launch vector
            if (extension.getIdealLaunch()) {
                LaunchVectorHillClimb findVector = new LaunchVectorHillClimb(status, extension);

                Map<String, Double> idealParameters = findVector.findIdealParameters();

                // update launch rod angle and launch direction to new ideal launch rod angle and direction
                double launchRodAngle = Math.toRadians(idealParameters.get("launchRodAngle"));
                double launchDirection = Math.toRadians(idealParameters.get("launchDirection"));


                sim.getOptions().setLaunchIntoWind(false);
                sim.getOptions().setLaunchRodAngle(launchRodAngle);
                sim.getOptions().setLaunchRodDirection(launchDirection);
            }
            // obtain the parameters for the monte carlo simulation
            OpenRocketMonteCarloParameters parameters = new OpenRocketMonteCarloParameters(sim, extension);

            // create a new monte carlo simulation from those parameters
            OpenRocketMonteCarloSimulation simulation = new OpenRocketMonteCarloSimulation(parameters);

            // run the monte carlo simulation, getting a collection of landing sites
            Collection<LandingSite> landingSites = simulation.monteCarloSim();

            MonteCarloResult result = new MonteCarloResult(
                    landingSites,
                    new LandingSite(Collections.emptyMap(), null, status, LandingSite.LandingColour.LAUNCH_LOCATION), parameters
            );


            // Open the output gui
            new SimulationOutputGui(result, extension.getWindAltitudeStep());
        } catch (MonteCarloException e) {
            throw new SimulationException(e);
        }
    }
}
