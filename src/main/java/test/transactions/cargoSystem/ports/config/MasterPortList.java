package test.transactions.cargoSystem.ports.config;

import annotations.UniqueStaticValue;
import annotations.UniqueValueValidator;
import java.util.List;
import test.MyError;
import test.transactions.cargoSystem.dataTypes.GalleonBarge;
import test.transactions.cargoSystem.dataTypes.OrderSlip;
import test.transactions.cargoSystem.ports.TokenPorts;

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
    
    //TOKEN PORTS:
    @UniqueStaticValue
    public static final short CREATE_NEW_TOKEN = 1;
    
    //NINJA PORTS:
    @UniqueStaticValue
    public static final short GET_ONE_NINJA_BY_ID = 2;
   
    
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
    
    /** A lookup table. A bit hackish, but cannot get reflection to work.
     *  So this is the best idea I have to remidy the problem.
     * @param barge :The barge (cargo ship) to operate on.
     * @param order   :The current order to fill.
     */
    public static void call(GalleonBarge galleon, OrderSlip order){
        
        //make sure portID is valid:
        short portID = order.portID;
        validatePortID(portID);
        
        //Enter my hackish lookup table. Because Java sucks and functions are
        //not first class citizens and reflection causes weird untraceable 
        //errors.
        int bc = 0;//break count.
        switch(portID){
        case CREATE_NEW_TOKEN:
            TokenPorts.create_new_token(galleon, order);
            bc++; break;
        default:
            //Default should never trigger, because "validatePortID()" validates
            //that we are using a valid portID. If default triggers, it means
            //we forgot to add a function to our lookup table.
            throw new Error("[Switch case forgotten for valid table index!]");
            //bc++; break;
        }//SWITCH::END
        
        //hackish way to make sure we don't accidentially fall-through
        //from forgetting to use break keyword:
        if(bc != 1){
            doError("[Missing a break in switch statement likely.]");
        }//break count != 1 ?
        
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
