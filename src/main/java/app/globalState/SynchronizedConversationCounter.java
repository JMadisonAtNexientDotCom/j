package app.globalState;

/**
 * Every time a new conversation starts on a thread, this utitility will
 * be responsible for giving that conversation a unique ID.
 * 
 * You could also think of it as a unique ID associated with
 * a hibernate session.
 * 
 * In terms of this application, a conversation is everything that
 * happens between TransUtil.enterTransaction() and TransUtil.exitTransaction()
 * 
 * What is this for?
 * Debugging concurrent transactions.
 * 
 * @author jmadison : 2015.09.30
 */
public class SynchronizedConversationCounter {
    
    //OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
    static private long _nextOpenID = (long)1;
    /** A unique global id telling us the ORDER in which this conversation
     *  ENDED compared to all other conversations. **/
    synchronized static public long getNextOpenID(){
        long op = _nextOpenID;
        _nextOpenID++;
        return op;
    }//FUNC::END
    //OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
    
    //CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
    static private long _nextCloseID = (long)1;
    /** A unique global id telling us the ORDER in which this conversation
     *  BEGAN compared to all other conversations. **/
    synchronized static public long getNextCloseID(){
        long op = _nextCloseID;
        _nextCloseID++;
        return op;
    }//FUNC::END
    //CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
    
}//FUNC::END
