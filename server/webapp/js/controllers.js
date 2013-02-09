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
                eb = new vertx.EventBus("http://localhost:8081/eventbus");
                eb.onopen = function() {
                    open = true;
                    for (var i = 0; i < onopen.length; i++) {
                        onopen[i]();
                    }

                    for (var i = oneventregister.length - 1; i >= 0; i--) {
                       eb.registerHandler(oneventregister[i].address,oneventregister[i].handler); 
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

                eb.onclose = function(e){
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
                if(eb) {
                    eb.registerHandler(address, handler);
                } else {
                    oneventregister.push({"address":address,"handler":handler});
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
/* Angular JS Application                         */
/**************************************************/


var sharedConnection = ['$rootScope', function(root) {
    root.statusinfos = "Wait ...";

    var modify = {};

    modify.checkConnect = function() {
        root.$apply(function() {
          //  console.log(message);
            root.statusinfos = "Not Connected";

        });
        $(".connected").hide();
        $(".notconnected").show();
    };

    modify.init = function() {
        if (!eventBus.isConnected()) {
            eventBus.addOnOpen(function() {
                root.$apply(function() {
                    root.statusinfos = "Server available";
                });
            });
            eventBus.addOnClose(function() {
                root.$apply(function() {
                    root.statusinfos = "Server unavailable";
                    eb = null;

                });
                $(".connected").hide();
                $(".notconnected").show();
            });




            eventBus.addIf(function(message) {
                if (message.result) {
                    root.$apply(function() {
                        console.log(message);
                        root.statusinfos = "Connected";
                    });
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

    modify.logout = function() {

        eventBus.sendEventBus('auth-logpile.logout', {
            "tmp": "test"
        }, function(message) {
            console.log(message);
        });
        eraseCookie("sessionID")
        document.location = "/index.html";
        return false;
    };
    eventBus.registerHandler("logpile.weboutput.new.event", function(messageNE) {
        root.$apply(function() {
            root.events.push(messageNE);
        });
    });

    return modify;
}];

var logpileLogin = angular.module('logpile-login', []).
config(['$routeProvider', function($routeProvider) {
    $routeProvider.
    when('/login', {
        templateUrl: 'templates/login.html',
        controller: LoginCtrl
    }).
    otherwise({
        redirectTo: '/login'
    });
}]);

logpileLogin.factory('connection', sharedConnection);

var logpilemain = angular.module('logpile-main', []).
config(['$routeProvider', function($routeProvider) {
    $routeProvider.
    when('/configuration', {
        templateUrl: 'templates/configuration.html',
        controller: ServerState
    }).
    when('/resume', {
        templateUrl: 'templates/resume.html',
        controller: Resume
    }).
    when('/weboutput', {
        templateUrl: 'templates/webouput.html',
        controller: WebOutput
    }).
    otherwise({
        redirectTo: '/configuration'
    });
}]);

logpilemain.factory('connection', sharedConnection);

logpilemain.factory('weboutput', ["$rootScope", function(rootScope) {
    rootScope.events = [];
    rootScope.active = false;
    service = {};
    service.initialize = true;

    service.delete = function(pIndex) {
        var newArray = [];
        for (var i = 0; i < rootScope.events.length; i++) {
            if (i != pIndex) {
                newArray.push(rootScope.events[i]);
            }
        }

        rootScope.events = newArray;
    };

    service.init = function() {
        if (this.initialize) {
            eventBus.addIf(function(message) {
                if (message.result) {
                    eventBus.sendEventBus("logpile.weboutput.status", {}, function(messageWO) {
                        if (messageWO.result) {
                            rootScope.$apply(function() {
                                console.log("Web output installed.");
                                rootScope.active = true;
                            });

                        } else {
                            console.log("Web output not installed.");
                            rootScope.active = false;
                        }
                    });
                }
            });
            this.initialize = false;
        }
    };
    return service;
}]);

/**************************************************/
/* Angular JS COntroller                          */
/**************************************************/

/**
 * Controller for login state.
 **/

function LoginCtrl($scope, $rootScope, connection) {
    $scope.email = "";
    $scope.password = "";
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
        connection.logout();
    }

    // initialize the globale variable of the event bus
    connection.init();
}

/**
 * Controller for Server state.
 **/

function ServerState($scope, connection) {
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
    var init = function() {
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
        eventBus.sendEventBus("logpile.activate", {
            "name": name,
            "activate": activate
        }, function(message) {
            init();
        });
    };

    eventBus.addIf(function(message) {
        if (message.result) {
            init();
        }
    });
    connection.init();
};

/**
 * Controller for Resume Panel.
 **/

function Resume($scope, connection) {
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
    connection.init();
}

function WebOutput($scope, $rootScope, connection, weboutput) {
    //$scope.active = false;

    $scope.selectedItem = {};

    $scope.showDetail = function(pIndex) {
        $scope.selectedItem = $rootScope.events[pIndex];
        $('#detailModal').modal();
    };


    $scope.delete = function(pIndex) {
        weboutput.delete(pIndex);
    }


    connection.init();
    weboutput.init();
};

