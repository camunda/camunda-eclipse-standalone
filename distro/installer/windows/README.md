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

Execute the `build-installers.bat` in this directory to generate 32/64 bit installers for Windows. This creates and copies the installers to `./installer/`.


### Version the Installer

Pass a [semver](http://semver.org/) as an arguments to the installer to version it.

```
build-installers.bat 1.0.0-SNAPSHOT
--> writes modeler-1.0.0-SNAPSHOT-setup.x86(_64).exe
```