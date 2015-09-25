package frontEndBackEndIntegration;

//No package. At root to make less hazardous for refactoring.
//Since it will be put into .JSP files.
import frontEndBackEndIntegration.childComponents.FBVarNameRegistry;


/**
 * 
 * ROOT class that is used to house information for front-end-back-end
 * integration. For the most part, this is getting our UI code to use
 * the same variable names and service URLS as our back-end.
 * 
 * Breaking some conventions and just calling this "FB".
 * In order to make a concise way to access variable names that
 * need to be used in both the front end and back ends.
 * 
 * @author jmadison :2015.09.25
 */
public class I {
    
    /** V == VARNAMES **/
    public static final FBVarNameRegistry V = new FBVarNameRegistry();
}//CLASS::END
