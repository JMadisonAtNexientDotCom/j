package test.transactions.cargoSystem.ports.config;

import annotations.UniqueStaticValue;
import annotations.UniqueValueValidator;
import java.util.List;
import test.MyError;

/**
 * A master indexing of all of the ports so that we can help prevent collisions.
 * @author jmadison
 */
public class MasterPortList {
    
    /** All of the values within this enum class are cached into here
     *  so that we can easily look up indicies and validate that they
     *  are valid port function handles. **/
    private static List<Short> _masterIndexValidatorTable = null;
    
    static{/////////////
        doStaticInit();
    }///////////////////
    
    private static void doStaticInit(){
        
        //If we don't put a gaurd here, is the validation of self
        //going to cause infinite recursion?
        
        _masterIndexValidatorTable = UniqueValueValidator.validateStaticShorts
                                                         (MasterPortList.class);
        
        //Validate that master collection of all valid indicies has been
        //properly setup:
        long extractedSumOfShortEnums = _masterIndexValidatorTable.size();
        if( extractedSumOfShortEnums != LONG_CHECK_SUM){
            String checkSumAsString = Long.toString(LONG_CHECK_SUM);
            String actualSumAsString= Long.toString(extractedSumOfShortEnums);
            String msg = "";
            msg += "[CheckSum validation failed.]";
            msg += "[WANTED SUM:[" + checkSumAsString + "]";
            msg += "[ACTUAL SUM:[" + actualSumAsString + "]";
            doError(msg);
        }//Error?
        
    }//FUNC::END
    
    @UniqueStaticValue
    public static final short CREATE_NEW_TOKEN = 1;
    
    /** Check sum so I can validate my _masterIndexValidatorTable extraction
     *  is working properly. **/
    public static final long  LONG_CHECK_SUM = 1;
    
    /**-------------------------------------------------------------------------
     *  throws error if the portID is invalid.
     *  Invalid because is NOT in this class.
     * @param inPortID :The portID to validate. If we don't crash
     *                  after calling this function, then there should
     *                  be an indexed port function with this portID.
     ------------------------------------------------------------------------**/
    public static void validatePortID(short inPortID){
        
        if(_masterIndexValidatorTable.indexOf(inPortID) <= (-1)){
            String msg = "portID is not in master index.";
            msg += "This means it cannot be a valid port/function handle.";
            doError(msg);
        }//Error?
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = MasterPortList.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
