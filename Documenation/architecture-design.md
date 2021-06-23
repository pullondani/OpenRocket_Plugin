# ENGR 301: Architectural Design and Proof-of-Concept

## Proof-of-Concept
The proof of concept for this project was the MVP. Our MVP served to validate the architectural design of the product, while also mitigating the risks associated with the development stage. 

The validation that the MVP provided includes validation of build systems, Kanban boards/issue management, CI/CD deployment and team management.  

---

# ENGR 301 *Open Rocket Simulation Plugin Project* Architectural Design and Proof-of-Concept
#### Author list: Thomas Page, Harrison Cook, Naveen Bandarage, Logan Brantley, Daniel Pullon, Lily Fahey


## 1. Introduction

### Client

Andre Geldenhuis. 
Email: andre.geldenhuis@vuw.ac.nz

### 1.1 Purpose

The purpose of this project is to add functionality to the open source software, OpenRocket.
The functionality that we are going to add is Monte Carlo simulations to simulate plausible landing sites and to implement a PID controller.

### 1.2 Scope

The software to be produced is called O.R Simulation plugin. This will act as a model rocket landing site simulator. This piece of software will act as an plugin to the open source software "OpenRocket".
In doing so, it will add the specified functionality based off the pre-exisiting software capabilities. OpenRocket allows a user to design a model rocket, and simulate its flight path.
This software will utilize this simulation code, and add a crude Monte Carlo simulation to identify a scatter plot graph of potential landing sites.
Additionally, the scope includes the designing and implementation of a PID controller and motor gimbaling.
This product is aimed to aid casual model rocket enthusiasts to safely launch their rocket with a clear understanding of where the rocket could land. In doing so, random environmental
and varying effects during flight are pre-determined, allowing the user to identify a safe launching site in the case of some failure, or discrepency.

## 2. References

Other than IEEE standards for writing documentation (i.e. for Architecture Design & Project Requirements), no external documents or standards are referenced in the documentation of this project.

## 3. Architecture

### 3.1 Stakeholders

| Stakeholder type | People | 
| ------ | ------ | 
| Users | Rocket Hobbyists | 
| Operators | Rocket hobbyists | 
| Acquirers| Andre Geldenhuis |
| Suppliers | Lily Fahey, Thomas Page, Daniel Pullon, Harrison Cook, Logan Brantley, Naveen Bandarage |
| Owners | Open Source |
| Developers | Lily Fahey, Thomas Page, Daniel Pullon, Harrison Cook, Logan Brantley, Naveen Bandarage | 
| Builders | Lily Fahey, Thomas Page, Daniel Pullon, Harrison Cook, Logan Brantley, Naveen Bandarage | 
| Maintainers | Lily Fahey, Thomas Page, Daniel Pullon, Harrison Cook, Logan Brantley, Naveen Bandarage | 

| Concerns | Stakeholder type |
| ------ | ------ |
| Purpose of System | Users, Operators, Acquirers, Suppliers, Owners, Developers, Builders, Maintainers |
| Suitability of the architecture for achieving the system’s purposes | Developers, Builders, Maintainers |
| Feasibility of constructing and deploying the system | Acquirers, Supplier, Developers, Builders, Maintainers |
| Potential risks and impacts of the system to its stakeholders throughout its life cycle | Users, Operators, Suppliers, Owners, Developers, Builders, Maintainers |
| Maintainability and evolvability of the system | Developers, Builders, Maintainers |

### 3.2 Architectural Viewpoints

- Viewpoint: The development view of the system
	- Developer -> source code
	- It indicates software management
	- It describes system components via UML component diagram
	- Concerns:
		- The maintainability of the system
		- Avoiding cyclic dependencies 
		- Scalability of the system
		- Coherence of the system/source code
<br><br>
- Viewpoint: The process view of the system
	- Indicates dynamic aspects of the system
	- Explains system processes and their communication
	- Focuses on run-time behaviour of the system
	- Addresses concurrency, performance and scalability
	- It describes the process sequence via UML sequence diagram
	- Concerns:
		- The scalability of the architecture  of the system
		- A non streamlined user experience
		- System resources 
		- Performance
<br><br>
- Viewpoint: The use-case view of the system
	- User -> system
	- Illustrates the system architecture via use-cases
	- Identifies architecture elements
	- Validates architecture design
	- Serves as starting point for architecture testing
	- Concerns:
		- The purpose of the system
		- A non streamlined user experience
<br><br>
- Viewpoint: The logical view of the system
	- Indicates functionality that the system provides to end users
	- Illustrates relationships between functionalities via UML diagrams including class diagrams
	- Concerns:
		- The maintainability of the system
		- The functionality of the system
		- Coherence of the system
		- Scalability of the system
		- Sustainability of the system
<br><br>
- Viewpoint: The physical view of the system
	- System engineer -> system
	- Indicates topology of software components on the physical layer
	- Indicates physical connections between software components 
	- AKA deployment view
	- Illustrates deployment of the system via UML diagrams including deployment diagrams
	- Concerns:
		- Risks and impacts
		- Feasibility
<br><br>

### 4. Architectural Views

### 4.1 Logical
![LogicalDiagram](imgs/LogicalDiagram5.png)

The logical viewpoint is from the users. The users will interact with the program via the 'Add Extension' button, this brings up a GUI that allows you to tweak the input parameters for the simulation. When you are happy with the variables, you can select the 'Simulate and Plot' button. Once pressed this will display the scatter plot of the monte carlo simulation, which is calculated based on your inputs.

### 4.2 Development
![DevelopmentalDiagram](imgs/DevelopmentalDiagram4.png)

This is from the viewpoint of the programmers. The project is split between the simulation and the gui. 

The simulation is run through the SimulationExtension class, this class holds responsibility for the extension.

The GUI is split between several classes. The input parameters to be used by the monte carlo simulation are controlled by the plugin configurator class.
The plugin configurator class is the class that takes in the parameters given to it from the SimulationExtension. It then displays each of these variables, along with spinners for editing the parameters before running the simulation.
In order to display the output, the SimulationOutputGui class holds the panel for the ScatterPlotCanvas which shows a scatter plot of the monte carlo simulation.

### 4.3 Process
![ProcessDiagram](imgs/ProcessDiagram3.png)

The viewpoint of the process document is for users of the program. The process diagram gives a path that the user will follow when using the program.
Currently our plugin is opened by adding an extension in the OpenRocket software, once open the parameters can be tweaked to the users preference. Once the parameters are finalised the simulation can be run, after completion the program will then display a scatter plot of the monte carlo simulation.

### 4.4 Physical 
![PhysicalDiagram](imgs/PhysicalDiagram3.png)

The viewpoint is from a system engineers perspective. The main piece in the diagram is the ORSimulation, this is our program which is run on a system, this system brings all of the other systems together. The weather and map data is gathered from a server that is connected via an api call.
Testing environment is built from a server that is run from one of the developers computer which checks and tests the system before an update is deployed.
Mission control needs to be added when we discuss further with other groups.

### 4.5 Scenarios
![PhysicalDiagram](imgs/UseCaseDiagram2.png)

This viewpoint is based on each use case, therefore the perspective may be different depending on what is required by the user.

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

* Lily Fahey - 5.4, 5.4.1, 5.3, 3.1, 4.1, 4.4, 4.2
* Thomas Page - 5.1, 5.2, 5.3, 3.1, 1.2, 1.1, 3.2, 4.3, 4.2
* Naveen Bandarage - Proof-of-concept, 1.2, 1.3, 2, 3.1, 3.2, 4, 5.1, 5.2.2, 5.3, 6
* Harrison Cook - 1.2, 4.2
* Daniel Pullon - 4.0, 4.2, 4.3
* Logan Brantley - Proof-of-concept, 1.3, 2, 3.2, 4, 5.1, 5.2.2, 6

---