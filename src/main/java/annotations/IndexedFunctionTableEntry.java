package annotations;

/**
 * Represents an entry into our lookup table.
 * @author jmadison
 */
public class IndexedFunctionTableEntry {
    
    /** The function name, found via reflection. **/
    public String  funcName = "NOT_SET";
    
    /** The class containing the function. **/
    public Class   clazz    = null;
    
    /** Is the function static? **/
    public boolean isStatic = false;
    
    
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
    
}//CLASS::END
