#Logpile - Server implementation
=======

##Documentation
=======

The documentation is available in at [http://devlogpile.github.io/logpile](http://devlogpile.github.io/logpile).

##For the developpement.
=======

###How to build this module
=======

1. Install the jdk 1.7.
2. Install maven. Maven is use to build the java and create the packaging of this module.
3. Install the bower tools. Bower get the js and css libraries for the client part from the net. The installation description is available on [the Bower site](http://bower.io/).
4. Go to the root directory.
5. Execute the command : `bower install`. This command copy the css and jss libraries in the "src/main/webapp/components".
6. Execute the command : `mvn clean install`.

The default profile is the *dev* profile. This profile does not minify the js files. For releasing this module, add the  `-P release` to the command line.

###How to launch
=======

For the developpement of this module, you can launch the server with : `mvn vertx:run`. 

###Coverage report
=======

There is a jacoco plugin in the `pom.xml`. This plugin provide the coverage of the test for this module. You must download the jacoco libraries and set the `jacoco.agent.path` to this library. The coverage report is available in the `target/site/jacoco-it`.The command line for creating the reporting is `mvn clean install jacoco:report`.  

##Version
=======

###__0.3.0__ :
* Migration of the vertx 1.3.0 to 2.0.0-final.
* Migration to bootstrap 3.0 and font awesome.
* Add a event couchbase persistor which use the mod-couchbase module.
* Add filtering options and datas  in the _resume list_ screen.



###__0.2.0__ :
* The output of the errors in the web console.
* Add the minify for releasing the css and the js.

###__0.1.0__ :
* Skeleton implementation server.
* Ws Rest service for registering new errors.
* Skeleton of an api for handlers errors treatments.
* Handlers which prints in the console the errors.
* Handler which write in a log file the errors whith rolling management.
* Web application.
* Authorisation manager for the web application using an file.
* Interface which resume the state of the server.
* Interface which resume the last application in errors.
* Create an distribution for using vertx.
