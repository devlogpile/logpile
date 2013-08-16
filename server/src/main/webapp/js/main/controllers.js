/**************************************************/
/* Angular JS COntroller                          */
/**************************************************/
/**
 * Controller for Server state.
 **/

var ServerState = function($scope, connection) {
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
                    $('[rel=tooltip]').tooltip({
                        html: true
                    });
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
ServerState.$inject = ['$scope', 'connection'];

/**
 * Controller for Resume Panel.
 **/

var Resume = function($scope, connection) {
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

Resume.$inject = ['$scope', 'connection'];



/**
 *Output for the web console.
 **/

var WebOutput = function($scope, connection, weboutput) {

        $scope.selectedItem = {};
        $scope.filterException = "";
        $scope.advanced = false;

        


        $scope.filterExceptionFct = function(input) {
            if (input) {
                if (input.stacktrace && "NoneExc" == $scope.filterException) {
                    return false;
                }
                if (!input.stacktrace && "Exc" == $scope.filterException) {
                    return false;
                }
            }
            return true;
        };

        $scope.showDetail = function(pIndex) {
            $scope.selectedItem = weboutput.getEvents()[pIndex];
            $("#collapseOne").collapse("show");
            $("#collapseTwo").collapse("hide");
            $('#detailModal').modal();
        };


        $scope.remove = function(pIndex) {
            weboutput.deleteOne(pIndex);
        }


        connection.init();
        weboutput.init();
    };

WebOutput.$inject = ['$scope', 'connection', 'weboutput'];
