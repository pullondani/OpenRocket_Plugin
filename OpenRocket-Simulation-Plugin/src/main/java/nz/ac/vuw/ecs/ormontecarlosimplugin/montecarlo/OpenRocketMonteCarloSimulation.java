package nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo;

import net.sf.openrocket.document.Simulation;

import java.util.Map;
import java.util.Random;

/**
 * An open rocket monte carlo simulation. This creates multiple LandingSiteSimulations and runs them. The result to be returned is a landing site
 */
public class OpenRocketMonteCarloSimulation extends AbstractMonteCarlo<LandingSite> {
    private Simulation parentSimulation;
    private WindParameters windParameters;
    private Map<String, Double> additionalParameters;
    private Random seedGenerator = new Random();

    /**
     * Create a monte carlo simulation from an open rocket simulation.
     *
     * @param parameters parameters object contains data about the simulation
     */
    public OpenRocketMonteCarloSimulation(OpenRocketMonteCarloParameters parameters) {
        super(parameters.getNumSimulations(), parameters.getParameters());

        parentSimulation = parameters.getSim();
        windParameters = parameters.getWindParameters();
        additionalParameters = parameters.getAdditionalParameters();
    }

    /**
     * Create a "LandingSiteSimulation" which runs a simulation with generated parameters.
     *
     * @param inputs the values of the parameters as doubles
     * @return a landing site array, containing two landing sites for a normal sim and a sim where the parachute failed
     */
    protected LandingSite simulate(Map<String, Double> inputs) {
        int seed = seedGenerator.nextInt();

        LandingSiteSimulation simulation = new LandingSiteSimulation(parentSimulation, seed, inputs, windParameters, additionalParameters);
        LandingSite deployedSite = simulation.getLandingSite();

        return deployedSite;
    }

    /**
     * Get sim.
     *
     * @return Gets the OpenRocket simulation
     */
    public Simulation getSimulation() {
        return parentSimulation;
    }
}
