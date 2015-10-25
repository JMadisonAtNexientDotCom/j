//
// NOTE: Useage of this class requires using require.js so that dependencies
//       can be loaded.
//
//Dependency hierarchy:
//======CLASS:===================|==============DEPENDENCIES===================+
//                                                                             |
//Point2DClass                   | NO DEPENDECIES                              |
//StackClass                     | NO DEPENDENCIES                             |
//CanvasKnockBackerFilterClass   | NO DEPENDENCIES                             |
//                                                                             |
//PermuterClass                  | StackClass                                  |
//                                                                             |
//SUB_TRI_BUILDER                | TriQuadClass, Tri2DClass, Point2DClass      |
//                                                                             |
//SUB_TRI_RENDERER               | TriQuadClass, Tri2DClass, Point2DClass      |
//                                                                             |
//SUB_TRI_REN_PERMUTE            | SUB_TRI_RENDERER, PermuterClass             |
//                                                                             |
//ITALLIC_PARALLELOGRAM_BUILDER  | Parallelogram2DClass, Tri2DClass,           |
//                               | Point2DClass                                |
//                                                                             |
//Parallelogram2DClass           | Tri2DClass, Point2DClass                    |
//                                                                             |
//Tri2DClass                     | Point2DClass                                |
//=============================================================================+


//Pre-loader animation that will set itself up on a canvas.
//Constructor requires the CANVAS object, NOT THE CONTEXT.
var NEXIENT_LOADER = function(canvas_container){

  //Starts the animation:
  this.start = function(){
	
	};//
	
	//Stops the animation:
	this.stop = function(){
	
	};//
	
	var construct = function(_self,canvas_container){
	
  };//
	
	//Build object!
	construct(this, canvas_container);
	
};