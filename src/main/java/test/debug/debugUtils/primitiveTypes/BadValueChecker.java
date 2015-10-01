package test.debug.debugUtils.primitiveTypes;

import test.MyError;

/**
 * Used to check entities for uninitialized fields.
 * As this is a problem with lazy fetches.
 * @author jmadison
 */
public class BadValueChecker {
    public static void checkLong(long value, String debugMessage){
        if(value == 0){
            String msg = "DebugMSG==" + debugMessage;
            msg += "[value of zero might mean non-inited from lazy fetch";
            doError(msg);
        }//zero?
    }//FUNC::END
    
    public static void checkString(String value, String debugMessage){
        
        String msg = "DebugMSG==((" + debugMessage + "))";
        if(null == value)
        {
            msg +="[String was null]";
            doError(msg);
        }
 
        //I AM NOT DE-REFFING A NULL POINTER!! (Compiler warning)
        if(value.equals("")){
            msg += "[String was empty]";
            doError(msg);
        }
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = BadValueChecker.class;
        err += clazz.getSimpleName();
        err += msg;
        throw new MyError(clazz, err);
    }//FUNC::END
}//CLASS::END
