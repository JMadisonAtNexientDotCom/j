package annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * As I try to figure out how the BeaconLightHouse will guide ships to the
 * correct ports... I come to this solution.
 * Basically, I want to create a lookup table that maps keys to functions.
 * The problem is that functions are NOT first class citizens in JAVA.
 * So how do I do it? Annotate functions with this annotation to enable
 * them to be scanned and put into a lookup table.
 * The lookup table caches the CLASS reference, and then uses reflection
 * to execute methods in the referenced class.
 * 
 * OKAY: The amount of setup is about the same...
 * Going to change portName --> portID
 * Going to use index key instead of string key.
 * Better to have contiguous array lookup table than
 * a crappy hash map that takes about the same effort to configure.
 * 
 * 
 * @author jmadison :2015.10.08_0600PM
 */
@Documented //so that java doc is included.
@Inherited //so that annotation is inherited if we extend class using it.
@Retention(RetentionPolicy.RUNTIME) //So can be inspected using reflection.
public @interface IndexedFunction {
    
    /**
     * Maps the function to a unique short-integer.
     * 32,000 functions should be plenty of space. 
     * @return : See above.
     */
    public short key();
    
}//ANNOTATION::END









////////////////// SCRAP. You can delete this if you want //////////////////////
/** A key used to map this function to an index in a lookup table.
     *  This string should be UNIQUE amongst the entire collection of
     *  annotated methods.
     * @return : A string I guess. How returning works with an interface,
     *           I do not know.
     */
    //public String key();