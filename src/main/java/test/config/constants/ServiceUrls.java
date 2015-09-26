/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.config.constants;

import java.util.HashMap;
import java.util.Map;
import test.MyError;

/**
 * NOTE: If debug code fails, you might get a "noclassdeffounderror"
 *       rather than the actual error that we are throwing. No clue why.
 * @author jmadison
 */
public class ServiceUrls {
    
    /** Used to prevent re-initialization of this static class. **/
    private static boolean _hasBeenInitedBefore = false;
    
    /** wish this was not hardcoded, but at least it will be in only ONE place
     *  and will make service call URLS easier to refactor. **/
    public static final String APP_ROOT_DOMAIN = 
                                   "https://j1clone01-madnamespace.rhcloud.com";
    
    public static final String API = "api";
    
    //API ENDPOINTS:
    //////////////////////////////////
    public static String ADMIN        = "NOT_INITED;";
    public static String OWNER        = "NOT_INITED";      
    public static String TOKEN        = "NOT_INITED"; ;
    public static String FILE         = "NOT_INITED"; ;
    public static String NINJA        = "NOT_INITED"; ;
    public static String RIDDLERHYME  = "NOT_INITED"; ;
    public static String TRANSDEBUG   = "NOT_INITED"; ;
    //////////////////////////////////                  
    
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
        
        //NOTE: I had an error in here, but it was SUPER HARD TO FIND!
        //Holy crap! There are more errors in here. TOKEN is using CLASSNAME rather
        //than mapping... Need to make it so errors print out easier.
        //
        //Right now errors are just... not bubbling up when being thrown
        //Inside initialization. Figure out how to fix that.
        //Might have to throw you own noclassdeffounderror !!
        //
         //Create fully-qualified api endpoints:
        ADMIN       = makeURL(ServletClassNames.OwnerRestService_MAPPING);
        OWNER       = makeURL(ServletClassNames.AdminRestService_MAPPING);
        TOKEN       = makeURL(ServletClassNames.TokenRestService_CLASSNAME);
        FILE        = makeURL(ServletClassNames.FileContentFetcher_MAPPING);
        NINJA       = makeURL(ServletClassNames.NinjaRestService_MAPPING);
        RIDDLERHYME = makeURL(ServletClassNames.RiddleRhymeRestService_MAPPING);
        TRANSDEBUG  = makeURL(ServletClassNames.TransDebugRestService_MAPPING);
        
        /*
        //Compiler warns me about using API_COUNT, but this
        //Should be fine! Getting a bit urked by how java likes false positives.
        if(API_COUNT != ServletClassNames.getNumberOfMappings()){//EEEEEEEEEEEEE
            
            setAllVarsToErrorMessage();
            
            
            //String msg = "[Mapping counts do not match]";
            //msg += "[AKA: Not all API endpoints exposed to this class]";
            //msg += "this class == " + ServiceUrls.class.getCanonicalName();
            //msg += "[Did you forget to include one of the mappings found]";
            //msg += "in:" + ServletClassNames.class.getCanonicalName() + "?";
            //msg += "[OTHER POSSIBLE PROBLEM:]";
            //msg += ServletClassNames.class.getSimpleName();
            //msg += "[Only knows about the mappings that have been bug checked.]";
            //msg += "[Which should be ALL OF THEM. But if it isn't, that]";
            //msg += "[Would be another reason the checksums do not agree.]";
            //doError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        */
        
    }//FUNC::END
    
    /**
     * Builds fully qualified rest service url endpoint.
     * @param mappingEndPoint :The last part of the url, including the slash.
     *                         Example: "tokenRestService/"
     * @return :A url that can be used in an HTTP-GET, HTTP-POST or whatever.
     */
    private static String makeURL(String mappingEndPoint){
        
        //THIS BLOCK: NOT cause of init error.
        int len = mappingEndPoint.length();
        if(mappingEndPoint.charAt(len-1) != '/'){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            //doError("API mappinEndPoint supplied must end with fwd-slash");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
            
        String url = APP_ROOT_DOMAIN + "/" + API + "/" + mappingEndPoint;
        
        
        //Put url into our map. Make sure no collisions. Also talley
        //How many there are.
        API_COUNT++;
        
        if(null == API_MAP){
            
            //doError("API_MAP was not initialized before makeURL call");
            setAllVarsToErrorMessage();
        }
        
        /*
        //Likely NOT the offending code. Did not un-crash program.
        if(API_MAP.containsKey(url)){
            doError("API_MAP already contains endpoint key! Bad setup!");
        }//ERROR!
        */
        
        /*
        
        //Will this uncrash program?
        API_MAP.put(url, 1);
        */
        
        return url;
    }//FUNC::END
    
    private static void setAllVarsToErrorMessage(){
        String cname = ServiceUrls.class.getCanonicalName();
        OWNER       = "INITIALIZATION_ERROR IN:" + cname;
        TOKEN       = "INITIALIZATION_ERROR IN:" + cname;
        FILE        = "INITIALIZATION_ERROR IN:" + cname;
        NINJA       = "INITIALIZATION_ERROR IN:" + cname;
        RIDDLERHYME = "INITIALIZATION_ERROR IN:" + cname;
        TRANSDEBUG  = "INITIALIZATION_ERROR IN:" + cname;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    * Wrapper function to throw errors from this class.
    * @param msg :Specific error message.
    ------------------------------------------------------------------------**/
   private static void doError(String msg){
       String err = "ERROR INSIDE:";
       err += ServiceUrls.class.getSimpleName();
       err += msg;
       throw new MyError(err);
   }//FUNC::END
    
}//CLASS::END
