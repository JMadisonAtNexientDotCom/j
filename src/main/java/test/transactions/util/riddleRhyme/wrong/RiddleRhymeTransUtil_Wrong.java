package test.transactions.util.riddleRhyme.wrong;

/**
 * CLASS SUMMARY:
 * Utility to query the table that stores riddle+rhyme (question+answer)
 * pairs that are explicitly marked as WRONG.
 * 
 * INTENTIONALLY BROKEN CONVENTION: BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB
 * //BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB
 * According to my convention this class should be:                       BBBBBB
 * [tableNameInCamelCase] minus the word: "table" plus "TransUtil"        BBBBBB
 * However:                                                               BBBBBB
 * RiddleRhymeWrongTransUtil.java &&                                      BBBBBB
 * RiddleRhymeTruthTransUtil.java                                         BBBBBB
 * Look too similar.                                                      BBBBBB
 *                                                                        BBBBBB
 * Putting the differences at the END of the word and separating that     BBBBBB
 * difference with an underscore make it easier to read.                  BBBBBB
 * //BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB
 * 
 * @author jmadison
 */
public class RiddleRhymeTransUtil_Wrong {
    
    /**
     * Returns TRUE if there is a record with riddleID+rhymeID paired together.
     * Meaning: The riddle+rhyme combo is EXPLICITLY marked as being WRONG.
     * @param riddleID :id of the riddle from the riddle table. (question table)
     * @param rhymeID  :id of the rhyme from the rhyme table. (answer table)
     * @return : True if pair found in table. **/
    public static boolean getIsPairInTable(long riddleID, long rhymeID){
        //TODO: Actual code.
        return false;
    }//FUNC::END
    
}//CLASS::END
