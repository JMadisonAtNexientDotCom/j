package test.config.debug;

/**
 * A logger object that will only be used when in debug configuration.
 * Because of this, I have named it "DebugConfigLogger".
 * @author jmadison :2015.10.09
 */
public class DebugConfigLogger {
    
    /** master revolving log. **/
    private static String[] _logs = new String[30];
    /** pointer that points to the next index that it will be
     *  inserting a log at. **/
    private static int _nextLogIndex = 0;
    
    /** Adds a message, and give it an object. The object is basically
     *  so we can quickly give it a .this reference and it will resolve
     *  it to a fully qualified class name for the logs.
     * @param obj : Object used to get the class message is coming from.
     * @param msg : Message text to log.
     */
    public static void add(Object obj, String msg){
        add_private(obj.getClass(), msg);
    }//FUNC::END
    
    public static void addFromStatic(Class clazz, String msg){
        add_private(clazz, msg);
    }//FUNC::END
    
    public static void add_private(Class clazz, String msg){
        
        //Knowing what thread we are executing on might
        //be handy for concurrency.
        Thread t = Thread.currentThread();
        String threadName = t.getName();
        
        String l = "";
        l += "LOCATION:[" + clazz.getCanonicalName() + "]";
        l += "MSG:[" + msg + "]";
        l += "THREAD:[" + threadName + "]";
        _logs[_nextLogIndex] = l;
        _nextLogIndex++;
        if(_nextLogIndex >= _logs.length){_nextLogIndex = 0;}
    }//FUNC::END
    
    //TODO: Actual method for reading these messages.
    public static String dumpLogs(){
        
        //Figure out where to start and end scanning of logs.
        //Where i1 == NEWEST LOG.
        //Where i0 == OLDEST LOG.
        //Then work BACKARDS from newest to oldest when printing out.
        //Index to STOP at: (The index where last log was inserted)
        int i1 = (_nextLogIndex > 0 ? _nextLogIndex-1 : _logs.length-1);
        //Index to START at: (The oldest log, which would be one ahead of i1)
        int i0= (i1 != _logs.length-1 ? i1+1 : 0);
        
        String msg = "";
        int ptr = i1 + 1; //prime for entry into loop.
        while(true){
            //Pointer movement and wrapping control:
            ptr--;
            if(ptr < 0){ ptr = _logs.length-1;} //wrap.
            
            //Logic:
            msg += "[LOG_ENTRY::START]";
            msg += _logs[ptr];
            msg += "[LOG_ENTRY::END]";
            
            //Hit the last log?
            if(ptr==i0){break;}
        }//INF LOOP
        
        //return the serialized log:
        return msg;
        
    }//FUNC::END
    
}//CLASS::END
