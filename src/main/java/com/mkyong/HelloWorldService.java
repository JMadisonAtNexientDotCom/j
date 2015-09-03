package com.mkyong.rest;
 
/** Source URL: http://www.mkyong.com/webservices/jax-rs/jersey-hello-world-example/
 *  Just cutting+pasting this tutorial so we can get a restful API up and running
 *  using JERSY. Between JERSY and SPRING, it is the lighter-weight option,
 *  so I am going to go with it. LESS PARTS == LESS THINGS THAT CAN GO WRONG.
 */

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
 
@Path("/hello")
public class HelloWorldService {
 
	@GET
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String msg) {
 
		String output = "Jersey say : " + msg;
 
		return Response.status(200).entity(output).build();
 
	}
 
}
