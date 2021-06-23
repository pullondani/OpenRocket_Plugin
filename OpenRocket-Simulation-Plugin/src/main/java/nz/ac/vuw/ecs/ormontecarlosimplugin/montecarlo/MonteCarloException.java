package nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo;

/**
 * Monte carlo exception occurs when there is an exception during a monte carlo simulation.
 */
public class MonteCarloException extends RuntimeException {
    /**
     * No parameters.
     */
    public MonteCarloException() {
        super();
    }

    /**
     * Exception with message.
     *
     * @param msg message
     */
    public MonteCarloException(String msg) {
        super(msg);
    }

    /**
     * Exception with another throwable as the cause.
     *
     * @param cause cause
     */
    public MonteCarloException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with both a message and a cause.
     *
     * @param message message
     * @param cause cause
     */
    public MonteCarloException(String message, Throwable cause) {
        super(message, cause);
    }
}
