"use strict";

var app = angular.module("store", ["ngRoute"])
    .config(['$routeProvider',
    function($routeProvider) {
        $routeProvider
            .when('/success', {
                templateUrl: '/success.html'
            })
            .when('/login', {
                templateUrl: '/login.html',
                controller: 'LoginController'
            })
            .when('/uploadreg', {
                templateUrl: '/upload.html',
                controller: 'RegistriesController'
            })
            .otherwise({redirectTo: '/login'})
    }])
    .run(function($rootScope, $location) {
        $rootScope.$on("$routeChangeStart", function(event, next) {
            if(!(next.templateUrl == '/login.html')
                && !eval(localStorage.getItem('authenticated'))) {
                $location.path('/login')
            }
        })
    });
