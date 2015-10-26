
define(function (require) {
    
		//Load all dependencies,
		//Keep dependency loading OUTSIDE of the app container class you want
		//to return;
		
		//PRIMITIVE CLASSES (No dependencies)
		var PRIM_01 = require('Point2DClass'                   ), 
				PRIM_02 = require('StackClass'                     ), 
				PRIM_03 = require('CanvasKnockBackerFilterClass'   ),
		//COMPOUND PRIMITIVES (Have dependencies)
				COMP_01 = require('Tri2DClass'                     ),  
				COMP_02 = require('Parallelogram2DClass'           ),
				COMP_03 = require('TriQuadClass'                   ),  
				COMP_04 = require('CirclePathPointClass'           ),
				COMP_05 = require('PermuterClass'                  ),
				COMP_06 = require('PointNormalSegment2DClass'      ),
				COMP_07 = require('Polygon2DClass'                 ),
				
		//MAKER CLASSES: Holds constructors for primitives and
									 //compound primitives:
				MAKER_01 = require('POINT_NORM_SEG_MAKER'          ),
		//BUILDER CLASSES:
				BUILDER_01 = require('SUB_TRI_BUILDER'             ),
				BUILDER_02 = require('SUB_TRI_RENDERER'            ),
				BUILDER_03 = require('ITALIC_PARALLELOGRAM_BUILDER'),
				BUILDER_04 = require('NEXIENT_LOGO_POLYGON_BUILDER'),
		//OTHER CLASSES WITH DEPENDENCIES:
				OTHER_01 = require('SUB_TRI_REN_PERMUTE'           ),
				OTHER_02 = require('NEXIENT_LOADER'                ),
		//SINGLETONS:
				SINGLETON_01 = require('LOOPAPP'); //<<semicolon on last item. 
		
		var NexFracLoader = function(){
			//http://www.ericfeminella.com/blog/2012/05/17/organizing-require-js-dependencies/
			//PRIMITIVE CLASSES:
			
		
					
		  //This will eventually become guts of NEXIENT_LOADER class,
			//so mock up our constructor:
			var canvas_container = null;
			var para_builder     = null;
			
			var para  = null; //parallelogram builder.
			
			var tri0 = null; //tri0 extracted from built parallelogram.
			var tri1 = null; //tri1 extracted from built parallelogram.
			
			var quad0 = null; //tri0 converted into a TriQuad fractal.
			var quad1 = null; //tri1 converted into a TriQuad fractal.
			
			var builder = null; //sub-tri builder.
			
			//Polygon logo that is not currently being used:
			var nex_maker = null;
			var poly      = null;
			
			var sub_ren   = null; //renders triangles in fractal structure.
			var filter    = null;
			
			var permu_ren0 = null; //permutation renderers, one for each
			var permu_ren1 = null; //triangle of the parallelogram.
			
			var _is_paused = true; //is the animation paused?
			
			var _has_been_initialized = false;
					
			//attempt to slowly throw stuff into here:
		  this.init = function(){
				canvas_container = document.getElementById('myCanvas');
				
				//create a parallelogram with the two triangles:--------+
				//BUILDER SETUP:                                        |
				para_builder = new ITALIC_PARALLELOGRAM_BUILDER();//    |
				para_builder.rect_wid       = canvas_container.width;// |
				para_builder.rect_hig       = canvas_container.height;//|
				para_builder.italic_percent = (1/3);//                  |
				//BUILD:                                                |
				para = para_builder.build();//                          |
				//------------------------------------------------------+
			
			  //extract the two triangles from 
				//the parallelogram you just built.
				tri0 = para.tri0;
				tri1 = para.tri1;
				
				//construct fractal:---------------------------------------+
				//INSTANTIATE BUILDER:                                     |
			  builder = new SUB_TRI_BUILDER();//                         |
				//                                                         |
				//CONFIGURE BUILDER:                                       |
				//NOTE: Permuter class currently expects there to be no    |
				//      missing branches in the quad tree. If there are,   |
				//      the program will crash on you, or fail to work.    |
				//      The fix for this is too much of a performance hit  |
				//      to implement.                                      |
				//Build LEFT tri-quad: ------------------------------+     |
				builder.tri_temp = tri0;     //<---Use tri0          |     |
				builder.num_levels_deep = 3; //                      |     |
				builder.use_sub0 =  true;    //                      |     |
				builder.use_sub1 =  true;    //                      |     |
				builder.use_sub2 =  true;    //                      |     |
				builder.use_sub3 =  true;    //                      |     |
				quad0 = builder.build();     //<---Assign to quad0   |     |
																		 //                      |     |
				//Build RIGHT tri-quad:      //                      |     |
				builder.tri_temp = tri1;     //<---Use tri1          |     |
				builder.num_levels_deep = 3; //                      |     |
				builder.use_sub0 =  true;    //                      |     |
				builder.use_sub1 =  true;    //                      |     |
				builder.use_sub2 =  true;    //                      |     |
				builder.use_sub3 =  true;    //                      |     |
				quad1 = builder.build();     //<---Assign to quad1   |     |
				//---------------------------------------------------+     |
				//                                                         |
				//---------------------------------------------------------+
				
				//create nexient polygon using parallelogram:
				nex_maker = new NEXIENT_LOGO_POLYGON_BUILDER();
				nex_maker.template_parallelogram = para;
				poly = nex_maker.build();		
				
				//Object responsible for rendering fractals that were built using 
				//the SUB_TRI_BUILDER. The two permutations renderers will share
				//the same renderer.
				sub_ren = new SUB_TRI_RENDERER();
		
				//create a filter object to fade-back the triangles
				//over time. Makes it look classy.
				filter = new CanvasKnockBackerFilterClass();
				filter.alpha_multiplier = 0.8;
				
				//clear and draw one permutation:
				permu_ren0 = new SUB_TRI_REN_PERMUTE(sub_ren);
				permu_ren1 = new SUB_TRI_REN_PERMUTE(sub_ren);
				permu_ren0.reset_permutation_tracker();
				permu_ren1.reset_permutation_tracker();
				
				_is_paused = false;
				
				_has_been_initialized = true;
				
			};//FUNC::END
			//this.init();
			
			var doPermuteDraw = function(){
				
				if(_is_paused){return;}
				
				//permu_ren.clear_linked_canvas();
				
				//draw polygon:
				//canvas_container.getContext('2d').globalCompositeOperation = 'destination-over'; //DRAW NEW PIXELS BELOW.
				//canvas_container.getContext('2d').globalCompositeOperation = "overlay";
				//poly.draw(canvas_container);
				//canvas_container.getContext('2d').globalCompositeOperation = 'source-over'; //DRAW NEW PIXELS ABOVE.
				
				filter.doFilter(canvas_container);
				permu_ren0.draw_next_permutation(quad0);
				permu_ren1.draw_next_permutation(quad1);

				
				//Give some time, then do again.
				setTimeout(doPermuteDraw,60); //240
			};//FUNC::END
			
			this.pause = function(){
				_is_paused = true;
			};//FUNC::END
			
			//starts/resumes:
			this.resume = function(){
				_is_paused = false;
				
				//If class not already initialized, 
				//will initialize now:
				if(true !== _has_been_initialized){
					this.init();
				}//
				
				doPermuteDraw();
			};//FUNC::END
			
			
			this.toggle = function(){
				if(_is_paused){
					this.resume();
				}else{
					this.pause();
				}//
			};
			
			//this.resume();
			
			
			return this; //return the [application container class / self]
		};//CLASS::END
		
		return new NexFracLoader();
		
});//DEFINE END: