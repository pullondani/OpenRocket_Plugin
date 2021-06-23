# ENGR 301: Project Requirements document

---

<div style="page-break-after: always;"></div>

# ENGR 301 *Open Rocket Simulation Plugin Project* Project Proposal and Requirements Document
#### Author list: Thomas Page, Harrison Cook, Naveen Bandarage, Logan Brantley, Daniel Pullon, Lily Fahey

## 1. Introduction

### Client

Andre Geldenhuis. Email: andre.geldenhuis@vuw.ac.nz


### 1.1 Purpose

The purpose of this project is to develop a landing site evaluation add on feature for the open source software "OpenRocket".

### 1.2 Scope

The software to be produced is called OR Simulation plugin. This will act as a model rocket landing site simulator. This piece of software will act as an plugin to the open source software "OpenRocket".
In doing so, it will add the specified functionality based off the pre-exisiting software capabilities. OpenRocket allows a user to design a model rocket, and simulate its flight path.
This software will thus utilize this simulation code, and add a crude Monte Carlo simulation to identify a scatter plot graph of potential landing sites.
Additionally, the scope includes the designing and implementation of a PID controller and motor gimbaling.
This product is aimed to aid casual model rocket enthusiasts to safely launch their rocket with a clear understanding of where the rocket could land. In doing so, random environmental
and varying effects during flight are pre-determined, allowing the user to identify a safe launching site in the case of some failure, or discrepency.

### 1.3 Product overview
#### 1.3.1 Product perspective

The major component that the OR Simulation Plugin will be interacting with is OpenRocket. This section will detail the interfaces between OR Simulation Plugin and OpenRocket

**System interfaces**
The interfaces are user interfaces and software interfaces.

**User interfaces**
The user interface that is to be implemented will be built into OpenRocket using plugins.

There are two main user interfaces. One interface is for adjusting the parameters for a Monte Carlo simulation while the other interface is for containing the results of the simulation.

The requirements for the parameter selection is that it is built into OR as an extension. Extensions can be added by Pressing the “Add extension” button in the simulation options, and selecting the extension. The extension parameter window should then popup. In this parameter window the user interface must allow the user to enter parameters for the plugin, such as a standard deviation for the launch angle of the rocket. A “Simulate” button is not needed, as this is already built into OR’s user interface.

The requirements for the simulation results is that it must contain a scatter plot of likely landing locations, the scatter plot must be detailed enough for users to be able to determine if the flight is safe. The user interface must also show the user an upwind rocket vector and PID parameters that minimises the distance from the launch site.

**Hardware interfaces**
This project is purely software, so the hardware to interface with should be monitor, keyboard and mouse.

**Software interfaces**
OpenRocket
Mnemonic: net.sf.openrocket
Specification Number: (Unknown)
Version Number: 15.03
Source: https://github.com/openrocket/openrocket

OR Simulation Plugin interfaces with OpenRocket as a Java Jar plugin. It is important to interface with OpenRocket as that is the program this plugin is for. It is required to have a class that extends AbstractSimulationExtensionProvider annotated as @Plugin. This will tell OpenRocket what class to use for the extension. This extension must extend AbstractSimulationExtension. Finally, there must be another class annotated as @Plugin, but this extends AbstractSwingSimulationExtensionConfigurator. This class is responsible for providing the user interface that lets the users choose parameters.

**Communication interfaces**
The design will utilise user choice as such the users operating system will suffice as the communication layer between outside programs and the OpenRocket Simulation plugin project. This choice was made such that the compatibility with external components/programs is not hindered by the operating systems file structure or other operating system constraints. The output of this communication is via the transferral of a csv file containing: information about each landing site, the parameters for each of the corresponding landing sites. This design decision was made such that most external programs can work and manipulate universal .csv file format. Likewise, (for the consistency of the program) the incoming communication is transferred by the same means (operating system file structure). The design decision for the incoming data (by csv format) is due to the program's ability to read this data in such a way that allocates for missing or invalid parameter key terms by which these values run via their default implementations.   

**Memory**
Memory constraints are limited to the machine the OpenRocket program is running on. It is expected that they have at least 4GB of RAM.

**Operations**
The operations a user is expected to do:
* Build a rocket
* Create a simulation
* Add the ORSimulationPlugin extension
* Edit the simulation’s parameters
* Run the simulation

The user can also save their work to a file and open it later. This feature is already built into OpenRocket, but our extension must utilise this functionality such that the extension’s parameters can be saved/loaded.

**Site adaptation requirements**
It is required that the system can run on different machines. OpenRocket already supports Windows, MacOS and Linux so it is expected that ORSimulation Plugin must support these operating systems as well.

#### 1.3.2 Product functions

Use cases that apply to a minimum viable product.

**Plugin installation**
The ORSimulation plugin should allow for easy installation into the OpenRocket application. The user needs to first run OpenRocket and create or load a rocket. Then the user needs to create a simulation and edit the configurations of the simulation to include the JAR file that is the ORSimulation plugin.

Uninstalling the ORSimulation plugin is equally easy as the user only needs to edit the simulation configuration and remove the ORSimulation plugin from the plugins list.

**Run a Monte Carlo simulation**
This is the main goal of the project, to run a monte carlo simulation. This will run multiple rocket launches with different parameters. The number of simulations is configurable and the output can be displayed.

**Vary a variety of parameters**
Within the monte carlo simulation, it is expected a lot of parameters will be altered slightly. Each parameter has a mean and standard deviation, and a new value is calculated for each iteration of the monte carlo simulation. These parameters should include launch angle, wind speed, wind direction, launch direction, temperature, pressure, motor performance, parachute ejection time, motor burn out time, drag coefficient, etc.

**Parameter input**
Most of the parameter input will involve setting standard deviations for different parameters that OpenRocket already accounts for, such as launch angle. OpenRocket simulation plugin should allow this to be configured.

**Scatterplot display**
On a successful run, what the user will see is a scatter plot based on the simulation that they have created. The launch site will be shown for reference on how far the rocket travels.

**Scatterplot scale**
The scatterplot should display a scale in units a user can understand so that the distance between points can be easily understood.

**Display a mean and standard deviation**
The scatterplot should contain a mean landing site, with a standard deviation that most sites will land within. The standard deviation can be calculated as an ellipse that surrounds the mean landing site.

**Display landing site details**
Each landing site should have information associated with it. This includes how far away from the launch site and mean landing site, along with information such as latitude/longitude and the parameters used.

#### 1.3.3 User characteristics   

One page identifying the main classes of users and their characteristics (9.5.5)

The main users of the product are casual hobbyists. They will use the product to get a better understanding of what a possible rocket will do before having to build it. This will allow for a greater level of experimentation while also decreasing the risk of encountering catastrophic errors in the real world.

A casual hobbyist is someone who does rocketry in their spare time. They are not doing rocketry at a commercial level. A hobbyist is someone who does the hobby for the enjoyment rather than a financial gain. A casual hobbyist is a sub-category that is even more focussed on leisure, this means that the aim for the program will be to cater to someone who wishes to use a lightweight program that is as simple as possible for the requirements that they desire. The program needs to cater to this demand by ensuring that it is not overly complicated and has a low barrier to entry.

Another characteristic is because this product is a plugin for OpenRocket the users will be users of OpenRocket, therefore this plugin should keep a similar design language to OpenRocket so that it doesn't appear jarring and unfamiliar to the average user.

#### 1.3.4 Limitations

One page on the limitations on the product (9.5.6)

The inputs will be limited by what OpenRocket can handle as well as the information that will be provided by the misssion control groups. Unless a consensus is achieved and all groups provide the same information, we will be limited in that regard. The simulation is also limited in terms of accuracy based on the data we are provided. Most importantly we are limited in terms of the degree of realism, no simulation can ever be entirely "realistic". Therfore a degree a variability is to be expected in our results.

Time constraints with students and differing time schedules makes formally arranging meetings between teams incredibly hard. Due to this, integreation between all mission control softwares and this Open Rocket Simulation Plugin project is hindered. Without a standardized means of communication, we lack an ability to create a hardcoded and automated system, for fear that an alternative operating systems or project root location could potentially block any means of communication. A plausible fix for this limitation (of time and standardized communication) is to enable mission control teams to save a csv file in any location on the computer, and thus enable the ability to access said file in the Simulation via user input. The supplier's of this information can thus implement any means of saving this data as a csv file and our program will be able to find it (with user aid).

## 2. References

References to other documents or standards. Follow the IEEE Citation  Reference scheme, available from the [IEEE website](https://www.ieee.org/) (please use the search box). (1 page, longer if required)

## 3. Specific requirements  

20 pages outlining the requirements of the system. You should apportion these pages across the following subsections to focus on the most important parts of your product.

### 3.1 External interfaces

For our simulation software to be effective, there are three crucial interfaces that are required to ensure that the software meets it's specification. OpenRocket, Input and Output external interfaces will ensure that this add-on simulation will perform the required task and fulfill its specification.

#### 3.1.1) OpenRocket:
The entire simulation is a plugin for a larger program, OpenRocket. This is the first external interface worth mentioning, as without the OpenRocket software, this project has no ground to run off. The OpenRocket software generates a simulation of it's own. By it's design, it is a precise simulation. Meaning, given the exact construction of a rocket and the exact launching parameters, the simulation given by the OpenRocket software will give a detailed view of how the flight path will look. This is not the same simulation we are wanting to recreate, however, the software's user interface will provide some necessary inputs for our simulation later down the track.
  - __*Rocket design and Motors & configuration:*__
  Upon launching OpenRocket, you are greeted immediately with the Rocket design user interface (external interface) which will provide the user a means in order to create a rocket to their specifications. This removes the need for our plugin to do the same, as the program will already have the data about the rocket accessible to the plugin. The motors & configuration tab allows the user to modify the motor they have used with their specific setup. This information is also, readily available to the plugin inside the internal interface.
  <br><br>
  - __*Flight simulations:*__
  Here the user can specify a new OpenRocket simulation, however, inside the simulation settings is where our plugin comes into play. The flight simulations "edit simulation" panel has another two tabs which (as part of the OpenRocket external interface) are crucial to ensure the plugin can run.
  <br><br>
    - *Launch conditions:* This interface is responsible for all the input variations for an OpenRocket simulation. These inputs are also needed for the Monte Carlo simulations of our product. Thus, this interface is automatically the starting point of our Monte Carlo simulation for inputs.
  <br><br>
    - *Simulation options:* This interface allows for the user to add and run this project. Without the simulation options tab, our plugin becomes inaccessible. Thus, it is a crucial external interface for this project.

#### 3.1.2) Inputs:
Listed below are a series of input variables on the plugin. These variables are not explicitly stated within the OpenRocket software, and thus, this input GUI is important, as it allows the user to manipulate the inner workings of the Simulation. These inputs can be provided via a CSV file which can be selected through the plugin GUI.
<br>

- __*Wind Direction Standard Deviation*__
    -  Purpose: This enables the user to simulate under varying wind speeds close to the current wind speed.  
    -  Source: Sourced from the user.
    -  Range: 0-180
    -  Units: Degrees
    -  Timing: Decided by the user.
    -  Relationships: It directly affects the wind direction parameter which is implemented in OpenRocket source.  
    -  Input Format: JSpinner which only accepts digits.
  <br><br>

- __*Pressure Standard Deviation*__
    -  Purpose: This enables the user to simulate under different pressure conditions.  
    -  Source: Sourced from the user.
    -  Range: 0-100
    -  Units: mBar
    -  Timing: Decided by the user.
    -  Relationships: It directly affects the pressure parameter which is implmented in OpenRocket source.
    -  Input Format: JSpinner which only accepts digits.
    <br><br>

- __*Temperature Standard Deviation*__
    -  Purpose: This enables the user to simulate under different temperature conditions.
    -  Source: Sourced from the user.
    -  Range: 0-5
    -  Units: Degrees Celsius
    -  Timing: Decided by the user.
    -  Relationships: It directly affects the temperature parameter which is implmented in OpenRocket source.
    -  Input Format: JSpinner which only accepts digits.
    <br><br>

- __*Launch Angle Standard Deviation*__
    -  Purpose: This enables the user to simulate different launch angles.
    -  Source: Sourced from the user.
    -  Range: 0-90
    -  Units: Degrees
    -  Timing: Decided by the user.
    -  Relationships: It directly affects the launch angle parameter which is implmented in OpenRocket source.
    -  Input Format: JSpinner which only accepts digits
    <br><br>

- __*Launch Direction Standard Deviation*__
    -  Purpose: This enables the user to simulate different launch directions.
    -  Source: Sourced from the user.
    -  Range: 0-90
    -  Units: Degrees
    -  Timing: Decided by the user.
    -  Relationships: It directly affects the launch direction parameter which is implmented in OpenRocket source.
    -  Input Format: JSpinner which only accepts digits
    <br><br>

- __*Parachute Deployment Time Standard Deviation*__
    -  Purpose: This enables the user to simulate different parachute deployment times.
    -  Source: Sourced from the user.
    -  Range: 0-10
    -  Units: Seconds.
    -  Timing: Decided by the user.
    -  Relationships: It directly affects the parachute rocket component which the user defines in OpenRocket source.  
    -  Input Format: JSpinner which only accepts digits
    <br><br>

- __*Motor Performance Mean*__
    -  Purpose: This enables the user to define a mean for motor performance.
    -  Source: Sourced from the user.
    -  Range: 20-100
    -  Units: Percentage.
    -  Timing: Decided by the user.
    -  Relationships: This is the mean value used by the motor performance standard deviation input. It directly affects the motor component which the user defines in OpenRocket source.  
    -  Input Format: JSpinner which only accepts digits
    <br><br>

- __*Motor Performance Standard Deviation*__
    -  Purpose: This enables the user to simulate varying motor performance.
    -  Source: Sourced from the user.
    -  Range: 0-50
    -  Units: Percentage.
    -  Timing: Decided by the user.
    -  Relationships: This utilises the motor performance mean input. It directly affects the motor component which the user defines in OpenRocket source.   
    -  Input Format: JSpinner which only accepts digits
    <br><br>

- __*Use PID*__
    -  Purpose: Toggle whether PID is used in the simulation
    -  Source: From user
    -  Timing: Decided by the user.
    -  Relationships: Sets the P, I and D constants to 0
    -  Input Format: Tick box
    <br><br>

- __*P constant*__
    -  Purpose: Proportional constant for the PID controller
    -  Source: Default value or sourced from user
    -  Range: 0-1
    -  Units: Double
    -  Timing: Decided by the user.
    -  Relationships: Unused if the Use PID isn't checked
    -  Input Format: JSpinner which only accepts doubles
    <br><br>

-__*I constant*__
    -  Purpose: Integral constant for the PID controller
    -  Source: Default value or sourced from user
    -  Range: 0-1
    -  Units: Double
    -  Timing: Decided by the user.
    -  Relationships: Unused if the Use PID isn't checked
    -  Input Format: JSpinner which only accepts doubles
    <br><br>

- __*D constant*__
    -  Purpose: Derivative constant for the PID controller
    -  Source: Default value or sourced from user
    -  Range: 0-1
    -  Units: Double
    -  Timing: Decided by the user.
    -  Relationships: Unused if the Use PID isn't checked
    -  Input Format: JSpinner which only accepts doubles
    <br><br>

- __*Wind altitude values*__
    -  Purpose: Sets the number of differing wind altitudes
    -  Source: Sourced from user
    -  Range: 1-Many
    -  Units: Number
    -  Timing: Decided by the user.
    -  Relationships: Determines how many wind speed and direction inputs there will be, works inconjuction with the wind step values
    -  Input Format: JSpinner
    <br><br>

- __*Wind altitude step*__
    -  Purpose: Sets the size of the step for each individual wind altitudes
    -  Source: Sourced from user
    -  Range: 1-Many
    -  Units: Km
    -  Timing: Decided by the user.
    -  Relationships: Determines how quickly the wind alitude values increase, works inconjuction with the wind altitude values
    -  Input Format: JSpinner
    <br><br>

- __*Wind speed*__
    -  Purpose: Wind speed at each altitude
    -  Source: From the user and created by the Wind Altitude Values input
    -  Range: 0-Many
    -  Units: m/s
    -  Timing: Decided by the user.
    -  Relationships: Has a corresponding wind direction input that together determine each of the wind speeds at differing altitudes
    -  Input Format: JSpinner
    <br><br>    

- __*Wind direction*__
    -  Purpose: Wind direction at each altitude
    -  Source: From the user and created by the Wind Altitude Values input
    -  Range: 0-359
    -  Units: degrees
    -  Timing: Decided by the user.
    -  Relationships: Has a corresponding wind speed input that together determine each of the wind speeds at differing altitudes
    -  Input Format: JSpinner
    <br><br> 

- __*Template for adding more inputs*__
    -  Purpose: Description
    -  Source: Source of input
    -  Range: The range of inputs we are allwowing
    -  Units: The unit for it.
    -  Timing: Upto the user.
    -  Relationships: Relationsips to other inputs
    -  Input Format: Talking about the spinner
    <br><br>

#### 3.1.3) Outputs:
 <br>
 Outputs are displayed to the user via a display panel which contains an interactive scatter plot graph. This interactive scatter plot graph allows the user to click on each calculated landing site and provides the user with information on the selected site. This output can also be exported to a CSV file, to be used by an external system.
 <br>
- __*Launch Site*__
    -  Purpose: This indicates where the rocket launched from
    -  Source: Sourced from OpenRocket input parameters
    -  Range: Valid latitude and longitude coordinates
    -  Units: Lat-Long
    -  Timing: After all simulations are completed
    -  Relationships: All landing sites originate from the launch site using varying inputs
    -  Output Format: Point on a coordinate graph, and lat-long coordinates are displayed numerically
<br><br>

- __*Distance between landing site and launch-pad*__
    -  Purpose: This shows the distance to a specific landing site from the launch site, relative to all other landing sites
    -  Source: Sourced from calculating the distance between the location of the launch site and landing site
    -  Range: Valid latitude and longitude coordinates
    -  Units: Metres
    -  Timing: After all simulations are completed
    -  Relationships: Related to the launch site and a specific landing site
    -  Output Format: Displayed as a numerical value
<br><br>

- __*Landing Site Longitude*__
    -  Purpose: To show the longitude of a specific landing site
    -  Source: Sourced from the results of a specific simulation
    -  Range: Valid longitude coordinate
    -  Units: Degrees
    -  Timing: After all simulations are completed
    -  Relationships: Related to a specific landing site and corresponding latitude
    -  Output Format: Displayed as a numerical value
<br><br>

- __*Landing Site Latitude*__
    -  Purpose: To show the latitude of a specific landing site
    -  Source: Sourced from the results of a specific simulation
    -  Range: Valid latitude coordinate
    -  Units: Degrees
    -  Timing: After all simulations are completed
    -  Relationships: Related to a specific landing site and corresponding longitude
    -  Output Format: Displayed as a numerical value
<br><br>

- __*Landing Site Standard Deviation*__
    -  Purpose: To give a statistical standard deviation that shows how far each landing site is from the launch site
    -  Source: All of the landing sites data and then calculated based on their locations relative to the launch site
    -  Range: > 0
    -  Units: Metres
    -  Timing: After all simulations are completed
    -  Relationships: Related to mean of all landing site locations and the launch site
    -  Output Format: Displayed as a circular line drawn over the output
<br><br>

- __*Mean Landing Location*__
    -  Purpose: To give a statistical mean that shows how far each landing site is from the launch site
    -  Source: All of the landing sites data and then calculated based on their locations relative to the launch site
    -  Range: Valid longitude and latitude
    -  Units: Longitude and latitude coordinates
    -  Timing: After all simulations are completed
    -  Relationships: Related to all landing site locations and the launch site
    -  Output Format: Displayed as a circular line drawn over the output
<br><br>

- __*Launch Rod Direction*__
    -  Purpose: To show the direction of the rod at the time of the rocket launch
    -  Source: Randomised from user input
    -  Range: 0-360
    -  Units: Degrees
    -  Timing: After all simulations are completed
    -  Relationships: Related to a specific landing site location
    -  Output Format: Displayed on the information panel once the landing point is selected
<br><br>

- __*Launch Rod Angle*__
    -  Purpose: To show the angle of the rod at the time of the rocket launch
    -  Source: Randomised from user input
    -  Range: -90 - 90
    -  Units: Degrees
    -  Timing: After all simulations are completed
    -  Relationships: Related to a specific landing site location
    -  Output Format: Displayed on the information panel once the landing point is selected
<br><br>

- __*Wind Angle*__
    -  Purpose: To show the angle of the wind at the time of the rocket launch
    -  Source: Randomised from user input
    -  Range: 0-360
    -  Units: Degrees
    -  Timing: After all simulations are completed
    -  Relationships: Related to a specific landing site location
    -  Output Format: Displayed on the information panel once the landing point is selected
<br><br>

- __*Air Pressure*__
    -  Purpose: To show the current air pressure at the time of the rocket launch
    -  Source: Randomised from user input
    -  Range: >0
    -  Units: Pascals
    -  Timing: After all simulations are completed
    -  Relationships: Related to a specific landing site location
    -  Output Format: Displayed on the information panel once the landing point is selected
<br><br>

- __*Temperature*__
    -  Purpose: To show the current temperature at the time of the rocket launch
    -  Source: Randomised from user input
    -  Range: >-270
    -  Units: Degrees Celsius
    -  Timing: After all simulations are completed
    -  Relationships: Related to a specific landing site location
    -  Output Format: Displayed on the information panel once the landing point is selected
<br><br>

- __*Parachute Deployment Delay*__
    -  Purpose: Specifies the extra time it took for the parachute to deploy when compared with standard operation 
    -  Source: Based on normal deployment time and then randomised through user inputted standard deviation
    -  Range: Any
    -  Units: Time in seconds
    -  Timing: Calculated during the simulation
    -  Relationships: Related to a specific landing site location
    -  Output Format: Displayed on the information panel once the landing point is selected
<br><br>

- __*Parachute Deployment*__
    -  Purpose: Specifies whether the parachute deploy or failed to deploy
    -  Source: Each launch has a point with parameters that either the parachute deployed or failed
    -  Range: True - False
    -  Units: Boolean
    -  Timing: Specified before the simulation
    -  Relationships: Related to a specific pair of landing site locations
    -  Output Format: Displayed on the information panel once the landing point is selected
<br><br>

- __*Motor Performance*__
    -  Purpose: Specifies the current maximum performance of the motor
    -  Source: Randomised from user input of Motor Performance mean and standard deviation
    -  Range: 0-100
    -  Units: A percentage
    -  Timing: Calculated for each landing site
    -  Relationships: Related to a specific landing site location
    -  Output Format: Displayed on the information panel once the landing point is selected
<br><br>

### 3.2 Functions
#### 10 Most Important Functions
**Open Rocket Plugin** - *MVP*

Goal: The goal of this use case is that the user can utilise this plugin within Open Rocket simulations.

This plugin should allow for easy installation into the OpenRocket application. The user needs to first run OpenRocket and create or load a rocket. Then the user needs to create a simulation and edit the configurations of the simulation to include the JAR file that is the ORSimulation plugin.

Uninstalling the ORSimulation plugin is equally easy as the user only needs to edit the simulation configuration and remove the ORSimulation plugin from the plugins list.

**Run Monte Carlo simulation** - *MVP*

Goal: The goal of this use case is for the user to run multiple simulations with slight variation.

This plugin should enable the user to run multiple OpenRocket simulations with slight variations to launch parameters. The user should be able to configure the number of simulations run, and the output of these multiple simulations should be displayed together.

**Randomise parameters** - *MVP*

Goal: The goal of this use case is for the user to be able define the extent of parameter variation.

Within the monte carlo simulation, it is expected a lot of parameters will be altered slightly. Each parameter has a mean and standard deviation, and a new value is calculated for each iteration of the monte carlo simulation. These parameters should include launch angle, wind speed, wind direction, launch direction, temperature, pressure, motor performance, parachute ejection time, motor burn out time, drag coefficient, etc.

The user should be able to set standard deviations for most parameters, some of which OpenRocket already accounts for, and some of which this plugin will add functionality for. The user should be able to set the mean for certain parameters, such as motor thrust performance.

**Import/export data**

Goal: The goal of this use case is to streamline integration with Mission Control teams' software products.

Parameter data should be able to be imported into this plugin via CSV file. This file could be sourced from a Mission Control software product or written manually.

Monte Carlo simulation results should be able to be exported from this plugin via CSV file. This file will contain all relevant result information for Mission Control software products.

**Graph results on a scatterplot graph** - *MVP*

Goal: The goal of this use case is to display all landing site results on one graph.

This plugin should display all results of the Monte Carlo simulation on a single scatterplot graph, accompanied by the general simulation configuration and the specific launch configuration for any landing site, which the user can specify by selecting a landing site. The scatterplot graph should scale in understandable units so that the distance between landing sites can easily be understood.

**Display landing site details** - *MVP*

Goal: The goal of this use case is to display the details for a selected landing site to the user.

This plugin should display the launch configuration details for a user-selected landing site in an information panel. These details should include the parameter values that resulted in the selected landing site.

**Display general simulation details** - *MVP*

Goal: The goal of this use case is to display the general details for every simulation.

This plugin should display the general Monte Carlo configuration details in an information panel. These details includes parameters such as the number of simulations run.

**Run simulations with PID**

Goal: The goal of this use case is for the user to be able to run Monte Carlo simulations with simulated PID gimble motors.

This plugin should allow for a PID rocket gimble extension. The extension will simulate a rocket with a PID controller that will help keep the rocket pointed in a specific direction.

The PID rocket gimble will have some parameters that need to be tuned depending on the design of the rocket. An extension that works out the best set of parameters, such as using machine learning will need to be implemented. This should minimise the distace to the launch site.

**Display mean of results** - *MVP*

Goal: The goal of this use case is to display the mean of the landing sites.

This plugin should calculate and display a mean landing site, which is at the average latitude and longitude of all landing sites whose simulations had parachutes that deployed correctly (i.e. simulation was not to show parachute failure).

**Display standard deviation of results** - *MVP*

Goal: The goal of this use case is to display the standard deviation of the landing sites.

This plugin should calculate and display the standard deviation of all the landing sites whose simulations had parachutes that deployed correctly. This will be displayed as an ellipse centered on the mean landing site.

#### Other Functions
**Display ideal upwind rocket vector**

This plugin will be able to identify the most ideal launch configuration out of the resulting landing sites. This ideal configuration should be displayed to the user on the results screen.

**Run simulations with varying windspeeds at different altitudes**

The user should be able to configure wind speeds to vary at different altitudes. These varying wind speeds should be displayed by altitude for each resulting landing site.

### 3.3 Usability Requirements

* The launch site should be clearly marked on the scatter plot.
* The user must be able to tell from a glance whether or not the rocket is safe to fly from the scatter plot.
* All the information must fit on the screen, minimum resolution 720p. With readable text.
* The user must be able to understand what each parameter does without doing any prior knowledge.
* The user should know what units each parameter is in.
* The user must be able to clearly see what points are anomalies and which points are to be expected (or close to the standard deviation).
* The user must be able to work out what situation would lead to a point landing in an anomaly position.
* All interfaces should be able to resize, making the scatterplot higher resolution for users with higher resolution screens.
* Points where the parachute have failed should be clearly marked and should be obvious to the user.
* The user should be able to go back and change the simulation parameters easily.
* You should be able to zoom in and out of the scatter plot.


### 3.4 Performance requirements

See 9.5.13. for most systems this will be around one page. Hardware projects also see section 9.4.6.

Notes to expand upon as the numerical information becomes known:
- Amount/type of information to be handled (static)
- Number of simulations within a time-period (dynamic)
- Number of changing variables within one run of the simulation (dynamic)

Topics to be covered in 3.4

**Response Time**
How long will it take to plot the data?
Depending on the number of simulations the data may take an extended period of time to plot and display. If the number of simulations is too large then it may become overwhelming for the user's system to display all of the information at once. 
For more reasonable simulation numbers it displays rapidly and in many cases under 1.0 second. Most of the waiting time is from running the actual simulation.

**Workload**
The expectation to be able to receive data from Mission Control and run 1000 simulations in under 1 minute. The output of this data should then display on a Monte Carlo simulation graph and do so in under 10 seconds.

Each of the 1000 simulations will contain many variables as specified by the user. 
These variables include but are not limited to: 
    - Wind speed
    - Launch angle
    - Launch direction
    and many others as stated in section 3.1.2.
    
Our system is expected to handle a simulation where all of these variables are considered and do so in approximately 1 minute.

The system should also calculate the ideal upwind rocket vector. This will be another process that may take longer than a minute to complete. Because this operation takes so long, it should be optional to the user whether the upwind rocket vector should be calculated.

**Scalability**
The information will predominantly consist of situational data such as wind speed, and the launch angle for example.

How much data is processed in a given space of time?
The amount of data is scalable. The number of simulations is varied and therefore for each execution the amount of data can change.

How many variables change within one run of the simulation?
Each variable can be tweaked and so can the standard deviation of each. This gives a large range of values and possibilities for the data.

How many simulations can be run per hour?
Simulations run quickly and currently can be executed in under a second. The upper bound on the number of simulations that can run is not set and therefore the limit is only determined by the user's system.

**Platform**
The software requires a modern system, produced in the approximately the previous 10 years. The amount of data that can be handled at once will be limited by the capabilities of the machine running the program. We can assume the machine it is running on to have at least 4GB of RAM and a functional CPU.

The system is to support one terminal and one user at a time. 

### 3.5 Logical database requirements

See 9.5.14. for most systems, a focus on d) and e) is appropriate, such as an object-oriented domain analysis. You should provide an overview domain model (e.g.  a UML class diagram of approximately ten classes) and write a brief description of the responsibilities of each class in the model (3 pages).
You should use right tools, preferabley PlantUML, to draw your URL diagrams which can be easily embedded into a Mardown file (PlantUML is also supported by GitLab and Foswiki).

(Notes to expand upon as the information becomes known:)<br>
a) numerical data of variables<br>
b) most data is used every run of simulation<br>
c) requires access to OpenRocket system<br>
d) a data entity could be the result of a single run of the simulation and the variable values that created the result; determine their relationship!<br>
e) the integrity of the result of a flight simulation must be sound, while the integrity of the variable data is important but not paramount (i.e. 1 or 2 could be lost, but too many lost results in useless simulation result)<br>
f) the results of the previous simulations must be kept temporarily, giving the user the option to permanently save the information<br>



##### Processes Diagram
![ProcessDiagram](imgs/ProcessDiagram3.png)

##### Logical Diagram
![LogicalDiagram](imgs/LogicalDiagram5.png)

##### Description of classes
##### OrSimulationExtension
Extends the openrocket abstract simulation extension. Contains getters and setters for values used in the Monte Carlo simulation. 

##### PluginListener
Responsible for listening to the start of the simulation and running the adjusted Monte Carlo simulation. Extends OpenRocket's abstract simulation Listener with an event for the simulation starting.

##### PluginProvider
Extends OpenRocket's abstract simulation extension provider. Used to give OpenRocket the OrSimulationExtension class. 

##### EnviromentPanel
Swing Panel containing input parameters for the enviroment e.g. wind direction. 

##### GeneralPanel
Swing panel containing input parameters for general Monte Carlo settings e.g. number of simulations. 

##### ImportPanel
Swing panel containing button to import data from a csv to set simulation parameters. 

##### LaunchPanel
Swing panel containing inputs for setting launch configurations e.g. launch angle. 

##### PidPanel
Swing panel containing inputs for the PID controller. The PID controller is used for rocket guidence during flight. 

##### WindAltitudeGui
Swing dialog containing inputs for wind speed and directions at multiple altitudes. This is accessed via the EnviromentPanel. 

##### LandingSiteInformationPanel
Swing panel showing information about a selected landing site. This displays information like the launches wind speed, angle etc. Also displays information about the launch site if launch is selected.

##### MapViewerPanel
Map viewer that is displayed on the output gui next to other output panels like LandingSiteInformationPanel. This panel shows a map and the different landing site points as a dotplot over the map. These points can be selected which will update the landing site information panel. 

##### SimulationInformationPanel
Swing panel that displays static information about the Monte Carlo simulation parameters before they are randomized. This is to show the user information about values like the starting launch rod angle. 

##### SwingLandingSite
An extension of a waypoint within the map. This contains the landing site that this waypoint represents and options to be selected by the user. 

##### SwingLandingSiteOverlayPainter
An extension of a waypoint painter within the map. This paints the waypoints onto the map with the correct possitioning. Contains calculations to convert the latitude and longitude to points on the screen. 

##### FontUtil
Utility class for getting fonts

##### PluginConfigurator
Extension of OpenRocket's Swing simulation extension configurator. This is responsible for creating the GUI for the Plugin. (Both input and output)

##### SimulationOutputGui
A Swing Dialogue that contains the output gui components (e.g. landing site information panel)

##### AbstractHillClimb
An abstract hill climbing operation. (Finding a minimum). The method is implemented in the doSimulation abstract method. This method tests 7 different points (-s, -0.2s, -0.04s, 0, +0.04s, +0.2s, +s) on each step. This method works well if you make the initial step value large (pick largest reasonable value you can think of).
The AbstractHillClimb is responsible for finding an ideal minimun using machine learning. 

##### LaunchVectorHillClimb
An extension of AbstractHillClimb. It runs multiple simulations where different parameters are changed to find the ideal value for each parameter that will result in a landing site closest to the launch site. 

##### GausianParameter
Represents a parameter that is randomly distributed. An extension of a RandomParameter. Contains a method for finding the next value based on the parameters set value and the standard deviation. 

##### ParameterType
Represents each of the parameter types that are used in the Monte Carlo simulation. Contains each parameter type and attributes for that parameter e.g. default minimum, max deviation

##### RandomParameter
An Abstract class that represents a parameter that can be randomised. Has a method to obtain the next random value.

##### UniformParameter
A uniformly Distributed parameter. This is an extension of RandomParameter. Contains method to get next value. 

##### AbstractMonteCarlo
An abstract class representing a Monte Carlo simulation. Recieves the parameters to run the simulation with and provides methods to run the simulation parallel or single threaded. 

##### LandingSite 
Represents a world coordinate of where one of the rocket simulations landed. Landing sites are colored and store what the parameters for that simulation were.

##### LandingSiteSimulation
A singular simulation that will return a landing site. It requires a simulation which will be duplicated and randomised. This clone simulation will be run and the results are stored.

##### MonteCarloException
Monte carlo exception occurs when there is an exception during a monte carlo simulation.

##### MonteCarloResult
Contains details about the result of the monte carlo simulation. Calculates the standard deviation elipse, mean landing site, and all the landing sites from the Monte Carlo simulation. 

##### OpenRocketMonteCarloParameters
Gets information from the parameters set in the extension to create some random parameters. 

##### OpenRocketMonteCarloSimulation
An open rocket monte carlo simulation. This creates multiple LandingSiteSimulations and runs them. The result to be returned is a landing site. 

##### WindAltitudeModel
Implements the wind altitude model for the simulation. Requires the wind speed and direction for a given amount of altitudes. 

##### WindParameters
Some wind parameters per altitude. This randomises the data for the wind speed and direction based on the deviations. 

##### Controller
Represents a PID controller. Is constructed with given P, I, D, constants and contains a method for stepping the PID controller. This will return a value based on current value (which is used to calculate an error for the controller).

##### OrientationUtils
Utility class for quaternions and eulers. Contains methods for converting eulers and quaternions and creating rotation quaternions from axis angles. 

##### OpenRocketStarter
Launches OpenRocket

##### Use Case Diagram
![UseCaseDiragram](imgs/UseCaseDiagram2.png)


### 3.6 Design constraints

see 9.5.15 and 9.5.16. for most systems, this will be around one page.

> 9.5.15 Design constraints<br>
> Specify constraints on the system design imposed by external standards, regulatory requirements, or project limitations.
>
> 9.5.16 Standards compliance<br>
> Specify the requirements derived from existing standards or regulations, including:
>
> a) Report format;<br>
> b) Data naming;<br>
> c) Accounting procedures;<br>
> d) Audit tracing.
>
> For example, this could specify the requirement for software to trace processing activity. Such traces are needed for some applications to meet minimum regulatory or financial standards. An audit trace requirement may, for example, state that all changes to a payroll database shall be recorded in a trace file with before and after values.

Constraints:
The ORSimulation is limited to being a plugin instead of being its own application. This is due to the project limitation that the ORSimulation must interact with OpenRocket, therefore necessitating it as a plugin.

We are restricted on the capacity of the machine that can be used to run the ORSimulation. Due to the software needing to be able to be run on site in a remote location for rocket launches, a large machine may not be viable. Therefore the ORSimulation should be able to run on lower end systems.

The ORSimulation is limited to drawing rocket flight paths into two-dimensional spaces. This is a project limitation to reduce scope creep.

The ORSimulation is constrained to being coded in java, JDK 1.8.

The ORSimulation will comply with Google's style scheme. 

The ORSimulation PID controller will not control any rocket launches other than the simulations. 


### 3.7 Nonfunctional system attributes

Present the systemic (aka nonfunctional) requirements of the product (see ISO/IEC 25010).
List up to twenty systemic requirements / attributes.
Write a short natural language description of the top nonfunctional requirements (approx. five pages).

The ORSimulation Plugin will be easy to operate. Functionality should be clear and users will have access to help tools to aid their understanding.

The ORSimulation Plugin will be safe to operate. It will not cause physical, emotional, or psychological harm to users. This will be achieved by having the ORSimulation only accessible via a terminal and not have loud noises or flashing lights.

The ORSimulation Plugin will be unable to physically harm the user. The program will only require physical interaction via a keyboard and mouse or trackpad. Users will not have to make many repeated actions over long periods of time that could cause the user strain. Additionally the ORSimulation will not cause excessively loud noises or have flashing lights.

The ORSimulation Plugin will not send or access private information. It will not store users information on the internet.

The ORSimulation Plugin will conform to Object Oriented patterns.

The ORSimulation Plugin will be constructed of abstract classes and interfaces to increase maintainability.

The ORSimulation Plugin should be able to interact with other OpenRocket plugins. It will not prevent other plugins from operating.

The ORSimulation Plugin should be able to run on a typical laptop, one with a somewhat recent processor and 4GB of RAM minimum.

The ORSimulation Plugin should be able to run on multiple operating systems.

Users can implement ORSimulation Plugin directly into OpenRocket easily by dragging and dropping a jar file.


### 3.8 Physical and Environmental Requirements

This System requires a computer, or laptop with java runtime 1.8. The computer requires 4 GB of ram. Any reasonable device from within the last 10 years is required. The device will need to have a battery lifetime sufficient for a trip to a remote launch location. This required battery lifetime will be dependent on how far away the launch site is.
This system requires the enviroment to not be hazardous or dangerous.
This sytem does not require radio or internet. Mission Control software which this plugin is dependant on may require internet or radio access.

### 3.9 Supporting information

see 9.5.19.

## 4. Verification

3 pages outlining how you will verify that the product meets the most important specific requirements. The format of this section should parallel section 3 of your document (see 9.5.18). Wherever possible (especially systemic requirements) you should indicate testable acceptance criteria.

NOTE - Discuss whether CI/CD is relevant in the verification process eg. verifying that our code is satisfactory

A crucial part of any new software is testing that it functions as expected and safely. Outlined in subsection 3 is an overview for this projects intended functionality. However, the intended functionality needs to be verified to ensure that it works.

- __*Working Safely:*__
  This is a small section of verification, however it should not be overlooked. This project needs to give it's users information that is accurate or worst case. The program is designed to aid in identifying the safety of a launch site and parameters. That means this project needs to promise the user that the information given to them will identify the worst cases along with all other cases. This is important. The user needs to know if their is a possibility of their rocket being a safety risk. This project should employ a false-positive approach. Where, landing sites that are not realistic but are reachable with input parameters should almost always be given to the user as a plausible landing site. Giving the user the authority to make the decision of if the input data is valid or not. To verify that this works, several approaches can be taken, however, as of yet none are implemented. The first viable option is automated testing methodologies.<br><br>
  Given the share amount of inputs given to the simulation, automated testing provides the developers an easier means to testing all worst case outputs are shown. However, automated testing may prove to be slow, and an in the field test would not be viable. Thus, an alternative approach is to use unit testing methods to locate all worst possible outputs from the given inputs. Verifying that worst case outputs are displayed to the user is not a specific requirement to the projects functionality, however, a specific requirement to the developers ethical obligation to developing software like the one designed in this project.
  <br><br>
- __*Unit Testing:*__
  This project is developed in Java. Java offers developers the JUnit library, a means of programmatically testing code. We will be utilizing this tool in order to ensure that the program is working as expected. Most of the JUnit testing will be done on inputs, ensuring that their impact in the Monte Carlo simulation is being implemented correctly. Most of these tests will be conducted with the following specifications.
<br><br>
    - *ONLY ONE:* To ensure that we are testing an input correctly, we must also ensure that all other inputs have no effect. This will often come in the form of placing all other variables with a value of 0. Meaning that in a given calculation, the only effecting parameter is the one which we are testing.
  <br><br>
    - *OUTPUT:* Once we have isolated some input variable, we then must test that it's output information is coherent with the input information. For straight forward parameters (wind speed) this means that we test that the given output is an EXACT match on where we wanted to place it. For other variables (standard deviations) this comes in the form of testing for a boundary. For example, if we were testing wind speed standard deviation, then we would check all outputs are within the bounds of the maximum and minimum values for the given standard deviation.
  <br><br>
    - *MORE THAN ONCE:* To really verify that the program is behaving as expected, the tests being run should be done more than once, particularly for those inputs values that have a wide range of data. This is standard practice for testing methods. Proving it works once can be a one-off occurrence. To truly verify that the software works as expected, then the software must be run in as many ways as possible to prove that in any case (or as many as possible) it will deliver reliable results.
  <br><br>
- __*User Interface:*__
  This project has a large dependency on the GUI. The input GUI allows the user to easily alter the way in which the Monte Carlo simulation is conducted. However, the most crucial user interface is the output GUI. The output GUI is going to be the stage where the user is deciding whether or not to launch based on the internal analysis of the Monte Carlo simulation. This output GUI can not be tested by a machine, as there is not currently any tools or means of defining what makes a good user interface based off reading the code. Here, we are required to run and use the interface by ourselves. Better yet, we will need to pull in some external users, likely the client, to use these GUIs. Their feedback will give us an indication on a verification into how well we have design a user friendly program.
<br><br>
- __*CI/CD Pipeline:*__
  CI/CD Pipelines offer two services that will become immediately helpful into verifying that the code is satisfactory. Although most of the testing is done by the JUnit library, what the CI/CD pipeline allows for is an overlook into how well new code integrates into the program. A new commit may break something else where in the program, and the developer may not know. What CI/CD does in our project is fist, build and run an instance of the program. If it can be run, then there is no issue. Secondly, it runs the program and then runs the internal tests within the program. This is helpful, as JUnit tests (as expressed above) allow the developers to programmatically validate that their program is working as expected. Thus, by utilizing CI/CD pipelines, we are extending the usage of the Unit Testing listed above, validating and verifying that all commits and updates to the program are not breaking the program.


## 5. Development Schedule

### 5.1 Schedule

- Prototype. 20/5/2020.
	- Minimum Viable Product
	- Goals (based on client requirements):
		- Open Rocket plug-in
		- Monte Carlo simulations
		- Randomised parameters for Monte Carlo
		- Graph results of simulations on a scatter plot graph
<br><br>
- Alpha Version 21/6/2020.
	- Alpha release
	- Includes all basic client requirements
	- Goals (based on client requirements and requests):
		- Display landing site details
		- Display simulation details
		- Indicate mean landing site
		- Indicate standard deviation of all landing sites
<br><br>
- Beta Version 21/9/2020. Before first performance assessment of the second trimester.
	- Beta release
	- Includes all functionality of client requirements and requests
	- Goals (based on client requirements and requests):
		- Eliminate bugs from alpha version
		- Map tiling behind scatter plot
		- CSV import/export
		- PID
		- Ideal rocket vector
		- Wind variations by altitude
<br><br>
- Final Version 19/10/2020.
	- Final release
	- Polished version of Beta release
		- Polished user interface
		- Functionality is easy to utilise
	- Eliminate all known bugs
<br><br>
### 5.2 Budget and Procurement

#### 5.2.1 Budget

We do not have to buy any hardware for this project.

#### 5.2.2 Procurement

| Good/Service | Goal |
| ------ | ------ |
| OpenRocket | The plugin is built for OpenRocket. |
| Java 8 | The plugin is built using Java 8. |


### 5.3 Risks 

Project Risk #1 - Limited time due to outside influences<br>
Likelihood - Likely<br>
Impact - Will mean team members won’t have enough time to work on the project and deadlines won’t be met.
<br>
Mitigation Strategies - Team members will do appropriate time management and complete assignments as soon as they can. Team members should communicate when they have too many commitments so that the team can reschedule or adjust as soon as possible.
<br>

Project Risk #2 - Schedule flaws<br>
Likelihood - Likely<br>
Impact - We won’t meet our deadlines and the project we deliver will be substandard.<br>
Mitigation Strategies - We should set milestones and try to meet them. Rediscuss those milestones if they are infeasible. We will hold regular meetings with a reflection on the work we achieved for that milestone. <br>

Project Risk #3 - Requirement inflation / scope creep<br>
Likelihood - Likely<br>
Impact - More work will be required to achieve a larger scope. Quality and time can run out.<br>
Mitigation Strategies - Clearly define the scope at the start of the project, determine which features are unnecessary before implementing them. <br>

Project Risk #4 - Unclear specification<br>
Likelihood - Likely<br>
Impact - Work completed won’t fit the specifications and will need to be re-done, wasting time.<br>
Mitigation Strategies - Regularly have team meetings discussing questions on the specifications. Regularly ask the customer about these questions.<br>

Project Risk #5 - Doing a sub-standard job on a task<br>
Likelihood - Likely<br>
Impact - Milestones are not met and extra work needs to be done later.<br>
Mitigation Strategies - Team members will verify the teams work to ensure it is up to standard and make any amendments as soon as possible. Peer-programming.<br>

Project Risk #6 - Overdesigning features<br>
Likelihood - Likely <br>
Impact - Work load will increase, reducing the ability to meet deadlines.<br>
Mitigation Strategies - Team meetings will be held to discuss what the features should entail before working on them. Peer-programming.<br>

Project Risk #7 - Incompatibility with other team’s software<br>
Likelihood - Moderate<br>
Impact - The software cannot be used for its intended purpose, therefore not meeting the requirements.<br>
Mitigation Strategies - Meetings with other teams will be scheduled to discuss compatibility issues.<br>

Project Risk #8 - Lack of testing<br>
Likelihood - Moderate<br>
Impact - Work completed may not achieve specs and not be compatible with existing work<br>
Mitigation Strategies - Team members will be assigned to testing and will have the work verified by other team members.<br>

Project Risk #9 - Illness/sickness (EG: COVID-19)<br>
Likelihood - Moderate<br>
Impact - Will reduce the ability of team member to do work for a limited time.<br>
Mitigation Strategies - Team members will self isolate and wash hands regularly. We will do meetings on 
discord.<br>

Project Risk #10 - Poor team dynamics<br>
Likelihood - Unlikely<br>
Impact - Team members may not communicate well or complete work together on time and to standard.<br>
Mitigation Strategies - Run team-building exercises to build team comradery. Don’t get angry at each other.<br>

### 5.4 Health and Safety

##### Covid-19
The recovery for Covid-19 is between 2 and 6 weeks depending on severity. During this time productivity would be very low and there is chance of permident health complications. 
Self isolation and proper hygiene such as washing of hands and maintaining a 2 meter distance from others will greatly reduce the risk of exposure. Team members who do contract the virus should do work up to their own discretion. 

##### Computer related risks
Occupational Overuse Syndrome
A work pattern with repeated actions can lead to occupational overuse syndrome. Prolonged use of a keyboard and mouse can cause a repetitive strain injury. The effects of a repetitive strain injury and occupational overuse can lead to a range of conditions affecting the fingers, ands, arms, and shoulders. These conditions can be between mild pain and chronic muscle/tissue pain. Early signs of overuse include pain, swelling of the arms and hands, and numbness of hands. 
Occupational overuse can be prevented with a varied work pattern that includes appropriate breaks and posture changes. Being aware of the symptoms of occupational overuse and having breaks when they are experienced can prevent increased severity. 
Additionally, looking at a computer monitor for extended periods of time can result in strained eyes, headaches, and blurred distorted vision. These conditions can be combated by using a larger monitor, breaks, and moving your head and eyes instead of focusing on one location. 

##### Posture
Working with poor posture can result in joint and muscle pain, stress, and fatigue. Posture is impacted both on workspace equipment and physical interaction. If the workspace involves a chair it needs to be set up such that the user has back and upper arms perpendicular with the ground and forearms parallel with the ground and desk. The mouse should be on a flat surface with no wrist support. By having no wrist support the chances of carpel tunnel are reduced. The centre of the monitor should be directly across from the user’s eyes.  
By conforming to this setup, the chances of muscle and tissue pain are reduced. Additionally, exercises that work muscles will prevent pain and other conditions like occupational overuse syndrome. 

##### Cable management
Cables for devices should be grouped together when possible and moved away walking paths. 

##### Workstation risks
Monitors in the workstation should: have a clear image with no flicker, have adjustable brightness and contrast, and have text that is easily readable to prevent eye straining.
Lighting and noise levels should not be distracting or painful. Lighting should be controlled such that there is no glare or reflections. 

##### External Workplaces
The project will not require any work to be completed at external workplaces that require a permit.

##### Test Subjects
The project will not require any testing on human or animal subjects. 

#### 5.4.1 Safety Plans
Project requirements do not involve risk of death, serious harm, harm or injury


## 6. Appendices

### 6.1 Assumptions and dependencies 

Assumptions:
- We assume that the user is knowledgable in operating OpenRocket.<br>
- We assume that the user has Java installed on their computer. (min. Java 8)<br>
- We assume that the user is likely to run this plugin on a portable device (i.e. laptop) at a launch site or to take to a launch site.
- We assume that the user has a decent internet connection.<br>
<br><br>
- We assume that the user is capable of procuring the latitude and longitude coordinates of their launch site.<br>
- We assume that the user can measure the wind speed at their launch site.<br>
- We assume that the user can measure air pressure at their launch site.<br>
- We assume that the user can measure temperature at their launch site.<br>
- We assume that the user can measure their rocket's launch angle.<br>
- We assume that the user can measure their rocket's launch direction.<br>
- We assume that the user can measure the launch altitude of their launch site.
<br><br>
- We assume that the user knows the motor thrust performance of their rocket.<br>
- We assume that the user knows how long their rocket's parachute takes to open.<br>
- We assume that the user knows the motor thrust performance of their rocket.
<br><br>

Dependencies:
- Java (min. Java 8)<br>
- OpenRocket (version 15)<br>
- Mockito (req. for unit tests only)
<br><br>
### 6.2 Acronyms and abbreviations
- PID =  A proportional integral derivative controller<br>
- CSV = A comma separated values file<br>
- OR = OpenRocket<br>

## 7. Contributions

A one page statement of contributions, including a list of each member of the group and what they contributed to this document.

Logan Brantley

- Section 3.1
- Section 3.4
- Section 3.5
- Section 5
- Section 6

---
Thomas Page

- Section 1.1
- Section 1.2
- Section 1.3.2
- Section 3.7
- Section 3.8
- Section 3.2
- Section 3.3

---
Lily Fahey

- Section 5.4, 5.4.1
- Section 3.2
- Section 3.4
- Section 3.6
- Section 3.7

---
Naveen Bandarage
- Section 1.2
- Section 1.3.1
- Section 1.3.4
- Section 3.1
- Section 3.2
- Section 5
- Section 6
- Proofreading

---
Daniel Pullon
- Section 1.3.2
- Section 1.3.3
- Section 1.3.4
- Section 3.1

---
Harrison Cook
- Section 1.1
- Section 1.2
- Section 3.1
- Section 4

---
