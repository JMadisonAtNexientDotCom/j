
//DEPENDENCIES: Tri2DClass.js, TriQuadClass.js

//BBBBBBBBBBBBBBBBBBBBBBBBBBBB--START--BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB//
var SUB_TRI_BUILDER = function() { 
  this.num_levels_deep = 2;           //How many levels deep 
                                      //should sub-division go?
  //use_sub# vars:
  //Which triangle sub-sections should be
  //considered when recursively sub-dividing?
  this.use_sub0 = true;
  this.use_sub1 = true;
  this.use_sub2 = true;
  this.use_sub3 = true;
  this.tri_temp = new Tri2DClass(); //Main triangle to start sub-dividing from.
	                                  //tri_temp == "triangle template"
	//Function to call when building is done:
  this._on_built_callback = {};
	this._has_on_built_callback = false;
	this._is_busy_building = false;
	
	this.set_on_built_callback = function(callback){
		this._on_built_callback = callback;
		this._has_on_built_callback = true;
	};
	
	this.get_is_busy_building = function(){
		return this._is_busy_building;
	};

  //Takes the root_tri and creates a TriQuadClass tree based on the
  //instructions in the builder. Returns the root node of the TriQuadClass.
  this.build = function(){
		if(this._is_busy_building){
			console.log("PROBLEM. Trying to build again when first did not finish");
		}
		
		_is_busy_building = true;
    console.log("hey");

    //Create empty output object to
    //init, subdivide, and return:
    var root= new TriQuadClass();
		 
		//Debug: Make sure the entire tri_temp is not all zeros:
		if( this.tri_temp.p0.x === 0 &&
		    this.tri_temp.p0.y === 0 &&
				
				this.tri_temp.p1.x === 0 &&
				this.tri_temp.p1.y === 0 &&
				
				this.tri_temp.p2.x === 0 &&
				this.tri_temp.p2.y === 0  )
		{
			console.log("[BAD BUILDER INPUT :START]");
			console.log(this.tri_temp);
			console.log("[BAD BUILDER INPUT :END]");
		}

     //Use the stored root_tri as template for the triangular
     //quad-tree you are construction:
     root.main.set_v0(this.tri_temp.p0.x, this.tri_temp.p0.y);
     root.main.set_v1(this.tri_temp.p1.x, this.tri_temp.p1.y);
     root.main.set_v2(this.tri_temp.p2.x, this.tri_temp.p2.y);

     //Begin subdividing:
     this.recursive_divide(root, this.num_levels_deep);

		if(this._has_on_built_callback){
			console.log("has on built callback");
			setTimeout(this._on_built_callback, 6000);
			 
		}
		
		//Putting the setting of flag AFTER callback to formalize that
		//using callback to build again is an ERROR unless you are using some
		//type of async delay.
		_is_busy_building = false;
    return root;
  };
  //var mass:Tri2DClass //The triangle to split. The main. "mass".
  //var quad:int //The quadrant of the split_me triangle to take.
	//returns : A Tri2DClass object that has been split according to the
	//          rule defined by the quad enum.
  this.split_tri = function(mass, quad){
    var op = new Tri2DClass();
    if(0==quad){ //TAKE TOP:
      
      //VERT:TOP: (ORIGIN IN THIS SCOPE)
      op.p0.x = (mass.p0.x);//XXXXXXXXXXX;
      op.p0.y = (mass.p0.y);//XXXXXXXXXXX;
      
      //VERT: BOTTOM-LEFT:
      op.p1.x = (mass.p0.x + mass.p1.x)/2;
      op.p1.y = (mass.p0.y + mass.p1.y)/2;

      //VERT: BOTTOM-RIGHT:
      op.p2.x = (mass.p0.x + mass.p2.x)/2;
      op.p2.y = (mass.p0.y + mass.p2.y)/2;

    }else
    if(1==quad){ //TAKE BOTTOM-LEFT:

      //VERT: BOTTOM-LEFT: (ORIGIN IN THIS SCOPE)
      op.p1.x = (mass.p1.x);//XXXXXXXXXXX;
      op.p1.y = (mass.p1.y);//XXXXXXXXXXX;

      //VERT:TOP:
      op.p0.x = (mass.p1.x + mass.p0.x)/2;
      op.p0.y = (mass.p1.y + mass.p0.y)/2;
      
      //VERT: BOTTOM-RIGHT:
      op.p2.x = (mass.p1.x + mass.p2.x)/2;
      op.p2.y = (mass.p1.y + mass.p2.y)/2;

      
    }else
    if(2==quad){ //TAKE BOTTOM-RIGHT:
     //VERT: BOTTOM-RIGHT: (ORIGIN IN THIS SCOPE)
      op.p2.x = (mass.p2.x);//XXXXXXXXXXX;
      op.p2.y = (mass.p2.y);//XXXXXXXXXXX;

     //VERT: BOTTOM-LEFT: 
      op.p1.x = (mass.p2.x + mass.p1.x)/2;
      op.p1.y = (mass.p2.y + mass.p1.y)/2;

      //VERT:TOP:
      op.p0.x = (mass.p2.x + mass.p0.x)/2;
      op.p0.y = (mass.p2.y + mass.p0.y)/2;
     
    }else
    if(3==quad){ //TAKE CENTER:

      //Unlike the 3 other processes:
      //This one has no origin:

      //VERT:TOP:
      op.p0.x = (mass.p0.x + mass.p1.x)/2;
      op.p0.y = (mass.p0.y + mass.p1.y)/2;
      
      //VERT: BOTTOM-LEFT: 
      op.p1.x = (mass.p1.x + mass.p2.x)/2;
      op.p1.y = (mass.p1.y + mass.p2.y)/2;

      //VERT: BOTTOM-RIGHT: 
      op.p2.x = (mass.p2.x + mass.p0.x)/2; //Use p0 because no p3 exists.
      op.p2.y = (mass.p2.y + mass.p0.y)/2; //Use p0 because no p3 exists.
    }else{
      console.log("invalid quad value specified");
    }

    //return output TRIANGLE OBJECT.:
    return op;
  };
  //arg_root: Root TriQuadClass you are sub-dividing.
  //arg_deep: How deep you currently are.
  this.recursive_divide = function(arg_root,arg_deep){
    console.log(arg_root);
    console.log(arg_deep);
    arg_deep = arg_deep - 1;
    if(arg_deep <= 0){ return; }
		

    //Ugly and hard-coded sub-division code:
    //SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
    //subdivide sub0:
    if(this.use_sub0){
      if(0>=arg_root.has0){//FALSE
				arg_root.sub0 = new TriQuadClass(); 
				arg_root.has0=100;//TRUE
			}
      arg_root.sub0.main = this.split_tri(arg_root.main,0); //<--split the mass
			this.recursive_divide(arg_root.sub0, arg_deep); //recursive entry point.
    }//00

    //subdivide sub1:
    if(this.use_sub1){
      if(0>=arg_root.has1){//FALSE
				arg_root.sub1 = new TriQuadClass(); 
				arg_root.has1=101;//TRUE
			}
      arg_root.sub1.main = this.split_tri(arg_root.main,1); //<--split the mass
			this.recursive_divide(arg_root.sub1, arg_deep); //recursive entry point.
    }//01

    //subdivide sub2:
    if(this.use_sub2){
      if(0>=arg_root.has2){//FALSE
				arg_root.sub2 = new TriQuadClass(); 
				arg_root.has2=102;//TRUE
		  }
      arg_root.sub2.main = this.split_tri(arg_root.main,2); //<--split the mass
			this.recursive_divide(arg_root.sub2, arg_deep); //recursive entry point.
    }//02

    //subdivide sub3:
    if(this.use_sub3){
      if(0>=arg_root.has3){//FALSE
				arg_root.sub3 = new TriQuadClass(); 
				arg_root.has3=103;//TRUE
			}
      arg_root.sub3.main = this.split_tri(arg_root.main,3); //<--split the mass
			this.recursive_divide(arg_root.sub3, arg_deep); //recursive entry point.
    }//03
    //SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS

  };//recursive_divide

};//sub-tri builder object.
//BBBBBBBBBBBBBBBBBBBBBBBBBBBBBB--END--BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB//