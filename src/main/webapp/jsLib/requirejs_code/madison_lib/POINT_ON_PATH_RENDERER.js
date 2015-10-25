//Point-ON-PATH-Rendering app:
//PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP--START--PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP//
var POINT_ON_PATH_RENDERER = POINT_ON_PATH_RENDERER || { //<--init if does not exist.
	
	canvas_name : "myCanvas",
	iteration_colors: ['#ff8800','#0055ff','#0088ff'],
	iteration_index : 0,

  draw_path_point: function(path){
    var pt = path.getPos();
		POINT_RENDERER.render_radius = path.rad;
		POINT_RENDERER.draw_pt(pt);
  },

  clear_linked_canvas:function(){
    var c = document.getElementById(this.canvas_name);
    var ctx = c.getContext("2d");
    ctx.clearRect(0, 0, c.width, c.height)
  }

};//point-ON-PATH-rendering app
//PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP--END--PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP//