package test.transactions.util;

/**
 * A base class for the cores of ThreadLocal Static Utility classes. -----------
 * The basic idea is to have the static utilities hold state information
 * that is unique to a [thread/session/connection] 
 * @author jmadison : 2015.09.16_0550PM -------------------------------------**/
public class ThreadLocalUtilityBase {
    
    /** An ID to help us debug. Helps us differentiate instances of this
     *  utility running on different threads.  ------------------------------**/
    public int utilityInstanceID = (-1);
    
}//FUNC::END
