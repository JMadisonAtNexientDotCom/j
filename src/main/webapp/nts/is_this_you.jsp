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
        
        <div id="two_halves" 
        style="width:600px; height:200px;   margin-left:-300px;">
          
          <div id="roundeddivs_wireframe" style="width:200px; float:left;">

            <div id="roundeddivs_headertab" align="center">

              <h2 style="color:#fff">Is This You?</h2>
              <p> {{active_token_hash}} </p>

            </div>
              <button data-ng-click="proceed_valid();" style="width:100%;">Yes, That's me!</button>
              <button data-ng-click="proceed_bogus();" style="width:100%;">No.</button>
              <p><b>Not you?</b> That's okay, just let us know!</p>

          </div>

          <div id="roundeddivs_wireframe" 
          style="width:400px; height:100%; float:left;">

            <div id="roundeddivs_headertab" align="center">

              <h2 style="color:#fff">User Info:</h2>
              <p> {{active_token_hash}} </p>

            </div>
            
            <div id="roundeddivs_inner_docked_box">
              
              <div style="width:390px; height:100%; position:relative; background:inherit; padding:0px; margin:0px; margin-top:5px;">
                <div id="collegerulepaper_sub_section_left" >
                  <div id="collegerulepaper_hor_line_whitespace">
                    
                  </div>
                  <div id="collegerulepaper_hor_pen_line"></div>
                  <div id="collegerulepaper_hor_line_whitespace">
                    
                  </div>
                  <div id="collegerulepaper_hor_pen_line"></div>
                  <div id="collegerulepaper_hor_line_whitespace">
                    
                  </div>
                  <div id="collegerulepaper_hor_pen_line"></div>
                  <div id="collegerulepaper_hor_line_whitespace">
                    
                  </div>
                  <div id="collegerulepaper_hor_pen_line"></div>
                </div>
                <div id="collegerulepaper_sub_section_divider_line">
                  
                </div>
                
                <div id="collegerulepaper_sub_section_right">
                  
                  <div id="lineitup_01" style="width:20%; float:left; ">
                    <div id="collegerulepaper_hor_line_whitespace_left_padded">
                      Name: 
                    </div>
                    <div id="collegerulepaper_hor_pen_line"></div>
                    <div id="collegerulepaper_hor_line_whitespace_left_padded">
                      Phone:
                    </div>
                    <div id="collegerulepaper_hor_pen_line"></div>
                    <div id="collegerulepaper_hor_line_whitespace_left_padded">
                      Email:
                    </div>
                    <div id="collegerulepaper_hor_pen_line"></div>
                    <div id="collegerulepaper_hor_line_whitespace_left_padded">
                      Portfolio:
                    </div>
                    <div id="collegerulepaper_hor_pen_line"></div>
                  </div> <!-- line it up! 01 -->
                  
                  <div id="lineitup_01" style="width:80%; float:left;">
                    <div id="collegerulepaper_hor_line_whitespace">
                      <b>{{maybe_you.name}}</b>
                    </div>
                    <div id="collegerulepaper_hor_pen_line"></div>
                    <div id="collegerulepaper_hor_line_whitespace">
                      <b>{{maybe_you.phone}}</b>
                    </div>
                    <div id="collegerulepaper_hor_pen_line"></div>
                    <div id="collegerulepaper_hor_line_whitespace">
                      <b>{{maybe_you.email}}</b>
                    </div>
                    <div id="collegerulepaper_hor_pen_line"></div>
                    <div id="collegerulepaper_hor_line_whitespace">
                      <b>{{maybe_you.portfolio_url}}</b>
                    </div>
                    <div id="collegerulepaper_hor_pen_line"></div>
                  </div> <!-- line it up! 01 -->
                  
                  
                </div>
             
              </div>
            </div>
              
            <div id="roundeddivs_footertab" align="center"
            style="width:390px; height:35px; bottom:5px;">
            </div>
			

          </div>
        </div> <!-- two halves -->
        
        
      </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
    </div>
  </div>
    
  <script>
  var app = angular.module('myApp', ['ngCookies','angularSpinners']);
  app.controller('myCtrl', function($scope, $http, $location, $cookies, spinnerService) {

    //variable initialization:
    $scope.VI = function(){
      $scope.msg_for_user = "Insert Token:";
      $scope.active_token_hash = $cookies.get("ACTIVE_TOKEN");
      
      $scope.maybe_you               = {}; //create empty object to populate.
      $scope.maybe_you.name          = "Ninja Bob";
      $scope.maybe_you.phone         = "(586)214-3958";
      $scope.maybe_you.email         = "Bob@Ninja.com";
      $scope.maybe_you.portfolio_url = "www.awesome.com";
      
      callService();
      
    };
    
    //Use the active_token_hash loaded in from cookies to display
    //That information of the Ninja that owns this token.
    function callService()
    {
      
      //spinnerService.show('html5spinner'); //<--where is ref to spinnerService?
      
      serviceURL = "http://j1clone01-madnamespace.rhcloud.com/api/NinjaCTRL/get_ninja_by_token_hash";
      qs = ""; //query string.
      qs = $.param({token_hash:$scope.active_token_hash});//QUERYSTRING::END

      apiCallURL = serviceURL + "?" + qs;
      
      
      $http.get(apiCallURL).success( onResponded );
    };//FUNC::END
    
    function onResponded(response){
      //http://stackoverflow.com/questions/8524933/
      //                                   json-parse-unexpected-character-error
      $scope.maybe_you = response; //response is already parsed, I think.
      //spinnerService.hide('html5spinner');
    }//FUNC::END
   
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

<%-- This .JSP page should basically be a normal .JSP page except for this  --%>
<%-- ONE AND ONLY IMPORT and the references to it.                          --%>
<%@ page import="frontEndBackEndIntegration.I" %>