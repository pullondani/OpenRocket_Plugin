package nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo;

import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter.RandomParameter;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

/**
 * An abstract monte carlo simulation.
 *
 * @param <T> Type of value to return from the simulation
 */
public abstract class AbstractMonteCarlo<T> {
    private int numSimulations;

    private Map<String, RandomParameter> parameters;

    /**
     * Create an abstract monte carlo simulation.
     *
     * @param numSimulations number of times to repeat the simulation
     * @param parameters     all parameters to vary
     */
    public AbstractMonteCarlo(int numSimulations, Map<String, RandomParameter> parameters) {
        this.numSimulations = numSimulations;
        this.parameters = parameters;
    }

    /**
     * Simulate monte carlo with parallel execution.
     * This should run faster on machines with higher core counts if the simulation is computationally expensive.
     *
     * @return
     */
    public Collection<T> parallelMonteCarloSim() {
        List<T> toReturn = new ArrayList<>();

        IntStream.range(0, numSimulations).parallel().peek((int i) -> {
            Map<String, Double> inputs = new HashMap<>();
            for (String inputName : parameters.keySet()) {
                double value = parameters.get(inputName).getNextValue();

                inputs.put(inputName, value);
            }
            toReturn.add(simulate(inputs));
        });

        return toReturn;
    }

    /**
     * Simulate monte carlo sequentially.
     *
     * @return
     */
    public Collection<T> monteCarloSim() {
        List<T> results = new ArrayList<>();

        for (int i = 0; i < numSimulations; i++) {
            Map<String, Double> inputs = new HashMap<>();
            for (String inputName : parameters.keySet()) {
                double value = parameters.get(inputName).getNextValue();
                inputs.put(inputName, value);
            }

            T result = simulate(inputs); // boolean - is this in the circle);

            results.add(result);
        }

        return results;
    }

    /**
     * Start the simulation with some set parameters.
     *
     * @param inputs the values of the parameters
     * @return result of the simulation
     */
    protected abstract T simulate(Map<String, Double> inputs);
}
