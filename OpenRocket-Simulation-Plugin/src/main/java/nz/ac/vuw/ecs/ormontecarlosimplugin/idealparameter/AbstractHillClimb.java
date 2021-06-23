package nz.ac.vuw.ecs.ormontecarlosimplugin.idealparameter;

import java.util.ArrayList;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

/**
 * An abstract hill climbing operation. (Finding a minimum).
 * The method is implemented in the doSimulation abstract method.
 * This method tests 7 different points (-s, -0.2s, -0.04s, 0, +0.04s, +0.2s, +s) on each step.
 * This method works well if you make the initial step value large (pick largest reasonable value you can think of).
 *
 * @author Thomas, Naveen
 */
public abstract class AbstractHillClimb {

    private List<String> inputs;

    /**
     * The parameters at the current step.
     */
    private Map<String, Double> parameters;

    /**
     * The amount of steps.
     */
    private Map<String, Double> steps;

    /**
     * The current input parameter to adjust.
     */
    private int inputToAdjust = 0;

    /**
     * A map for the minimum values.
     */
    private Map<String, Double> minValues;

    /**
     * A map for the maximum values.
     */
    private Map<String, Double> maxValues;

    /**
     * A map for telling if an input should loop (eg: directions from 0 to 360).
     */
    private Map<String, Boolean> shouldLoop;

    /**
     * The number of steps to run.
     */
    private int numSteps;


    /**
     * An abstract hill climb machine learning operation that finds an ideal minimum.
     *
     * @param initialParameters - These are the initial parameters that we start with.
     * @param initialSteps - These are the initial steps.
     */
    public AbstractHillClimb(int numSteps, Map<String, Double> initialParameters, Map<String, Double> initialSteps, Map<String, Double> minValues,
                             Map<String, Double> maxValues, Map<String, Boolean> shouldLoop) {

        this.numSteps = numSteps;
        if (!initialParameters.keySet().equals(initialSteps.keySet())) {
            throw new IllegalArgumentException("Key values for parameters and steps should be the same");
        }
        if (!minValues.keySet().equals(initialParameters.keySet())) {
            throw new IllegalArgumentException("Key values for parameters and steps should be the same");
        }
        if (!maxValues.keySet().equals(initialParameters.keySet())) {
            throw new IllegalArgumentException("Key values for parameters and steps should be the same");
        }
        if (!shouldLoop.keySet().equals(initialParameters.keySet())) {
            throw new IllegalArgumentException("Key values for parameters and steps should be the same");
        }


        this.parameters = initialParameters;
        this.steps = initialSteps;
        this.minValues = minValues;
        this.maxValues = maxValues;
        this.shouldLoop = shouldLoop;
        this.inputs = new ArrayList<>(parameters.keySet());

    }

    /**
     * A constructor without a minimum and maximum.
     *
     * @param initialParameters A map for the initialParameters.
     * @param initialSteps  A map for the initialSteps.
     */
    public AbstractHillClimb(int numSteps, Map<String, Double> initialParameters, Map<String, Double> initialSteps) {
        this(numSteps, initialParameters, initialSteps, generateMins(initialParameters.keySet()),
                generateMax(initialParameters.keySet()), generateShouldLoop(initialParameters.keySet(), false));
    }

    private static Map<String, Double> generateMins(Collection<String> keys) {
        Map<String, Double> mins = new HashMap<>();
        for (String key : keys) {
            mins.put(key, Double.NEGATIVE_INFINITY);
        }
        return mins;
    }

    private static Map<String, Double> generateMax(Collection<String> keys) {
        Map<String, Double> max = new HashMap<>();
        for (String key : keys) {
            max.put(key, Double.POSITIVE_INFINITY);
        }
        return max;
    }

    private static Map<String, Boolean> generateShouldLoop(Collection<String> keys, boolean shouldLoop) {
        Map<String, Boolean> loop = new HashMap<>();
        for (String key : keys) {
            loop.put(key, shouldLoop);
        }
        return loop;
    }


    /**
     * Simulate 1 step of the process. The output of this method should be the value you are trying to minimise.
     *
     * @param parameters input parameters
     */
    protected abstract double doSimulation(Map<String, Double> parameters);


    /**
     * Make 1 step in the "hill climbing" operation.
     * This is some method we randomly came up with in the lab.
     * It tests 7 points and goes toward the minimum. The spread between the 7 points decreases each iteration.
     */
    private void step() {
        // select next input to change
        inputToAdjust = (inputToAdjust + 1) % inputs.size();

        String parameterKey = inputs.get((inputToAdjust));

        // work out the 7 input values
        Double[] inputValues = new Double[7];
        Double middle = this.parameters.get(parameterKey);
        Double s = this.steps.get(parameterKey);
        inputValues[0] = middle - s;
        inputValues[1] = middle - s * (0.2);
        inputValues[2] = middle - s * (0.04);
        inputValues[3] = middle;
        inputValues[4] = middle + s * (0.2);
        inputValues[5] = middle + s * (0.04);
        inputValues[6] = middle + s;

        for (int i = 0; i < inputValues.length; i++) {
            // wrap variables around if they go over the maximum/minimum. This is used because
            Double min = this.minValues.get(parameterKey);
            Double max = this.maxValues.get(parameterKey);

            if (shouldLoop.get(parameterKey)) {
                while (inputValues[i] < min || inputValues[i] > max) {
                    // loop from minimum
                    if (inputValues[i] < min) {
                        double dif = min - inputValues[i];
                        inputValues[i] = max - dif;
                        // loop from maximum
                    } else if (inputValues[i] > max) {
                        double dif = inputValues[i] - max;
                        inputValues[i] = min + dif;
                    }

                }
            } else {

                if (inputValues[i] < min) {
                    inputValues[i] = min;
                } else if (inputValues[i] > max) {
                    inputValues[i] = max;
                }
            }
        }


        // calculate output values and find the minimum
        Double[] simulatedValues = new Double[7];

        int minIndex = -1;
        double minValue = Double.MAX_VALUE;

        Map<String, Double> parameters = new HashMap<>(this.parameters);
        for (int i = 0; i < simulatedValues.length; i++) {
            parameters.put(parameterKey, inputValues[i]);
            Double simulationValue = doSimulation(parameters);
            simulatedValues[i] = simulationValue;

            if (simulationValue < minValue) {
                minValue = simulationValue;
                minIndex = i;
            }
        }

        // handle each case, update the step values and new middle input
        switch (minIndex) {
            case 0:
                steps.put(parameterKey, s);
                this.parameters.put(parameterKey, inputValues[0]);
                break;
            case 1:
                steps.put(parameterKey, s * 0.8);
                this.parameters.put(parameterKey, inputValues[1]);
                break;
            case 2:
                steps.put(parameterKey, s * 0.16);
                this.parameters.put(parameterKey, inputValues[2]);
                break;
            case 3:
                steps.put(parameterKey, s * 0.04);
                this.parameters.put(parameterKey, inputValues[3]);
                break;
            case 4:
                steps.put(parameterKey, s * 0.16);
                this.parameters.put(parameterKey, inputValues[4]);
                break;
            case 5:
                steps.put(parameterKey, s * 0.8);
                this.parameters.put(parameterKey, inputValues[5]);
                break;
            case 6:
                steps.put(parameterKey, s);
                this.parameters.put(parameterKey, inputValues[6]);
                break;

            default:
                break;
        }



    }

    /**
     * This is a map that is for the calculation of the ideal parameters.
     *
     * @return
     */
    public Map<String, Double> findIdealParameters() {

        for (int i = 0; i < numSteps; i++) {
            step();
        }
        return parameters;
    }

}
