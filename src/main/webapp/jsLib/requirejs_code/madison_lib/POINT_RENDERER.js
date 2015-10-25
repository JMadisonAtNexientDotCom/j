//Point-Rendering app:
//PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP--START--PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP//
var POINT_RENDERER = POINT_RENDERER || { //<--init if does not exist.
	
	render_radius : 5,
	canvas_name : "myCanvas",
	iteration_colors: ['#ff8800','#0055ff','#0088ff'],
	iteration_index : 0,

  draw_pt: function(pt){
    var c = document.getElementById(this.canvas_name);
    var ctx = c.getContext("2d");
		
		this.iteration_index ++;
		var wrap_dex = this.iteration_index % this.iteration_colors.length;
		
		var centerX = pt.x;
		var centerY = pt.y;
		var radius  = 5;
		
		ctx.beginPath();
    ctx.arc(centerX, centerY, radius, 0, 2 * Math.PI, false);
    ctx.fillStyle = this.iteration_colors[wrap_dex];
    ctx.fill();
    ctx.lineWidth = 5;
    ctx.strokeStyle = '#003300';
    ctx.stroke();
  },

  clear_linked_canvas:function(){
    var c = document.getElementById(this.canvas_name);
    var ctx = c.getContext("2d");
    ctx.clearRect(0, 0, c.width, c.height)
  }

};//point-rendering app
//PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP--END--PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP//