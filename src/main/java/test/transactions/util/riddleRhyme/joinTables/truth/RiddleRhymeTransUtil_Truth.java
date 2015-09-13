package test.transactions.util.riddleRhyme.joinTables.truth;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import test.MyError;
import test.dbDataAbstractions.entities.containers.BaseEntityContainer;
import test.dbDataAbstractions.entities.tables.RhymeTable;
import test.dbDataAbstractions.entities.tables.RiddleRhymeTruthTable;
import test.dbDataAbstractions.entities.tables.TokenTable;
import test.transactions.util.TransUtil;
import test.transactions.util.riddleRhyme.joinTables.RiddleRhymeJoinTablesTransUtil;
import test.transactions.util.riddleRhyme.rhymeRiddle.rhyme.RhymeTransUtil;

/**
 * CLASS SUMMARY:
 * Utility to query the table that stores riddle+rhyme (question+answer)
 * pairs that are explicitly marked as TRUE.
 * 
 * DESIGN NOTE: Why I am not currently pulling random questions from any tables:
 * 1. Random pulling is more work. We just want RUNNING+WORKING for now.
 * 2. Random wrong answers will probably be obvious. What we want is a
 *    FAKER table. A table full of answers that SOUND correct.
 *    But are not correct.
 * 
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
    
    /** Used to indicate we want to return non-matching records.
     *  AKA: We want WRONG rhymes (answers) returned. **/
    private static final boolean WRONG_MATCH = false;
    
    /** Used to indicate we want to return matching/correct records.
     *  AKA: We want CORRECT rhymes (answers) returned. **/
    private static final boolean MATCH_TRUE = true;
    
     /**
     * Returns TRUE if there is a record with riddleID+rhymeID paired together.
     * Meaning: The riddle+rhyme combo is EXPLICITLY marked as being TRUE.
     * @param riddleID :id of the riddle from the riddle table. (question table)
     * @param rhymeID  :id of the rhyme from the rhyme table. (answer table)
     * @return : True if pair found in table. **/
    public static boolean getIsPairInTable(long riddleID, long rhymeID){
        
        //ERROR CHECK: Make sure we are in a transaction state:
        TransUtil.insideTransactionCheck();
        
        Class joinTableToQuery = RiddleRhymeTruthTable.class;
        boolean op = RiddleRhymeJoinTablesTransUtil.getIsPairInTable
                                          (riddleID, rhymeID, joinTableToQuery);
        return op;
    }//FUNC::END
    
   
    /**
     * Pairs riddle (question) with [VALID/CORRECT/TRUE] rhymes (answers).
     * @param riddleID :The Riddle you want correct responses to.
     * @param amountToReturn :How many rhyme choices to return?
     * @return : The amount asked for, or less. But never more.              **/
    public static List<RhymeTable> makeMatches_ForRiddle_TRUTH
                                            (long riddleID, int amountToReturn){
        //ERROR CHECK: Make sure we are in a transaction state:
        TransUtil.insideTransactionCheck();
        
        //Make CORRECT RHYMES (Correct Answers):
        List<RhymeTable> op;
        op = makeMatches_ForRiddle(riddleID, amountToReturn, MATCH_TRUE);
        return op;
    }//END::FUNC
    
 
    /**
     * Pairs riddle (question) with [INVALID/INCORRECT/FALSE] rhymes (answers).
     * @param riddleID :The Riddle you want invalid responses to.
     * @param amountToReturn :How many rhyme choices to return?
     * @return : The amount asked for, or less. But never more.              **/
    public static List<RhymeTable> makeMatches_ForRiddle_WRONG
                                            (long riddleID, int amountToReturn){
        //ERROR CHECK: Make sure we are in a transaction state:
        TransUtil.insideTransactionCheck();
        
        //Make WRONG RHYMES (Wrong Answers):
        List<RhymeTable> op;
        op = makeMatches_ForRiddle(riddleID, amountToReturn, WRONG_MATCH);
        return op;
    }//FUNC::END
                          

    /**
     * Takes a riddle and returns MATCHES or ANTI-MATCHES
     * @param riddleID : The riddle we want to find matches for.
     * @param amountToReturn :The amount of matches to return.
     * @param shouldMatch: TRUE == return MATCHES.
     *                     FALSE== return ANTI-MATCHES.
     * @return :A list of RhymeTable entities that represent
                responses to the riddleID supplied. 
                Responses are either ALL CORRECT or ALL WRONG.
                Depending on value of shouldMatch argument.                  **/
    private static List<RhymeTable> makeMatches_ForRiddle
                    (long riddleID, int amountToReturn, boolean shouldMatch){            
        //ERROR CHECK: Make sure we are in a transaction state:
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();
        
        //BUGFIX: Looks like "setMaxResults(0)" does NTO work with hibernate.
        //So if amountToReturn==0, we will just return empty list.
        if(amountToReturn == 0){ ///////////////////////////////////////////////
            List<RhymeTable> emptyList = new ArrayList<RhymeTable>();
            return emptyList;
        }else
        if(amountToReturn < 0){
            throw new MyError("Asking to return negative amount of matches.");
        }///////////////////////////////////////////////////////////////////////
        
        //Use criteria to get list of random results that DO NOT MATCH the
        //riddleID supplied: Use "ne" (NOT EQUALS) restriction.
        String riddleIDColumn = RiddleRhymeTruthTable.RIDDLE_ID_COLUMN;
        Criteria criteria = ses.createCriteria(RiddleRhymeTruthTable.class);
        SimpleExpression matchingRestriction;
        if(true == shouldMatch){  //use EQUAL restriction:  ////////////////////
            matchingRestriction = Restrictions.eq(riddleIDColumn, riddleID);
        }else
        if(false == shouldMatch){ //use NOT-EQUAL restriction:
            matchingRestriction = Restrictions.ne(riddleIDColumn, riddleID);
        }else{
            String msg = "";
            msg+="Bool can only be true or false.";
            msg+="So else block should never be called.";
            throw new MyError(msg);
        }///////////////////////////////////////////////////////////////////////
        
        //Add the matching criteria and return a list of results:
        criteria.add(matchingRestriction);
        criteria.setMaxResults(amountToReturn);
        List<RiddleRhymeTruthTable> joinTableResults = criteria.list();
        
        //Convert results:
        List<RhymeTable> op = listOfRiddleRhyme_to_rhyme(joinTableResults);
        
        //return results:
        return op;      
                        
    }//FUNC::END
              
    
                                            
    /**
     * Resolves our RiddleRhyme (q+a) Truth table entries to rhymes (answers) 
     * @param joinTableEntries : The entries of our TRUTH join table.
     * @return : RhymeTable entries associated with the join table entries  
     
       DESIGN NOTE: All of the entries in our list should be able to be 
     *              resolved. If they cannot be, that is a data integrity error
     *              and we should throw an error to notify.
     
     **/
    public static List<RhymeTable> listOfRiddleRhyme_to_rhyme
                                 (List<RiddleRhymeTruthTable> joinTableEntries){
              
        //convert the entries into entities:
        List<RhymeTable> rhymes = new ArrayList<RhymeTable>();
        RiddleRhymeTruthTable joinRecord;
        long currentRhymeID;
        BaseEntityContainer currentContainer;
        RhymeTable currentEntity; //<--IDE says not used. It is used!
        int len = joinTableEntries.size();
        for(int i = 0; i < len; i++)
        {//iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii
            joinRecord       = (RiddleRhymeTruthTable)joinTableEntries.get(i);
            currentRhymeID   = joinRecord.getRhymeId();
            currentContainer = RhymeTransUtil.getRhymeByID(currentRhymeID);
            
            if(false == currentContainer.exists)
            {//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
                String msg = "";
                String name1 = RiddleRhymeTruthTable.class.getSimpleName();
                String name2= RhymeTable.class.getSimpleName();
                msg += "[The join table entity:" + name1 + "]";
                msg += "[Was not able to resolve]";
                msg += "[record in table:" + name2 + "]";
                throw new MyError(msg);
            }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            
            currentEntity    = (RhymeTable)currentContainer.entity;
            rhymes.add( currentEntity );
        }//iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii
       
        //Error check: Is the output list the same length as input list?
        if(joinTableEntries.size() != rhymes.size())
        {//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            String msg = "output list size does not match input list size;";
            throw new MyError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //return output:
        return rhymes;                                 
                                                           
    }//FUNC::END
    
}//CLASS::START
