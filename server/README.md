#Logpile - Server implementation
=======
##What does it do?  
=======
This projects provide an server implementation for the errors event registering. 

The clients send event call an ws rest service to register new errors. This server can be configure to treat the errors event ( Example : aggregate all the errors of differents servers to one log file or send an email to warn someone).

This implementation provides also a web console which manage the server treatment or view the current errors.

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