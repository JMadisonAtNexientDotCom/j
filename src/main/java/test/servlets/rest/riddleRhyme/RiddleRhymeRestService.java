package test.servlets.rest.riddleRhyme;
import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import test.config.constants.ServletClassNames;

/** A rest service class that handles any api calls involving our
 *  1: riddle table (questions)
 *  2: rhyme  table (answers)
 * @author jmadison **/
@Path(ServletClassNames.RiddleRhymeRestService_MAPPING)
public class RiddleRhymeRestService {
    
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
