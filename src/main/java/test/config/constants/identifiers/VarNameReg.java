package test.config.constants.identifiers;

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
    
    static{//////////
        validate();
    }////////////////
    
    //Names used as table columns or parameters:
    public static final String ID        = "id";
    public static final String TOKEN_ID  = "token_id";
    public static final String ADMIN_ID  = "admin_id";
    public static final String NINJA_ID  = "ninja_id";
    public static final String RIDDLE_ID = "riddle_id";
    public static final String RHYME_ID  = "rhyme_id";
    public static final String NAME          = "name";
    public static final String PHONE         = "phone";
    public static final String EMAIL         = "email";
    public static final String PORTFOLIO_URL = "portfolio_url";
    
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
    
    /** Validates itself. Will this work? **/
    private static void validate(){//VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV
        ConstNameRegDebugUtil.validateStaticVars(VarNameReg.class);
    }//VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV
    
}//CLASS::END
