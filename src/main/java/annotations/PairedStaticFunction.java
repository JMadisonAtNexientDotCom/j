package annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An annotation that says the function name is paired with another function
 * of the same name. Used to enforce that function of same name exists in
 * another class.
 * @author jmadison :2015.10.03_1140AM
 */
@Documented //so that java doc is included.
@Inherited //so that annotation is inherited if we extend class using it.
@Retention(RetentionPolicy.RUNTIME) //So can be inspected using reflection.
public @interface PairedStaticFunction {
    
   //No flags in here. Existance of annotation enforces constraint.
    
}//ANNOTATION::END