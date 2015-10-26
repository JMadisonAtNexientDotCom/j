package test.transactions.util.tables.cuecard;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
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
        
        //Error check inputs:
        if(riddle_id <= 0 || ink_id <= 0){
            doError("Bad input for making cuecard in database");
        }//Error?
        
        TransUtil.insideTransactionCheck();
        CuecardTable cct = new CuecardTable();
        cct.setComment("Touched by CueCardTransUtil.makeCueCard");
        cct.riddle_id = riddle_id;
        cct.ink_id    = ink_id;
        
        //.save to persist object into database.
        //This should also set it's id.
        Session ses = TransUtil.getActiveTransactionSession();
        ses.save(cct);
        
        //Make sure save worked properly:
        long id_check = cct.getId();
        if(id_check <= 0){doError("Save did not set primary key");}
        
        //return the cue card table entity that has
        //just been created and persisted into the database:
        return cct;
    }//FUNC::END
    
    /**
     * Sees if the cuecard configuration already is persisted in the
     * database. Does NOT CARE ABOUT THE CUE-CARD's ID at all. In fact,
     * the ID of the cuecard might be invalid, because the cue-card supplied
     * may be supplied just for the purpose of making this query.
     * 
     * @param card :The card to check for in database.
     * @return :Returns >= 1 if card already exist. The number
     *          being the ID of the existing CuecardTable entity
     *          in the database.
     * 
     *          If not found, returns (-1)
     *          If not found, the CALLING FUNCTION might want to do a
     *          sessionInstance.save(card) on the object.
     * 
     *          This function is NOT responsible for saving a cuecard
     *          that is found to not exist in the database.
     */
    public static long findCuecardBySignature(CuecardTable card){
        
        //Make sure components of cue-card we care about are valid:
        if(card.ink_id <= 0 || card.riddle_id <= 0){
            doError("bad cuecard supplied");
        }//error?
        
        TransUtil.insideTransactionCheck();
        
        //Create a search for the cue-card signature:
        //SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS//
        Criteria cri  = TransUtil.makeGloballyFilteredCriteria
                                                           (CuecardTable.class);
        String INK    = CuecardTable.INK_ID_COLUMN;
        String RIDDLE = CuecardTable.RIDDLE_ID_COLUMN;
        cri.add( Restrictions.eq(INK   , card.ink_id));
        cri.add( Restrictions.eq(RIDDLE, card.riddle_id));
        List<CuecardTable> results = cri.list();
        //SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS//
        
        //1 result: exists
        //0 result: does not exist.
        //1+result: ERROR. All cue-card signatures should be unique.
        int num_results = results.size();
        long op = 0;
        if(0==num_results){
            op = (-1);
        }else
        if(1==num_results){
            CuecardTable cct = (CuecardTable)results.get(0);
            op = cct.getId();
            if(op <= 0){doError("bad id fetched");}
        }else{
            //Assuming num_result is 2 or greater:
            doError("all cue card signatures should be unique in database.");
        }//
        
        if(op == 0){
            doError("Not founds == (-1), founds are >=1, NO ZEROS!");
        }//
        
        //return the result of the signature search:
        return op;
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
