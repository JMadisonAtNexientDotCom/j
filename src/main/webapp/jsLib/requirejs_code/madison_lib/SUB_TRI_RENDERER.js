//Rendering app:
//RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR--START--RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR//
var SUB_TRI_RENDERER = function(){
		
		//PUBLIC:
		this.canvas_name = "myCanvas";
		var _canvas_name_loaded = "";
		var _canvas_container = null; //canvas container.
		var _can_ctx = null; //canvas context.
		
		this.iteration_colors = ['#ff8800','#0055ff','#0088ff'];
		//this.iteration_colors = ['#ff8800','#0055ff','#0088ff','#ff0000','#00ff00'];
		this.iteration_index = 0;
		
		this.draw_tri_alpha = 1;
		
		//Add offset so we can use
		//renderer like a clone-stamper:
		this.offset_x = 0;
		this.offset_y = 0;
		
		this.setCanvasReferences = function(){
		  _canvas_container = document.getElementById(this.canvas_name);
			_can_ctx = _canvas_container.getContext("2d");
			_canvas_name_loaded = this.canvas_name;
		};//
			
		
		//PUBLIC METHOD:
		this.draw_tri = function(tri){
			
			if(_canvas_name_loaded != this.canvas_name){
				this.setCanvasReferences();
			}//
			
			//delete these 3 lines below:
			//var c = document.getElementById(this.canvas_name);
			//var ctx = c.getContext("2d");
			//ctx .fillStyle = '#f0f';
			
			this.iteration_index ++;
			var wrap_dex = this.iteration_index % this.iteration_colors.length;
			
			_can_ctx.fillStyle = this.iteration_colors[wrap_dex];
			_can_ctx .beginPath();
			_can_ctx .moveTo(tri.p0.x + this.offset_x, tri.p0.y + this.offset_y);
			_can_ctx .lineTo(tri.p1.x + this.offset_x, tri.p1.y + this.offset_y);
			_can_ctx .lineTo(tri.p2.x + this.offset_x, tri.p2.y + this.offset_y);
			_can_ctx .closePath();
			
			//So you can draw transparent triangles to canvas without having
			//to do stupid string concats that are not compatible with
			//the way you did your iteration colors arrray.
			if(this.draw_tri_alpha < 1){
				_can_ctx.save();
				_can_ctx.globalAlpha=this.draw_tri_alpha;
				_can_ctx .fill();
				_can_ctx.restore();
			}else{
				_can_ctx.fill();
		  }//way to fill, end.
	
		};//FUNC::END

		//PUBLIC METHOD:
		this.clear_linked_canvas = function(){
			var c = document.getElementById(this.canvas_name);
			var ctx = c.getContext("2d");
			ctx.clearRect(0, 0, c.width, c.height)
		};

		//non-recursive entry point:
		//renders whatever quad is stored in the renderer
		//PUBLIC METHOD:
		this.draw_quad = function(quad){
			
			//clear canvas before re-rendering:
			this.clear_linked_canvas();
			
			//re-render:
			this.draw_quad_recursive(quad);
		};

		//recursive drawing:
		//PUBLIC METHOD:
		this.draw_quad_recursive = function(arg_quad){

			//draw the MAIN SECTION ONLY. (main ALWAYS exists)
			//All other sections drawn through recursion:
			this.draw_tri( arg_quad.main );

			//TOP:
			if(arg_quad.has0 >= 1){//TRUE
				this.draw_quad_recursive(arg_quad.sub0); //recursive entry point.
			}

			//TOP:
			if(arg_quad.has1 >= 1){//TRUE
				this.draw_quad_recursive(arg_quad.sub1); //recursive entry point.
			}

			//TOP:
			if(arg_quad.has2 >= 1){//TRUE
				this.draw_quad_recursive(arg_quad.sub2); //recursive entry point.
			}

			//TOP:
			if(arg_quad.has3 >= 1){//TRUE
				this.draw_quad_recursive(arg_quad.sub3); //recursive entry point.
			}

		};//draw_quad_recursive
}//SUB_TRI_RENDERER
//RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR--END--RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR//