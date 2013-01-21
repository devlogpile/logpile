#Logpile - Server implementation
=======
##What does it do?  
=======
This projects provide an server implementation for the errors event registering. 

The clients send event call an ws rest service to register new errors. This server can be configure to treat the errors event ( Example : aggregate all the errors of differents servers to one log file or send an email to warn someone).

This implementation provides also a web console which manage the server treatment or view the current errors.

##Description of the project
=======
* [Server configuration](https://github.com/devlogpile/logpile/wiki/Server-Configuration)

##How to install the server
=======

1. Install the Jdkl 1.7.
2. Download and install [Vertx](http://vertx.io/).
3. Download the zip file which is in bin directory.
4. unzip the file.
5. In the distribution, run the "run_server" file depending of your operating system.
6. Try to connect at [http://localhost:8081/](http://localhost:8081/)
7. log with the email `root@logpile.org` and with the password `gtn`
8. That's it. Your server is runnning and functionnal.

##Version
=======
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
