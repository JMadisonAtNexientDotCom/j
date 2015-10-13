package utils;

/**
 *
 * @author jmadison
 */
public class TimeMathUtil {
    
    /** Converts minutes into milliseconds. **/
    public static long minutesToMS(int numMinutes){
        long op;
        op = numMinutes; //set first, to avoid overflow.
        op = op * 60000;
        return op;
    }//FUNC::END
    
}//CLASS::END
