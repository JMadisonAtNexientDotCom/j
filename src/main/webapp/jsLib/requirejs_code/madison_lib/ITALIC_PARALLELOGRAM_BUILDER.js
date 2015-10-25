 // AUTHOR: Jmadison:2015.10.23 (Oct23rd,Year2015)
 //
 //DEPENDS ON:
 //Point2DClass
 //Tri2DClass           <--Depends on Point2DClass
 //Parallelogram2DClass <--Depends on Tri2DClass
 //
 //
 // |<-i->|   i==this.italic_percent.|        EDGE CASE: 100% is divide by zero:
 // ----->X                          |        ------------------>X             |
 // +-----+-----------------+        |        +---------------------+          |
 // |                       |        |        |                     |          |
 // |                       |        |        |                     |          |
 // |Start With A Rectangle |        |        |     Start With A    |          |
 // |                       |        |        |       Rectangle     |          |
 // |                       |        |        |                     |          |
 // +-----------------------+        |        +---------------------+          |
 //                                  |                                         |
 // ----->X                          |        ------------------>X             |
 // +-----+-----------------+        |        +------------------X--+          |
 // |                       |        |        |                     |          |
 // | Move in an equal      |        |        |  As you approach    |          |
 // | percentage on both    |        |        |  100% italic, your  |          |
 // | top and bottom.       |        |        |  parallelogram area |          |
 // |                       |        |        |  approaches ZERO    |          |
 // +-----------------+-----+        |        +--X------------------+          |
 //                   X<-----        |           X<------------------          |
 //                                  |        At 100%, parallel lines overlap: |
 // +-----X-----------------+        |        +------------------X--+          |
 // .	 	 /              	 /.        |        |                 /  /|          |
 // .	  /  slant right    / .        |        |             /  /    |          |
 // .  / and left edges  /  .        |        |         /  /        |          |
 // . /  by connecting  /   .        |        |    /  /             |          |
 // ./  X's to corners /    .        |        |/  /                 |          |
 // +-----------------X-----+        |        +--X------------------+          |
 
 //Parallelograms are composed of exactly TWO triangles:
 //       X-----------+                X-----------+   |
 //  	 	 /        	 /            	  / \         /    |
 //  	  /           /             	 /   \       /     |     *************
 //    /           /  ---VALID--->  /     \     /      |<<<<<*    YES    *
 //   /           /                /       \   /       |     *************
 //  /           /                /         \ /        |     
 // +-----------X                +-----------X         |    
 //                                                    |
 //NOTE: Parallelograms are sub-divided as shown       |
 //      above in order to create a template for       |
 //      building an ITALIC nexient logo.              |
 //                                                    |
 //       X-----------+                X-----------+   |
 //  	 	 /        	 /            	  /          //    |
 //  	  /           /             	 /        /  /     |      *************
 //    /           /  ---BOGUS-->   /      /    /      | <<<<<*    NO!    *
 //   /           /                /   /       /       |      *************
 //  /           /                //          /        |
 // +-----------X                +-----------X         |

var ITALIC_PARALLELOGRAM_BUILDER = function(){

  //A square rectangle used as the base of building the parallelogram.
	//We want the final product to fit snugly into the original rectangle.
  this.rect_wid = 100;
	this.rect_hig = 50;
	this.italic_percent = 0.3333; //<-- 1/3 will help make equilateral triangles.
	
	//returns an instance of Parallelogram2DClass
	this.build = function(){
		
		//Create anchor points for building triangles:
		var last_pixel_x = (this.rect_wid-1);
		var last_pixel_y = (this.rect_hig-1);
		var top_x = last_pixel_x * this.italic_percent; //shift point to RIGHT.
		var bot_x = last_pixel_x - top_x;               //shift point to LEFT.
		
		//Connect the dots to tessilate parallelogram into two triangles:
		var tri0 = new Tri2DClass();
		var tri1 = new Tri2DClass();
		
  //--------------------------------------------------------------------------//
  //         FIRST:                                                           //
  //         tri0.p0 ---->  b-----------d            To maintain symmetry,    //
  //                   	   / \         /             these will be the        //
  //                   	  /   \       /              locations of the         //
  //                     /     \     /               FIRST vertex of each     //
  //                    /       \   /                triangle.                //
  //                   /         \ /        SECOND:                           //
  //                  a-----------c   <---- tri1.p0                           //
  //--------------------------------------------------------------------------//
		
	//To make code easier to read, we will construct values for each unique vert
	//before building our triangles: Refer to diagram above for vert reference.
	var a = new Point2DClass();
	var b = new Point2DClass();
	var c = new Point2DClass();
	var d = new Point2DClass();
	//-------------------------------------+
	a.x = 0;            //no inset         |
	a.y = last_pixel_y; //bottom           |
	//-------------------------------------+
	b.x = top_x;        //inset to RIGHT   |
	b.y = 0;            //top              |
	//-------------------------------------+
	c.x = bot_x;        //inset to LEFT    |
	c.y = last_pixel_y; //bottom           |
	//-------------------------------------+
	d.x = last_pixel_x; //no inset         |
	d.y = 0;            //top              |
  //-------------------------------------+	
	                             
	//tri0.p0 ---->  b-----------d       --+
	//          	  / \         /          |
	//          	 /   \ tri1  /           |
	//   LEFT     /     \     /      RIGHT |        
	//           / tri0  \   /             |
	//          /         \ /        
	//         a-----------c   <---- tri1.p0	
					 
		//ORDER VERTS COUNTER-CLOCKWISE. (open-gl standard)
		//tri0 = [c,b,a]:
		tri0.set_v0(b.x,b.y);
		tri0.set_v1(a.x,a.y);
		tri0.set_v2(c.x,c.y);
		
		//tri1 = [c,d,b]:
		tri1.set_v0(c.x,c.y);
		tri1.set_v1(d.x,d.y);
		tri1.set_v2(b.x,b.y);
		
		//pack the two triangles into output parallelogram:
		var para = new Parallelogram2DClass();
		para.setTris(tri0,tri1);
		
		//RETURN THE PARALLELOGRAM:
		return para;
		
	};//FUNC::END
}//CLASS::END


 
 
 
 
 
 
 
 
 
 
 
 
                                               