# Installer for Windows

This folder contains a [NSIS 3](http://nsis.sourceforge.net/) script that generates an installation binary for the the camunda Modeler on Windows.


## Features

The installer has the following features

* displays / makes you accept EPL license
* lets you choose installation folder (including reinstall)
* creates a start menu entry
* links modeler to `*.bpmn` files
* generates uninstaller and links to installed programs list


## Build Installer

Get [NSIS 3](http://nsis.sourceforge.net/) and install it. Expose the install directory through the `NSIS_HOME` variable.

Create the modeler artifacts via `mvn clean verify` on the projects root directory (`../../../`).

Execute the `build-installers.bat` in this directory to generate 32/64 bit installers for Windows. This creates and copies the installers to `./installer/` as well as to `../../../org.camunda.bpm.modeler.standalone.product/target/products/`.


### Version the Installer

Pass major and minor version as arguments to the installer to generate a specific version.

```
build-installers.bat 1 2
--> writes modeler(32|64)-1.2.0-setup.exe
```