package test.transactions.util.tables.cuecard;

import java.util.List;
import test.MyError;
import test.dbDataAbstractions.entities.composites.CueCard;
import test.dbDataAbstractions.entities.containers.BaseEntityContainer;
import test.dbDataAbstractions.entities.tables.RhymeTable;
import test.dbDataAbstractions.entities.tables.RiddleTable;
import test.dbDataAbstractions.entities.tables.riddleTrialStore.questionStore.CuecardTable;
import test.transactions.util.TransUtil;
import test.transactions.util.tables.rhyme.RhymeTransUtil;
import test.transactions.util.tables.riddle.RiddleTransUtil;

/**
 * Class manages transactions involving CuecardTable entity and database.
 * @author jmadison
 */
public class CuecardTransUtil {
    
    /**
     * Makes a new cue-card entity. Assumes that riddle_id + ink_id 
     * combinations do NOT yet exist in the database. If they do, a
     * fatal error will be thrown.
     * @param riddle_id :The unique id of a riddle in the riddle table.
     * @param ink_id :A unique id identifying a group of 
     *                [quips/rhymes/possible answers] on the cuecard.
     * @return :Return an entity representing the newly made cuecard.
     */
    public static CuecardTable makeCueCard(long riddle_id, long ink_id){
        doError("[TODO: CuecardTransUtil.makeCueCard()]");
        return new CuecardTable();
    }//FUNC::END
    
    /**
     * Sees if the cuecard configuration already is persisted in the
     * database.
     * @param card :The card to check for in database.
     * @return :Returns >= 1 if card already exist. The number
     *          being the ID of the existing CuecardTable entity
     *          in the database.
     * 
     *          If not found, returns (-1)
     *          If not found, the CALLING FUNCTION might want to do a
     *          sessionInstance.save(card) on the object.
     */
    public static long findCuecard(CuecardTable card){
        doError("[TODO: findCueCard]");
        return (-1);
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
        RiddleTable rt = null;
        BaseEntityContainer rtCon = RiddleTransUtil.getRiddleByID(riddleID);
        if(false == rtCon.exists){
            doError("riddle of this id does not exist");
        }else{
            if(null==rtCon.entity){doError("rtCon.entity is null");}
            rt = (RiddleTable)(rtCon.entity);
        }////
        
        //if RT is null, that is an error!
        if(null == rt){doError("rt is null");}
        
        
        //Make our cue card!         
        CueCard op = CueCard.makeCueCard_WithJest_And_EmptyQuips(rt);
        
        //create a choice list:
        List<RhymeTable> rhymeList = RhymeTransUtil.makeChoicesToChooseFrom
                                    (riddleID, numberOfChoices, numberOfTruths);
        
        //Pack the rhymeList (possible answers) into our output container:
        op.quips = rhymeList;
        
        //return output:
        return op;
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = CuecardTransUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
}//CLASS::END
