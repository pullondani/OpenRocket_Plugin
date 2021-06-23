package nz.ac.vuw.ecs.ormontecarlosimplugin.gui.components.output;

import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.LandingSite;
import org.jxmapviewer.viewer.DefaultWaypoint;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;

/**
 * A waypoint that is represented by a button on the map.
 *
 * @author Daniel Pullon, Thomas Page
 */
public class SwingLandingSite extends DefaultWaypoint {
    private final Color color;
    private final LandingSite landingSite;
    private final LandingSiteButton button;
    private boolean selected = false;

    /**
     * New clickable landing site on the gui.
     *
     * @param landingSite landing site
     */
    public SwingLandingSite(LandingSite landingSite) {
        super(landingSite.toGeoPosition());
        this.landingSite = landingSite;
        color = landingSite.getLandingSiteColor();
        button = new LandingSiteButton();
        button.setSize(10, 10);
        button.setPreferredSize(new Dimension(10, 10));
        button.setFocusable(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setRolloverEnabled(false);
    }

    /**
     * Get JButton component.
     *
     * @return button
     */
    public JButton getButton() {
        return button;
    }

    /**
     * Get the landing site object.
     *
     * @return landing site
     */
    public LandingSite getLandingSite() {
        return landingSite;
    }

    /**
     * Set if this landing site is selected.
     *
     * @param selected selected site
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Returns if this is selected.
     *
     * @return boolean is selected
     */
    public boolean isSelected() {
        return selected;
    }

    private class LandingSiteButton extends JButton {

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(selected ? LandingSite.LandingColour.SELECTED.getColor() : color);
            g.fillOval(0, 0, 10, 10);
        }
    }
}