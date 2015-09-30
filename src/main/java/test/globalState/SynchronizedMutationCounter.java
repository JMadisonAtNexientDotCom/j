package test.globalState;

/**
 * Used to keep track of when records are created or edited (mutated).
 * Every record in the entire database is given a unique mutation id
 * so that we can debug the logic later when problems occur.
 * @author jmadison
 */
public class SynchronizedMutationCounter {
    
    static private long _nextMutateId = (long)1;
    
    synchronized static public long getNextMutateID(){
        long op = _nextMutateId;
        _nextMutateId++;
        return op;
    }//FUNC::END
}//CLASS::END
