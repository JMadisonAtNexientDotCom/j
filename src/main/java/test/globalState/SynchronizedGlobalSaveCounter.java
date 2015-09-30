package test.globalState;

/**
 * Used to keep track of when records are created or edited (mutated)
 * Every record in the entire database is given a unique mutation id
 * so that we can debug the logic later when problems occur.
 * @author jmadison :2015.09.30
 */
public class SynchronizedGlobalSaveCounter {
    
    static private long _nextGlobalSaveID = (long)1;
    
    synchronized static public long getNextGlobalSaveID(){
        long op = _nextGlobalSaveID;
        _nextGlobalSaveID++;
        return op;
    }//FUNC::END
}//CLASS::END
