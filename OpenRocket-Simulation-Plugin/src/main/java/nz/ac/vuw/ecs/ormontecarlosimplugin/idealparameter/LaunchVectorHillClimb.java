package nz.ac.vuw.ecs.ormontecarlosimplugin.idealparameter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.openrocket.document.Simulation;
import net.sf.openrocket.simulation.SimulationStatus;
import nz.ac.vuw.ecs.ormontecarlosimplugin.extend.OrSimulationExtension;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.LandingSite;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.OpenRocketMonteCarloParameters;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.OpenRocketMonteCarloSimulation;


public class LaunchVectorHillClimb extends AbstractHillClimb {
    // number of simulations per step. Takes an average distance.
    private static final int NUM_SIMULATION = 10;


    private SimulationStatus status;
    private OrSimulationExtension simulationExtension;

    /**
     * Hill climbing operation to find the ideal launch vector that gives the lowest possible distance from the launch site.
     *
     * @param status              simulation status
     * @param simulationExtension the extension
     */
    public LaunchVectorHillClimb(SimulationStatus status, OrSimulationExtension simulationExtension) {
        super(15, getInitialParameters(status.getSimulationConditions().getLaunchRodAngle(), status.getSimulationConditions().getLaunchRodDirection()),
                getInitialSteps(simulationExtension.getMaxAngle()), getInitialMinValues(simulationExtension.getMaxAngle()),
                getInitialMaxValues(simulationExtension.getMaxAngle()), getShouldLoop());
        this.status = status;
        this.simulationExtension = simulationExtension;
    }


    @Override
    protected double doSimulation(Map<String, Double> parameters) {
        double launchRodAngle = parameters.get("launchRodAngle");
        double launchDirection = parameters.get("launchDirection");

        // duplicate simulation with new rod angle and direction
        Simulation simulation = status.getSimulationConditions().getSimulation().copy();

        simulation.getOptions().setLaunchRodAngle(Math.toRadians(launchRodAngle));
        simulation.getOptions().setLaunchRodDirection(Math.toRadians(launchDirection));

        OrSimulationExtension extensionDuplicate = (OrSimulationExtension) simulationExtension.clone();
        extensionDuplicate.setNumSims(NUM_SIMULATION);
        extensionDuplicate.setLaunchAngleStdDev(0);
        extensionDuplicate.setLaunchDirectionStdDev(0);

        // run the simulation
        OpenRocketMonteCarloParameters orParameters = new OpenRocketMonteCarloParameters(simulation, extensionDuplicate);

        OpenRocketMonteCarloSimulation mcSimulation = new OpenRocketMonteCarloSimulation(orParameters);

        Collection<LandingSite> landingSites = mcSimulation.monteCarloSim();

        // calculate average distance
        double distance = 0;

        LandingSite launch = new LandingSite(Collections.emptyMap(), null, status, LandingSite.LandingColour.LAUNCH_LOCATION);

        for (LandingSite landingSite : landingSites) {
            distance += landingSite.distanceTo(launch);
        }

        distance /= NUM_SIMULATION;

        return distance;

    }


    private static Map<String, Double> getInitialParameters(Double launchRodAngle, Double launchDirection) {
        Map<String, Double> initialParameters = new HashMap<>();

        initialParameters.put("launchRodAngle", launchRodAngle);
        initialParameters.put("launchDirection", launchDirection);

        return initialParameters;
    }

    private static Map<String, Double> getInitialSteps(Double maxAngle) {
        Map<String, Double> initialSteps = new HashMap<>();
        initialSteps.put("launchRodAngle", maxAngle);
        initialSteps.put("launchDirection", 180.0);


        return initialSteps;
    }

    private static Map<String, Double> getInitialMinValues(Double maxAngle) {
        Map<String, Double> minValues = new HashMap<>();
        minValues.put("launchRodAngle", -maxAngle);
        minValues.put("launchDirection", 0.0);


        return minValues;
    }

    private static Map<String, Double> getInitialMaxValues(Double maxAngle) {
        Map<String, Double> maxValues = new HashMap<>();
        maxValues.put("launchRodAngle", maxAngle);
        maxValues.put("launchDirection", 360.0);

        return maxValues;

    }

    private static Map<String, Boolean> getShouldLoop() {
        Map<String, Boolean> shouldLoop = new HashMap<>();
        shouldLoop.put("launchRodAngle", false);
        shouldLoop.put("launchDirection", true);

        return shouldLoop;

    }
}

