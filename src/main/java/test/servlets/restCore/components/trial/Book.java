package test.servlets.restCore.components.trial;

import annotations.PairedStaticFunction;
import annotations.Verbatim;
import test.config.constants.EntityErrorCodes;
import test.config.constants.identifiers.FuncNameReg;
import test.dbDataAbstractions.requestAndResponseTypes.postTypes.postRequest.Edict;
import utils.MapperUtil;

/**-----------------------------------------------------------------------------
 * Process:
 * BOOK -- Convert request into an object. "BOOK" the request.
 * CHOP -- Chop booked request into valid and invalid requests. Two halves.
 * FILL -- Fills the INVALID booked order, and the VALID booked order.
 * JOIN -- Joins the invalid+valid back together into one output object.
 * 
 * SHOP -- Object that joins all of these processes into a nice encapsulated
 *         function and returns the correct response object.
 *
 * @author jmadison :2015.10.13
 ----------------------------------------------------------------------------**/
public class Book {
    
    @PairedStaticFunction //All classes scanned should have func with this name
    @Verbatim( name=FuncNameReg.DISPATCH_TOKENS ) //refactor easier.
    public static Edict dispatch_tokens(String jsonText){
        //Convert the request to JSON:
        Edict op;
        try{
            op = MapperUtil.readAsObjectOf(Edict.class, jsonText);
        }catch(Exception ex){
            op = Edict.makeErrorEdict("[JsonText Parse failed]");
            op.errorCode = EntityErrorCodes.JSON_PARSE_ERROR;
        }//END TRY.
        
        return op;
    }//FUNC::END
    
}//CLASS::END
