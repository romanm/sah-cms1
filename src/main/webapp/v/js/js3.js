//inject angular file upload directives and services.
var fileUploadApp = angular.module('fileUploadApp', ['ngFileUpload']);

fileUploadApp.controller('fileUploadCtrl', ['$scope', 'Upload', '$timeout', '$http'
, function ($scope, Upload, $timeout, $http) {
	
	readPublicFiles = function(){
		$http.get("/v/readPublicFiles").success(function(data) {
			$scope.publicFiles = data;
			console.log($scope.publicFiles );
		});
	}
	readPublicFiles();

	$http.get("/read_user").success(
	function(data) {
		$scope.user = data;
		$scope.role = $scope.user.authorities[0].authority
				.split("_")[1];
	});

	$scope.setFile = function(e){
		if(e.picFile){
			console.log("---------------");
			console.log(e.picFile.name);
			console.log("---------------");
		}
	}
	$scope.uploadPic = function(file) {
			console.log("---------------");
			console.log(file);
			console.log(file.name);
			console.log("---------------");
  file.upload = Upload.upload({
    //url: 'https://angular-file-upload-cors-srv.appspot.com/upload',
	  url: '/upload',
//    url: '/upload2',
    data: {file: file, fileName: file.name},
  });

  file.upload.then(function (response) {
	  console.log("file.upload.then 1 param");
  	console.log(response);
    $timeout(function () {
      file.result = response.data;
    });
  }, function (response) {
	  console.log("file.upload.then 2 param");
  	console.log(response);
    if (response.status > 0)
      $scope.errorMsg = response.status + ': ' + response.data;
  }, function (evt) {
	  console.log("file.upload.then 3 param");
  	console.log(evt);
  	console.log("------------------------");
  	readPublicFiles();
  	console.log("------------------------");
    // Math.min is to fix IE which reports 200% sometimes
    file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
  });
  }
}]);
