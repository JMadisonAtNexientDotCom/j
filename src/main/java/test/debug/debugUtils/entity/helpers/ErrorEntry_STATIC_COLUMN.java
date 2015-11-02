package test.debug.debugUtils.entity.helpers;

import java.util.List;

/**-----------------------------------------------------------------------------
 * Collects data for STATIC: [[varname.toUpperCase()]+_COLUMN] 
 * variable missing errors.
 * @author jmadison :2015.07.19_0547PM
 -----------------------------------------------------------------------------*/
public class ErrorEntry_STATIC_COLUMN {
    
    public Class c;
    public String instanceVarName;
    
    
     /**-------------------------------------------------------------------------
     * Helps compile output log of errors.
     * @param msg    :The error message to append to.
     * @param errors :The list of error entries to serialize.
     * @return       :New and improved error message.
     ------------------------------------------------------------------------**/
    public static String add(String msg, List<ErrorEntry_STATIC_COLUMN> errors){
        
        String nl = System.lineSeparator();
        ErrorEntry_STATIC_COLUMN cur;
        msg+="[::CONSTVAL_COLUMN ERROR $$$$$$$$$$$$$$$ ::]" + nl;
        msg+="[ Missing: [instVarName + '_COLUMN' static string ]" + nl;
       
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
    private static String makeErrorRecord(ErrorEntry_STATIC_COLUMN cur){
        String msg = "";
        msg+="[" + cur.c.getCanonicalName() + "]";
        msg+="[" + cur.instanceVarName + "]";
        
        //Mock up the variable that is missing:
        String constName = cur.instanceVarName.toUpperCase() + "_COLUMN";
        msg+="[" + "missing static const:[" + constName + "]";
        
        msg+= System.lineSeparator();
        return msg;
    }//FUNC::END
}//CLASS::END
