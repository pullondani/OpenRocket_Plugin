import nz.ac.vuw.ecs.ormontecarlosimplugin.idealparameter.AbstractHillClimb;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Test the implementation of hill climbing and finding the ideal upwind rocket vector.
 */
public class IdealVectorTests {
    /**
     * Parabola function, takes an x and outputs a y. There is a little bit of noise to the value.
     */
    private static class ParabolaFunction extends AbstractHillClimb {
        private double noise;

        /**
         * Init with x = 0.
         *
         * @return map
         */
        private static Map<String, Double> getInitialParameters() {
            Map<String, Double> map = new HashMap<>();

            map.put("x", 0.0);

            return map;
        }

        /**
         * Set a very high initial step value.
         *
         * @return map
         */
        private static Map<String, Double> getInitialSteps() {
            Map<String, Double> map = new HashMap<>();

            map.put("x", 100000000000.0);

            return map;
        }

        /**
         * return x values.
         *
         * @param value x
         * @return map
         */
        private static Map<String, Double> values(double value) {
            Map<String, Double> values = new HashMap<>();

            values.put("x", value);

            return values;
        }

        /**
         * x should not loop.
         *
         * @return map
         */
        private static Map<String, Boolean> shouldLoop() {
            Map<String, Boolean> map = new HashMap<>();
            map.put("x", false);
            return map;
        }


        /**
         * An abstract hill climb machine learning operation that finds a minimum.
         */
        public ParabolaFunction(double min, double max, double noise) {
            super(50, getInitialParameters(), getInitialSteps(), values(min), values(max), shouldLoop());
            this.noise = noise;
        }

        @Override
        protected double doSimulation(Map<String, Double> parameters) {
            // y = (x-5)^2.

            // the minimum should be at x = 5 - where y = 0
            double vxMinus5 = (parameters.get("x") - 5);


            return vxMinus5 * vxMinus5 + (Math.random() * noise - noise / 2.0);
        }
    }

    /**
     * Sin function. This is bounded between 0 and 2PI. minimum is at 3/2 PI.
     */
    private static class SinFunction extends AbstractHillClimb {
        private double noise;

        /**
         * init x = 0.
         *
         * @return map
         */
        private static Map<String, Double> getInitialParameters() {
            Map<String, Double> map = new HashMap<>();

            map.put("x", 0.0);

            return map;
        }

        /**
         * high initial start.
         *
         * @return map
         */
        private static Map<String, Double> getInitialSteps() {
            Map<String, Double> map = new HashMap<>();

            map.put("x", 10 * Math.PI);

            return map;
        }

        /**
         * return x.
         *
         * @param value x
         * @return map
         */
        private static Map<String, Double> values(double value) {
            Map<String, Double> values = new HashMap<>();

            values.put("x", value);

            return values;
        }

        /**
         * x should loop.
         *
         * @return map
         */
        private static Map<String, Boolean> shouldLoop() {
            Map<String, Boolean> map = new HashMap<>();
            map.put("x", true);
            return map;
        }


        /**
         * An abstract hill climb machine learning operation that finds a minimum.
         */
        public SinFunction() {
            super(50, getInitialParameters(), getInitialSteps(), values(0), values(2 * Math.PI), shouldLoop());
        }

        @Override
        protected double doSimulation(Map<String, Double> parameters) {
            return Math.sin(parameters.get("x"));
        }
    }

    /**
     * A 3D parabola function. Has two inputs.
     */
    private static class Some3DFunction extends AbstractHillClimb {
        /**
         * x = 0, y = 0.
         *
         * @return map
         */
        private static Map<String, Double> getInitialParameters() {
            Map<String, Double> map = new HashMap<>();

            map.put("x", 0.0);
            map.put("y", 0.0);

            return map;
        }

        /**
         * High step values.
         *
         * @return map
         */
        private static Map<String, Double> getInitialSteps() {
            Map<String, Double> map = new HashMap<>();

            map.put("x", 100000000000.0);
            map.put("y", 100000000000.0);

            return map;
        }


        /**
         * An abstract hill climb machine learning operation that finds a minimum.
         */
        public Some3DFunction() {
            super(50, getInitialParameters(), getInitialSteps());
        }

        @Override
        protected double doSimulation(Map<String, Double> parameters) {
            double x = parameters.get("x") - 2;
            double y = parameters.get("y") - 2;

            return x * x + y * y;

        }
    }

    /**
     * Parabola hill climb.
     */
    @Test
    public void testParabolaFunctionHillClimb() {
        ParabolaFunction fun = new ParabolaFunction(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);

        Map<String, Double> parameters = fun.findIdealParameters();

        double idealX = parameters.get("x");

        assertEquals(5, idealX, 0.01);
    }


    /**
     * Parabola hill climb with noise.
     */
    @Test
    public void testParabolaFunctionHillClimbNoise() {
        ParabolaFunction fun = new ParabolaFunction(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0.01);

        Map<String, Double> parameters = fun.findIdealParameters();

        double idealX = parameters.get("x");

        assertEquals(5, idealX, 0.05);
    }

    /**
     * Parabola hill climb bounded by maximum.
     */
    @Test
    public void testParabolaFunctionHillClimbBound1() {
        ParabolaFunction fun = new ParabolaFunction(Double.NEGATIVE_INFINITY, 1, 0);

        Map<String, Double> parameters = fun.findIdealParameters();

        double idealX = parameters.get("x");

        assertEquals(1, idealX, 0.01);
    }


    /**
     * Parabola hill climb bounded by minimum.
     */
    @Test
    public void testParabolaFunctionHillClimbBound2() {
        ParabolaFunction fun = new ParabolaFunction(6, Double.POSITIVE_INFINITY, 0);

        Map<String, Double> parameters = fun.findIdealParameters();

        double idealX = parameters.get("x");

        assertEquals(6, idealX, 0.01);
    }

    /**
     * 3d parabola, 2 values.
     */
    @Test
    public void test3DParabola() {
        Some3DFunction fun = new Some3DFunction();

        Map<String, Double> parameters = fun.findIdealParameters();

        double idealX = parameters.get("x");
        double idealY = parameters.get("y");

        assertEquals(2, idealX, 0.01);
        assertEquals(2, idealY, 0.01);

    }

    /**
     * Sin function - when considering looping there is one minimum. otherwise no.
     */
    @Test
    public void testSinFunction() {
        SinFunction fun = new SinFunction();

        Map<String, Double> parameters = fun.findIdealParameters();

        double idealX = parameters.get("x");

        assertEquals(1.5 * Math.PI, idealX, 0.01);

    }

}
