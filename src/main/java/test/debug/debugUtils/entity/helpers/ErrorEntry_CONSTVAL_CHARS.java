package test.debug.debugUtils.entity.helpers;

import java.util.List;

/**-----------------------------------------------------------------------------
 * Error entry for:
 * 
 * WHEN SITUTATION == :
 *     Static Const (final) who's name ends with _COLUMN and corrosponds
 *     to an entity member variable annotated with @Column
 * 
 * AND:
 *     [instanceMemberVariable.toUpperCase() + _COLUMN != [value of STATIC VAR]
 * 
 * Example:
 * If you have a variable called: TOKEN_MSG_COLUMN,
 * Its value should == "token_msg_column"
 * There should be a instance variable in the class called "token_msg_column"
 * 
 * @author jmadison
 ----------------------------------------------------------------------------**/
public class ErrorEntry_CONSTVAL_CHARS{
    
    public Class c;
    public String fieldName;
    public String columnName;
    
    /**-------------------------------------------------------------------------
     * Helps compile output log of errors.
     * @param msg    :The error message to append to.
     * @param errors :The list of error entries to serialize.
     * @return       :New and improved error message.
     ------------------------------------------------------------------------**/
    public static String add(String msg, List<ErrorEntry_CONSTVAL_CHARS> errors){
        
        String nl = System.lineSeparator();
        ErrorEntry_CONSTVAL_CHARS cur;
        msg+="[::CONSTVAL_CHARS ERROR FOR STATIC _COLUMN::] (Details Below)" + nl;
       
        int len = errors.size();
        for(int i = 0; i < len; i++){
            cur = errors.get(i);
            msg+=makeErrorRecord(cur);
        }//NEXT i
        
        return msg;
        
    }//FUNC::END
    
    /** Serializes one line/row of one of the errors we have.-------------------
     *  So that we can build our ascii table of errors to output
     *  as error message.
     * @param cur : The current error to serialize into a line of text.
     * @return    : Serialized ErrorEntry
     ------------------------------------------------------------------------**/
    private static String makeErrorRecord(ErrorEntry_CONSTVAL_CHARS cur){
        String msg = "";
        msg+="[" + cur.c.getCanonicalName() + "]";
        msg+="[" + cur.fieldName + "]";
        msg+="[" + cur.columnName + "]";
        msg+= System.lineSeparator();
        return msg;
    }//FUNC::END
}//CLASS::END