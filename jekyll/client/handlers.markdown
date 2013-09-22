---
layout: default
title: Client Log API - handlers
---

{% include header_client.html %}

##The available handlers

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