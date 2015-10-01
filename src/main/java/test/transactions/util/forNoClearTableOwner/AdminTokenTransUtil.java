package test.transactions.util.forNoClearTableOwner;

import test.MyError;
import test.dbDataAbstractions.entities.tables.AdminTable;
import test.dbDataAbstractions.entities.tables.OwnerTable;
import test.dbDataAbstractions.entities.tables.TokenTable;
import test.debug.debugUtils.primitiveTypes.BadValueChecker;
import test.transactions.util.TransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.owner.OwnerTransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.session.SessionTransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.token.TokenTransUtil;

/**-----------------------------------------------------------------------------
 * Handles transactions involving BOTH admin_table and token_table.
 * Because they involve BOTH, they cannot belong to AdminTransUtil or
 * TokenTransUtil
 * 
 * @author jmadison
 ----------------------------------------------------------------------------**/
public class AdminTokenTransUtil {
    
    /**-------------------------------------------------------------------------
     * A lazy way to link admin to token. Will find or create a token
     * that is not owned by anyone else and associate it with this admin.
     * @param theAdmin : The admin entity we want to link to a token.
     *                   This link setup does not necessarily add the token
     *                   to the session_table. As this utility should handle
     *                   only admin_table and token_table transactions.
     ------------------------------------------------------------------------**/
    public static TokenTable linkAdminToNewToken(AdminTable theAdmin){
        
        
        //ErrorCheck:
        TransUtil.insideTransactionCheck();
        
        long admin_id = theAdmin.getId();
        BadValueChecker.checkLong(admin_id);
        
        //Mark entries in the admin table for deletion. Records marked for
        //deletion cannot be used. We can later delete them during a cleanup
        //proceedure on the database. Decided overwriting or deleting while
        //having a conversation/transaction will just make things harder to
        //debug.
        TransUtil.deleINTEGER
                       (OwnerTable.class, OwnerTable.ADMIN_ID_COLUMN, admin_id);
        
        //Get a brand new token:
        //We do NOT want to re-use tokens because that could lead to an easy
        //way for hackers to hijack sessions.
        TokenTable tt = TokenTransUtil.makeNextToken();
        long token_id = tt.getId();
        BadValueChecker.checkLong(token_id);
            
        //Use token_id + admin_id to make entry into owner table:
        OwnerTable own;
        own = OwnerTransUtil.makeEntryUsing_admin(token_id, admin_id);
        if(null == own){doError("[Own should never be null here. 3242523]");}
        
        //Now that admin OWNS this token, we want the token in the session table
        //so that the token can be used to login.
        SessionTransUtil.makeSessionUsingToken(token_id);
        
        //return the token table object, because that is what gives the
        //admin access to admin tools:
        BadValueChecker.checkString(tt.getToken_hash());
        return tt;
      
    }//FUNC::END
    
    /* dont think this is needed.
    //------------------------------------------------------------------------*
     * Explicityly link Admin to a Token in the TokenTable.
     * @param theAdmin :The admin entity.
     * @param theToken :The token entity used to associate admin with.
     -------------------------------------------------------------------------//
    public static void explicitLinkAdminToToken
                                     (AdminTable theAdmin, TokenTable theToken){
        
    }//FUNC::END
    */
             
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = AdminTokenTransUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw new MyError(clazz, err);
    }//FUNC::END
    
}//CLASS::END
