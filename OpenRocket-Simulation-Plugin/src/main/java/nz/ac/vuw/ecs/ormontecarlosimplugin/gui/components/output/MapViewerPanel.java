package nz.ac.vuw.ecs.ormontecarlosimplugin.gui.components.output;

import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.LandingSite;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.MonteCarloResult;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Comparator;

/**
 * Panel that contains the map and all waypoints.
 */
public class MapViewerPanel extends JXMapViewer implements MouseListener {

    public LandingSiteInformationPanel landingSiteInformationPanel;

    /**
     * Create new map viewer.
     * 
     * @param landingSiteInformationPanel information panel to put information of selected site to.
     */
    public MapViewerPanel(LandingSiteInformationPanel landingSiteInformationPanel) {
        this.landingSiteInformationPanel = landingSiteInformationPanel;

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);

        // Setup JX
        setTileFactory(tileFactory);

        // Set the zoom level
        setZoom(2);

        // Add interactions
        MouseInputListener mia = new PanMouseInputListener(this);
        addMouseListener(mia);
        addMouseMotionListener(mia);

        addMouseListener(new CenterMapListener(this));
        addMouseListener(this);

        addMouseWheelListener(new ZoomMouseWheelListenerCursor(this));

        addKeyListener(new PanKeyListener(this));
    }

    /**
     * Add all waypoints to the map viewer.
     *
     * @param monteCarloResult Graph point to be displayed as a coloured circle on the map
     */
    public void addPoints(MonteCarloResult monteCarloResult) {

        // create all waypoints
        List<SwingLandingSite> waypoints = new ArrayList<>();

        for (LandingSite landingSite : monteCarloResult.getLandingSites()) {
            SwingLandingSite sls = new SwingLandingSite(landingSite);
            sls.getButton().addActionListener(e -> {
                landingSiteInformationPanel.setInformation(sls);
                repaint();
            });
            waypoints.add(sls);
        }
        LandingSite launch = monteCarloResult.getLaunchSite();

        SwingLandingSite launchSls = new SwingLandingSite(launch);
        launchSls.getButton().addActionListener(e -> {
            landingSiteInformationPanel.setInformation(launchSls);
            repaint();
        });
        waypoints.add(launchSls);

        SwingLandingSite meanSls = new SwingLandingSite(monteCarloResult.getMeanLandingSite());
        meanSls.getButton().addActionListener(e -> {
            landingSiteInformationPanel.setInformation(meanSls);
            repaint();
        });
        waypoints.add(meanSls);

        setAddressLocation(launch.toGeoPosition());

        WaypointPainter<SwingLandingSite> swingWaypointPainter = new SwingLandingSiteOverlayPainter(monteCarloResult);
        swingWaypointPainter.setWaypoints(new HashSet<>(waypoints));

        // set the overlay
        setOverlayPainter(swingWaypointPainter);

        // Add the JButtons to the map viewer
        waypoints.sort(Comparator.comparingInt((SwingLandingSite sls) -> sls.getLandingSite().getLandingColour().getDepth()));
        for (SwingLandingSite w : waypoints) {
            add(w.getButton());
        }
    }

    /**
     * Deselect when clicking on nothing.
     *
     * @param e mouse event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getComponent().equals(this)) {
            landingSiteInformationPanel.setInformation(null);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
