<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>nts welcome</title>
    
    <script src="../jsLib/requirejs_code/require.js"></script>
				<script src="Config02.js"></script>
				
    <%= I.INCLUDE_JS() %>
    <%= I.INCLUDE_CSS() %>
    
  </head>
<!-- AUTHOR: JMadison.  ON:20##.##.##_####AMPM                               -->
<!-- TABSIZE: 2 Spaces.                                                      -->
<!-- This header belongs BELOW the <head></head> declaration.                -->
<!--   10|       20|       30|       40|       50|       60|       70|       -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->
  <body>
    
    <!--
    <img src="../jsLib/graphics/NexLogoCenteredFaded.svg" >
    -->
    
    <canvas id="myCanvas" width="150" height="100" style="border:1px solid #d3d3d3;">
		    Your browser does not support the HTML5 canvas tag.</canvas>
    
    <div data-ng-app="myApp" data-ng-controller="myCtrl" data-ng-init="VI();" >
      <!-- Vertically + horizontally centered dialog -->
      <div class="horcen_parent" > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      <div class="horcen_child"  > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      <div class="vertcen_parent"> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      <div class="vertcen_child" > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
        <div id="roundeddivs_wireframe" style="width:200px;">
            
          <div id="roundeddivs_headertab" align="center">
            
            <h2 style="color:#fff">Welcome!</h2>
           
          </div>
            <p></p>
            <button data-ng-click="ninjaLogin();" style="width:100%;">Ninja Login Page</button>
            <button data-ng-click="adminLogin();" style="width:100%;">Admin Login Page</button>
            
        </div>
      </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
    </div>
      
    <script>
    var app = angular.module('myApp', []);
    app.controller('myCtrl', function($scope, $http) {
      
      //variable initialization:
      $scope.VI = function(){
        $scope.appInitialized = true;
        hack_waitThenTryToLoad();
      };//FUNC::END
      
      $scope.ninjaLogin = function(){
        location.href = '/nts/give_me_your_token.jsp';
      };//FUNC::END
      
      $scope.adminLogin = function(){
        location.href = '/nts/prove_you_are_admin.jsp';
        
      };//FUNC::END
      
      //Require-Js section of app:
      //RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR//
      //$scope.doIt = function(){
      //  console.log("pressed");
      //$scope.loaderToggle();
      //};//
      
					 console.log("REQUIRE JS!");
					
					 var rjs_app = null;
					 var has_rjs_app = false;
      var attempt = 0;
					
				 	var myCallback = function(arg_app){
					  
					   console.log("called back");
						  console.log("arg_app==" + arg_app);
						  arg_app.resume();
						  rjs_app = arg_app;
						  has_rjs_app = true;
					 };//
					
					 var myErrback = function(arg_app){
					   console.log("errored back");
        attempt++;
        console.log("attempt#" + attempt + " failed");
        
        //try again:
        if(attempt < 13){
          setTimeout( tryToLoad(), 3000); //try again in 3 seconds.
        }//
				 	};//
					
      var tryToLoad = function(){
        //Path to app must be relative location from this HTML file:
        //var rel_path = "../jsLib/requirejs_code/madison_app/NexFracLoader";
        //require([rel_path], myCallback, myErrback);
        
        requirejs(['app_rel_path/NexFracLoader'], myCallback, myErrback);
        
        
      };//
      
      var hack_waitThenTryToLoad = function(){
        console.log("Loading hack initialized");
        setTimeout( tryToLoad(), 13000 ); //13 seconds.
      };

      $scope.loaderToggle = function(){
        rjs_app.toggle();
      };//
      //RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR//
      
    });
    </script>
      
  </body>
</html>
<!--   10|       20|       30|       40|       50|       60|       70|       -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->

<%-- This .JSP page should basically be a normal .JSP page except for this  --%>
<%-- ONE AND ONLY IMPORT and the references to it.                          --%>
<%@ page import="frontEndBackEndIntegration.I" %>