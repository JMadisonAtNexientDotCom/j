package test.transactions.util.riddleRhyme.rhyme;

import java.util.ArrayList;
import test.MyError;
import test.config.debug.DebugConfig;
import test.entities.composites.RiddleWithPossibleRhymes;
import test.entities.tables.RhymeTable;

/**
 * A utility for transactions that only involve the rhyme_table. (answer table)
 * @author jmadison **/
public class RhymeTransUtil {
    
    /**
     * Returns an array of Rhymes (answers) for a given riddleID (question)
     * Debatable whether this function belongs in this class, or in
     * the RiddleRhymeTransUtil since this operation involves logic with
     * both riddles and rhymes. BUT IT RETURNS JUST Rhymes (answers)
     * @param riddleID :ID of the riddle.
     * @param numberOfChoices :How many choices to return?
     * @param numberOfTruths  :How many choices should be correct answers?
     *                         Must work with data we have, so may NOT hit
     *                         the target you want. But will try.
     * @return                                                               **/
    public static ArrayList<RhymeTable> getRiddleChoices
                       (long riddleID, int numberOfChoices, int numberOfTruths){
                  
        //TODO::TODO::TODO::TODO::TODO::TODO::TODO::TODO::TODO::TODO::TODO::TODO
        if(DebugConfig.isDebugBuild)
        {
            throw new MyError("[TODO:RhymeTransUtil.getRiddleChoices]");
        }
        return null;
        //TODO::TODO::TODO::TODO::TODO::TODO::TODO::TODO::TODO::TODO::TODO::TODO
        
    }//FUNC::END
}//CLASS::END
