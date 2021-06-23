import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import net.sf.openrocket.document.Simulation;
import net.sf.openrocket.plugin.PluginModule;
import net.sf.openrocket.rocketcomponent.Rocket;
import net.sf.openrocket.simulation.SimulationOptions;
import net.sf.openrocket.startup.Application;
import net.sf.openrocket.startup.GuiModule;
import net.sf.openrocket.startup.SwingStartup;
import net.sf.openrocket.util.TestRockets;
import nz.ac.vuw.ecs.ormontecarlosimplugin.extend.OrSimulationExtension;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.OpenRocketMonteCarloParameters;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.OpenRocketMonteCarloSimulation;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Performance tests test that our programs meets the performance requirements.
 *
 * @author Thomas
 */
public class PerformanceTests {
    @Before
    public void removeLogging() {
        SwingStartup.initializeLogging();
    }

    /**
     * UTIL METHOD to set up an Open Rocket Simulation.
     *
     * @param orSimulationExtension An extension object used to create a Simulation.
     * @return The new Simulation.
     */
    private Simulation setUp(OrSimulationExtension orSimulationExtension) {
        GuiModule guiModule = new GuiModule();
        Module pluginModule = new PluginModule();
        Injector injector = Guice.createInjector(guiModule, pluginModule);
        Application.setInjector(injector);
        Rocket rocket = TestRockets.makeSmallFlyable();
        SimulationOptions simulationOptions = new SimulationOptions(rocket);
        simulationOptions.setMotorConfigurationID(rocket.getFlightConfigurationIDs()[1]);

        return new Simulation(rocket, Simulation.Status.NOT_SIMULATED, "Test",
                simulationOptions, Collections.singletonList(orSimulationExtension),
                null);
    }


    /**
     * This tests that the parallel implementation runs faster. This method works.
     */
    @Test
    public void testParallelImplementation() {
        OpenRocketMonteCarloSimulation simulation = getSimulation(1000);

        long time = System.currentTimeMillis();

        simulation.monteCarloSim();

        long sequentialTime = System.currentTimeMillis() - time;

        time = System.currentTimeMillis();

        simulation.parallelMonteCarloSim();

        long parallelTime = System.currentTimeMillis() - time;

        assertTrue(parallelTime < sequentialTime);

        System.out.println("Time to execute 1,000 simulations sequentially: " + sequentialTime + "ms");
        System.out.println("Time to execute 1,000 simulations in parallel: " + parallelTime + "ms");
    }


    /**
     * Create a new simulation to test from.
     *
     * @param numSims number of simulations
     * @return simulation
     */
    private OpenRocketMonteCarloSimulation getSimulation(int numSims) {

        OrSimulationExtension simExtension = new OrSimulationExtension();
        simExtension.setNumSims(numSims);
        simExtension.setLaunchAngleStdDev(0);
        simExtension.setLaunchDirectionStdDev(0);
        simExtension.setTimeToOpenParachuteStdDev(0);
        simExtension.setMotorPerformanceMean(100);
        simExtension.setMotorPerformanceStdDev(0);
        simExtension.setWindDirectionStdDev(0);
        simExtension.setPressureStdDev(0);
        simExtension.setTemperatureStdDev(0);

        Simulation sim = setUp(simExtension);
        SimulationOptions simOptions = sim.getOptions();
        simOptions.setWindSpeedDeviation(0);
        simOptions.setLaunchRodAngle(0);

        // Variable states of the simulation to be tested.
        simOptions.setLaunchLatitude(90);
        simOptions.setLaunchLongitude(0);
        simOptions.setWindSpeedAverage(2);
        simOptions.setWindDirection(0);
        assertNotNull(sim.getSimulationExtensions().get(0));
        OpenRocketMonteCarloParameters params = new OpenRocketMonteCarloParameters(sim,
                simExtension);
        OpenRocketMonteCarloSimulation monteCarloSim = new OpenRocketMonteCarloSimulation(params);
        return monteCarloSim;
    }
}
