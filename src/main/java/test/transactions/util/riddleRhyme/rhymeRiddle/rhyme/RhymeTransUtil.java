package test.transactions.util.riddleRhyme.rhymeRiddle.rhyme;

import java.util.ArrayList;
import java.util.List;
import test.MyError;
import test.config.debug.DebugConfig;
import test.entities.composites.CueCard;
import test.entities.containers.BaseEntityContainer;
import test.entities.tables.RhymeTable;
import test.entities.tables.RiddleTable;
import test.transactions.util.TransUtil;
import test.transactions.util.riddleRhyme.RiddleRhymeTransUtil;
import test.transactions.util.riddleRhyme.joinTables.truth.RiddleRhymeTransUtil_Truth;
import test.transactions.util.riddleRhyme.rhymeRiddle.RRCommonCodeTransUtil;

/**
 * A utility for transactions that only involve the rhyme_table. (answer table)
 * @author jmadison **/
public class RhymeTransUtil {
    
    /**
     * Returns a RhymeTable entity with the given ID. 
     * These IDs should be unique!
     * @param rhymeID
     * @return  **/
    public static BaseEntityContainer getRhymeByID(long rhymeID){
        //make sure we are in a transaction state:
        //and get the session object.
        TransUtil.insideTransactionCheck();
       
        //HeavyLifting function. Common code between RhymeTable and RiddleTable:
        BaseEntityContainer op;
        op = RRCommonCodeTransUtil.getTableEntityByID
                                                  (RhymeTable.class, rhymeID);
        
        //ERROR CHECK: Make sure we have the correct entity type:
        if(op.exists)//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        {//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            if(false==(op.entity instanceof RhymeTable))
            {
                String msg="";
                msg+="[Entity is not instance of correct table:]";
                msg+="[" + RhymeTable.class.getSimpleName() + "]";
                msg+="[Error is mostly in logic of this function.]";
                msg+="[Check that the correct class references were used.]";
                throw new MyError(msg);
            }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //return the output:
        return op;
        
    }//FUNC::END
    
    /**
     * Returns an array of Rhymes (answers) for a given riddleID (question)
     * Debatable whether this function belongs in this class, or in
     * the RiddleRhymeTransUtil since this operation involves logic with
     * both riddles and rhymes. BUT IT RETURNS JUST Rhymes (answers)
     * 
     * DESIGN NOTE:
     * Was originally named "getRiddleChoices" but the emphasis was on the
     * riddles and sounded too much like "choose a riddle"
     * When we are trying to make a collection of RHYME to choose from.
     * So I chose a more generic sounding "makeChoicesToChooseFrom"
     * which doesn't have the mis-directing quality of the other name.
     * 
     * @param riddleID :ID of the riddle.
     * @param numberOfChoices :How many choices to return?
     * @param numberOfTruthsDesired :How many choices should be correct answers?
     *                               Must work with data we have, so may NOT hit
     *                               the target you want. But will try.
     * @return                                                               **/
    public static List<RhymeTable> makeChoicesToChooseFrom
                (long riddleID, int numberOfChoices, int numberOfTruthsDesired){
              
        //error check: Make sure we are in a transaction state:
        TransUtil.insideTransactionCheck();
                    
        //Error Check: If numberOfTruths is GREATER than number of choices...
        //We could handle that if we wanted... But... It would be a hassle to
        //handle. We should consider numberOfChoices < numberOfTruths to be
        //an ERROR:
        if(numberOfChoices < numberOfTruthsDesired){////////////////////////////
            //Example illustrating why this is an error:
            //Imagine a question where 10 out of 5 questions are correct.
            throw new MyError("ERROR:You are asking for an improper fraction.");
        }///////////////////////////////////////////////////////////////////////
                         
        //Attempt to get the number of Rhymes (answers) that are true
        //for the riddleID (question being asked)
        List<RhymeTable> rhymesThatAre_TRUTH;
        rhymesThatAre_TRUTH = RiddleRhymeTransUtil.getRhymesThatAre_TRUTH
                                              (riddleID, numberOfTruthsDesired);
        //The remainder of the possible answers will be false:
        int actualNumberOfTruths = rhymesThatAre_TRUTH.size();
        int numberOfWrongAnswersNeeded = numberOfChoices - actualNumberOfTruths;
        
        
        List<RhymeTable> rhymesThatAre_WRONG;
        rhymesThatAre_WRONG = RiddleRhymeTransUtil.getRhymesThatAre_WRONG
                                         (riddleID, numberOfWrongAnswersNeeded);
        int actualNumberOfWrongs = rhymesThatAre_WRONG.size();
        int allRhymesTotal = actualNumberOfTruths + 
                             actualNumberOfWrongs;
        
	//Error check: EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        //None of the arrays should EXCEED what is asked for.               //EE
        //However, they can underflow. If the data isn't there in the tables//EE
        //then that shouldn't be considered an error. Just a problem.       //EE
        //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        if(actualNumberOfTruths > numberOfTruthsDesired)                    //EE
        {                                                                   //EE
            throw new MyError("got more truths than asked for!");           //EE
        }                                                                   //EE
                                                                            //EE
        if(actualNumberOfWrongs > numberOfWrongAnswersNeeded)               //EE
        {                                                                   //EE
            throw new MyError("got more wrongs than asked for!");           //EE
        }                                                                   //EE
                                                                            //EE
        if(allRhymesTotal > numberOfChoices)                                //EE
        {                                                                   //EE
            String msg = "";                                                //EE
            msg += "[total number of choices to return]";                   //EE
            msg += "[exceeds what was asked for.]";                         //EE
            throw new MyError(msg);                                           //EE
        }                                                                   //EE
        //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        
        //Pack the contents of both arrays together, then shuffle:
        List<RhymeTable> op;
        op = new ArrayList<RhymeTable>();
        op.addAll(rhymesThatAre_TRUTH);
        op.addAll(rhymesThatAre_WRONG);
        if(op.size() != numberOfChoices)
        {   //always do error check when you are uncertain of the results
            //of an operation. Example: Functions you've never used before.
            throw new MyError("Concatination did not work as expectd.");
        }/////
        
        //Return the Rhyme
        return op; 
        
    }//FUNC::END
}//CLASS::END
