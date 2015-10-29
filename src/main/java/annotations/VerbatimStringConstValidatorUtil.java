package annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import test.MyError;
import utils.ReflectionHelperUtil;
import utils.StringUtil;

/**
 * Similar to VerbatimValidatorUtil.java, but works on string constants only.
 * @author jmadison :2015.10.29
 */
public class VerbatimStringConstValidatorUtil {
 
    
    /**
     * Validate both static and annotated fields.
     * @param clazz :Class to validate. **/
    public static void validateAllAnnotatedFields(Class clazz){
        Object inst = ReflectionHelperUtil.makeInstanceUsingClass(clazz);
        validateStaticFields(clazz);
        validateInstanceFields(clazz,inst);
    }//FUNC::END
    
    public static void validateInstanceFields(Class clazz, Object inst){
        Class anno = VerbatimStringConst.class;
        List<Field> fields = ReflectionHelperUtil.getFieldsWithAnnotation_INST
                                                                  (clazz, anno);
        
        VerbatimStringConst ver; //current Verbatim annotation on current field.
        for(Field f : fields){
            Annotation ann = f.getAnnotation(VerbatimStringConst.class);
            if(ann instanceof VerbatimStringConst){
                ver = (VerbatimStringConst)ann;
                Object fieldValue = ReflectionHelperUtil.
                                                getValueOfInstanceField(f,inst);
                String fieldName  = f.getName();

                boolean isEquivalent;
                isEquivalent = getIsEquivalent(ver,fieldName,fieldValue);
                
             
                if(false == isEquivalent){
                    doInstanceValidateFail(clazz,inst,fieldName,fieldValue);
                }//
            }//Is annotation?
        }//next field.
        
    }//FUNC::END
    
    private static boolean getIsEquivalent
                 (VerbatimStringConst ver, String fieldName, Object fieldValue){
        boolean isEquivalent = false;
        if(ver.nameMod().equals(VerbatimStringConst.TRIM_UNDERSCORES)){
            String altered = StringUtil.trimUnderscores(fieldName);
            isEquivalent = altered.equals(fieldValue);
        }else
        if(ver.nameMod().equals(VerbatimStringConst.AS_IS)){
            isEquivalent = fieldName.equals(fieldValue);
        }else{
            doError("annotation modifier not supported for verbatim");
        }//BLOCK::END
        return isEquivalent;
    }//FUNC::END
    
    public static void validateStaticFields(Class clazz){
        Class anno = VerbatimStringConst.class;
        List<Field> fields = ReflectionHelperUtil.getFieldsWithAnnotation_STATIC
                                                                  (clazz, anno);
        
        VerbatimStringConst ver; //current Verbatim annotation on current field.
        for(Field f : fields){
            Annotation ann = f.getAnnotation(VerbatimStringConst.class);
            if(ann instanceof VerbatimStringConst){
                ver = (VerbatimStringConst)ann;
                Object fieldValue = ReflectionHelperUtil.
                                                       getValueOfStaticField(f);
                String fieldName  = f.getName();
                
                boolean isEquivalent;
                isEquivalent = getIsEquivalent(ver,fieldName,fieldValue);
                
                if(false == isEquivalent){
                    doStaticValidateFail(clazz,fieldName,fieldValue);
                }//
            }//Is annotation?
        }//next field.
        
    }//FUNC::END
    
    private static void doStaticValidateFail
                             (Class clazz, String fieldName, Object fieldValue){
        String msg = "[STATIC VALIDATE FAIL:]";
        if(false==isString(fieldValue)){
            msg += "[Failed because value of field was NOT a string]";
        }//
        msg += printClass(clazz);
        msg += printFieldName(fieldName);
        msg += printFieldValue(fieldValue);
        throw new Error(msg);
        
    }//FUNC::END
       
    private static void doInstanceValidateFail
                (Class clazz, Object inst, String fieldName, Object fieldValue){
        String msg = "[INSTANCE VALIDATE FAIL:]";
        if(false==isString(fieldValue)){
            msg += "[Failed because value of field was NOT a string]";
        }//
        
        msg += printClass(clazz);
        msg += printInstance(inst);
        msg += printFieldName(fieldName);
        msg += printFieldValue(fieldValue);
        throw new Error(msg);
        
    }//FUNC::END
                
    private static String printClass(Class clazz){
        return "[Class:[" + clazz.getCanonicalName() + "]]";
    }//
    
    private static String printInstance(Object inst){
        return "[Inst:[" + inst.toString() + "]]";
    }//
    
    private static String printFieldName(String fieldName){
        return "[Name:[" + fieldName + "]]";
    }//
    
    private static String printFieldValue(Object FieldValue){
        return "[Value:[" + FieldValue.toString() + "]]";
    }//
    
    
                             
    private static boolean isString(Object val){
        return (val instanceof String);
    }//FUNC::END
     
    
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = VerbatimValidatorUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
}//CLASS::END
