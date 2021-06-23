package nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter;

import java.util.Random;

/**
 * A random parameter that is normally distributed.
 */
public class GaussianParameter extends RandomParameter {
    private final Random random = new Random();

    private double mean;
    private double standardDev;

    @Override
    public double getNextValue() {
        return mean + random.nextGaussian() * standardDev;
    }

    /**
     * Initialize the random parameter with a maximum and a minimum.
     *
     * @param mean        mean
     * @param standardDev standardDev
     */
    public GaussianParameter(double mean, double standardDev) {
        this.mean = mean;
        this.standardDev = standardDev;
    }

    /**
     * Gets the Mean for this GaussianParameter.
     *
     * @return Mean as a double.
     */
    public double getMean() {
        return mean;
    }

    /**
     * Gets the Standard Deviation for this GaussianParameter.
     *
     * @return Std Dev as a double.
     */
    public double getStandardDev() {
        return standardDev;
    }
}
