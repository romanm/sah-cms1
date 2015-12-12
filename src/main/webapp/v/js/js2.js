angular.module("sah1App", ['textAngular'])
.controller("sah1Ctrl", function demoController($scope, $http) {
	$scope.pageKey = window.location.search.substring(1);
	console.log($scope.pageKey);

	$http.get("/v/readContent").success(function(response) {
		$scope.jsonFromRam = response;
		console.log($scope.jsonFromRam);
		$scope.pageObject = response[$scope.pageKey];
		console.log($scope.pageObject);
	});

	$http.get("/read_user").success(function(data) {
		$scope.user = data;
		if($scope.user.authorities)
			$scope.role = $scope.user.authorities[0].authority
			.split("_")[1];
		console.log($scope.role);
	});
});


