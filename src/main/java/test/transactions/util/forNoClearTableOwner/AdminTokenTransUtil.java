package test.transactions.util.forNoClearTableOwner;

import test.MyError;
import test.dbDataAbstractions.entities.tables.AdminTable;
import test.dbDataAbstractions.entities.tables.TokenTable;

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
    public static void linkAdminToToken(AdminTable theAdmin){
        throw new MyError("finish this 3456745642322");
    }//FUNC::END
    
    /**------------------------------------------------------------------------*
     * Explicityly link Admin to a Token in the TokenTable.
     * @param theAdmin :The admin entity.
     * @param theToken :The token entity used to associate admin with.
     -------------------------------------------------------------------------*/
    public static void explicitLinkAdminToToken
                                     (AdminTable theAdmin, TokenTable theToken){
        throw new MyError("Finish this. 3453453");
    }//FUNC::END
             
    //Function always throws and 
    //public static void errorStubThatEvadesCompilerError(){
    //    
    //}
    
}//CLASS::END
