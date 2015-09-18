
(function(angular){

		angular.module('docsIsolateScopeDirective', [])
		.controller('Controller', ['$scope', function($scope) {
			$scope.naomi = { name: 'Naomi', address: '1600 Amphitheatre' };
			$scope.igor = { name: 'Igor', address: '123 Somewhere' };
		}])
		.directive('myCustomer', function() {
			return {
				restrict: 'E',
				scope: {
					myCustomerInfo: '=info'
				},
				templateUrl: 'my-customer-iso.html'
			};
		});
// C:\DEV\REPO\GIT\j\src\main\webapp\jsLib\nexient\angularLib\directives\test\index.html

})(window.angular);