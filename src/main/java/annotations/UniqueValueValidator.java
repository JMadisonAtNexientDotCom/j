package annotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import test.MyError;
import utils.ReflectionHelperUtil;

/**
 * Validator for validating that values within a class are unique.
 * Currently just used for LONG values.
 * @author jmadison
 */
public class UniqueValueValidator {
    
    /**For readability **/
    private static final boolean GET_STATIC = true;
    
    /**For readability **/
    private static final boolean GET_INSTANCE = true;
    
    /**For readability **/
    private static final boolean STATIC_TRUE = true;
    
    /**For readability **/
    private static final boolean STATIC_FALSE = false;
    
    /**For readability **/
    private static final boolean IGNORE = false;
    
    /**
     * Throw error if all ~publically~ exposed STATIC values are NOT unique.
     * @param clazz :The class to inspect via reflection. Note: Only checks
     *               for unique amongst annotated.
     */
    public static void validateStaticLongs(Class clazz){
        
        //For checking that @UniqueStaticValue is not on an INSTANCE field.
        //Or alternatively  @UniqueInstanceValue is not on a STATIC field.
        makeSureFieldsDoNotHaveIncorrectAnnotations(clazz);
        
        List<Field> fields;
        fields = ReflectionHelperUtil.getFieldsWithAnnotation
                           (clazz, UniqueStaticValue.class, GET_STATIC, IGNORE);
        
        //Supply TRUE, because the fields are static:
        //Give null, because static fields do not require an instance
        //to fetch values from it.
        validateLongs_InstanceAndStaticCommonCode(fields,STATIC_TRUE,null);
        
        //TODO NOTES: 2015.10.09:
        //return the value of all the fields in a list so that we can
        //use this as a LOOKUP TABLE, if we so desire:
        //CRAP.. Only works on longs... Should we refactor our shorts to long?
        //Should we refactor this code??
        //Quickest way to go would be to refactor shorts to long.
        
    }//FUNC::END
    
    /** 
     *  This is.. horrible... Code duplication. Probably should have made
     *  use of a template function... Oh well. Faster to hack it.
     * 
     *  WHAT THIS FUNCTION DOES:
     *  Validates that all of the static shorts are unique.
     *  If there is a short with the same value, and a long with the same
     *  value... That will NOT be caught by this error check.
     * 
     *  DESIGN COMMENT:
     *  there is no REF TYPE of short... like there is with Long&long
     *  and Integer&int... Which means, must return array. Which means...
     *  that we cannot have access to built-in methods like index-of.
     *  That sucks.
     * @param clazz :The class
     * @return :returns an array containing all of the values, so that
     *          we can cache it for a lookup function. This will enable
     *          us to easily make enum classes that can check to see if
     *          the value you are asking about exists in the class.
     */
    public static short[] validateStaticShorts(Class clazz){
        
        return null;
        
        //we could solve this using TEMPLATE functions to create validation
        //for different types. Lets figure that out.
        /*
        makeSureFieldsDoNotHaveIncorrectAnnotations(clazz);
        
        List<Field> fields;
        fields = ReflectionHelperUtil.getFieldsWithAnnotation
                         (clazz, UniqueStaticValue.class, IGNORE, GET_INSTANCE);
        
        //This mostly makes sense on ENUM style classes:
        Object inst = ReflectionHelperUtil.makeInstanceUsingClass(clazz);
        
        validateShorts_InstanceAndStaticCommonCode(fields,STATIC_TRUE, null);
        */
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Throw error if all ~publically~ exposed INSTANCE values are NOT unique.
     * @param clazz :The class to inspect via reflection. Note: Only checks
     *               for unique amongst annotated.
     ------------------------------------------------------------------------**/
    public static void validateInstanceLongs(Class clazz){
        
        makeSureFieldsDoNotHaveIncorrectAnnotations(clazz);
        
        List<Field> fields;
        fields = ReflectionHelperUtil.getFieldsWithAnnotation
                         (clazz, UniqueStaticValue.class, IGNORE, GET_INSTANCE);
        
        //This mostly makes sense on ENUM style classes:
        Object inst = ReflectionHelperUtil.makeInstanceUsingClass(clazz);
        
        //Supply false, because the fields are NON-STATIC:
        validateLongs_InstanceAndStaticCommonCode(fields,STATIC_FALSE,inst);
        
        
    }//FUNC::END
    
    /**
     * private shared common code between functions:
     * 1: validateInstanceLongs
     * 2: validateStaticLongs
     * @param fields :Fields to validate.
     * @param doStaticCheckMode :Are we checking static fields?
     * @param inst :If checking instance fields, we must provide instance.
     */
    private static void validateLongs_InstanceAndStaticCommonCode
                   (List<Field> fields, boolean doStaticCheckMode, Object inst){            
        //Create a hashmap that will collect all of the values on the fields.
        //If collision, we throw error. Validation has failed.
                        
        //This map will check for value collisions:
        HashMap<Long,Boolean> mapOfUniqueValues = new HashMap<Long,Boolean>();                   
                        
        Long value;
        Object valueOfUnknownType;
        for(Field f : fields){
            valueOfUnknownType = ReflectionHelperUtil.getValueOfField
                                                     (f,doStaticCheckMode,inst);
            if(is_long_or_Long(valueOfUnknownType)){
                value = (Long)valueOfUnknownType;
                if(mapOfUniqueValues.containsKey(value)){
                    doError("[validation of instance longs/Longs has failed]");
                }//CRASH!
                mapOfUniqueValues.put(value, true);
            }//Is type we are checking for.
        }//Next field.
    }//FUNC::END
    
    /**
     * Make sure that annotations are not incorrectly applied:
     * Examples:
     * 1. @UniqueInstanceVar applied to STATIC   variable.
     * 2. @UniqueStaticVar   applied to INSTANCE variable.
     * @param clazz :The class to check annotations on.
     */  
    private static void makeSureFieldsDoNotHaveIncorrectAnnotations
                                                                  (Class clazz){
                                                                      
        List<Field> badStaticFields;
        List<Field> badInstanceFields;
        
        //Ask for improperly configured fields:
        //Asking for occurances of @UniqueInstanceValue on STATIC fields.
        badStaticFields = ReflectionHelperUtil.getFieldsWithAnnotation
                         (clazz, UniqueInstanceValue.class, GET_STATIC, IGNORE);
        
        //Ask for improperly configured fields:
        //Asking for occurances of @UniqueInstanceValue on INSTANCE fields.
        badInstanceFields = ReflectionHelperUtil.getFieldsWithAnnotation
                       (clazz, UniqueStaticValue.class, IGNORE, GET_INSTANCE);
        
        int b0 = badStaticFields.size();
        int b1 = badInstanceFields.size();
        int totalNumberOfBadFields = b0 + b1;
        if(totalNumberOfBadFields > 0){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            
            String msg = "[Misuse of @Unique_____Value annotations:]";
            msg += "CLASS WITH ERROR:[" + clazz.getCanonicalName() + "]";
            if(b0 > 0){
                msg += "[STATIC FIELDS WITH ERRORS :: START]";
                for(Field bs : badStaticFields){
                    msg += "STATIC field:[" + bs.getName() + "]";
                }//next field.
                msg += "[STATIC FIELDS WITH ERRORS :: END]";
            }//BAD STATIC END.
            
            if(b1 > 0){
                msg += "[INSTANCE FIELDS WITH ERRORS :: START]";
                for(Field bi : badInstanceFields){
                    msg += "INSTANCE field:[" + bi.getName() + "]";
                }//next field.
                msg += "[INSTANCE FIELDS WITH ERRORS :: END]";
            }//BAD INSTANCE END.
            
            doError(msg);
            
        }//ERROR!! //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
       
    }//FUNC::END
    
    /**
     * @param valueOfUnknownType:Object of unknown type.
     * @return :Returns true if object is "long" or "Long" type.
     */
    private static boolean is_long_or_Long(Object valueOfUnknownType){
        if(valueOfUnknownType instanceof Long ||
           valueOfUnknownType.getClass() == long.class  ){
            return true;
        }//is long?
        return false; //NOT long or Long type.
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Get all of the fields that are on the class.
     * @param curClass :The class we want fields off of.
     * @return :The fields. Un-tampered with.
     *          You might want to tamper with them and edit the write access.
     *          That way you can access private fields.
     ------------------------------------------------------------------------**/
    private static List<Field> getFieldsOnClass(Class curClass){
        return ReflectionHelperUtil.getFieldsOnClass(curClass);
    }//FUNC::END
    
    /**
     * Helper Function:Converts an array of fields into a LIST of fields. 
     * @param fArr :The array of Field(s) you want to convert to List. **/
    private static List<Field> arrayToList_Field(Field[] fArr){
        return ReflectionHelperUtil.arrayToList_Field(fArr);
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = UniqueValueValidator.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
