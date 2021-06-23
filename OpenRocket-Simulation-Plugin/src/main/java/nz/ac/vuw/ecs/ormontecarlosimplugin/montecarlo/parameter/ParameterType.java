package nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter;

/**
 * Represents each of the parameters that affect the rocket simulation.
 * All units are based on how user inputs the values to the gui.
 *
 * @author Lily, Thomas
 */
public enum ParameterType {
    /**
     * Latitude of launch site: degrees.
     */
    LAUNCH_LAT("launchLat", "Latitude of the launch site in degrees.", 0, 45, -90, 90, 0),

    /**
     * Longitude of launch site: degrees.
     */
    LAUNCH_LONG("launchLong", "Longitude of the launch site in degrees.", 0, 0, -180, 180, 0),

    /**
     * Altitude of launch site: meters.
     */
    ALTITUDE("altitude", "Altitude of the launch site in metres.", 0, 0, 0, 1000, 0),

    /**
     * angle of launch rod perpendicular from ground: degrees.
     */
    LAUNCH_ANGLE("rodAngle", "Angle of the rocket in degrees. 0 points straight upwards.", 5, 0, -60, 60, 90),

    /**
     * NSEW direction of launch rod: degrees.
     */
    LAUNCH_DIRECTION("launchDirection", "Angle the rocket is pointed in degrees. North = 0, clockwise is positive.", 5, 90, 0, 360, 90),

    /**
     * Direction of wind: degrees.
     */
    WIND_DIRECTION("windDirection", "Wind direction in degrees. North = 0, clockwise is positive.", 15, 90, 0, 360, 180),

    /**
     * Speed of wind assuming uniform speed: meters per second.
     */
    WIND_SPEED("windspeed", "Speed of the wind in metres per second.", 0.2, 2, 0, 100, 0.6),

    /**
     * Motor performance as percentage of max performance: percentage.
     */
    MOTOR_PERFORMANCE("motorPerformance", "The thrust of the motor as a percentage of its ideal performance.", 15, 85, 0, 100, 50),

    /**
     * Number of simulations to run: int.
     */
    SIM_NUMBER("numSimulations", "Number of monte carlo simulation data-points to simulate.", 0, 100, 1, 10000, 0),

    /**
     * Pressure of launch environment: Pa.
     */
    PRESSURE("pressure", "Pressure at the launch site in pascals.", 1000, 100000, 90000, 105000, 5000),

    /**
     * Temperature of launch environment: degrees C.
     */
    TEMPERATURE("temperature", "Temperature at the launch site in degrees C.", 1, 15, -20, 35, 5),

    /**
     * Time to open parachute: Seconds.
     */
    PARACHUTE_TIME("parachuteTime", "A delay to how long the parachute should take to open in seconds.", 1, 0, 0, Double.MAX_VALUE, 10),

    /**
     * Angle of Motor (Motor nozzle): Degrees.   (0 is aligned with rocket).
     */
    MOTOR_DIRECTION("motorDirection", "Angle of the motor nozzle in degrees. Offset from the rocket's centre axis.", 0, 0, -10, 10, 10),

    /**
     * Maximum angle: degrees.
     */
    MAX_ANGLE("maxAngle", "Maximum launch angle for the rocket. Useful for calculating the ideal rocket angle.", 0.0, 60.0, 0.0, 90.0, 0.0),


    /**
     * Wind altitude step: km.
     */
    WIND_ALTITUDE_STEP("windAltitudeStep", "Difference between each altitude you specify different wind speed/directions for.", 0.0, 1.0, 0.01, 100.0, 0),

    /**
     * Wind altitude values: integer.
     */
    WIND_ALTITUDE_VALUES("windAltitudeValues", "Number of different altitudes to specify different wind speed/directions for.", 0, 3, 1, 10, 0),

    /**
     * PID - p.
     */
    P("p", "Proportional multiplier for the PID controller.", 0, 0.7, 0, 1, 0),

    /**
     * PID - i.
     */
    I("i", "Integral multiplier for the PID controller.", 0, 0.05, 0, 1, 0),

    /**
     * PID - d.
     */
    D("d", "Derivative multiplier for the PID controller.", 0, 0.005, 0, 1, 0),


    /**
     * PID - target angle X axis.
     */
    TARGET_ANGLE_X("targetAngleX", "The target angle to aim for in the X axis. (Degrees)", 0, 0, -60.0, 60.0, 0),
    /**
     * PID - target angle Y axis.
     */
    TARGET_ANGLE_Y("targetAngleY", "The target angle to aim for in the Y axis. (Degrees)", 0, 0, -60.0, 60.0, 0),


    /**
     * Parachute fail rate.
     */
    PARACHUTE_FAIL("parachuteFail", "Rate at which the parachute will fail. 1 = always fails. 0 = never fails. (value from 0 to 1)", 0, 0.5, 0, 1, 0);

    /**
     * Name of parameter type.
     */
    private final String name;

    /**
     * Default deviation of parameter type.
     */
    private final double defaultDeviation;

    /**
     * Default minimum value of parameter type.
     */
    private final double defaultMin;

    /**
     * Default maximum value of parameter type.
     */
    private final double defaultMax;

    /**
     * Default Mean value of parameter type.
     */
    private final double defaultMean;

    /**
     * Default maximum deviation value of parameter type.
     */
    private final double deviationMax;

    /**
     * Description of what the parameter does.
     */
    private final String description;

    /**
     * Initialise a parameter type.
     *
     * @param name             name
     * @param description      description
     * @param defaultDeviation deviation
     * @param defaultMean      mean
     * @param defaultMin       min
     * @param defaultMax       max
     * @param deviationMax     deviation max
     */
    ParameterType(String name, String description, double defaultDeviation, double defaultMean, double defaultMin, double defaultMax, double deviationMax) {
        this.name = name;
        this.description = description;
        this.defaultDeviation = defaultDeviation;
        this.defaultMax = defaultMax;
        this.defaultMin = defaultMin;
        this.defaultMean = defaultMean;
        this.deviationMax = deviationMax;
    }

    /**
     * Get Default deviation of this parameter.
     *
     * @return double deviation
     */
    public double getDefaultDeviation() {
        return defaultDeviation;
    }

    /**
     * Get default max value of this parameter.
     *
     * @return double max
     */
    public double getDefaultMax() {
        return defaultMax;
    }

    /**
     * Get default mean value of this parameter.
     *
     * @return double mean
     */
    public double getDefaultMean() {
        return defaultMean;
    }

    /**
     * Get default minimum value for this parameter.
     *
     * @return double minimum
     */
    public double getDefaultMin() {
        return defaultMin;
    }

    /**
     * Get default maximum standard deviation value for this parameter.
     *
     * @return double deviation max
     */
    public double getDeviationMax() {
        return deviationMax;
    }

    /**
     * Get the description of the parameter.
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get parameter type name.
     *
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Get name of parameter type and StdDev.
     *
     * @return String name of parameter and stdDev
     */
    public String getStandardDeviationName() {
        return name + "StdDev";
    }

    /**
     * Create a description for a standard deviation.
     *
     * @param name  name of variable
     * @param units units
     * @return string
     */
    public static String strBuilderForStdDev(String name, String units) {
        return name + " Standard Deviation: A deviation of " + name + " in " + units;
    }
}
