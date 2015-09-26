package frontEndBackEndIntegration;

//No package. At root to make less hazardous for refactoring.
//Since it will be put into .JSP files.
import frontEndBackEndIntegration.childComponents.FBVarNameRegistry;
import frontEndBackEndIntegration.childComponents.ServiceURLRegistry;
import frontEndBackEndIntegration.childComponents.TextFileCacheRegistry;
import test.MyError;
import test.config.constants.ServiceUrlsInitializer;



/**
 * NOTE: IF INITIALIZATION OF THIS CLASS FAILS... The stack trace will not
 * be very helpful. I am not sure how to fix this and make the errors bubble up.
 * 
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
    
    //Variable name getters:
    //VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV
    /** Shared reference so that we can have shorthand and longhand versions
     *  to be able to put into the code. **/
    private static FBVarNameRegistry _varNameRegSharedRef;
    
    /** Short-handed accessor for GET_VARIABLE_NAMES **/
    public static FBVarNameRegistry V(){
        return getFBVarNameRegistry();
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * @return Gets container holding variable names used by rest services. 
     *  So you can use them in an HTTP-GET/POST/WHATEVER call.
     *  This is a master list of all variables used in all functions. It does
     *  NOT guarantee you are going to pass an invalid parameter to a call,
     *  but minimizes the risk that you will be using invalid param names.
     *  Example: Using this, we will avoid mistakes like using:
     *  "tokenID" vs "token_id" because we will have standardized what variable
     *  name we use for the token id variable.
     -------------------------------------------------------------------------*/
    public static FBVarNameRegistry GET_VARIABLE_NAMES(){
        return getFBVarNameRegistry();
    }//FUNC::END
    
    /** Common private accessor function **/
    private static FBVarNameRegistry getFBVarNameRegistry(){
        if(null == _varNameRegSharedRef){//EEEEEEEEEEEEEEEEEEE
            doError("[_varNameRegSharedRef is null!]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        return _varNameRegSharedRef;
    }//FUNC::END
    //VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV
    
    //Rest Service URLS:
    //RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR     
    /** Short-handed accessor for GET_REST_SERVICE_URLS **/
    public static ServiceURLRegistry R(){
        return getServiceURLRegistry();
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * @return Gets container holding the rest service urls so you can use them
     *  in an HTTP-GET/POST/WHATEVER call.
     -------------------------------------------------------------------------*/
    public static ServiceURLRegistry GET_REST_SERVICE_URLS(){
        return getServiceURLRegistry();
    }//FUNC::END
    
    private static ServiceURLRegistry getServiceURLRegistry(){
        return ServiceUrlsInitializer.getServiceURLRegistry();
    }//FUNC::END
    //RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
    
    /** Call this in your JSP document where you want standard CSS header files
     *  to be injected. By standard, I mean the ones agreed to be used in
     *  this project.
     * @return : The CSS tags needed for including our css libraries. */
    public static String INCLUDE_CSS(){
        return TextFileCacheRegistry.INCLUDE_CSS;
    }//FUNC::END
    
    /** Call this in your JSP document where you want standard JavaScript (JS)
     *  header files to be injected. By standard, I mean the ones agreed 
     *  to be used in this project.
     * @return : The JS tags needed for including our JS libraries. */
    public static String INCLUDE_JS(){
        return TextFileCacheRegistry.INCLUDE_JS;
    }//FUNC::END
    
    
    
   
    /**-------------------------------------------------------------------------
    * Wrapper function to throw errors from this class.
    * @param msg :Specific error message.
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
    String err = "ERROR INSIDE:";
    err += I.class.getSimpleName();
    err += msg;
    throw new MyError(err);
    }//FUNC::END
         
}//CLASS::END


//Though syntatically, I would prefer to use properies, they create 2 problems:
//1: Crashes during static init are hard to find.
//   better to crash on retrieval of propery via getter function.
//
//2: Properties can be overridden. Getter values less likely to be.
////////////////////////////////////////////////////////////////////////////////
/*
 private static boolean _hasBeenStaticallyInitialized = false;
    static{//////////////
        doStaticInit();
    }////////////////////
    
    private static void doStaticInit(){//IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
        if(_hasBeenStaticallyInitialized){return;}
        _hasBeenStaticallyInitialized = true;
        
        //VARIABLE NAMES CONTAINER:
        _varNameRegSharedRef = new FBVarNameRegistry();
        V                    =  _varNameRegSharedRef;
        VARNAME              = _varNameRegSharedRef;
        
        //REST URLS CONTAINER:
        if(ServiceUrlsInitializer.getDidErrorsOccurDuringInit()){//EEEEEEEEEEEEE
            String bubbleUpErrors = ServiceUrlsInitializer.getInitErrors();
            doError("BUBBLED-UP-ERRORS!::" + bubbleUpErrors);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        _restUrlsSharedRef = ServiceUrlsInitializer.getServiceURLRegistry();
        R                  = _restUrlsSharedRef;
        REST_SERVICE_URL   = _restUrlsSharedRef;
    }//INIT! IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
    */

    //Variable names used by services:
    //VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV
    /** Shared reference so that we can have shorthand and longhand versions
     *  to be able to put into the code. **/
    //private static FBVarNameRegistry _varNameRegSharedRef;
                                                        
    /** Shorthand version of VARNAME **/
    //public static FBVarNameRegistry V;
    /** Container used to reference variable names that need to be consistent
     *  amongst the front-end and back end. **/
    //public static FBVarNameRegistry VARNAME;
    //VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV
    
   
    //Rest Service URLS:
    //RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
    /** Shared reference so that we can have shorthand and longhand versions
     *  to be able to put into the code. **/
     //private static ServiceURLRegistry _restUrlsSharedRef;                                        
    /** Shorthand version of REST_SERVICE_URLS **/
      //public static ServiceURLRegistry R;
    /** Container used to reference fully-qualified API endpoint URLS **/
      //public static ServiceURLRegistry REST_SERVICE_URL;
    //RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
   
    
    /** Reference this variable in .JSP file to include all of the standard
     *  CSS files used in this application. **/
   // public static String INCLUDE_CSS = 
   // "BACK_END_PROBLEM(NOT UI PEOPLE'S FAULT)::INCLUDE_CSS_FAILED_TO_INITIALIZE";
    
    /** Reference this variable in .JSP file to include all of the standard
     *  JS files used in this application. **/
    //public static String INCLUDE_JS = 
    // "BACK_END_PROBLEM(NOT UI PEOPLE'S FAULT)::INCLUDE_JS_FAILED_TO_INITIALIZE";
    
    /**-------------------------------------------------------------------------
    * Wrapper function to throw errors from this class.
    * @param msg :Specific error message.
    -------------------------------------------------------------------------**/
/*
    private static void doError(String msg){
    String err = "ERROR INSIDE:";
    err += I.class.getSimpleName();
    err += msg;
    throw new MyError(err);
    }//FUNC::END
*/
////////////////////////////////////////////////////////////////////////////////





////////////////////////////////////////////////////////////////////////////////
//Notes / Messy area you don't have to pay attention to.


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