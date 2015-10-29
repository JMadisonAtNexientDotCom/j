<!DOCTYPE html> <!-- Numbers below mark 80 characters -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->
<html lang="en-US">
    <head>
      
        <!-- So we can get awesome patterns into the background. -->
        <script src="../jsLib/requirejs_code/require.js"></script>
				    <script src="../jsLib/requirejs_code/config/nts_config.js"></script>
      
        <%-- Inject JavaScript and CSS Libraries --%>
        <%= I.INCLUDE_JS()  %>
        <%= I.INCLUDE_CSS() %>		
        <title><%=I.DN().NINJA%> Sign Up Form</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">						
    </head>
<body>

<div data-ng-app="myApp" data-ng-controller="myCtrl" data-ng-init="VI();" >
 
  <canvas id="myCanvas"  style="position:absolute; 
          margin-top:-205px; top:50%; 
          margin-left:-300px; left:50%;"
            width="600" height="410">
            Your browser does not support the HTML5 canvas tag.
  </canvas>  
  
  <!-- Vertically + horizontally centered dialog -->
  <div class="horcen_parent" > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
  <div class="horcen_child"  > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
  <div class="vertcen_parent"> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
  <div class="vertcen_child" > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
    
    <div id="PACK_OF_TWO" style="width:500px;margin-left:-250px;">
    
      <div id="roundeddivs_wireframe" style="width:200px;float:left;">

        <!-- The table with title at head of container. -->
        <div id="roundeddivs_headertab" align="center">
          <h2 style="color:#fff">Add:</h2>
        </div>

        <!-- F is for FORM FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF -->
        <!-- FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF -->
        <form name="myForm">
        <p>New <%=I.DN().NINJA%> Registration:</p>
        <p>
          Name : <input type="text" name="iName"  ng-model="nName" 
                        placeholder="Enter name here" style="width:100%;" >
        </p>

        <!-- using number type is not what I want for phone numbers. It gives me a spinner! -->
        <p>Phone: <input type="text" 
                         name="iPhone" 
                         ng-model="nPhone" 
                         style="width:100%;"    
                         ng-required="number"
                         ng-minlength="3"
                         ng-maxlength="13"
                         placeholder="##########">
            <span class="error" ng-show="myForm.iPhone.$error.minlength">
              Too short!
            </span>
            <span class="error" ng-show="myForm.iPhone.$error.maxlength">
              Too long!
            </span>
        </p>
        <p>
           Email: <input type="text" name="iEmail" ng-model="nEmail" 
           placeholder="Required@Email.com" style="width:100%;" >
        </p>

        <p>Website / Portfolio / Resume URL: </p>
        <p><input type="text" name="iPortfolio" ng-model="nPortfolio" placeholder="www.Zombo.com" style="width:100%;" ></p>
        <button data-ng-click="onButtonClicked()" style="width:100%;" >REGISTER</button>

        <!--  <textarea readonly>{{apiCallUsed}}</textarea> -->
        </form>
        <!-- FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF -->
        
        <!-- quick test to make sure we have relative .css URL correct. -->
        <spinner name="html5spinner">  
          <div class="overlay"></div>
          <div class="spinner">
                <div class="double-bounce1"></div>
                <div class="double-bounce2"></div>
          </div>
          <div class="please-wait">Test of loading spinner...</div>
        </spinner> 
      </div> <!-- SIGN UP FORM DIV ABOVE -->
    
    
      <div id="roundeddivs_wireframe" ng-show="has_a_ninja_been_made"
      style="width:300px;float:right;border-color:#FF8800;">

        <!-- The table with title at head of container. -->
        <div id="roundeddivs_headertab" align="center" style="background:#FF8800;">
          <h2 style="color:#fff">Just Made:</h2>
        </div>

        <!-- F is for FORM FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF -->
        <!-- FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF -->
        <form name="myForm">
        
          <p>Name : <b>{{prev_ninja.name}}</b> </p>

        <!-- using number type is not what I want for phone numbers. It gives me a spinner! -->
        <p>Phone: <b>{{prev_ninja.phone}}</b> </p>
        <p>Email: <b>{{prev_ninja.email}}</b> </p>

        <p>Website / Portfolio / Resume URL: </p>
        <p><b>{{prev_ninja.portfolio_url}}</b></p>
        
        <p><i>You can make more, or:</i></p>
        <button data-ng-click="goto_home()" 
        style="width:100%;" >
          GOTO <%=I.DN().ADMIN.toUpperCase()%> HOME
        </button>

        <!--  <textarea readonly>{{apiCallUsed}}</textarea> -->
        </form>
        <!-- FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF -->
      </div> <!-- JUST MADE DIV -->
    
    </div> <!-- PACK_OF_TWO -->
    
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
app.controller('myCtrl', function($scope, $http, $window, spinnerService) {
    

    //set function reference:
    $scope.onButtonClicked = onRegisterButtonClick;
    
    $scope.VI = function(){
      $scope.has_a_ninja_been_made=false;
      $scope.apiCallUsed='ONE';
      $scope.prev_ninja = {};
      $scope.prev_ninja.<%=I.OT().NINJA.NAME%> = "MASTER SPLINTER";
      $scope.prev_ninja.<%=I.OT().NINJA.EMAIL%>= "NINJA@EMAIL.COM";
      $scope.prev_ninja.<%=I.OT().NINJA.PHONE%>= "555-555-555";
      $scope.prev_ninja.<%=I.OT().NINJA.PORTFOLIO_URL%> = "ENTER_THE_NINJA.COM";
      tryToLoadScripts();
    };//FUNC::END

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
      spinnerService.hide('html5spinner');
      
      $scope.has_a_ninja_been_made    = true;
      $scope.prev_ninja.<%=I.OT().NINJA.NAME%>          = response.<%=I.OT().NINJA.NAME%>;
      $scope.prev_ninja.<%=I.OT().NINJA.EMAIL%>         = response.<%=I.OT().NINJA.EMAIL%> ;
      $scope.prev_ninja.<%=I.OT().NINJA.PHONE%>         = response.<%=I.OT().NINJA.PHONE%> ;
      $scope.prev_ninja.<%=I.OT().NINJA.PORTFOLIO_URL%> = response.<%=I.OT().NINJA.PORTFOLIO_URL%> ;
    }
    
    $scope.goto_home = function(){
      msg_for_user     = "Redirecting...";
      
      //http://stackoverflow.com/questions/4709037/
      //                                    window-location-versus-just-location
      $window.location = "admin_home.jsp";
    };//FUNC::END
    
    //Require-Js section of app:
    //RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR//
    console.log("REQUIRE JS!");

    var rjs_app = null;
    var has_rjs_app = false;

    var myCallback = function(arg_app){
      console.log("RequireJS Scripts Loaded");
      arg_app.setMaxFramesPerPlay(33);
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

<%-- This .JSP page should basically be a normal .JSP page except for this  --%>
<%-- ONE AND ONLY IMPORT and the references to it.                          --%>
<%@ page import="frontEndBackEndIntegration.I" %>