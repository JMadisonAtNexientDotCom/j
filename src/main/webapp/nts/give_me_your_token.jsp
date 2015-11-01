<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Token Submit</title>
    
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
  
  <canvas id="myCanvas"  style="position:absolute; 
          margin-top:-200px; top:50%; 
          margin-left:-300px; left:50%;"
            width="600" height="360">
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

          <h2 style="color:#fff">Token?</h2>

        </div>
          <p></p>
          <p> {{msg_for_user}} </p>
          <input type="text" data-ng-model="token_input" style="width:100%;"><br>
          <button data-ng-click="submitToken();" style="width:100%;">SUBMIT</button>
          
          <!-- quick test to make sure we have relative .css URL correct. -->
          <spinner name="html5spinner">  
            <div class="overlay"></div>
            <div class="spinner">
                  <div class="double-bounce1"></div>
                  <div class="double-bounce2"></div>
            </div>
            <div class="please-wait">Test of loading spinner...</div>
          </spinner> 
        
        </div>
    </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
    </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
    </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
    </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
  </div>
  
  <script>
  var app = angular.module('myApp', ['ngCookies','angularSpinners']);
  app.controller('myCtrl', function($scope, $http, $location, $cookies, spinnerService) {
  
    //variable initialization:
    $scope.VI = function(){
        $scope.msg_for_user = "Insert Token:";
        $scope.token_input = "PUT_TOKEN_HERE";
        tryToLoadScripts();
    };

    $scope.submitToken = function(){
        msg_for_user = "Please Wait...";
        callService();
    };

    function callService()
    {
        serviceURL ="<%= I.API().IS_TOKEN_HASH_OWNED_BY_NINJA.URL %>";
        
        qs = ""; //query string.
        qs = $.param({ 
        <%=I.API().IS_TOKEN_HASH_OWNED_BY_NINJA.ARG.TOKEN_HASH %>:$scope.token_input
        });//QUERYSTRING::END
        
        apiCallURL = serviceURL + "?" + qs;
        
        spinnerService.show('html5spinner'); //<--where is ref to spinnerService?
        $http.get(apiCallURL).success( onResponded );
    };//FUNC::END
    
    function onResponded(response){
      $scope.instancedJSON_RESPONSE = response;
      spinnerService.hide('html5spinner');
        
        if(response.value === true){
            $scope.msg_for_user = "VALID!";
            $scope.proceed_further();
        }else{
            $scope.msg_for_user = "BAD TOKEN!";
        }//BLOCK::END
    };//FUNC::END
    
    //If valid token entered, proceed further:
    $scope.proceed_further = function(){
      $cookies.put("ACTIVE_TOKEN",$scope.token_input);
      location.href = '/nts/is_this_you.jsp'; //<--why no dollar sign?
    };//FUNC::END
    
    //Require-Js section of app:
    //RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR//
    console.log("REQUIRE JS!");

    var rjs_app = null;
    var has_rjs_app = false;

    var myCallback = function(arg_app){
      console.log("RequireJS Scripts Loaded");
      arg_app.setMaxFramesPerPlay(12);
      arg_app.num_levels_deep = 4;
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