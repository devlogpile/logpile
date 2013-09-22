---
layout: default
title: Client Log API - example application
---

{% include header_client.html %}

##Example application

###Description 
=======
This a test application. The application is just a swing application whichh allow to call the logpile server.
This application is a swing application. The logger which is configured for logpile is the _ERROR_ scope.

###Where to retrieve the binaries ? 
=======

The example application is available in the distribution package in the `example` directory. 
	
	total 10
	d---------+ 1 skarb None   0 10 août  09:11 classes
	d---------+ 1 skarb None   0 10 août  09:11 lib
	----------+ 1 skarb None 131 10 févr.  2013 run_java_logging
	----------+ 1 skarb None 131 10 févr.  2013 run_java_logging.bat
	----------+ 1 skarb None 124 10 févr.  2013 run_log4j
	----------+ 1 skarb None 124 10 févr.  2013 run_log4j.bat
	----------+ 1 skarb None 126 10 févr.  2013 run_logback
	----------+ 1 skarb None 126 10 févr.  2013 run_logback.bat

####Structure 
=======

 * `run_java_logging` / `run_java_logging.bat` : Run the application with the java logging logger.
 * `run_log4j` / `run_log4j.bat` : Run the application with the log4j logger.
 * `run_logback` / `run_logback.bat` : Run the application with the log4j logger. 
 * `classes` : This directory contains the configurations files for the loggers and for logpile client (`logpile.client.xml`).
 * `lib` : This directory contains the jar of the application.

Example for launching the application :

	java -cp "./lib/client-example-0.2.0-jar-with-dependencies.jar;./classes" org.skarb.log.pile.client.example.SwingMain  JAVA_LOGGING

###The application 
=======

![The application](/logpile/images/client/example_application.png)

####Call  logpile with a message. 
=======

 1. Tips text in the `Message` input.
 2. Click on the `Call logger` button and your message is send. :))

This action simulate :

	LOGGER.error("My message");

####Call  logpile with a message. 
=======

 1. Select an exception in the `Exception` list.
 2. Tips a message for your exception in the `Message of Exception` textbox.
 3. Click on the `Call logger` button. 

This action simulate :

	LOGGER.error(exception);


##Source code
=======

The source code is available [here](https://github.com/devlogpile/logpile/tree/master/client-example).
