package annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import javax.persistence.Column;
import test.MyError;
import utils.ReflectionHelperUtil;

/**
 * Used to validate that @Verbatim annotations used on variable and method
 * names match the name of that variable or method.
 * 
 * How @Verbatim works: Check the @Verbatim class.
 * /////////////////////////////////////////////////////////////////////////////
 * Short version:
 * //This will NOT crash:
 * @Verbatim(name="norton")
 * public long norton = 5242342;
 * 
 * //This WILL crash because of bad validation:
 * @Verbatim(name="norton")
 * public long willywonka = 5242342;
 * 
 * Why are we doing something like this?
 * So that we can highly couple API calls in a way that allows
 * us to give .JSP files "autocomplete" access to controller methods.
 * In order to do things like this, we have to verify functions and variables
 * are using the names we think they are. Else the auto-complete could
 * lie to us.
 * /////////////////////////////////////////////////////////////////////////////
 * 
 * Usage: Creating better coupling between front end and back end.
 * 
 * @author jmadison :2015.10.04 -Stub made. OtherClass:Verbatim.java:finished.
 */
public class VerbatimValidatorUtil {
    
    /**For readability **/
    private static final boolean GET_STATIC = true;
    
    /**For readability **/
    private static final boolean GET_INSTANCE = true;
    
    /**
     * Validate annotated STATIC fields.
     * @param clazz
     * @param anno 
     */
    public static void validateAnnotatedFields(Class clazz){
        Class anno = Verbatim.class;
        List<Field> fields = ReflectionHelperUtil.getFieldsWithAnnotation
                                        (clazz, anno, GET_STATIC ,GET_INSTANCE);
        
        Verbatim ver; //current Verbatim annotation on current field.
        for(Field f : fields){
            Annotation ann = f.getAnnotation(Verbatim.class);
            if(ann instanceof Verbatim){
                ver = (Verbatim)ann;
                String verbatimName = ver.name();
                String fieldName    = f.getName();
                
                if(verbatimName.equals(fieldName)){
                    String msg = "[@Verbatim failiure!]";
                    msg += "verbatimName ==[" + verbatimName + "]";
                    msg += "fieldName == [" + fieldName + "]";
                    msg += "[Field annotated with @Verbatim has failed]";
                    msg += "[To have an identifier value exactly equal to]";
                    msg += "[The @Verbatim(name='____') value.]";
                    msg += "[For the integrity of the whole system,]";
                    msg += "[To ensure nothing breaks.]";
                    msg += "[To ensure consistency amongst different pieces]";
                    msg += "[Of the app....]";
                    msg += "[Please fix this problem by:]";
                    msg += "OPTION#1: Change the identifier value]";
                    msg += "OPTION#2: Change the @Verbatim.name value]";
                    doError(msg);
                }//ERROR!!!
                
            }else{
                String msg = "[All fields should have this annotation.]";
                msg += "[Possible error in getFieldsWithAnnotation Or you...]";
                msg += "[are calling getFieldsWithAnnotaion incorrectly]";
                doError(msg);
            }//BLOCK::END 
        }//Next field.
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
        throw new MyError(clazz, err);
    }//FUNC::END
    
}//CLASS::END
