# map-scala

# Description
* Contains files for server and client
* Client is located in Map_Leaflet folder
* conversion.Server.scala is the server, and ArsUtils.scala is a dependency

# Building

* Java Development Kit v8
* Scala Development Kit 2.11
* Simple Build Tool (SBT) 0.13
* tar

To package for distribution, execute following:

```
sbt universal:packageZipTarball
```
This will create a tarball that can be found in /target/universal/conversion-XXX.tgz. Explode the tarball, and run the bin/conversion executable.
