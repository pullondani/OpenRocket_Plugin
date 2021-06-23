package nz.ac.vuw.ecs.ormontecarlosimplugin.gui.components.input;

import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Window;
import java.awt.KeyboardFocusManager;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;

import net.sf.openrocket.gui.adaptors.DoubleModel;
import net.sf.openrocket.gui.adaptors.IntegerModel;
import nz.ac.vuw.ecs.ormontecarlosimplugin.extend.OrSimulationExtension;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter.ParameterType;

/**
 * Panel containing parameters for the environment.
 * <p>
 * This includes wind, temperature and pressure.
 * </p>
 */
public class EnvironmentPanel extends JPanel {

    private OrSimulationExtension extension;

    /**
     * Create new environment parameters panel.
     */
    public EnvironmentPanel() {
        setLayout(new GridBagLayout());
    }

    /**
     * Set dimensions of the environment panel.
     *
     * @param dimension Dimension for panel size.
     */
    public void setPanelSize(Dimension dimension) {
        setPreferredSize(dimension);
        setBorder(BorderFactory
                .createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.blue, Color.black), "ENVIRONMENT PARAMETERS"));
    }

    /**
     * Create components for the environment parameters panel.
     *
     * @param extension extension to create the parameters for
     * @author Lily
     */
    public void setPanelComponents(OrSimulationExtension extension) {
        this.extension = extension;
        // wind direction
        JLabel windDirStdDevLabel = new JLabel("  Wind Direction Standard Deviation (degrees):");
        windDirStdDevLabel.setToolTipText(ParameterType.strBuilderForStdDev("Wind Direction", "Degrees"));
        DoubleModel windDirStdDevModel = new DoubleModel(extension, "WindDirectionStdDev", 0, ParameterType.WIND_DIRECTION.getDeviationMax());
        JSpinner windDirStdDevSpinner = new JSpinner(windDirStdDevModel.getSpinnerModel());
        windDirStdDevSpinner.setPreferredSize(new Dimension(getPreferredSize().width * 2 / 3, windDirStdDevSpinner.getPreferredSize().height));
        ((DefaultEditor) windDirStdDevSpinner.getEditor()).getTextField().setEditable(true);

        // pressure
        JLabel pressureStdDevLabel = new JLabel("  Pressure Standard Deviation (Pa):");
        pressureStdDevLabel.setToolTipText(ParameterType.strBuilderForStdDev("Pressure", "Pascals"));
        DoubleModel pressureStdDevModel = new DoubleModel(extension, "PressureStdDev", 0, ParameterType.PRESSURE.getDeviationMax());
        JSpinner pressureStdDevSpinner = new JSpinner(pressureStdDevModel.getSpinnerModel());
        pressureStdDevSpinner.setPreferredSize(new Dimension(getPreferredSize().width * 2 / 3, pressureStdDevSpinner.getPreferredSize().height));
        ((DefaultEditor) pressureStdDevSpinner.getEditor()).getTextField().setEditable(true);

        // temperature
        JLabel temperatureStdDevLabel = new JLabel("  Temperature Standard Deviation (degrees C):");
        temperatureStdDevLabel.setToolTipText(ParameterType.strBuilderForStdDev("Temperature", "Degrees Celsius"));
        DoubleModel temperatureStdDevModel = new DoubleModel(extension, "TemperatureStdDev", 0, ParameterType.TEMPERATURE.getDeviationMax());
        JSpinner temperatureStdDevSpinner = new JSpinner(temperatureStdDevModel.getSpinnerModel());
        temperatureStdDevSpinner.setPreferredSize(new Dimension(getPreferredSize().width * 2 / 3, temperatureStdDevSpinner.getPreferredSize().height));
        ((DefaultEditor) temperatureStdDevSpinner.getEditor()).getTextField().setEditable(true);

        // wind altitude values
        JLabel altitudeValueLabel = new JLabel("  Wind altitude values:");
        altitudeValueLabel.setToolTipText(ParameterType.WIND_ALTITUDE_VALUES.getDescription());
        IntegerModel altitudeValueModel = new IntegerModel(extension, "WindAltitudeValues", 1, 10); // TODO allow scrollbars such that this can be extended more
        JSpinner altitudeValueSpinner = new JSpinner(altitudeValueModel.getSpinnerModel());
        altitudeValueSpinner.setPreferredSize(new Dimension(getPreferredSize().width * 2 / 3, altitudeValueSpinner.getPreferredSize().height));
        altitudeValueSpinner.addChangeListener((ChangeEvent e) -> resetWindSpeedAltitudes());

        // wind altitude step
        JLabel altitudeStepLabel = new JLabel("  Wind altitude step (km):");
        altitudeStepLabel.setToolTipText(ParameterType.WIND_ALTITUDE_STEP.getDescription());
        DoubleModel altitudeStepModel = new DoubleModel(extension, "WindAltitudeStep", 0.01, 100.0);
        JSpinner altitudeStepSpinner = new JSpinner(altitudeStepModel.getSpinnerModel());
        altitudeStepSpinner.setPreferredSize(new Dimension(getPreferredSize().width * 2 / 3, altitudeStepSpinner.getPreferredSize().height));
        altitudeStepSpinner.addChangeListener((ChangeEvent e) -> resetWindSpeedAltitudes());
        ((DefaultEditor) altitudeStepSpinner.getEditor()).getTextField().setEditable(true);

        Window parentWindow = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();

        JButton editWindAltitudeLabel = new JButton("Edit");
        editWindAltitudeLabel.addActionListener((e) -> new WindAltitudeGui(parentWindow, extension));


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.weightx = 1;
        add(windDirStdDevLabel, gbc);
        gbc.gridy = 1;
        add(pressureStdDevLabel, gbc);
        gbc.gridy = 2;
        add(temperatureStdDevLabel, gbc);
        gbc.gridy = 3;
        add(altitudeValueLabel, gbc);
        gbc.gridy = 4;
        add(altitudeStepLabel, gbc);
        gbc.gridy = 5;
        JLabel editWindAltitudesLabel = new JLabel("  Edit wind speed and directions:");
        add(editWindAltitudesLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridy = 0;
        add(windDirStdDevSpinner, gbc);
        gbc.gridy = 1;
        add(pressureStdDevSpinner, gbc);
        gbc.gridy = 2;
        add(temperatureStdDevSpinner, gbc);
        gbc.gridy = 3;
        add(altitudeValueSpinner, gbc);
        gbc.gridy = 4;
        add(altitudeStepSpinner, gbc);
        gbc.gridy = 5;
        add(editWindAltitudeLabel, gbc);


    }

    /**
     * Reset wind speed/direction when you change the step or the number of values.
     */
    private void resetWindSpeedAltitudes() {
        // reset values to default
        extension.getWindSpeedAltitudes().clear();
        extension.getWindDirectionAltitudes().clear();
        double altitude = 0;
        for (int i = 0; i < extension.getWindAltitudeValues(); i++) {
            extension.getWindSpeedAltitudes().put(altitude, ParameterType.WIND_SPEED.getDefaultMean());
            extension.getWindDirectionAltitudes().put(altitude, ParameterType.WIND_DIRECTION.getDefaultMean());
            altitude += extension.getWindAltitudeStep();
        }

    }
}
