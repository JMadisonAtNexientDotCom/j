<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Token Submit</title>
    <%= I.INCLUDE_JS() %>
    <%= I.INCLUDE_CSS()%>
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
  var app = angular.module('myApp', ['angularSpinners']);
  app.controller('myCtrl', function($scope, $http, spinnerService) {
  
    //variable initialization:
    $scope.VI = function(){
        $scope.msg_for_user = "Insert Token:";
        $scope.token_input = "PUT_TOKEN_HERE";
    };

    $scope.submitToken = function(){
        msg_for_user = "Please Wait...";
        callService();
    };

    function callService()
    {
        serviceURL ="<%= I.API().DOES_TOKEN_HAVE_OWNER.URL %>";
        
        qs = ""; //query string.
        qs = $.param({ 
        <%=I.API().DOES_TOKEN_HAVE_OWNER.ARG.TOKEN_ID %>:$scope.token_input
        });//QUERYSTRING::END
        
        spinnerService.show('html5spinner'); //<--where is ref to spinnerService?
        $http.get(serviceURL).success( onResponded );
    };//FUNC::END
    
    function onResponded(response){
	$scope.instancedJSON_RESPONSE = response;
	spinnerService.hide('html5spinner');
        
        if(response.value === true){
            $scope.msg_for_user = "VALID!";
        }else{
            $scope.msg_for_user = "BAD TOKEN!";
        }
            
        
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