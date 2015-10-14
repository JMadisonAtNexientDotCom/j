<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title> <%=I.DN().TRIAL%> Assigner</title>
    
    <%= I.INCLUDE_JS()  %>
    <%= I.INCLUDE_CSS() %>
		
  </head>
<!-- AUTHOR: JMadison.  ON:2015.09.18_0223AMPM                               -->
<!-- TABSIZE: 2 Spaces.                                                      -->
<!-- This header belongs BELOW the <head></head> declaration.                -->
<!--   10|       20|       30|       40|       50|       60|       70|       -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->
  <body>
    <div id="PIKA" data-ng-app="myApp" data-ng-controller="myCtrl" data-ng-init="VI();">
      
      <!-- Vertically + horizontally centered dialog -->
      <div class="horcen_parent" > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      <div class="horcen_child"  > <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
   
        <div id="roundeddivs_wireframe" style="width:100%; height:100%;">

          <div id="roundeddivs_headertab" align="center" style="margin-bottom:25px;">
            <h2 style="color:#fff"><%=I.DN().TRIAL%> Assigner</h2>
          </div>
          
          <div style="width:600px; height:400px;"> <!-- split container::START -->
            <div class="panel panel-default" style="width:300px; height:100%; float:left">
            <div class="panel-heading">Who below will be taking this <%=I.DN().TRIAL%>?</div>


              <form name="userForm" style="width:100%;" novalidate>
                <select name="multipleSelect" data-ng-change="onListChange( formData )"
                style="width:100%; height:280px;" 
                id="mulipass" ng-model="formData.multipleSelect" multiple>
                  <!-- This chunk repeats for each item in users collection          -->
                  <!-- RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR -->
                  <option data-ng-repeat="ninja in instancedJSON_01.members" 
                           value="{{ninja}}">
                                  {{ninja.<%=I.OT().NINJA.NAME%>}}
                  </option>
                  <!-- RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR -->
                </select> <!-- form-group -->

               <nav class="navbar navbar-default">
                <ul class="pager">
                 <li class="previous" id="ninja_list_prv"><a href=""><span aria-hidden="true">&larr;</span> PREV</a></li>
                 
                  <li type="button" style="padding:0px;" class="btn btn-primary">{{ninja_page_title}}</li>
                 
                 <li class="next" id="ninja_list_nxt"><a href="#">NEXT <span aria-hidden="true">&rarr;</span></a></li>
                </ul>
               </nav>

                <!--
                useful for showing you what is selected from the list:
               <tt>multipleSelect = {{data.multipleSelect}}</tt><br/>
                -->

              </form>  <!-- userForm   -->
            </div> <!--panel -->
          
            <!-- THE RIGHT SIDE OF THE UI: HOLDER FOR MULTIPLE PANELS. ----- -->
            <div class="panel panel-default" 
            style="position:relative; z-index:0;
            width:300px; height:100%;
            float:right;background:#AAAAAA;">

              <div      id="subpanel_phase___SELECT_A_NINJA_PROMPT"
              data-ng-show="subpanel_phase===SELECT_A_NINJA_PROMPT;"
              style="z-index:1;background:#4488FF;position:absolute;float:none;
              color:#FFFFFF;
              width:100%; height:100%;"> <!--11111111111111111111111111111111-->
                
                WELCOME! To begin, select a <%=I.DN().NINJA%> from the list to the left.

              </div><!--11111111111111111111111111111111111111111111111111111-->
              <div      id="subpanel_phase___PLEASE_WAIT_LOADING_NINJA_LIST"
              data-ng-show="subpanel_phase===PLEASE_WAIT_LOADING_NINJA_LIST;"
              style="z-index:2;background:#FF8800;position:absolute;float:none;
              width:100%; height:100%;"><!--222222222222222222222222222222222-->
                
                WAIT! We are loading the list of <%=I.DN().NINJA%>'s for you.
                
              </div> <!--2222222222222222222222222222222222222222222222222222-->
              <div      id="subpanel_phase___ASSIGN_TRIAL_TOKEN_TO_NINJA"
              data-ng-show="subpanel_phase===ASSIGN_TRIAL_TOKEN_TO_NINJA;"
              style="z-index:3;background:#AAAAAA;position:absolute;float:none;
              width:100%; height:100%;"><!--333333333333333333333333333333333-->
                
                <button class="btn" id="token_giver_button">
                  DISPATCH {{formData.multipleSelect.length}} TOKEN(S)
                </button>
                
              </div> <!--3333333333333333333333333333333333333333333333333333-->
              <div      id="subpanel_phase___PLEASE_WAIT_CREATING_TRIAL"
              data-ng-show="subpanel_phase===PLEASE_WAIT_CREATING_TRIAL;"
              style="z-index:4;background:#FF8800;position:absolute;float:none;
              width:100%; height:100%;"><!--444444444444444444444444444444444-->
                
                WAIT! We are creating a <%=I.DN().TRIAL%> for this ninja. 
                A token you can give to this <%=I.DN().NINJA%> will be 
                available shortly.
                
              </div> <!--4444444444444444444444444444444444444444444444444444-->
              <div      id="subpanel_phase___GIVE_NINJA_BOB_THIS_TOKEN"
              data-ng-show="subpanel_phase===GIVE_NINJA_BOB_THIS_TOKEN;"
              style="z-index:5;background:#22FFAA;position:absolute;float:none;
              color:#000000;
              width:100%; height:100%;"><!--555555555555555555555555555555555-->
                
                <!-- [80][80][80][80][80][80][80][80][80][80][80][80][80][80]-->
                <div style="width:100%; height:80%;">
                <!-- SCROLLING CONTENT! -->
                <!-- SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS[SCROLL::START]SSSS -->
                <div id="my_scroll_bar" 
                ng-scrollable="{scrollX:'none',scrollY:'right'}" 
                style="width: 100%; height: 100%;background:#AA0000;">
                    <form name="userForm" novalidate
                    style="width: 95%; height:100%;" >
                      <div class="form-group" 
                      data-ng-repeat="ticket in coffer.tickets" >
                        <!-- Repeats for each item in users collection       -->
                        <!-- RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR -->
                        <data-ng-form name='userFieldForm'>
                          <label>Ninja {{ ticket.ninja_name }}'s TOKEN: </label>
                          <input class='form-control' name='token_entry' 
                          ng-model='ticket.token_hash' required='' type='text'>
                          </input>
                        </data-ng-form>
                        <!-- RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR -->
                      </div> <!-- form-group -->
                    </form>  <!-- userForm   -->
                </div> <!-- my_scroll_bar -->
                <!-- SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS[SCROLL::END]SSSS -->
                </div>
                <!-- [80][80][80][80][80][80][80][80][80][80][80][80][80][80]-->
                
                <!-- [20][20][20][20][20][20][20][20][20][20][20][20][20][20]-->
                <div style="width:100%; height:20%;">
                  <h2>DONE!</h2>
                </div>
                <!-- [20][20][20][20][20][20][20][20][20][20][20][20][20][20]-->
                
              </div> <!--5555555555555555555555555555555555555555555555555555-->
            </div> <!--panel::THE RIGHT SIDE. END -->
          </div><!-- split container::END -->
          
          
          

        </div> <!-- ROUNDED BLUE CORNERS -->
      </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      </div> <!-- CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC -->
      
      
      
      
      
		
    </div> <!-- APP::END -->
		
		<script>
		$(function() { 
    //http://dotnet-concept.com/Tip/2015/6/5798829/
    //                   How-to-call-angularJS-function-from-javascript-jquery
    
    //Reading up on this, says I should not do it the way above I mentioned
    //because it will NOT work in debug mode:
    //http://www.toptal.com/angular-js/
    //                           top-18-most-common-angularjs-developer-mistakes
    //UNFORTUNATELY, DOES NOT OFFER AN ALTERNATIVE!!!

    $("#token_giver_button").click(function(){
    angular.element(document.getElementById('PIKA')).scope().dispatchTokens();
    });

    $("#ninja_list_prv").click(function() {
    angular.element(document.getElementById('PIKA')).scope().goBAK();
    });

    $("#ninja_list_nxt").click(function() {
    angular.element(document.getElementById('PIKA')).scope().goFWD();
    });
			
		});
		</script>
		
		
		
    <script>
    var app = angular.module('myApp', ['ngScrollable']);
    app.controller('myCtrl', function($scope, $http) {
      //function to execute on click
      $scope.onClick = function(){
        var obj = JSON.parse(document.getElementById('embeddedJSON_01').innerHTML);
        $scope.instancedJSON_01=obj;
      };//FUNC::END
      //Variable initialization:
      $scope.VI = function(){
        
        //INITIALIZE CONSTANTS:
        $scope.SELECT_A_NINJA_PROMPT          = 1; //Tell admin what to do.
        $scope.PLEASE_WAIT_LOADING_NINJA_LIST = 2; //ASYNC LOADING
        $scope.ASSIGN_TRIAL_TOKEN_TO_NINJA    = 3; //Let admin dispatch token.
        $scope.PLEASE_WAIT_CREATING_TRIAL     = 4; //ASYNCH LOADING
        $scope.GIVE_NINJA_BOB_THIS_TOKEN      = 5; //Tell admin what to do.
        $scope.subpanel_phase = $scope.SELECT_A_NINJA_PROMPT;
        
        $scope.instancedJSON_01 = {};
        $scope.isInitialized = true;
       	$scope.pageIndexWanted   = 0;
        $scope.numResultsPerPage = 8;
        $scope.ninja_page_title = "READY";
        
        <%-- Embed the request schema for DISPATCH_TOKENS POST call. --%>
        <%= I.API().DISPATCH_TOKENS.EMBED_REQUEST_SCHEMA("$scope.arg_obj", 4) %>
        
        $scope.callService();
        
      };//FUNC::END
      
      //Function execute when item is click on
      //from the multi-select ninja-selector.
      $scope.onListChange = function(){
        console.log("onListChange() called!");
        $scope.subpanel_phase = $scope.ASSIGN_TRIAL_TOKEN_TO_NINJA;
      };//FUNC::END
      
      $scope.dispatchTokens = function(){
        
        $scope.subpanel_phase = $scope.PLEASE_WAIT_CREATING_TRIAL;
        console.log("dispatchTokens() was entered!");
        $scope.$apply(); //http://stackoverflow.com/questions/22389949/div-with-ng-show-not-updating-after-ajax-call Maybe?
        $scope.postRequest();
        
      };//FUNC::END
			
      $scope.goBAK = function(){
        $scope.pageIndexWanted--;
        $scope.callService();
      };//FUNC::END

      $scope.goFWD = function(){
        $scope.pageIndexWanted++;
        $scope.callService();
      };//FUNC::END

      //TODO: Some more comprehensive logic.
      $scope.goTOG = function(){
        $scope.pageIndexWanted = 2;
        $scope.callService();
      };//FUNC::END
      
      //HTTP POST REQUEST FOR DIPATCHING TOKENS:
      $scope.postRequest = function(){
        var ids = create_id_list();
        $scope.arg_obj.<%= I.API().DISPATCH_TOKENS.POSTARG.NINJA_ID_LIST %> = ids;
        $scope.arg_obj.<%= I.API().DISPATCH_TOKENS.POSTARG.DURATION_IN_MINUTES%> = 30;
        $scope.arg_obj.<%= I.API().DISPATCH_TOKENS.POSTARG.TRIAL_KIND %> = <%= I.PV().TRIAL_KIND.RIDDLE_TRIAL %>
        postURL = "<%= I.API().DISPATCH_TOKENS.URL %>";
        $http.post(postURL,$scope.arg_obj).success(onPostResponse);
      };//FUNC::END
      
      
     //Create list of ninjas from whatever was selected in UI:
      function create_id_list(){
        //The ninjas selected in the UI's mutliple select picker window:
        //
        //HACK:
        //
        //For some reason, each entry in this objec is non-parsed JSON, even
        //though it operates on a parsed json object. I have no clue why.
        //hence why we have the JSON.parse line in the for loop.
        //Possibly if data format changes, so that arr[ninja_dex] is an
        //actual object, this code will break.
        var arr = $scope.formData.multipleSelect;
     
        var ids = [];
        var cur_ninja_json;
        var cur_ninja;
        var cur_id;
        var ninja_dex;
        
        for (ninja_dex in arr){
          cur_ninja_json = arr[ninja_dex];
          cur_ninja      = JSON.parse(cur_ninja_json); //<--HACK
          cur_id         = cur_ninja.id;
          ids.push( cur_id );
        }//next ninja
       
        return ids;
    
      }//FUNC::END
      
      
      //Response to $scope.postRequest. No $scope on this because we do not
      //want it to be exposed outside of the controller.
      function onPostResponse(response){
        
        //When response is given back, we can give the tokens!
        console.log("onPostResponse() was entered!");
        $scope.subpanel_phase = $scope.GIVE_NINJA_BOB_THIS_TOKEN;
        $scope.coffer = response; //the ticket coffer given by server.
        console.log("fist token in ticket coffer:" + $scope.coffer.tickets[0].token_hash);
        $scope.$emit('content.changed'); //<-- to register scroll bar change.
        $scope.$apply();
        
      }//FUNC::END

      $scope.callService = function()
      {
        $scope.subpanel_phase = $scope.PLEASE_WAIT_LOADING_NINJA_LIST;
        $scope.ninja_page_title = "LOADING";
        
        serviceURL = "<%= I.API().GET_PAGE_OF_NINJAS.URL %>";

        //Build query string using J-QUERY:
        //SOURCE: http://stackoverflow.com/questions/316781/
        //                             how-to-build-query-string-with-javascript
        qs = "";
        qs = $.param({ 
        <%= I.API().GET_PAGE_OF_NINJAS.ARG.PAGE_INDEX %>:$scope.pageIndexWanted, 
        <%= I.API().GET_PAGE_OF_NINJAS.ARG.NUM_RESULTS_PER_PAGE %>:$scope.numResultsPerPage});

        //url of rest-api responsible for making new ninja records using arguments:
        apiURL= serviceURL + "?" + qs;

        $http.get(apiURL).success( onResponded );
      };//FUNC::END

      function onResponded(response){
        $scope.instancedJSON_01 = response;
        $scope.ninja_page_title = response.displayName;
        $scope.subpanel_phase = $scope.SELECT_A_NINJA_PROMPT;
      };//FUNC::END
						
			
			
    });//APP::END
    </script>
		
    <script type="application/json" id="embeddedJSON_01">
      {
      "members":[ { "name":"bob", "email":"bob@bob.com"},
                   { "name":"sam", "email":"sam@sam.com"},
                   { "name":"jay", "email":"jay@jay.com"}],

      "unicorns": "awesome",
      "abc": [1, 2, 3]
      }
    </script>
  </body>
</html>
<!--   10|       20|       30|       40|       50|       60|       70|       -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->

<%-- This .JSP page should basically be a normal .JSP page except for this  --%>
<%-- ONE AND ONLY IMPORT and the references to it.                          --%>
<%@ page import="frontEndBackEndIntegration.I" %>