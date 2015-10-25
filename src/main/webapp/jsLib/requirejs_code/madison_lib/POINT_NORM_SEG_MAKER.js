


//Has methods for different ways to create
//PointNormalSegment2DClass objects.
var POINT_NORM_SEG_MAKER = function(){

  //makes using two points:
  this.byPoints = function(p0, p1){
		var op = new PointNormalSegment2DClass();
		var vec_x = p1.x - p0.x;
		var vec_y = p1.y - p0.y;
		
		var len = Math.sqrt(  (vec_x * vec_x) + (vec_y * vec_y)  );
		
		//First point is used for the point of the point normal:
		op.p.x = p0.x;
		op.p.y = p0.y;
		
		//Normal is vector p0-->p1 divided by it's length:
		op.n.x = (vec_x / len);
		op.n.y = (vec_y / len);
		
		//original length is, well, the len you made:
		op.original_length = len;
		
		//RETURN WHAT YOU MADE:
		return op;
		
	};//FUNC::END
	
	//Make using a point and a normal:
	this.byComponents = function(point, normal, original_length){
		var op = new PointNormalSegment2DClass();
		
		//copy point:
		op.p.x = point.x;
		op.p.y = point.y;
		
		//copy normal:
		op.n.x = normal.x;
		op.n.y = normal.y;
		
		//copy length:
		op.original_length = original_length;
		
		//RETURN WHAT YOU MADE:
		return op;
		
	};//FUNC::END

};//CLASS::END







