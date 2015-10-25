

var PointNormalSegment2DClass = function(){

  this.p = new Point2DClass(); //point.
	this.n = new Point2DClass(); //normal.
	
	//The length of the segment used to create this point-normal.
	//If was not created using two points, just set it to 1.
	//Used to traverse segment by a percentage.
	this.original_length = 1;
	
	//gets position along the point-normal segment, by using a percentage.
	this.getSegPos = function(percent){
		
		var dist = this.original_length * percent;
		return this.getRayPos(dist);
	
	};//FUNC::END
	
	//gets position along point-normal ray, by using scalar.
	this.getRayPos = function(distance){
		var op = new Point2DClass();
		
		//Multiply normal by distance, and add resulting
		//vector onto the end of this.p to get tip.
		op.x = (this.p.x + (this.n.x * distance) );
		op.y = (this.p.y + (this.n.y * distance) );
		
		return op;
	};//FUNC::END
	
	
};//CLASS::END