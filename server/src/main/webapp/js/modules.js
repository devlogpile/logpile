/**************************************************/
/* Angular JS Modules                             */
/**************************************************/
var sharedConnection = ['$rootScope', function(root) {
    root.statusinfos = "Wait ...";

    var modify = {};

    modify.checkConnect = function() {
        //  console.log(message);
        root.$apply(function() {
            root.statusinfos = "Not Connected";
        });
        $(".connected").hide();
        $(".notconnected").show();
    };

    modify.init = function() {
        if (!eventBus.isConnected()) {
            eventBus.addOnOpen(function() {
                root.$apply(function() {
                    root.statusinfos = "Server available - open ...";
                });
            });
            eventBus.addOnClose(function() {
                root.$apply(function() {
                    root.statusinfos = "Server unavailable - closed";
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

    return {
        initialize: true,
        deleteOne: function(pIndex) {
            var newArray = [];
            for (var i = 0; i < rootScope.events.length; i++) {
                if (i != pIndex) {
                    newArray.push(rootScope.events[i]);
                }
            }
            rootScope.events = newArray;
        },

        getEvents: function() {
            return rootScope.events;
        },

        init: function() {
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
        }
    };

}]);
