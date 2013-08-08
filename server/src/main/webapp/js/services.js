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
