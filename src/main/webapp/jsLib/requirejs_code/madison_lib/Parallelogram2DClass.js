
//Depends on Tri2DClass
//           Tri2DClass depends on Point2DClass
var Parallelogram2DClass = function(){

  //Parallelogram is made up of two triangles:
  this.tri0 = new Tri2DClass(); //Tri on LEFT  by convention.
	this.tri1 = new Tri2DClass(); //Tri on RIGHT by convention.
	
	this.setTris = function(arg_tri0, arg_tri1){
	  this.tri0 = arg_tri0;
		this.tri1 = arg_tri1;
	};//FUNC::END
	
};//CLASS::END