package test.servlets.restCore;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**-----------------------------------------------------------------------------
 * A base rest service from which all other rest services will be derived.
 * Reason:
 * So we can setup new rest servlets in smaller incriments.
 * When creating a new servlet, we can make it a STUB that extends
 * BaseRestService. We can then use the ping methods on our new stub servlet
 * to make sure it is wired in correctly. Once we know the servlet is configured
 * correctly, we can go on writing more code.
 * 
 * //My design philosophy here:
 * /////////////////////////////////////////////////////////////////////////////
 * 1. Small change. 
 * 2. Small commit. 
 * 3. Test to make sure still working.
 * Repeat in smallest possible steps that will result in UNBROKEN product.
 * Planning out the increments to your final goal is a good idea.
 * /////////////////////////////////////////////////////////////////////////////
 * 
 * Debug notes:
 * This error may lead you to believe it is an entity serialization issue on
 * a JSON response from a servlet. But this actually happened to me because I
 * left off the @QueryParam annotations on the servlet function.
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * HTTP Status 415 - Unsupported Media Type
   type Status report
   message: Unsupported Media Type
   description: The server refused this request because the request entity is 
   *            in a format not supported by the requested resource for the 
   *            requested method.
   !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * 
 * @author jmadison ---------------------------------------------------------**/
public class BaseCTRL {
    
    /**
     * Ping the service using this url formulation:
     * [AppBaseURL] / [API Root] / [Servlet's Name] / "ping?msg=helloworld"
     * 
     * AppBaseURL: The root domain of your web application.
     * Example: http://myDomain.com
     * 
     * API Root: The relative url that defines where all your API services are.
     * Example: "api" (with AppBaseURL we have: http://myDomain.com/api/
     * 
     * Servlet's Name: The class name, servlet name, and service name 
     *                                                  should all be IDENTICAL.
     * Example: "BaseRestService"  (derived from BaseRestService.java)
     * 
     * @param msg: The test message we would like to bounce off the servlet.
     * @return : A response where servlet identifies itself and confirms
     *           the contents of the message you sent to it.                 **/
    @GET
    @Path("ping")
    public Response ping(@QueryParam("msg") String msg) {
        return getCommonPingResponse(msg);
    }//FUNC::END
    
    /** An alternate way to ping the service that might be more friendly
     *  to typing by hand. But less friendly when trying to implement using
     *  an angular $httpGet request.
     * @param path :Some made up path used as message to ping service.
     * @return     :A message containing the path, confirming data was
     *              successfully bounced from client to service and back. **/
    @GET
    @Path("ping/{path}")
    public Response pingPath(@PathParam("path") String path) {
        return getCommonPingResponse(path);
    }//FUNC::END
    
    /** Common shared code between ping & pingPath functions.
     *  What it does:
     *  1: Servlet identifies itself in response.
     *     WHY: Helps us confirm we are talking to correct servlet.
     *  2: Servlet spits back message that was given to it.
     *     WHY: To confirm information was successfully bounced without loss.
     *          AKA: 2-way communication is working.
     * @param baseMessage : The test message you would like to use.
     * @return : A response confirming the servlet is there and listening. **/
    private Response getCommonPingResponse(String baseMessage){
        String className = this.getClass().getSimpleName();
        String output = "Servlet:["+ className + "] says:[" + baseMessage + "]";
        return Response.status(200).entity(output).build();
    }//FUNC::END
    
    
}//CLASS::END
