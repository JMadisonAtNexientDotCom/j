package test.transactions.util.forNoClearTableOwner;

import primitives.BooleanWithComment;
import primitives.StringWithComment;
import test.MyError;
import test.config.constants.DatabaseConsts;
import test.dbDataAbstractions.entities.containers.BaseEntityContainer;
import test.dbDataAbstractions.entities.entityHelpers.WhoOwnsToken;
import test.dbDataAbstractions.entities.tables.OwnerTable;
import test.dbDataAbstractions.entities.tables.TokenTable;
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
    
    private static long UNUSED_ID = DatabaseConsts.UNUSED_ID;
    
    /**
     * Use the token hash to see who owns that token. Returns an enum
     * for who owns that token.
     * @param token_hash :The hash representation of the token.
     * @return :An enum representing what type of user owns token.
     */
    public static int getWhoOwnsToken(String token_hash){
        //Error Checking:
        TransUtil.insideTransactionCheck();
        
        //in an error state unless otherwise set:
        int ownerResult = WhoOwnsToken.ERROR_ENUM;
        
        BaseEntityContainer bec;
        bec = TokenTransUtil.getTokenEntityUsingTokenString(token_hash);
        if(false == bec.exists){
            ownerResult = WhoOwnsToken.DOES_NOT_EXIST_IN_TOKEN_TABLE;
        }else{
            
            //We found a token in our master token table, so it is now
            //time to see if this token is in the owner table:
            TokenTable tok = (TokenTable)bec.entity;
            long token_id = tok.getId();
            BaseEntityContainer own_container;
            own_container = TransUtil.getEntityFromTableUsingPrimaryKey
                       (OwnerTable.class, OwnerTable.TOKEN_ID_COLUMN, token_id);
            
            if(false == own_container.exists){
                ownerResult = WhoOwnsToken.DOES_NOT_EXIST_IN_OWNER_TABLE;
            }else{
                OwnerTable own;
                own = (OwnerTable)own_container.entity;
                
                //Find the owner: 
                //(If no owner at this point, it is considered an error)
                boolean hasOneValidOwner = false;
                boolean hasNinjaOwner = (own.getAdmin_id() >UNUSED_ID);
                boolean hasAdminOwner = (own.getNinja_id() >UNUSED_ID);
                if(hasNinjaOwner && hasAdminOwner){
                    doError("token cannot be owned by admin and ninja");
                }else
                if(hasNinjaOwner || hasAdminOwner){
                    hasOneValidOwner = true; //one owner. But NOT more.
                }else{
                    doError("token should not be in owner table if not claimed");
                }//IF:BLOCK:END
                
                if(hasOneValidOwner){
                    if(hasNinjaOwner){ ownerResult = WhoOwnsToken.NINJA_OWNS;}
                    if(hasAdminOwner){ ownerResult = WhoOwnsToken.ADMIN_OWNS;}
                }else{
                    throw new Error("Error should have been caught earlier");
                }/////
            }//BLOCK::END
        }//BLOCK::END
        
        //return enum for who owns the token:
        return ownerResult;
        
    }//FUNC::END
    
    /**Get if the token is owned by a ninja.
     * @param token_hash: The HASH representation of the tokenID.
     * @return :Returns TRUE if token is owned by Ninja.
     *          Returns FALSE if token does not exist. **/
    public static boolean isTokenHashOwnedByNinja(String token_hash){
        //Error Checking:
        TransUtil.insideTransactionCheck();
        
        //Logic:
        int owner_enum = getWhoOwnsToken(token_hash);
        return (owner_enum == WhoOwnsToken.NINJA_OWNS);
    }//FUNC::END
    
    /**Get if the token is owned by a admin.
     * @param token_hash: The HASH representation of the tokenID.
     * @return :Returns TRUE if token is owned by Admin.
     *          Returns FALSE if token does not exist. **/
    public static boolean isTokenHashOwnedByAdmin(String token_hash){
        //Error Checking:
        TransUtil.insideTransactionCheck();
        
        //Logic:
        int owner_enum = getWhoOwnsToken(token_hash);
        return (owner_enum == WhoOwnsToken.ADMIN_OWNS); 
    }//FUNC::END
    
    /**
     * Does the token that may or may not be in the table
     * have an owner?
     * @param token_hash :The token HASH to use to find if owner exists.
     * @return         :Returns true if token has owner associated with it.
     */
    public static boolean doesTokenHashHaveOwner(String token_hash){
        
        //Error Checking:
        TransUtil.insideTransactionCheck();
        
        //If there is a valid owner, return true:
        int owner_enum = getWhoOwnsToken(token_hash);
        if(owner_enum == WhoOwnsToken.ADMIN_OWNS){return true;}
        if(owner_enum == WhoOwnsToken.NINJA_OWNS){return true;}
        return false;
        
    }//FUNC::END
   
    /**
     * Used for testing.
     * @param token_id
     * @return:Returns TRUE if the tokenID is owned by someone.
     */
    public static boolean isTokenIDOwned(long token_id){
        
        //error check: Make sure in transaction state:
        TransUtil.insideTransactionCheck();
        
        //use token id to fetch token record from token table:
        BaseEntityContainer bec;
        bec = TransUtil.getEntityFromTableUsingPrimaryKey
                             (TokenTable.class, TokenTable.ID_COLUMN, token_id);
        
        //return true or false:
        if(false == bec.exists){
            return false;
        }else{
            TokenTable tt = (TokenTable)bec.entity;
            String token_hash = tt.getTokenHash();
            return doesTokenHashHaveOwner( token_hash );
        }//BLOCK::END
       
    }//FUNC::END
    
    
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
                                                                
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = OwnerTokenTransUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw new MyError(clazz, err);
    }//FUNC::END

}//CLASS::END
