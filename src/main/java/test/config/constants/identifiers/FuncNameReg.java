package test.config.constants.identifiers;

import utils.generalDebugUtils.ConstNameRegDebugUtil;

/** 
 *  TODO: Validate that all constants are upper-case 
 *        versions of the lower-case values.
 *        Use reflection to do this.
 * 
 *  Master list of function names. Original use:
 *  For mapping service call names in a way where Front-End + back-end
 *  can agree on the names.
 * 
 * @author jmadison
 */
public class FuncNameReg {
    
    static{//////////
        validate();
    }////////////////
    
    //ADMIN SERVICE:
    public static final String LOGIN_VALIDATE = "login_validate";
    public static final String LOGIN_AND_GET_TOKEN_FOR_SELF = "login_and_get_token_for_self";
    
    //NINJA SERVICE:
    public static final String GET_NINJA_BY_ID   = "get_ninja_by_id";
    public static final String GET_NEXT_NINJA    = "get_next_ninja";
    public static final String MAKE_NINJA_RECORD = "make_ninja_record";
    
    //OWNER SERVICE:
    public static final String MAKE_ENTRY_USING_NINJA   = "make_entry_using_ninja";
    public static final String MAKE_ENTRY_USING_ADMIN   = "make_entry_using_admin";
    public static final String MAKE_ENTRY_USING_RANDOM  = "make_entry_using_random";
    public static final String DOES_TOKEN_HAVE_OWNER    = "does_token_have_owner";
    public static final String GET_TOKEN_OWNER          = "get_token_owner";
    
    //TOKEN SERVICE
    public static final String GET_NEXT_TOKEN = "get_next_token";
    
    //RIDDLERHYME SERVICE:
    public static final String GET_IS_CORRECT           = "get_is_correct";
    public static final String GRADE_ONE_BLANK_SLATE    = "grade_one_blank_slate";
    public static final String GET_FILLED_OUT_TEST_SLATE_TRUTH = "get_filled_out_test_slate_truth";
    public static final String GET_FILLED_OUT_TEST_SLATE_WRONG = "get_filled_out_test_slate_wrong";
    public static final String GET_ONE_BLANK_SLATE      = "get_one_blank_slate";
    public static final String GET_ONE_RANDOM_RIDDLE    = "get_one_random_riddle";
    public static final String MAKE_FILLED_OUT_CUE_CARD = "make_filled_out_cue_card";
    public static final String GET_RANDOM_TRIVIA_BUNDLE = "get_random_trivia_bundle";
    public static final String POST_QUAR_FOR_GRADING    = "post_quar_for_grading";
    public static final String GET_LAST_POSTED_QUAR     = "get_last_posted_quar88";
    
    /** Validates itself. Will this work? **/
    private static void validate(){//VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV
        ConstNameRegDebugUtil.validateStaticVars(FuncNameReg.class);
    }//VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV
    
}//FUNC::END
