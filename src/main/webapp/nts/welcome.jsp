<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>nts welcome</title>
    
    <%= I.INCLUDE_JS() %>
    <%= I.INCLUDE_CSS() %>
    
  </head>
<!-- AUTHOR: JMadison.  ON:20##.##.##_####AMPM                               -->
<!-- TABSIZE: 2 Spaces.                                                      -->
<!-- This header belongs BELOW the <head></head> declaration.                -->
<!--   10|       20|       30|       40|       50|       60|       70|       -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->
  <body>
    
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
            <button data-ng-click="ninjaLogin();">Ninja Login Page</button>
            <button data-ng-click="adminLogin();">Admin Login Page</button>
            
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
      };//FUNC::END
      
      $scope.ninjaLogin = function(){
        location.href = '/nts/give_me_your_token.jsp';
      };//FUNC::END
      
      $scope.adminLogin = function(){
        location.href = '/nts/prove_you_are_admin.jsp';
        
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