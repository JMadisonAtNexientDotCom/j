package test.transactions.util.debugUtils.researchTest;

/**
 * An attempt to make a thread-safe static utility where all static variables
 * have UNIQUE INSTANCE for each thread this utility is being used on.
 * @author jmadison
 */
public class ThreadSafeStaticUtility {
    
    //This seems not to work. Trying another approach.
    //public static final ThreadLocal<ThreadSafeStaticUtility_CORE> core = 
    //                            new ThreadLocal<ThreadSafeStaticUtility_CORE>();
    
    private static ThreadLocal<ThreadSafeStaticUtility_CORE> _core;
    
    public static ThreadSafeStaticUtility_CORE getCore(){
        ThreadSafeStaticUtility_CORE op = _core.get();
        if(null == op){
            _core.set( op = new ThreadSafeStaticUtility_CORE());
        }
        
        return op; 
    } 
    
}//CLASS::END
