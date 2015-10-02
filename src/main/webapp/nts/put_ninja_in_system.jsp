<!DOCTYPE html> <!-- Numbers below mark 80 characters -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->
<html lang="en-US">
    <head>
        <%-- Inject JavaScript and CSS Libraries --%>
        <%= I.INCLUDE_JS()  %>
        <%= I.INCLUDE_CSS() %>		
        <title>Ninja Sign Up Form</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">						
    </head>
<body>

<div data-ng-app="myApp" data-ng-controller="myCtrl" data-ng-init="apiCallUsed='ONE';" >
 
  
  <!-- Vertically + horizontally centered dialog -->
  <div class="horcen_parent" > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
  <div class="horcen_child"  > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
  <div class="vertcen_parent"> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
  <div class="vertcen_child" > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
    <div id="roundeddivs_wireframe" style="width:200px;">

      <!-- The table with title at head of container. -->
      <div id="roundeddivs_headertab" align="center">
        <h2 style="color:#fff">Register a new Ninja:</h2>
      </div>
        
      <!-- F is for FORM FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF -->
      <!-- FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF -->
      <form name="myForm">
      <p>New Ninja Registration:</p>
      <p>Name : <input type="text" name="iName"  ng-model="nName" placeholder="Enter name here"></p>

      <!-- using number type is not what I want for phone numbers. It gives me a spinner! -->
      <p>Phone: <input type="text" name="iPhone" ng-model="nPhone"    
                       ng-required="number"
                       ng-minlength="3"
                       ng-maxlength="13"
                       placeholder="##########">
          <span class="error" ng-show="myForm.iPhone.$error.minlength">Too short!</span>
          <span class="error" ng-show="myForm.iPhone.$error.maxlength">Too long!</span>
      </p>
      <p>Email: <input type="text" name="iEmail" ng-model="nEmail"    placeholder="Required@Email.com"></p>
      <p>Website / Portfolio / Resume URL: </p>
      <p><input type="text" name="iPortfolio" ng-model="nPortfolio"placeholder="www.Zombo.com"></p>
      <button data-ng-click="onButtonClicked()">REGISTER NINJA</button>

      <textarea readonly>{{apiCallUsed}}</textarea>
      </form>
      <!-- FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF -->
      

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
  
  
  





</div> <!-- APP::END -->

<script>
//sample app declaration: 
//<div ng-app="myApp" ng-controller="myCtrl">
//sample input binded to firstName: 
//<input type="text" ng-model="firstName"><br>

var app = angular.module('myApp', ['angularSpinners']);
app.controller('myCtrl', function($scope, $http, spinnerService) {
    

    //set function reference:
    $scope.onButtonClicked = onRegisterButtonClick;

    function onRegisterButtonClick(){
        spinnerService.show('html5spinner');
        
        //Build query string using J-QUERY:
        //SOURCE: http://stackoverflow.com/questions/316781/
        //                             how-to-build-query-string-with-javascript
        qs = "";
        qs = $.param({ 
        <%=I.API().MAKE_NINJA_RECORD.ARG.NAME         %>:$scope.nName, 
        <%=I.API().MAKE_NINJA_RECORD.ARG.PHONE        %>:$scope.nPhone,
        <%=I.API().MAKE_NINJA_RECORD.ARG.EMAIL        %>:$scope.nEmail,
        <%=I.API().MAKE_NINJA_RECORD.ARG.PORTFOLIO_URL%>:$scope.nPortfolio });

        //url of rest-api responsible for making new ninja records using arguments:
        apiURL= "<%=I.API().MAKE_NINJA_RECORD.URL%>" + "?" + qs;
        
        //for debugging:
        $scope.apiCallUsed = apiURL;
        
        $http.get(apiURL).success(onResponded);

    }

    function onResponded(response){
	//$scope.theStuffWeReallyNeed = response;
	spinnerService.hide('html5spinner');
    }

});
</script>

</body>
</html>

<%-- This .JSP page should basically be a normal .JSP page except for this  --%>
<%-- ONE AND ONLY IMPORT and the references to it.                          --%>
<%@ page import="frontEndBackEndIntegration.I" %>