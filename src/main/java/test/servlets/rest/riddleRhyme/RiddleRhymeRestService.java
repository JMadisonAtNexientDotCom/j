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
import test.entities.bases.BaseEntity;
import test.entities.composites.CompositeEntityBase;
import test.entities.composites.CueCard;
import test.servlets.rest.BaseRestService;
import test.transactions.util.TransUtil;
import test.transactions.util.riddleRhyme.RiddleRhymeTransUtil;
import test.transactions.util.riddleRhyme.rhymeRiddle.riddle.RiddleTransUtil;
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
    public Response getIsCorrect(@QueryParam("riddleID") long riddleID, 
                                 @QueryParam("rhymeID")  long rhymeID ){
        
        //Enter transaction state:
        Session ses = TransUtil.enterTransaction();
        
        //Use utility to figure out if riddle+rhyme pair is correct:
        int op = RiddleRhymeTransUtil.getIsCorrect(riddleID, rhymeID);
        
        //Exit transaction state, with NOTHING TO SAVE:
        TransUtil.exitTransaction(ses,TransUtil.EXIT_NO_SAVING);
        
        //DEBUG CODE: REMOVE EVENTUALLY.
        //return Response.ok().build();
        
        //Return the response:
        //If in debug mode, populate comment with useful information.
        String help = "C";//c is for comment.+
        if(DebugConfig.isDebugBuild){
            help = "";
            help += "1==CORRECT,-1==WRONG,0==UNDEFINED";
            help += " riddleID==" + riddleID + " rhymeID==" + rhymeID;
        }
        return JSONUtil.numberToJSONResponse(op, help);
        
    }//FUNC::END
    
    @GET
    @Path("makeFilledOutCueCard")
    public Response makeFilledOutCueCard(
                             @QueryParam("riddleID")        long riddleID, 
                             @QueryParam("numberOfChoices") int numberOfChoices,
                             @QueryParam("numberOfTruths")  int numberOfTruths){
        //Enter transaction state:
        Session ses = TransUtil.enterTransaction();
        
        //Use utility to get what you want:
        CueCard c;
        
        //Handle Error checking more gracefully on REST servlet by returning
        //An "Error Cue Card" Still respond with 200/OK. But the information
        //Sent back will indicate an error on the non-server side.
        if(RiddleTransUtil.doesRiddleExist(riddleID)){
            String msg = "riddle of that id does not exist in database";
            c = CueCard.makeErrorCueCard(msg, numberOfChoices);
        }else
        if(numberOfTruths > numberOfChoices)
        {
            String msg = "numberOfTruths>numberOfChoices";
            c = CueCard.makeErrorCueCard(msg, numberOfChoices);
        }else{
            //Our non-error case. Note that ZERO number of truths is allowed.
            c = RiddleRhymeTransUtil.makeFilledOutCueCard
                                    (riddleID, numberOfChoices, numberOfTruths);
        }//BLOCK::END
        
        //EXIT TRANSACTION!!!
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
        //Create our response:
        CompositeEntityBase ceb = (CompositeEntityBase)c;
        return JSONUtil.compositeEntityToJSONResponse(ceb);
    
    }//FUNC::END
    
    
}//CLASS::END
