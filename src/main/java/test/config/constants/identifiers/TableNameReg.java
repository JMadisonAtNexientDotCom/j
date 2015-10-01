package test.config.constants.identifiers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import test.MyError;
import test.config.constants.identifiers.utils.StringConstantFinderUtil;

/**
 * Master registry for all of the table names.
 * @author jmadison :2015.09.30
 */
public class TableNameReg {
    
    public static final String RIDDLE_RHYME_TRUTH_TABLE = "riddle_rhyme_truth_table";
    public static final String RIDDLE_RHYME_WRONG_TABLE = "riddle_rhyme_wrong_table";
    public static final String TRANS_TABLE   = "trans_table";
    public static final String RHYME_TABLE   = "rhyme_table";
    public static final String RIDDLE_TABLE  = "riddle_table";
    public static final String ADMIN_TABLE   = "admin_table";
    public static final String NINJA_TABLE   = "ninja_table";
    public static final String TOKEN_TABLE   = "token_table";
    public static final String SESSION_TABLE = "session_table";
    public static final String OWNER_TABLE   = "owner_table";
    
    /**
     * ORIGINAL USEAGE: Checking for string transposition errors where
     * string argument to function has been swapped with table name.
     * 
     * @param value :The const value we are checking for.
     * @return :returns true if the constant exists.
     */
    public static boolean contains(String value){
        return StringConstantFinderUtil.contains(TableNameReg.class, value);
    }//FUNC::END
    
}//CLASS::END
