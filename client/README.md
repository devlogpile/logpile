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

##The specific handlers

###For java Logging

3 implementations ara availables :
* __org.skarb.log.pile.client.post.util.log.JavaUtilLogHandler__ : An handler which do only the error registration on the server. 
* __org.skarb.log.pile.client.post.util.log.ConsoleHandler__ : An handler which do the error registration on the server and the output console. The parameters of the console output are identicals of the class _java.util.logging.ConsoleHandler_.
* __org.skarb.log.pile.client.post.util.log.FileHandler__ : An handler which do the error registration on the server and write into a file. The parameters of the file parameters are identicals of the class _java.util.logging.FileHandler_.

Use case :

    handlers=org.skarb.log.pile.client.post.util.log.ConsoleHandler
    java.util.logging.SimpleFormatter.format=%5$s %6$s\n
    org.skarb.log.pile.client.post.util.log.ConsoleHandler.formatter=java.util.logging.SimpleFormatter
    org.skarb.log.pile.client.post.util.log.ConsoleHandler.level=INFO
    .level=INFO
    org.skarb.log.pile.tyty.level=SEVERE


###For log4J

3 implementations ara availables :
* __org.skarb.log.pile.client.post.log4j.Log4JAppender__ : An handler which do only the error registration on the server.
* __org.skarb.log.pile.client.post.log4j.ConsoleAppender__ : An handler which do the error registration on the server and the output console. The parameters of the console output are identicals of the class _java.util.logging.ConsoleHandler_.
* __org.skarb.log.pile.client.post.log4j.FileAppender__ : An handler which do the error registration on the server and write into a file. The parameters of the file parameters are identicals of the class _java.util.logging.FileHandler_.

Use case :

    log4j.rootLogger=ERROR, CA
    log4j.appender.CA=org.skarb.log.pile.client.post.log4j.ConsoleAppender
    log4j.appender.CA.layout=org.apache.log4j.PatternLayout
    log4j.appender.CA.layout.ConversionPattern=%-4r [%t] %-5p %c %x - %m%n

###For LogBack

4 implementations ara availables :
* __org.skarb.log.pile.client.post.logback.LogbackAppender__ : An handler which do only the error registration on the server.
* __org.skarb.log.pile.client.post.logback.ConsoleAppender__ : An handler which do the error registration on the server and the output console. The parameters of the console output are identicals of the class _java.util.logging.ConsoleHandler_.
* __org.skarb.log.pile.client.post.logback.FileAppender__ : An handler which do the error registration on the server and write into a file. The parameters of the file parameters are identicals of the class _java.util.logging.FileHandler_.
* __org.skarb.log.pile.client.post.logback.RollingFileAppender__ : An handler which do the error registration on the server and write into a file with rolling file policy. The parameters of the file parameters are identicals of the class _java.util.logging.FileHandler_.

Use case :

    <configuration>
        <appender name="STDOUT" class="org.skarb.log.pile.client.post.logback.ConsoleAppender">
            <encoder>
                <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
            </encoder>
        </appender>
        <root level="debug">
            <appender-ref ref="STDOUT" />
        </root>
    </configuration>

##Version 
=======

###__0.2.0__ :
* Adding Java Logging Handler which do console output and file logging.
* Adding Log4j Appender which do console output and file logging.
* Adding LogBack Appender which do console output and file logging.

###__0.1.0__ :
* Skeleton implementation client.
* Implementation for log4j.
* Implementation for java logging.
* Implementation for logback.
* Implementation of calling a ws to register error events.



