<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Honors System:</title>
    <!-- spinner animation for waiting after pressing button that -->
<!-- results in an asynchronous call. AKA: Wait for something to load and -->
<!-- let user know the UI is still responsive. -->
<LINK REL=StyleSheet HREF="../jsLib/css/components/loading_spinner.css" TYPE="text/css">

<!-- Angular Material CSS now available via Google CDN; version 0.10 used here -->
<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/angular_material/0.10.0/angular-material.min.css">
<!-- MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM -->

<!-- style stuff for scrollbars! -->
<link rel="stylesheet" href="../jsLib/git_repo_3rd_party/ng_scrollable/ng-scrollable.min.css">

<!-- enable centering. -->
 <link href="../jsLib/css/layout/vertcen.css" rel="stylesheet">
 <link href="../jsLib/css/layout/horcen.css" rel="stylesheet">
 <link href="../jsLib/css/layout/bs_docs_example.css" rel="stylesheet">
 <link href="../jsLib/css/aesthetics/roundeddivs.css" rel="stylesheet">
 
<!-- make it look like school paper. -->
<link href="../jsLib/css/aesthetics/collegerulepaper.css" rel="stylesheet">

    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.7/angular.min.js"></script>

<!-- for angularJS cookies. SRC: https://code.angularjs.org/ -->
<!-- VERSION MUST BE IDENTICAL TO ANGULAR VERSION YOU ARE USING!!! -->
<script src="https://code.angularjs.org/1.4.7/angular-cookies.min.js"></script>
				
<!-- for async spinners. THEY WORK! -->
<script src="../jsLib/node_modules/angular-spinners/src/spinner-service.js"></script>
<script src="../jsLib/node_modules/angular-spinners/src/spinner-directive.js"></script>
<script src="../jsLib/node_modules/angular-spinners/dist/angular-spinners.js"></script>

<!-- host jquery so $ make sense? I think. -->
<!-- we probably don't need this version of Jquery since we are using another.
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
-->

<!-- BEFORE BOOTSTRAP INCLUDE: Bootstrap requiers JQuery Library.        -->
<script src="https://code.jquery.com/jquery-2.1.4.min.js"></script>
<!-- Bootstrap: .js and .css respectively.                               -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js" ></script>
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">

<!-- Angular Materials Project. Makes UI asthetically pleasing. -->
<!-- MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM -->
<!-- Angular Material Dependencies -->
<!-- include: angular.min.js -->
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.15/angular-animate.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.15/angular-aria.min.js"></script>

<!-- Angular Material Javascript now available via Google CDN; version 0.10 used here -->
<script src="https://ajax.googleapis.com/ajax/libs/angular_material/0.10.0/angular-material.min.js"></script>

<!-- Get some scroll bar action up in here! -->
<script src="../jsLib/git_repo_3rd_party/ng_scrollable/ng-scrollable.min.js"></script>

    
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

            </div>
              <button data-ng-click="proceed_valid();" style="width:100%;">Yes, That's me!</button>
              <button data-ng-click="proceed_bogus();" style="width:100%;">No.</button>
              <p><b>Not you?</b> That's okay, just let us know!</p>

          </div>

          <div id="roundeddivs_wireframe" 
          style="width:400px; height:100%; float:left;">

            <div id="roundeddivs_headertab" align="center">

              <h2 style="color:#fff">User Info:</h2>

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