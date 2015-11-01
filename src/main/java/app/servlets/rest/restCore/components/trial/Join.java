package app.servlets.rest.restCore.components.trial;

import annotations.PairedStaticFunction;
import annotations.Verbatim;
import app.MyError;
import app.config.constants.identifiers.FuncNameReg;
import app.dbDataAbstractions.requestAndResponseTypes.postTypes.postResponse.Coffer;

/**-----------------------------------------------------------------------------
 * Process:
 * BOOK -- Convert request into an object. "BOOK" the request.
 * CHOP -- Chop booked request into valid and invalid requests. Two halves.
 * FILL -- Fills the INVALID booked order, and the VALID booked order.
 * JOIN -- Joins the invalid+valid back together into one output object.
 *
 * @author jmadison :2015.10.13
 ----------------------------------------------------------------------------**/
public class Join {
    /**
     * Joins the two coffers into ONE object. Where the error entries are
     * at the END. However, if object has errors, we try to take all of the
     * good data we can. But we will have to mark the entire object as an
     * error, since we don't know what to trust.
     * @param cofs :The coffers to join.
     * @return :A joined coffer.
     */
    @PairedStaticFunction //All classes scanned should have func with this name
    @Verbatim( name=FuncNameReg.DISPATCH_TOKENS ) //refactor easier.
    public static Coffer dispatch_tokens(Coffer[] cofs){
        if(null==cofs[0] && null==cofs[1]){doError("[BothNotAllowedNull]");}
        
        //Only Valid Entries:
        if(null == cofs[1]){
            return cofs[0]; //return only the VALID.
        }else //only bogus entries:
        if(null == cofs[0]){
            return cofs[1]; //return only the BOGUS.
        }else{ //A mix of valid & bogus:
            return cofs[1]; //return only the BOGUS.
        }//BLOCK::END
    }//FUNC::END
    
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = Join.class;
        err += clazz.getCanonicalName(); //need canonical name for this class!
        err += msg;                      //Because has one-word class name.
        throw MyError.make(clazz, err);
    }//FUNC::END
}//CLASS::END
