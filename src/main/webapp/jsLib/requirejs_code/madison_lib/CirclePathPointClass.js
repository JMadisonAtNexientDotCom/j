
var CirclePathPointClass = function(){
	
	//vars:
	this.amt = 0.1;   //Speed in percentage moved per frame.
	                  //AKA: cycles per frame. Since per = 0-1
	this.org   = new Point2DClass(); //--origin of path.
	this.rad   = 5; //------------------radius of path in pixels.
	
	//PRIVATE:
	var cur = 0; //<--current percentage along path.
	var pos = new Point2DClass(); //<--mapped 2d position of path.
	var pos_dirty_x = true;
	var pos_dirty_y = true;
	var PI2 = Math.PI * 2;
	
	this.next_frame = function(){
		pos_dirty_x = true;
		pos_dirty_y = true;
		cur += this.amt;
		if(cur > 1){ cur = cur % 1;}
	}
	
	this.getPos = function(){
		var op = new Point2DClass();
		op.x = this.getPosX();
		op.y = this.getPosY();
		return op;
  }
	
	this.getPosX = function(){
		if(pos_dirty_x){
			 pos_dirty_x = false;
			 pos.x = this.org.x + ( Math.cos(PI2*cur) * this.rad );
		}
		return pos.x;
	}
	
  this.getPosY = function(){
		if(pos_dirty_y){
			 pos_dirty_y = false;
			 pos.y = this.org.y + ( Math.sin(PI2*cur) * this.rad );
		}
		return pos.y;
	}
	
}//CLASS::END