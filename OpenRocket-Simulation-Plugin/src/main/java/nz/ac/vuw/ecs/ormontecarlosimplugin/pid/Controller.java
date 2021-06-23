package nz.ac.vuw.ecs.ormontecarlosimplugin.pid;

/**
 * A simple on-off pid controller.
 *
 * @author Lily and Harrison
 */
public class Controller {
    /**
     * A target angle for the rocket to align with. Units: Radians
     */
    private final double targetAngle;

    /**
     * The previous time stamp Used to find the difference in time between calls for Integral and Derivative values.
     */
    private double prevTimeStamp;

    /**
     * Stores the sum of all the errors * time.
     */
    private double integral;

    /**
     * This value will be updated for every reading after the first one.
     */
    private double prevError;

    /**
     * P, I and D values.
     */
    private double valueP;
    private double valueI;
    private double valueD;

    /**
     * Create a single dimension bang-bang controller with a given target angle, Units: Degrees.
     *
     * @param p           p
     * @param i           i
     * @param d           d
     * @param targetAngle targetAngle
     * @author Lily, Thomas
     */
    public Controller(double p, double i, double d, double targetAngle) {
        this.targetAngle = targetAngle;
        this.valueP = p;
        this.valueI = i;
        this.valueD = d;
    }

    /**
     * Calculate the P, I, D given the current angle of the rocket. This PID will attempt to aim the rocket back to it's
     * preferred angle.
     *
     * @param currentAngle current angle of rocket, in degrees
     * @return - Double, value representing the rotation of the gimbal to correct the course.
     * @author Harrison and Lily
     */
    public double pid(double currentAngle) {
        double riseTime = (System.currentTimeMillis() - prevTimeStamp);
        // First calculate the error. In this case, the difference in angle between current and target is this error.
        double error = targetAngle - currentAngle;

        double derivative = 0;
        if (prevTimeStamp > 0) {
            // Now, the integral part of this PID
            integral += (error * (riseTime / 1000.0));

            // Now the derivative (Check for division by zero)
            if (riseTime == 0) {
                riseTime = 1.0;
            }
            derivative = ((error - prevError) / riseTime);
        }

        this.prevError = error;
        this.prevTimeStamp = System.currentTimeMillis();

        return ((valueP * error) + (valueI * integral) + (valueD * derivative));
    }

}
