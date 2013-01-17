#Logpile - client API
=======
##What does it do? 
=======

The developpers add this jar to their projects. They can configure their loggers to use the appenders which are provided by this projects. 

When the appenders of this project are calling, they call an ws rest service to register the error events. 

##Log api supported 
=======
This project provide jar appenders for java application. The common logging implementation supported by this project are : 
* [Java util logging APi](http://docs.oracle.com/javase/1.4.2/docs/guide/util/logging/)
* [Log4j](http://logging.apache.org/log4j/1.2/) 
* [LogBack](http://logback.qos.ch/)

##How to setup ?
=======
This client api require some data to be used. There is 3 ways to pass data to the logger api :
* by an xml file.
* by an properties file.
* by the System properties of the JVM.

There is an order for datas loading : first the xml file, the properties and the System properties. All files must be in the root of the classpath.

The datas which are used by the loggers are :
* _the application name_ : An identifier that indicates which server is or what the application. This parameter is optional. if it's not set, then an identifer is generated.
* _the engine type class_ : The type of communication used for the server. This parameter is required for the good functioner of the logpile. If the parameter is not set, then an null implementation is used.
* _ the url of the server_ : The address of the logpile server. This parameter is required. If it does not set, then no error registration is run.




##Version 
=======

###__0.2.0__ :
* Adding Java Logging Handler which do console output and file logging.
* Adding Log4j Handler which do console output and file logging.

###__0.1.0__ :
* Skeleton implementation client.
* Implementation for log4j.
* Implementation for java logging.
* Implementation for logback.
* Implementation of calling a ws to register error events.



