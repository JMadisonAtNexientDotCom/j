package test.servlets.rest.riddleRhyme;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import test.MyError;
import test.config.constants.ServletClassNames;
import test.config.debug.DebugConfig;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import test.dbDataAbstractions.entities.composites.CompositeEntityBase;
import test.dbDataAbstractions.entities.composites.CueCard;
import test.dbDataAbstractions.entities.tables.RiddleTable;
import test.dbDataAbstractions.fracturedTypes.clientServerConversation.lectern.Slate;
import test.servlets.rest.BaseRestService;
import test.transactions.util.TransUtil;
import test.transactions.util.forCompositeEntities.SlateTransUtil;
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
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("gradeOneBlankSlate")
    public Response gradeOneBlankSlate(){
        return null;
    }//FUNC::END
    
   
    
    /**
     * Gets a slate that has been filled out with information from a random
     * riddle. Used to test our "gradeOneBlankSlate() function. 
     * 
     *  This slate should be populated so that when it is graded it checks
     *  out as being 100% correct.
     * 
     * @param riddleID: The riddleID we want to make a filled out slate for.
     *                  A slate being basically a contestant answer card.
     * 
     *                  If riddle ID <=-1, we will retrieve random.
     *                  Otherwise, we retrieve for specific riddle.
     * @return : A slate populated with the CORRECT [rhyme/answer](s) **/
    @Path("getFilledOutTestSlate_TRUTH")
    public Response getFilledOutTestSlate_TRUTH(
                     @DefaultValue("-1") @QueryParam("riddleID") long riddleID){
        return getFilledOutTestSlate_COMMON
                                       (riddleID, Slate.SLATE_DEBUG_TYPE_TRUTH);                     
    }//FUNC::END
    
    /**
     *  Get a test slate for grading that has been populated
     *  so that it will be graded as WRONG. Specifically, 100% incorrect.
     * 
     *  This slate should be populated so that when it is graded it checks
     *  out as being 100% FALSE/INCORRECT.
     * 
     * @param riddleID: The riddleID we want to make a filled out slate for.
     *                  A slate being basically a contestant answer card.
     * 
     *                  If riddle ID <=-1, we will retrieve random.
     *                  Otherwise, we retrieve for specific riddle.
     * @return : A slate populated with the WRONG [rhyme/answers] **/
    @Path("getFilledOutTestSlate_WRONG")
    public Response getFilledOutTestSlate_WRONG(
                     @DefaultValue("-1") @QueryParam("riddleID") long riddleID){
        return getFilledOutTestSlate_COMMON
                                       (riddleID, Slate.SLATE_DEBUG_TYPE_WRONG); 
    }//FUNC::END
    
    private Response getFilledOutTestSlate_COMMON
                                         (long riddleID, String slateDebugType){
        //Enter transaction state:
        Session ses = TransUtil.enterTransaction();
        
        //Transaction logic:
        boolean riddleExists = RiddleTransUtil.doesRiddleExist(riddleID);
        Slate s;
        if(riddleExists){
            s = SlateTransUtil.makeFilledOutTestSlate_COMMON
                                                     (riddleID, slateDebugType);
        }else{
            s = Slate.makeErrorSlate(riddleID);
        }//BLOCK::END
        
        if(null == s){ //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            throw new MyError("how did s become null??");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //EXIT TRANSACTION!!!
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
        //Return JSON response:
        return JSONUtil.fracturedTypeToJSONResponse(s);     
    }//FUNC::END
    
    /**
     * Returns a blank slate representing an "answer card" that the ninja
     *  must fill out. Think of the Jester's CueCard containing a riddle
     *  as a trivial-~persuit~ (board game) card. We don't want to write on it. 
     *  Because it is part of the board game. So we give the ninja a 
     *  blank slate that they can fill out.
     *
     * @param riddleID :The riddle ID this ninja's slate is attempting
     *                  to answer.
     * @return : A blank slate the UI developer can fill out and send back
     *           to the server for grading.                                  **/
    @GET
    @Path("getOneBlankSlate")
    public Response getOneBlankSlate( @QueryParam("riddleID") 
                                           long riddleID){
        
        //Enter transaction state:
        Session ses = TransUtil.enterTransaction();
        
        //Transaction logic:
        //Make sure the riddleID exists. If the riddleID does not exist,
        //The slate will be populated with an ERROR_ID as it's riddleID
        //To notify the [UI/FRONT-END] dev and hopefully prevent error
        //from being graded.
        boolean riddleExists = RiddleTransUtil.doesRiddleExist(riddleID);
        Slate s;
        if(riddleExists){
            s = Slate.makeBlankSlate(riddleID);
        }else{
            s = Slate.makeErrorSlate(riddleID);
        }//BLOCK::END
        
        if(null == s){ //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            throw new MyError("how did s become null??");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //EXIT TRANSACTION!!!
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
        //Return JSON response:
        return JSONUtil.fracturedTypeToJSONResponse(s);
        
    }//FUNC::END
    
    @GET
    @Path("getOneRandomRiddle")
    public Response getOneRandomRiddle(){
        
        //Enter transaction state:
        Session ses = TransUtil.enterTransaction();
        
        //Transaction logic:
        RiddleTable rt = RiddleTransUtil.getOneRandomRiddle();
        
        //EXIT TRANSACTION!!!
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
        //Return JSON response:
        return JSONUtil.entityToJSONResponse(rt);
        
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
        if(numberOfChoices < 1){
            String msg = "[makeFilledOutCueCard needs at least one choice!]";
            c = CueCard.makeErrorCueCard(msg, numberOfChoices);
        }else
        if(numberOfTruths < 0){
            String msg = "[CAN have zero truths, but NEVER negative!!]";
            c = CueCard.makeErrorCueCard(msg, numberOfChoices);
        }else
        if(false == RiddleTransUtil.doesRiddleExist(riddleID)){
            String msg = "[riddle of that id does not exist in database]:";
            msg+= "id==" + riddleID +"]";
            if(riddleID < 0){ msg+= "[(riddle ID is NEGATIVE)]";}
            c = CueCard.makeErrorCueCard(msg, numberOfChoices);
        }else
        if(numberOfTruths > numberOfChoices)
        {
            String msg = "[numberOfTruths>numberOfChoices]";
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
