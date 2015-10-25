
//Generic polygon class.
var Polygon2DClass = function(){

	this.verts = []; //array of Point2DClass. Ordered counter-clockwise.
	this.color  = '#0088ff'; //color to render as. In format accepted by canvas.
	this.alpha  = 0.5;   //transparency.
	this.isNormalized = false;
	
	this.draw = function(canvas_container){
		if(this.isNormalized){
		  this._drawNormalized(canvas_container);
		}else{
		  this._drawMaverkized(canvas_container);
		}//
	};//FUNC::END
	
	
	this._drawCommon = function(norm_flag,can_ctx, can_wid, can_hig){
		
		//set fill color of drawing canvas context:
		can_ctx.fillStyle = this.color;
		
		//loop through all verts:
		var type = 0;
		var pt = null;
		var max_dex = this.verts.length - 1;
		for(var v = 0; v <= max_dex; v++){
			
			//Get point to draw, and decide what type of point it is:
			pt = this.verts[v];
			type = 2;//<---------------//2== MIDDLE VERTS
			if(0===v      ){type = 1;} //1== FIRST VERTEX.
			if(v===max_dex){type = 3;} //3== LAST  VERTEX.
			
			//Are we drawing normalized points, or maverkized (as-is) points?
			if(norm_flag){
				this._drawNormalizedPoint(pt,type,can_ctx,can_wid,can_hig);
			}else{
				this._drawPointAsIs(pt,type,can_ctx);
			}//BLOCK::END
			
		}//next vert
	}//FUNC::END
	
	//draws non-normalized polygon.
	this._drawMaverkized = function(canvas_container){
		
		var can_ctx = canvas_container.getContext("2d");
		
		//FALSE == NOT_NORMALIZED:
		this._drawCommon(false,can_ctx,null,null);
		
	};//FUNC::END
	
	//Expects that all points have values in range 0-to-1
	//So that drawing can be scaled to any size canvas.
	this._drawNormalized = function(canvas_container){
	  var can_wid = canvas_container.width;
		var can_hig = canvas_container.height;
		var can_ctx = canvas_container.getContext("2d");
		
		//TRUE == IS_NORMALIZED:
		this._drawCommon(true,can_ctx,can_wid,can_hig);
		
	};//FUNC::END
	
	//type:1 == first point. use moveTo
	//type:2 == points in middle. use lineTo
	//type:3 == last point. use moveTo, then closePath.
	this._drawNormalizedPoint = function(pt,type,can_ctx, can_wid, can_hig){
		//Convert point with 0-to-1 range into boundaries of canvas.
	  var norm = new Point2DClass();
		norm.x = pt.x * (can_wid-1);
		norm.y = pt.y * (can_hig-1);
		this._drawPointAsIs(norm,type,can_ctx);
	};//FUNC::END
	
	//type:1 == first point. use moveTo
	//type:2 == points in middle. use lineTo
	//type:3 == last point. use moveTo, then closePath.
	this._drawPointAsIs = function(pt,type,can_ctx){
	  
		if(1===type){ //FIRST vertex of shape.
			can_ctx.beginPath();
			can_ctx.moveTo(pt.x, pt.y);
		}else
	  if(2==type){ //One of the middle vertices shape.
			can_ctx.lineTo(pt.x, pt.y);
		}else
		if(3===type){ //LAST vertex of shape.
			can_ctx.lineTo(pt.x, pt.y);
			can_ctx.closePath();
			can_ctx.fill();
		}else{
			console.log("unknown vertex type");
		}//BLOCK::END
		
	};//FUNC::END
	
};//CLASS::END