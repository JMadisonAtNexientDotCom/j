package test.transactions.util.riddleRhyme;

import java.util.ArrayList;
import java.util.List;
import test.MyError;
import test.config.debug.DebugConfig;
import test.entities.composites.CueCard;
import test.entities.containers.BaseEntityContainer;
import test.entities.tables.RhymeTable;
import test.entities.tables.RiddleTable;
import test.transactions.util.TransUtil;
import test.transactions.util.riddleRhyme.joinTables.truth.RiddleRhymeTransUtil_Truth;
import test.transactions.util.riddleRhyme.joinTables.wrong.RiddleRhymeTransUtil_Wrong;
import test.transactions.util.riddleRhyme.rhymeRiddle.rhyme.RhymeTransUtil;
import test.transactions.util.riddleRhyme.rhymeRiddle.riddle.RiddleTransUtil;

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
    
    /**
     * Creates a CueCard (question with answers) that 
     * represents a question on the game show (test being taken).
     * @param riddleID        :The ID of the riddle.
     * @param numberOfChoices :How many choices you want this riddle to have?
     * @param numberOfTruths  :How many of those choices are true?
     *                         Will TRY to hit the target you specify, however,
     *                         it has to work with the data that exists.
     *                         If the Riddle has no possible solution, then
     *                         the actual numberOfTruths will be ZERO regardless
     *                         of what was supplied.
     */
    public static CueCard makeFilledOutCueCard
                       (long riddleID, int numberOfChoices, int numberOfTruths){
                           
        //make sure we are in a transaction state, since we must fetch data:
        TransUtil.insideTransactionCheck();
        
        //get the riddle entry using the id:
        RiddleTable rt;
        BaseEntityContainer rtCon = RiddleTransUtil.getRiddleByID(riddleID);
        if(false == rtCon.exists){
            throw new MyError("riddle of this id does not exist");
        }else{
            rt = (RiddleTable)(rtCon.entity);
        }////
        
        
        //RAAAAWWWWEEEERRR!!!!            
        CueCard op = CueCard.makeCueCard_WithJest_And_EmptyQuips(rt);
        
        //create a choice list:
        List<RhymeTable> rhymeList = RhymeTransUtil.makeChoicesToChooseFrom(riddleID, numberOfChoices, numberOfTruths);
        
        //Pack the rhymeList (possible answers) into our output container:
        op.quips = rhymeList;
        
        //return output:
        return op;
        
    }//FUNC::END
                  
   
                       
    /**
     * Get [CORRECT/TRUTH] solutions for a given riddle 
     * @param riddleID :The unique identifier for the riddle
     * @param numberOfTruthsDesired : How many rhymes do you want returned?
     * @return :The amount requested or LESS. But never more. **/
    public static List<RhymeTable> getRhymesThatAre_TRUTH
                                     (long riddleID, int numberOfTruthsDesired){
        /** op == "output" **/
        List<RhymeTable> op;                                 

        //To find TRUE solutions, find matches in the TRUTH table:
        op = RiddleRhymeTransUtil_Truth.makeMatches_ForRiddle_TRUTH
                                              (riddleID, numberOfTruthsDesired);

        if(op.size() > numberOfTruthsDesired){ //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            throw new MyError("Exceeded number of TRUTHS desired");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE

        return op;
        
    }//FUNC::END     
            
    /**
     * Get [INCORRECT/WRONG] solutions for a given riddle 
     * @param riddleID :The unique identifier for the riddle.
     * @param numberOfWrongsDesired : How many rhymes do you want returned?
     * @return :The amount requested or LESS. But never more. **/
    public static List<RhymeTable> getRhymesThatAre_WRONG
                                     (long riddleID, int numberOfWrongsDesired){
        
        /** op == "output" **/
        List<RhymeTable> op;                                   

        //To find WRONG solutions, find ANTI-matches in the TRUTH table.
        op = RiddleRhymeTransUtil_Truth.makeMatches_ForRiddle_WRONG
                                               (riddleID, numberOfWrongsDesired);

        if(op.size() > numberOfWrongsDesired){ //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
             throw new MyError("Exceeded number of WRONGS desired");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE

        return op;
      
    }//FUNC::END       
                       
}//CLASS::END
