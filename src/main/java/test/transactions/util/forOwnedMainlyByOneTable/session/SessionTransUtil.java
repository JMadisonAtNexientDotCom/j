package test.transactions.util.forOwnedMainlyByOneTable.session;
//345678901234567890123456789012345678901234567890123456789012345678901234567890

import primitives.StringWithComment;
import test.MyError;
import test.dbDataAbstractions.entities.containers.BaseEntityContainer;
import test.dbDataAbstractions.entities.tables.AdminTable;
import test.dbDataAbstractions.entities.tables.TokenTable;
import test.transactions.util.TransUtil;
import test.transactions.util.forNoClearTableOwner.AdminTokenTransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.admin.AdminTransUtil;

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
        //theToken = AdminTokenTransUtil.linkAdminToToken(theAdmin);
        
        //Return the string representing admin's token:
        //return theToken.token;
        
        op.value = "TODO: Finish actual logic for getActiveTokenForAdmin";
        op.isError = false;
        return op;
        
    }//FUNC::END
    
    

}//CLASS::END
