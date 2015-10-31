
define(function (require) {
	
			//Request animation frame polyfill:
			//PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP//
			http://creativejs.com/resources/requestanimationframe/
			// http://paulirish.com/2011/requestanimationframe-for-smart-animating/
			 
			// requestAnimationFrame polyfill by Erik Möller
			// fixes from Paul Irish and Tino Zijdel
			 
			(function() {
					var lastTime = 0;
					var vendors = ['ms', 'moz', 'webkit', 'o'];
					for(var x = 0; x < vendors.length && !window.requestAnimationFrame; ++x) {
							window.requestAnimationFrame = window[vendors[x]+'RequestAnimationFrame'];
							window.cancelAnimationFrame = window[vendors[x]+'CancelAnimationFrame']
																				 || window[vendors[x]+'CancelRequestAnimationFrame'];
					}
			 
					if (!window.requestAnimationFrame)
							window.requestAnimationFrame = function(callback, element) {
									var currTime = new Date().getTime();
									var timeToCall = Math.max(0, 16 - (currTime - lastTime));
									var id = window.setTimeout(function() { callback(currTime + timeToCall); },
										timeToCall);
									lastTime = currTime + timeToCall;
									return id;
							};
			 
					if (!window.cancelAnimationFrame)
							window.cancelAnimationFrame = function(id) {
									clearTimeout(id);
							};
			}());
			//PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP//
	
	
    
		//Modified version of NexientLoader that creates a strip that can be used
		//for header and footer decoration:
		
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
		
		  var NexFracStripLoader = function(){
			//http://www.ericfeminella.com/blog/2012/05/17/
			//                                     organizing-require-js-dependencies/
			//PUBLIC CONFIG::
			
			//What container to use? Must specify before initialize:
			this.canvas_id_name = "myCanvas";
			
			//Opacity to lay down each triangle with:
			this.draw_tri_alpha = 0.5;
			
			//HOW DEEP? :
			this.num_levels_deep = 3; //how many levels deep to construct?
			//value of this.num_levels_deep during last init call:
			var inited_num_levels_deep = null; 
			
			//PRIVATE MISC vars:
			
			//Used to prevent permutation animation for accelerating by accidentially
			//stacking together multiple chains of callbacks to the permute drawing
			//function that controls the animation.
			//var _is_callback_chain_is_active = false;
			
			//dont allow animations to run while initializing.
			var _is_initializing = false;
			var _on_enter_frame_loop_running = false;
			
		  //PRIVATE: Animation config options:
			var _frames_to_play_for = 0;
			var _frames_played = 0;
			var _max_hor_slots_fwd = 0;
			var _total_hor_slots   = 0;
			var _current_slot_index_offset = 0;
			
			//Used to offset-the-offset. Used because sometimes all the values divide
			//into each other completely evenly and the entire canvas does NOT fillup.
			var _phase_offset = 0;
			var _frame_phase  = 0;
			
			//Use these variables to make the permutations SLIDE slower.
			//The larger your group, the slower they will slide.
			_num_iterations_to_play_in_same_spot = 2;
			var _current_frames_group_number = 0;
			
			//how much to offset by so pattern snaps nicely together.
			var _clean_hor_jump = 0;
					
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
				_is_initializing = true;
				canvas_container = document.getElementById(this.canvas_id_name);
				
				//create a parallelogram with the two triangles:--------+
				//BUILDER SETUP:                                        |
				para_builder = new ITALIC_PARALLELOGRAM_BUILDER();//    |
				
				//calculate width based on height so that parallelogram
				//will fit two perfect equilateral triangles within it:
				var pro_hig = canvas_container.height;
				var rad30 = 30 * (Math.PI / 180);
				var rad60 = 60 * (Math.PI / 180);
				var pro_d = Math.sin(rad30) * pro_hig / Math.sin(rad60);
				var pro_wid = pro_d * 3;
				_clean_hor_jump = pro_d * 2; //amount you can jump and fit in perfectly.
				
				//how many pro_d(s) are within the space of the canvas:
				//_total_hor_slots is +1 because we have to account for reversing one.
				_max_hor_slots_fwd = Math.ceil(canvas_container.width / _clean_hor_jump);
				_total_hor_slots = _max_hor_slots_fwd + 1;//
				
				para_builder.rect_wid       = pro_wid;
				para_builder.rect_hig       = pro_hig;
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
				builder.num_levels_deep = this.num_levels_deep;//    |     |
				builder.use_sub0 =  true;    //                      |     |
				builder.use_sub1 =  true;    //                      |     |
				builder.use_sub2 =  true;    //                      |     |
				builder.use_sub3 =  true;    //                      |     |
				quad0 = builder.build();     //<---Assign to quad0   |     |
																		 //                      |     |
				//Build RIGHT tri-quad:      //                      |     |
				builder.tri_temp = tri1;     //<---Use tri1          |     |
				builder.num_levels_deep = this.num_levels_deep;//    |     |
				builder.use_sub0 =  true;    //                      |     |
				builder.use_sub1 =  true;    //                      |     |
				builder.use_sub2 =  true;    //                      |     |
				builder.use_sub3 =  true;    //                      |     |
				quad1 = builder.build();     //<---Assign to quad1   |     |
				//---------------------------------------------------+     |
				//Store settings used during init:                         |
				inited_num_levels_deep = this.num_levels_deep; //          |
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
				//sub_ren.iteration_colors = ['#000000','#0088ff','#0088ff']; //too scattered.
				//sub_ren.iteration_colors = ['#0088ff'];
				sub_ren.iteration_colors = ['#002244'];
				sub_ren.draw_tri_alpha = this.draw_tri_alpha;
		
				//create a filter object to fade-back the triangles
				//over time. Makes it look classy.
				filter = new CanvasKnockBackerFilterClass();
				filter.alpha_multiplier = 0.995;
				
				//clear and draw one permutation:
				permu_ren0 = new SUB_TRI_REN_PERMUTE(sub_ren);
				permu_ren1 = new SUB_TRI_REN_PERMUTE(sub_ren);
				permu_ren0.reset_permutation_tracker();
				permu_ren1.reset_permutation_tracker();
				
				_is_paused = false;
				
				_has_been_initialized = true;
				_is_initializing      = false;
			};//FUNC::END
			//this.init();
			
			var doPermuteDraw = function(){
				
				//EXIT IF drawing if paused:
				if(_is_paused){
					return;
				}
				
				//EXIT IF currently re-initializing:
				if(_is_initializing){
					return;
				}
				
				//Figure out how much you should horizontally offset the parallelogram
				//that draws to the canvas: OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO//
				
				//Calculate which group of frames you are drawning:
				_current_frames_group_number = 
				Math.floor(_frames_played / _num_iterations_to_play_in_same_spot);
				
				//Calculate phase offset. Offset +1 per each complete horizontal pass:
				_phase_offset = Math.floor(_current_frames_group_number / _total_hor_slots);
				
				
				
				_frame_phase = (_current_frames_group_number + _phase_offset);
				_current_slot_index_offset = (_frame_phase % _total_hor_slots) - 1;
				sub_ren.offset_x = _current_slot_index_offset * (_clean_hor_jump-0.5);
				//OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO//
				
				//Make it so animation can play for a certain number
				//of frames before stopping:
				_frames_played++;
				if(_frames_to_play_for > 0){
					if(_frames_played > _frames_to_play_for){
						 _is_paused = true;
						 return;
					}
				}
				
				filter.doFilter(canvas_container);
				permu_ren0.draw_next_permutation(quad0);
				permu_ren1.draw_next_permutation(quad1);

				
			};//FUNC::END
			
			//Make an on enter frame loop that will manage animation speed
			//Only onEnterFrameLoop and onEnterFrame (itself) should be allowed
			//to call onEnterFrame
			var onEnterFrame = function(){
				doPermuteDraw();
				//better than setTimeout:
				requestAnimationFrame(onEnterFrame);
			};//
			
			//Entry point for on enter frame:
			var enterOnEnterFrameLoop = function(){
				if(_on_enter_frame_loop_running){
				  return;
				}
				
				_on_enter_frame_loop_running = true;
				onEnterFrame();
			};
			
			//see if app is paused:
			this.getIsPaused = function(){
				return _is_paused;
			};//FUNC::END
			
			this.pause = function(){
				_is_paused = true;
			};//FUNC::END
			
			//starts/resumes:
			this.resume = function(){
				
				//If class not already initialized, 
				//will initialize now:
				if(true !== _has_been_initialized){
					this.init();
				}else{
					checkForReInitialization(this);
				}//
				
				_is_paused = false;
				resetFrameCounter();
				
				//Make sure on enter frame loop is running:
				enterOnEnterFrameLoop();
				
			};//FUNC::END
			
			//If builder settings have been changed, re-initialize the app:
			//Used when re-starting:
			var checkForReInitialization = function(_this){
				if(_this.num_levels_deep !== inited_num_levels_deep){
					_this.init();
				}
			}
			
			var resetFrameCounter = function(){
				frames_played = 0;
			};//
			
			//Makes it so animation automatically pauses after
			//playing max number of frames.
			this.setMaxFramesPerPlay = function(arg_max){
				_frames_to_play_for = arg_max;
			};//
			
			
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
		
		return new NexFracStripLoader();
		
});//DEFINE END: