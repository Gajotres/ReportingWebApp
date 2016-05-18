var reporting = angular.module('reporting', ['ngRoute','ngMaterial','angular.filter','ngMessages']);

reporting.constant("serverConstants", {
    "serverPath": "http://localhost:8080/"
});

reporting.config(function($routeProvider, $locationProvider, $mdDateLocaleProvider) {
    $routeProvider.
    when('/', {
        templateUrl: 'templates/report.html',
        controller: 'ReportCtrl'
    })
    .otherwise({
        redirectTo: '/'
    }); 

    $mdDateLocaleProvider.formatDate = function(date) {
       return moment(date).format('YYYY-MM-DD');
    };    
});

reporting.controller('ReportCtrl', [
    '$scope',
    '$routeParams',
    '$location',
    'serverConstants',
    '$http',
    function ($scope, $routeParams, $location, serverConstants, $http) {

      $scope.files = [];

      $scope.input = {
        start_date : new Date(),
        end_date : new Date()
      }

      $scope.tenants = [
        { id: 29, name: 'ABSI' },
        { id: 30, name: 'APEIRON' }
      ];

      $scope.selectedTenant = { id: 29, name: 'Bob' };

      $scope.generateReport = function() {
        $http.post(serverConstants.serverPath + 'generate/report01', {
          start_date: moment($scope.input.start_date).format('YYYY-MM-DD'),
          end_date: moment($scope.input.end_date).format('YYYY-MM-DD'),
          tenant_id: $scope.selectedTenant.id
        }).success(function(data) {
            console.log('Employee profile updated');
        });
      }

      $scope.getListOfReports = function() {
        $http.get('http://localhost:8080/generate/list/sfsd').success(function(data) {
          $scope.files = data;
        });        
      }

      $scope.getListOfReports();
    }
]);