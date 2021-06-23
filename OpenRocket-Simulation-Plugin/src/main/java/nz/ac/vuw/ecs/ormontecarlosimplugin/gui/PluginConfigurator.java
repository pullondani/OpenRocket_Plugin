package nz.ac.vuw.ecs.ormontecarlosimplugin.gui;

import java.awt.Dimension;
import java.awt.BorderLayout;

import net.sf.openrocket.document.Simulation;
import net.sf.openrocket.plugin.Plugin;
import net.sf.openrocket.simulation.extension.AbstractSwingSimulationExtensionConfigurator;
import nz.ac.vuw.ecs.ormontecarlosimplugin.extend.OrSimulationExtension;

import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import nz.ac.vuw.ecs.ormontecarlosimplugin.gui.components.input.GeneralPanel;
import nz.ac.vuw.ecs.ormontecarlosimplugin.gui.components.input.LaunchPanel;
import nz.ac.vuw.ecs.ormontecarlosimplugin.gui.components.input.EnvironmentPanel;
import nz.ac.vuw.ecs.ormontecarlosimplugin.gui.components.input.PidPanel;
import nz.ac.vuw.ecs.ormontecarlosimplugin.gui.components.input.ImportPanel;

/**
 * Creates the GUI for the configuration of the extension.
 *
 * <p>The gui allows you to adjust the standard deviations of a variety of parameters affecting the flight of the rocket.
 */
@Plugin
public class PluginConfigurator extends AbstractSwingSimulationExtensionConfigurator<OrSimulationExtension> {
    private Dimension panelSize;

    private GeneralPanel generalPanel;
    private LaunchPanel launchDirectionPanel;
    private EnvironmentPanel windDirectionPanel;
    private PidPanel pidPanel;
    private ImportPanel importPanel;

    private OrSimulationExtension extension;
    private Simulation simulation;
    private JPanel panel;

    /**
     * Create the configurator, this supplies the class to be used as the extension.
     */
    protected PluginConfigurator() {
        super(OrSimulationExtension.class);
    }

    /**
     * Create the component containing the configuration for the extension.
     *
     * @param extension  open rocket simulation extension
     * @param simulation the open rocket simulation object
     * @param panel      the panel to add components to
     * @return the panel with added components
     */
    @Override
    protected JComponent getConfigurationComponent(OrSimulationExtension extension, Simulation simulation, JPanel panel) {
        this.extension = extension;
        this.simulation = simulation;
        this.panel = panel;

        panelSize = new Dimension(500, 350);
        panel.setPreferredSize(panelSize);
        createAllComponents();

        return panel;
    }

    /**
     * Creates all the components for the GUI.
     */
    private void createAllComponents() {
        panel.setLayout(new BorderLayout());
        createGeneralPanel();
        createLaunchPanel();
        createEnvironmentPanel();
        createPidPanel();
        createImportPanel();

        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.addTab("General", generalPanel);
        tabs.addTab("Launch", launchDirectionPanel);
        tabs.addTab("Enviro", windDirectionPanel);
        tabs.addTab("PID", pidPanel);
        tabs.setPreferredSize(new Dimension(panelSize.width, panelSize.height));

        panel.add(tabs, BorderLayout.CENTER);
        panel.add(importPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates the general panel. This is responsible for holding variables like the number of simulations.
     */
    private void createGeneralPanel() {
        generalPanel = new GeneralPanel();
        generalPanel.setPanelSize(new Dimension(panelSize.width, panelSize.height));
        generalPanel.setPanelComponents(extension);
    }

    /**
     * Creates the launch parameters panel. This is responsible for parameters about the rocket's launch and flight.
     */
    private void createLaunchPanel() {
        launchDirectionPanel = new LaunchPanel();
        launchDirectionPanel.setPanelSize(new Dimension(panelSize.width, panelSize.height));
        launchDirectionPanel.setPanelComponents(extension);
    }

    /**
     * Creates the environment panel. This is responsible for details about the environment such as wind, temperature and pressure.
     */
    private void createEnvironmentPanel() {
        windDirectionPanel = new EnvironmentPanel();
        windDirectionPanel.setPanelSize(new Dimension(panelSize.width, panelSize.height));
        windDirectionPanel.setPanelComponents(extension);
    }

    private void createPidPanel() {
        pidPanel = new PidPanel();
        pidPanel.setPanelSize(new Dimension(panelSize.width, panelSize.height));
        pidPanel.setPanelComponents(extension);
    }

    /**
     * Creates the import panel. This is responsible for adding the button which imports the CSV data.
     */
    private void createImportPanel() {
        importPanel = new ImportPanel(simulation);
        importPanel.setPanelComponents(extension);
    }
}
