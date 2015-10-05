package annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An annotation to ENFORCE that a variable identifier must MATCH a string.
 * Used so we can maintain consistency amongst variable names in the project.
 * 
 * All identifiers that are able to be SERIALIZED and used for front-end
 * to back-end communication (usually via request+responses) should have
 * this "VarIdentifier" constraint applied to them so that we can assure
 * consistency amongst the variables used by the front end and back end.
 * 
 * @author jmadison
 */

@Documented //so that java doc is included.
@Inherited //so that annotation is inherited if we extend class using it.
@Retention(RetentionPolicy.RUNTIME) //So can be inspected using reflection.
public @interface Verbatim {
    public String   name();
}//ANNOTATION::END
