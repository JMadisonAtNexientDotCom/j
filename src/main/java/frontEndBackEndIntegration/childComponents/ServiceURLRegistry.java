/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontEndBackEndIntegration.childComponents;

import frontEndBackEndIntegration.childComponents.restDoc.RestCallDocument;
import test.MyError;
import test.config.constants.ServiceUrlsInitializer;

/**
 * An instantiatable class holding fully resolved API endpoints that can
 * be referenced in .JSP files to make HTTP-REST calls less prone to error.
 * 
 * This will assure that UI and Back-End people are using the same API
 * calls.
 * 
 * DESIGN NOTES:
 * //Rather than make an instanceable container that grabs from
    //{@value ServiceUrlsInitializer} , we should just have this instanceable
    //container OWNED by ServiceUrlsInitializer.
    //WHY?
    //1: We don't have to duplicate our error checking making sure things
    //   have switched hands correctly again. Case and point below:
    //    public final String OWNER       = ServiceUrlsInitializer.OWNER;   
    //    public final String ADMIN       = ServiceUrlsInitializer.ADMIN;
    //    public final String TOKEN       = ServiceUrlsInitializer.TOKEN;
    //    public final String FILE        = ServiceUrlsInitializer.FILE;
    //    public final String NINJA       = ServiceUrlsInitializer.NINJA;
    //    public final String RIDDLERHYME = ServiceUrlsInitializer.RIDDLERHYME;
    //    public final String TRANSDEBUG  = ServiceUrlsInitializer.TRANSDEBUG;
    //
    //I already did error checking to make sure these mappings were correct.
    //I shouldn't have to pass all the values again and do the checks again.
    //Example:
    //If you accidentially did this: 
    ////////////////////////////////////////////////////////////////////////////
    //public final String OWNER       = ServiceUrlsInitializer.ADMIN; 
    ////////////////////////////////////////////////////////////////////////////
    //Problem being: OWNER==ADMIN
    //
    //2: Throwing errors during initialization causes NoClassDefFoundError,
    //   Which is pretty much IMPOSSIBLE to find the root cause of in stack
    //   trace. The {@value ServiceUrlsInitializer} can init a single container,
    //   and then whenever asked for the container, does a check to see if
    //   it has been initialized without problems. If it had problems during
    //   init, then it throws an error! But the error is now thrown OUTSIDE
    //   of the static init and will show up clearly in the stack trace.
 * 
 * @author jmadison :2015.09.25_????AMPM
 * @author jmadison :2015.09.26_0129PM  --Making errors easy to find.
 */
public class ServiceURLRegistry {
    
    public ServiceURLRegistry(String initPassword){
        
        //Make sure only the registry initializer is allowed
        //to make an instance of this class.
        if( notEQ(initPassword,ServiceUrlsInitializer.initPassword) ){//EEEEEEEE
            String msg = "Init access error!";
            msg += "only:" + ServiceUrlsInitializer.class.getCanonicalName();
            msg += "is allowed to make an instance of this class.";
            doError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
    }//INIT::END
    
    //----SERVICE VARIABLE----------|--Path inited to "NOT_INITIALIZED" to ----|
    //----For HTTP REST CALLS-------|--Help hunt down mapping errors       ----|
    public RestCallDocument OWNER       = new RestCallDocument("OWNER");  
    public RestCallDocument ADMIN       = new RestCallDocument("ADMIN");  
    public RestCallDocument TOKEN       = new RestCallDocument("TOKEN"); 
    public RestCallDocument FILE        = new RestCallDocument("FILE"); 
    public RestCallDocument NINJA       = new RestCallDocument("NINJA"); 
    public RestCallDocument RIDDLERHYME = new RestCallDocument("RIDDLERHYME"); 
    public RestCallDocument TRANSDEBUG  = new RestCallDocument("TRANSDEBUG"); 
   
    /**-------------------------------------------------------------------------
    * Wrapper function to throw errors from this class.
    * @param msg :Specific error message.
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
    String err = "ERROR INSIDE:";
    err += ServiceURLRegistry.class.getSimpleName();
    err += msg;
    throw new MyError(err);
    }//FUNC::END
    
    private static boolean notEQ(String s01, String s02){
        boolean isEqual = s01.equals(s02);
        return (!isEqual);
    }//FUNC::END
    
}//CLASS::END
