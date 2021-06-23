package nz.ac.vuw.ecs.ormontecarlosimplugin.gui.components.output;

import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter.ParameterType;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.LandingSite;
import nz.ac.vuw.ecs.ormontecarlosimplugin.gui.FontUtil;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.text.DecimalFormat;
import java.util.Map;

/**
 * Should have all the variables used for each landing site.
 * For example what the launch angle was, wind speed etc,
 * To show why they point is where it is on the screen
 */
public class LandingSiteInformationPanel extends JPanel {

    private SwingLandingSite landingSite;
    private final LandingSite launchSite;

    /**
     * Label for displaying the landing site information.
     */
    private final JTextPane label;

    /**
     * Creates the panel for when a landing site is selected, giving information about that specific site.
     * @param launchSite Contains the landing site information for only the launch site
     */
    public LandingSiteInformationPanel(LandingSite launchSite) {
        setLayout(new BorderLayout());
        this.launchSite = launchSite;

        JLabel heading = new JLabel("Selected Landing Site");
        heading.setFont(FontUtil.getHeadingFont());
        add(heading, BorderLayout.NORTH);


        label = new JTextPane();
        label.setFont(FontUtil.getDefaultFont());
        label.setEditable(false);
        label.setBackground(null);

        JScrollPane scrollArea = new JScrollPane(label);
        add(scrollArea, BorderLayout.CENTER);
    }

    /**
     * Get the selected landing site.
     *
     * @return the selected landing site
     */
    public SwingLandingSite getSelected() {
        return landingSite;
    }

    /**
     * Update the landing site information panel with a new landing site.
     *
     * @param landingSite site to get information from
     */
    public void setInformation(SwingLandingSite landingSite) {
        if (this.landingSite != null) {
            this.landingSite.setSelected(false);
        }

        this.landingSite = landingSite;

        if (landingSite == null) {
            label.setText("No Landing Site selected");
            return;
        }
        landingSite.setSelected(true);

        StringBuilder landingInfo = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.###");

        if (landingSite.getLandingSite().getLandingColour() == LandingSite.LandingColour.LAUNCH_LOCATION) {
            landingInfo.append("Launch Site\n");
        } else if (landingSite.getLandingSite().getLandingColour() == LandingSite.LandingColour.MEAN) {
            landingInfo.append("Mean Landing Site (Deployed parachute)\n");
        }


        landingInfo
                .append("Distance from launch: ")
                .append(df.format(landingSite.getLandingSite().distanceTo(launchSite)))
                .append(" metres").append("\n\n");

        landingInfo
                .append("Latitude: ")
                .append(df.format(Math.toDegrees(landingSite.getLandingSite().getLatitude())))
                .append(" degrees").append("\n");
        landingInfo
                .append("Longitude: ")
                .append(df.format(Math.toDegrees(landingSite.getLandingSite().getLongitude())))
                .append(" degrees").append("\n\n");

        Map<String, Double> parameters = landingSite.getLandingSite().getParameters();
        if (parameters.containsKey(ParameterType.LAUNCH_ANGLE.getName())) {
            landingInfo.append("Launch rod angle: ").append(df.format(Math.toDegrees(parameters.get(ParameterType.LAUNCH_ANGLE.getName())))).append(" degrees")
                    .append("\n");
        }
        if (parameters.containsKey(ParameterType.LAUNCH_DIRECTION.getName())) {
            landingInfo.append("Launch rod direction: ").append(df.format(Math.toDegrees(parameters.get(ParameterType.LAUNCH_DIRECTION.getName()))))
                    .append(" degrees").append("\n\n");
        }
        if (parameters.containsKey(ParameterType.WIND_DIRECTION.getName())) {
            landingInfo.append("Wind angle: ").append(df.format(Math.toDegrees(parameters.get(ParameterType.WIND_DIRECTION.getName())))).append(" degrees")
                    .append("\n");
        }
        if (parameters.containsKey(ParameterType.PRESSURE.getName())) {
            landingInfo.append("Air Pressure: ").append(df.format(parameters.get(ParameterType.PRESSURE.getName()))).append(" Pa").append("\n");
        }
        if (parameters.containsKey(ParameterType.TEMPERATURE.getName())) {
            landingInfo.append("Temperature: ").append(new DecimalFormat("#.#")
                    .format(parameters.get(ParameterType.TEMPERATURE.getName()) - 273.15)).append(" C").append("\n");
        }
        if (parameters.containsKey(ParameterType.PARACHUTE_TIME.getName())) {
            landingInfo.append("Parachute time: ").append(df.format(parameters.get(ParameterType.PARACHUTE_TIME.getName()))).append(" seconds").append("\n\n");
        }
        if (parameters.containsKey(ParameterType.MOTOR_PERFORMANCE.getName())) {
            landingInfo.append("Motor performance: ").append(df.format(parameters.get(ParameterType.MOTOR_PERFORMANCE.getName()))).append("%").append("\n");

            if (landingSite.getLandingSite().getLandingColour() == LandingSite.LandingColour.PARACHUTE_DEPLOYED) {
                landingInfo.append("Parachute: Parachute deployed \n\n");

            } else if (landingSite.getLandingSite().getLandingColour() == LandingSite.LandingColour.PARACHUTE_FAILED) {
                landingInfo.append("Parachute: Parachute failed to deploy \n\n");
            }
        }

        label.setText(landingInfo.toString());
    }

}
