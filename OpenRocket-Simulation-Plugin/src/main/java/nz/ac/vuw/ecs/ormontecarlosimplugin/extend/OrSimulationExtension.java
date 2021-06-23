package nz.ac.vuw.ecs.ormontecarlosimplugin.extend;

import java.util.HashMap;
import java.util.Map;
import net.sf.openrocket.simulation.SimulationConditions;
import net.sf.openrocket.simulation.extension.AbstractSimulationExtension;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter.ParameterType;


/**
 * This is the class responsible for the extension.
 */
public class OrSimulationExtension extends AbstractSimulationExtension {

    private final Map<Double, Double> windSpeedAltitudes = defaultWindSpeedAltitudes();
    private final Map<Double, Double> windDirectionAltitudes = defaultWindDirectionAltitudes();

    /**
     * Default hash map for wind speed altitudes.
     *
     * @return Map for wind speeds
     */
    private static Map<Double, Double> defaultWindSpeedAltitudes() {
        Map<Double, Double> map = new HashMap<>();

        double altitude = 0;
        for (int i = 0; i < (int) ParameterType.WIND_ALTITUDE_VALUES.getDefaultMean(); i++) {
            map.put(altitude, ParameterType.WIND_SPEED.getDefaultMean());

            altitude += ParameterType.WIND_ALTITUDE_STEP.getDefaultMean();
        }

        return map;
    }

    /**
     * Default hash map for wind direction altitudes.
     *
     * @return Map for wind directions
     */
    private static Map<Double, Double> defaultWindDirectionAltitudes() {
        Map<Double, Double> map = new HashMap<>();

        double altitude = 0;
        for (int i = 0; i < (int) ParameterType.WIND_ALTITUDE_VALUES.getDefaultMean(); i++) {
            map.put(altitude, ParameterType.WIND_DIRECTION.getDefaultMean());

            altitude += ParameterType.WIND_ALTITUDE_STEP.getDefaultMean();
        }

        return map;
    }

    @Override
    public void initialize(SimulationConditions conditions) {

        // listens for the start of the simulation, then starts a monte carlo simulation from there.
        conditions.getSimulationListenerList().add(new PluginListener(this));
    }

    /**
     * Get launch angles standard deviation.
     *
     * @return double angle deviation
     */
    public double getLaunchAngleStdDev() {
        return config.getDouble(ParameterType.LAUNCH_ANGLE.getStandardDeviationName(), ParameterType.LAUNCH_ANGLE.getDefaultDeviation());
    }

    /**
     * Set the launch angles standard deviation.
     *
     * @param launchAngleStandardDeviation double new deviation
     */
    public void setLaunchAngleStdDev(double launchAngleStandardDeviation) {
        config.put(ParameterType.LAUNCH_ANGLE.getStandardDeviationName(), launchAngleStandardDeviation);
        fireChangeEvent();
    }

    /**
     * Gets the Launch Direction standard deviation value.
     *
     * @return The Launch Direction standard deviation as a double.
     * @author Logan, Naveen, Lily
     */
    public double getLaunchDirectionStdDev() {
        return config.getDouble(ParameterType.LAUNCH_DIRECTION.getStandardDeviationName(), ParameterType.LAUNCH_DIRECTION.getDefaultDeviation());
    }

    /**
     * Sets the Launch Direction standard deviation value.
     *
     * @param launchDirectionStdDev The new standard deviation value as a double.
     * @author Logan, Naveen, Lily
     */
    public void setLaunchDirectionStdDev(double launchDirectionStdDev) {
        config.put(ParameterType.LAUNCH_DIRECTION.getStandardDeviationName(), launchDirectionStdDev);
        fireChangeEvent();
    }

    /**
     * Gets the Wind Direction standard deviation value.
     *
     * @return The Wind Direction standard deviation as a double.
     * @author Logan, Naveen, Lily
     */
    public double getWindDirectionStdDev() {
        return config.getDouble(ParameterType.WIND_DIRECTION.getStandardDeviationName(), ParameterType.WIND_DIRECTION.getDefaultDeviation());
    }

    /**
     * Sets the Wind Direction standard deviation value.
     *
     * @param windDirectionStdDev The new standard deviation value as a double.
     * @author Logan, Naveen, Lily
     */
    public void setWindDirectionStdDev(double windDirectionStdDev) {
        config.put(ParameterType.WIND_DIRECTION.getStandardDeviationName(), windDirectionStdDev);
        fireChangeEvent();
    }

    /**
     * Gets the Pressure standard deviation value.
     *
     * @return The Pressure standard deviation value as a double.
     * @author Logan, Lily
     */
    public double getPressureStdDev() {
        return config.getDouble(ParameterType.PRESSURE.getStandardDeviationName(), ParameterType.PRESSURE.getDefaultDeviation());
    }

    /**
     * Sets the Pressure standard deviation value.
     *
     * @param pressureStdDev The new standard deviation value as a double.
     * @author Logan, Lily
     */
    public void setPressureStdDev(double pressureStdDev) {
        config.put(ParameterType.PRESSURE.getStandardDeviationName(), pressureStdDev);
        fireChangeEvent();
    }

    /**
     * Gets the Temperature standard deviation value.
     *
     * @return The Temperature standard deviation value as a double.
     * @author Logan, Lily
     */
    public double getTemperatureStdDev() {
        return config.getDouble(ParameterType.TEMPERATURE.getStandardDeviationName(), ParameterType.TEMPERATURE.getDefaultDeviation());
    }

    /**
     * Sets the Temperature standard deviation value.
     *
     * @param temperatureStdDev The new standard deviation value as a double.
     * @author Logan, Lily
     */
    public void setTemperatureStdDev(double temperatureStdDev) {
        config.put(ParameterType.TEMPERATURE.getStandardDeviationName(), temperatureStdDev);
        fireChangeEvent();
    }

    /**
     * Gets the Time To Open Parachute standard deviation value.
     *
     * @return The Time To Open Parachute standard deviation value as a double.
     * @author Logan, Lily
     */
    public double getTimeToOpenParachuteStdDev() {
        return config.getDouble(ParameterType.PARACHUTE_TIME.getStandardDeviationName(), ParameterType.PARACHUTE_TIME.getDefaultDeviation());
    }

    /**
     * Sets the Time To Open Parachute standard deviation value.
     *
     * @param timeStdDev The new standard deviation value as a double.
     * @author Logan, Lily
     */
    public void setTimeToOpenParachuteStdDev(double timeStdDev) {
        config.put(ParameterType.PARACHUTE_TIME.getStandardDeviationName(), timeStdDev);
        fireChangeEvent();
    }

    /**
     * Gets the Motor Performance mean value.
     *
     * @return The Motor Performance mean value as a double.
     * @author Logan, Lily
     */
    public double getMotorPerformanceMean() {
        return config.getDouble(ParameterType.MOTOR_PERFORMANCE.getName(), ParameterType.MOTOR_PERFORMANCE.getDefaultMean());
    }

    /**
     * Sets the Motor Performance mean value.
     *
     * @param motorPerformanceMean The new mean value as a double.
     * @author Logan, Lily
     */
    public void setMotorPerformanceMean(double motorPerformanceMean) {
        config.put(ParameterType.MOTOR_PERFORMANCE.getName(), motorPerformanceMean);
        fireChangeEvent();
    }

    /**
     * Gets the Motor Performance standard deviation value.
     *
     * @return The Motor Performance standard deviation value as a double.
     * @author Logan, Lily
     */
    public double getMotorPerformanceStdDev() {
        return config.getDouble(ParameterType.MOTOR_PERFORMANCE.getStandardDeviationName(), ParameterType.MOTOR_PERFORMANCE.getDefaultDeviation());
    }

    /**
     * Sets the Motor Performance standard deviation value.
     *
     * @param motorPerformanceStdDev The new standard deviation value as a double.
     * @author Logan, Lily
     */
    public void setMotorPerformanceStdDev(double motorPerformanceStdDev) {
        config.put(ParameterType.MOTOR_PERFORMANCE.getStandardDeviationName(), motorPerformanceStdDev);
        fireChangeEvent();
    }

    /**
     * Sets the Motor Direction standard deviation value.
     *
     * @return The motor direction std dev
     */
    public double getMotorDirectionStdDev() {
        return config.getDouble(ParameterType.MOTOR_DIRECTION.getStandardDeviationName(), ParameterType.MOTOR_DIRECTION.getDefaultDeviation());
    }

    /**
     * Get maximum launch angle.
     *
     * @return The maximum launch angle
     */
    public double getMaxAngle() {
        return config.getDouble(ParameterType.MAX_ANGLE.getName(), ParameterType.MAX_ANGLE.getDefaultMean());
    }

    /**
     * Get number of simulations.
     * @return number of simulations
     */
    public int getNumSims() {
        return config.getInt(ParameterType.SIM_NUMBER.getName(), (int) ParameterType.SIM_NUMBER.getDefaultMean());
    }

    /**
     * Set number of simulations.
     * @param numSimulations number of simulations
     */
    public void setNumSims(int numSimulations) {
        config.put(ParameterType.SIM_NUMBER.getName(), numSimulations);
        fireChangeEvent();
    }


    /**
     * Get number of wind altitudes to specify.
     * @return number of wind altitudes
     */
    public int getWindAltitudeValues() {
        return config.getInt(ParameterType.WIND_ALTITUDE_VALUES.getName(), (int) ParameterType.WIND_ALTITUDE_VALUES.getDefaultMean());
    }

    public void setWindAltitudeValues(int windAltitudeValues) {
        config.put(ParameterType.WIND_ALTITUDE_VALUES.getName(), windAltitudeValues);
        fireChangeEvent();
    }

    public double getWindAltitudeStep() {
        return config.getDouble(ParameterType.WIND_ALTITUDE_STEP.getName(), ParameterType.WIND_ALTITUDE_STEP.getDefaultMean());
    }

    public void setWindAltitudeStep(double windAltitudeStep) {
        config.put(ParameterType.WIND_ALTITUDE_STEP.getName(), windAltitudeStep);
        fireChangeEvent();
    }

    public boolean getIdealLaunch() {
        return config.getBoolean("useIdealLaunch", false);
    }


    public void setIdealLaunch(boolean idealLaunch) {
        config.put("useIdealLaunch", idealLaunch);
        fireChangeEvent();
    }

    public boolean getUsePid() {
        return config.getBoolean("usePID", true);
    }


    public void setUsePid(boolean b) {
        config.put("usePID", b);
        fireChangeEvent();
    }

    public double getP() {
        return config.getDouble(ParameterType.P.getName(), ParameterType.P.getDefaultMean());
    }

    public void setP(double p) {
        config.put(ParameterType.P.getName(), p);
        fireChangeEvent();
    }

    public double getI() {
        return config.getDouble(ParameterType.I.getName(), ParameterType.I.getDefaultMean());
    }

    public void setI(double i) {
        config.put(ParameterType.I.getName(), i);
        fireChangeEvent();
    }

    public double getD() {
        return config.getDouble(ParameterType.D.getName(), ParameterType.D.getDefaultMean());
    }

    public void setD(double d) {
        config.put(ParameterType.D.getName(), d);
        fireChangeEvent();
    }

    public double getTargetAngleX() {
        return config.getDouble(ParameterType.TARGET_ANGLE_X.getName(), ParameterType.TARGET_ANGLE_X.getDefaultMean());
    }

    public void setTargetAngleX(double targetAngle) {
        config.put(ParameterType.TARGET_ANGLE_X.getName(), targetAngle);
        fireChangeEvent();
    }

    public double getTargetAngleY() {
        return config.getDouble(ParameterType.TARGET_ANGLE_Y.getName(), ParameterType.TARGET_ANGLE_Y.getDefaultMean());
    }

    public void setTargetAngleY(double targetAngle) {
        config.put(ParameterType.TARGET_ANGLE_Y.getName(), targetAngle);
        fireChangeEvent();
    }

    public double getParachuteFailRate() {
        return config.getDouble(ParameterType.PARACHUTE_FAIL.getName(), ParameterType.PARACHUTE_FAIL.getDefaultMean());
    }

    public void setParachuteFailRate(double failRate) {
        config.put(ParameterType.PARACHUTE_FAIL.getName(), failRate);
        fireChangeEvent();
    }


    public Map<Double, Double> getWindSpeedAltitudes() {
        return windSpeedAltitudes;
    }

    public Map<Double, Double> getWindDirectionAltitudes() {
        return windDirectionAltitudes;
    }
}
