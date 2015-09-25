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
 *
 * @author jmadison
 */
public class ServiceUrls {
    
    /** wish this was not hardcoded, but at least it will be in only ONE place
     *  and will make service call URLS easier to refactor. **/
    public static final String APP_ROOT_DOMAIN = 
                                   "https://j1clone01-madnamespace.rhcloud.com";
    
    public static final String API = "api";
    
    //API ENDPOINTS:
    //////////////////////////////////
    public static String OWNER;      
    public static String TOKEN;
    public static String FILE;
    public static String NINJA;
    public static String RIDDLERHYME;
    public static String TRANSDEBUG;
    //////////////////////////////////                  
    
    /** map of all API endpoints. Needs to have same length as map
     *  of ServletClassnames, else there is an integrity issue.
     *  Most likely would mean that not all ServiceUrls have been wired up.
     **/
    private static final Map<String,Integer> API_MAP;
                                                  
    private static int API_COUNT;
    
    /**
     * Need to do static init, because there is some checking we want
     * to do after setup.
     */
    static{//SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
        //Create fully-qualified api endpoints:
        OWNER       = makeURL(ServletClassNames.AdminRestService_MAPPING);
        TOKEN       = makeURL(ServletClassNames.OwnerRestService_MAPPING);
        FILE        = makeURL(ServletClassNames.FileContentFetcher_MAPPING);
        NINJA       = makeURL(ServletClassNames.NinjaRestService_MAPPING);
        RIDDLERHYME = makeURL(ServletClassNames.RiddleRhymeRestService_MAPPING);
        TRANSDEBUG  = makeURL(ServletClassNames.TransDebugRestService_MAPPING);
        
        //Create map that will be used for checksums:
        API_COUNT = 0;
        API_MAP     = new HashMap<String,Integer>();
        
        //Compiler warns me about using API_COUNT, but this
        //Should be fine! Getting a bit urked by how java likes false positives.
        if(API_COUNT != ServletClassNames.getNumberOfMappings()){//EEEEEEEEEEEEE
            String msg = "[Mapping counts do not match]";
            msg += "[AKA: Not all API endpoints exposed to this class]";
            msg += "this class == " + ServiceUrls.class.getCanonicalName();
            msg += "[Did you forget to include one of the mappings found]";
            msg += "in:" + ServletClassNames.class.getCanonicalName() + "?";
            msg += "[OTHER POSSIBLE PROBLEM:]";
            msg += ServletClassNames.class.getSimpleName();
            msg += "[Only knows about the mappings that have been bug checked.]";
            msg += "[Which should be ALL OF THEM. But if it isn't, that]";
            msg += "[Would be another reason the checksums do not agree.]";
            doError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
    }//SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
    
    
    /**
     * Builds fully qualified rest service url endpoint.
     * @param mappingEndPoint :The last part of the url, including the slash.
     *                         Example: "tokenRestService/"
     * @return :A url that can be used in an HTTP-GET, HTTP-POST or whatever.
     */
    private static String makeURL(String mappingEndPoint){
        
        int len = mappingEndPoint.length();
        if(mappingEndPoint.charAt(len-1) != '/'){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            doError("API mappinEndPoint supplied must end with fwd-slash");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            
        String url = APP_ROOT_DOMAIN + "/" + API + "/" + mappingEndPoint;
        
        //Put url into our map. Make sure no collisions. Also talley
        //How many there are.
        API_COUNT++;
        if(API_MAP.containsKey(url)){
            doError("API_MAP already contains endpoint key! Bad setup!");
        }//ERROR!
        API_MAP.put(url, 1);
        
        return url;
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
