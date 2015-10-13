package test.debug;

import test.servlets.restCore.TokenCTRL;

/**
 * Constants for debugging/experimenting as I develop.
 * Put whatever experimental debug vars you need here.
 * @author jmadison
 */
public class DebugConsts {
    
    public static String HARD_CODED_TOKEN = "HARD_CODED_TOKEN";
    
    /** This var must be final because experimenting with servlet mapping.
     *  and servlet mapping must use constant expressions.
     */
    
    //Cant do it this way:
    //public static final String HARD_CODED_MAPPING = TokenRestService.class.getSimpleName() + "/";
    public static final String HARD_CODED_MAPPING = "HARD_CODED_MAPPING/";     
            
    
}
