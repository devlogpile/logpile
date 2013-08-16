/**************************************************/
/* Angular JS COntroller                          */
/**************************************************/

/**
 * Controller for login state.
 **/
var LoginCtrl = function($scope, connection) {
        $scope.email = "";
        $scope.password = "";
        $scope.error = false;

        $scope.login = function() {
            eventBus.getEb().send('auth-logpile.login', {
                "username": $scope.email,
                "password": $scope.password
            }, function(message) {
                if (message.result == true) {
                    connection.addSession(message.sessionID);
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

       

        // initialize the globale variable of the event bus
        connection.init();
    }
LoginCtrl.$inject = ['$scope', 'connection'];