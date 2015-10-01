package test.debug.debugUtils.primitiveTypes;

import test.MyError;

/**
 * Used to check entities for uninitialized fields.
 * As this is a problem with lazy fetches.
 * @author jmadison
 */
public class BadValueChecker {
    public static void checkLong(long value){
        if(value == 0){
            doError("[value of zero might mean non-inited from lazy fetch");
        }//zero?
    }//FUNC::END
    
    public static void checkString(String value){
        if(null == value){doError("[String was null]");}
 
        //I AM NOT DE-REFFING A NULL POINTER!! (Compiler warning)
        if(value.equals("")){doError("[String was empty]");}
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
