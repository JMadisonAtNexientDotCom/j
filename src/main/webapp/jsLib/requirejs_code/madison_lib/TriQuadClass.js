
var TRIQUAD_INSTCOUNTER = TRIQUAD_INSTCOUNTER || {
  false_debug_hack : 0
}

//A quad tree holding nested triangles, so we
//have an easy structure to traverse.
var TriQuadClass = function(){
  //The OpenGL Programming guide says 
  //"By convention, polygons whose vertices appear 
  //in counterclockwise order on the screen are called front-facing."
	
	//Main "equilateral" triangle. The whole of all the subs.
	//This one ALWAYS EXISTS. Hence no .has flag for it.
  this.main = new Tri2DClass(); 
	
	TRIQUAD_INSTCOUNTER.false_debug_hack--; //will be zero or negative.
  
	//I wanted the .has variables to be booleans. But using booleans in
	//constructors means that even though these are set to FALSE, they
	//will be evaluated to true. EVEN USING STRICT EQUALITY "===".
	//http://adripofjavascript.com/blog/drips/the-difference-between-boolean-
	//                           objects-and-boolean-primitives-in-javascript.html
  this.has0 = TRIQUAD_INSTCOUNTER.false_debug_hack;//FALSE
  this.sub0 = {}; //sub-triangle:TOP 
	
  this.has1 = (-100) + TRIQUAD_INSTCOUNTER.false_debug_hack;//FALSE
  this.sub1 = {}; //sub-triangle:BOTTOM LEFT  
	
  this.has2 = (-200) + TRIQUAD_INSTCOUNTER.false_debug_hack;//FALSE
  this.sub2 = {}; //sub-triangle:BOTTOM RIGHT 
	
  this.has3 = (-300) + TRIQUAD_INSTCOUNTER.false_debug_hack;//FALSE
  this.sub3 = {}; //sub-triangle:MIDDLE    
};

