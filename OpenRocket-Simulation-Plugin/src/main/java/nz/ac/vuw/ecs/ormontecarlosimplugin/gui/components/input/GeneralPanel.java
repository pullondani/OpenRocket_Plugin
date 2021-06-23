package nz.ac.vuw.ecs.ormontecarlosimplugin.gui.components.input;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.border.BevelBorder;

import net.sf.openrocket.gui.adaptors.BooleanModel;
import net.sf.openrocket.gui.adaptors.IntegerModel;
import nz.ac.vuw.ecs.ormontecarlosimplugin.extend.OrSimulationExtension;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter.ParameterType;

/**
 * Panel for general settings about the monte carlo simulation.
 * <p>
 * This includes the number of simulations to run
 * </p>
 */
public class GeneralPanel extends JPanel {

    /**
     * Create the panel.
     */
    public GeneralPanel() {
        setLayout(new GridBagLayout());
    }


    /**
     * Set size of the panel.
     *
     * @param dimension Dimension of panel size.
     */
    public void setPanelSize(Dimension dimension) {
        setPreferredSize(dimension);
        setBorder(BorderFactory
                .createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.magenta, Color.black), "GENERAL PARAMETERS"));
    }


    /**
     * Create components for the panel.
     *
     * @param extension extension to output the settings to
     * @author Lily
     */
    public void setPanelComponents(OrSimulationExtension extension) {
        // number of simulations
        JLabel numSimsLabel = new JLabel("Number of simulations");
        numSimsLabel.setToolTipText(ParameterType.SIM_NUMBER.getDescription());
        IntegerModel numberSimsModel = new IntegerModel(extension, "NumSims", (int) ParameterType.SIM_NUMBER.getDefaultMin(),
                (int) ParameterType.SIM_NUMBER.getDefaultMax());
        JSpinner numberSimsSpinner = new JSpinner(numberSimsModel.getSpinnerModel());
        numberSimsSpinner.setPreferredSize(new Dimension(getPreferredSize().width * 2 / 3, numberSimsSpinner.getPreferredSize().height));
        ((DefaultEditor) numberSimsSpinner.getEditor()).getTextField().setEditable(true);

        JLabel idealLaunchVectorText = new JLabel("Calculate Ideal Launch Vector");
        idealLaunchVectorText.setToolTipText("Toggle Machine Learning ideal launch vector calculations and usage");

        // constraints
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.weightx = 1;
        add(numSimsLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(numberSimsSpinner, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.weightx = 1;
        add(idealLaunchVectorText, gbc);

        gbc.gridx = 1;
        gbc.weightx = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        //getter and setter inside simulation extension
        BooleanModel idealLaunchVectorModel = new BooleanModel(extension, "IdealLaunch");
        JCheckBox idealLaunchVectorBox = new JCheckBox(idealLaunchVectorModel);
        add(idealLaunchVectorBox, gbc);
    }
}
