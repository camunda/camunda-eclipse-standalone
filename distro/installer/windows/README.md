# Installer for Windows

This folder contains a [NSIS 3](http://nsis.sourceforge.net/) script that generates an installation binary for the the camunda Modeler on Windows.


## Features

The notable installer features are

* display license
* choose installation folder (including reinstall)
* create start menu entry
* link modeler to *.bpmn files
* uninstall


## Build Installer

Get [NSIS 3](http://nsis.sourceforge.net/) and install it to `NSIS_HOME`. 

To generate the installer script execute 

```
$NSIS_HOME/makensis /DSRC_DIR=path/to/modeler/files /DVER_MAJOR=1 /DVER_MINOR=0 camundaModeler.nsi
```