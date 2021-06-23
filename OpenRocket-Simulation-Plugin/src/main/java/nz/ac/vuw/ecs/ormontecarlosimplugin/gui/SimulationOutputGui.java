package nz.ac.vuw.ecs.ormontecarlosimplugin.gui;

import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Window;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Map;

import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.LandingSite;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.MonteCarloResult;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.WindParameters;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter.GaussianParameter;
import nz.ac.vuw.ecs.ormontecarlosimplugin.gui.components.output.LandingSiteInformationPanel;
import nz.ac.vuw.ecs.ormontecarlosimplugin.gui.components.output.MapViewerPanel;
import nz.ac.vuw.ecs.ormontecarlosimplugin.gui.components.output.SimulationInformationPanel;

/**
 * Represents the GUI that shows the results of the simulation.
 */
public class SimulationOutputGui extends JDialog {

    private static final int INSET_SIZE = 10;

    /**
     * The dimensions of the gui.
     */
    private static final Dimension MIN_DIMENSION = new Dimension(800, 600);

    private static final int COMPONENTS_HEIGHT = MIN_DIMENSION.height - INSET_SIZE * 6;
    private static final int SIDE_WIDTH = (MIN_DIMENSION.width / 3) - INSET_SIZE * 2;
    private static final int MAP_WIDTH = ((MIN_DIMENSION.width * 2) / 3) - INSET_SIZE * 2;
    private static final int MAP_HEIGHT = MIN_DIMENSION.height - INSET_SIZE * 2;
    public final Dimension guiDefaultDimension;

    private final MonteCarloResult result;
    private final double altitudeStep;

    /**
     * Constraints on gui layout.
     */
    private final GridBagConstraints gbc = new GridBagConstraints();

    /**
     * Information panel for landing sites.
     */
    private LandingSiteInformationPanel landingSiteInformationPanel;

    /**
     * Launch site in simulation.
     */
    public final LandingSite launchSite;

    /**
     * Get parent window.
     *
     * @return Window parent window
     */
    private static Window getParentWindow() {
        return KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
    }


    /**
     * Launch gui.
     *
     * @param result the results of the monte carlo simulation
     */
    public SimulationOutputGui(MonteCarloResult result, Double step) {
        super(getParentWindow());
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        guiDefaultDimension = new Dimension(screenDim.width / 2, screenDim.height / 2);
        setSize(guiDefaultDimension);
        setMinimumSize(MIN_DIMENSION);
        setResizable(true);
        setLayout(new GridBagLayout());
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        launchSite = result.getLaunchSite();

        this.result = result;
        this.altitudeStep = step;

        createAllComponents();

        //setAlwaysOnTop(true);
        setModalityType(ModalityType.DOCUMENT_MODAL);
        setVisible(true);
        repaint();
    }

    /**
     * Create gui swing components.
     */
    private void createAllComponents() {
        createSimulationInformationPanel();
        createLandingSiteInformationPanel();
        createScatterPlotCanvas();
        setUpExportButton();
    }

    /**
     * Create Scatter plot canvas (Swing).
     */
    private void createScatterPlotCanvas() {
        /*
         * Scatter plot of landing sites
         */
        MapViewerPanel mapViewerPanel = new MapViewerPanel(landingSiteInformationPanel);
        mapViewerPanel.addPoints(result);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.weightx = 2;
        gbc.weighty = 2 + 1.0 / 4.5;
        gbc.insets = new Insets(INSET_SIZE, INSET_SIZE, INSET_SIZE, INSET_SIZE);

        add(mapViewerPanel, gbc);

        // set a minimum size to the map
        mapViewerPanel.setMinimumSize(new Dimension(MAP_WIDTH, MAP_HEIGHT));
    }

    /**
     * Create simulation information panel (Swing).
     */
    private void createSimulationInformationPanel() {

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(INSET_SIZE, INSET_SIZE, INSET_SIZE, INSET_SIZE);

        /*
         * Information panel for simulation
         */
        SimulationInformationPanel simulationInformationPanel = new SimulationInformationPanel(result);

        add(simulationInformationPanel, gbc);
        simulationInformationPanel.setMinimumSize(new Dimension(SIDE_WIDTH, (int)(COMPONENTS_HEIGHT * 0.45)));
        simulationInformationPanel.setInformation();
    }

    /**
     * Create Landing site information panel (Swing).
     */
    private void createLandingSiteInformationPanel() {
        landingSiteInformationPanel = new LandingSiteInformationPanel(this.launchSite);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(INSET_SIZE, INSET_SIZE, INSET_SIZE, INSET_SIZE);

        add(landingSiteInformationPanel, gbc);
        landingSiteInformationPanel.setMinimumSize(new Dimension(SIDE_WIDTH, (int)(COMPONENTS_HEIGHT * 0.45)));
    }

    /**
     * Button to export all landing sites into a CSV file.
     */
    public void setUpExportButton() {

        JButton exportButton = new JButton("Export to CSV");

        exportButton.addActionListener(e -> {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            jfc.setDialogTitle("Choose a directory to save \"openrocket_monte_carlo.csv\"");
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int userSelection = jfc.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                if (jfc.getSelectedFile().isDirectory()) {
                    File fileToSave = new File(jfc.getSelectedFile().getPath() + "\\openrocket_monte_carlo.csv");
                    try {
                        if (fileToSave.createNewFile()) {
                            System.out.println("File created successfully!");
                        } else {
                            System.out.println("File already exists! Overwriting...");
                        }

                        FileWriter fileWriter = new FileWriter(fileToSave);
                        fileWriter.write(formatDataCsv());
                        fileWriter.close();
                    } catch (IOException exceptionIo) {
                        System.out.println("Problem creating or saving to file.");
                        exceptionIo.printStackTrace();
                    }
                }
            }
        });
        exportButton.setBackground(new Color(97, 183, 232));
        exportButton.setContentAreaFilled(false);
        exportButton.setOpaque(true);
        exportButton.setFont(FontUtil.getHeadingFont());
        exportButton.setForeground(Color.WHITE);


        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.weighty = 1.0 / 4.5;
        gbc.weightx = 1;
        gbc.insets = new Insets(INSET_SIZE, INSET_SIZE, INSET_SIZE, INSET_SIZE);

        add(exportButton, gbc);
        landingSiteInformationPanel.setMinimumSize(new Dimension(SIDE_WIDTH, (int)(COMPONENTS_HEIGHT * 0.1)));
    }

    /**
     * Gets and formats landing site data into a String to output into a CSV file.
     *
     * @return The landing site data as a String.
     */
    private String formatDataCsv() {
        StringBuilder data = new StringBuilder("Launch Latitude,Launch Longitude,Distance From Launch,Mean Latitude,"
                + "Mean Longitude,Landing Latitude,Landing Longitude,Launch Angle,"
                + "Launch Direction,Pressure,Temperature,Parachute Deployed,Parachute Deployment Delay,"
                + "Motor Thrust Performance,Wind Altitude Step,Wind Speed Launch (Mean),Wind Speed One (Mean),"
                + "Wind Speed Two (Mean),Wind Speed Three (Mean),Wind Speed Four (Mean),Wind Speed Five (Mean),"
                + "Wind Speed Six (Mean),Wind Speed Seven (Mean),Wind Speed Eight (Mean),"
                + "Wind Speed Nine (Mean),Wind Speed (Std Dev),Wind Direction Launch (Mean),Wind Direction One (Mean),"
                + "Wind Direction Two (Mean),Wind Direction Three (Mean),Wind Direction Four (Mean)"
                + ",Wind Direction Five (Mean),Wind Direction Six (Mean),Wind Direction Seven (Mean),"
                + "Wind Direction Eight (Mean),Wind Direction Nine (Mean),Wind Direction (Std Dev)\n");

        DecimalFormat df = new DecimalFormat("#.###");
        Collection<LandingSite> landingSites = result.getLandingSites();
        LandingSite launchSite = result.getLaunchSite();
        for (LandingSite ls : landingSites) {
            Map<String, Double> lsParams = ls.getParameters();
            WindParameters lsWindParams = ls.getWindParameters();
            Map<Double, GaussianParameter> windSpeeds = lsWindParams.getWindSpeed();
            Map<Double, GaussianParameter> windDirections = lsWindParams.getWindDirection();

            data.append(launchSite.getLatitude()).append(",").append(launchSite.getLongitude()).append(","); //Launch site
            data.append(df.format(ls.distanceTo(launchSite))).append(","); //Distance from launch
            data.append(df.format(result.getMeanLandingSite().getLatitude())).append(",")
                    .append(df.format(result.getMeanLandingSite().getLongitude())).append(","); //Mean site
            data.append(ls.getLatitude()).append(",").append(ls.getLongitude()).append(","); //Landing site
            data.append(df.format(lsParams.get("rodAngle"))).append(","); //Launch rod angle
            data.append(df.format(lsParams.get("launchDirection"))).append(","); //Launch rod direction
            data.append(df.format(lsParams.get("pressure"))).append(","); //Air Pressure
            data.append(df.format(lsParams.get("temperature") - 273.15)).append(","); //Temperature

            if (ls.getLandingColour() == LandingSite.LandingColour.PARACHUTE_DEPLOYED) {
                data.append("true"); //Parachute did deploy
            } else if (ls.getLandingColour() == LandingSite.LandingColour.PARACHUTE_FAILED) {
                data.append("false"); //Parachute failed to deploy
            } else {
                data.append("n/a");
            }
            data.append(",");

            data.append(df.format(lsParams.get("parachuteTime"))).append(","); //Parachute Time
            data.append(df.format(lsParams.get("motorPerformance"))).append(","); //Motor Performance

            data.append(df.format(altitudeStep)).append(","); //Altitude Step

            double step = 0.0;
            for (int i = 0; i < 10; i++) {
                if (windSpeeds.containsKey(step)) {
                    GaussianParameter gaussian = windSpeeds.get(step);
                    data.append(df.format(gaussian.getMean())).append(","); //Speed
                } else {
                    data.append("ERROR,");
                }

                step += altitudeStep;
            }
            step = 0.0;

            data.append(df.format(lsWindParams.getSpeedStdDev())).append(","); //Wind speed std dev

            for (int i = 0; i < 10; i++) {
                if (windDirections.containsKey(step)) {
                    GaussianParameter gaussian = windDirections.get(step);
                    data.append(df.format(gaussian.getMean())).append(","); //Direction
                } else {
                    data.append("ERROR,");
                }

                step += altitudeStep;
            }

            data.append(df.format(lsWindParams.getDirectionStdDev())); //Wind direction std dev

            data.append("\n");
        }

        return data.toString();
    }
}

