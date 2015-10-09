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
    
}//CLASS::END
