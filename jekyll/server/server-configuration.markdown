---
layout: default
title: Server - configuration 
---

{% include header_server.html %}

##The server configuration

The server is configured with a json file. The Vertx api is used to read the file.

The configuration is decomposed in 4 objects :

* `event_json` : This object contains the configuration for the Web service which registers the events.
* `log_pile_web` : This object contains the configuration for the Web Console.
* `org.skarb.logpile.vertx.web.AuthManagerJSonFile` : The datas in this object allows to used json file to login to the web console.
* `web_server` : The configuration of the web server module. This module is used by the web console.

The default configuration is available in the config directory of the server.

##The configuration of event web service

The datas used by the web service are :

* `context` : The name of the context used by the web service. 
* `port` : The port used by the service. This port should not be the same as the web server module.
* `instance` :  The number of instances of the web service available.
* `services` : the list of the service used to treat the events. The name used in this configuration is the name of the java class.

Example :

` "event_json" : {
            "context":"event",
            "port": 8082,
            "instance":1,
            "services":["org.skarb.logpile.vertx.event.ConsoleOutEvent"],
        } `

##The configuration of the web console

The datas used by the web console are :

* `active` : Active the web console if this value is equal to true. 
* `instance` :  The number of instances of the web console.
* `refresh_server_state` : the number in milliseconds of the web console refreshment. The information which are refreshed are the datas in the _resume tabs_. 

Example :

`"log_pile_web" : { "active":true, "instance":1, "refresh_server_state": 60000 }`

##The configuration of AuthManager

For accessing to the web console, the user must be logged. The first available implementation store the users in a Json file. The path of the file is configured with this object :

`"org.skarb.logpile.vertx.web.AuthManagerJSonFile":{ "path" : "config/users.json" }`

##The configuration of the web server module

The description of the configuration is available by this [link](https://github.com/vert-x/mod-web-server). 