package test.transactions.util.debugUtils.researchTest;

/**
 * An attempt to make a thread-safe static utility where all static variables
 * have UNIQUE INSTANCE for each thread this utility is being used on.
 * @author jmadison
 */
public class ThreadSafeStaticUtility {
    
    public static final ThreadLocal<ThreadSafeStaticUtility_CORE> core = 
                                new ThreadLocal<ThreadSafeStaticUtility_CORE>();
    
}//CLASS::END
