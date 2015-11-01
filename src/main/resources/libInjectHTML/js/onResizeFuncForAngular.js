
    //ON][RESIZE][ON][RESIZE][ON][RESIZE][ON][RESIZE][ON][RESIZE][ON][RESIZE]///
      //This script expects:
      //1: A div named "angular_div"
      //2: Angular controller to have function called: 
      //   "$scope.onResizedAndStable"
    
      var _time_of_last_detected_resize = 0;
      var _time_of_resize_event_dispatch = 0;
      var _window_wid   = 0;
      var _window_hig   = 0;
      var _resize_count = 0;
      var _resize_event_launched = false; //prevent spam of resizing.
      
      function onResizeFuncForAngular(){
        _resize_count++;
        console.log("resized!" + _resize_count);
        
        cacheWindowSize();
        _time_of_last_detected_resize = getTimeMS();
        
        //If a resize event is not in our queue, then
        //queue up a resize that will happen with a delay.
        //By using a delay, we prevent resize code from executing
        //every single millisecond as the window is resized.
        if(false === _resize_event_launched){
          _resize_event_launched = true;
          _time_of_resize_event_dispatch = _time_of_last_detected_resize;
          setTimeout(doResize, 100);
        }
      }//FUNC::END
      
      function cacheWindowSize(){
        _window_wid = window.innerWidth;
        _window_hig = window.innerHeight;
      }//FUNC::END
      
      function doResize(){
      
        //It is possible that the delayed doResize() call may be called while
        //the user is still moving the window. In this case, we want to
        //"procrastinate" and put off the resize till a later time:
        if(_time_of_resize_event_dispatch !== _time_of_last_detected_resize){
          console.log("procrastinate resize");
          _time_of_resize_event_dispatch = _time_of_last_detected_resize;
          setTimeout(doResize, 100);
          return;
        }
      
        console.log("doResize()");
        //use currently cached window size to re-initialize:
        // http://stackoverflow.com/questions/16709373/
        var scope = angular.element(document.getElementById("angular_div")).scope();
        scope.onResizedAndStable();
      
        //now we can listen for more resize events.
        _resize_event_launched = false;
      }//FUNC::END
      
      //Get time in milliseconds
      function getTimeMS(){
        var d = new Date();
        var n = d.getTime();
        return n;
      }//FUNC::END
    //[ON][RESIZE][ON][RESIZE][ON][RESIZE][ON][RESIZE][ON][RESIZE][ON][RESIZE]//
  