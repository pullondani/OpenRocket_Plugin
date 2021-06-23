package nz.ac.vuw.ecs.ormontecarlosimplugin.gui.components.input;

import net.sf.openrocket.gui.adaptors.BooleanModel;
import net.sf.openrocket.gui.adaptors.DoubleModel;
import nz.ac.vuw.ecs.ormontecarlosimplugin.extend.OrSimulationExtension;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter.ParameterType;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.Color;

/**
<<<<<<< HEAD
 * Represents the JPanel with inputs for PID constants and checkbox to enable PID
=======
 * PID input panel.
>>>>>>> lily_checkstyle
 */
public class PidPanel extends JPanel {
    /**
     * Create the panel.
     */
    public PidPanel() {
        setLayout(new GridBagLayout());
    }

    /**
     * Set size of the panel.
     *
     * @param dimension size of panel
     */
    public void setPanelSize(Dimension dimension) {
        setPreferredSize(dimension);
        setBorder(BorderFactory
                .createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.black), "PID PARAMETERS"));
    }

    /**
     * Create components for the panel.
     *
     * @param extension extension to output the settings to
     * @author Lily, Harrison
     */
    public void setPanelComponents(OrSimulationExtension extension) {

        //Boolean, turn PID on an off
        JLabel utilizePidText = new JLabel("Use PID");
        utilizePidText.setToolTipText("Toggle PID controller for engine gimbal");

        // P constant for PID controller
        JLabel labelP = new JLabel("P: ");
        labelP.setToolTipText(ParameterType.P.getDescription());
        DoubleModel modelP = new DoubleModel(extension, "P", ParameterType.P.getDefaultMin(), ParameterType.P.getDefaultMax());
        JSpinner spinnerP = new JSpinner(modelP.getSpinnerModel());
        spinnerP.setPreferredSize(new Dimension(getPreferredSize().width * 2 / 3, spinnerP.getPreferredSize().height));
        ((JSpinner.DefaultEditor) spinnerP.getEditor()).getTextField().setEditable(true);

        // I constant for PID controller
        JLabel labelI = new JLabel("I: ");
        labelI.setToolTipText(ParameterType.I.getDescription());
        DoubleModel modelI = new DoubleModel(extension, "I", ParameterType.I.getDefaultMin(), ParameterType.P.getDefaultMax());
        JSpinner spinnerI = new JSpinner(modelI.getSpinnerModel());
        spinnerI.setPreferredSize(new Dimension(getPreferredSize().width * 2 / 3, spinnerI.getPreferredSize().height));
        ((JSpinner.DefaultEditor) spinnerI.getEditor()).getTextField().setEditable(true);

        // D constant for PID controller
        JLabel labelD = new JLabel("D: ");
        labelD.setToolTipText(ParameterType.D.getDescription());
        DoubleModel modelD = new DoubleModel(extension, "D", ParameterType.D.getDefaultMin(), ParameterType.P.getDefaultMax());
        JSpinner spinnerD = new JSpinner(modelD.getSpinnerModel());
        spinnerD.setPreferredSize(new Dimension(getPreferredSize().width * 2 / 3, spinnerD.getPreferredSize().height));
        ((JSpinner.DefaultEditor) spinnerD.getEditor()).getTextField().setEditable(true);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(utilizePidText, gbc);
        gbc.gridy = 1;
        add(labelP, gbc);
        gbc.gridy = 2;
        add(labelI, gbc);
        gbc.gridy = 3;
        add(labelD, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 2;
        gbc.anchor = GridBagConstraints.LINE_END;

        // use PID checkbox
        BooleanModel pidBoolModel = new BooleanModel(extension, "UsePid");
        JCheckBox pidCheckBox = new JCheckBox(pidBoolModel);
        add(pidCheckBox, gbc);
        gbc.gridy = 1;
        add(spinnerP, gbc);
        gbc.gridy = 2;
        add(spinnerI, gbc);
        gbc.gridy = 3;
        add(spinnerD, gbc);
    }
}
