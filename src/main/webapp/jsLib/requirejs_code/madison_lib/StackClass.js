var StackClass = function(preAllocationAmount){
  
//GROWING: Growing will push a [0] at the end of the stack.
//         If the stack is analysed and we realize the current stack path
//         leads nowhere, we PRUNE the stack.
//
//         Growing after pruning will INCRIMENT the value at the current
//         pointer position. (Which should be the end of the stack)
//         Because it already tried to push a [0] onto the end and that was
//         found to be invalid.
//
//PRUNING: Pruning will pop a [#] off the end of the stack.
//         This could be a [0] from trying a dead end, or the current level
//         of the stack may have been maxed out. 
//         AKA: [0][1][2][MAX_USABLE_INDEX]
//         Either way, the last level needs to be popped off, and we
//         need to flag as having reached a dead end so we grow properly
//         the next iteration.
//
//When to prune and when to grow?
//Outside the scope of this class.
//Another function should be looking at this objects stack and
//PRUNING it if found to be bogus, or GROWING it if found to be valid.
//
////////////////////////////////////////////////////////////////////////////////
//                                                                            //
//        [0][0][0][X]                                                        //
//            DEAD_END    [NEVER TRIED]                                       //
//               X         |                                                  //
//               |         |                                                  //
//               +---------+                                                  //
//                    |                                                       //
//                    |                                                       //
//           [0][0][0]+   +[0][1][1]   [0][1][0]+   +[1][1][1]                //
//                    |   |                     |   |                         //
//                    |   |                     |   |                         //
//              [0][0]+---+[0][1]         [1][0]+---+[1][1]                   //
//                      |                         |                           //
//                      |                         |                           //
//                   [0]+-------------------------+[1]                        //
//                                   |                                        //
//                                  [ ]EMPTY                                  //
//                                                                            //
////////////////////////////////////////////////////////////////////////////////   

  //Design choice, refactor 2015.10.25:
  //To avoid garbage collection we will NOT be popping off the stack.
  //Rather, we will leave (-1) at the position and move the pointer backwards.
  //This is to avoid hitches in the cycle due to constant invocation of garbage
  //collector when pushing and popping off of the stack.  
  
  //Design choice: 
  //Going with python style "private by convention".
  //There is a way called "Private Prototype Functions" to make a private
  //Container that can be accessed by instance methods in your function...
  //But I decided to keep it simple for now. Don't want to be doing two
  //learning experiments at the same time.
  
  this._stack = [];   //Empty array represents _stack address of ROOT object.
  this._stack_max_index = (-1); //<--max index value in _stack. (-1) for empty.
                                //this is the ACTUAL max index of the stack.
                                //not the max-index we pretend it has by using
                                //the _ptr value.
  this._ptr   = (-1); //pointer rests on where you want to grow from.
  
  //When in debug mode, will do some performance compromising error checks:
  this._is_debug = false;
  
  //Lets us know not to grow by pushing a [0] onto the stack, because
  //that path is a dead-end, known from previous experience.
  this._just_pruned = false;
  
  //Attempts to find next iteration in stack:
  this.grow = function(){
    
    if(this._primed){
      //no growing, just was primed for smooth entry
      //into an iteration loop. Now we can use isEmpty() to validate
      //if stack is spent or not without any trouble.
      this._primed = false;
    }else
    if(this._just_pruned){
      //Try finding next branch by incrementing
      //value at current depth/level.
      //DO NOT: this._stack[this._stack.length-1]++;
      this._stack[this._ptr]++; //<--do this instead.
      this._just_pruned = false;
      this._primed      = false;
    }else{
      
      this._pushZero();
      
    }//BLOCK::END
    
    if(this._is_debug){ this._doIntegrityCheck();}
    
  };//FUNC::END
  
  //The .push() operation abstracted to battle the garbage collector:
  this._pushZero = function(){
    //Try finding next branch by
    //pushing on a new level:
    //only push if the current pointer is at the deepest existing level
    //on the internal stack:
    if(this._ptr < this._stack_max_index){
      this._ptr++;
      this._stack[this._ptr] = 0; //put zero so we awknowledge existence.
    }else
    if(this._ptr === this._stack_max_index){
      this._stack.push(0); //<--pushing is OKAY. Popping is NOT.
      this._stack_max_index++;
      this._ptr++;
    }else
    if(this._ptr > this._stack_max_index){
      throw "_ptr is not in sync with stack max index";
    }//
  };//FUNC::END
  
  //Simulates a prune without actually pruning, so we can skip over
  //non-existent branches without changing current node position:
  this.forceNextGrowToIncrimentValueAtCurrentLevel = function(){
    this._just_pruned = true;
  };
  
  //Prunes off a level that was found to be invalid:
  this.prune = function(){
    if(this._ptr <= (-1)){
      throw "StackClass.js::ERROR:NothingToPop";
    }
    
    //DO NOT: this._stack.pop();
    //This will kill performance with garbage collection, put a (-1)
    //in the slot at the current level. (-1) will denote pretending nothing
    //after it exists. Then move the pointer back one.
    this._stack[this._ptr] = (-1); //<--(-1) to pretend doesn't exist.
    this._ptr--;
    this._just_pruned = true;
  };
  
  //Clears the stack to the root.
  this._clear = function(){
    //DO NOT: this._stack = [];
    //Instead: Put a (-1) at the first level of the stack, which will make us
    //pretend the stack is entirely empty:
    if(this._stack_max_index >= 0){ //has at least first slot?
      this._stack[0] = (-1);
    }
    this._ptr   = (-1);
    this._just_pruned = false;
  };//
  
  //Priming is the act of "priming/readying" the stack for smooth
  //entry into a looping structure.
  //
  //When primed, invoking the grow() method will only
  //turn ._primed to false. We have this setup for priming
  this.prime = function(){
    this._clear();
    
    
    //DO NOT: this._stack = [0];
    //Instead of making entire new array, just set the pointers
    //to pretend there is only one item, a zero, at the specified spot:
    if(this._stack_max_index < 0){ //<--less than zero == EMPTY[] stack.
      this._stack.push(0);
      this._stack_max_index++;
    }else{
      //We know we have at least one slot,
      //set it to ZERO.
      this._stack[0] = (0);
      
      //If we have more than one level, set the level after the first
      //to a (-1) so that we pretend that level and everything after it
      //does NOT exist.
      if(this._stack_max_index > 0){ //Greater than 0 == at least 2 slots.
        this._stack[1] = (-1); //mark the 2nd level as not existing.
      }//
    }//BLOCK::END
    
    //Mark the stack object as primed:
    this._primed = true;
    
    //Put the pointer at first level so it is not considered empty:
    this._ptr = 0;
    
    if(this._is_debug){ this._doIntegrityCheck();}
    
  };//FUNC::END
  
  this.isEmpty = function(){
    
    if(this._is_debug){ this._doIntegrityCheck();}
    
    //DO NOT: return (this._stack.length <= 0);
    return (this._ptr < 0); //<--instead.
  };//
  
  this.getStackArray = function(){
    return this._stack;
  };//
  
  //Returns the index that is at last level of stack:
  this.getIndexAtLastLevelOfStack = function(){
    
    //Debug:
    if(this._is_debug){this._DEBUG_getIndexAtLastLevelOfStack();};
    
    //return this._stack[this._stack.length - 1];
    return this._stack[this._ptr];
  };
  
  //Debug function making sure everything is alright:
  this._doIntegrityCheck = function(){
    var actual_max_index = this._stack.length - 1;
    if(actual_max_index != this._stack_max_index){
      console.log("we have an integrity problem");
      throw "Integrity problem in StackClass.js"
    }//error?
  };
  
  //error checking code for function:
  this._DEBUG_getIndexAtLastLevelOfStack = function(){
    if(this._stack[this._ptr] < 0){
        this._doError("bad value in level");
    };
    this._doIntegrityCheck();
  };//FUNC::END
  
  //How many slots do we want to have in array at time of initialization?
  function preAllocateMemory(_this, preAllocationAmount){
    console.log("pre allocate start");
    for(var i = 0; i < preAllocationAmount; i++){
      _this._stack.push(-1);
      _this._stack_max_index++;
    };
    console.log("pre allocation done. stack mi==" + _this._stack_max_index);
  };//
  preAllocateMemory(this, preAllocationAmount); //<<constructor??
  

}//CLASS::END