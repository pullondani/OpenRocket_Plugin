package nz.ac.vuw.ecs.ormontecarlosimplugin.gui.components.input;

import net.sf.openrocket.document.Simulation;
import net.sf.openrocket.simulation.SimulationOptions;
import nz.ac.vuw.ecs.ormontecarlosimplugin.extend.OrSimulationExtension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Panel containing CSV import button.
 */
public class ImportPanel extends JPanel {
    private JButton csvButton;
    private final Simulation simulation;

    /**
     * Create import panel.
     *
     * @param simulation simulation
     */
    public ImportPanel(Simulation simulation) {
        this.simulation = simulation;
        setLayout(new BorderLayout());
    }

    /**
     * Add CSV button.
     *
     * @param extension extension
     */
    public void setPanelComponents(OrSimulationExtension extension) {
        csvButton = new JButton("Import CSV data");

        csvButton.addActionListener(e -> {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            jfc.setDialogTitle("Select a CSV file to input the data");
            jfc.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv", "CSV");
            jfc.addChoosableFileFilter(filter);

            File csvFile;
            int returnValue = jfc.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                csvFile = jfc.getSelectedFile();
                try {
                    csvImport(simulation, extension, csvFile);
                } catch (IOException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            }
        });

        add(csvButton, BorderLayout.CENTER);
    }

    /**
     * Function to place data into OpenRocket and our plugin.
     * Data is imported from the supplied CSV
     *
     * @param extension  - Class to input our plugin data
     * @param simulation - Class to input data to OpenRocket
     */
    public static void csvImport(Simulation simulation, OrSimulationExtension extension, File csvFile) throws IOException {
        BufferedReader csvReader;
        csvReader = new BufferedReader(new FileReader(csvFile));
        String row;
        String[] firstRow = csvReader.readLine().split(",");
        SimulationOptions options = simulation.getOptions();

        double altitudeStep = 1.0;
        Double[] windSpeeds = {null, null, null, null, null, null, null, null, null, null};
        Double[] windDirections = {null, null, null, null, null, null, null, null, null, null};

        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            // do something with the data
            for (int i = 0; i < data.length; i++) {
                double value = Double.parseDouble(data[i]);

                switch (firstRow[i]) {
                    case "Launch Latitude": //Degrees
                        options.setLaunchLatitude(value);
                        break;
                    case "Launch Longitude": //Degrees
                        options.setLaunchLongitude(value);
                        break;
                    case "Launch Angle": //Degrees
                        double angleAsRads = Math.toRadians(value);
                        options.setLaunchRodAngle(angleAsRads);
                        break;
                    case "Launch Angle (Std Dev)": //Degrees
                        extension.setLaunchAngleStdDev(value);
                        break;
                    case "Launch Direction": //Degrees
                        options.setLaunchIntoWind(false);
                        double directionAsRads = Math.toRadians(value);
                        options.setLaunchRodDirection(directionAsRads);
                        break;
                    case "Launch Direction (Std Dev)": //Degrees
                        extension.setLaunchDirectionStdDev(value);
                        break;
                    case "Launch Altitude": //Metres
                        options.setLaunchAltitude(value);
                        break;
                    case "Temperature": //Degrees C (converted to K automatically)
                        options.setISAAtmosphere(false);
                        options.setLaunchTemperature(value + 273.15);
                        break;
                    case "Temperature (Std Dev)": //Degrees C
                        extension.setTemperatureStdDev(value);
                        break;
                    case "Pressure": //Pa
                        options.setISAAtmosphere(false);
                        options.setLaunchPressure(value);
                        break;
                    case "Pressure (Std Dev)": //Pa
                        extension.setPressureStdDev(value);
                        break;
                    case "Launch Rod Length": //Metres
                        options.setLaunchRodLength(value);
                        break;
                    case "Motor Thrust Performance (Mean)": //Percentage
                        extension.setMotorPerformanceMean(value);
                        break;
                    case "Motor Thrust Performance (Std Dev)": //Percentage
                        extension.setMotorPerformanceStdDev(value);
                        break;
                    case "Number of Simulations": //Int
                        extension.setNumSims(Integer.parseInt(data[i]));
                        break;
                    case "Parachute Deployment Time (Std Dev)": //Seconds
                        extension.setTimeToOpenParachuteStdDev(value);
                        break;
                    case "Wind Altitude Step": //Km
                        altitudeStep = value;
                        break;

                    case "Wind Speed Launch": //m/s
                        windSpeeds[0] = value;
                        break;
                    case "Wind Speed One": //m/s
                        windSpeeds[1] = value;
                        break;
                    case "Wind Speed Two": //m/s
                        windSpeeds[2] = value;
                        break;
                    case "Wind Speed Three": //m/s
                        windSpeeds[3] = value;
                        break;
                    case "Wind Speed Four": //m/s
                        windSpeeds[4] = value;
                        break;
                    case "Wind Speed Five": //m/s
                        windSpeeds[5] = value;
                        break;
                    case "Wind Speed Six": //m/s
                        windSpeeds[6] = value;
                        break;
                    case "Wind Speed Seven": //m/s
                        windSpeeds[7] = value;
                        break;
                    case "Wind Speed Eight": //m/s
                        windSpeeds[8] = value;
                        break;
                    case "Wind Speed Nine": //m/s
                        windSpeeds[9] = value;
                        break;
                    case "Wind Speed (Std Dev)": //m/s
                        options.setWindSpeedDeviation(value);
                        break;

                    case "Wind Direction Launch": //Degrees
                        windDirections[0] = value;
                        break;
                    case "Wind Direction One": //Degrees
                        windDirections[1] = value;
                        break;
                    case "Wind Direction Two": //Degrees
                        windDirections[2] = value;
                        break;
                    case "Wind Direction Three": //Degrees
                        windDirections[3] = value;
                        break;
                    case "Wind Direction Four": //Degrees
                        windDirections[4] = value;
                        break;
                    case "Wind Direction Five": //Degrees
                        windDirections[5] = value;
                        break;
                    case "Wind Direction Six": //Degrees
                        windDirections[6] = value;
                        break;
                    case "Wind Direction Seven": //Degrees
                        windDirections[7] = value;
                        break;
                    case "Wind Direction Eight": //Degrees
                        windDirections[8] = value;
                        break;
                    case "Wind Direction Nine": //Degrees
                        windDirections[9] = value;
                        break;
                    case "Wind Direction (Std Dev)": //Degrees
                        extension.setWindDirectionStdDev(value);
                        break;

                    default:
                        System.out.println("Unrecognized key term -> " + firstRow[i]);
                }
            }
        }
        csvReader.close();

        //Set number of altitudes for wind
        for (int i = 0; i < 10; i++) {
            if (windSpeeds[i] == null) {
                extension.setWindAltitudeValues(i);
                break;
            }
            if (windDirections[i] == null) {
                extension.setWindAltitudeValues(i);
                break;
            }

            if (i == 9) {
                extension.setWindAltitudeValues(10);
            }
        }
        extension.setWindAltitudeStep(altitudeStep);

        //Set wind speeds and directions
        double step = 0.0;
        for (int i = 0; i < 10; i++) {
            if (windSpeeds[i] != null) {
                extension.getWindSpeedAltitudes().put(step, windSpeeds[i]);
            }
            if (windDirections[i] != null) {
                extension.getWindDirectionAltitudes().put(step, windDirections[i]);
            }

            step += altitudeStep;
        }
    }

}
