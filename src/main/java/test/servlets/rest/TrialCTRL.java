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
        
        //Enter transaction state:
        Session ses = TransUtil.enterTransaction();
        
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
        
        //Exit transaction state:
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
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
        boolean hasError = false;
        Edict trialTokenDispatchEdict;
        try{
            trialTokenDispatchEdict = MapperUtil.readAsObjectOf
                                                     (Edict.class, jsonRequest);
        }catch(Exception ex){
            hasError = true;
            trialTokenDispatchEdict = null;
        }//END TRY.
        
        Coffer tickets = null; //<--make compiler happy.
        if(false == hasError){
            //Use the data to dispatch tokens:
            try{
                tickets = TrialTransUtil.dispatchTokens(
                                   trialTokenDispatchEdict.ninja_id_list,
                                   trialTokenDispatchEdict.trial_kind,
                                   trialTokenDispatchEdict.duration_in_minutes);
            }catch(Exception theProblem){
                hasError = true;
            }//TRY::END
        }//Another opprotunity to error.
        
        if(true == hasError){
            String msg = "[SomeWeird Error happened.]";
            if(null != trialTokenDispatchEdict){    
                String isNull;
                int    ninjaLen;
                String ninjaLenAsString;
                String tKind = Integer.toString
                                           (trialTokenDispatchEdict.trial_kind);
                String tDura = Integer.toString
                                  (trialTokenDispatchEdict.duration_in_minutes);
                if(null == trialTokenDispatchEdict.ninja_id_list){
                    isNull = "TRUE";
                    ninjaLenAsString = "N/A"; //<--cant get len from null ref.
                }else{
                    isNull = "FALSE";
                    ninjaLen = trialTokenDispatchEdict.ninja_id_list.size();
                    ninjaLenAsString = Integer.toString(ninjaLen);
                }//
                msg += "and I don't know how to fix it!]";
                msg += "ninja_id_list null? : [" + isNull + "]";
                msg += "ninja_id_list length?:[" + ninjaLenAsString + "]";
                msg += "trial_kind -->[" + tKind + "]";
                msg += "duration_in_minutes:" + tDura + "]";
            }else{
                msg += "THE TICKET COFFER IS NULL!";
            }//BLOCK::END
            
            tickets = Coffer.makeErrorCoffer(msg);
        }//BLOCK::END
        
        //EXIT WITHOUT SAVE BECAUSE WE ARE STILL TESTING.
        //Exit transaction state before returning data:
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
        //make absolutely sure tickets are not null:
        if(null == tickets){
            tickets = Coffer.makeErrorCoffer("[tickets were null!]");
        }//Extra check to make compiler happy.
        
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