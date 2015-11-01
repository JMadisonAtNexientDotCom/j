package annotations;

import app.MyError;

/**
 * Represents an entry into our lookup table.
 * @author jmadison
 */
public class IndexedFunctionTableEntry {
    
    private static String NOT_SET = "NOT_SET";
    
    /** The function name, found via reflection. **/
    public String  funcName = NOT_SET;
    
    /** The class containing the function. **/
    public Class   clazz    = null;
    
    /** Is the function static? **/
    public boolean isStatic = false;
    
    /**
     * throws error if invalid entry configuration.
     * @param entry :Instance of this class.
     */
    public static void validateEntry(IndexedFunctionTableEntry entry){
        if(null == entry){doError("[Entry validate fail. NULL.]");}
        if(null == entry.funcName){
            doError("[Entry validate fail. NULL FUNCNAME.]");
        }
        if("".equals(entry.funcName)){
            doError("[funcName is empty string]");
        }
        if(NOT_SET.equals(entry.funcName)){
            doError("[funcName was never set.]");
        }
        
        if(null == entry.clazz){doError("[clazz ref null]");}

    }//FUNC::END
    
    
    /**-------------------------------------------------------------------------
     * used to debug this class in error messages. ADDED:2015.10.10
     * @param entry :Instance of this class to serialize for debug.
     * @return :See above.
     ------------------------------------------------------------------------**/
    public static String printForDebug(IndexedFunctionTableEntry entry){
        
        String msg = "";
        
        if(null == entry){
            return "[NULL IndexedFunctionTableEntry!!]";
        }//Bail if null.
        
        String fnName = (null != entry.funcName ? entry.funcName : "NULL!");
        String clName = (null != entry.clazz ? entry.clazz.getCanonicalName() 
                                                                 : "NULL!");
        String isStat = (null != entry.funcName ? entry.funcName : "NULL!");
        msg += "funcName:[" + fnName + "]";
        msg += "class:["    + clName + "]";
        msg += "isStatic:[" + isStat + "]";
        
        return msg;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = IndexedFunctionTableEntry.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
