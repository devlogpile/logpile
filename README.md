#Logpile project
=======
##Description 
=======
The purpose of this project is to have one for monitoring application. the api aggregate all errors from the application to one instance. This instance can do treatment on error reception. Example : create an unique console log.

![General description](https://raw.github.com/devlogpile/logpile/v0.2.0/doc/image/general_description.png)

The workflow is very simple :

1. One application produce an error.
2. The connector of the logpile client catch the error.
3. The connector send the information on the error to a Logpile server.
4. This server receives the 'event' and do a configured work on it.
    Example : send an email for warnong the developpement team, write to a single file the error, etc ...

##Structure
=======

This project is composed of two sub project :
* the client : It's a small jar which provide appenders for log framework in java. The implementation is in [client directory](https://github.com/devlogpile/logpile/tree/master/client)
* the server : it's a tiny application which collects errors provided by the appenders. The implementation is in [server directory](https://github.com/devlogpile/logpile/tree/master/server)

An example client project is available in the [client-example directory](https://github.com/devlogpile/logpile/tree/master/client-example).

The bin directory store the installation package distribution and the /doc contains images for the online documentation.

##Installation Instruction
=======

The installation for the server is available in the [server directory](https://github.com/devlogpile/logpile/tree/master/server).

And the configuration of the appenders are in the [client directory](https://github.com/devlogpile/logpile/tree/master/client).

##How to build the project
=======

1. Install the jdk 1.7
2. Install maven.
2. Download or fork the project.
3. Go to the root directory and execute the command : `mvn clean install`.


##Version
=======
__0.1.0__ : 

* skeleton implementation of the server and the client.
 
