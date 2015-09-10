package test.transactions.util.riddleRhyme;

import test.transactions.util.TransUtil;
import test.transactions.util.riddleRhyme.truth.RiddleRhymeTransUtil_Truth;
import test.transactions.util.riddleRhyme.wrong.RiddleRhymeTransUtil_Wrong;

/**
 * Utility that is responsible for transactions that involve both the
 * RiddleTable.java (question table) and the RhymeTable.java (answer table)
 * @author jmadison
 */
public class RiddleRhymeTransUtil {
    
    /** Positive numbers denote TRUE **/
    public static final int IS_TRUTH  = 1; 
    /** Negative numbers denote FALSE **/
    public static final int IS_WRONG = -1;
    /** Zero denotes UNDEFINED (Error in table data) **/
    public static final int IS_UNDEFINED = 0;
    
    /**
     * For a given riddleID (a question) paired with a rhymeID (an answer)
     * Is this judged as correct or not?
     * @param riddleID :The unique id of a riddle from RiddleTable.java
     * @param rhymeID  :The unique id of a rhyme from RhymeTable.java
     * @return : FALSE == -1
     *           TRUE  ==  1
     *           UNDEFINED == 0 
     *           For a riddleID+rhymeID combo that results in return of
     *           undefined, we would like to:
     *           1. NOT give points to test taker.
     *           2. NOT subtract points from test taker.
     *           3. Detect this problem and fix it.
     *              UNDEFINED usually means to graders could not agree on if
     *              the question is true or false. And the riddleID+rhymeID
     *              pair is found in both our truth-table and our wrong-table
     * 
     * QUESTION: Why did I choose (-1) for FALSE instead of ZERO?
     * ANSER: Chose zero for undefined because erroring code
     * is more likely to return 0 than any other number.
     * Thus breaking convention of true == 1, false == 0
     * In order to catch errors.
     * 
     * Think of it like this, positive and negatives are OPPOSITES.
     * True and False are OPPOSITES. UNDEFINED is in a grey area in the middle.
     */
    public static int getIsCorrect(long riddleID, long rhymeID){
        
        //Make sure we are in a transaction state:
        TransUtil.insideTransactionCheck();
        
        boolean isPairInTable_truth;
        boolean isPairInTable_wrong;
        
        //Check the truth_table:
        isPairInTable_truth = 
        RiddleRhymeTransUtil_Truth.getIsPairInTable(riddleID,rhymeID);
        
        //Check the wrong_table:
        isPairInTable_wrong = 
        RiddleRhymeTransUtil_Wrong.getIsPairInTable(riddleID,rhymeID);
        
        /** Is question-answer pair found in BOTH tables? **/
        boolean isPairInTable_both = (isPairInTable_truth &&
                                      isPairInTable_wrong  );
        /** Is question-answer pair found in NIETHER table? **/
        boolean isPairInTable_none = (false==(isPairInTable_truth  ||
                                              isPairInTable_wrong ));
        /** AKA: "output" **/
        int op = IS_UNDEFINED; //<-- to make compiler happy.
        if(isPairInTable_both) { op = IS_UNDEFINED;}else //Error in Data.
        if(isPairInTable_none) { op = IS_WRONG;    }else //implicitly wrong.
        if(isPairInTable_wrong){ op = IS_WRONG;    }else //EXPLICITLY wrong.
        if(isPairInTable_truth){ op = IS_TRUTH;    }     //explicitly CORRECT.
          
        //return the true/false/undefined code:
        return op; //<--says I have syntax error. Intellisense hates me.
        
    }//FUNC::END
    
}
