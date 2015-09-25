package frontEndBackEndIntegration;

//No package. At root to make less hazardous for refactoring.
//Since it will be put into .JSP files.
import frontEndBackEndIntegration.childComponents.FBVarNameRegistry;
import frontEndBackEndIntegration.childComponents.ServiceURLRegistry;



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
    
    //Variable names used by services:
    //VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV
    /** Shared reference so that we can have shorthand and longhand versions
     *  to be able to put into the code. **/
    private static final FBVarNameRegistry _varNameRegSharedRef = 
                                                        new FBVarNameRegistry();
    /** Shorthand version of VARNAME **/
    public static final FBVarNameRegistry V = _varNameRegSharedRef;
    /** Container used to reference variable names that need to be consistent
     *  amongst the front-end and back end. **/
    public static final FBVarNameRegistry VARNAME = _varNameRegSharedRef;
    //VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV
    
   
    //Rest Service URLS:
    //RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
    /** Shared reference so that we can have shorthand and longhand versions
     *  to be able to put into the code. **/
   // private static final ServiceURLRegistry _restUrlsSharedRef = 
   //                                                    new ServiceURLRegistry();
    /** Shorthand version of REST_SERVICE_URLS **/
    //public static final ServiceURLRegistry R = _restUrlsSharedRef;
    /** Container used to reference fully-qualified API endpoint URLS **/
    //public static final ServiceURLRegistry REST_SERVICE_URL =_restUrlsSharedRef;
    //RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
   
    
    /** Reference this variable in .JSP file to include all of the standard
     *  CSS files used in this application. **/
    public static String INCLUDE_CSS = 
    "BACK_END_PROBLEM(NOT UI PEOPLE'S FAULT)::INCLUDE_CSS_FAILED_TO_INITIALIZE";
    
    /** Reference this variable in .JSP file to include all of the standard
     *  JS files used in this application. **/
    public static String INCLUDE_JS = 
     "BACK_END_PROBLEM(NOT UI PEOPLE'S FAULT)::INCLUDE_JS_FAILED_TO_INITIALIZE";
    
    //Turns out, I can't ask for resources from WEB-INF here. Only the
    //servlets can. So making a config servlet to cache the values.
    /*
    
     *  Used to include CSS libs in header of JSP file.
     *  Usage: <%= I.INCLUDE_CSS %> 
     *  Where "I" is the name of this class we are in. 
     
     *  DESIGN NOTE:
     *  INCLUDE_CSS must be a function call rather than a cached property
     *  because of initialization orders involved in fetching resource.
     

    public static final String INCLUDE_CSS(){
        return LibraryInjection.getLibTagsCSS();
    }
    
     *  Used to include CSS libs in header of JSP file.
     *  Usage: <%= I.INCLUDE_CSS %> 
     *  Where "I" is the name of this class we are in. 
     
     DESIGN NOTE:
     *  INCLUDE_CSS must be a function call rather than a cached property
     *  because of initialization orders involved in fetching resource.
   
    public static final String INCLUDE_JS(){
        return LibraryInjection.getLibTagsJS();
    }
    
    */
            
}//CLASS::END
