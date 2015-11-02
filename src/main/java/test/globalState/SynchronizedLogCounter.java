package test.globalState;

/**
 * A counter for when we make logs in the trans_table.
 * Used for debugging concurrent transactions by giving
 * all logged transactions a definite order.
 * This way, I can see what types of interweaving transactions
 * are causing [database/transaction] [problems/bugs].
 *
 * @author jmadison:2015.09.30
 */
public class SynchronizedLogCounter {
    
    static private long _nextLogId = (long)1;
    
    synchronized static public long getNextLogID(){
        long op = _nextLogId;
        _nextLogId++;
        return op;
    }//FUNC::END
}//CLASS::END
