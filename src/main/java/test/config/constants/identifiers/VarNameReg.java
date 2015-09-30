package test.config.constants.identifiers;

import test.MyError;
import utils.generalDebugUtils.ConstNameRegDebugUtil;


/**
 * Registry class used to store variable names that are used in:
 * 1. table columns.
 * 2. servlet parameters.
 * 3. UI JSP code.
 *
 * The variable names should be consistent among these.
 * So by creating constants for them, it will help enforce this.
 * 
 * DESIGN NOTE: Non-Static because we want the root front-end back end
 * class to be able to access these with dot operator.
 * 
 * Random comment: Just learned I can do this:
 *  {@value FBVarNameRegistry#TOKEN_ID}
 * In order to reference variables in documentation in a refactorable way.
 * 
 * @author jmadison
 */
public class VarNameReg {
    
   // static{//////////
   //     validate();
   // }////////////////
    
    //Names used as: TableColumns + Parameters + Required of Base Entities:
    public static final String ID           = "id";
    
    /** A comment that can be added to a record to help us debug. 
     *  Usually tells us what function last touched the record. **/
    public static final String COMMENT      = "comment";
    
    /** DELE != DELETE. DELE means to "mark for deletion".
     *  Drew mentioned deleting information from a database can lead to all
     *  sorts of trouble. Combine that with the fact that deleting or 
     *  overwriting records will make it harder to debug... I think a good
     *  solution is to mark records as DELE. Objects marked as dele will
     *  be ignored in transactions.
     * 
     *  Then LATER we could have a clean-up method pessimistically locks
     *  the entire database and cleans up records marked for deletion.
     * 
     *  Splitting up clean-up into a whole different procedure rather than
     *  mixing it in with the transaction logic seems like a good separation
     *  of concerns which will make this easier. **/
    public static final String DELE         = "dele";
    
    /** A unique ID that is unique to the ENTIRE database. It represents
     *  the order in which the record was created or edited. Using this so when
     *  I run into concurrency issues I can have some insight into what is
     *  going wrong. **/
    public static final String MUTATE_ID     = "mutate_id";
    
    /** When a record is first made, the SAVE_COUNTER == 1.
     *  When a record is edited and re-saved
     *  (Which should be NEVER according to my current design)
     *  The number is incremented. **/
    public static final String SAVE_COUNTER = "save_counter";
    
    
    //Names used as table columns or parameters:
    public static final String TOKEN_ID  = "token_id";
    public static final String TOKEN_HASH= "token_hash"; //string representation of token.
    public static final String ADMIN_ID  = "admin_id";
    public static final String NINJA_ID  = "ninja_id";
    public static final String RIDDLE_ID = "riddle_id";
    public static final String RHYME_ID  = "rhyme_id";
    public static final String NAME          = "name";
    public static final String PHONE         = "phone";
    public static final String EMAIL         = "email";
    public static final String PORTFOLIO_URL = "portfolio_url";
    public static final String TEXT          = "text"; //used in TextTableBaseEntity.java
    
    /** Time in milliseconds that a session was opened in the session table. **/
    public static final String OPENED_ON = "opened_on";
    /** Duration in milliseconds that session should last. **/
    public static final String DURATION  = "duration";
    /** Is the session active/valid? (NOT expired) **/
    public static final String IS_ACTIVE = "is_active";
    
    
    //Names only used as params to rest services:
    public static final String USER_NAME         = "user_name";
    public static final String PASS_WORD         = "pass_word";
    public static final String NUMBER_OF_CHOICES = "number_of_choices";
    public static final String NUMBER_OF_TRUTHS  = "number_of_truths";
    public static final String CARD_COUNT        = "card_count";
    public static final String NUM_QUIPS         = "num_quips";
    public static final String TRU_MIN           = "tru_min";
    public static final String TRU_MAX           = "tru_max";
    
    //For HTTP POST:
    public static final String JSON_OBJ          = "json_obj";
    
    /*
    xx Replaces all variable names with error message to help bring attention
     *  to the problem. xx
    public static void wreckIt(String msg){
        ID                = msg;
        TOKEN_ID          = msg;
        ADMIN_ID          = msg;
        NINJA_ID          = msg;
        RIDDLE_ID         = msg;
        RHYME_ID          = msg;
        NAME              = msg;
        PHONE             = msg;
        EMAIL             = msg;
        PORTFOLIO_URL     = msg;

        USER_NAME         = msg;
        PASS_WORD         = msg;
        NUMBER_OF_CHOICES = msg;
        NUMBER_OF_TRUTHS  = msg;
        CARD_COUNT        = msg;
        NUM_QUIPS         = msg;
        TRU_MIN           = msg;
        TRU_MAX           = msg;

        JSON_OBJ          = msg;
    }//FUNC::END
    */
    
}//CLASS::END
