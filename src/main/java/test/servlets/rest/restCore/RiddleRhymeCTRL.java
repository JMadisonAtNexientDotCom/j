package test.servlets.rest.restCore;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import test.MyError;
import test.config.constants.ServletClassNames;
import test.config.constants.identifiers.FuncNameReg;
import test.config.constants.identifiers.VarNameReg;
import test.config.debug.DebugConfig;
import test.dbDataAbstractions.bundleTypes.TriviaBundle;
import test.dbDataAbstractions.entities.bases.CompositeEntityBase;
import test.dbDataAbstractions.entities.composites.CueCard;
import test.dbDataAbstractions.entities.composites.Quar;
import test.dbDataAbstractions.entities.tables.RiddleTable;
import test.dbDataAbstractions.fracturedTypes.clientServerConversation.lectern.Slate;
import test.debug.debugUtils.tempDataStore.TempServiceDataUtil;
import test.servlets.rest.restCore.BaseCTRL;
import test.transactions.util.TransUtil;
import test.transactions.util.forBundleEntities.TriviaBundlePojoUtil;
import test.transactions.util.forCompositeEntities.SlatePojoUtil;
import test.transactions.util.forNoClearTableOwner.riddleRhyme.RiddleRhymeTransUtil;
import test.transactions.util.forNoClearTableOwner.riddleRhyme.rhymeRiddle.riddle.RiddleTransUtil;
import utils.JSONUtil;
import utils.MapperUtil;

/** A rest service class that handles any api calls involving our --------------
 *  1: riddle table (questions)
 *  2: rhyme  table (answers)
 * @author jmadison ---------------------------------------------------------**/
@Path(ServletClassNames.RiddleRhymeCTRL_MAPPING)
public class RiddleRhymeCTRL extends BaseCTRL {
    
    /** Returns an integer code telling you if the answer is correct or not.----
     * @param riddle_id :The ID# of riddle (question) being asked.
     * @param rhyme_id  :The ID# of rhyme answering the [riddle/question].
     * @return : 1 == TRUE
     *          -1 == FALSE
     *           0 == UNDEFINED ----------------------------------------------*/
    @GET
    @Path(FuncNameReg.GET_IS_CORRECT)
    public Response get_is_correct(
            @QueryParam(VarNameReg.RIDDLE_ID) long riddle_id, 
            @QueryParam(VarNameReg.RHYME_ID)  long rhyme_id ){
        
        //Enter transaction state:
        Session ses = TransUtil.enterTransaction();
        
        //Use utility to figure out if riddle+rhyme pair is correct:
        int op = RiddleRhymeTransUtil.getIsCorrect(riddle_id, rhyme_id);
        
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
            help += " riddleID==" + riddle_id + " rhymeID==" + rhyme_id;
        }
        return JSONUtil.numberToJSONResponse(op, help, JSONUtil.ALL_IS_WELL);
        
    }//FUNC::END
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(FuncNameReg.GRADE_ONE_BLANK_SLATE)
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
     * @param riddle_id: The riddleID we want to make a filled out slate for.
     *                  A slate being basically a contestant answer card.
     * 
     *                  If riddle ID <=-1, we will retrieve random.
     *                  Otherwise, we retrieve for specific riddle.
     * @return : A slate populated with the CORRECT [rhyme/answer](s) **/
    @GET
    @Path(FuncNameReg.GET_FILLED_OUT_TEST_SLATE_TRUTH)
    public Response get_filled_out_test_slate_truth(
          @DefaultValue("-1") @QueryParam(VarNameReg.RIDDLE_ID) long riddle_id){
        return getFilledOutTestSlate_COMMON
                                      (riddle_id, Slate.SLATE_DEBUG_TYPE_TRUTH);                     
    }//FUNC::END
    
    /**
     *  Get a test slate for grading that has been populated
     *  so that it will be graded as WRONG. Specifically, 100% incorrect.
     * 
     *  This slate should be populated so that when it is graded it checks
     *  out as being 100% FALSE/INCORRECT.
     * 
     * @param riddle_id: The riddleID we want to make a filled out slate for.
     *                  A slate being basically a contestant answer card.
     * 
     *                  If riddle ID <=-1, we will retrieve random.
     *                  Otherwise, we retrieve for specific riddle.
     * @return : A slate populated with the WRONG [rhyme/answers] **/
    @GET
    @Path(FuncNameReg.GET_FILLED_OUT_TEST_SLATE_WRONG)
    public Response get_filled_out_test_slate_wrong(
          @DefaultValue("-1") @QueryParam(VarNameReg.RIDDLE_ID) long riddle_id){
        return getFilledOutTestSlate_COMMON
                                      (riddle_id, Slate.SLATE_DEBUG_TYPE_WRONG); 
    }//FUNC::END
    
    /** NOT WIRED TO OUTSIDE. So we can leave named as is **/
    private Response getFilledOutTestSlate_COMMON
                                         (long riddleID, String slateDebugType){
        //Enter transaction state:
        Session ses = TransUtil.enterTransaction();
        
        
        //Transaction logic:
        //If the riddleID exists, NO ERROR.
        //If the riddleID is NEGATIVE, also NO ERROR. Because in this
        //Case, negative signifies we want a RANDOM riddle.
        Slate s;
        boolean riddleExists = RiddleTransUtil.doesRiddleExist(riddleID);
        if(riddleExists || (riddleID<0) ){
            s = SlatePojoUtil.makeFilledOutTestSlate_COMMON
                                                     (riddleID, slateDebugType);
        }else{
            s = Slate.makeErrorSlate(riddleID);
        }//BLOCK::END
        
        if(null == s){ //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            doError("how did s become null??");
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
     * @param riddle_id :The riddle ID this ninja's slate is attempting
     *                  to answer.
     * @return : A blank slate the UI developer can fill out and send back
     *           to the server for grading.                                  **/
    @GET
    @Path(FuncNameReg.GET_ONE_BLANK_SLATE)
    public Response get_one_blank_slate( 
                               @QueryParam(VarNameReg.RIDDLE_ID)long riddle_id){
        
        //Enter transaction state:
        Session ses = TransUtil.enterTransaction();
        
        //Transaction logic:
        //Make sure the riddleID exists. If the riddleID does not exist,
        //The slate will be populated with an ERROR_ID as it's riddleID
        //To notify the [UI/FRONT-END] dev and hopefully prevent error
        //from being graded.
        boolean riddleExists = RiddleTransUtil.doesRiddleExist(riddle_id);
        Slate s;
        if(riddleExists){
            s = Slate.makeBlankSlate(riddle_id);
        }else{
            s = Slate.makeErrorSlate(riddle_id);
        }//BLOCK::END
        
        if(null == s){ //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            doError("how did s become null??");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //EXIT TRANSACTION!!!
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
        //Return JSON response:
        return JSONUtil.fracturedTypeToJSONResponse(s);
        
    }//FUNC::END
    
    @GET
    @Path(FuncNameReg.GET_ONE_RANDOM_RIDDLE)
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
    @Path(FuncNameReg.MAKE_FILLED_OUT_CUE_CARD)
    public Response make_filled_out_cue_card(
                @QueryParam(VarNameReg.RIDDLE_ID)        long riddle_id, 
                @QueryParam(VarNameReg.NUMBER_OF_CHOICES) int number_of_choices,
                @QueryParam(VarNameReg.NUMBER_OF_TRUTHS)  int number_of_truths){
        //Enter transaction state:
        Session ses = TransUtil.enterTransaction();
        
        //Use utility to get what you want:
        CueCard c;
        
        //Handle Error checking more gracefully on REST servlet by returning
        //An "Error Cue Card" Still respond with 200/OK. But the information
        //Sent back will indicate an error on the non-server side.
        if(number_of_choices < 1){
            String msg = "[makeFilledOutCueCard needs at least one choice!]";
            c = CueCard.makeErrorCueCard(msg, number_of_choices);
        }else
        if(number_of_truths < 0){
            String msg = "[CAN have zero truths, but NEVER negative!!]";
            c = CueCard.makeErrorCueCard(msg, number_of_choices);
        }else
        if(false == RiddleTransUtil.doesRiddleExist(riddle_id)){
            String msg = "[riddle of that id does not exist in database]:";
            msg+= "id==" + riddle_id +"]";
            if(riddle_id < 0){ msg+= "[(riddle ID is NEGATIVE)]";}
            c = CueCard.makeErrorCueCard(msg, number_of_choices);
        }else
        if(number_of_truths > number_of_choices)
        {
            String msg = "[numberOfTruths>numberOfChoices]";
            c = CueCard.makeErrorCueCard(msg, number_of_choices);
        }else{
            //Our non-error case. Note that ZERO number of truths is allowed.
            c = RiddleRhymeTransUtil.makeFilledOutCueCard
                                    (riddle_id, number_of_choices, number_of_truths);
        }//BLOCK::END
        
        //EXIT TRANSACTION!!!
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
        //Create our response:
        CompositeEntityBase ceb = (CompositeEntityBase)c;
        return JSONUtil.compositeEntityToJSONResponse(ceb);
    
    }//FUNC::END
    
    @GET
    @Path(FuncNameReg.GET_RANDOM_TRIVIA_BUNDLE)
    public Response get_random_trivia_bundle
                    (@DefaultValue("5") @QueryParam(VarNameReg.CARD_COUNT) int card_count, 
                     @DefaultValue("4") @QueryParam(VarNameReg.NUM_QUIPS)  int num_quips, 
                     @DefaultValue("0") @QueryParam(VarNameReg.TRU_MIN)    int tru_min, 
                     @DefaultValue("2") @QueryParam(VarNameReg.TRU_MAX)    int tru_max){
                            
        //Enter a transaction state:
        Session ses = TransUtil.enterTransaction();
        
        //Core Logic:
        TriviaBundle bund = TriviaBundlePojoUtil.getRandomTrivaBundle
                                          (card_count, num_quips, tru_min, tru_max);
        
        //Create our output response:
        Response op = JSONUtil.bundleEntityToJSONResponse(bund);
       
        //Exit Transaction state, no saving:
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
        //return response:
        return op;
        
    }//FUNC::END
         
    // http://www.vogella.com/tutorials/REST/article.html
    //http://stackoverflow.com/questions/1662490/consuming-json-object-in-jersey-service
    /**-------------------------------------------------------------------------
     * 
     * Post the QUAR for grading.
     * Will eventually need to include a session token as a handle
     * that will allow you to get the results in a later GET call.
     * 
     * DESIGN THOUGHT:
     * Best design choice is to return 201 for created and have a separate
     * GET for getting the results of the POST.
     * @param  jsonRequest: The JSON data sent from the client/ui side.
     * @return :Results of post to UI front end.
     ------------------------------------------------------------------------**/
    @POST
    //@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path(FuncNameReg.POST_QUAR_FOR_GRADING)
    public Response post_quar_for_grading(String jsonRequest){
        
        //Convert the request to JSON:
        Quar slateQuar = MapperUtil.readAsObjectOf(Quar.class, jsonRequest);
        
        //Store it in temporary place for testing:
        TempServiceDataUtil.theQuar = slateQuar;
        
        //Send back a 200OK response with nothing.
        return Response.ok().build();
    }//FUNC::END
    
    /**
     * TEMPORARY: Gets results of last graded quar that is in a debug utility.
     * Doing this until we get proper session management working.
     * @return 
     */
    @GET
    @Path(FuncNameReg.GET_LAST_POSTED_QUAR)
    public Response get_last_posted_quar(){
        Quar theQuar = TempServiceDataUtil.theQuar;
        return JSONUtil.compositeEntityToJSONResponse(theQuar);
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = RiddleRhymeCTRL.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
