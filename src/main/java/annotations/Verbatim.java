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
 * How @Verbatim works: Short summary:
 * /////////////////////////////////////////////////////////////////////////////
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
 * @author jmadison :2015.??.??_????AMPM
 * @author jmadison :2015.10.06_0321PM: --added more documentation.
 */

@Documented //so that java doc is included.
@Inherited //so that annotation is inherited if we extend class using it.
@Retention(RetentionPolicy.RUNTIME) //So can be inspected using reflection.
public @interface Verbatim {
    public String   name();
}//ANNOTATION::END
