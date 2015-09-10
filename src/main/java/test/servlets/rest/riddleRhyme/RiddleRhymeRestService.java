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
    
    /** Been duplicating this code a lot. Maybe we can put this in a base
     *  class and have it called "getTestMessage" ?
     *  It can be used basically as a ping.. Maybe it should be called...
     *  "ping" Since that is the usage. A basic connectivity test.
     *  Can you get back the message you sent via a ping?
     * @param msg
     * @return 
     */
    @GET
    @Path("ping")
    public Response getMsg(@QueryParam("msg") String msg) {

        String output = "Servlet: RiddleRhymeRestService : " + msg;

        return Response.status(200).entity(output).build();

    }//FUNC::END
    
    
}//CLASS::END
