package nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo;

import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter.GaussianParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * Some wind parameters per altitude.
 *
 * @author: Thomas, Naveen
 */
public class WindParameters {
    private Map<Double, GaussianParameter> windSpeed;
    private Map<Double, GaussianParameter> windDirection;
    private Double currentStep;
    private int size;

    private double speedStdDev;
    private double directionStdDev;

    /**
     * Constructor.
     *
     * @param windAltitudeMap Map of wind speeds by altitude.
     * @param windDirectionMap Map of wind directions by altitude.
     * @param windSpeedStdDev Wind speed sigma.
     * @param windDirectionStdDev Wind direction sigma.
     * @param windAltitudeStep Step between altitudes.
     */
    public WindParameters(Map<Double, Double> windAltitudeMap, Map<Double, Double> windDirectionMap, Double windSpeedStdDev, Double windDirectionStdDev,
                          Double windAltitudeStep) {
        windSpeed = new HashMap<>();
        windDirection = new HashMap<>();
        for (Double alititude : windAltitudeMap.keySet()) {
            GaussianParameter windGaussian = new GaussianParameter(windAltitudeMap.get(alititude), windSpeedStdDev);
            windSpeed.put(alititude, windGaussian);
        }
        for (Double alititude : windDirectionMap.keySet()) {
            GaussianParameter windGaussian = new GaussianParameter(windDirectionMap.get(alititude), windDirectionStdDev);
            windDirection.put(alititude, windGaussian);
        }
        currentStep = windAltitudeStep;

        size = windAltitudeMap.size();

        speedStdDev = windSpeedStdDev;
        directionStdDev = windDirectionStdDev;
    }

    public Map<Double, GaussianParameter> getWindSpeed() {
        return windSpeed;
    }

    public Map<Double, GaussianParameter> getWindDirection() {
        return windDirection;
    }

    public Double getCurrentStep() {
        return currentStep;
    }

    public int getSize() {
        return size;
    }

    public double getSpeedStdDev() {
        return speedStdDev;
    }

    public double getDirectionStdDev() {
        return directionStdDev;
    }
}
