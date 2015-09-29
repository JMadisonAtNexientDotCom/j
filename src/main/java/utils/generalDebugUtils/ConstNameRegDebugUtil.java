package utils.generalDebugUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import test.MyError;

/**
 * Used to debug registry classes that contain constants that
 * follow a pattern where the identifier is all 
 * UPPER_CASE_SEPARATED_BY_SPACES and the value of the identifier is
 * lower_case_separated_by_spaces.
 * Where they letters are identical, except for the case.
 * 
 * Used to debug const registries that follow this rule:
 * Constants declared like this: String THIS_IS_A_CONST =
 * Constants values like this  :        this_is_a_const
 * 
 * ORIGINAL USAGE:
 * Making sure mistakes were not made in constants that need to have values
 * directly associated with their identifiers. Used in servlet mapping and
 * the documentation classes I made for the .JSP files.
 * 
 * DESIGN NOTE:
 * Not going to enforce which one is upper and which one is lowercase.
 * Rather, just going to enforce that ucased versions should match.
 * 
 * 
 * @author jmadison
 */
public class ConstNameRegDebugUtil {
    
    /** Validates only the static variables of class passed. **/
    public static boolean validateStaticVars
                                             (Class clazz, Boolean throwErrors){
        
        if(null == clazz){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            if(throwErrors){
                doError("clazz==null. Maybe can't check yourself?");
            }else{
                return false;
            }/////////////////
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //count how many times loop executes:
        int debugCounter_field = 0;
        int debugCounter_inst  = 0;
        
        Field[] farr = clazz.getDeclaredFields();
        String var_name;
        String var_value;
        for(Field f : farr){
            debugCounter_field++;
            if( Modifier.isStatic( f.getModifiers() ) ){
                debugCounter_inst++;
                var_name = f.getName();
                var_value= getStringValueFromField_STATIC(f);
                if(notEQ_CaseInsensitive(var_name,var_value)){//EEEEEEEEEEEEEEEE
                    String msg = "Const Identifier does not match it's value!";
                    msg+="var_name:[" + var_name + "]";
                    msg+="var_value:[" + var_value + "]";
                    doError(msg);
                }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            }//Is it static?
        }//Next variable in class.
        
        if(debugCounter_inst <= 0){/////////
            if(throwErrors){
                doError("No static vars found.");
            }else{
                return false;
            }///////////////
        }///////////////////////////////////
        
        return true;
        
    }//FUNC::END
    
  
    
    /**
     * Validates only the static variables of class passed.
     * @param inst :The INSTANCE we want to look at.
     */
    public static boolean validateInstanceVars
                                             (Object inst, boolean throwErrors){
        
        if(null == inst){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            if(throwErrors){
                doError("inst==null. While attempt to check inst vars");
            }else{
                return false;
            }////
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //count how many times loop executes:
        int debugCounter_field = 0;
        int debugCounter_inst = 0;
        
        Class clazz = inst.getClass();
        Field[] farr = clazz.getDeclaredFields();
        String var_name;
        String var_value;
        for(Field f : farr){
            debugCounter_field++;
            if( false == Modifier.isStatic(f.getModifiers() ) ){ //<---DIF#1
                debugCounter_inst++;
                var_name = f.getName();
                var_value= getStringValueFromField_INSTANCE(inst, f);
                if(notEQ_CaseInsensitive(var_name,var_value)){//EEEEEEEEEEEEEEEE
                    String msg = "[Const Identifier != it's value!]";
                    msg+="var_name:[" + var_name + "]";
                    msg+="var_value:[" + var_value + "]";
                    doError(msg);
                }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            }//Is it static?
        }//Next variable in class.
        
        if(debugCounter_inst <= 0){/////////
            if(throwErrors){
                doError("No inst vars found.");
            }else{
                return false;
            }/////////////////////////////////
        }///////////////////////////////////
        
        return true;
        
    }//FUNC::END
    
    /**
     * @param s01 :String to compare #1
     * @param s02 :String to compare #2
     * @return :Returns TRUE if the strings are NOT equal when comparing
     *          in case-insensitive manner.
     */
    private static boolean notEQ_CaseInsensitive(String s01, String s02){
        String U01 = s01.toUpperCase();
        String U02 = s02.toUpperCase();
        boolean doesEqual = (U01.equals(U02));
        return (!doesEqual); //flip result.
    }//FUNC::END
    
    private static String getStringValueFromField_STATIC(Field f){
        Object var_value_as_object = null;
        
        try{//TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
            var_value_as_object = f.get(null);
        }catch(Exception e){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            doError("[Something went wrong in getStringValueFromField_STATIC]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE

        if(null==var_value_as_object){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            //this should never happen. Because previous error check should
            //find it.
            throw new Error("[Should never fire. Prev error check handles]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE

        if(false == (var_value_as_object instanceof String) ){//EEEEEEEEEEEEEEEE
            doError("all static vars should be type STRING!");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //cast to string and return:
        String op = (String)var_value_as_object;
        return op;
        
    }//FUNC::END
    
    /**
     * Gets string value from INSTANCE VAR. (non-static var)
     * @param f :The field (variable) we want to look at.
     * @return :The value in the field.
     */
    private static String getStringValueFromField_INSTANCE
                                                         (Object inst, Field f){
        Object var_value_as_object = null;
        
        try{//TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
            var_value_as_object = f.get(inst); //<--value of field f within inst
        }catch(Exception e){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            doError("[Problem in getStringValueFromField_INSTANCE]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE

        if(null==var_value_as_object){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            //this should never happen. Because previous error check should
            //find it.
            throw new Error("[Should never fire. Prev error check handles]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE

        if(false == (var_value_as_object instanceof String) ){//EEEEEEEEEEEEEEEE
            doError("all static vars should be type STRING!");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //cast to string and return:
        String op = (String)var_value_as_object;
        return op;
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = ConstNameRegDebugUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw new MyError(clazz, err);
    }//FUNC::END
    
}//CLASS::END
