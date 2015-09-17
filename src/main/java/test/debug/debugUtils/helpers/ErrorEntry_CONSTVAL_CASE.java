package test.debug.debugUtils.helpers;

import java.util.List;

/**-----------------------------------------------------------------------------
 * Error for when the public static WHATEVER_DATA_COLUMN has a value
 * that contains upper-case characters. This is not allowed.
 * And by association, column names in database and entities are
 * not allowed to have upper case characters either.
 * 
 * @author jmadison:2015.09.17_0715PM
 ----------------------------------------------------------------------------**/
public class ErrorEntry_CONSTVAL_CASE {
    
    public Class c;
    public String fieldName;
    public String columnName;
    
    /**-------------------------------------------------------------------------
     * Helps compile output log of errors.
     * @param msg    :The error message to append to.
     * @param errors :The list of error entries to serialize.
     * @return       :New and improved error message.
     ------------------------------------------------------------------------**/
    public static String add(String msg, List<ErrorEntry_CONSTVAL_CASE> errors){
        
        String nl = System.lineSeparator();
        ErrorEntry_CONSTVAL_CASE cur;
        msg+="[::CONSTVAL_CASE ERROR FOR STATIC _COLUMN::] (Details Below)" + nl;
       
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
    private static String makeErrorRecord(ErrorEntry_CONSTVAL_CASE cur){
        String msg = "";
        msg+="[" + cur.c.getCanonicalName() + "]";
        msg+="[" + cur.fieldName + "]";
        msg+="[" + cur.columnName + "]";
        msg+= System.lineSeparator();
        return msg;
    }//FUNC::END
    
    
    
}//CLASS:END
