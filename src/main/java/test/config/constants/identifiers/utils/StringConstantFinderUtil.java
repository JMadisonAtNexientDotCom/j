package test.config.constants.identifiers.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import test.MyError;

/**
 * Noticed TableNameReg and VarNameReg used almost identical blocks of code.
 * So put into utility. Searches a given constant-registry class for a given
 * static constant value.
 * @author jmadison :2015.10.01_1038PM
 */
public class StringConstantFinderUtil {
    
    public static boolean contains(Class clazz, String value){
        String currentConstValue = "";
        Field[] fArr = clazz.getDeclaredFields();
        for(Field f : fArr){
            if( Modifier.isStatic( f.getModifiers() ) ){
                try {//[TRY][TRY][TRY][TRY][TRY][TRY][TRY][TRY][TRY][TRY][TRY]]]
                    currentConstValue = (String)f.get(null);
                } catch (Exception ex) {
                    String msg = "[fetched const value was not string?]";
                    msg += "ex==" + ex.toString();
                    doError(msg);
                }//[TRY][TRY][TRY][TRY][TRY][TRY][TRY][TRY][TRY][TRY][TRY][TRY]]
                
                if(currentConstValue.equals(value)){
                    return true;
                }//FOUND!
            }//is variable static?
        }//next field.
        
        //return false. Field NOT found.
        return false;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = StringConstantFinderUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw new MyError(clazz, err);
    }//FUNC::END
    
}//CLASS::END
