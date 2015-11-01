<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>nts welcome</title>
    
    <script src="../jsLib/requirejs_code/require.js"></script>
				<script src="../jsLib/requirejs_code/config/nts_config.js"></script>
				
    <%= I.INCLUDE_JS() %>
    <%= I.INCLUDE_CSS() %>
    
  </head>
<!-- AUTHOR: JMadison.  ON:20##.##.##_####AMPM                               -->
<!-- TABSIZE: 2 Spaces.                                                      -->
<!-- This header belongs BELOW the <head></head> declaration.                -->
<!--   10|       20|       30|       40|       50|       60|       70|       -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->
  <body onresize="onResizeFuncForAngular()" >
    
    
    <div id="dyna_header_div" style="background:#00ff00;width:50px;height:50px;">
    <canvas id="dyna_header_canvas" width="55" height="55"></canvas>
    </div>
    
    <!-- Rather than having code execute for the footer, just make it clone header data -->
    <div id="clone_footer_div" 
    style="background:#ff8800;width:100%;height:50px;
    position:absolute;bottom:0px;">
    <canvas id="clone_footer_canvas" width="55" height="55"></canvas>
    </div>
    
    <div id="angular_div"
      data-ng-app="myApp" data-ng-controller="myCtrl" data-ng-init="VI();" >
      <!-- Vertically + horizontally centered dialog -->
      <div class="horcen_parent" > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      <div class="horcen_child"  > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      <div class="vertcen_parent"> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      <div class="vertcen_child" > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
        
        <div id="vertical_center_hack"
        style="margin-left:-100px;">
          <div id="div_for_canvas" 
          style="position:absolute; 
          z-index:1; margin-left:-50px; margin-top:-10px;">
            <canvas id="myCanvas"
            width="300" height="180" 
            style="border:0px solid #d3d3d3;">
            Your browser does not support the HTML5 canvas tag.
            </canvas>
          </div>

          <div id="roundeddivs_wireframe" 
          style="width:200px; z-index:2; position:relative;">

            <div id="roundeddivs_headertab" align="center">

              <h2 style="color:#fff">Welcome!</h2>

            </div>
              <p></p>
              <button data-ng-click="ninjaLogin();" style="width:100%;"><%= I.DN().NINJA%> Login</button>
              <button data-ng-click="adminLogin();" style="width:100%;"><%= I.DN().ADMIN%> Login</button> 
          </div>
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
        $scope.isDebugMode = false;
        $scope.appInitialized = true;
        hack_waitThenTryToLoad();
      };//FUNC::END
      
      //To be called when the window has been resized and is
      //no longer moving (stable).
      $scope.onResizedAndStable = function(){
        $scope.initHeaderAndFooters();
      };//FUNC::END
      
      $scope.ninjaLogin = function(){
        location.href = '/nts/give_me_your_token.jsp';
      };//FUNC::END
      
      $scope.adminLogin = function(){
        location.href = '/nts/prove_you_are_admin.jsp';
        
      };//FUNC::END
      
      //Require-Js section of app:
      //RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR//
      //$scope.doIt = function(){
      //  console.log("pressed");
      //$scope.loaderToggle();
      //};//
      
      console.log("REQUIRE JS!");
      
      //canvas references that are set when header fractal is initialized:
      var _head_can = null;
      var _head_ctx = null;
      var _foot_can = null;
      var _foot_ctx = null;
          
      var rjs_app = null;
      var has_rjs_app = false;
      var attempt = 0;
          
      var myCallback = function(arg_app){
        console.log("called back");
        console.log("arg_app==" + arg_app);
        arg_app.setMaxFramesPerPlay(15);
        arg_app.resume();
        rjs_app = arg_app;
        has_rjs_app = true;
        
        //Header and footer code depends on requireJS scripts:
        $scope.initHeaderAndFooters();
        
      };//
      
      //App returned when loading first strip loader:
      var onStripLoaderCallback = function(arg_app){
        console.log("onStripLoaderCallback");
        
        _has_head_app = true;
        _head_app = arg_app;
        
        maybeInitHeaderFractal();
        
      };//FUNC::END
      
      //Call when everything is ready:
      var initHeaderFractal = function(){
      
        //set references to header+footer canvases:
        _head_can = document.getElementById("dyna_header_canvas");
        _head_ctx = _head_can.getContext("2d");
        _foot_can = document.getElementById("clone_footer_canvas");
        _foot_ctx = _foot_can.getContext("2d");
      
        _head_app.canvas_id_name = "dyna_header_canvas";
        _head_app.setFrameDrawCallback(doFooterDraw);
        _head_app.init();
        _head_app.resume();
      };//
      
      //Copy data in header graphic to footer:
      var doFooterDraw = function(){
        var idata = _head_ctx.getImageData(0,0,_head_targ_wid,_head_targ_hig);
        _foot_ctx.putImageData(idata,0,0);
      };//FUNC::END
      
      var maybeInitHeaderFractal = function(){
        if(_has_head_app && _head_div_ready){
          initHeaderFractal();
        }
      };//FUNC::END
          
      var myErrback = function(arg_app){
        console.log("errored back");
        console.log("app==" + arg_app);
      };//FUNC::END
          
      var tryToLoad = function(){
        //Path to app must be relative location from this HTML file:
        //var rel_path = "../jsLib/requirejs_code/madison_app/NexFracLoader";
        //require([rel_path], myCallback, myErrback);
        
        requirejs(['app_rel_path/NexFracLoader'], myCallback, myErrback);
        requirejs(['app_rel_path/NexFracStripLoader'], 
                    onStripLoaderCallback, myErrback);
        
      };//
      
      var hack_waitThenTryToLoad = function(){
        console.log("Loading hack initialized");
        setTimeout( tryToLoad(), 13000 ); //13 seconds.
      };

      $scope.loaderToggle = function(){
        rjs_app.toggle();
      };//
      //RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR//
      
      //header and footer code:
      //HFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHF//
      
      var _head_targ_wid = 0;
      var _head_targ_hig = 0;
      var _head_app = null; //the actual requireJS app in charge of header.
      var _has_head_app = false; //does a header app ref exist yet?
      var _head_div_ready = false; //is the header div ready yet?
      
      $scope.initHeaderAndFooters = function(){
      
        _head_div_ready = false;
      
        console.log("initHeaderAndFooters!");

        var win_wid = window.innerWidth;
        var win_hig = window.innerHeight;
        var header_div = document.getElementById("dyna_header_div");
        var footer_div = document.getElementById("clone_footer_div");
        var stretch_time; //how long to stretch banner to fit?
        if(false===$scope.isDebugMode){
          header_div.style.background = "transparent";
          footer_div.style.background = "transparent";
          stretch_time = 0;
        }else{
          header_div.style.background = "#ff0000";
          footer_div.style.background = "#ff8800";
          stretch_time = 2;
        }
        
        _head_targ_wid = win_wid;
        _head_targ_hig = 50;
        TweenMax.to(header_div,stretch_time,
                   {height:_head_targ_hig, 
                    width :_head_targ_wid, 
                    onComplete:onHeaderDoneResizing});
      }//
      
      function onHeaderDoneResizing(){
        _head_div_ready = true;
        draw();
        maybeInitHeaderFractal();
      }//
    
      function draw(){
        var canvas_container = document.getElementById("dyna_header_canvas");
        canvas_container.width = _head_targ_wid;
        canvas_container.height= _head_targ_hig;
        
        //make the footer match the header:
        var foot = document.getElementById("clone_footer_canvas");
        foot.width = _head_targ_wid;
        foot.height= _head_targ_hig;
        
        drawCanvas(foot);
      }//
    
      function drawCanvas(canvas_container){
        fillCanvas(canvas_container);
        
        
      }//FUNC::END
      
      function fillCanvas(canvas_container){
        console.log("fillCanvas");
        var wid = canvas_container.width;
        var hig = canvas_container.height;
        can_ctx = canvas_container.getContext("2d");
        can_ctx.fillStyle = "blue";
        can_ctx.fillRect(0, 0, wid, hig);
      }//FUNC::END
      
    //HFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHFHF//
    
    $scope.angularPing = function(){
      console.log("angular ping!");
    };//
    
      
    });//Controller, end
    </script>
    
    <%-- OnResizeFunction for angularJS controllers. --%>
    <%=I.INLINE_SCRIPT_BLOCK(I.SBN().ON_RESIZE_FUNC_FOR_ANGULAR)%>
      
  </body>
</html>
<!--   10|       20|       30|       40|       50|       60|       70|       -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->

<%-- This .JSP page should basically be a normal .JSP page except for this  --%>
<%-- ONE AND ONLY IMPORT and the references to it.                          --%>
<%@ page import="frontEndBackEndIntegration.I" %>