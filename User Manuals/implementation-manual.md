## Purpose

The purpose of this document is to explain how developers can use our code to improve our plugin.

## Importing the project into IntelliJ

Please use at least the 2020 version of IntelliJ in order to do this.

- Open the ‘import’ dialog for IntelliJ
- Go to the location where the GitLab repository is stored on your computer
- Select the folder named 'OpenRocket-Simulation-Plugin'
- Import the project and let everything load appropriately
- You can check whether the project has been imported correctly by running 'src/main/java/nz.ac.vuw.ecs.ormontecarlosimplugin.OpenRocketStarter'

NOTE: ensure that 'src/main' and 'src/test' are set as modules in the project structure, and that your project's Java JDK is set to Java 8.

### Exporting the plugin as a JAR

You can export as a jar using the command "gradle jar".

Alternatively, using IntelliJ, you can export the plugin as a jar by going:

Project Structure -> Artifacts -> + Jar (empty)

Enter a name for the plugin (just name it something like ORSimulationPlugin).

Set the output this to your OpenRocket plugins directory.

### Running directly through intelliJ

You can run the plugin through OpenRocket directly without exporting by running nz.ac.vuw.ecs.ormontecarlosimplugin.OpenRocketStarter.

## Code Implementation

### extend

The extend package contains most of the hooks into OpenRocket's extension system.

**OrSimulationExtension** Contains all getters and setters for every extension parameter. This is the main class for the extension that Open Rocket uses.

**PluginListener** The plugin listener listens for when the user presses the "Simulate and plot" button. This is the main entry point to running the simulation.

**PluginProvider** You do not need to change this class. This is just a hook into Open Rocket.

### gui

The GUI package is for all GUI related items. There are two panels, the output GUI (SimulationOutputGui) and the input GUI (Plugin Configurator) each component is put separately into components/input and component/output respectively.

##### input

**PluginConfigurator** Note that this class is also a hooked directly into OpenRocket. The panel is created in a method called "getConfigurationComponent".

Other classes are contained within components/input and are mostly self-explanatory. Do note that every input field will update something within the extension itself, which is an instance of OrSimulationExtension. The way it is set up at the moment is to use either IntegerModel, BooleanModel, or DoubleModel from OpenRocket and put it into a JSpinner.

##### output

**SimulationOutputGui** This is the main panel that holds the output GUI. There are 3 panels: MapViewerPanel, SimulationInformationPanel and LandingSiteInformationPanel.

**MapViewerPanel** This extends a JXMapViewer. This creates a map with a scatter plot.

**SwingLandingSiteOverlayPainter** This is built into the MapViewerPanel and helps display all the details on the map. The code for drawing the standard deviational ellipse is here. This will also update the locations of clickable landing sites.

**SwingLandingSite** An instance of this represents one GUI representation of a landing site on the map.

**SimulationInformationPanel** Contains some details about the simulation.

**LandingSiteInformatitonPanel** Is updated when you click a landing site. Will display information about the landing site you clicked.

### montecarlo

This package handles most of the code for the Monte Carlo simulation. The framework for a Monte Carlo simulation is defined in **AbstractMonteCarlo** and the implementation is in the **OpenRocketMonteCarloSimulation** class. The simulation can be run by executing either monteCarloSim() or parallelMonteCarloSim().

To tweak our implementation of the Monte Carlo simulation, you can add new parameters and settings into the **OpenRocketMonteCarloParameters** class. An instance of OpenRocketMonteCarloSimulation will take an OpenRocketMonteCarloParameters instance. Modifying the OpenRocketMonteCarloParameters to handle new additions to OrSimulationExtension will allow you to add new variables to the simulation. Randomized parameters will take instances of RandomParameter which can be either GaussianParameter or UniformParameter (more types could easily be added by extension) these types are contained within the montecarlo/parameter package.

To change the effect that variables make on the simulation, you can do so by modifying **LandingSiteSimulation**. This takes a copy of an Open Rocket simulation and will adjust some of the parameters. This class has a built-in simulation listener too which handles additions like a PID controller and a thrust modifier. A more detailed output of the Monte Carlo simulation can be created by creating an instance of **MonteCarloResult** which will also calculate details like standard deviation and mean of the landing sites.

The output of the Monte Carlo simulation is a collection of **Landing Site** objects. Landing sites are defined by a latitude and longitude but also contain other details like how far east/north from the launch site and the parameters that it allowed it to get to that point. Any extra details to each landing site can be added here.

There is also a **WindAltitudeModel** class that changes the wind speed and direction at different altitudes. This extends an Open Rocket class called WindModel. The parameters for this are contained in an instance of **WindParameters**.


### idealparameter

This package defines a machine-learning algorithm to work out the ideal launch angle and direction for the rocket. An abstract algorithm is defined in AbstractHillClimb and the implementation is contained in LaunchVectorHillClimb.

### pid

**OrientationUtils** defines several methods to help deal with quaternions.

**Controller** defines an abstract PID controller to be used within the simulation.