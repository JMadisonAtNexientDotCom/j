//var LOOPAPP = LOOPAPP || {};
var LOOPAPP = {};
LOOPAPP.guts = {
  _hasFunc: false,
  _alltime : {},
  _isPaused:false,
  pause:function(){
    LOOPAPP.guts._isPaused = true;
  },
  resume:function(){
    LOOPAPP.guts._isPaused = false;
    LOOPAPP.guts.wrapper();
  },
  toggle:function(){
    console.log("TOGGLE() HIT!");
    if(LOOPAPP.guts._isPaused){
      LOOPAPP.guts.resume();
    }else{
      LOOPAPP.guts.pause();
    }//
  },
  wrapper : function(){
    if(LOOPAPP.guts._isPaused){return;}
		
		if(Array.isArray(LOOPAPP.guts._alltime)){
			var len = LOOPAPP.guts._alltime.length;
			for(var i=0; i<len; i++){
			  LOOPAPP.guts._alltime[i]();
			}//next i
		}else{
			LOOPAPP.guts._alltime();
	  }
		
    //console.log(LOOPAPP.guts._hasFunc);
    setTimeout(LOOPAPP.guts.wrapper, 300);
  },
  start : function(in_loopfunc){
    LOOPAPP.guts._alltime = in_loopfunc;
    LOOPAPP.guts.resume();
  }
}