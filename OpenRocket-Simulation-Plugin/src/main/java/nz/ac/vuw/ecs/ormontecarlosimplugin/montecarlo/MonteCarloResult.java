package nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo;

import java.awt.geom.Point2D;
import java.util.Collection;

/**
 * Contains details about the result of the monte carlo simulation.
 */
public class MonteCarloResult {

    private Collection<LandingSite> landingSites;
    private LandingSite launchSite;
    private OpenRocketMonteCarloParameters parameters;
    private LandingSite meanLandingSite;

    // the ellipse here is referring to the standard deviation ellipse
    private double ellipseAngle;

    // standard
    private double standardDevX;
    private double standardDevY;

    /**
     * Create new monte carlo result.
     *
     * @param landingSites list of landing sites obtained from simulation
     * @param launchSite launch site
     * @param parameters parameters used
     */
    public MonteCarloResult(Collection<LandingSite> landingSites, LandingSite launchSite, OpenRocketMonteCarloParameters parameters) {
        this.parameters = parameters;
        this.landingSites = landingSites;
        this.launchSite = launchSite;
        launchSite.setPosition(launchSite);

        for (LandingSite landingSite : landingSites) {
            landingSite.setPosition(launchSite);
        }

        calculateMean(landingSites);

        calculateStandardDeviationEllipse(meanLandingSite.getPosition(), landingSites);
    }

    /**
     * Calculate the mean position of landing sites who's parachutes deployed.
     *
     * @param landingSites landing sites
     * @return the mean
     */
    private void calculateMean(Collection<LandingSite> landingSites) {
        double x = 0;
        double y = 0;
        double numSites = 0;
        for (LandingSite landingSite : landingSites) {
            if (landingSite.getLandingColour() == LandingSite.LandingColour.PARACHUTE_DEPLOYED) {
                x += landingSite.getPosition().getX();
                y += landingSite.getPosition().getY();
                numSites++;
            }
        }

        x /= numSites;
        y /= numSites;

        meanLandingSite = new LandingSite(new Point2D.Double(x, y), launchSite, LandingSite.LandingColour.MEAN);
    }

    /**
     * Calculate the standard deviation ellipse.
     * <p>
     * This works out the ellipseAngle, standardDevX and standardDevY
     * </p>
     *
     * @param meanLandingSite mean landing site
     * @param landingSites    all landing site data
     */
    private void calculateStandardDeviationEllipse(Point2D meanLandingSite, Collection<LandingSite> landingSites) {
        double sumXSquared = 0;
        double sumYSquared = 0;
        double sumXyDif = 0;
        double numSites = 0;

        for (LandingSite site : landingSites) {
            if (site.getLandingColour() == LandingSite.LandingColour.PARACHUTE_DEPLOYED) {
                sumXSquared += (meanLandingSite.getX() - site.getPosition().getX()) * (meanLandingSite.getX() - site.getPosition().getX());
                sumYSquared += (meanLandingSite.getY() - site.getPosition().getY()) * (meanLandingSite.getY() - site.getPosition().getY());
                sumXyDif += (site.getPosition().getX() - meanLandingSite.getX()) * (site.getPosition().getY() - meanLandingSite.getY());
                numSites++;
            }
        }

        // angle of rotation
        double a = sumXSquared - sumYSquared;
        double b = Math.sqrt(a * a + 4 * sumXyDif * sumXyDif);
        double c = 2 * sumXyDif;

        ellipseAngle = Math.atan2(a + b, c);

        // std(x) and std(y)

        double stdx = 0;
        double stdy = 0;

        for (LandingSite site : landingSites) {
            if (site.getLandingColour() == LandingSite.LandingColour.PARACHUTE_DEPLOYED) {
                double x = (site.getPosition().getX() - meanLandingSite.getX()) * Math.cos(ellipseAngle) - (site.getPosition().getY()
                        - meanLandingSite.getY()) * Math.sin(ellipseAngle);
                stdx += x * x;
                double y = (site.getPosition().getX() - meanLandingSite.getX()) * Math.sin(ellipseAngle) + (site.getPosition().getY()
                        - meanLandingSite.getY()) * Math.cos(ellipseAngle);
                stdy += y * y;
            }
        }

        standardDevX = Math.sqrt(stdx / numSites);
        standardDevY = Math.sqrt(stdy / numSites);
    }

    /**
     * Landing sites.
     *
     * @return
     */
    public Collection<LandingSite> getLandingSites() {
        return landingSites;
    }

    /**
     * Parameters.
     *
     * @return
     */
    public OpenRocketMonteCarloParameters getParameters() {
        return parameters;
    }

    /**
     * Launch site.
     *
     * @return
     */
    public LandingSite getLaunchSite() {
        return launchSite;
    }

    /**
     * Mean landing site.
     *
     * @return
     */
    public LandingSite getMeanLandingSite() {
        return meanLandingSite;
    }

    /**
     * Angle of the standard deviation ellipse.
     *
     * @return
     */
    public double getEllipseAngle() {
        return ellipseAngle;
    }

    /**
     * Standard deviation in x direction (after rotation).
     *
     * @return
     */
    public double getStandardDevX() {
        return standardDevX;
    }


    /**
     * Standard deviation in y direction (after rotation).
     *
     * @return
     */
    public double getStandardDevY() {
        return standardDevY;
    }
}
