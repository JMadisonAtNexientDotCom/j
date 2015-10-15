package utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import test.MyError;

/**
 * For COMMON functions between different utilities I have made using reflection.
 * Not meant to do anything too complex. Just used for basic reflection
 * boiler plate code that I have happened to duplicate across multiple utilites.
 * @author jmadison :2015.10.06
 */
public class ReflectionHelperUtil {
    
    /**
     * Validates that class is an annotation. Helps prevent
     * us from ~accidentially~ transposing the annotation class with
     * the class we are checking.
     * @param anno :The class we want to make sure is an annotation.
     */
    public static void validateIsAnnotation(Class anno){
        if(false == anno.isAnnotation()){
            doError("[class not annotation. Did you transpose arguments?]");
        }//
    }//FUNC::END
    
    
    public static List<Field> getFieldsWithAnnotation
              (Class clazz, Class anno, boolean getStatic, boolean getInstance){
                  
        //Error checking inputs:
        if(null == clazz){doError("[inputted class is null!]");}
        if(null == anno){doError("[inputted annotation class is null!]");}
        if(false==(getStatic || getInstance)){doError("[Pick at least 1!]");}
        
        //Check for accidential transposition of arguments by making sure
        //the anno class is an annotation:
        validateIsAnnotation(anno);
        
        //Get the wanted field types:
        List<Field> fields = null;
        if(getStatic && getInstance){
            fields = getFieldsOnClass(clazz);
        }else
        if(getStatic){
            fields = getFieldsOnClass_STATIC(clazz);
        }else
        if(getInstance){
            fields = getFieldsOnClass_INSTANCE(clazz);
        }else{
            String msg = "";
            msg+="[This should be dead code. Should have been caught earlier.]";
            doError(msg);
        }//BLOCK::END
        
        //In case one of the funcs returned null:
        if(null == fields){doError("[fields can be empty, but not null!]");}
        
        //Was used for debugging, DELETE if you want to clean up code.
        //TEMPORARY ERROR: Don't allow for empty either:
        //if(fields.isEmpty()){doError("[TEMP ERROR: DO not allow empty list]");}
        
        //Filter down results to get only the fields that have the
        //annotation you asked for:
        List<Field> wantedFields = new ArrayList<Field>();
        for(Field f : fields){
            if(doesFieldHaveWantedAnnotation(f, anno)){
                wantedFields.add(f);
            }//Has annotation we want?
        }//next field
        
        //return the filtered results:
        return wantedFields;
        
    }//FUNC::END
    
    /**
     * Retrieve only the STATIC fields on a class.
     * @param curClass :Class to inspect.
     * @return :--See details above.--
     */
    public static List<Field> getFieldsOnClass_STATIC(Class curClass){
        //Supplying false will get only STATIC fields:
        return getFieldsOnClass_StaticTrue(curClass, true);
    }//FUNC::END
    
    /**
     * Retrieve only the INSTANCE fields on a class.
     * @param curClass :Class to inspect.
     * @return :--See details above.--
     */
    public static List<Field> getFieldsOnClass_INSTANCE(Class curClass){
        //Supplying false will get only INSTANCE fields:
        return getFieldsOnClass_StaticTrue(curClass, false);
    }//FUNC::END
    
    /**
     * @param curClass :Class to inspect.
     * @param typeToGetToggle: If true, gets static. If false, gets instance.
     * @return : Returns static fields if supplied TRUE,
     *           else returns instance variables.
     */
    private static List<Field> getFieldsOnClass_StaticTrue
                                      (Class curClass, boolean typeToGetToggle){
        List<Field> staticAndInstanceFields = getFieldsOnClass(curClass);
        
        //Output object:
        List<Field> op = new ArrayList<Field>();
        
        //Collect all fields of wanted type:
        boolean curType;
        for(Field f : staticAndInstanceFields){
            curType = getIsFieldStatic(f);
            
            //If typeToGetToggle==TRUE, will enter if field is static.
            //If typeToGetToggle==FALSE,will enter if field is instance.
            if(curType == typeToGetToggle){
                op.add(f);
            }//Is the type of field we want.
        }//next field.
        
        //return the collection of wanted fields:
        return op;
    }//FUNC::END
    
    
    /**-------------------------------------------------------------------------
     * Get all of the fields that are on the class.
     * @param curClass :The class we want fields off of.
     * @return :The fields. Un-tampered with.
     *          You might want to tamper with them and edit the write access.
     *          That way you can access private fields.
     ------------------------------------------------------------------------**/
    public static List<Field> getFieldsOnClass(Class curClass){
        Field[] fArr = curClass.getDeclaredFields();
        List<Field> op = arrayToList_Field(fArr);
        return op;
    }//FUNC::END
    
    /**
     * Helper Function:Converts an array of fields into a LIST of fields. 
     * @param fArr :The array of Field(s) you want to convert to List. **/
    public static List<Field> arrayToList_Field(Field[] fArr){
        
        Field current_field;
        ArrayList<Field> myList = new ArrayList<Field>();
        
        int len = fArr.length;
        for(int i = 0; i < len; i++){
            current_field = fArr[i];
            myList.add(current_field);
        }//NEXT i
        
        return myList;
    }//FUNC::END
    
    public static boolean getIsMethodStatic(Method m){
        boolean result = false;
        if(java.lang.reflect.Modifier.isStatic( m.getModifiers()) ){
            result = true;
        }//Modifier logic.
        return result;
    }//FUNC::END
    
    public static boolean getIsFieldStatic(Field f){
       boolean result = false;     
       //http://stackoverflow.com/questions/3422390/
       //                     retrieve-only-static-fields-declared-in-java-class
       if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
           result = true;
       }//Modifier Logic.
       
       return result;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * @param f   :The field to inspect.
     * @param anno:The annotation we are matching for.
     * @return :Returns TRUE if the wanted annotation exists
     *          on the field we are inspecting.
     ------------------------------------------------------------------------**/
    public static boolean doesFieldHaveWantedAnnotation(Field f, Class anno){
        Annotation[] annoArr = f.getDeclaredAnnotations();
        
        //go through all annotations, see if you find the one you want;
        Class annoType;
        for(Annotation a : annoArr){
            annoType = a.annotationType();
            if(annoType == anno){ return true;} //annotation found!
        }//next annotation.
        
        //return false if we did not return true within the loop.
        return false;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * NOTE: Class must have a ZERO PARAMETER constructor.
     * 
     * Refactored into here because now used in:
     * UniqueValueValidator.java && PostEndPoint.java
     * @param clazz :Class you want to make instance of.
     * @return :Instance of that class.
     ------------------------------------------------------------------------**/
    public static Object makeInstanceUsingClass(Class clazz){
        
        //the instance we want to return:
        Object inst = null;
        
        try{
           inst = clazz.newInstance(); 
        }catch(Exception exep){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            String msg = "";
            msg += "[Unable to embed instance.]";
            msg += "[Possibly this instance does not have...]";
            msg += "[...a zero parameter constructor?]";
            doError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        if(null == inst){doError("[Really? Null? How?]");}
        
        return inst;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Gets a value of a field. Can be static field or non-static.
     * If non-static, inst should be non-null. 
     * If static    , inst should be NULL.
     * @param f             :The field to check the value of.
     * @param isFieldStatic :Is the field static?
     * @param inst          :Provided instance for non-static fields.
     * @return :Value of field checked.
     *          WILL THROW ERROR IF VALUE IS NULL.
     *          Purpose of this code is to check values of enums and const.
     *          A const with a null value is a problem.
     ------------------------------------------------------------------------**/
    public static Object getValueOfField
                                  (Field f, Boolean isFieldStatic, Object inst){
       
        //INPUT ERROR CHECKS:
        //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        if(null == f){doError("inputted field is null");}
        boolean inputCheck = ReflectionHelperUtil.getIsFieldStatic(f);
        if(isFieldStatic != inputCheck){
            doError("[you said it was one type, but it was another.]");
        }//Input check end.
        
        if(false == isFieldStatic){
            if(null == inst){doError("inst param should NOT be null");}
        }else
        if(true == isFieldStatic){
            if(null != inst){doError("inst SHOULD BE NULL if static field");}
        }else{
            doError("This should be a line of dead code.");
        }//BLOCK::END
        //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //IGNORE COMPILER WARNING!! inst is allowed to be null,
        //And should be null, if isFieldStatic is flagged to TRUE.
        //UGH! After all my error checking... you force me to use a try-catch.
        //This language called java... seems to enforce a coding style?
        Object value = null;
        try{//TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
            value = f.get(inst); //<--inst could be null. Thats okay.
        }catch(Exception exep){
            doError("Should never happen. Inst should be allowed to be null");
        }//TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
        
        //We do not allow null values. Because the purpose of our
        //checker is to check registry classes. Like enum classes and
        //classes storing constants. If a constant has a value set to
        //null, that is a huge problem.
        if(null == value){
            doError("Null field values not allowed.");
        }//Block::END
        
        //Return the extracted value:
        return value;
        
    }//FUNC::END
                     
    /**
     * Gets STATIC & INSTANCE METHODS. And scans the hierarchy, taking into
     * account inheritance.
     *
     * SOURCE: http://stackoverflow.com/questions/6593597/
     *    java-seek-a-method-with-specific-annotation-and-its-annotation-element
     * @param type       :The class we are scanning.
     * @param annotation :The annotation we are looking for.
     * @return           :A list of all the methods with annotations you want.
     */
    public static List<Method> getMethodsAnnotatedWith
           (final Class<?> type, final Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<Method>();
        Class<?> klass = type;
        while (klass != Object.class) { 
            //need to iterated thought hierarchy in order to retrieve methods 
            //from above the current instance iterate though the list of methods 
            //declared in the class represented by klass variable, and add those 
            //annotated with the specified annotation
            final List<Method> allMethods = new ArrayList<Method>
                                    (Arrays.asList(klass.getDeclaredMethods()));       
            for (final Method method : allMethods) {
                if (method.isAnnotationPresent(annotation)) {
                    
                    //If you wanted to do more:
                    //Annotation annotInstance = method.getAnnotation
                    //                                             (annotation);
                    // TODO process annotInstance
                    
                    methods.add(method);
                }//Is annotation you want present?
            }//get next method.
            
            //move to the upper class in the 
            //hierarchy in search for more methods
            klass = klass.getSuperclass();
        }//INF LOOP.
        return methods;
    }//FUNC::END
                                  
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = ReflectionHelperUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
