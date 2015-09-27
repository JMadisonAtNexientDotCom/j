<!DOCTYPE html> <!-- Numbers below mark 80 characters -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->
<html lang="en-US">
    <head>
      
        <%= I.INCLUDE_JS()  %>
        <%= I.INCLUDE_CSS() %>
				
        <title>NINJA MAKER 2000</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

			
    </head>
    <body>
        
        
        <div data-ng-app="myApp" data-ng-controller="customersCtrl"> 


    <div>

        <!-- quick test to make sure we have relative .css URL correct. -->
        <spinner name="html5spinner">  
            <div class="overlay"></div>
            <div class="spinner">
                    <div class="double-bounce1"></div>
                    <div class="double-bounce2"></div>
            </div>
            <div class="please-wait">Test of loading spinner...</div>
        </spinner>  
    NINJA MAKER 2000
    </div>

     <!-- these two calls work. But neither individually. Guessing ASYNC problem? -->
     <button data-ng-click="inc();">MAKE NINJA!</button> 


<div>
<textarea readonly>
{{ theStuffWeReallyNeed }} 
</textarea>
</div>
  
  
</div>

<script>
var app = angular.module('myApp', ['angularSpinners']);
//var app = angular.module('myApp', []);

//CONTROLLER::START
app.controller('customersCtrl', function($scope, $http, spinnerService) {
//app.controller('customersCtrl', function($scope, $http) {
    

    $scope.inc = function()
    {
      //serviceURL is potential refactoring nightmare. //
      //Can we use a .jsp page and dynamically pull URLs from classes? //
      serviceURL = "<%=I.API.GET_NEXT_NINJA.URL%>";
      //$http.get(serviceURL).success(function (response) {$scope.name = response;});
			
			spinnerService.show('html5spinner'); //<--where is ref to spinnerService?
			$http.get(serviceURL).success( onResponded );

   };
	 
	 function onResponded(response){
			$scope.theStuffWeReallyNeed = response;
			spinnerService.hide('html5spinner');
	 }


}); //CONTROLLER::END

</script>
        <!-- sdfffffffffffffffffffffffffffffff -->
        
   
    </body>
</html>

<%-- This .JSP page should basically be a normal .JSP page except for this  --%>
<%-- ONE AND ONLY IMPORT and the references to it.                          --%>
<%@ page import="frontEndBackEndIntegration.I" %>