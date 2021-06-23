package nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo;

import net.sf.openrocket.simulation.SimulationStatus;
import net.sf.openrocket.simulation.exception.SimulationException;
import net.sf.openrocket.util.WorldCoordinate;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.Map;

/**
 * Class representing a world coordinate with some data about a rocket's flight path.
 */
public class LandingSite {
    private static final double EARTH_RADIUS = 6371000;

    // latitude longitude in radians
    private double latitude;
    private double longitude;

    // point relative to landing site
    private Point2D point = null;

    private Map<String, Double> parameters;
    private WindParameters windParams;

    public enum LandingColour {
        PARACHUTE_DEPLOYED(new Color(118, 120, 237), 3),
        PARACHUTE_FAILED(new Color(209, 35, 35), 4),
        LAUNCH_LOCATION(new Color(9, 3, 2), 1),
        MEAN(new Color(112, 156, 68), 2),
        SELECTED(new Color(231, 90, 124), 0);

        private Color color;
        private int depth;

        LandingColour(Color c, int depth) {
            this.color = c;
            this.depth = depth;
        }

        public Color getColor() {
            return color;
        }

        /**
         * "Depth" of the colour. landing sites with a lower depth will be drawn on top of landing sites that have a higher depth
         *
         * @return
         */
        public int getDepth() {
            return depth;
        }
    }

    /**
     * Color of the landing site to be used on scatter plot.
     */
    private LandingColour landingSiteColor;

    /**
     * Create a landing site for unusual sites (i.e. launch, mean, etc).
     *
     * @param parameters parameters used to obtain this data point
     * @param status     final status of the simulation, containing the location of the landing site
     * @throws SimulationException SimulationException can be thrown when creating a LandingSite.
     */
    public LandingSite(Map<String, Double> parameters, WindParameters windParameters, SimulationStatus status, LandingColour landingColour) {
        WorldCoordinate rocketWorldCoord = status.getRocketWorldPosition();
        latitude = rocketWorldCoord.getLatitudeRad();
        longitude = rocketWorldCoord.getLongitudeRad();
        this.parameters = parameters;
        this.windParams = windParameters;
        landingSiteColor = landingColour;
    }

    /**
     * Create a landing site from a Point2D. This does not contain a method for obtaining the latitude and longitude.
     * Useful for the mean landing location
     *
     * @param point         distance from launch
     * @param launchSite    launch
     * @param landingColour landing colour
     */
    public LandingSite(Point2D point, LandingSite launchSite, LandingColour landingColour) {
        this.point = point;
        this.parameters = Collections.emptyMap();
        this.landingSiteColor = landingColour;

        double distance = Math.sqrt(point.getX() * point.getX() + point.getY() * point.getY());
        double angle = Math.atan2(point.getX(), -point.getY());

        setLatLong(launchSite, distance, angle);
    }

    /**
     * Get a launch site of a particular distance and angle away from a reference landing site.
     *
     * @param reference Reference landing site.
     * @param distance  Distance from reference landing site.
     * @param angle     Angle from reference landing site.
     */
    public LandingSite(LandingSite reference, double distance, double angle) {
        this.point = null;
        this.landingSiteColor = null;
        this.parameters = Collections.emptyMap();
        setLatLong(reference, distance, angle);
    }

    /**
     * Get the color for this landing site, which is used on scatter plot.
     *
     * @return Landing site color as a Color object.
     */
    public Color getLandingSiteColor() {
        return landingSiteColor.getColor();
    }

    /**
     * The enum for the what kind of landing site it is.
     *
     * @return the landing site type
     */
    public LandingColour getLandingColour() {
        return landingSiteColor;
    }

    /**
     * Get latitude.
     *
     * @return
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Get longitude.
     *
     * @return
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Get parameters.
     *
     * @return The options that hold all of the launch information
     */
    public Map<String, Double> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    /**
     * Get wind parameters.
     *
     * @return The wind parameters for wind speed/direction at different altitudes.
     */
    public WindParameters getWindParameters() {
        return windParams;
    }

    /**
     * Set the position of the landing site in metres relative to the launch site.
     *
     * @param launch launch site
     */
    public void setPosition(LandingSite launch) {
        double distance = launch.distanceTo(this);
        double angle = launch.angleWith(this);

        double x = Math.sin(angle) * distance;
        double y = -Math.cos(angle) * distance;

        point = new Point2D.Double(x, y);
    }

    /**
     * Set the latitude and longitude based on a point relative to the launch site.
     */
    public void setLatLong(LandingSite reference, double distance, double angle) {
        double lat = reference.getLatitude();
        double lon = reference.getLongitude();

        latitude = Math.asin(Math.sin(lat) * Math.cos(distance / EARTH_RADIUS) + Math.cos(lat) * Math.sin(distance / EARTH_RADIUS) * Math.cos(angle));
        longitude = lon + Math.atan2(Math.sin(angle) * Math.sin(distance / EARTH_RADIUS) * Math.cos(lat),
                Math.cos(distance / EARTH_RADIUS) - Math.sin(lat) * Math.sin(latitude));
    }

    /**
     * Get the position of the landing site relative to the launch site in metres.
     *
     * @return
     */
    public Point2D getPosition() {
        if (point == null) {
            throw new NullPointerException("Position not set");
        }
        return point;
    }

    /**
     * Get the geoposition of the landing site. Useful for maps.
     *
     * @return
     */
    public GeoPosition toGeoPosition() {
        return new GeoPosition(Math.toDegrees(latitude), Math.toDegrees(longitude));
    }


    /**
     * Landing site as a string.
     *
     * @return String of the latitude and longitude of the landing site
     */
    public String toString() {
        return "Lat: " + latitude + ", Lon: " + longitude;
    }

    /**
     * Get the great circle distance between two landing sites.
     *
     * @param other another landing site to calculate the distance to
     * @return
     */
    public double distanceTo(LandingSite other) {
        return EARTH_RADIUS * radiansBetween(latitude, longitude, other.getLatitude(), other.getLongitude());
    }

    /**
     * Get the great circle distance between this site and a given coordinate.
     *
     * @param otherLatitude  Coordinate to compare distance to
     * @param otherLongitude Coordinate to compare distance to
     * @return Double meters distance
     */
    public double distanceTo(double otherLatitude, double otherLongitude) {
        return EARTH_RADIUS * radiansBetween(latitude, longitude, otherLatitude, otherLongitude);
    }


    /**
     * Calculate the angle between this landing site and the other landing site with the north direction.
     * This angle will be 0 for a point directly north, PI for a point directly south and PI/2 for directly east.
     * <p>
     * This uses the law of haversines formula to compute an angle.
     * https://en.wikipedia.org/wiki/Haversine_formula.
     * </p>
     * @param other Other landing site.
     * @return Angle as a double.
     */
    public double angleWith(LandingSite other) {
        double otherLat = other.getLatitude();
        double otherLon = other.getLongitude();

        // some arbitrary point north of this point. The distance in radians for this will always be 0.01
        double a = 0.01;
        double northLat = latitude + a;
        double northLon = longitude;

        // check for case where the northly point extends over the north pole
        if (northLat > Math.PI / 2.0) {
            northLon += Math.PI;
            northLat = (1 - northLat) * Math.PI / 2.0;
        }

        // angle between this point and the other
        double b = radiansBetween(latitude, longitude, otherLat, otherLon);
        double c = radiansBetween(northLat, northLon, otherLat, otherLon);

        // calculate hav(theta) for the angle

        double havTheta = (haversine(c) - haversine(a - b)) / (Math.sin(a) * Math.sin(b));
        double angle = arcHaversine(havTheta);

        // return negative if the other point is west of this point
        double lonDif = otherLon - longitude;
        if (Math.abs(lonDif) > Math.PI) { // case for when the angle is difference is so large it wraps around more than half the diameter
            lonDif = (Math.PI * 2.0 + otherLon) - longitude;
        }
        if (lonDif < 0 || lonDif > Math.PI) {
            return -angle;
        }

        return angle;
    }


    /**
     * Calculate the angle in radians between two points on a sphere using the inverse Haversine formula.
     * https://en.wikipedia.org/wiki/Haversine_formula.
     *
     * @param lat1 latitude for point 1
     * @param lon1 longitude for point 1
     * @param lat2 latitude for point 2
     * @param lon2 longitude for point 2
     * @return angle in radians
     */
    private static double radiansBetween(double lat1, double lon1, double lat2, double lon2) {
        // difference between latitude and longitude
        double latDif = lat2 - lat1;
        double lonDif = lon2 - lon1;

        // calculate Haversines of latitude and longitude differences
        double latHav = haversine(latDif);
        double lonHav = haversine(lonDif);

        // calculate Haversine of the central angle hav(theta)
        double havTheta = latHav + Math.cos(lat1) * Math.cos(lat2) * lonHav;

        // return inverse Haversine
        return arcHaversine(havTheta);
    }

    /**
     * Calculate the haversine of an angle.
     *
     * @param angle The angle in question.
     * @return Haversine as a double.
     */
    private static double haversine(double angle) {
        double sin = Math.sin(angle / 2.0);
        return sin * sin;
    }

    /**
     * Calculate the inverse haversine of an angle.
     *
     * @param havTheta The havTheta in question.
     * @return The inverse haversine as a double.
     */
    private static double arcHaversine(double havTheta) {
        if (havTheta <= 0) {
            return 0; // for slightly negative cases
        }

        return 2.0 * Math.atan2(Math.sqrt(havTheta), Math.sqrt(1 - havTheta));
    }

}
