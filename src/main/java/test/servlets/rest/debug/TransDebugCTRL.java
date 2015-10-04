package test.servlets.rest.debug;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import test.config.constants.ServletClassNames;
import test.servlets.rest.BaseCTRL;
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
@Path(ServletClassNames.TransDebugCTRL_MAPPING)
public class TransDebugCTRL extends BaseCTRL{
    
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
        
        String comment = "TransDebugRestService.incrementSharedCounter";
        return JSONUtil.numberToJSONResponse(op, comment, JSONUtil.ALL_IS_WELL);
        
    }//FUNC::END
    
    @GET
    @Path("getSharedCounterValue")
    public Response getSharedCounterValue(){
        
        //Enter transaction state:
        Session ses = TransUtil.enterTransaction();
        
        //Logic:
        int op = TransUtil_Debugger.getSharedCounterValue();
        
        //Exit transaction state:
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
        String comment = "TransDebugRestService.getSharedCounterValue";
        return JSONUtil.numberToJSONResponse(op, comment, JSONUtil.ALL_IS_WELL);
        
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
        
        String comment = "TransDebugRestService.incrementHordedCounter";
        return JSONUtil.numberToJSONResponse(op, comment, JSONUtil.ALL_IS_WELL);
        
    }//FUNC::END
    
    @GET
    @Path("getHordedCounterValue")
    public Response getHordedCounterValue(){
        
        //Enter transaction state:
        Session ses = TransUtil.enterTransaction();
        
        //Logic:
        int op = TransUtil_Debugger.getHordedCounterValue();
        
        //Exit transaction state:
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
        String comment = "TransDebugRestService.getHordedCounterValue";
        return JSONUtil.numberToJSONResponse(op,comment, JSONUtil.ALL_IS_WELL);
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Testing the ability to assign the Static Utils core a different id
     * for each thread it exists on.
     * @return :The ID of the utility core executing on the request thread.
     ------------------------------------------------------------------------**/
    @GET
    @Path("getUtilityInstanceID")
    public Response getUtilityInstanceID(){
        
        //Enter transaction state:
        Session ses = TransUtil.enterTransaction();
        
        //Logic:
        int op = TransUtil_Debugger.getUtilityInstanceID();
        
        //Exit transaction state:
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
        String comment = "TransDebugRestService.getUtilityInstanceID";
        return JSONUtil.numberToJSONResponse(op,comment,JSONUtil.ALL_IS_WELL);
        
    }//FUNC::END
}//CLASS::END
