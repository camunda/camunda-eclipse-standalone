> Looking for a modern BPMN 2.0 modeler? Try out the [Camunda Modeler](https://github.com/camunda/camunda-modeler).

# Camunde Eclipse Standalone

A standalone version of the [Camunda Eclipse Plug-in](https://github.com/camunda/camunda-eclipse-plugin). 
Use for seamless modeling of [BPMN](https://en.wikipedia.org/wiki/BPMN) process and collaboration diagrams.
Built with simplicity and modeling only in mind.

![Standalone Modeler](https://raw.github.com/camunda/camunda-eclipse-standalone/master/documentation/images/screenshot.png)


## Features

* BPMN 2.0 compliant modeler for process and collaboration diagrams
* Stripped down [Camunda Eclipse Plug-in](https://github.com/camunda/camunda-eclipse-plugin) inside
* Small footprint (50 MB in size)
* Native operating system integration (Windows, via installer)


## Resources

* [Issues](https://github.com/camunda/camunda-eclipse-standalone/issues)
* [Roadmap](https://github.com/camunda/camunda-eclipse-standalone/issues?milestone=1&state=open)
* [How to Contribute](./CONTRIBUTE.md)


## Troubleshooting

##### The modeler freezes on Linux/GTK

This is due to an [ancient Eclipse bug](https://bugs.eclipse.org/bugs/show_bug.cgi?id=215234).
Add `-Dorg.eclipse.swt.internal.gtk.disablePrinting` to your `modeler.ini` or `eclipse.ini` after the `-vmargs` line to solve the issue (see [Eclipse SWT FAQ](http://www.eclipse.org/swt/faq.php#printOnGTKHangs)).


## License

[Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html)
