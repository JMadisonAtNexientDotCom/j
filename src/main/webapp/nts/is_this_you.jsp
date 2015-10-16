<%@page import="frontEndBackEndIntegration.I"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Honors System:</title>
    <%= I.INCLUDE_CSS() %>
    <%= I.INCLUDE_JS()  %>
    
  </head>
<!-- AUTHOR: JMadison.  ON:20##.##.##_####AMPM                               -->
<!-- TABSIZE: 2 Spaces.                                                      -->
<!-- This header belongs BELOW the <head></head> declaration.                -->
<!--   10|       20|       30|       40|       50|       60|       70|       -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->

  <body>
  
  <div data-ng-app="myApp" data-ng-controller="myCtrl" data-ng-init="VI();" >
    
    <img src="../jsLib/graphics/NexLogoCenteredFaded.svg" >

    <div data-ng-app="myApp" data-ng-controller="myCtrl" data-ng-init="VI();" >
      <!-- Vertically + horizontally centered dialog -->
      <div class="horcen_parent" > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      <div class="horcen_child"  > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      <div class="vertcen_parent"> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      <div class="vertcen_child" > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
        <div id="roundeddivs_wireframe" style="width:200px;">
            
          <div id="roundeddivs_headertab" align="center">
            
            <h2 style="color:#fff">Is This You?</h2>
           
          </div>
            <button data-ng-click="proceed_valid();" style="width:100%;">Yes, That's me!</button>
            <p><b>Not you?</b> That's okay, let us know and we'll fix it!</p>
            <button data-ng-click="proceed_bogus();" style="width:100%;">No.</button>
            
        </div>
      </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
    </div>
  </div>
    
  <script>
  var app = angular.module('myApp', ['angularSpinners']);
  app.controller('myCtrl', function($scope, $http, $location, spinnerService) {

    //variable initialization:
    $scope.VI = function(){
      $scope.msg_for_user = "Insert Token:";
    };
   
    //Everything is in order, proceed onward:
    $scope.proceed_valid = function(){
      location.href = '/nts/press_start_when_ready.jsp';
    };//FUNC::END

    //Token compromised, goto notification page:
    $scope.proceed_bogus = function(){
      location.href = '/nts/token_has_been_compromised.jsp';
    };//FUNC::END

  });//CONTROLLER::END
  </script>
    
  </body>
</html>
<!--   10|       20|       30|       40|       50|       60|       70|       -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->