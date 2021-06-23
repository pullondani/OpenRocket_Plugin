package nz.ac.vuw.ecs.ormontecarlosimplugin.gui.components.output;

import net.sf.openrocket.simulation.SimulationOptions;
import nz.ac.vuw.ecs.ormontecarlosimplugin.gui.FontUtil;

import java.text.DecimalFormat;

import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.MonteCarloResult;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class SimulationInformationPanel extends JPanel {
    /**
     * Label for displaying the Simulation information.
     */
    private final JTextPane label;

    private final MonteCarloResult result;

    /**
     * Information panel on monte carlo simulation.
     *
     * @param result monte carlo result
     */
    public SimulationInformationPanel(MonteCarloResult result) {
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("Simulation Information");
        heading.setFont(FontUtil.getHeadingFont());
        add(heading, BorderLayout.NORTH);

        label = new JTextPane();
        label.setFont(FontUtil.getDefaultFont());
        label.setEditable(false);
        label.setBackground(null);

        JScrollPane scrollArea = new JScrollPane(label);
        add(scrollArea, BorderLayout.CENTER);

        this.result = result;
    }

    /**
     * Set information about clicked landing site.
     */
    public void setInformation() {
        SimulationOptions options = result.getParameters().getSim().getOptions();

        StringBuilder simInfo = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.###");

        simInfo.append("INITIAL CONFIG").append("\n\n");

        simInfo.append("Launch Latitude: ").append(df.format(options.getLaunchLatitude())).append(" degrees").append("\n");
        simInfo.append("Launch Longitude: ").append(df.format(options.getLaunchLongitude())).append(" degrees").append("\n\n");

        simInfo.append("Launch rod angle: ").append(df.format(Math.toDegrees(options.getLaunchRodAngle()))).append(" degrees").append("\n");
        simInfo.append("Launch rod direction: ").append(df.format(Math.toDegrees(options.getLaunchRodDirection()))).append(" degrees").append("\n\n");

        simInfo.append("Launch wind angle: ").append(df.format(Math.toDegrees(options.getWindDirection()))).append(" degrees").append("\n");
        simInfo.append("Launch air Pressure: ").append(df.format(options.getLaunchPressure())).append(" Pa").append("\n");
        simInfo.append("Launch temperature: ").append(df.format(options.getLaunchTemperature() - 273.15)).append(" C").append("\n\n");

        simInfo.append("Launch Altitude: ").append(df.format(options.getLaunchAltitude())).append(" metres").append("\n");
        simInfo.append("Launched into wind: ").append(options.getLaunchIntoWind()).append("\n");
        simInfo.append("Launched rod length: ").append(options.getLaunchRodLength()).append("\n");

        label.setText(simInfo.toString());
    }
}
