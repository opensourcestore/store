'use strict';

app.controller('RegistriesController', ['$scope', '$http',
    '$location', function($scope, $http, $location) {

        $scope.fileinputs = [1];

        $scope.addFile = function() {
            $scope.fileinputs.push($scope.fileinputs.length + 1);
        }

        $scope.upload = function() {
            var fd = new FormData($('form')[0]);
            $http.post("/uploadregistries", fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            })
        }
}])