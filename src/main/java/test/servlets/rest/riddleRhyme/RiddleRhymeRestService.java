package test.servlets.rest.riddleRhyme;
import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import test.config.constants.ServletClassNames;
import test.config.debug.DebugConfig;
import test.servlets.rest.BaseRestService;
import test.transactions.util.TransUtil;
import test.transactions.util.riddleRhyme.RiddleRhymeTransUtil;
import utils.JSONUtil;

/** A rest service class that handles any api calls involving our --------------
 *  1: riddle table (questions)
 *  2: rhyme  table (answers)
 * @author jmadison ---------------------------------------------------------**/
@Path(ServletClassNames.RiddleRhymeRestService_MAPPING)
public class RiddleRhymeRestService extends BaseRestService {
    
    /** Returns an integer code telling you if the answer is correct or not.
     * 
     * @param riddleID
     * @param rhymeID
     * @return : 1 == TRUE
     *          -1 == FALSE
     *           0 == UNDEFINED
     */
    @GET
    @Path("getIsCorrect")
    public Response getIsCorrect(long riddleID, long rhymeID){
        
        //Enter transaction state:
        Session ses = TransUtil.enterTransaction();
        
        //Use utility to figure out if riddle+rhyme pair is correct:
        int op = RiddleRhymeTransUtil.getIsCorrect(riddleID, rhymeID);
        
        //Exit transaction state, with NOTHING TO SAVE:
        TransUtil.exitTransaction(ses,TransUtil.EXIT_NO_SAVING);
        
        return Response.ok().build();
        
        /*
        //Return the response:
        //If in debug mode, populate comment with useful information.
        String help = "C";//c is for comment.+
        if(DebugConfig.isDebugBuild)
        {help = "1==CORRECT,-1==WRONG,0==UNDEFINED";}
        return JSONUtil.numberToJSONResponse(op, help);
                */
        
    }//FUNC::END
    
    
    
}//CLASS::END
