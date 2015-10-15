<!DOCTYPE html>
<html lang="en">
  <head>
    <!-- BASE: https://docs.angularjs.org/error/$location/nobase -->
    <base href="/nts/"> 
    <meta charset="utf-8">
    <title>Admin Home -NTS</title>
    <%= I.INCLUDE_JS() %>
    <%= I.INCLUDE_CSS()%>
  </head>
<!-- AUTHOR: JMadison.  ON:20##.##.##_####AMPM                               -->
<!-- TABSIZE: 2 Spaces.                                                      -->
<!-- This header belongs BELOW the <head></head> declaration.                -->
<!--   10|       20|       30|       40|       50|       60|       70|       -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->

<body>
  
  <img src="../jsLib/graphics/NexLogoCenteredFaded.svg" >
  
  <div data-ng-app="myApp" data-ng-controller="myCtrl" data-ng-init="VI();" >
    <!-- Vertically + horizontally centered dialog -->
    <div class="horcen_parent" > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
    <div class="horcen_child"  > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
    <div class="vertcen_parent"> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
    <div class="vertcen_child" > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      <div id="roundeddivs_wireframe" style="width:200px;">

        <div id="roundeddivs_headertab" align="center">

          <h2 style="color:#fff">Admin Home:</h2>

        </div>
          <p></p>
          <p> {{msg_for_user}} </p>
          <button data-ng-click="make_user();"   style="width:100%;">Make User</button>
          <button data-ng-click="give_test();"   style="width:100%;">Give Test</button>
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
    
    };

    $scope.make_user = function(){
        msg_for_user = "Redirecting...";
        $window.location = "add_new_ninja.jsp"; //try this.
    };//FUNC::END
    
    $scope.give_test = function(){
        msg_for_user = "Redirecting...";
        $window.location = "trial_assigner.jsp"; //try this.
    };//FUNC::END
    
    $scope.merge_user = function(){
        msg_for_user = "Redirecting...";
        $window.location = "add_new_ninja.jsp"; //try this.
    };//FUNC::END

    
      
  });
  </script>
</body>
</html>
<!--   10|       20|       30|       40|       50|       60|       70|       -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->
        
<%-- This .JSP page should basically be a normal .JSP page except for this  --%>
<%-- ONE AND ONLY IMPORT and the references to it.                          --%>
<%@ page import="frontEndBackEndIntegration.I" %>