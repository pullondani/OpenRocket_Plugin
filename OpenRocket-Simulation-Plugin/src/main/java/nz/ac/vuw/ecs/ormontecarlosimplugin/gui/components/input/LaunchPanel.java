package nz.ac.vuw.ecs.ormontecarlosimplugin.gui.components.input;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.border.BevelBorder;

import net.sf.openrocket.gui.adaptors.DoubleModel;
import nz.ac.vuw.ecs.ormontecarlosimplugin.extend.OrSimulationExtension;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter.ParameterType;

/**
 * Panel for configuring some settings about the launch.
 * Including the launch angle/direction.
 * This also includes some rocket flight settings, such as motor performance and parachute deployment time.
 */
public class LaunchPanel extends JPanel {

    /**
     * Create launch settings panel.
     */
    public LaunchPanel() {
        setLayout(new GridBagLayout());
    }

    /**
     * set the size of the panel.
     *
     * @param dimension size
     */
    public void setPanelSize(Dimension dimension) {
        setPreferredSize(dimension);
        setBorder(BorderFactory
                .createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.red, Color.black), "LAUNCH PARAMETERS"));
    }

    /**
     * Add components to the launch parameters panel.
     *
     * @param extension extension to set parameters for
     * @author Lily
     */
    public void setPanelComponents(OrSimulationExtension extension) {
        // launch angle
        JLabel launchAngle = new JLabel("  Launch Angle Standard Deviation (degrees):");
        launchAngle.setToolTipText(ParameterType.LAUNCH_ANGLE.getDescription());
        DoubleModel launchAngleStdDevModel = new DoubleModel(extension, "LaunchAngleStdDev", 0, ParameterType.LAUNCH_ANGLE.getDeviationMax());
        JSpinner launchAngleStdDevSpinner = new JSpinner(launchAngleStdDevModel.getSpinnerModel());
        launchAngleStdDevSpinner.setPreferredSize(new Dimension(getPreferredSize().width * 2 / 3,
                launchAngleStdDevSpinner.getPreferredSize().height));
        ((DefaultEditor) launchAngleStdDevSpinner.getEditor()).getTextField().setEditable(true);

        // launch direction
        JLabel launchDirection = new JLabel("  Launch Direction Standard Deviation (degrees):");
        launchDirection.setToolTipText(ParameterType.LAUNCH_DIRECTION.getDescription());
        DoubleModel launchDirStdDevModel = new DoubleModel(extension, "LaunchDirectionStdDev", 0, ParameterType.LAUNCH_DIRECTION.getDeviationMax());
        JSpinner launchDirStdDevSpinner = new JSpinner(launchDirStdDevModel.getSpinnerModel());
        launchDirStdDevSpinner.setPreferredSize(new Dimension(getPreferredSize().width * 2 / 3,
                launchDirStdDevSpinner.getPreferredSize().height));
        ((DefaultEditor) launchDirStdDevSpinner.getEditor()).getTextField().setEditable(true);

        // launch direction
        JLabel maxAngle = new JLabel("  Maximum Launch Angle (degrees):");

        maxAngle.setToolTipText(ParameterType.MAX_ANGLE.getDescription());
        DoubleModel maxAngleSpinnerModel = new DoubleModel(extension, "MaxAngle",
                ParameterType.MAX_ANGLE.getDefaultMin(), ParameterType.MAX_ANGLE.getDefaultMax());
        JSpinner maxAngleSpinner = new JSpinner(maxAngleSpinnerModel.getSpinnerModel());
        maxAngleSpinner.setPreferredSize(new Dimension(getPreferredSize().width * 2 / 3,
                maxAngleSpinner.getPreferredSize().height));
        ((DefaultEditor) maxAngleSpinner.getEditor()).getTextField().setEditable(true);

        // parachute deployment time
        JLabel timeToOpenParachute = new JLabel("  Parachute Deployment Time Standard Deviation (seconds):");

        timeToOpenParachute.setToolTipText(ParameterType.PARACHUTE_TIME.getDescription());
        DoubleModel timeToOpenParachuteStdDevModel = new DoubleModel(extension, "TimeToOpenParachuteStdDev",
                0, ParameterType.PARACHUTE_TIME.getDefaultMax());
        JSpinner timeToOpenParachuteStdDevSpinner = new JSpinner(timeToOpenParachuteStdDevModel.getSpinnerModel());
        timeToOpenParachuteStdDevSpinner.setPreferredSize(new Dimension(getPreferredSize().width * 2 / 3,
                timeToOpenParachuteStdDevSpinner.getPreferredSize().height));
        ((DefaultEditor) timeToOpenParachuteStdDevSpinner.getEditor()).getTextField().setEditable(true);

        // motor performance mean
        JLabel motorMean = new JLabel("  Motor Performance Mean (percentage):");
        motorMean.setToolTipText(ParameterType.MOTOR_PERFORMANCE.getDescription());
        DoubleModel motorPerformanceMeanModel = new DoubleModel(extension, "MotorPerformanceMean",
                20, ParameterType.MOTOR_PERFORMANCE.getDefaultMax());
        JSpinner motorPerformanceMeanSpinner = new JSpinner(motorPerformanceMeanModel.getSpinnerModel());
        motorPerformanceMeanSpinner.setPreferredSize(new Dimension(getPreferredSize().width * 2 / 3,
                motorPerformanceMeanSpinner.getPreferredSize().height));
        ((DefaultEditor) motorPerformanceMeanSpinner.getEditor()).getTextField().setEditable(true);

        // Motor performance standard deviation
        JLabel motorStdDev = new JLabel("  Motor Performance Standard Deviation (percentage):");
        motorStdDev.setToolTipText(ParameterType.strBuilderForStdDev("Motor Performance", "Percentage (%)"));

        DoubleModel motorPerformanceStdDevModel = new DoubleModel(extension, "MotorPerformanceStdDev",
                0, ParameterType.MOTOR_PERFORMANCE.getDeviationMax());
        JSpinner motorPerformanceStdDevSpinner = new JSpinner(motorPerformanceStdDevModel.getSpinnerModel());
        motorPerformanceStdDevSpinner.setPreferredSize(new Dimension(getPreferredSize().width * 2 / 3,
                motorPerformanceStdDevSpinner.getPreferredSize().height));
        ((DefaultEditor) motorPerformanceStdDevSpinner.getEditor()).getTextField().setEditable(true);

        // Motor direction standard deviation
        JLabel motorDirectionStdDev = new JLabel(" Motor Direction Standard Deviation (angle, degrees):");
        motorDirectionStdDev.setToolTipText(ParameterType.strBuilderForStdDev("Motor Direction", "Degrees"));

        DoubleModel motorDirectionStdDevModel = new DoubleModel(extension, "MotorDirectionStdDev",
                0, ParameterType.MOTOR_DIRECTION.getDeviationMax());
        JSpinner motorDirectionStdDevSpinner = new JSpinner(motorDirectionStdDevModel.getSpinnerModel());
        motorDirectionStdDevSpinner.setPreferredSize(new Dimension(getPreferredSize().width * 2 / 3,
                motorDirectionStdDevSpinner.getPreferredSize().height));
        ((DefaultEditor) motorDirectionStdDevSpinner.getEditor()).getTextField().setEditable(true);

        // constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(launchAngle, gbc);
        gbc.gridy = 1;
        add(launchDirection, gbc);
        gbc.gridy = 2;
        add(maxAngle, gbc);
        gbc.gridy = 3;
        add(timeToOpenParachute, gbc);
        gbc.gridy = 4;
        add(motorMean, gbc);
        gbc.gridy = 5;
        add(motorStdDev, gbc);
        gbc.gridy = 6;
        add(motorDirectionStdDev, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(launchAngleStdDevSpinner, gbc);
        gbc.gridy = 1;
        add(launchDirStdDevSpinner, gbc);
        gbc.gridy = 2;
        add(maxAngleSpinner, gbc);
        gbc.gridy = 3;
        add(timeToOpenParachuteStdDevSpinner, gbc);
        gbc.gridy = 4;
        add(motorPerformanceMeanSpinner, gbc);
        gbc.gridy = 5;
        add(motorPerformanceStdDevSpinner, gbc);
        gbc.gridy = 6;
        add(motorDirectionStdDevSpinner, gbc);
    }

}
