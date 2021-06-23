package nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter;

import java.util.Random;

/**
 * A uniformly distributed random parameter.
 */
public class UniformParameter extends RandomParameter {
    private final Random random = new Random();
    private double min;
    private double max;

    @Override
    public double getNextValue() {
        return random.nextDouble() * (max - min) + min;
    }

    /**
     * Initialize the random parameter with a maximum and a minimum.
     *
     * @param min min
     * @param max max
     */
    public UniformParameter(double min, double max) {
        this.min = min;
        this.max = max;
    }
}
