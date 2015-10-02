package test.transactions.util.forOwnedMainlyByOneTable.session;
//345678901234567890123456789012345678901234567890123456789012345678901234567890

import java.util.List;
import org.hibernate.Session;
import primitives.StringWithComment;
import test.MyError;
import test.config.constants.EntityErrorCodes;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import test.dbDataAbstractions.entities.containers.BaseEntityContainer;
import test.dbDataAbstractions.entities.tables.AdminTable;
import test.dbDataAbstractions.entities.tables.SessionTable;
import test.dbDataAbstractions.entities.tables.TokenTable;
import test.transactions.util.TransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.admin.AdminTransUtil;
import test.transactions.util.forNoClearTableOwner.AdminTokenTransUtil;
import test.transactions.util.forNoClearTableOwner.OwnerTokenTransUtil;

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
    
    /**
     * Wrapper for killSessionsOfAdmin,
     * Assumes: Sessions MIGHT exists.
     * Assumes: Admin user name supplied is valid.
     * @param adminsUserName  :NOT CASE SENSITIVE.
     * @return :The number of sessions that were killed.
     */
    public static int killPossiblyExistingSessionsOfExistingAdmin
                                                        (String adminsUserName){
        int kills;
        boolean ERROR_IF_ADMIN_DOES_NOT_EXIST = true;
        boolean ERROR_IF_NO_SESSIONS_FOUND    = false;
        kills = killSessionsOfAdmin(adminsUserName,
                                                  ERROR_IF_ADMIN_DOES_NOT_EXIST,
                                                  ERROR_IF_NO_SESSIONS_FOUND);
        return kills;
    }//FUNC::END

    /**
     * Kills all sessions of admin by removing them from the session table.
     * @param userName :The username. NOT case sensitive.
     * @param errorIfAdminNotFoundInAdminTable :Should we error if the username
     *                                          does NOT exist?
     * @param errorIfNoActiveSessionsFound :Should we error if no sessions were
     *                                      found for a user of that username?
     * @return :The number of sessions that were killed.
     */
    public static int killSessionsOfAdmin
                  (String userName, boolean errorIfAdminNotFoundInAdminTable, 
                                    boolean errorIfNoActiveSessionsFound){
        TransUtil.insideTransactionCheck();
        
        String lowerUser = userName.toLowerCase();
        BaseEntityContainer bec;
        bec = TransUtil.getEntityFromTableUsingUniqueString
                     (AdminTable.class, AdminTable.USER_NAME_COLUMN, lowerUser);
        
        if(false == bec.exists)
        {
            if(errorIfAdminNotFoundInAdminTable){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEE
                String msg = "";
                msg += "[Admin did not exist in admin table, and therefor]";
                msg += "[We cannot remove ANONYMOUS from the session table.]";
                doError(msg);
            }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            return 0;
        } //user did not exist. Nothing to remove.
        
        //If here, we found a container representing the admin.
        AdminTable theAdmin = (AdminTable)bec.entity;
        long admin_id = theAdmin.getId();
        if(admin_id <= 0){ doError("[admin_id lazy fetch error likely.]"); }
        
        boolean MARK_FOR_DELETION = true;
        List<Long> thisAdminsTokens = OwnerTokenTransUtil.
                         getAllTokensOwnedBy_ADMIN(admin_id, MARK_FOR_DELETION);
        
        //List<BaseEntity> adminOccurancesWithinSessionTable = TransUtil.
        //               getEntitiesUsingString
        //               (SessionTable.class, SessionTable., userName);
        
        int len = thisAdminsTokens.size();
        if(0 == len){
            if(errorIfNoActiveSessionsFound){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
                String msg = "[No active sessions found for admin.]";
                msg       += "[Reason: Admin does not own any tokens.]";
                doError(msg);
            }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            return 0;
        }//NO ADMIN TOKENS FOUND? (0==len)
        
        //if admin owns some tokens, go through all of the token id's
        //and remove them from the session table:
        int killCount;
        boolean ERROR_IF_TOKEN_ID_NOT_FOUND = true;
        killCount = SessionTransUtil.killSessionsByTokenID
                                (thisAdminsTokens, ERROR_IF_TOKEN_ID_NOT_FOUND);
        
        if(ERROR_IF_TOKEN_ID_NOT_FOUND){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            if(killCount != len){
                doError("[number of killed does not match number of tokens]");
            }
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //Return the number of sessions that  have been killed.
        return killCount;
        
    }//FUNC::END
           
    /**Closes sessions and marks them for deletion. Finds sessions to
     * close in the session table based on the token_id associated with
     * the session entry.
     * @param tokenIDS :List of token id's that will match any session to close.
     * @param errorIfNoSessionsFound: If true, will throw error if no sessions
     *                                were found in session table using the list
     *                                of token id(s) supplied.
     * @return :Return the number of killed/closed sessions.
     */
    public static int killSessionsByTokenID
                           (List<Long>tokenIDS, boolean errorIfNoSessionsFound){
        TransUtil.insideTransactionCheck(); //<--as before advice would be nice.
        Session ses = TransUtil.getActiveTransactionSession();
        
        int numberOfTokenIDS = tokenIDS.size();
        if(numberOfTokenIDS <= 0){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            doError("[Do not supply this function an empty list!]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //Total number of records killed:
        int killCount = 0;
        
        List<BaseEntity> sesRecords = TransUtil.getEntitiesUsingListOfLong
                   (SessionTable.class, SessionTable.TOKEN_ID_COLUMN, tokenIDS);
        
        if(errorIfNoSessionsFound){
            if(sesRecords.size() <= 0){
                throw new Error("No sessions found for token id's supplied.");
            }//IF::END
        }//IF::END
        
        //go through them all, close them, dele them, and save them:
        SessionTable sesEntry;
        for(BaseEntity b : sesRecords){
            sesEntry = (SessionTable)b;
            sesEntry.setIs_active(false); //<--De-activate session.
            sesEntry.setDele(true);       //<--Dele record.
            killCount++;
        }//next record
        
        return killCount;
       
    }//FUNC::END
    
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
        
        //makeNextSession() already marked the object for save!
        //x//TransUtil.markEntityForSaveOnExit(st); <--DO NOT DO.
        
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
