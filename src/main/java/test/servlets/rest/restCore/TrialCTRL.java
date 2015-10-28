package test.servlets.rest.restCore;

import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import test.MyError;
import test.config.constants.ServletClassNames;
import test.config.constants.identifiers.FuncNameReg;
import test.config.constants.signatures.paramVals.TRIAL_KIND_ENUMS;
import test.dbDataAbstractions.requestAndResponseTypes.postTypes.postRequest.Edict;
import test.dbDataAbstractions.requestAndResponseTypes.postTypes.postResponse.Coffer;
import test.transactions.util.TransUtil;
import utils.JSONUtil;

//The 4 processes involved in processing request:
import test.servlets.rest.restCore.components.trial.Book; //standardize into object.
import test.servlets.rest.restCore.components.trial.Chop; //Halve object. good/bad
import test.servlets.rest.restCore.components.trial.Fill; //Fill the orders.
import test.servlets.rest.restCore.components.trial.Join; //Join the orders.
import test.servlets.rest.restCore.components.trial.Shop;


/**
 * A controller that manages services mainly concerned with Trials.
 * Trials being the word for tests that the Ninja participates in.
 * @author jmadison
 */
@Path( ServletClassNames.TrialCTRL_MAPPING )
public class TrialCTRL extends BaseCTRL {
    
    /** A wrapper that calls the @POST method dispatch_tokens
     *  so I can more easily debug what is going wrong.
     * @return */
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
        
        return dispatch_tokens(jsonRequest);
        //return dispatch_tokens_PRIVATE(jsonRequest);
    }//FUNC::END
    
     
    /**
     * Will dispatch a trial for each Ninja. Giving them all the
     * same test kind, and the same allotted time.
     * @return :A coffer filled with tickets that contain the token
     *          as well as basic ninja information so both HUMAN and
     *          PROGRAM can identify the ninja.
     */
    @POST
    //@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path(FuncNameReg.DISPATCH_TOKENS)
    public Response dispatch_tokens(String jsonRequest){ 
        doError("Will this show up?");
        return Shop.dispatch_tokens(jsonRequest);
    }//FUNC::END
    
   
    
    /*
    old version in progress of being replaced.
    private Response dispatch_tokens_PRIVATE(String jsonRequest){
        //Enter transaction state:
        Session ses = TransUtil.enterTransaction();
        
        //variable declare:
        Coffer tickets = null; //<--make compiler happy.
        
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
        
       
        
        //If we get to here, error check the edict:
        BooleanWithComment stat = Edict.validate(trialTokenDispatchEdict);
        if(false == stat.value){
            hasError = true;
            tickets = Coffer.makeErrorCoffer(stat.comment);
        }//Error of bad inputs!
        
        
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
    */
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = TrialCTRL.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END


//Thoughts on analogy:

//      STAGE                    BACK STAGE            BOX OFFICE
// |----------------------||---------------------||--------------------------|
// | Front End Where      || BACK-END. Where info|| Front End Where ADMINS   |
// | Ninja takes trials   || is processed and    || Dispatch tokens that     |
// |                      || spit back to stage  || allow the Ninjas to      |
// |                      || and the box office. || participate in the show. |