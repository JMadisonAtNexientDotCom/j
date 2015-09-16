package test.transactions.util.debugUtils.researchTest;

/**
 * A non-static instance containing the guts of our static utility. This hack
 * might make it so we can cleanly create a static utility who's variables
 * are all unique for a given thread.
 * 
 * @author jmadison
 */
public class ThreadSafeStaticUtility_CORE {
    
    /** An ID to help us debug. Helps us differentiate instances of this
     *  utility running on different threads.  ------------------------------**/
    public int utilityInstanceID = (-1);
    
    public int debugCounter = 0;
    
    public void incrimentDebugCounter(){
        debugCounter++;
    }
    
}//CLASS::END
