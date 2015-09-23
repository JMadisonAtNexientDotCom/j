package test.transactions.util.forNoClearTableOwner;

import primitives.BooleanWithComment;
import primitives.StringWithComment;
import test.dbDataAbstractions.entities.containers.BaseEntityContainer;
import test.transactions.util.TransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.owner.OwnerTransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.token.TokenTransUtil;

/**
 * 
 * @author jmadison
 */

//345678901234567890123456789012345678901234567890123456789012345678901234567890
/**##########################CLASS HEADER FILE##################################
//WHAT THIS CLASS DOES:
// Handles transactions that involve BOTH the owner_table and
// the token_table
// 
//ORIGINAL USE CASE:
//Creating more friendly errors in the REST API. By friendly errors,
//I mean errors that:
//    1: Do NOT crash the server application.
//    2: Return the expected JSON response, and flag
//       that response as being an ERROR.
//
//DESIGN NOTE (Justifications for why things are the way they are):
//1. UI people are more likely to see errors when they happen this way.
//2. Average consumer more likely to see error message this way as well.
//   In the case that the UI ends up having errors in it.
//
//   Note on naming:
//   Called "OwnerTokenTransUtil" rather than "TokenOwnerTransUtil"
//   because I am sorting the names alphabetically.
//   OwnerTokenTransUtil is shorthand for 
//   owner_table_and_token_table_transaction_utility
//   This might be a bit confusing since someone might be:
//   Asking "I don't see a token_owner_table in the database!
//   To prevent this problem. I am going to try and keep all table
//   names to two works only.   [WORD]_TABLE will be the basic format
//   for the database tables.
//
//@author JMadison : 2015.09.23_0631PM    -first created.
//@author XXXXXXXX : 20XX.XX.XX_####AMPM
//@author XXXXXXXX : 20XX.XX.XX_####AMPM 
//@author XXXXXXXX : 20XX.XX.XX_####AMPM
########10########20########30########40########50########60########70########*/
//-------0---------0---------0---------0---------0---------0---------0---------0
public class OwnerTokenTransUtil {
    
    /**
     * In order for a token to be able to be entered into the owner table,
     * the token must:
     * 1. already exist in the main token_table
     * 2. NOT already exist in the owner_table
     * @param token_id :The token_id we want to insert into owner_table.
     * @return :Returns a boolean with a comment attached to it. If
     *          the boolean is false, it returns a message for WHY
     *          the token is unable to be entered into the owner table.
     */
    public static BooleanWithComment isTokenAbleToBeEnteredIntoOwnerTable
                                                                (long token_id){
        //Error check:
        TransUtil.insideTransactionCheck();
        
        //Note: I have the habit of initializing strings to "NOT_SET"
        //      So I can identify where I screwed up if I ever see
        //      "NOT_SET" in a string. An empty string would be harder to find.
        BooleanWithComment op = new BooleanWithComment();
        String msg = "OwnerTokenTransUtil.java::NOT_SET"; //<-- "NOT_SET" != ""
        
        BaseEntityContainer bec;
        bec = TokenTransUtil.getTokenEntityUsingTokenID(token_id);
        if(false == bec.exists){
            op.value   = false;
            op.isError = false;
            msg = "";//clear
            msg += "[Token cannot be put into owner table ]";
            msg += "[because it does not exist in the token_table]";
            op.comment = msg;
            return op;
        }//entity not exist?
        
       Boolean alreadyInOwnerTable = OwnerTransUtil.doesTokenExist(token_id);
       if(alreadyInOwnerTable){
           op.value   = false;
           op.isError = false;
           msg = "";//clear
           msg += "[Token cannot be put into owner table ]";
           msg += "[because it is already in the owner table]";
           //Note: if token is in owner table, it should be claimed.
           //Unclaimed tokens should NOT be in owner table.
           //Unclaimed == no one owns the token.
           op.comment = msg;
           return op;
       }//already in owner table?
       
       op.value = true;
       op.isError=false;
       op.comment="SUCCESS! token_id can be inserted into owner_table";
       return op;
        
    }//FUNC::END
    
}//CLASS::END
