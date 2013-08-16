
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
