# OpenRocket Simulation Plugin User Manual

### Intro

Hello User. This is the User Manual for the Simulation Plugin for OpenRocket, Designed by Thomas, Lily, Naveen, Logan, Daniel, and Harrison. Within this manual you will learn about the purpose of this plugin, its features, and how to use it effectively. This manual will assume that you already have OpenRocket version 15.3 or later installed and working. 



### Installation

You will receive a .jar file containing the plugin when you download it. This .jar file will need to be placed within OpenRocket's plugin folder. On Windows this is typically C:\Users\USERNAME\AppData\Roaming\OpenRocket\Plugins.

Once added to the plugins folder the plugin will also need to be added to flight simulations within OpenRocket. This is achieved with the following steps:

1. Start OpenRocket
2. Load the rocket you wish to simulate<br>
    a. You can use an ORK file if you wish<br>
    b. You can use the Apocalypse rocket by OpenRocket ("File -> Open example -> Apocalypse with decals")<br><br>
3. Select a flight simulation
4. Edit the flight simulation
5. Select Add extension within Simulation options
6. Select OR Simulation Plugin

This will add the plugin to that flight simulation and will display a GUI for you to interact with the plugin.



### Modifying Simulation Parameters

With the plugin loaded into a flight simulation, you can now change how the simulation is run and displayed. The purpose of changing the simulation is to account for environment variations that could effect the flight to provide accurate estimation on where the rocket will land after launch. This is achieved by running the simulation many times with with automatically varied parameters and recording where each simulation landed. 

For you the user, the simulation parameters will need to be set to be what you think the launch environment is and the standard deviation for some of your estimations. The slight variations of these parameters in each simulation will be handled by the plugin based on the standard deviation that you enter. 

Note on how the parameters to change are in both the Open Rocket edit simulation screen and in the plugin input screen. 



### Simulation Results

To view the results of the simulation, select the 'simulate and plot' option within OpenRocket's input GUI. This will take  some time to run the simulation and then display the results screen. The map covering most of the output screen shows the launch site, with a dot plot to visualize where the launch site and landing sites are. There is a green oval on the map behind the dots that shows the average landing site. Red dots display a landing site where the parachute failed to open. Blue dots display a landing site where the parachute did open. The black dot represents the launch site. 

The user can move around the map by clicking and dragging, and can zoom in and out with the scroll wheel. 

To the top right of the map is the Simulation Information panel. This displays the initial configuration of the simulation before deviations are taken into account. Underneath this panel is the Selected landing site panel. This panel displays information about a selected dot on the scatter plot, such as the motor performance. 



### Importing Simulation Parameters

On the plugin's parameter input screen there is a button to import csv data. Selecting this option will open a file chooser where you choose the csv file to load. Csv files that can be accepted should have 2 rows, where each column represents the name of the variable to set and the value to set. The parameter names that can be accepted are: "Launch Latitude", "Launch Longitude", "Launch Angle", "Launch Angle (Std Dev)", "Launch Direction", "Launch Direction (Std Dev)", "Launch Altitude", "Temperature", "Temperature (Std Dev)", "Pressure", "Pressure (Std Dev)", "Launch Rod Length", "Motor Thrust Performance (Mean)", "Motor Thrust Performance (Std Dev)", "Number of Simulations", "Parachute Deployment Time (Std Dev)", "Wind Altitude Step", "Wind Speed Launch", "Wind Speed One", "Wind Speed Two" ... "Wind Speed Nine", "Wind Speed (Std Dev)", "Wind Direction launch", "Wind Direction One" ... "Wind Direction Nine", "Wind Direction (Std Dev)". 

The csv file being imported doe not have to contain each of these parameter types, and any parameter types not specified will use the default value. Additionally not all of the 'Wind Direction ...' or 'Wind Speed ...' values need to be set. If you wish to have each Wind Direction / Speed parameter to have the same value then just set the Launch Value. 



### Exporting  Simulation Results

On the output display there is an option to export the simulation results to a csv file. Selecting this option will open a file selector allowing you to chose the destination of the results data. Upon clicking save the csv file will be placed in that file. The csv file contains columns for each of the simulation variables like temperature, and rows for each run of the simulation. 



### PID Controller

The PID controller is for simulating a rocket that has a guidance system. This can be anything from moving fins to a gimballing engine. The PID controller can be toggled by deselecting the checkbox in the input parameters GUI. You can also set the values to use for the PID constants. These constants change how the PID controller will react mid flight, and may result in better rocket guidance. Values for each constant should be between 0 and 1 where 0 is ignore that aspect of the controller. 