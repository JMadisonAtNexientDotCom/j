package annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An annotation to ENFORCE that variable within a class is UNIQUE VALUE
 * for that STATIC variable. Used for enum classes to make sure we did not
 * ~accidentially~ make a collision with our values.
 * 
 * @author jmadison :2015.10.06
 */

@Documented //so that java doc is included.
@Inherited //so that annotation is inherited if we extend class using it.
@Retention(RetentionPolicy.RUNTIME) //So can be inspected using reflection.
public @interface UniqueStaticValue {
   //contains nothing inside. Just used for enforcement.
}//ANNOTATION::END
