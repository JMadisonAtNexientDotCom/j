package test.servlets.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import test.config.constants.ServletClassNames;
import test.transactions.util.TransUtil;
import test.transactions.util.admin.AdminTransUtil;
import utils.JSONUtil;

/**-----------------------------------------------------------------------------
 * Servlet that handles requests involving the admin_table.
 * Could think of this servlet as a wrapper for AdminTransUtil.java.
 * @author jmadison :2015.09.20_0625PM
 ----------------------------------------------------------------------------**/
@Path(ServletClassNames.AdminRestService_MAPPING)
public class AdminRestService extends BaseRestService{
    
    /**-------------------------------------------------------------------------
     * Used to test login credentials. 
     * Original Usage:
     *     Used to for developing. Need to make sure I can validate admin
     *     credentials before I write code that uses admin credentials.
     * 
     * Future Usage possibility:
     *     Could be used if the UI people need to validate username + password
     *     before using it in a transaction for some reason. Like a psuedo
     *     login? That only exists on the front-end.
     * 
     * @param userName :Case-insensitive user name.
     * @param passWord :Case-sensitive password.
     * @return :Returns TRUE if accepted credentials.
     ------------------------------------------------------------------------**/
    @GET
    @Path("loginValidate")
    public Response loginValidate(
            @QueryParam("userName") String userName, 
            @QueryParam("passWord") String passWord){
        
        //Enter transaction:
        Session ses = TransUtil.enterTransaction();
        
        //Use utility to validate:
        boolean tf = AdminTransUtil.loginValidate(userName, passWord);
        
        //Exit transaction:
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
        //Return response:
        return JSONUtil.booleanToJSONResponse(tf,"made by loginValidate");
        
    }//FUNC::END
    
    
}//CLASS::END
