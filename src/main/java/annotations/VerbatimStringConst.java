package annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An annotation to ENFORCE that a variable constant must equal itself.
 * Similar to "verbatim" annotation. Except we don't have to include a
 * name() attribute on the annotation. Because it is matching against itself.
 * 
 * @author jmadison :2015.10.29(Oct29th,Year2015,Thursday)
 */

@Documented //so that java doc is included.
@Inherited //so that annotation is inherited if we extend class using it.
@Retention(RetentionPolicy.RUNTIME) //So can be inspected using reflection.
public @interface VerbatimStringConst {
    
    /** Trims leading and trailing underscores from identifier name before
     *  we match against it. "_SOME_VAR_" becomes "SOME_VAR" **/
    public static final String TRIM_UNDERSCORES = "trim_underscores";
    
    /** Do not modify. Keep as is. **/
    public static final String AS_IS      = "as_is";
   
    /** Modifies the constant expression we are matching against. **/
    public String   nameMod() default AS_IS; //
                              
}//ANNOTATION::END

