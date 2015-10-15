<!DOCTYPE html>
<html lang="en">
  <head>
    <!-- BASE: https://docs.angularjs.org/error/$location/nobase -->
    <base href="/nts/"> 
    <meta charset="utf-8">
    <title>Admin Login</title>
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

          <h2 style="color:#fff">Admin Login:</h2>

        </div>
          <p></p>
          <p> {{msg_for_user}} </p>
          <input type="text" data-ng-model="user_name" style="width:100%;"><br>
          <input type="text" data-ng-model="pass_word" style="width:100%;"><br>
          <button data-ng-click="submit_my_creds();" style="width:100%;">SUBMIT</button>
          
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
        $scope.msg_for_user = "Credentials?:";
        $scope.user_name    = "sensei";
        $scope.pass_word    = "password_hashed";
    };

    $scope.submit_my_creds = function(){
        msg_for_user = "Please Wait...";
        callService();
    };

    function callService()
    {
        serviceURL ="<%= I.API().LOGIN_AND_GET_TOKEN_FOR_SELF.URL %>";
        
        qs = ""; //query string.
        qs = $.param({ 
        <%=I.API().LOGIN_AND_GET_TOKEN_FOR_SELF.ARG.USER_NAME %>:$scope.user_name,
        <%=I.API().LOGIN_AND_GET_TOKEN_FOR_SELF.ARG.PASS_WORD %>:$scope.pass_word
        });//QUERYSTRING::END
        
        apiCallURL = serviceURL + "?" + qs;
        
        spinnerService.show('html5spinner'); //<--where is ref to spinnerService?
        $http.get(apiCallURL).success( onResponded );
    };//FUNC::END
    
    function onResponded(response){
	$scope.instancedJSON_RESPONSE = response;
	spinnerService.hide('html5spinner');
        
        //Display error message, or session token value,
        //depending on if login was success.
        if(response.isError === true){
            $scope.msg_for_user = response.errorCode;
        }else{
            $scope.msg_for_user = response.value;
            
            //In order for this to work, you have to submit TWICE!
            //$location.path("admin_home.jsp");
            //$window.location.reload();//Force refresh.
            
            $window.location = "admin_home.jsp"; //try this.
            
        }//BLOCK::END
            
        
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