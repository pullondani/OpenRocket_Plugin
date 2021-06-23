import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import net.sf.openrocket.document.Simulation;
import net.sf.openrocket.plugin.PluginModule;
import net.sf.openrocket.rocketcomponent.Rocket;
import net.sf.openrocket.simulation.SimulationOptions;
import net.sf.openrocket.simulation.SimulationStatus;
import net.sf.openrocket.startup.Application;
import net.sf.openrocket.startup.GuiModule;
import net.sf.openrocket.util.Coordinate;
import net.sf.openrocket.util.TestRockets;
import net.sf.openrocket.util.WorldCoordinate;
import nz.ac.vuw.ecs.ormontecarlosimplugin.extend.OrSimulationExtension;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.LandingSite;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.OpenRocketMonteCarloParameters;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.OpenRocketMonteCarloSimulation;
import nz.ac.vuw.ecs.ormontecarlosimplugin.montecarlo.parameter.ParameterType;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Some util methods for running tests.
 */
public class TestUtil {


    /**
     * Get a mock landing site at a latitude and longitude (degrees).
     *
     * @param latitude  lat (deg)
     * @param longitude long (deg)
     * @return a landing site object
     */
    public static LandingSite getLandingSite(double latitude, double longitude) {
        SimulationStatus status = mock(SimulationStatus.class);
        WorldCoordinate coordinate = new WorldCoordinate(latitude, longitude, 0);

        when(status.getRocketWorldPosition()).thenReturn(coordinate);
        when(status.getRocketPosition()).thenReturn(new Coordinate(0, 0));

        LandingSite site = new LandingSite(null, null, status, LandingSite.LandingColour.PARACHUTE_DEPLOYED);

        return site;
    }

    /**
     * Create the rocket and simulation.
     *
     * @param orSimulationExtension Plugin To add to the simulation
     * @return Simulation with rocket and plugin
     */
    public static Simulation testSetup(OrSimulationExtension orSimulationExtension) {
        GuiModule guiModule = new GuiModule();
        Module pluginModule = new PluginModule();
        Injector injector = Guice.createInjector(guiModule, pluginModule);
        Application.setInjector(injector);

        Rocket rocket = TestRockets.makeSmallFlyable();
        SimulationOptions simulationOptions = new SimulationOptions(rocket);

        simulationOptions.setMotorConfigurationID(rocket.getFlightConfigurationIDs()[1]);

        return new Simulation(rocket, Simulation.Status.NOT_SIMULATED, "name", simulationOptions,
                Collections.singletonList(orSimulationExtension), null);
    }

    /**
     * Creates the Monte Carlo simulation.
     *
     * @param simulation            Simulation to repeatedly test
     * @param orSimulationExtension Extension , containing extra launch parameters like deviation
     * @return OpenRocketMonteCarloSimulation monteCarlo simulation of the openRocket simulation.
     */
    public static OpenRocketMonteCarloSimulation createMonteCarloSimulation(Simulation simulation, OrSimulationExtension orSimulationExtension) {
        assertNotNull(simulation.getSimulationExtensions().get(0));
        OpenRocketMonteCarloParameters parameters = new OpenRocketMonteCarloParameters(simulation, orSimulationExtension);
        return new OpenRocketMonteCarloSimulation(parameters);
    }

    /**
     * Run a simulation with given parameters.
     *
     * @param parameters HashMap< String, Double> Simulation variables to change
     * @return Collection of LandingSites collection of landing sites from simulation
     */
    public static Collection<LandingSite> test(Map<String, Double> parameters) {
        OrSimulationExtension simExtension = new OrSimulationExtension();
        double simNum = parameters.getOrDefault(ParameterType.SIM_NUMBER.getName(), ParameterType.SIM_NUMBER.getDefaultMean());
        simExtension.setNumSims((int) simNum);
        simExtension.setMotorPerformanceMean(parameters.getOrDefault(ParameterType.MOTOR_PERFORMANCE.getName(),
                ParameterType.MOTOR_PERFORMANCE.getDefaultMean()));
        simExtension.setLaunchAngleStdDev(Math.toRadians(parameters.getOrDefault(ParameterType.LAUNCH_ANGLE.getStandardDeviationName(), 0.0)));
        simExtension.setLaunchDirectionStdDev(Math.toRadians(parameters.getOrDefault(ParameterType.LAUNCH_DIRECTION.getStandardDeviationName(), 0.0)));
        simExtension.setTimeToOpenParachuteStdDev(parameters.getOrDefault(ParameterType.PARACHUTE_TIME.getStandardDeviationName(), 0.0));
        simExtension.setMotorPerformanceStdDev(parameters.getOrDefault(ParameterType.MOTOR_PERFORMANCE.getStandardDeviationName(), 0.0));
        simExtension.setWindDirectionStdDev(Math.toRadians(parameters.getOrDefault(ParameterType.WIND_DIRECTION.getStandardDeviationName(), 0.0)));
        simExtension.setPressureStdDev(parameters.getOrDefault(ParameterType.PRESSURE.getStandardDeviationName(), 0.0) * 100.0);
        simExtension.setTemperatureStdDev(parameters.getOrDefault(ParameterType.TEMPERATURE.getStandardDeviationName(), 0.0));

        Simulation sim = testSetup(simExtension);
        SimulationOptions simOptions = sim.getOptions();

        // Variable states of the simulation to be tested.
        simOptions.setLaunchLatitude(parameters.getOrDefault(ParameterType.LAUNCH_LAT.getName(), 0.0));
        simOptions.setLaunchLongitude(parameters.getOrDefault(ParameterType.LAUNCH_LONG.getName(), 0.0));
        simOptions.setWindSpeedAverage(parameters.getOrDefault(ParameterType.WIND_SPEED.getName(), 0.0));
        simOptions.setWindDirection(Math.toRadians(parameters.getOrDefault(ParameterType.WIND_DIRECTION.getName(), 0.0)));
        simOptions.setWindSpeedDeviation(parameters.getOrDefault(ParameterType.WIND_SPEED.getStandardDeviationName(), 0.0));
        simOptions.setLaunchRodAngle(Math.toRadians(parameters.getOrDefault(ParameterType.LAUNCH_ANGLE.getName(), 0.0)));
        simOptions.setLaunchRodDirection(Math.toRadians(parameters.getOrDefault(ParameterType.LAUNCH_DIRECTION.getName(), 0.0)));
        simOptions.setLaunchPressure(parameters.getOrDefault(ParameterType.PRESSURE.getName(), ParameterType.PRESSURE.getDefaultMean()) * 100.0);
        simOptions.setLaunchTemperature(parameters.getOrDefault(ParameterType.TEMPERATURE.getName(), ParameterType.TEMPERATURE.getDefaultMean()) + 273.15);
        OpenRocketMonteCarloSimulation monteCarloSim = createMonteCarloSimulation(sim, simExtension);
        return monteCarloSim.monteCarloSim();
    }
}
