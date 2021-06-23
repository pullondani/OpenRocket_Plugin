import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.LandingSite;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.MonteCarloResult;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.OpenRocketMonteCarloParameters;
import org.junit.Test;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Testing for drawing different items on the output GUI.
 */
public class OutputGuiTests {

    /**
     * Test the distance between two points.
     */
    @Test
    public void testProjectionDistance() {
        LandingSite site1 = TestUtil.getLandingSite(0, 0);
        LandingSite site2 = TestUtil.getLandingSite(10, 0);

        assertEquals(0, (int) site1.distanceTo(site1));
        assertEquals(1111949, (int) site1.distanceTo(site2));
        assertEquals(314402, (int) TestUtil.getLandingSite(1, 2).distanceTo(TestUtil.getLandingSite(3, 4)));
    }

    /**
     * Test the angle between two points.
     */
    @Test
    public void testProjectionAngle() {
        LandingSite centre = TestUtil.getLandingSite(0, 0);
        LandingSite north = TestUtil.getLandingSite(1, 0);

        assertTrue(Math.abs(centre.angleWith(north)) < 0.0001); // north angle should be 0

        LandingSite south = TestUtil.getLandingSite(-1, 0);
        assertTrue(Math.abs(centre.angleWith(south) - Math.PI) < 0.0001
                || Math.abs(centre.angleWith(south) + Math.PI) < 0.0001); // south angle should be PI (or -PI)

        LandingSite east = TestUtil.getLandingSite(0, 1);
        assertTrue(Math.abs(centre.angleWith(east) - Math.PI / 2.0) < 0.0001); // east angle should be PI/2

        LandingSite west = TestUtil.getLandingSite(0, -1);
        assertTrue(Math.abs(centre.angleWith(west) + Math.PI / 2.0) < 0.0001); // west angle should be -PI/2

        LandingSite westDateLine = TestUtil.getLandingSite(0, 179);
        LandingSite eastDateLine = TestUtil.getLandingSite(0, -179);

        assertTrue(westDateLine.angleWith(eastDateLine) > 0);  // the point east of the international dateline should have an east angle
        assertTrue(eastDateLine.angleWith(westDateLine) < 0);  // the point east of the international dateline should have an west angle
    }

    /**
     * Test calculation of the mean landing site.
     */
    @Test
    public void testLaunchMean()  {

        LandingSite north = TestUtil.getLandingSite(1, 0);
        LandingSite south = TestUtil.getLandingSite(-1, 0);
        LandingSite east = TestUtil.getLandingSite(0, 1);
        LandingSite west = TestUtil.getLandingSite(0, -1);
        LandingSite launchSite = TestUtil.getLandingSite(0, 0);
        Collection<LandingSite> landingSites = Arrays.asList(north, south, east, west);
        OpenRocketMonteCarloParameters parameters = mock(OpenRocketMonteCarloParameters.class);

        MonteCarloResult test = new MonteCarloResult(landingSites, launchSite, parameters);
        assertEquals((int) test.getMeanLandingSite().getPosition().getX(), 0);
        assertEquals((int) test.getMeanLandingSite().getPosition().getY(), 0);
    }

    /**
     * Test calculation for the standard deviation.
     */
    @Test
    public void testLaunchStandard() {
        Random random = new Random();
        random.setSeed(0);
        Collection<LandingSite> landingSites = new ArrayList<>();

        for (int i = 0; i < 500; i++) {
            LandingSite site = TestUtil.getLandingSite(random.nextGaussian(), random.nextGaussian());
            landingSites.add(site);
        }
        LandingSite launchSite = TestUtil.getLandingSite(0, 0);

        OpenRocketMonteCarloParameters parameters = mock(OpenRocketMonteCarloParameters.class);

        MonteCarloResult test = new MonteCarloResult(landingSites, launchSite, parameters);

        assertEquals(test.getStandardDevX(), 111000, 10000);
        assertEquals(test.getStandardDevY(), 111000, 10000);
    }

    /**
     * Test calculation for a rotated standard deviation.
     */
    @Test
    public void testLaunchStandardRotated()  {
        Random random = new Random();
        random.setSeed(0);
        Collection<LandingSite> landingSites = new ArrayList<>();

        double angle = Math.PI / 4;


        for (int i = 0; i < 500; i++) {
            double x = random.nextGaussian() * 0.1;
            double y = random.nextGaussian() * 1;

            double rotateX = Math.cos(angle) * x - Math.sin(angle) * y;
            double rotateY = Math.sin(angle) * x + Math.cos(angle) * y;

            LandingSite site = TestUtil.getLandingSite(rotateX, rotateY);
            landingSites.add(site);
        }
        LandingSite launchSite = TestUtil.getLandingSite(0, 0);

        OpenRocketMonteCarloParameters parameters = mock(OpenRocketMonteCarloParameters.class);

        MonteCarloResult test = new MonteCarloResult(landingSites, launchSite, parameters);

        double x = test.getStandardDevX();
        double y = test.getStandardDevY();
        double a = test.getEllipseAngle();

        assertEquals(11100, x, 10000);
        assertEquals(111000, y, 1000);
        assertEquals(angle, a, 0.01);


    }

    /**
     * Test calculation for a rotated standard deviation (again - rotated).
     */
    @Test
    public void testLaunchStandardRotated2() {
        Random random = new Random();
        random.setSeed(0);
        Collection<LandingSite> landingSites = new ArrayList<>();

        double angle = -Math.PI / 4;


        for (int i = 0; i < 500; i++) {
            double x = random.nextGaussian() * 0.1;
            double y = random.nextGaussian() * 1;

            double rotateX = Math.cos(angle) * x - Math.sin(angle) * y;
            double rotateY = Math.sin(angle) * x + Math.cos(angle) * y;

            LandingSite site = TestUtil.getLandingSite(rotateX, rotateY);
            landingSites.add(site);
        }
        LandingSite launchSite = TestUtil.getLandingSite(0, 0);

        OpenRocketMonteCarloParameters parameters = mock(OpenRocketMonteCarloParameters.class);

        MonteCarloResult test = new MonteCarloResult(landingSites, launchSite, parameters);

        double x = test.getStandardDevX();
        double y = test.getStandardDevY();
        double a = test.getEllipseAngle();

        assertEquals(11100, x, 10000);
        assertEquals(111000, y, 1000);
        assertEquals(Math.PI - Math.PI / 4, a, 0.01); // 180 degree rotated


    }

    /**
     * Test conversion of angle+distance to latitude longitude.
     */
    @Test
    public void testDistanceToLatLong() {
        LandingSite launch = TestUtil.getLandingSite(0, 0);

        LandingSite east1km = new LandingSite(new Point(1000, 0), launch, LandingSite.LandingColour.MEAN);
        assertEquals(0, Math.toDegrees(east1km.getLatitude()), 0.001);
        assertEquals(0.008889, Math.toDegrees(east1km.getLongitude()), 0.001);

        LandingSite west1km = new LandingSite(new Point(-1000, 0), launch, LandingSite.LandingColour.MEAN);
        assertEquals(0, Math.toDegrees(west1km.getLatitude()), 0.001);
        assertEquals(-0.008889, Math.toDegrees(west1km.getLongitude()), 0.001);

        LandingSite north1km = new LandingSite(new Point(0, -1000), launch, LandingSite.LandingColour.MEAN);
        assertEquals(0.008889, Math.toDegrees(north1km.getLatitude()), 0.001);
        assertEquals(0, Math.toDegrees(north1km.getLongitude()), 0.001);

        LandingSite south1km = new LandingSite(new Point(0, 1000), launch, LandingSite.LandingColour.MEAN);
        assertEquals(-0.008889, Math.toDegrees(south1km.getLatitude()), 0.001);
        assertEquals(0, Math.toDegrees(south1km.getLongitude()), 0.001);


        LandingSite east1km2 = new LandingSite(launch, 1000, Math.PI / 2.0);
        assertEquals(0, Math.toDegrees(east1km2.getLatitude()), 0.001);
        assertEquals(0.008889, Math.toDegrees(east1km2.getLongitude()), 0.001);

        LandingSite west1km2 = new LandingSite(launch, 1000, -Math.PI / 2.0);
        assertEquals(0, Math.toDegrees(west1km2.getLatitude()), 0.001);
        assertEquals(-0.008889, Math.toDegrees(west1km2.getLongitude()), 0.001);

        LandingSite north1km2 = new LandingSite(launch, 1000, 0);
        assertEquals(0.008889, Math.toDegrees(north1km2.getLatitude()), 0.001);
        assertEquals(0, Math.toDegrees(north1km2.getLongitude()), 0.001);

        LandingSite south1km2 = new LandingSite(launch, 1000, Math.PI);
        assertEquals(-0.008889, Math.toDegrees(south1km2.getLatitude()), 0.001);
        assertEquals(0, Math.toDegrees(south1km2.getLongitude()), 0.001);
    }

    /**
     * Test the distance to method.
     */
    @Test
    public void testLatLongToDistance() {
        LandingSite launch = TestUtil.getLandingSite(45, 0);
        LandingSite east1km = TestUtil.getLandingSite(45, 1);
        east1km.setPosition(launch);

        Point2D point = east1km.getPosition();

        assertEquals(78630, launch.distanceTo(east1km), 1000);
        assertEquals(78630, Math.sqrt(point.getX() * point.getX() + point.getY() + point.getY()), 1000);

    }


}

