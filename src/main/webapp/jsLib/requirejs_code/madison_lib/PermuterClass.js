
//DEPENDENCIES: StackClass.js

var PermuterClass = function(){

  

  //PRIVATE:
	var _stack = new StackClass(5); //permutation stack.
	var _has_does_path_exist_func     = false;
	var _does_path_exist_func         = {}; //function to see if path exists.
	var _does_path_exist_func_context = {};
	
	var _auto_wrap_permutations = true;
	
	//Think of it as which routes it tries to go down from current node
	//before it gives up and backs down.
	this.fork_tine_max_index = 0; //Upping this is a HUGE PERFORMANCE HIT.
	                              //but may need to use to fix errors depending
																//on what structures you are traversing.
	
	//Seeding it with negative 1 so that first used value will be [0].
	//Zero being ROOT.0, the root node is never drawn.
	this.reset_permutations = function(){
		_stack.prime(); //primes for entry into loop.
	};
	
	//Sets function that determines if path exists.
	//Function takes the permutation stack as an input:
	//
	//NOTE: To optimize with the fractal code you are making, the
	//setPathCheckFunc could be code that returns true/false, but the
	//code internally is also RENDERING if the path is valid.
	//other == the .this of the object that owns the func. (the context)
	//func   == the actual function callback.
	this.setPathCheckFunc = function(func, other){
		
		//Thought: We might want to make context OPTIONAL for static methods
		//that don't need a context... 
		if (typeof other === "undefined"){
			console.log("no context provided");
			return;
		}
		
		if (typeof func === "undefined"){ 
				console.log("[PermuterClass.js :: func supplied is undefined]");
				return; //<--bail out. Invalid input.
	  }//ERROR CHECK.
		
	  _does_path_exist_func         = func;
		_does_path_exist_func_context = other;
	  _has_does_path_exist_func     = true;
	};
	
	//returns TRUE if was able to move to another permutation:
	this.goto_next_permutation = function(){
		
		//No function to help permuter know how to traverse tree:
		if(!_has_does_path_exist_func){
			console.log("cannot proceed, no func set");
			return false;
		}
		
		//All possible permutations have been done and the stack
		//has been popped to empty:
		if(_stack.isEmpty()){
			console.log("permutations have come to an end.");
			return false;
		}
		
		//We need to make an actual StackClass.js object to handle this.
		
		while(true){
			_stack.grow(); //<--pop new level, or increment value at current level.
			var stack_arr = _stack.getStackArray();
			var results = _does_path_exist_func
			                             ( stack_arr , _does_path_exist_func_context);
			
			if(results){ break;} //<--exit if stack_arr's path was valid.
			
			//Path terminated. Back down one. Then re-enter loop
			//and try again to find a valid [permutation/endpoint].
			_stack.prune();
			
			//THIS VERSION IS TOO MUCH OF A PERFORMANCE HIT:
			////UPDATE: Guard added. In case first path is invalid, but others are:
		  //var value_at_level = _stack.getIndexAtLastLevelOfStack();
			//if(value_at_level >= this.fork_tine_max_index){
			//	_stack.prune();
			//}else{
			//	//Keep on incrementing like nothing happened.
			//	//Fake prune, so that keeps counting up and doesn't push on new level:
			//	_stack.forceNextGrowToIncrimentValueAtCurrentLevel();
			//	continue;
			//}//
			
			//Have we popped the stack into oblivion?:
			if(_stack.isEmpty()){
				if(_auto_wrap_permutations)
				{ //wrap around and get first valid
					//permutation by re-entering loop
					//with a reset stack:
					this.reset_permutations(); 
				}else{
					return false;
				}//
			}//	
		}//INF LOOP.
		
		//If we did not exit loop by returning false, but rather used "break",
		//we will end up here and return true.
		return true;
		
	};//goto_next_permutation
	
	//CONSTRUCTOR: Must be on bottom, because references code above.
	this.reset_permutations();
	
}//CLASS::END