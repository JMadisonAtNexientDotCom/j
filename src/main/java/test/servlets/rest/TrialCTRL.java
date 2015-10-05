package test.servlets.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import test.config.constants.ServletClassNames;
import test.config.constants.identifiers.FuncNameReg;
import test.dbDataAbstractions.entities.composites.Quar;
import test.dbDataAbstractions.requestAndResponseTypes.postTypes.postRequest.Edict;
import test.dbDataAbstractions.requestAndResponseTypes.postTypes.postResponse.Coffer;
import test.debug.debugUtils.tempDataStore.TempServiceDataUtil;
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
    
    @POST
    //@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path(FuncNameReg.DISPATCH_TOKENS)
    public Response dispatch_tokens(String jsonRequest){
        
        //Convert the request to JSON:
        Edict trialTokenDispatchEdict = 
                            MapperUtil.readAsObjectOf(Edict.class, jsonRequest);
        
        //Use the data to dispatch tokens:
        Coffer tickets = TrialTransUtil.dispatchTokens(
                                   trialTokenDispatchEdict.ninja_id_list,
                                   trialTokenDispatchEdict.trial_kind,
                                   trialTokenDispatchEdict.duration_in_minutes);
        
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