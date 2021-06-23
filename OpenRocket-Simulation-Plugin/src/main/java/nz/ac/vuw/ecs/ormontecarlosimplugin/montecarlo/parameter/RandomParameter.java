package nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter;

/**
 * A parameter is a parameter that is randomized.
 * All parameters are doubles.
 */
public abstract class RandomParameter {

    /**
     * Return a random double for this parameter.
     *
     * @return a double variable
     */
    public abstract double getNextValue();
}
