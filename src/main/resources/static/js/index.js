var reporting = angular.module('reporting', ['ngRoute','ngMaterial','angular.filter','ngMessages']);

reporting.constant("serverConstants", {
    "serverPath": "http://localhost:8080/"
});

reporting.constant("reportConstants", {
    "reportList": [{ name: 'Report01' }]
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

      /*$scope.tenants = [
        { id: 29, name: 'ABSI' },
        { id: 30, name: 'APEIRON' }
      ];

      $scope.selectedTenant = { id: 29, name: 'Bob' };*/

      $scope.showLoader = false;      

      $scope.generateReport = function() {
        $scope.showLoader = !$scope.showLoader;
        $http.post(serverConstants.serverPath + 'generate/Report01', {
          start_date: moment($scope.input.start_date).format('YYYY-MM-DD'),
          end_date: moment($scope.input.end_date).format('YYYY-MM-DD'),
          tenant_id: $scope.selectedTenant.id
        }).success(function(data) {
            $scope.showLoader = !$scope.showLoader;
            $scope.getListOfReports();
        });
      }

      $scope.getListOfReports = function() {
        $http.get(serverConstants.serverPath + 'generate/list/Report01').success(function(data) {
          $scope.files = data;
        });        
      }

      $scope.getListOfTenents = function() {
        $http.get(serverConstants.serverPath + 'generate/getTenantList').success(function(data) {
          $scope.tenants = data;
        });        
      }

      $scope.downloadReport = function(fileName) {

        var a = document.createElement("a");
        document.body.appendChild(a);
        a.style = "display: none";

        $http.get(serverConstants.serverPath + 'generate/get/Report01/file/' + fileName, { responseType: 'arraybuffer' }).then(function (response) {
            var file = new Blob([response.data], {type: 'application/pdf'});
            var fileURL = window.URL.createObjectURL(file);
            a.href = fileURL;
            a.download = fileName;
            a.click();          
        });
      }

      $scope.getListOfTenents();
      $scope.getListOfReports();
    }
]);