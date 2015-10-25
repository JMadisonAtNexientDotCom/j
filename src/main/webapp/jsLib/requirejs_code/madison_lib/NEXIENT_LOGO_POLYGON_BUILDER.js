
//creates a Polygon2DClass configured to contain italic nexient logo
//that fits within parallelogram.
//
//              B-------------------------D +----------------------------------+  
//             / \                       /  | Hopefully, we are starting with  |
//            /   \                     /   | a parallelogram with this        |    
//           /     \                   /    | structure or something similar.  |
//          /       \                 /     | Pretend triangles are equilateral)
//         /         \               /      |                                  |
//        /           \             /       | ---------------------------------+
//       /             \           /        |                                  |
//      /               \         /         | Going to denote verts we started |
//     /                 \       /          | with using capital letters.      |
//    /                   \     /           | Yes,                             |
//   /                     \   /            | breaking constant-class          |
//  /                       \ /             | convention.                      |
// A-------------------------C              +----------------------------------+
//
//              B-------------------------D  POINTS:             
//             / \                  db/  /   ac: moved along vec:AC by thick_per
//            /  /\                  /  /    bc: moved along vec:BC by thick_per
//           /  /bc\                /  /     cb: moved along vec:CB by thick_per
//          /  /    \              /  /      db: moved along vec:DB by thick_per
//         /  /      \            /  /
//        /  /        \          /  /        bc_ac: moved along vec: bc-->ac
//       /  /          \        /  /  
//      /  /            \      /  /   
//     /  /              \    /  /    
//    /  /                \cb/  / 
//   /  /                  \/  /
//  /  /                    \ /
// A--ac---------------------C    
//
//              X==bc_ac:
//              bc_ac: moved along vec bc-->ac by thick_per.
//              used to create that tapered line in the center:
//              +-------------------------+        +-------------------------+
//             / \bc                  /  /        / \                    /  / 
//            /  /\                  /  /        /  /\                  /  /  
//           /  X  \                /  /        /  X  \                /  /   
//          /  /    \              /  /        /  / \  \              /  /    
//         /  /      \            /  /        /  /   \  \            /  /
//        /  /        \          /  /        /  /     \  \          /  / 
//       /  /          \        /  /        /  /       \  \        /  /  
//      /  /            \      /  /        /  /         \  \      /  /   
//     /  /              \    /  /        /  /           \  \    /  /    
//    /  /                \  /  /        /  /             \  \  /  / 
//   /  /                  \/  /        /  /                \ \/  /
//  /  /                    \ /        /  /                   \\ /
// +--ac---------------------+        +-------------------------+    
//                  
//              B-------------------------D //We need a diagonal in order 
//             / \                     / /  //to get those sharp
//            /   \       outer-->  /   /   //tips that are facing towards 
//           /     \             /     /    //the center.
//          /       \         /       /
//         /         \     /         /      //"outer-->" and "<--outer"
//        /           \ /           /       //are pointer to the outer-facing
//       /           / \           /        //edges of the tips.
//      /         /     \         /         //denoting this because it is a bit
//     /       /         \       /          //hard to clearly visualize with
//    /     /             \     /           //ascii diagram.
//   /   / <--outer        \   /
//  / /                     \ /
// A-------------------------C   
//
//   Note:                              |    POINTS:
//   Though the "-----" lines           |      X: center of polygon.
//   appear to be vertical, they        |         where diagonals intersect.
//   are actually angled lines.         |     
//   However, drawing them as sharp     |     XA: moved along vec X-->A
//   (acute) angles was not working.    |         by an amount.
//   Take this as a SKEMATIC drawing,   |
//   NOT a proportional drawing.        |     XD: moved along vec X-->D
//                                      |         by an amount.
//                       
//
//              B--/-------------------/--D          B--/-------------------+--D
//             / \/                   // /          / \/                    / / 
//            /  /\                 //  /          /  /\                 /   /  
//           /  /  \             /  /  /          /  /  \       XD    /     /   
//          /  /    \         /    /  /          /  /    \      v  /       /
//         /  /      \     /      /  /          /  /      \     /------/  /     
//        /  /        \ /        /  /          /  /        \ /        /  /      
//       /  /        / \        /  /          /  /        / \        /  /       
//      /  /      /     \      /  /          /  /------/     \      /  /        
//     /  /    /         \    /  /          /       /  ^XA    \    /  /         
//    /  /  /             \  /  /          /     /             \  /  /          
//   /  //                 \/  /          /   /                 \/  /
//  / //                   /\ /          / /                    /\ /
// A--/-------------------/--C          A--+-------------------/--C   




var NEXIENT_LOGO_POLYGON_BUILDER = function(){

  //parallelogram to build log from.
  //Since fractal and logo should be based on the same
  //underlying parallelogram, decided that builder must be supplied
  //with a parallelogram to build from.
  this.template_parallelogram = {};
	this.thick_per = 0.2; //how thick are lines, value in 0-1 range, but
	                      //NOT including 0 or 1.
	
  //Makes point-normal form objects:	
	var pns_make = new POINT_NORM_SEG_MAKER();
  
  //Use the supplied polygon to build.
  this.build = function(){
    
		//create output variable:
    var poly = new Polygon2DClass();
    
		//Create shorter alias variable for parallelogram used as scaffolding
		//to build the logo:
		var para  = this.template_parallelogram;
		
		//create alias for thickness percentage used by builder:
		var thick = this.thick_per;
		
		//-------------------------------------------------------------------------+
		//Verticies are ordered COUNTER CLOCKWISE:              0      2         1 |
		//                            //      B---------D       B      B---------D |
		//Tri0 is pointing UP: "/\"   //     / \       /       / \      \       /  |
		var A = para.tri0.p1;         //    /   \     /       /   \      \tri-1/   |
		var B = para.tri0.p0;         //   /     \   /       /tri-0\      \   /    |
		                              //  /       \ /       /       \      \ /     |
		//Tri1 is pointing DOWN: "\/" // A---------C       A---------C      C      |
		var C = para.tri1.p0;         //                   1         2      0      |
		var D = para.tri1.p1;         //                                           |
		//-------------------------------------------------------------------------+

		// Question: Can we get this shape easier by making inset triangles?
    //		
		//                    B---------------------db----D                       
		//                   / \                    /    /                        
		//                  /   \                  /----/<--dc                    
		//                 /\   /\                /    /                          
		//                /  \ /  \              /    /   
		//               /    ?    \            /    /    
		//              /    / \    \          /    /     
		//             /    /   \    \        /    /      
		//            /    /     \    \      /    /       
		//           /    /       \    \    /    /        
		//          /    /         \    \  /    /         
		//     ab->/----/           \    \/    /          
		//        /    /             \  cb\   /   
    //       /    /               \    \ /     
		//      A---ac----------------ca----C     
		
		//DIAGRAM #11:-------------------------------------------------------------+
		// Question: Can we get this shape easier by making inset triangles?       |
		//              B                           D                              |
		//              8----------------------6----5                              |
		//             / \                    /    /                               |
		//            /   \__________________/    /    To get point 7: that is cb. |                
    //           /\   /\    \           /    /                                 |
    //          /  \ /  \    \         /    /      To get point 2:             |
		//         /    2    \    \       /    /       Intersect ray:              |
		//        /    / \    \    \     /    /        [PT:ac, NORM:ab]            |
		//       /    /   \    \    \   /    /         With:                       |
		//      /    /     \    \    \ /    /          [PT:ca, NORM:cb]            |
		//     /    /       \    \    /    /                                       |
		//    /    /         \    \  /    /                                        |
		//   /    /___________\    7/    /                                         |
		//  /    /             \    \   /                                          |
		// /    /               \    \ /                                           |
		//0----1-----------------3----4                                            |
		//A                            C                                           |
		//-------------------------------------------------------------------------+
		
		//Lets create all of the points listed on diagram #11: --------------------+
		//EXCEPT FOR POINT 2, point2 is a bit more complicated to find.            |
		//-------------------------------------------------------------------------+
		var p0 = A; //alias for point "A" to simplify problem.                     |
		var p8 = B; //alias for point "B" to simplify problem.                     |
		var p4 = C; //alias for point "C" to simplify problem.                     |
		var p5 = D; //alias for point "D" to simplify problem.                     |
		//                                                                         |
		//make point-normal segments: PointNormalSegment2DClass                    |
		//using start and end points:                                              |
		var pns_ac_01 = pns_make.byPoints(A,C); //vec a-->c                        |
		var pns_ca_03 = pns_make.byPoints(C,A); //vec c-->a                        |
		var pns_db_06 = pns_make.byPoints(D,B); //vec d-->b                        |
		var pns_cb_07 = pns_make.byPoints(C,B); //vec c-->b                        |
		//                                                                         |
		//Get segment position, takes scalar between 0-1 to project                |
		//to a location somewhere on the line segment.                             |
		var p1 = pns_ac_01.getSegPos(thick);//                                     |
		var p3 = pns_ca_03.getSegPos(thick);//                                     |
		var p6 = pns_db_06.getSegPos(thick);//                                     |
		var p7 = pns_cb_07.getSegPos(thick); //                                    |
		//-------------------------------------------------------------------------+
		
		//Diagram #12: Finding point "2": -----------------------------------------+
		//To find point 2, we create vectors BA & BC, 
		//Then starting at point B and using vector addition, we can arrive
		//at point 2: -------------------------------------------------------------+
		/*               B               */ //Make point-normal form objects:
		/*               8               */ var pns_ba = pns_make.byPoints(B,A);
		/*              / \              */ var pns_bc = pns_make.byPoints(B,C);
		/*             /   \             */
		/*            /\   /\            // Convert percent thick_per down vec ba
		/*           /  \ /  \           // into a distance value:
		/*          /    2    \          */ var ba   = pns_ba.getSegPos(thick);
		/*         /    / \    \         */ var dx   = ba.x - p8.x;
		/*        /    /   \    \        */ var dy   = ba.y - p8.y;
		/*       /    /     \    \       */ var dist  = Math.sqrt((dx*dx)+(dy*dy));
		/*      /    /       \    \      */ 
		/*     /    /         \    \     //  Move from p8 to p2:
		/*    /    /___________\    \    */  var p2 = new Point2DClass();
		/*   /    /             \    \   */  p2.x = p8.x + (pns_ba.n.x * dist)  
		/*  /    /               \    \  */              + (pns_bc.n.x * dist);
		/* 0---------------------------4 */  p2.y = p8.y + (pns_ba.n.y * dist)  
		/* A                           C */              + (pns_bc.n.y * dist);
		//-------------------------------------------------------------------------+
		
		//The "N" shape we have is good enough for now. When we are done debugging
		//and have a visual on the "N" within our HTML page, the we can work
		//on the other major details.
		poly.verts = [p0,p1,p2,p3,p4,p5,p6,p7,p8];
    
    return poly;
  };//FUNC::END

};//CLASS::END

























