package test.transactions.util.riddleRhyme.joinTables;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import test.MyError;
import test.entities.bases.RiddleRhymeJoinTableBaseEntity;
import test.entities.tables.RiddleRhymeTruthTable;
import test.transactions.util.TransUtil;

/**
 * Common operations that are performed both on the
 * truth-table and the wrong-table.
 * @author jmadison
 */
public class RiddleRhymeJoinTablesTransUtil {
    
    /**
     * Returns TRUE if the pair is found in the table.
     * Will throw error if pair found more than ONCE.
     * @param riddleID :The ID of the riddle (question) in your pair.
     * @param rhymeID  :The ID of the rhyme  (answer)   in your pair.
     * @param joinTableClass: The truth-table class or wrong-table class.
     *                        Needs to be a table that has a riddleID and
     *                        a rhymeID.
     * @return :True if pair exists in joinTableClass, false if not.
     */
    public static boolean getIsPairInTable
                           (long riddleID, long rhymeID, Class joinTableClass ){                  
        //Make sure we are in a transaction state:
        //And get the session we are in:
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();
        
        //Create a criteria query to find an entry with riddleID and rhymeID
        //Throw an error if the entry exists more than once:
        //Transaction Logic:
        Criteria c = ses.createCriteria(joinTableClass);
        c.add(Restrictions.eq(RiddleRhymeJoinTableBaseEntity.RIDDLE_ID_COLUMN,riddleID)); //
        c.add(Restrictions.eq(RiddleRhymeJoinTableBaseEntity.RHYME_ID_COLUMN ,rhymeID )); //
        List results = c.list();
        
        //Decide what to return based on contents of our results list
        //from the database query://////////////////////////////////////////////
        int numberOfResults = results.size();
        boolean output = false;
        if(0==numberOfResults){ output = false;}else
        if(1==numberOfResults){ output = true; }else
        if(numberOfResults > 1){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            output = true; //<-- technically so. Just happens to exist twice.
            String tableName = joinTableClass.getSimpleName();
            String msg="";
            msg+="DATABASE INTEGRITY ERROR:";
            msg+="RiddleRhyme Pair not unique in table:" + tableName;
            throw new MyError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        ////////////////////////////////////////////////////////////////////////
        
        //return the output result:
        return output;
                              
    }//FUNC::END
    
}//CLASS::END
