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
git clone git@github.com:camunda/camunda-modeler.git camunda-modeler
git clone git@github.com:camunda/camunda-modeler-standalone.git camunda-modeler-standalone
```

The result is a folder structure like the following:

```
{root}
  ├───camunda-modeler
  │   ├───org.camunda.bpm.modeler
  │   └───org.camunda.bpm.modeler.tests
  └───camunda-modeler-standalone
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

Append `-Pwindows-distro` to build the Windows installers. This requires Windows machine, [NSIS 3](http://nsis.sourceforge.net/) and exposed `NSIS_HOME` variable. Read more about [how the installer is built](distro/installer/windows).

Append `-Plinix-distro` to build distributions for Linux and Mac OS X.


The generated distributions are put into the folder `{root}/camunda-modeler-standalone/target/modeler`.