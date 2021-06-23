import net.sf.openrocket.document.Simulation;
import nz.ac.vuw.ecs.ormontecarlosimplugin.extend.OrSimulationExtension;
import nz.ac.vuw.ecs.ormontecarlosimplugin.gui.components.input.ImportPanel;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;


public class CsvTests {
    /**
     * Test all CSV data inputs.
     */
    @Test
    public void testImport() {
        OrSimulationExtension extension = new OrSimulationExtension();
        Simulation simulation = TestUtil.testSetup(extension);

        File csvFile;
        try {
            csvFile = new File("./src/test/resources/testCSV.csv");
            ImportPanel.csvImport(simulation, extension, csvFile);
        } catch (IOException e) {
            fail();
        }

        assertEquals(5, simulation.getOptions().getLaunchLatitude(), 0);
        assertEquals(5, simulation.getOptions().getLaunchLongitude(), 0);

        assertEquals(0.5, extension.getWindAltitudeStep(), 0);

        assertEquals(1, extension.getWindSpeedAltitudes().get(0.0), 0);
        assertEquals(2, extension.getWindSpeedAltitudes().get(0.5), 0);
        assertEquals(3, extension.getWindSpeedAltitudes().get(1.0), 0);
        assertEquals(4, extension.getWindSpeedAltitudes().get(1.5), 0);
        assertEquals(5, extension.getWindSpeedAltitudes().get(2.0), 0);
        assertEquals(6, extension.getWindSpeedAltitudes().get(2.5), 0);
        assertEquals(7, extension.getWindSpeedAltitudes().get(3.0), 0);
        assertEquals(8, extension.getWindSpeedAltitudes().get(3.5), 0);
        assertEquals(9, extension.getWindSpeedAltitudes().get(4.0), 0);
        assertEquals(10, extension.getWindSpeedAltitudes().get(4.5), 0);
        assertEquals(0.5, simulation.getOptions().getWindSpeedDeviation(), 0);

        assertEquals(0, extension.getWindDirectionAltitudes().get(0.0), 0);
        assertEquals(10, extension.getWindDirectionAltitudes().get(0.5), 0);
        assertEquals(20, extension.getWindDirectionAltitudes().get(1.0), 0);
        assertEquals(30, extension.getWindDirectionAltitudes().get(1.5), 0);
        assertEquals(40, extension.getWindDirectionAltitudes().get(2.0), 0);
        assertEquals(50, extension.getWindDirectionAltitudes().get(2.5), 0);
        assertEquals(60, extension.getWindDirectionAltitudes().get(3.0), 0);
        assertEquals(70, extension.getWindDirectionAltitudes().get(3.5), 0);
        assertEquals(80, extension.getWindDirectionAltitudes().get(4.0), 0);
        assertEquals(90, extension.getWindDirectionAltitudes().get(4.5), 0);
        assertEquals(5, extension.getWindDirectionStdDev(), 0);

        assertEquals(Math.toRadians(5), simulation.getOptions().getLaunchRodAngle(), 0);
        assertEquals(5, extension.getLaunchAngleStdDev(), 0);
        assertEquals(1, simulation.getOptions().getLaunchRodLength(), 0);

        assertFalse(simulation.getOptions().getLaunchIntoWind());
        assertEquals(Math.toRadians(5), simulation.getOptions().getLaunchRodDirection(), 0);
        assertEquals(5, extension.getLaunchDirectionStdDev(), 0);
        assertEquals(0, simulation.getOptions().getLaunchAltitude(), 0);

        assertEquals(20 + 273.15, simulation.getOptions().getLaunchTemperature(), 0);
        assertEquals(2, extension.getTemperatureStdDev(), 0);
        assertEquals(100000, simulation.getOptions().getLaunchPressure(), 0);
        assertEquals(1500, extension.getPressureStdDev(), 0);


        assertEquals(80, extension.getMotorPerformanceMean(), 0);
        assertEquals(5, extension.getMotorPerformanceStdDev(), 0);

        assertEquals(100, extension.getNumSims(), 0);
        assertEquals(0.5, extension.getTimeToOpenParachuteStdDev(), 0);
    }
}
