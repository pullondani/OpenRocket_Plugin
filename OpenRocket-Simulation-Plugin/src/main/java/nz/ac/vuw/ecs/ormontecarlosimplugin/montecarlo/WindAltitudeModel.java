package nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo;

import net.sf.openrocket.models.wind.PinkNoiseWindModel;
import net.sf.openrocket.models.wind.WindModel;
import net.sf.openrocket.util.Coordinate;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter.GaussianParameter;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter.RandomParameter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Implements the wind altitude model for the simulation.
 *
 * @author: naveen
 */
public class WindAltitudeModel implements WindModel {
    private Map<Double, RandomParameter> windSpeed;
    private Map<Double, RandomParameter> windDirection;
    private Double currentStep;
    private int size;

    private double stdDev;

    private PinkNoiseWindModel windModel = new PinkNoiseWindModel(new Random().nextInt());

    /**
     * Constructor.
     *
     * @param parameters Wind params.
     */
    public WindAltitudeModel(WindParameters parameters) {
        windSpeed = new HashMap<>();
        windDirection = new HashMap<>();
        for (Double alititude : parameters.getWindSpeed().keySet()) {
            GaussianParameter windGaussian = new GaussianParameter(parameters.getWindSpeed().get(alititude).getNextValue(), parameters.getSpeedStdDev());
            windSpeed.put(alititude, windGaussian);
        }
        for (Double alititude : parameters.getWindDirection().keySet()) {
            GaussianParameter windGaussian = new GaussianParameter(parameters.getWindDirection().get(alititude).getNextValue(),
                    parameters.getDirectionStdDev());
            windDirection.put(alititude, windGaussian);
        }
        currentStep = parameters.getCurrentStep();
        size = parameters.getSize();

        stdDev = parameters.getSpeedStdDev();
    }

    /**
     * Interpolates the wind speed based on the current altitude.
     *
     * @param altitude Altitude of wind speed.
     * @return speed Speed as double.
     * @author naveen
     */
    public double getWindSpeed(Double altitude) {
        //if less than 0 return the minimum value inside the map
        if (altitude <= 0) {
            return windSpeed.get(0.0).getNextValue();
        } else if (altitude >= (size - 1) * currentStep) { //otherwise if the altitude is the max, return the max value inside the map
            return windSpeed.get((size - 1) * currentStep).getNextValue();
        } else { // the size is greater than 2 interpolate the values to get the speed based on the altitude.
            double lowerBound = Math.floor(altitude / currentStep);
            double upperBound = Math.floor(altitude / currentStep) + 1;
            double t = altitude / currentStep - lowerBound;

            double speedLowerBound = windSpeed.get(lowerBound * currentStep).getNextValue();
            double speedUpperBound = windSpeed.get(upperBound * currentStep).getNextValue();

            return (1 - t) * speedLowerBound + t * speedUpperBound;
        }

    }

    /**
     * Interpolates the wind speed based on the current altitude.
     *
     * @param altitude Altitude of wind direction.
     * @return Wind direction as double.
     */
    public double getWindDirection(Double altitude) {
        //if less than 0 return the minimum value inside the map
        if (altitude <= 0) {
            return windDirection.get(0.0).getNextValue();
        } else if (altitude >= (size - 1) * currentStep) { //otherwise if the altitude is the max, return the max value inside the map
            return windDirection.get((size - 1) * currentStep).getNextValue();
        } else {
            double lowerBound = Math.floor(altitude / currentStep);
            double upperBound = Math.floor(altitude / currentStep) + 1;
            double t = altitude / currentStep - lowerBound;

            double speedLowerBound = windDirection.get(lowerBound * currentStep).getNextValue();
            double speedUpperBound = windDirection.get(upperBound * currentStep).getNextValue();

            if (speedUpperBound - speedLowerBound < 180) {
                return (1 - t) * speedLowerBound + t * speedUpperBound;
            } else {
                speedUpperBound -= 360;
                Double direction = (1 - t) * speedLowerBound + t * speedUpperBound;

                if (direction < 0) {
                    return direction + 360;
                }

                return direction;
            }
        }

    }

    @Override
    public Coordinate getWindVelocity(double time, double altitude) {

        windModel.setAverage(getWindSpeed(altitude / 1000.0));
        windModel.setDirection(Math.toRadians(getWindDirection(altitude / 1000.0)));
        windModel.setStandardDeviation(stdDev);

        return windModel.getWindVelocity(time, altitude);
    }

    @Override
    public int getModID() {
        return windModel.getModID();
    }
}
