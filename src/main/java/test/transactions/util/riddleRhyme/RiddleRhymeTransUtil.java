package test.transactions.util.riddleRhyme;

import java.util.ArrayList;
import java.util.List;
import test.MyError;
import test.config.debug.DebugConfig;
import test.dbDataAbstractions.entities.composites.CueCard;
import test.dbDataAbstractions.entities.containers.BaseEntityContainer;
import test.dbDataAbstractions.entities.tables.RhymeTable;
import test.dbDataAbstractions.entities.tables.RiddleTable;
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
    
    /**-------------------------------------------------------------------------
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
     * @return : A CueCard that is filled out to spec.
     ------------------------------------------------------------------------**/
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
        List<RhymeTable> rhymeList = RhymeTransUtil.makeChoicesToChooseFrom
                                    (riddleID, numberOfChoices, numberOfTruths);
        
        //Pack the rhymeList (possible answers) into our output container:
        op.quips = rhymeList;
        
        //return output:
        return op;
        
    }//FUNC::END
              
   /**--------------------------------------------------------------------------
    * Combines getRhymesThatAre_TRUTH and getRhymesThatAre_WRONG into
    * one function that gives you a collection of both.
    * @param riddleID :The riddleID these these [Rhymes/Answers] are addressing.
    * @param numWantedTrue :How many [true/correct] [rhymes/answers] do you
    *                       want? You may get less if information not available.
    * @param numWantedFalse:How many [FALSE/WRONG] [rhymes/answers] do you
    *                       want? You may get less if information not available.
    * @return :A collection consisting of a mix or good-+-bad 
    *         [Rhymes/responses/answers]. Represented as entities.
    *------------------------------------------------------------------------**/
    public static List<RhymeTable> getRhymesTrueFalse
                         (long riddleID, int numWantedTrue, int numWantedFalse){
                             
       //make sure we are in transaction state:
       TransUtil.insideTransactionCheck();
           
       //BUGFIX: WRONG/TRUTH were transposed:
       List<RhymeTable> truth = getRhymesThatAre_TRUTH(riddleID,numWantedTrue);
       List<RhymeTable> wrong = getRhymesThatAre_WRONG(riddleID,numWantedFalse);
       
       //If something goes wrong, god help your string function searching works:
       int actual_wrong = wrong.size();
       int actual_truth = truth.size();
       if(actual_wrong > numWantedFalse){throw new MyError("[424jkl234jl2f]");}
       if(actual_truth > numWantedTrue ){throw new MyError("[6654ggdsfgf34]");}
       
       //Concat the lists together and output:
       List<RhymeTable> tf = new ArrayList<RhymeTable>();
       tf.addAll(wrong);
       tf.addAll(truth);
       int actual_sum = actual_wrong + actual_truth;
       if(actual_sum != tf.size()){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
           String msg = "Concat did not work as expected.";
           msg+="RiddleRhymeTransUtil.java :: getRhymesTrueFalse(...)";
           throw new MyError(msg);
       }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
       
       //return the list of Rhymes(answers) to the riddle supplied.
       //Some being correct solutions, some being invalid solutions.
       return tf;
       
    }//FUNC::END
                       
    /**
     * Get [CORRECT/TRUTH] solutions for a given riddle 
     * @param riddleID :The unique identifier for the riddle
     * @param numberOfTruthsDesired : How many rhymes do you want returned?
     * @return :The amount requested or LESS. But never more. **/
    public static List<RhymeTable> getRhymesThatAre_TRUTH
                                     (long riddleID, int numberOfTruthsDesired){
            
        //ERROR CHECK: Make sure we are in a transaction state:
        TransUtil.insideTransactionCheck();
                                         
        /** op == "output" **/
        List<RhymeTable> op;                                 

        //To find TRUE solutions, find matches in the TRUTH table:
        op = RiddleRhymeTransUtil_Truth.makeMatches_ForRiddle_TRUTH
                                              (riddleID, numberOfTruthsDesired);

        if(op.size() > numberOfTruthsDesired){ //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            
            String msg = "";
            msg+="Exceeded number of TRUTHS desired";
            msg+="[numberOfTruthsDesired==[" +numberOfTruthsDesired + "]]";
            msg+="[op.size() ==" + op.size() + "]";
            
            //list out contents of op:
            msg+="[Output contents:]";
            int len = op.size();
            for(int i = 0; i < len; i++)
            {
                msg+="[op[i].getId()==[" + op.get(i).getId()+ "]]";
                msg+="[op[i].getText()==[" + op.get(i).getText() + "]]";
            }
            
            throw new MyError(msg);
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
                                         
        //ERROR CHECK: Make sure we are in a transaction state:
        TransUtil.insideTransactionCheck();
        
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
          
    /**-------------------------------------------------------------------------
     * Compares the set of rhymeIDS against the riddleID and returns
     * how many correct solutions were in the set.
     * @param riddleID :ID of riddle we are addressing.
     * @param rhymeIDS :Our rhymes that are attempting to answer riddle.
     * @return : How many in set were CORRECT -------------------------------**/
    public static int getNumberOf_TRUTH(long riddleID, List<Long> rhymeIDS){
        
        //Call function with "true" to say that we want to collect a talley
        //of how many are true:
        int talleyOutput;
        talleyOutput = getNumberOf_EITHER(riddleID,rhymeIDS,true);
        
        //return the talley:
        return talleyOutput;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Compares the set of rhymeIDS against the riddleID and returns
     * how many [WRONG/BAD/INCORRECT] solutions were in the set.
     * @param riddleID :ID of riddle we are addressing.
     * @param rhymeIDS :Our rhymes that are attempting to answer riddle.
     * @return : How many in set were [WRONG/BAD/INCORRECT] -----------------**/
    public static int getNumberOf_WRONG(long riddleID, List<Long> rhymeIDS){
        
        //Call function with "false" to say that we want to collect a talley
        //of how many are false:
        int talleyOutput;
        talleyOutput = getNumberOf_EITHER(riddleID,rhymeIDS,false);
        
        //return the talley:
        return talleyOutput;
    }//FUNC::END
    
    /**
     * COMMON logic between getNumberOf_WRONG and getNumberOf_TRUTH
     * @param riddleID :ID of riddle we are addressing.
     * @param rhymeIDS :Our rhymes that are attempting to answer riddle.
     * @param resultTypeWanted_tf: Do we want to find the number of WRONG
     *                             or the number of TRUTH?
     *                             true --> #TRUTH#
     *                             false--> #WRONG#
     * @return :Number of TRUTHS or WRONGS, depending on config parameter. **/
    public static int getNumberOf_EITHER
        (long riddleID, List<Long> rhymeIDS, boolean resultTypeWanted_tf){
           
        //Variable declaration:
        int talleyOutput = 0;
        int result_code;
        boolean result;
        long currentRhymeID;
        
        //Main Logic Loop:
        int len = rhymeIDS.size();
        for(int i = 0; i < len; i++){
            currentRhymeID = rhymeIDS.get(i);
            result_code = getIsCorrect(riddleID, currentRhymeID);
            result = resultCodeToBoolean(result_code);
            if(resultTypeWanted_tf==result){ talleyOutput++;}
        }//Next Rhyme ID.
        
        //return the talley:
        return talleyOutput;
    }//FUNC::END
       
    /**-------------------------------------------------------------------------
     * JMadison Note: 
     * Feel like this code should NOT live in RiddleRhymeTransUtil.java.
     * 
     * Converts result code into boolean.
     * @param resultCode :A code representing a trinary value. 
     *                    true,false,undefined.
     * @return :True,False, or ERROR if undefined. --------------------------**/
    private static boolean resultCodeToBoolean(int resultCode){//---------------
        if(resultCode < 0){ return false;}else
        if(resultCode > 0){ return true; }
        else{//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            //This probably means the answer is in BOTH
            //the TRUTH table and the WRONG table.
            throw new MyError("[grading an undefined result type.]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
    }//-------------------------------------------------------------------------
                       
}//CLASS::END
