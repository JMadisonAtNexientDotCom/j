package app.config.constants;

import java.util.HashMap;
import java.util.Map;
import app.MyError;
import frontEndBackEndIntegration.childComponents.ServiceURLRegistry;
import app.debug.GlobalErrorState;

/**
 * NOTE: If debug code fails, you might get a "noclassdeffounderror"
 *       rather than the actual error that we are throwing. No clue why.
 * @author jmadison
 */
public class ServiceUrlsInitializer {
    
    /** Used to make sure only THIS class can initialize
     *  an instance of ServiceURLRegistry.java. **/
    public static final String initPassword = "ThePassword";
    
    /** THIS SHOULD BE THE ONLY PUBLICALLY EXPOSED METHOD OF THIS CLASS !! -----
     *  By routing everything from here, we have an easy way to catch 
     *  errors that happened during initialization.
     * @return :The registry object with all of the API endpoint vars.
     ------------------------------------------------------------------------**/
    public static ServiceURLRegistry getServiceURLRegistry(){ //UUUUUUUUUUUUUUUU
        
        if(false == _hasBeenInitedBefore){//EEEEEEEEE
            doError("Class was never initialized!");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        if(_errorOccurredDuringStaticInitialization){
            String msg = "Error happened during init!";
            msg += "[ERROR_LOG:]";
            msg += _initErrorLog;
            msg += "[ERROR_LOG_END]";
            doError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        return _serviceEndPointsContainer;
    }//FUNC::END //UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU
    
    /*
    ## To allow errors to bubble-up in our stack, we will have the class that--
     *  DEPENDS on this class to be able to call this function during 
     *  it's initialization.
     * @return :Returns TRUE if error happens in this class during init.
     ------------------------------------------------------------------------##
    public static boolean getDidErrorsOccurDuringInit(){////////////////////////
        if(false == _hasBeenInitedBefore){
            doStaticInit();
        }/////////////////////////////////
        
        return _errorOccurredDuringStaticInitialization;
    }//FUNC::END////////////////////////////////////////////////////////////////
    
    ## Return the initialization errors that happened in this class. ##
    public static String getInitErrors(){
        return _initErrorLog;
    }////////////////////////////////////
    */
    
    /** Used to prevent re-initialization of this static class. **/
    private static boolean _hasBeenInitedBefore = false;
    
    /** wish this was not hardcoded, but at least it will be in only ONE place
     *  and will make service call URLS easier to refactor. **/
    public static final String APP_ROOT_DOMAIN = 
                                   "https://j1clone01-madnamespace.rhcloud.com";
    
    public static final String API = "api";
    
    //API ENDPOINTS:
    ////////////////////////////////////////////////////////////////////////////
    /** MUST BE PRIVATE. The get call for this does error checking to make sure
     *  the initialization worked. We don't throw error DURING init because
     *  the stack trace for that doesn't give us any useful information. **/
    private static ServiceURLRegistry _serviceEndPointsContainer;
                                                       
    
    /**Shorthand version of _serviceEnPointsContainer **/
    private static ServiceURLRegistry U;
    
    private static boolean _errorOccurredDuringStaticInitialization = false;
    
    /** Log of any errors that happened during initialization. Don't listen to
     *  IDE. This variable IS used! Contrary to what warning says. -----------*/
    private static String  _initErrorLog = 
      "[NO_INIT_ERRORS_IN::]" + ServiceUrlsInitializer.class.getCanonicalName();
    
    
    
    ////////////////////////////////////////////////////////////////////////////     
    
    /** map of all API endpoints. Needs to have same length as map
     *  of ServletClassnames, else there is an integrity issue.
     *  Most likely would mean that not all ServiceUrls have been wired up.
     **/
    private static Map<String,Integer> API_MAP = null;
                             
    /** Why does IDE think API_COUNT is NOT used?? **/
    private static int API_COUNT = 0;
    
    /**
     * Need to do static init, because there is some checking we want
     * to do after setup.
     */
    static{//SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
        
        //Java seems to HATE your initializer code:
        //http://stackoverflow.com/questions/1401111/
        //                 noclassdeffounderror-could-not-initialize-class-error
        doStaticInit();
      
    }//SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
    
    
    private static void doStaticInit(){
        
        if(_hasBeenInitedBefore){ return; }
        _hasBeenInitedBefore = true;
        
        //API COUNT BELONGS ABOVE makeURL calls. If you don't,
        //You will get an ellusive: noclassdeffounderror
        API_COUNT = 0;
        
        //Create map that will be used for checksums:
        API_MAP     = new HashMap<String,Integer>();
        
        _serviceEndPointsContainer = new ServiceURLRegistry(initPassword);
        /** Shorthand variable for setting up mapping. **/
        U = _serviceEndPointsContainer;
        //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        if(null == U || null == API_MAP || API_COUNT != 0 || 
                                          null == _serviceEndPointsContainer){
            makeInitErrorLog("a value was null during beginning of init.");
            return;
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //NOTE: I had an error in here, but it was SUPER HARD TO FIND!
        //Holy crap! There are more errors in here. TOKEN is using CLASSNAME rather
        //than mapping... Need to make it so errors print out easier.
        //
        //Right now errors are just... not bubbling up when being thrown
        //Inside initialization. Figure out how to fix that.
        //Might have to throw you own noclassdeffounderror !!
        //
        
        //Create fully-qualified api endpoints: //UUUUUUUUUUUUUUUUUUUUUUUUUUUUUU
        U.KINDA  = mkURL(ServletClassNames.KindaCTRL_MAPPING);
        
        U.QUAR   = mkURL(ServletClassNames.QuarCTRL_MAPPING);
        U.SLATE  = mkURL(ServletClassNames.SlateCTRL_MAPPING);
        U.CHALK  = mkURL(ServletClassNames.ChalkCTRL_MAPPING);
        
        U.DECK   = mkURL(ServletClassNames.DeckCTRL_MAPPING);
        U.CUECARD= mkURL(ServletClassNames.CuecardCTRL_MAPPING);
        U.INK    = mkURL(ServletClassNames.InkCTRL_MAPPING);
        
        U.GROUP  = mkURL(ServletClassNames.GroupCTRL_MAPPING);
        U.TRIAL  = mkURL(ServletClassNames.TrialCTRL_MAPPING);
        U.ADMIN  = mkURL(ServletClassNames.AdminCTRL_MAPPING);
        U.OWNER  = mkURL(ServletClassNames.OwnerCTRL_MAPPING);
        U.TOKEN  = mkURL(ServletClassNames.TokenCTRL_MAPPING);
        U.FILE   = mkURL(ServletClassNames.FileContentFetcher_MAPPING);
        U.NINJA  = mkURL(ServletClassNames.NinjaCTRL_MAPPING);
        
        String RIDDLERHYME_MAP = ServletClassNames.RiddleRhymeCTRL_MAPPING;
        String TRANSDEBUG_MAP = ServletClassNames.TransDebugCTRL_MAPPING;
        
        U.RIDDLERHYME = mkURL(RIDDLERHYME_MAP);
        U.TRANSDEBUG  = mkURL(TRANSDEBUG_MAP);
        //UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU
        
        
        //Compiler warns me about using API_COUNT, but this
        //Should be fine! Getting a bit urked by how java likes false positives.
        //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        if(API_COUNT != ServletClassNames.getNumberOfMappings()){//EEEEEEEEEEEEE
            
            setAllVarsToErrorMessage("[Wrong # of Mappings]");
           
            String thisClass = ServiceUrlsInitializer.class.getCanonicalName();
            String msg = "[Mapping counts do not match]";
            msg += "[AKA: Not all API endpoints exposed to this class]";
            msg += "this class == " + thisClass;
            msg += "[Did you forget to include one of the mappings found]";
            msg += "in:" + ServletClassNames.class.getCanonicalName() + "?";
            msg += "[OTHER POSSIBLE PROBLEM:]";
            msg += ServletClassNames.class.getSimpleName();
            msg += "[Only knows about the mappings that have been bug checked.]";
            msg += "[Which should be ALL OF THEM. But if it isn't, that]";
            msg += "[Would be another reason the checksums do not agree.]";
            
            //Log error for now and crash when we ask for container, 
            //because will be easier to find error if we 
            //crash OUTSIDE of initialization.
            makeInitErrorLog(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //before we exit, make absolutely sure shorthand and long-hand
        //references are identical.
        if(false == (U.equals(_serviceEndPointsContainer)) ){///////////////////
            makeInitErrorLog("[ShortHand does not match longHand ref!]");
        }///////////////////////////////////////////////////////////////////////
        
    }//FUNC::END
    
    /**
     * Builds fully qualified rest service url endpoint.
     * @param mappingEndPoint :The last part of the url, including the slash.
     *                         Example: "tokenRestService/"
     * @return :A url that can be used in an HTTP-GET, HTTP-POST or whatever.
     */
    private static String mkURL(String mappingEndPoint){
        
        //THIS BLOCK: NOT cause of init error.
        int len = mappingEndPoint.length();
        if(mappingEndPoint.charAt(len-1) != '/'){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            setAllVarsToErrorMessage("[Did not end with '/']");
            makeInitErrorLog("[API mappinEndPoint must end with fwd-slash]");
            makeInitErrorLog("[Offending mapping end point:]");
            makeInitErrorLog("[" + mappingEndPoint + "]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
            
        String url = APP_ROOT_DOMAIN + "/" + API + "/" + mappingEndPoint;
        
        
        //Put url into our map. Make sure no collisions. Also talley
        //How many there are.
        API_COUNT++;
        
        if(null == API_MAP){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            setAllVarsToErrorMessage("[null==API_MAP]");
            makeInitErrorLog("[API_MAP was not inited before makeURL call]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        
        //Likely NOT the offending code. Did not un-crash program.
        if(API_MAP.containsKey(url)){
            setAllVarsToErrorMessage("[API_MAP_ALREADY_KEY]");
            makeInitErrorLog("[API_MAP already contains endpoint key!]");
        }//ERROR!
        
        //Add the path we just added so we can assert all paths
        //are UNIQUE!
        API_MAP.put(url, 1);
       
        return url;
    }//FUNC::END
    
    //Set error message into all servlet url paths so that it increases the
    //chance that you will see it somewhere, or that application will crash.
    //Yes, increase chances of things crashing when we are aware something
    //went wrong. Find the bugs quickly and fix them. Don't write try-catches
    //trying to circumvent errors. That just leads to running in an unstable
    //environment.
    private static void setAllVarsToErrorMessage(String extraMSG){
        String cname = ServiceUrlsInitializer.class.getCanonicalName();
        
        U.KINDA = "INITIALIZATION_ERROR IN:" + cname + extraMSG;
        
        //Servlets representing tables that persist a complete response
        //to a generated riddle-trial (test):
        U.QUAR   = "INITIALIZATION_ERROR IN:" + cname + extraMSG;
        U.SLATE  = "INITIALIZATION_ERROR IN:" + cname + extraMSG;
        U.CHALK  = "INITIALIZATION_ERROR IN:" + cname + extraMSG;
        
        //Servlets representing tables that persist a generated riddle-trial
        //test:
        U.DECK   = "INITIALIZATION_ERROR IN:" + cname + extraMSG;
        U.CUECARD= "INITIALIZATION_ERROR IN:" + cname + extraMSG;
        U.INK    = "INITIALIZATION_ERROR IN:" + cname + extraMSG;
        
        U.GROUP  = "INITIALIZATION_ERROR IN:" + cname + extraMSG;
        U.TRIAL  = "INITIALIZATION_ERROR IN:" + cname + extraMSG;
        U.ADMIN  = "INITIALIZATION_ERROR IN:" + cname + extraMSG;
        
        U.OWNER       = "INITIALIZATION_ERROR IN:" + cname + extraMSG;
        U.TOKEN       = "INITIALIZATION_ERROR IN:" + cname + extraMSG;
        U.FILE        = "INITIALIZATION_ERROR IN:" + cname + extraMSG;
        U.NINJA       = "INITIALIZATION_ERROR IN:" + cname + extraMSG;
        U.RIDDLERHYME = "INITIALIZATION_ERROR IN:" + cname + extraMSG;
        U.TRANSDEBUG  = "INITIALIZATION_ERROR IN:" + cname + extraMSG;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    * Wrapper function to throw errors from this class.
    * @param msg :Specific error message.
    ------------------------------------------------------------------------**/
   private static void doError(String msg){
       String err = "ERROR INSIDE:";
       Class  clazz = ServiceUrlsInitializer.class;
       String cName = clazz.getCanonicalName();
       err += cName;
       err += msg;
       //GlobalErrorState.addError(clazz, err);
       throw MyError.make(ServiceUrlsInitializer.class, err);
   }//FUNC::END
   
    /**------------------------------------------------------------------------- 
     *  Log errors that happen during init and crash AFTER init,
     *  because errors during initialization don't show up in the
     *  stack trace in an informative way.
     * 
     *  Example: I was NOT able to read my custom error message that
     *           I threw in this class when it was thrown during init.
     *           Which totally defeated the point of me making a
     *           custom error message.
     * @param msg :The message you want to log to help future programmer fix
     *             the problem.
     ------------------------------------------------------------------------**/
    private static void makeInitErrorLog(String msg){ //////////////////////////
        _errorOccurredDuringStaticInitialization = true;
        _initErrorLog += msg;
    }///////////////////////////////////////////////////////////////////////////
    
}//CLASS::END
