package test.servlets.rest.debug;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import test.config.constants.ServletClassNames;
import test.servlets.rest.BaseRestService;
import test.transactions.util.TransUtil;
import test.transactions.util.debugUtils.TransUtil_Debugger;
import utils.JSONUtil;

/** ----------------------------------------------------------------------------
 * Debug servlet for our TransUtil.java.
 * Original use:
 * Concurrency testing. Specifically, figuring out if multiple calls to
 * a service using a static utility will be able to increment a static
 * counter variable on static utility, or if the variable will be reset
 * back to zero with each new request.
 * 
 * @author jmadison ----------------------------------------------------------*/
@Path(ServletClassNames.TransDebugRestService_MAPPING)
public class TransDebugRestService extends BaseRestService{
    
    @GET
    @Path("incrementSharedCounter")
    public Response incrementSharedCounter(){
        
        //Enter transaction state:
        Session ses = TransUtil.enterTransaction();
        
        //Logic:
        TransUtil_Debugger.incrementSharedCounter();
        int op = TransUtil_Debugger.getSharedCounterValue();
        
        //Exit transaction state:
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
        return JSONUtil.numberToJSONResponse
                            (op,"TransDebugRestService.incrementSharedCounter");
        
    }//FUNC::END
    
    @Path("getSharedCounterValue")
    public Response getSharedCounterValue(){
        
        //Enter transaction state:
        Session ses = TransUtil.enterTransaction();
        
        //Logic:
        int op = TransUtil_Debugger.getSharedCounterValue();
        
        //Exit transaction state:
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
        return JSONUtil.numberToJSONResponse
                            (op,"TransDebugRestService.getSharedCounterValue");
        
    }//FUNC::END
    
    @GET
    @Path("incrementHordedCounter")
    public Response incrementHordedCounter(){
        
        //Enter transaction state:
        Session ses = TransUtil.enterTransaction();
        
        //Logic:
        TransUtil_Debugger.incrementHordedCounter();
        int op = TransUtil_Debugger.getHordedCounterValue();
        
        //Exit transaction state:
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
        return JSONUtil.numberToJSONResponse
                            (op,"TransDebugRestService.incrementHordedCounter");
        
    }//FUNC::END
    
    @Path("getHordedCounterValue")
    public Response getHordedCounterValue(){
        
        //Enter transaction state:
        Session ses = TransUtil.enterTransaction();
        
        //Logic:
        int op = TransUtil_Debugger.getHordedCounterValue();
        
        //Exit transaction state:
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
        return JSONUtil.numberToJSONResponse
                            (op,"TransDebugRestService.getHordedCounterValue");
        
    }//FUNC::END
    
    
}//CLASS::END
