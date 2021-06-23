package nz.ac.vuw.ecs.ormontecarlosimplugin.pid;

import net.sf.openrocket.util.Quaternion;

/**
 * Utility class for quaternions and eulers.
 *
 * @author Lily, Harrison
 */
public class OrientationUtils {

    /**
     * Converts quaternion into euler for static analysis.
     *
     * @param quaternion - Orientation of object
     * @return double[] with the values XYZ in Radians
     * @Author Lily Fahey && Harrison Cook
     */
    public static double[] eulerFromQuaternion(Quaternion quaternion) {

        double w = quaternion.getW();
        double x = quaternion.getX();
        double y = quaternion.getY();
        double z = quaternion.getZ();

        double sqw = w * w;
        double sqx = x * x;
        double sqy = y * y;
        double sqz = z * z;
        double unit = sqx + sqy + sqz + sqw; // if normalised is one, otherwise is correction factor
        double test = x * y + z * w;


        double heading = Math.atan2(2 * y * w - 2 * x * z, sqx - sqy - sqz + sqw);
        double attitude = Math.asin(2 * test / unit);
        double bank = Math.atan2(2 * x * w - 2 * y * z, -sqx + sqy - sqz + sqw);

        if (test > 0.499 * unit) { // singularity at north pole
            heading = 2 * Math.atan2(x, w);
            attitude = Math.PI / 2;
            bank = 0;
        }
        if (test < -0.499 * unit) { // singularity at south pole
            heading = -2 * Math.atan2(x, w);
            attitude = -Math.PI / 2;
            bank = 0;
        }

        return new double[]{attitude, heading, bank};
    }

    /**
     * Turns a euler orientation into a Quaternion.
     *
     * @param euler orientation. Units(Radians)
     * @return Quaternion orientation
     * @author Lily
     */
    public static Quaternion quaternionFromEuler(double[] euler) {
        double yaw = euler[0];
        double pitch = euler[1];
        double roll = euler[2];


        // Abbreviations for the various angular functions
        double cosYaw = Math.cos(yaw * 0.5);
        double sinYaw = Math.sin(yaw * 0.5);
        double cosPitch = Math.cos(pitch * 0.5);
        double sinPitch = Math.sin(pitch * 0.5);
        double cosRoll = Math.cos(roll * 0.5);
        double sinRoll = Math.sin(roll * 0.5);

        double w = (cosRoll * cosPitch * cosYaw) + (sinRoll * sinPitch * sinYaw);
        double x = (sinRoll * cosPitch * cosYaw) - (cosRoll * sinPitch * sinYaw);
        double y = (cosRoll * sinPitch * cosYaw) + (sinRoll * cosPitch * sinYaw);
        double z = (cosRoll * cosPitch * sinYaw) - (sinRoll * sinPitch * cosYaw);

        return new Quaternion(w, x * -1, y, z);
    }

    /**
     * Create a quaternion from an axis angle.
     * This will be useful for rotating a quaternion with another quaternion that was created from specific axis angles.
     * Should get around the gimbal lock problem of euler angles and means the order of rotations doesn't matter.
     *
     * <p>
     * Example of use:
     * Assume we wish to rotate the original orientation quaternion by 20 degrees on the y axis
     * The operation would be achieved by 'quaternionFromAxisAngle(0, 1, 0, Math.toRadians(-20))' * originalOrientation
     * </p>
     *
     * @param axisAngleX double x axis angle
     * @param axisAngleY double y axis angle
     * @param axisAngleZ double z axis angle
     * @param theta      double magnitude of angle (radians)
     * @return Quaternion
     * @author Lily
     */
    public static Quaternion quaternionFromAxisAngle(double axisAngleX, double axisAngleY, double axisAngleZ, double theta) {

        double factor = Math.sin(theta / 2.0);

        // Calculate the x, y, z of the quaternion
        double quaternionX = axisAngleX * factor;
        double quaternionY = axisAngleY * factor;
        double quaternionZ = axisAngleZ * factor;

        // Calculate w from cos(theta/2)
        double quaternionW = Math.cos(theta / 2.0);

        return new Quaternion(quaternionW, quaternionX, quaternionY, quaternionZ).normalize();
    }

    /**
     * Converts a double array of Radians into a double array of Degrees.
     *
     * @param radEuler - Double array of Radians
     * @return - Double array of radians converted into degrees
     */
    public static double[] radEulerToDegEuler(double[] radEuler) {
        double[] degEuler = new double[3];
        for (int i = 0; i < 3; i++) {
            degEuler[i] = Math.toDegrees(radEuler[i]);
        }
        return degEuler;
    }
}
