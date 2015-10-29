<!DOCTYPE html>
<html lang="en">
  <head>
    <!-- BASE: https://docs.angularjs.org/error/$location/nobase -->
    <base href="/nts/"> 
    <meta charset="utf-8">
    <title><%=I.DN().ADMIN%> Home -NTS</title>
    
    <!-- So we can get awesome patterns into the background. -->
    <script src="../jsLib/requirejs_code/require.js"></script>
				<script src="../jsLib/requirejs_code/config/nts_config.js"></script>
    
    <%= I.INCLUDE_JS() %>
    <%= I.INCLUDE_CSS()%>
  </head>
<!-- AUTHOR: JMadison.  ON:20##.##.##_####AMPM                               -->
<!-- TABSIZE: 2 Spaces.                                                      -->
<!-- This header belongs BELOW the <head></head> declaration.                -->
<!--   10|       20|       30|       40|       50|       60|       70|       -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->

<body>
  
  <canvas id="myCanvas"  style="position:relative; 
          margin-top:-150px; top:50%; 
          margin-left:-250px; left:50%;"
            width="500" height="300">
            Your browser does not support the HTML5 canvas tag.
  </canvas>  
  
  <div data-ng-app="myApp" data-ng-controller="myCtrl" data-ng-init="VI();" >
    <!-- Vertically + horizontally centered dialog -->
    <div class="horcen_parent" > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
    <div class="horcen_child"  > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
    <div class="vertcen_parent"> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
    <div class="vertcen_child" > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      <div id="roundeddivs_wireframe" style="width:200px; margin-left:-100px;">

        <div id="roundeddivs_headertab" align="center">

          <h2 style="color:#fff"><%=I.DN().ADMIN%> Home:</h2>

        </div>
          <p></p>
          <p> {{msg_for_user}} </p>
          <button data-ng-click="make_user();"   style="width:100%;">Make User</button>
          <button data-ng-click="give_test();"   style="width:100%;">Give Test</button>
          <button data-ng-click="user_stats();"   style="width:100%;">User Stats</button>
          <button data-ng-click="merge_user();" style="width:100%;">TODO:Merge User</button>
          
          <!-- quick test to make sure we have relative .css URL correct. -->
          <spinner name="html5spinner">  
            <div class="overlay"></div>
            <div class="spinner">
                  <div class="double-bounce1"></div>
                  <div class="double-bounce2"></div>
            </div>
            <div class="please-wait">Please Wait...</div>
          </spinner> 
        
        </div>
    </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
    </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
    </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
    </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
  </div>
  
  <script>
  var app = angular.module('myApp', ['angularSpinners'])
  .config(function($locationProvider) {
  
  //You CANNOT inject $locationProvider into the controller.
  //You must do it with app.config:
  //http://stackoverflow.com/questions/22892246/
  //                        how-do-i-access-the-locationprovider-to-configure-it
    $locationProvider.html5Mode(true);
    $locationProvider.hashPrefix('!');
  });//CONFIG::END
  
  app.controller('myCtrl', function($scope, $http, $window, $location, 
                                                               spinnerService) {
  
    //variable initialization:
    $scope.VI = function(){
      $scope.msg_for_user = "Select Action:";
      tryToLoadScripts();
    };

    $scope.make_user = function(){
        msg_for_user = "Redirecting...";
        $window.location = "add_new_ninja.jsp"; //try this.
    };//FUNC::END
    
    $scope.give_test = function(){
        msg_for_user = "Redirecting...";
        $window.location = "trial_assigner.jsp"; //try this.
    };//FUNC::END
    
    $scope.user_stats = function(){
        msg_for_user = "Redirecting...";
        $window.location = "find_user_stats.jsp"; //try this.
    };//FUNC::END
    
    $scope.merge_user = function(){
        msg_for_user = "Redirecting...";
        $window.location = "add_new_ninja.jsp"; //try this.
    };//FUNC::END

    //Require-Js section of app:
    //RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR//
    console.log("REQUIRE JS!");

    var rjs_app = null;
    var has_rjs_app = false;

    var myCallback = function(arg_app){
      console.log("RequireJS Scripts Loaded");
      arg_app.setMaxFramesPerPlay(25);
      arg_app.num_levels_deep = 3;
      arg_app.resume();
      rjs_app = arg_app;
      has_rjs_app = true;
    };//

    var myErrback = function(arg_app){
      console.log("errored back");
    };//

    var tryToLoadScripts = function(){
      requirejs(['app_rel_path/NexFracLoader'], myCallback, myErrback);
    };//
    
    $scope.loaderToggle = function(){
      rjs_app.toggle();
    };//
    //RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR//
      
  });//CONTROLLER::END
  </script>
</body>
</html>
<!--   10|       20|       30|       40|       50|       60|       70|       -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->
        
<%-- This .JSP page should basically be a normal .JSP page except for this  --%>
<%-- ONE AND ONLY IMPORT and the references to it.                          --%>
<%@ page import="frontEndBackEndIntegration.I" %>