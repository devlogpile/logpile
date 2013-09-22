---
layout: default
title: Client Log API - configuration
---

{% include header_client.html %}

##How to setup ?
=======
This client api require some data to be used. There is 3 ways to pass data to the logger api :

* by an xml file.
* by an properties file.
* by the System properties of the JVM.

There is an order for datas loading : first the xml file, the properties and the System properties. All files must be in the root of the classpath.

The datas which are used by the loggers are :

* _the application name_ : An identifier that indicates which server is or what the application. This parameter is optional. if not set, then an identifer is generated.
* _the engine type class_ : The type of communication used for the server. This parameter is required for the good functioner of the logpile. If not set, then an null implementation is used.
* _the url of the server_ : The address of the logpile server. This parameter is required. If not set, then no error registration is run.


###The xml configuration file.

The file must be named : 'logpile.client.xml'.

    <log.pile>
        <log.pile.application>Mon application</log.pile.application>
        <log.pile.engine >org.skarb.log.pile.client.post.engine.rest.EngineRestPost</log.pile.engine>
        <log.pile.url>http://localhost:8082/event</log.pile.url>
    </log.pile>

###The Properties file.

The file must be named : 'logpile.client.properties'.
    log.pile.server.id=My Server
    log.pile.application=application.name
    log.pile.engine=org.skarb.log.pile.client.post.engine.rest.EngineRestPost
    log.pile.url=http://localhost:8082/event 

###The System properties.

You can set the same properties than with a file directly in the System properties of the jvm.

    java -Dlog.pile.url=http://localhost:8082/event ... org.tyty.MyApp

or with the code
    
    System.setProperty("log.pile.url","http://localhost:8082/event");
    System.setProperty("log.pile.application","application.name");
    // My code
    Logger.error("my error");

