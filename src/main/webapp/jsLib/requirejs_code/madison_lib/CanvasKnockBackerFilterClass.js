
//Class used to fade an entire canvas.
var CanvasKnockBackerFilterClass = function(){

  //how much to knock back all of the pixels:
	//closer to zero == more extreme the fade.
  this.alpha_multiplier = 0.8;

	//Takes a canvas, and applies filter to it:
  this.doFilter = function(canvas_container){
	
	  //width and height of canvas:
	  var wid = canvas_container.width;
		var hig = canvas_container.height;
		var canvas_context = canvas_container.getContext("2d");
		
		//get pixel data using selection rectangle set to entire canvas:
		var ipic = canvas_context.getImageData(0, 0, wid, hig);
		
		//get the core data:
		var idata = ipic.data;
		
		//iterate over all of the data:
		//I believe data is packed like this:
		//[0][1][2][3] |[4][5][6][7] |[8][9][A][B] |[C][D][E][F] |
		// A  R  G  B  | A  R  G  B  | A  R  G  B  | A  R  G  B  |
		for (var i = 0; i < idata.length; i += 4) 
		{
		  //knock back the value of the alpha pixel:
			idata[i+3] = (idata[i+3] * this.alpha_multiplier);
		}
		canvas_context.putImageData(ipic, 0, 0);

	};//FUNC::END
	
};//CLASS::END