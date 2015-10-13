package test.servlets.rest.restCore.components.trial;

import annotations.PairedStaticFunction;
import annotations.Verbatim;
import test.MyError;
import test.config.constants.identifiers.FuncNameReg;
import test.dbDataAbstractions.requestAndResponseTypes.postTypes.postRequest.Edict;
import test.dbDataAbstractions.requestAndResponseTypes.postTypes.postResponse.Coffer;

/**-----------------------------------------------------------------------------
 * Process:
 * BOOK -- Convert request into an object. "BOOK" the request.
 * CHOP -- Chop booked request into valid and invalid requests. Two halves.
 * FILL -- Fills the INVALID booked order, and the VALID booked order.
 * JOIN -- Joins the invalid+valid back together into one output object.
 *
 * @author jmadison :2015.10.13
 ----------------------------------------------------------------------------**/
public class Fill {
    
    /**
     * Takes the two edicts, and completes their orders, returning two coffers.
     * If a slot is NULL, process is ignored. Only non-error slot (1st slot)
     * is allowed to be null.
     * @param eds:The edicts to fetch info for.
     * @return 
     */
    @PairedStaticFunction //All classes scanned should have func with this name
    @Verbatim( name=FuncNameReg.DISPATCH_TOKENS ) //refactor easier.
    public static Coffer[] dispatch_tokens(Edict[] eds){
        
        if(null == eds[0] && null == eds[1]){doError("both cannot be null");}
        Coffer[] op = new Coffer[2];
        op[0] = dispatch_tokens_process_valid( eds[0] ); //null input allowed.
        op[1] = dispatch_tokens_process_bogus( eds[1] ); //null input allowed.
        return op;
        
    }//FUNC::END
    
    /** Process VALID request. **/
    private static Coffer dispatch_tokens_process_valid( Edict ed){
        
        if(ed==null){return null;}

        //TODO: Actual logic:
        Coffer op = Coffer.makeStubCofferUsingEdict(ed);
        return op;

    }//FUNC::END
    
    /** Process BOGUS request. **/
    private static Coffer dispatch_tokens_process_bogus( Edict ed){
        
        if(ed==null){return null;}

        String msg = "";
        msg += "[dis...process_bogus]";
        msg += ed.comment;
        Coffer op = Coffer.makeErrorCoffer(msg);
        op.errorCode = ed.errorCode;
        return op;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = Fill.class;
        err += clazz.getCanonicalName();//NEED CANONICAL NAME FOR THIS!
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
