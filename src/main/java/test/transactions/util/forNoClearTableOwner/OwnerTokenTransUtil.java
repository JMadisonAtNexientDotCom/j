package test.transactions.util.forNoClearTableOwner;

import primitives.BooleanWithComment;
import primitives.StringWithComment;
import test.dbDataAbstractions.entities.containers.BaseEntityContainer;
import test.transactions.util.TransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.owner.OwnerTransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.token.TokenTransUtil;

/**
 * Handles transactions that involve BOTH the owner_table and
 * the token_table
 * @author jmadison
 */
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
        
        BooleanWithComment op = new BooleanWithComment();
        String msg = "isTokenAbleToBeEnteredIntoOwnerTable_NOT_SET";
        
        BaseEntityContainer bec;
        bec = TokenTransUtil.getTokenEntityUsingTokenID(token_id);
        if(false == bec.exists){
            op.value   = false;
            op.isError = false;
            msg += "[Token cannot be put into owner table ]";
            msg += "[because it does not exist in the token_table]";
            op.comment = msg;
            return op;
        }//entity not exist?
        
       Boolean alreadyInOwnerTable = OwnerTransUtil.doesTokenExist(token_id);
       if(alreadyInOwnerTable){
           op.value   = false;
           op.isError = false;
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
