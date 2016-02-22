'use strict';

app.controller('LoginController', ['$scope', '$http', 'TokenStorage', '$location',
    function($scope, $http, TokenStorage, $location) {
        $scope.lock = false;

        $scope.login = function() {
            var model = {
                username: $scope.user.username,
                password: $scope.user.password
            }

            $scope.lock = true;

            $http({
                method: 'POST',
                url: '/login',
                headers: {'Content-Type': 'application/www-form-urlencoded'},
                data: model
            }).success(function(data, status, headers) {
                TokenStorage.store(headers('X-AUTH-TOKEN'));
                localStorage.setItem('authenticated', true);
                $scope.lock = false;
                $location.path ('success');
            }).error(function() {
                $scope.lock = false;
                localStorage.removeItem('authenticated');
                TokenStorage.clear();
            })
        }

        $scope.init = function() {
            $http({
                method: 'GET',
                url: '/current'
            }).success(function(data) {
                if(!data || data.username == 'anonymousUser') {
                    TokenStorage.clear();
                    localStorage.removeItem('authenticated');
                }
            }).error(function() {
                TokenStorage.clear();
                localStorage.removeItem('authenticated');
            })
        }
    }]);
