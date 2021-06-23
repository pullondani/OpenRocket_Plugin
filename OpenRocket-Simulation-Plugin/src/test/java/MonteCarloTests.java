
import net.sf.openrocket.document.Simulation;
import net.sf.openrocket.simulation.SimulationOptions;
import net.sf.openrocket.startup.SwingStartup;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.LandingSite;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.MonteCarloResult;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.OpenRocketMonteCarloSimulation;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.WindAltitudeModel;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.WindParameters;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter.ParameterType;
import nz.ac.vuw.ecs.ormontecarlosimplugin.extend.OrSimulationExtension;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Tests for checking simulation parameters and their affects on the result of the simulation.
 */
public class MonteCarloTests {
    /**
     * Initialise the logging.
     */
    @Before
    public void removeLogging() {
        SwingStartup.initializeLogging();
    }

    /**
     * Testing Rocket launched straight up lands around launch area.
     */
    @Test
    public void monteCarloTest1() {

        // Maximum difference in latitude and longitude between launch site and landing site.
        double maximumPositionDifference = 30; // meters

        // Create extension
        OrSimulationExtension simExtension = new OrSimulationExtension();
        // Create simulation
        Simulation simulation = TestUtil.testSetup(simExtension);

        // Edit launch variable
        simExtension.setNumSims(10);
        simExtension.setLaunchAngleStdDev(0);
        SimulationOptions options = simulation.getOptions();
        //set options, then:
        options.setWindSpeedDeviation(0);
        options.setWindSpeedAverage(0);
        options.setLaunchRodAngle(0);


        double launchLongitude = 0;
        double launchLatitude = 0;
        options.setLaunchLatitude(launchLatitude);
        options.setLaunchLongitude(launchLongitude);

        // Create the monte carlo simulation
        OpenRocketMonteCarloSimulation monteCarloSimulation = TestUtil.createMonteCarloSimulation(simulation, simExtension);

        // Test landing s1.3390311687988836E7ite is same as launch site
        Collection<LandingSite> landingSites = monteCarloSimulation.monteCarloSim();

        for (LandingSite landingSite : landingSites) {
            //System.out.println(landingSite.getLatitude() + ", " + landingSite.getLongitude());
            //System.out.println(landingSite.distanceTo(Math.toRadians(launchLatitude), Math.toRadians(launchLongitude)));
            assert Math.abs(landingSite.distanceTo(Math.toRadians(launchLatitude), Math.toRadians(launchLongitude))) < maximumPositionDifference;

            //assert Math.abs(launchLatitude - landingSite.getLatitude()) < maximumPositionDifference;
            //assert Math.abs(launchLongitude - landingSite.getLongitude()) < maximumPositionDifference;
        }
    }

    /**
     * Testing that the rocket fires east.
     */
    @Test
    public void monteCarloTest2() {
        // Create extension
        OrSimulationExtension orSimulationExtension = new OrSimulationExtension();
        orSimulationExtension.setUsePid(false);

        // Create simulation
        Simulation simulation = TestUtil.testSetup(orSimulationExtension);

        // Edit launch variable
        orSimulationExtension.setNumSims(10);
        orSimulationExtension.setLaunchAngleStdDev(0);
        SimulationOptions options = simulation.getOptions();
        //set options, then:
        options.setWindSpeedDeviation(0);
        options.setWindSpeedAverage(0);
        options.setLaunchRodAngle(Math.toRadians(45));

        double launchLongitude = 0;
        double launchLatitude = 0;

        options.setLaunchLatitude(launchLatitude);
        options.setLaunchLongitude(launchLongitude);

        // Create the monte carlo simulation
        OpenRocketMonteCarloSimulation monteCarloSimulation = TestUtil.createMonteCarloSimulation(simulation, orSimulationExtension);

        // Test landing site is same as launch site
        Collection<LandingSite> landingSites = monteCarloSimulation.monteCarloSim();

        for (LandingSite landingSite : landingSites) {

            assert launchLongitude < landingSite.getLongitude();
        }
    }

    /**
     * Testing that the rocket fires west.
     */
    @Test
    public void monteCarloTest3() {
        double maximumPositionDifference = 20; // meters
        double launchLongitude = 0;
        double launchLatitude = 0;

        // Create extension
        OrSimulationExtension orSimulationExtension = new OrSimulationExtension();
        // Create simulation
        Simulation simulation = TestUtil.testSetup(orSimulationExtension);
        SimulationOptions options = simulation.getOptions();
        //set options
        options.setWindSpeedDeviation(0);
        options.setWindSpeedAverage(0);
        options.setLaunchRodAngle(Math.toRadians(-45));
        options.setLaunchLatitude(launchLatitude);
        options.setLaunchLongitude(launchLongitude);

        // Edit launch variable
        orSimulationExtension.setNumSims(10);
        orSimulationExtension.setLaunchAngleStdDev(0);


        // Create the monte carlo simulation
        OpenRocketMonteCarloSimulation monteCarloSimulation = TestUtil.createMonteCarloSimulation(simulation, orSimulationExtension);

        // Test landing site is same as launch site
        Collection<LandingSite> landingSites = monteCarloSimulation.monteCarloSim();

        for (LandingSite landingSite : landingSites) {
            //System.out.println(launchLongitude - landingSite.getLongitude());
            //System.out.println(landingSite.distanceTo(Math.toRadians(launchLatitude), Math.toRadians(launchLongitude)) + " metres");

            assert launchLongitude > landingSite.getLongitude();

        }
    }

    /**
     * Test the standard deviation of the launch angle. Higher standard deviation should give a greater spread.
     */
    @Test
    public void standardDeviationTest1() {
        Map<String, Double> parameters = new HashMap<>();

        parameters.put(ParameterType.SIM_NUMBER.getName(), 200.0);
        parameters.put(ParameterType.LAUNCH_ANGLE.getStandardDeviationName(), 10.0);

        // lower standard deviation
        Collection<LandingSite> landingSitesA = TestUtil.test(parameters);


        // higher standard deviation
        parameters.put(ParameterType.LAUNCH_ANGLE.getStandardDeviationName(), 40.0);
        Collection<LandingSite> landingSitesB = TestUtil.test(parameters);

        MonteCarloResult resultA = new MonteCarloResult(landingSitesA, TestUtil.getLandingSite(0, 0), null);
        MonteCarloResult resultB = new MonteCarloResult(landingSitesB, TestUtil.getLandingSite(0, 0), null);

        // check the spread is higher for result B
        assertTrue(resultB.getStandardDevY() + resultB.getStandardDevX() > resultA.getStandardDevY() + resultA.getStandardDevX());

    }


    /**
     * Run a test only varying the parameters entered into this method.
     * This is deprecated because the wind speed and direction is now calculated per altitude.
     *
     * @param windDir         - The wind direction in degrees.
     * @param windSpeed       - The wind speed.
     * @param launchLongitude - The launch Longitude.
     * @param launchLatitude  - The launch Latitude.
     * @param numOfSims       - The number of simulations to be performed.
     * @return -
     */
    @Deprecated
    private Collection<LandingSite> windTest(double windDir, double windSpeed,
                                             double launchLongitude, double launchLatitude, int numOfSims) {
        Map<String, Double> parameters = new HashMap<>();
        parameters.put(ParameterType.WIND_DIRECTION.getName(), windDir);
        parameters.put(ParameterType.WIND_SPEED.getName(), windSpeed);
        parameters.put(ParameterType.LAUNCH_LONG.getName(), launchLongitude);
        parameters.put(ParameterType.LAUNCH_LAT.getName(), launchLatitude);
        parameters.put(ParameterType.SIM_NUMBER.getName(), (double) numOfSims);

        return TestUtil.test(parameters);
    }

    /**
     * Tests the wind altitude model based on various altitudes.
     */
    @Test
    public void windSpeedAltitudeTest() {
        Map<Double, Double> windMap = new HashMap<>();
        windMap.put(0.0, 0.0);
        windMap.put(10.0, 15.0);
        windMap.put(20.0, 40.0);

        Map<Double, Double> windDirection = new HashMap<>();
        windDirection.put(0.0, 0.0);
        windDirection.put(10.0, 15.0);
        windDirection.put(20.0, 40.0);


        WindAltitudeModel test = new WindAltitudeModel(new WindParameters(windMap, windDirection, 0.0, 0.0, 10.0));
        assertEquals(test.getWindSpeed(0.0), 0.0, 0.00001);
        assertEquals(test.getWindSpeed(5.0), 7.5, 0.00001);
        assertEquals(test.getWindSpeed(20.0), 40.0, 0.00001);
        assertEquals(test.getWindSpeed(-1.0), 0.0, 0.00001);
        assertEquals(test.getWindSpeed(40.0), 40.0, 0.00001);

    }

    /**
     * Tests the wind altitude model based on various altitudes.
     */
    @Test
    public void windDirectionAltitudeTest() {
        Map<Double, Double> windMap = new HashMap<>();
        windMap.put(0.0, 0.0);
        windMap.put(10.0, 15.0);
        windMap.put(20.0, 40.0);

        Map<Double, Double> windDirection = new HashMap<>();
        windDirection.put(0.0, 0.0);
        windDirection.put(10.0, 50.0);
        windDirection.put(20.0, 330.0);


        WindAltitudeModel test = new WindAltitudeModel(new WindParameters(windMap, windDirection, 0.0, 0.0, 10.0));
        assertEquals(test.getWindDirection(0.0), 0.0, 0.00001);
        assertEquals(test.getWindDirection(5.0), 25.0, 0.00001);
        assertEquals(test.getWindDirection(20.0), 330.0, 0.00001);
        assertEquals(test.getWindDirection(15.0), 10.0, 0.00001);
        assertEquals(test.getWindDirection(17.5), 350.0, 0.00001);
        assertEquals(test.getWindDirection(-1.0), 0.0, 0.00001);
        assertEquals(test.getWindDirection(40.0), 330.0, 0.00001);

    }


    //TODO testing things other than wind speed and direction, like temperature
    // when the pressure is lower the rocket should travel further


    /**
     * Test a rocket with motor performance of 100% with one with 80%. The one with 100% should travel further.
     *
     * @author Thomas
     */
    @Test
    public void motorPerformanceTest1() {
        int numSims = 10;

        List<LandingSite> highSpeed = new ArrayList<>(motorPerformanceTest(100, numSims, 45));
        List<LandingSite> lowSpeed = new ArrayList<>(motorPerformanceTest(80, numSims, 45));


        for (int i = 0; i < numSims; i++) {
            assertTrue(highSpeed.get(i).getLatitude() > lowSpeed.get(i).getLatitude());
        }
    }

    /**
     * Higher motor performance should result in the rocket going further.
     *
     * @param motorPerformance motor performance value
     * @param numOfSims        number of landing sites to generate
     * @param launchAngle      launch angle
     * @return collection of landing sites
     */
    private Collection<LandingSite> motorPerformanceTest(double motorPerformance, int numOfSims, double launchAngle) {
        Map<String, Double> parameters = new HashMap<>();
        parameters.put(ParameterType.SIM_NUMBER.getName(), (double) numOfSims);
        parameters.put(ParameterType.LAUNCH_ANGLE.getName(), launchAngle);
        parameters.put(ParameterType.LAUNCH_DIRECTION.getName(), 90.0);
        parameters.put(ParameterType.MOTOR_PERFORMANCE.getName(), motorPerformance);
        return TestUtil.test(parameters);
    }

}
