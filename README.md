#  *ShadowFunds Manager (SFM)*

## Table of contents
* [Description](#description)
* [Technologies](#technologies)
* [Key Features](#key-features)
* [Installation and Execution](#installation-and-execution-without-mavengradle)
* [Packaging](#packaging-the-application-with-launch4j)
* [Config](#backup-and-configuration)
* [Resources](#external-links-resources)
* [Contributors](#contributors)

## Description

**ShadowFunds Manager (SFM)** is a JavaFX application dedicated to managing funds, tracking financial inflows and 
outflows using dynamic charts to improve your finances. 
It also includes a **Task Manager** to help manage tasks and objectives, with visual priority indicators.

## Technologies
Project is created with:
* Java version 20.0.2
* JavaFX version 21.0.4
* html
* css

### Key Features:

- **Task Management**:
  - Tasks are color-coded based on priority.
  - Add tasks using the `+` and `-` keys when the tree view is focused.
  - To add a sub-task, select a parent task, then click `+`, or right-click and choose "add".
  - The "Add to TreeView" button allows tasks to be added directly to the root of the tree.

- **Report Generation**:
  - The **Report** button generates a monthly report in `.html` format, stored in the `report` folder.

- **Dynamic Charts**:
  - Charts adapt to user needs, including the **PC Period** chart, accessible via `More -> Charts -> PC Period`.
  - Double-click charts to enlarge them.

### Requirements

- **JavaFX 21**: This application is built using **JavaFX 21** for the user interface.
- **JDK 17** or later is required to run the project.

### Installation and Execution (Without Maven/Gradle)

* Information on installing, configuring, and running this program with an IDE is available on (Getting Started with JavaFX)[https://openjfx.io/openjfx-docs/] 
* Executable files stored in the folder `Releases`
* Jar files stored in `out\artifacts\SFM_jar`


### Packaging the Application with Launch4j

**Launch4j** is used to package the Java application into a Windows executable (`.exe`).

### Backup and Configuration

- The last saved state is automatically loaded at each startup.
- A default save file is available in the `Conf` folder.

### External Links (Resources)

- Icons(gradient): [Icons8](https://icons8.com)
- Logo: [Flaticon](https://www.flaticon.com)
- Launch4j: [Launch4j.sourceforge.net](https://launch4j.sourceforge.net/)

---

### Contributors

- [Tkb] - Lead Developer

--- 

> Enjoy the app. Feedbacks are welcome

```
while(alive)
{
    // eat(); 
    // sleep();  
    code();
}
```