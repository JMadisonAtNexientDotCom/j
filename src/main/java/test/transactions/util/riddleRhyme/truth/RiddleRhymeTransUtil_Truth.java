package test.transactions.util.riddleRhyme.truth;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import test.entities.TokenTable;
import test.transactions.util.TransUtil;
import test.entities.RiddleRhymeTruthTable;
import test.MyError;

/**
 * CLASS SUMMARY:
 * Utility to query the table that stores riddle+rhyme (question+answer)
 * pairs that are explicitly marked as TRUE.
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
public class RiddleRhymeTransUtil_Truth {
    
     /**
     * Returns TRUE if there is a record with riddleID+rhymeID paired together.
     * Meaning: The riddle+rhyme combo is EXPLICITLY marked as being TRUE.
     * @param riddleID :id of the riddle from the riddle table. (question table)
     * @param rhymeID  :id of the rhyme from the rhyme table. (answer table)
     * @return : True if pair found in table. **/
    public static boolean getIsPairInTable(long riddleID, long rhymeID){
        
        //Make sure we are in a transaction state:
        //And get the session we are in:
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();
        
        //Create a criteria query to find an entry with riddleID and rhymeID
        //Throw an error if the entry exists more than once:
        //Transaction Logic:
        Criteria c = ses.createCriteria(RiddleRhymeTruthTable.class);
        c.add(Restrictions.eq(RiddleRhymeTruthTable.RIDDLE_ID_COLUMN,riddleID)); //
        c.add(Restrictions.eq(RiddleRhymeTruthTable.RHYME_ID_COLUMN ,rhymeID )); //
        List results = c.list();
        
        //Decide what to return based on contents of our results list
        //from the database query://////////////////////////////////////////////
        int numberOfResults = results.size();
        boolean output = false;
        if(0==numberOfResults){ output = false;}else
        if(1==numberOfResults){ output = true; }else
        if(numberOfResults > 1){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            output = true; //<-- technically so. Just happens to exist twice.
            String msg="";
            msg+="DATABASE INTEGRITY ERROR:";
            msg+="RiddleRhyme Pair not unique in Truth Table.";
            throw new MyError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        ////////////////////////////////////////////////////////////////////////
        
        //return the output result:
        return output;
    }//FUNC::END
    
}//CLASS::START
