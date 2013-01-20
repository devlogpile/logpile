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
            onclose = [];
        var privatesendEventBus = function(address, jsonObject, handler) {
                var newObject = JSON.parse(JSON.stringify(jsonObject));
                newObject.sessionID = readCookie("sessionID");
                eb.send(address, newObject, handler);
            };
        return {
            connect: function() {
                eb = new vertx.EventBus("http://localhost:8081/eventbus");
                eb.onopen = function() {

                    for (var i = 0; i < onopen.length; i++) {
                        onopen[i]();
                    }

                    privatesendEventBus("auth-logpile.authorise", {
                        "tmp": "initialize"
                    }, function(message) {
                        for (var i = 0; i < ifauthorized.length; i++) {
                            ifauthorized[i](message);
                        }


                    });
                }
            },

            sendEventBus: function(address, jsonObject, handler) {
                privatesendEventBus(address, jsonObject, handler);
            },
            getEb: function() {
                return eb;
            },
            registerHandler: function(address, handler) {
                eb.registerHandler(address, handler);
            },
            addIf: function(handler) {
                ifauthorized.push(handler);
            },
            addOnOpen: function(handler) {
                onopen.push(handler);
            },
            addOnClose: function(handler) {
                onclose.push(handler);
            }
        };
    }; /**************************************************/
/* Angular JS COntroller                          */
/**************************************************/

/**
 * Controller for login state.
 **/

function LoginCtrl($scope) {
    $scope.email = "";
    $scope.password = "";
    $scope.statusinfos = "Wait ...";
    $scope.error = false;


    $scope.login = function() {
        eventBus.getEb().send('auth-logpile.login', {
            "username": $scope.email,
            "password": $scope.password
        }, function(message) {
            if (message.result == true) {
                //alert("connecter");
                createCookie("sessionID", message.sessionID);
                document.location = "/welcome.html";

            } else {
                $scope.$apply(function() {
                    $scope.error = true;
                    $scope.password = "";

                });
            }

        });
        return false;
    };

    $scope.logout = function() {

        eventBus.sendEventBus('auth-logpile.logout', {
            "tmp": "test"
        }, function(message) {
            console.log(message);
        });
        eraseCookie("sessionID")
        document.location = "/index.html";
        return false;
    };
    // initialize the globale variable of the event bus

    eventBus.addOnOpen(function() {
        $scope.$apply(function() {
            $scope.statusinfos = "Server available";
        });
    });



    eventBus.addIf(function(message) {
        if (message.result) {
            $scope.$apply(function() {
                console.log(message);
                $scope.statusinfos = "Connected";
                $(".connected").show();
                $(".notconnected").hide();
            });

        } else {
            $scope.$apply(function() {
                console.log(message);
                $scope.statusinfos = "Not Connected";
                $(".connected").hide();
                $(".notconnected").show();
            });
        }
    })

    eventBus.addOnClose(function() {
        $scope.$apply(function() {
            $scope.statusinfos = "Server unavailable";
            eb = null;
        });
    });
    eventBus.connect();
}

/**
 * Controller for Server state.
 **/

function ServerState($scope) {
    $scope.datas = {
        config: {
            event_json: {
                instance: -1,
                port: -1
            },
            log_pile_web: {
                active: false,
                instance: -1,
                refreshserverstate: -1
            },
            web_server: {
                auth_address: "",
                bridge: true,
                instance: -1,

                port: -1,
                web_root: ""
            }
        },
        services: []

    };
    var init =function (){
        eventBus.sendEventBus("logpile.server-status", {}, function(message) {
               
                $scope.$apply(function() {
                    $scope.datas = message;
                    var host = window.location.hostname;
                    $scope.addressEvent = "http://" + host + ":" + message.config.event_json.port + "/" + message.config.event_json.context;


                });
                $('[rel=tooltip]').tooltip();
            });
    }
    $scope.addressEvent = "";

     $scope.modifyActivate = function(name, activate) {
        eventBus.sendEventBus("logpile.activate", {"name":name,"activate":activate}, function(message) {
              init(); 
        });
     };

    eventBus.addIf(function(message) {
        if (message.result) {
           init(); 
        }
    });

};

/**
 * Controller for Resume Panel.
 **/
function Resume($scope) {
    $scope.datas = {
        "totalError": 0
    };
    $scope.newdatas = {};
    $scope.notinitialized = true;

    $scope.refresh = function() {
        $scope.datas = $scope.newdatas;
        $scope.newdatas = {};
    };

    eventBus.addIf(function(message) {
        if (message.result) {
            eventBus.registerHandler("logpile.resume", function(message) {
                console.log(message);
                $scope.$apply(function() {
                    $scope.notinitialized = false;
                    for (var i = 0; i < message.applications.length; i++) {
                        message.applications[i].indexName = "app-resume-" + i;
                    }

                    if ($("#status-2").is(":hidden") || ($scope.datas.totalError == 0)) {
                        $scope.datas = message;
                        $scope.newdatas = {};
                    } else if ($scope.datas.totalError < message.totalError) {
                        $scope.newdatas = message;
                    }
                });
            });
        }
    });
}

function WebOutput($scope) {
   $scope.active=false;
    $scope.events=[];

   eventBus.addIf(function(message) {
           if (message.result) {
             eventBus.sendEventBus("logpile.weboutput.status",{}, function(messageWO){
                 if(messageWO.result){
                    $scope.$apply(function() {
                        $scope.active=true;
                    });
                    console.log("Web output installed.");
                    eventBus.registerHandler("logpile.weboutput.new.event", function(messageNE) {
                        $scope.$apply(function() {
                            $scope.events.push(messageNE);
                        });
                    });

                 } else {
                    console.log("Web output not installed.");
                 }
             });
           }
   });
}
