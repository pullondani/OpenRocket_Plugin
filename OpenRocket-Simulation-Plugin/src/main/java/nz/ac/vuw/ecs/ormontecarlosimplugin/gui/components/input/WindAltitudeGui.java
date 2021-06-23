package nz.ac.vuw.ecs.ormontecarlosimplugin.gui.components.input;

import nz.ac.vuw.ecs.ormontecarlosimplugin.extend.OrSimulationExtension;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter.ParameterType;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.Window;

public class WindAltitudeGui extends JDialog {
    private OrSimulationExtension extension;

    /**
     * The dimensions of the gui.
     */
    public final Dimension guiDefaultDimension;

    /**
     * Gui for configuring the different wind speed/direction per altitude.
     *
     * @param parentWindow parent window to this window
     * @param extension ORSimulation extension
     */
    public WindAltitudeGui(Window parentWindow, OrSimulationExtension extension) {
        super(parentWindow);

        this.extension = extension;

        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        guiDefaultDimension = new Dimension(screenDim.width / 2, screenDim.height / 2);
        setSize(guiDefaultDimension);
        setResizable(true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        double altitude = 0;

        for (int i = 0; i < extension.getWindAltitudeValues(); i++) {
            final double thisAltitude = altitude;

            // wind speed
            double speed = extension.getWindSpeedAltitudes().get(thisAltitude);
            JLabel windSpeedLabel = new JLabel("  Wind speed (" + altitude + "km) (m/s):");

            windSpeedLabel.setToolTipText(ParameterType.WIND_SPEED.getDescription());
            SpinnerNumberModel windSpeedModel = new SpinnerNumberModel(speed, ParameterType.WIND_SPEED.getDefaultMin(),
                    ParameterType.WIND_SPEED.getDefaultMax(), 1.0);
            JSpinner speedSpinner = new JSpinner(windSpeedModel);
            speedSpinner.addChangeListener((ChangeEvent e) -> setWindSpeed(thisAltitude,
                    Double.parseDouble(speedSpinner.getValue().toString())));

            // wind direction
            double direction = extension.getWindDirectionAltitudes().get(thisAltitude);
            JLabel windDirLabel = new JLabel("  Wind direction (" + altitude + "km) (degrees):");

            windDirLabel.setToolTipText(ParameterType.WIND_DIRECTION.getDescription());
            SpinnerNumberModel windDirModel = new SpinnerNumberModel(direction, ParameterType.WIND_DIRECTION.getDefaultMin(),
                    ParameterType.WIND_DIRECTION.getDefaultMax(), 1.0);
            JSpinner dirSpinner = new JSpinner(windDirModel);
            dirSpinner.addChangeListener((ChangeEvent e) -> setWindDirection(thisAltitude,
                    Double.parseDouble(dirSpinner.getValue().toString())));

            altitude += extension.getWindAltitudeStep();

            gbc.gridy = i * 2;

            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.weightx = 1;

            add(windSpeedLabel, gbc);

            gbc.gridx = 1;
            gbc.weightx = 2;
            gbc.anchor = GridBagConstraints.LINE_END;
            add(speedSpinner, gbc);

            gbc.gridy = i * 2 + 1;

            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.weightx = 1;

            add(windDirLabel, gbc);

            gbc.gridx = 1;
            gbc.weightx = 2;
            gbc.anchor = GridBagConstraints.LINE_END;
            add(dirSpinner, gbc);
        }

        setModalityType(ModalityType.APPLICATION_MODAL);
        pack();
        setVisible(true);
        this.setLocationRelativeTo(parentWindow);


        repaint();
    }

    /**
     * Update the wind speed for a specific altitude.
     *
     * @param altitude altitude
     * @param speed    speed
     */
    private void setWindSpeed(double altitude, double speed) {
        extension.getWindSpeedAltitudes().put(altitude, speed);
    }


    /**
     * Update the wind direction for a specific altitude.
     *
     * @param altitude  altitude
     * @param direction direction
     */
    private void setWindDirection(double altitude, double direction) {
        extension.getWindDirectionAltitudes().put(altitude, direction);
    }
}
