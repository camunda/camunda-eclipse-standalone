# How to Contribute

Contributions to the project are welcome in terms of filed issues, feature suggestions as well as pull requests.


## File issues / feature suggestions

* Make sure to include a screenshot that describes your idea


## Make pull requests

Unless extensions are changed, pull requests should go to the [camunda Modeler](https://github.com/camunda/camunda-modeler) project. 
Make sure to [add an issue](./issues) in this project, too so that progress on the matter can be tracked.


# Working on the Project

Make sure you have [Java](http://www.oracle.com/technetwork/java/javase/downloads/index.html) installed and a running version of [Apache Maven](https://maven.apache.org/) on your path. 


## Setup

To get ready for development clone the relevant projects and configure your IDE.


### Clone Projects

Create a new `{root}` directory. From within that directory clone this project and the [camunda Modeler](https://github.com/camunda/camunda-modeler) into sub directories.

```
git clone git@github.com:camunda/camunda-modeler.git modeler
git clone git@github.com:camunda/camunda-modeler-standalone.git modeler.standalone
```

The result is a folder structure like the following:

```
{root}
  ├───modeler
  │   ├───org.camunda.bpm.modeler
  │   └───org.camunda.bpm.modeler.tests
  └───modeler.standalone
      ├───distro
      ├───org.camunda.bpm.modeler.standalone.bundle
      ├───org.camunda.bpm.modeler.standalone.parent
      └───org.camunda.bpm.modeler.standalone.product
```


### Setup Eclipse IDE

Download a fresh copy of [Eclipse Kepler](http://eclipse.org/downloads/) and follow the instructions on [how to install relevant development dependencies](https://github.com/camunda/camunda-modeler/blob/master/CONTRIBUTING.md#kepler).


### Grab Project dependencies

Execute `mvn clean verify` once to download project dependencies.


### Import Project into the IDE

Import maven projects from `{root}` directory. The required projects are: 

*   `org.camunda.bpm.modeler`
*   `org.camunda.bpm.modeler.test`
*   `org.camunda.bpm.modeler.standalone.bundle`
*   `org.camunda.bpm.modeler.standalone.product`


## Build the Distributions

From within the directory `{root}/modeler.standalone` execute

```
mvn clean verify
```

This creates the standalone modeler distributions in the folder `{root}/modeler.standalone/org.camunda.bpm.modeler.standalone.product/target/products`.

### Create installers (Windows)

To create installers from the distribution execute

```
cd {root}\distro\installer\windows
build-installers.bat
```

This adds 32 and 64 bit installers for windows to the products directory. Note that [NSIS 3](http://nsis.sourceforge.net/) and a Windows system are required to build the installer.