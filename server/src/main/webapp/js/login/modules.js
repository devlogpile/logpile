
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
