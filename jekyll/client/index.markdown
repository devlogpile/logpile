---
layout: default
title: Client Log API 
---

{% include header_client.html %}

##What does it do? 
=======

The developpers add this jar to their projects. They can configure their loggers to use the appenders which are provided by this projects. 

When the appenders of this project are calling, they call an ws rest service to register the error events. 

##Dependencies 
=======

This library requires no dependence except your logger library.

##Log api supported 
=======
This project provide jar appenders for java application. The common logging implementation supported by this project are : 
 [Java util logging APi](http://docs.oracle.com/javase/1.4.2/docs/guide/util/logging/), [Log4j](http://logging.apache.org/log4j/1.2/), [LogBack](http://logback.qos.ch/)

##Source code
=======

The source code is available [here](https://github.com/devlogpile/logpile/tree/master/client).

