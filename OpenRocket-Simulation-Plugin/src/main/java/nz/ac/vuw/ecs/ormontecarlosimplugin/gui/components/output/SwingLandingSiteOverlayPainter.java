package nz.ac.vuw.ecs.ormontecarlosimplugin.gui.components.output;

import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.LandingSite;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.MonteCarloResult;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointPainter;

import javax.swing.JButton;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * "Paints" the Swing waypoints. In fact, just takes care of correct positioning of the representing button.
 *
 * @author Daniel Pullon, Thomas Page
 */
public class SwingLandingSiteOverlayPainter extends WaypointPainter<SwingLandingSite> {

    private final MonteCarloResult result;

    /**
     * Create renderer for landing site waypoints.
     *
     * @param result result of monte carlo simulation
     */
    public SwingLandingSiteOverlayPainter(MonteCarloResult result) {
        this.result = result;
    }

    /**
     * Renders all waypoints on the map.
     *
     * @param g graphics 2d
     * @param jxMapViewer map viewer instance
     * @param width width of display
     * @param height height of display
     */
    @Override
    protected void doPaint(Graphics2D g, JXMapViewer jxMapViewer, int width, int height) {
        Rectangle rectangle = jxMapViewer.getViewportBounds();


        for (SwingLandingSite swingLandingSite : getWaypoints()) {
            Point2D point = jxMapViewer.getTileFactory().geoToPixel(
                    swingLandingSite.getPosition(), jxMapViewer.getZoom());

            int buttonX = (int) (point.getX() - rectangle.getX());
            int buttonY = (int) (point.getY() - rectangle.getY());
            JButton button = swingLandingSite.getButton();
            button.setLocation(buttonX - button.getWidth() / 2, buttonY - button.getHeight() / 2);
        }

        // draw standard deviation ellipse
        AffineTransform at = g.getTransform();  // old transform
        LandingSite mean = result.getMeanLandingSite();

        Point2D meanPosition = jxMapViewer.getTileFactory().geoToPixel(mean.toGeoPosition(), jxMapViewer.getZoom());
        double meanX = meanPosition.getX() - rectangle.getX();
        double meanY = meanPosition.getY() - rectangle.getY();

        LandingSite offsetX = new LandingSite(mean, result.getStandardDevX(), result.getEllipseAngle());
        LandingSite offsetY = new LandingSite(mean, result.getStandardDevY(), result.getEllipseAngle() + Math.PI / 2.0);

        Point2D offsetPositionX = jxMapViewer.getTileFactory().geoToPixel(offsetX.toGeoPosition(), jxMapViewer.getZoom());
        double offsetXByX = offsetPositionX.getX() - rectangle.getX();
        double offsetXByY = offsetPositionX.getY() - rectangle.getY();
        Point2D offsetPositionY = jxMapViewer.getTileFactory().geoToPixel(offsetY.toGeoPosition(), jxMapViewer.getZoom());
        double offsetYByX = offsetPositionY.getX() - rectangle.getX();
        double offsetYByY = offsetPositionY.getY() - rectangle.getY();

        double scaleX = new Point2D.Double(offsetXByX, offsetXByY).distance(new Point2D.Double(meanX, meanY));
        double scaleY = new Point2D.Double(offsetYByX, offsetYByY).distance(new Point2D.Double(meanX, meanY));

        g.translate(meanX, meanY);
        g.rotate(-result.getEllipseAngle());
        g.scale(scaleX, scaleY);
        g.setColor(new Color(112, 156, 68, 70));
        g.fillOval(-1, -1, 2, 2);

        g.setTransform(at);
    }
}