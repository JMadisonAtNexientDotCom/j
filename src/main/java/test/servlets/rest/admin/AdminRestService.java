package test.servlets.rest.admin;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import primitives.StringWithComment;
import test.config.constants.ServletClassNames;
import test.servlets.rest.BaseRestService;
import test.servlets.rest.MasterServiceDoc;
import test.servlets.rest.admin.docs.LoginValidateDoc;
import test.transactions.util.TransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.admin.AdminTransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.session.SessionTransUtil;
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
    @Path(LoginValidateDoc.PATH)
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
        String comment = "made by loginValidate";
        return JSONUtil.booleanToJSONResponse(tf,comment, JSONUtil.ALL_IS_WELL);
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Admin logs in and grants themselves a session token.
     * Admin does not control what session token they get. 
     * However, the token from the token_table should NOT be associated
     * with any [Trials/Tests]
     * @param userName :Admin's user name. NOT case sensitive.
     * @param passWord :Case sensitive password.
     * @return :Token from the token_table that will now grant admin access
     *          because the token is also registered in the session_table.
     ------------------------------------------------------------------------**/
    @GET
    @Path("loginAndGetTokenForSelf")
    public Response loginAndGetTokenForSelf(String userName, String passWord){
        
        //Enter transaction:
        Session ses = TransUtil.enterTransaction();
        
        StringWithComment adminToken;
        boolean isValidLogin = AdminTransUtil.loginValidate(userName, passWord);
        if(isValidLogin){
            adminToken = SessionTransUtil.getActiveTokenForAdmin(userName);
        }else{
            adminToken = new StringWithComment();
            adminToken.value = "ERROR:ACCESS_DENIED";
            adminToken.isError = true;
        }//Valid?
        adminToken.comment = "last touched by loginAndGetTokenForSelf";
        
        //Exit transaction:
        //We exit with saving, because getting admin token involves
        //either creating a new entry in session_table, or re-activating
        //a pre-existing entry in the session_table.
        //TransUtil.exitTransaction(ses, TransUtil.EXIT_WITH_SAVE);
        
        //Temporary, until all the logic is in place and entities
        //Are actually registered for saving.
        TransUtil.exitTransaction(ses, TransUtil.EXIT_WITH_SAVE);
        
        Response op;
        op = JSONUtil.typeWithCommentToJSONResponse(adminToken);
        
        return op;
    }//FUNC::END
    
    
}//CLASS::END
