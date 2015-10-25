var Tri2DClass = function(){
  //Don't use .exists within triangle.
  this.p0 = new Point2DClass(0,0);
  this.p1 = new Point2DClass(0,0);
  this.p2 = new Point2DClass(0,0);
};
Tri2DClass.prototype.set_v0 = function(x,y){
  this.p0.x = x;
  this.p0.y = y;
};
Tri2DClass.prototype.set_v1 = function(x,y){
  this.p1.x = x;
  this.p1.y = y;
};
Tri2DClass.prototype.set_v2 = function(x,y){
  this.p2.x = x;
  this.p2.y = y;
};