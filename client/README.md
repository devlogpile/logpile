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

##Version 
=======
###__0.1.0__ :
* Skeleton implementation client.
* Implementation for log4j.
* Implementation for java logging.
* Implementation for logback.
* Implementation of calling a ws to register error events.



