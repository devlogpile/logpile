#Logpile - Server implementation
=======
##What does it do?  
=======
This projects provide an server implementation for the errors event registering. 

The clients send event call an ws rest service to register new errors. This server can be configure to treat the errors event ( Exemple : aggregate all the errors of different server to one log file or send an email to warn someone).

This implementation provides also a web console which manage the server treatment or view the current errors.   