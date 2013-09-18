/**************************************************/
/* Cookie management                         */
/**************************************************/

function createCookie(name, value) {
    var date = new Date();
    date.setTime(date.getTime() + (60 * 60 * 1000));
    var expires = "; expires=" + date.toGMTString();
    document.cookie = name + "=" + value + expires + "; path=/";
}


function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}

function eraseCookie(name) {
    createCookie(name, "", -1);
}



/**************************************************/
/* Event bus Management                          */
/**************************************************/

var eventBus = new function() {
        var eb = null,
            ifauthorized = [],
            onopen = [],
            onclose = [],
            oneventregister = [],
            open = false,
            authorisationCheck = false,
            authorzationMessage;
        var privatesendEventBus = function(address, jsonObject, handler) {
                var newObject = JSON.parse(JSON.stringify(jsonObject));
                newObject.sessionID = readCookie("sessionID");
                eb.send(address, newObject, handler);
            };
        return {
            isConnected: function() {
                return eb != null;
            },
            connect: function() {
                eb = new vertx.EventBus("http://" + window.location.host + "/eventbus");
                eb.onopen = function() {
                    open = true;
                    for (var i = 0; i < onopen.length; i++) {
                        onopen[i]();
                    }

                    for (var i = oneventregister.length - 1; i >= 0; i--) {
                        eb.registerHandler(oneventregister[i].address, oneventregister[i].handler);
                    };

                    privatesendEventBus("auth-logpile.authorise", {
                        "tmp": "initialize"
                    }, function(message) {
                        authorisationCheck = true;
                        authorzationMessage = message;

                        for (var i = 0; i < ifauthorized.length; i++) {
                            ifauthorized[i](message);
                        }


                    });
                };

                eb.onclose = function(e) {
                    for (var i = onclose.length - 1; i >= 0; i--) {
                        onclose[i](e);
                    };
                }
            },



            sendEventBus: function(address, jsonObject, handler) {
                privatesendEventBus(address, jsonObject, handler);
            },
            getEb: function() {
                return eb;
            },
            registerHandler: function(address, handler) {
                if (eb) {
                    eb.registerHandler(address, handler);
                } else {
                    oneventregister.push({
                        "address": address,
                        "handler": handler
                    });
                }

            },
            addIf: function(handler) {
                if (authorisationCheck) {
                    handler(authorzationMessage);
                } else {
                    ifauthorized.push(handler);
                }
            },
            addOnOpen: function(handler) {
                if (open) {
                    handler();
                } else {
                    onopen.push(handler);
                }
            },
            addOnClose: function(handler) {
                onclose.push(handler);
            }
        };
    };

/**************************************************/
/* Angular JS Modules                             */
/**************************************************/
var sharedConnection = ['$rootScope', function(root) {
    root.statusinfos = "Wait ...";

    var modify = {};

    var setMessage = function(message){
        if(root.$$phase) {
            root.statusinfos = message;
        } else {
            root.$apply(function() {
                root.statusinfos = message;
            });
        }
    };

    modify.checkConnect = function() {
        setMessage("Not Connected");
        $(".connected").hide();
        $(".notconnected").show();
    };

    modify.init = function() {
        if (!eventBus.isConnected()) {
            eventBus.addOnOpen(function() {
                setMessage( "Server available - open ...");
            });
            eventBus.addOnClose(function() {
               setMessage("Server unavailable - closed");
                eb = null;
                $(".connected").hide();
                $(".notconnected").show();
            });




            eventBus.addIf(function(message) {
                if (message.result) {
                    setMessage( "Connected");
                    $(".connected").show();
                    $(".notconnected").hide();
                } else {
                    modify.checkConnect();
                }
            });
            eventBus.connect();
            modify.checkConnect();
        }
    };
    modify.addSession = function(session) {
        createCookie('sessionID',session);
    },

    modify.logout = function() {

        eventBus.sendEventBus('auth-logpile.logout', {
            "tmp": "test"
        }, function(message) {
            console.log(message);
        });
        createCookie('sessionID', "");
        document.location = "/index.html";
        return false;
    };
    eventBus.registerHandler("logpile.weboutput.new.event", function(messageNE) {
        root.$apply(function() {
            root.events.push(messageNE);
        });
    });

    root.logout = modify.logout;

    return modify;
}];
