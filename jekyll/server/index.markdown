---
layout: default
title: Server 
---

{% include header_server.html %}

##What does it do?  
=======
This projects provide an server implementation for the errors event registering. 

The clients send event call an ws rest service to register new errors. This server can be configure to treat the errors event ( Example : aggregate all the errors of differents servers to one log file or send an email to warn someone).

This implementation provides also a web console which manage the server treatment or view the current errors.

##Description of the project
=======

##How to install the server
=======

1. Install the Jdkl 1.7.
2. Download and install [Vertx](http://vertx.io/).
3. Download the zip file which is in [bin directory](../bin).
4. unzip the file.
5. In the distribution, run the "run_server" file depending of your operating system.
6. Try to connect at [http://localhost:8081/](http://localhost:8081/)
7. Log with the email `root@logpile.org` and with the password `gtn`. ![Web console]({{site.baseurl}}/images/server/login.png) 
8. That's it. Your server is runnning and functionnal.


* You can customize the parameters in the  [Server configuration section]({{site.baseurl}}/server/server-configuration.html).

##Source code
=======

The source code is available [here](https://github.com/devlogpile/logpile/tree/master/server).

##Library
=======
This implementaion use the open source projects :

* [Vertx](http://vertx.io/)  - 2.0.0 final version.
* [JQuery](http://jquery.com/) - latest version.
* [Twitter bootstrap](http://twitter.github.com/bootstrap/index.html) - 3.0 version.
* [Font awesome](http://fortawesome.github.io/Font-Awesome/) - latest.
* [Angular Js](http://angularjs.org/) - latest version.
* [Bower](http://bower.io/) - latest version.

