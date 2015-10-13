package test.servlets.rest.restCore.components.trial;
import annotations.PairedStaticFunction;
import annotations.Verbatim;
import javax.ws.rs.core.Response;
import test.dbDataAbstractions.requestAndResponseTypes.postTypes.postRequest.Edict;
import test.dbDataAbstractions.requestAndResponseTypes.postTypes.postResponse.Coffer;
import test.config.constants.identifiers.FuncNameReg;
import utils.JSONUtil;

/**
 * The shop is the area that houses the functions that bundle together all
 * of the request processing steps. Doing this so we can make the main
 * servlet code less verbose.
 * @author jmadison
 */
public class Shop {
    
    
    @PairedStaticFunction //All classes scanned should have func with this name
    @Verbatim( name=FuncNameReg.DISPATCH_TOKENS ) //refactor easier.
    public static Response dispatch_tokens(String jsonRequest){
        //return dispatch_tokens_PRIVATE(jsonRequest);
      
        //Process Summary: We convert input into an object,
        //we then CHOP that object into two objects, one representing a
        //bad request, one representing a good request. We then fill both
        //orders, assuming the first entry always exists, and the 2nd entry
        //of array, it exists, is an order with BAD INPUT DATA.
        //We then JOIN the two outputs together. So that all information is
        //given back, with the bad data at the end.
        Edict    ed  = Book.dispatch_tokens(jsonRequest); //Convert req to obj
        Edict [] eds = Chop.dispatch_tokens(ed);          //Chop into good+bad
        Coffer[] cofs= Fill.dispatch_tokens(eds);         //Fill orders
        Coffer   cof = Join.dispatch_tokens(cofs);        //Join into one object
        
        //Convert Coffer into Json response:
        Response op = JSONUtil.postResponseToJSONResponse(cof);
        return op;
    }//FUNC::END
}//CLASS::END
