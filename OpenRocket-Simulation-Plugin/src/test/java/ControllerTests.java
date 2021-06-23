import net.sf.openrocket.util.Quaternion;
import nz.ac.vuw.ecs.ormontecarlosimplugin.pid.OrientationUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test the PID Controller.
 *
 * @author Lily, Harrison
 */
public class ControllerTests {


    /**
     * Comparing floating points after equations needs a little rounding to ensure that small differences due to
     * uncontrollable calculation differences in COMPUTERS are ignored.
     */
    private static final double DELTA = 0.0000001;


    /**
     * Rotate a quaternion by an axis angle.
     */
    @Test
    public void quaternionRotationWithAxisAngle() {
        Quaternion startOrientation = new Quaternion(0.7897607, 0.4119345, 0.1920882, 0.4119345);
        Quaternion rotationQuaternion = OrientationUtils.quaternionFromAxisAngle(1, 0, 0, Math.toRadians(10));
        Quaternion finalOrientation = rotationQuaternion.multiplyRight(startOrientation);
        Quaternion answer = new Quaternion(0.750853, 0.4791992, 0.1554548, 0.4271086);

        assertEquals("answer == actual", answer.getW(), finalOrientation.getW(), 0.000001);
        assertEquals("answer == actual", answer.getX(), finalOrientation.getX(), 0.000001);
        assertEquals("answer == actual", answer.getY(), finalOrientation.getY(), 0.000001);
        assertEquals("answer == actual", answer.getZ(), finalOrientation.getZ(), 0.000001);
    }

    /**
     * Convert an axis angle to a quaternion.
     */
    @Test
    public void axisAngleToQuaternion() {
        Quaternion answer = new Quaternion(0.996195, 0.0871557, 0, 0);
        Quaternion actual = OrientationUtils.quaternionFromAxisAngle(1, 0, 0, Math.toRadians(10));
        assertEquals("answer == actual", answer.getW(), actual.getW(), 0.000001);
        assertEquals("answer == actual", answer.getX(), actual.getX(), 0.000001);
        assertEquals("answer == actual", answer.getY(), actual.getY(), 0.000001);
        assertEquals("answer == actual", answer.getZ(), actual.getZ(), 0.000001);
    }

    /**
     * Convert quaternion to a euler.
     */
    @Test
    public void quaternionToEuler() {
        // EULER DEGREES:  x: 53.1301017, y: 90, z: 0
        Quaternion quaternion = new Quaternion(1.0, 0.5, 1.0, 0.5);
        double[] eulerXyz = OrientationUtils.eulerFromQuaternion(quaternion);
        double[] answer = {0.9272952, 1.5707963, 0};

        for (int i = 0; i < eulerXyz.length; i++) {
            assertEquals(answer[i], eulerXyz[i], DELTA);
        }
    }

    /**
     * Convert euler to a quaternion.
     */
    @Test
    public void eulerToQuaternion() {
        double[] euler = new double[]{0.9272952, 1.5707963, 0};
        Quaternion quaternion = OrientationUtils.quaternionFromEuler(euler);
        Quaternion answer = new Quaternion(0.6324555, 0.3162278, 0.6324555, 0.3162278);
        assertEquals(answer.getW(), quaternion.getW(), DELTA);
        assertEquals(answer.getX(), quaternion.getX(), DELTA);
        assertEquals(answer.getY(), quaternion.getY(), DELTA);
        assertEquals(answer.getZ(), quaternion.getZ(), DELTA);
    }

    /**
     * Convert radians to degrees for eulers.
     */
    @Test
    public void eulerRadToDeg() {
        Quaternion quaternion = new Quaternion(1.0, 0.5, -1, 0.5);
        double[] eulerXyz = OrientationUtils.eulerFromQuaternion(quaternion);
        double[] eulerDeg = OrientationUtils.radEulerToDegEuler(eulerXyz);

        for (int i = 0; i < eulerDeg.length; i++) {
            assertEquals(Math.toDegrees(eulerXyz[i]), eulerDeg[i], DELTA);
        }
    }
}
