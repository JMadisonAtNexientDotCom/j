package test.servlets.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
 
@Path("/")
public class TokenRestService {
 
	@GET
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String msg) {
 
		String output = "Servlet: TokenRestService : " + msg;
 
		return Response.status(200).entity(output).build();
 
	}
 
}