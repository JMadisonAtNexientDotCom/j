package test.servlets.rest;

import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import test.config.constants.ServletClassNames;
import test.config.constants.identifiers.FuncNameReg;
import test.config.constants.signatures.paramVals.TRIAL_KIND_ENUMS;
import test.dbDataAbstractions.entities.composites.Quar;
import test.dbDataAbstractions.requestAndResponseTypes.postTypes.postRequest.Edict;
import test.dbDataAbstractions.requestAndResponseTypes.postTypes.postResponse.Coffer;
import test.debug.debugUtils.tempDataStore.TempServiceDataUtil;
import test.transactions.util.TransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.trial.TrialTransUtil;
import utils.JSONUtil;
import utils.MapperUtil;

/**
 * A controller that manages services mainly concerned with Trials.
 * Trials being the word for tests that the Ninja participates in.
 * @author jmadison
 */
@Path( ServletClassNames.TrialCTRL_MAPPING )
public class TrialCTRL extends BaseCTRL {
    
    /** A wrapper that calls the @POST method dispatch_tokens
     *  so I can more easily debug what is going wrong.
     * @return 
     */
    @GET
    @Path(FuncNameReg.DISPATCH_TOKENS_DEBUG)
    public Response dispatch_tokens_debug(){
        
        Edict testEdict = new Edict();
        testEdict.comment = "touched by dispatch_tokens_debug";
        testEdict.duration_in_minutes = 30;
        testEdict.trial_kind = TRIAL_KIND_ENUMS.RIDDLE_TRIAL_;
        testEdict.ninja_id_list = new ArrayList<Long>();
        testEdict.ninja_id_list.add(new Long(1) );
        testEdict.ninja_id_list.add(new Long(2) );
        testEdict.ninja_id_list.add(new Long(3) );
        testEdict.ninja_id_list.add(new Long(4) );
        
        //Convert testEdict into text:
        String jsonRequest;
        jsonRequest = JSONUtil.serializeObj_NoNULL(testEdict);
        return dispatch_tokens_PRIVATE(jsonRequest);
    }//FUNC::END
    
    @POST
    //@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path(FuncNameReg.DISPATCH_TOKENS)
    public Response dispatch_tokens(String jsonRequest){
        return dispatch_tokens_PRIVATE(jsonRequest);
    }//FUNC::END
    
    private Response dispatch_tokens_PRIVATE(String jsonRequest){
        //Enter transaction state:
        Session ses = TransUtil.enterTransaction();
        
        //Convert the request to JSON:
        Edict trialTokenDispatchEdict = 
                            MapperUtil.readAsObjectOf(Edict.class, jsonRequest);
        
        //Use the data to dispatch tokens:
        Coffer tickets = TrialTransUtil.dispatchTokens(
                                   trialTokenDispatchEdict.ninja_id_list,
                                   trialTokenDispatchEdict.trial_kind,
                                   trialTokenDispatchEdict.duration_in_minutes);
        
        //EXIT WITHOUT SAVE BECAUSE WE ARE STILL TESTING.
        //Exit transaction state before returning data:
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
        //Send back a 200OK response with the data!
        return JSONUtil.postResponseToJSONResponse(tickets);
    }//FUNC::END
    
}//CLASS::END


//Thoughts on analogy:

//      STAGE                    BACK STAGE            BOX OFFICE
// |----------------------||---------------------||--------------------------|
// | Front End Where      || BACK-END. Where info|| Front End Where ADMINS   |
// | Ninja takes trials   || is processed and    || Dispatch tokens that     |
// |                      || spit back to stage  || allow the Ninjas to      |
// |                      || and the box office. || participate in the show. |