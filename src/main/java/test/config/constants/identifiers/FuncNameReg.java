package test.config.constants.identifiers;


/** 
 *  TODO: Validate that all constants are upper-case 
 *        versions of the lower-case values.
 *        Use reflection to do this.
 * 
 *  Master list of function names. Original use:
 *  For mapping service call names in a way where Front-End + back-end
 *  can agree on the names.
 * 
 * @author jmadison :Original date unknown.
 * @author jmadison :Last touched: 2015.10.26(Oct26th,Year2015,Monday)
 */
public class FuncNameReg {
    
   // static{//////////
    //    validate();
   // }////////////////
    
    //DEBUG:
    //public static final String DEBUGGER_STUB_FUNCTION="debugger_stub_function";
    
    //CUECARD SERVICE:
    public static final String MAKE_FILLED_OUT_CUE_CARD_AND_PERSIST_IT = "make_filled_out_cue_card_and_persist_it";
    public static final String MAKE_FILLED_OUT_CUE_CARD = "make_filled_out_cue_card";
    
    //INK SERVICE:
    public static final String PERSIST_GROUP_OF_1_QUIP_TEST = "persist_group_of_1_quip_test";
    public static final String PERSIST_GROUP_OF_2_QUIP_TEST = "persist_group_of_2_quip_test";
    public static final String PERSIST_GROUP_OF_3_QUIP_TEST = "persist_group_of_3_quip_test";
    public static final String PERSIST_GROUP_OF_4_QUIP_TEST = "persist_group_of_4_quip_test";
    
    //GROUP SERVICE:
    public static final String MAKE_NEW_GROUP="make_new_group";
    
    //TRIAL SERVICE:
    public static final String DISPATCH_TOKENS="dispatch_tokens";
    public static final String DISPATCH_TOKENS_DEBUG="dispatch_tokens_debug";
    
    //ADMIN SERVICE:
    public static final String LOGIN_VALIDATE = "login_validate";
    public static final String LOGIN_AND_GET_TOKEN_FOR_SELF = "login_and_get_token_for_self";
    
    //NINJA SERVICE:
    public static final String GET_NINJA_BY_TOKEN_HASH = "get_ninja_by_token_hash";
    public static final String GET_NINJA_BY_ID   = "get_ninja_by_id";
    public static final String GET_NEXT_NINJA    = "get_next_ninja";
    public static final String MAKE_NINJA_RECORD = "make_ninja_record";
    public static final String GET_PAGE_OF_NINJAS= "get_page_of_ninjas";
    
    //OWNER SERVICE:
    public static final String MAKE_ENTRY_USING_NINJA       = "make_entry_using_ninja";
    public static final String MAKE_ENTRY_USING_ADMIN       = "make_entry_using_admin";
    public static final String MAKE_ENTRY_USING_RANDOM      = "make_entry_using_random";
    public static final String IS_TOKEN_ID_OWNED            = "is_token_id_owned";
    public static final String GET_TOKEN_OWNER              = "get_token_owner";
    public static final String IS_TOKEN_HASH_OWNED_BY_NINJA = "is_token_hash_owned_by_ninja";
    public static final String IS_TOKEN_HASH_OWNED_BY_ADMIN = "is_token_hash_owned_by_admin";
    
    //TOKEN SERVICE
    public static final String GET_NEXT_TOKEN = "get_next_token";
    
    //RIDDLERHYME SERVICE:
    public static final String GET_IS_CORRECT           = "get_is_correct";
    public static final String GRADE_ONE_BLANK_SLATE    = "grade_one_blank_slate";
    public static final String GET_FILLED_OUT_TEST_SLATE_TRUTH = "get_filled_out_test_slate_truth";
    public static final String GET_FILLED_OUT_TEST_SLATE_WRONG = "get_filled_out_test_slate_wrong";
    public static final String GET_ONE_BLANK_SLATE      = "get_one_blank_slate";
    public static final String GET_ONE_RANDOM_RIDDLE    = "get_one_random_riddle";
    public static final String GET_RANDOM_TRIVIA_BUNDLE = "get_random_trivia_bundle";
    public static final String POST_QUAR_FOR_GRADING    = "post_quar_for_grading";
    public static final String GET_LAST_POSTED_QUAR     = "get_last_posted_quar";
    
    /* cannot do to final vars.
    XX populate all consts will error message to help find error. XXX
    public static void wreckIt(String msg){
        LOGIN_VALIDATE                  = msg;
        LOGIN_AND_GET_TOKEN_FOR_SELF    = msg;

        GET_NINJA_BY_ID                 = msg;
        GET_NEXT_NINJA                  = msg;
        MAKE_NINJA_RECORD               = msg;

        MAKE_ENTRY_USING_NINJA          = msg;
        MAKE_ENTRY_USING_ADMIN          = msg;
        MAKE_ENTRY_USING_RANDOM         = msg;
        DOES_TOKEN_HAVE_OWNER           = msg;
        GET_TOKEN_OWNER                 = msg;

        GET_NEXT_TOKEN                  = msg;

        GET_IS_CORRECT                  = msg;
        GRADE_ONE_BLANK_SLATE           = msg;
        GET_FILLED_OUT_TEST_SLATE_TRUTH = msg;
        GET_FILLED_OUT_TEST_SLATE_WRONG = msg;
        GET_ONE_BLANK_SLATE             = msg;
        GET_ONE_RANDOM_RIDDLE           = msg;
        MAKE_FILLED_OUT_CUE_CARD        = msg;
        GET_RANDOM_TRIVIA_BUNDLE        = msg;
        POST_QUAR_FOR_GRADING           = msg;
        GET_LAST_POSTED_QUAR            = msg;
    }//FUNC::END
    */
    
}//CLASS::END
