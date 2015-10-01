package test.transactions.util.forOwnedMainlyByOneTable.session;
//345678901234567890123456789012345678901234567890123456789012345678901234567890

import primitives.StringWithComment;
import test.MyError;
import test.config.constants.EntityErrorCodes;
import test.dbDataAbstractions.entities.containers.BaseEntityContainer;
import test.dbDataAbstractions.entities.tables.AdminTable;
import test.dbDataAbstractions.entities.tables.SessionTable;
import test.dbDataAbstractions.entities.tables.TokenTable;
import test.transactions.util.TransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.admin.AdminTransUtil;
import test.transactions.util.forNoClearTableOwner.AdminTokenTransUtil;

/**##########################CLASS HEADER FILE##################################
//WHAT THIS CLASS DOES:
//Transaction utility used to handle transactions primarily
//involving the session table.
//
//ORIGINAL USE CASE:
//AdminRestService.LoginAndGetTokenForSelf(userName, passWord)
//
//DESIGN NOTE (Justifications for why things are the way they are):
//No validation: Access to this utility assumes you already have admin
//               credentials. Reasoning? I don't want to have admin credentials
//               added to the signatures of all methods in here when
//               this class is not even exposed to the outside world.
//               Telescoping parameters == anti-pattern.
//
//
//
//@author JMadison : 20XX.XX.XX_####AMPM
//@author XXXXXXXX : 20XX.XX.XX_####AMPM
//@author XXXXXXXX : 20XX.XX.XX_####AMPM 
//@author XXXXXXXX : 20XX.XX.XX_####AMPM
########10########20########30########40########50########60########70########*/
//-------0---------0---------0---------0---------0---------0---------0---------0
public class SessionTransUtil {
    
    /**-------------------------------------------------------------------------
     * Makes sure there is a token in the session table owned by the
     * admin specified by userName. 
     * 
     * CASE 1:
     * If token already exists, the session
     * entry is [re-opened / reset / refreshed]
     * 
     * CASE 2:
     * If token does not exist for this user, a fresh token is made in
     * the token table. A token associated with an ADMIN should not be
     * associated with a [Trial/Test]
     * 
     * @param userName :The user name of admin in our system.
     *                  We may want to validate this name exists
     *                  for error checking.
     * @return : A token that grants the admin access.
     *           Use angularJS to store this token in browser's cookies.
     ------------------------------------------------------------------------**/
    public static StringWithComment getActiveTokenForAdmin(String userName){
        
        //check to see if we are in a transaction state:
        TransUtil.insideTransactionCheck();
        
        //create output string:
        StringWithComment op = new StringWithComment();
        op.comment = "initialized by getActiveTokenForAdmin";
        
        //Check to see if userName exists in admin table:
        BaseEntityContainer bec = AdminTransUtil.getAdminEntity(userName);
        BaseEntityContainer.validate(bec); //<<--error check flags.
        if(false == bec.exists){
            op.comment = "getActiveTokenForAdmin::false==bec.exists";
            op.isError = true;
            op.value   = "error: userName not found in admin_table";
            op.errorCode = EntityErrorCodes.ADMIN_404;
            return op;
        }///////////////////////

        //Using the admin entity, 
        //Associate that admin with a token via the
        //owner_table
        //Then put token into session_table.
        //And make sure token associated with admin is:
        //1: Not associated with any other admin.
        //2: Not associated with a [Trial/Test] (AKA: A ninja)
        AdminTable theAdmin;
        TokenTable theToken;
        theAdmin = (AdminTable)bec.entity;
        theToken = AdminTokenTransUtil.linkAdminToNewToken(theAdmin);
        
        //Return the string representing admin's token:
        op.value = theToken.getToken_hash(); //Return the token HASH value.
        op.isError = false;
        op.comment = "success in getActiveTokenForAdmin";
        
        //Guard against lazy fetching:
        if(null == op.value || op.value.equals("")){ //EEEEEEEEEEEEEEEEEEEEEEEEE
            doError("[token hash lazy fetch problem most likely]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        return op;
        
    }//FUNC::END
    
    /** Make a new session using token. Expects that token does NOT
     *  exist in the session table already. If token already exists in
     *  the session table, will throw error.
     * @param token_id :The token id to put into the session table.
     */
    public static void makeSessionUsingToken(long token_id){
        //check to see if we are in a transaction state:
        TransUtil.insideTransactionCheck();
        
        //see if token already exists:
        boolean alreadyHere = isTokenInSessionTable(token_id);
        if(alreadyHere){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            String msg = "";
            msg+="Token already exists in the session table.";
            msg+="Cannot insert again.";
            doError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //Makes a blank session object that will need 
        //to be populated and saved.
        SessionTable st = makeNextSession();
        
        //associate this session with the token ID and make
        //sure the session is fresh:
        st.setComment("touched by makeSessionUsingToken");
        st.setDuration( hours_to_milliseconds(24));
        st.setIs_active(true);
        st.setOpened_on( System.currentTimeMillis() );
        st.setToken_id(token_id);
        
        //no need to return anything, we log what needs to be saved
        //with the transaction utility.
        TransUtil.markEntityForSaveOnExit(st);
        
    }//FUNC::END
    
    /**
     * Similar to makeNextToken in the token transaction utility.
     * Makes a new entry in the session table. It is up to you to
     * populate it with data.
     * @return :A new session table entity.
     */
    public static SessionTable makeNextSession(){
        
        //Error check: Make sure inside transaction state:
        TransUtil.insideTransactionCheck();
        
        //Create the session object:
        SessionTable st = new SessionTable();
        st.setComment("touched by makeNextSession");
        
        //Mark it for saving upon exiting:
        TransUtil.markEntityForSaveOnExit(st);
        
        return st;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * @param token_id :The ID of the token we wish to check for.
     * @return :Returns TRUE if the token_id is present in the table.
     * It does not matter the state of the token. It could have
     * all null fields, be active, or in-active. Does not matter.
     ------------------------------------------------------------------------**/
    public static boolean isTokenInSessionTable(long token_id){
        BaseEntityContainer bec;
        bec = TransUtil.getEntityFromTableUsingLong
                   (SessionTable.class, SessionTable.TOKEN_ID_COLUMN, token_id);
        
        return (bec.exists);
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = SessionTransUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw new MyError(clazz, err);
    }//FUNC::END
    
    private static long hours_to_milliseconds(long hours){
        return (hours * 3600000);
    }//FUNC::END
    
    private static long milliseconds_to_hours(long ms){
        return (long)(ms / 3600000);
    }//FUNC::END

}//CLASS::END
