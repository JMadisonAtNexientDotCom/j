<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>AHK SHORTCUT:?REFA</title>
    <!-- INCLUDE: ANGULAR JQUERY, BOOTSTRAP, in that order!                  -->
		
		<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
		<!-- BEFORE BOOTSTRAP INCLUDE: Bootstrap requiers JQuery Library.        -->
		<script src="https://code.jquery.com/jquery-2.1.4.min.js"></script>
		<!-- Bootstrap: .js and .css respectively.                               -->
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js" ></script>
		<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
		
  </head>
<!-- AUTHOR: JMadison.  ON:2015.09.18_0223AMPM                               -->
<!-- TABSIZE: 2 Spaces.                                                      -->
<!-- This header belongs BELOW the <head></head> declaration.                -->
<!--   10|       20|       30|       40|       50|       60|       70|       -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->
  <body>
    <div id="PIKA" data-ng-app="myApp" data-ng-controller="myCtrl" data-ng-init="VI();">
      
			<div class="panel panel-default" style="width:25%;">
				<div class="panel-heading">Panel heading without title</div>
			
			
				<form name="userForm" style="width: 100%;" novalidate>
					<select name="multipleSelect" style="width:100%; height:300px;" id="mulipass" ng-model="data.multipleSelect" multiple>
						<!-- This chunk repeats for each item in users collection          -->
						<!-- RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR -->
							<option data-ng-repeat="ninja in instancedJSON_01.members" 
																		value="{{ninja.name}}">{{ninja.name}}</option>
						<!-- RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR -->
					</select> <!-- form-group -->
		
					<nav class="navbar navbar-default">
						<ul class="pager">
							<li class="previous" id="myButton"><a href=""><span aria-hidden="true">&larr;</span> Older</a></li>
							<div class="btn-group">
								<button type="button" class="btn btn-primary">01</button>
								<button type="button" class="btn btn-primary">02</button>
								<button type="button" class="btn btn-primary">03</button>
								<button type="button" class="btn btn-primary">04</button>
								<button type="button" class="btn btn-primary">05</button>
							</div>
							<li class="next"><a href="#">Newer <span aria-hidden="true">&rarr;</span></a></li>
						</ul>
					</nav>
						
					<tt>multipleSelect = {{data.multipleSelect}}</tt><br/>
					
				</form>  <!-- userForm   -->
			</div> <!--panel -->
    </div>
		
		<script>
		$(function() { 
		  //http://dotnet-concept.com/Tip/2015/6/5798829/
			//                   How-to-call-angularJS-function-from-javascript-jquery
			$(".previous").click(function() {
				angular.element(document.getElementById('PIKA')).scope().goBAK();
			});
			
			$(".next").click(function() {
				angular.element(document.getElementById('PIKA')).scope().goFWD();
			});
			
			$(".btn").click(function() {
				angular.element(document.getElementById('PIKA')).scope().goTOG();
			});
			
		});
		</script>
		
		
		
    <script>
    var app = angular.module('myApp', []);
    app.controller('myCtrl', function($scope, $http) {
      //function to execute on click
      $scope.onClick = function(){
        var obj = JSON.parse(document.getElementById('embeddedJSON_01').innerHTML);
        $scope.instancedJSON_01=obj;
      }//FUNC::END
      //Variable initialization:
      $scope.VI = function(){
        $scope.instancedJSON_01 = {};
        $scope.isInitialized = true;
				$scope.pageIndexWanted   = 0;
				$scope.numResultsPerPage = 8;
      }//FUNC::END
			
			
			
			$scope.goBAK = function(){
			  $scope.pageIndexWanted--;
				$scope.callService();
			};
			
			$scope.goFWD = function(){
				$scope.pageIndexWanted++;
				$scope.callService();
			};
			
			//TODO: Some more comprehensive logic.
			$scope.goTOG = function(){
				$scope.pageIndexWanted = 2;
				$scope.callService();
			};
			
			
			
			$scope.callService = function()
			{
				//serviceURL = "https://j1clone01-madnamespace.rhcloud.com/api/NinjaRestService/get_page_of_ninjas?page_index=0&num_results_per_page=10";
				serviceURL = "https://j1clone01-madnamespace.rhcloud.com/api/NinjaRestService/get_page_of_ninjas";
			
				//Build query string using J-QUERY:
				//SOURCE: http://stackoverflow.com/questions/316781/
				//                             how-to-build-query-string-with-javascript
				qs = "";
				qs = $.param({ 
				page_index:$scope.pageIndexWanted, 
				num_results_per_page:$scope.numResultsPerPage});

				//url of rest-api responsible for making new ninja records using arguments:
				apiURL= serviceURL + "?" + qs;

				$http.get(apiURL).success( onResponded );
			};//FUNC::END
			
			function onResponded(response){
				$scope.instancedJSON_01 = response;
			};//FUNC::END
						
			
			
    });//APP::END
    </script>
		
    <script type="application/json" id="embeddedJSON_01">
      {
      "members":[ { "name":"bob", "email":"bob@bob.com"},
                   { "name":"sam", "email":"sam@sam.com"},
                   { "name":"jay", "email":"jay@jay.com"}],

      "unicorns": "awesome",
      "abc": [1, 2, 3]
      }
    </script>
  </body>
</html>
<!--   10|       20|       30|       40|       50|       60|       70|       -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->
