package test.debug.debugUtils.entity.helpers;

import java.util.List;

/**-----------------------------------------------------------------------------
 * An object that logs an @Column(name=...) error in our code.
 * @author jmadison
 ----------------------------------------------------------------------------**/
public class ErrorEntry {
    public Class c;
    public String fieldName;
    public String columnName;
    
    /**-------------------------------------------------------------------------
     * Helps compile output log of errors.
     * @param msg    :The error message to append to.
     * @param errors :The list of error entries to serialize.
     * @return       :New and improved error message.
     ------------------------------------------------------------------------**/
    public static String add(String msg, List<ErrorEntry> errors){
        
        String nl = System.lineSeparator();
        ErrorEntry cur;
        msg+="[::BROKEN ENFORCED CONVENTION ERROR::] (Details Below)" + nl;
        msg+="Variable Name != Column Name" + nl;
        msg+="|--Class Name:--||--Variable Name:--||--Column Name:--|" + nl;
       
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
    private static String makeErrorRecord(ErrorEntry cur){
        String msg = "";
        msg+="[" + cur.c.getCanonicalName() + "]";
        msg+="[" + cur.fieldName + "]";
        msg+="[" + cur.columnName + "]";
        msg+= System.lineSeparator();
        return msg;
    }//FUNC::END
    
}//CLASS::END
