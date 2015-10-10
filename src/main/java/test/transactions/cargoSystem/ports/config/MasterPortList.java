package test.transactions.cargoSystem.ports.config;

import annotations.UniqueStaticValue;

/**
 * A master indexing of all of the ports so that we can help prevent collisions.
 * @author jmadison
 */
public class MasterPortList {
    
    static{/////////////
        doStaticInit();
    }///////////////////
    
    private static void doStaticInit(){
        
    }//FUNC::END
    
    @UniqueStaticValue
    public static final short CREATE_NEW_TOKEN = 1;
    
    /**-------------------------------------------------------------------------
     *  throws error if the portID is invalid.
     *  Invalid because is NOT in this class.
     * @param inPortID :The portID to validate. If we don't crash
     *                  after calling this function, then there should
     *                  be an indexed port function with this portID.
     ------------------------------------------------------------------------**/
    public static void validatePortID(short inPortID){
        
    }//FUNC::END
}//CLASS::END
