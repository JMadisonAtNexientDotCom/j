package test.transactions.util.debugUtils.researchTest;

/**
 * THIS IS HOW WE WILL DO IT!
 * Though for TransUtil.java, we might want a less verbose call.
 * Like TransUtil.i.someFunction() rather than TransUtil.getCore.someFunction();
 * 
 * 
 * An attempt to make a thread-safe static utility where all static variables
 * have UNIQUE INSTANCE for each thread this utility is being used on.
 * @author jmadison
 */
public class ThreadSafeStaticUtility {
    
    /** The next instance ID to give to the next new utility. **/
    private static int _nextUtilityInstanceID = 0;
    
    /** One instance for all threads. Used for debugging so we can confirm
     *  that API calls are indeed using different instances of static utility
     *  core on different threads.
     * @return : An integer to use for identification -----------------------**/
    private static int getNextUtilityInstanceID(){
        int op = _nextUtilityInstanceID;
        _nextUtilityInstanceID ++;
        return op;
    }
    
    private static ThreadLocal<ThreadSafeStaticUtility_CORE> _core =
                                new ThreadLocal<ThreadSafeStaticUtility_CORE>();
    
    /** If all externally ~accessable~ components need to be accessed via
     *  the getCore() function. Maybe getCore() should be called "i" for
     *  "instance core". Alternatively we could individually wrap every single
     *  method. However, that seems like more maintenance than it is worth.
     * 
     *  If utility methods are called with UtilityName.i.function(),
     *  then we know it is a ThreadLocal static utility.
     *  Sounds good to me!
     * 
     * @return  -------------------------------------------------------------**/
    public static ThreadSafeStaticUtility_CORE getCore(){
        ThreadSafeStaticUtility_CORE op = _core.get();
        if(null == op){
            op = new ThreadSafeStaticUtility_CORE();
            op.utilityInstanceID = getNextUtilityInstanceID();
            _core.set( op );
        }
        
        return op; 
    } 
    
}//CLASS::END
