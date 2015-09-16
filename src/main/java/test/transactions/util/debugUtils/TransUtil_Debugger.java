package test.transactions.util.debugUtils;

import test.transactions.util.TransUtil;

/**-----------------------------------------------------------------------------
 * A debug class for TransUtil.java. I have some questions about concurrency
 * that I need answered. I've read up on it. But it is time to do some actual
 * tests to see what is really going on.
 * @author jmadison      ----------------------------------------------------**/
public class TransUtil_Debugger {
    
    /** Have multiple API calls on server call this method. **/
    public static void incrementSharedCounter(){
        TransUtil.debugSharedThreadCounter++;
    }//FUNC::END
    
    /** Have API call on server that calls this method and sees ----------------
     *  if it's result is the total sum of all incrementSharedCounter()
     *  calls that were executed on all threads.
     * @return : Value of variable I believe to be shared between multiple 
                 threads.  --------------------------------------------------**/
    public static int getSharedCounterValue(){
        return TransUtil.debugSharedThreadCounter;
    }//FUNC::END
    
}//CLASS::END
