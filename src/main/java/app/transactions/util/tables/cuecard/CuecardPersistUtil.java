package app.transactions.util.tables.cuecard;

import primitives.LongBool;
import app.MyError;
import app.dbDataAbstractions.entities.composites.CueCard;
import app.dbDataAbstractions.entities.tables.riddleTrialStore.questionStore.CuecardTable;
import app.transactions.util.TransUtil;
import app.transactions.util.tables.ink.InkPersistUtil;
import app.transactions.util.tables.rhyme.RhymePersistUtil;
import app.transactions.util.tables.riddle.RiddlePersistUtil;

/**
 * Utility used to persist cuecard pojo objects into cuecardTable structs.
 * @author jmadison :Original date unknown.
 * @author jmadison :2015.10.26(Oct26th,Year2015,Monday)
 *                   Finishing persist function, then testing.
 */
public class CuecardPersistUtil {
    
    /** Makes sure card is represented in database. **/
    public static LongBool persist(CueCard card){
        
        //Error check, make sure inside transaction:
        TransUtil.insideTransactionCheck();
        
        //Persist all of the individual components of the CueCard object.
        LongBool riddle_id = RiddlePersistUtil.persist( card.jest );
        LongBool ink_id    = InkPersistUtil.persistQuips(card.quips );
        
        //C == Individual Component Check:
        //CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC//
        //If any of the persist calls on individual components of the cuecard
        //resulted in LongBool.b==TRUE being sent back to us, this indicates
        //the component did not previously exist in the database. In this case
        //we know for certain that a new cue-card must be persisted.
        //Otherwise, we don't know for sure. Though the individual elements
        //already existed in the database, the unique COMPOSITION of elements
        //making up the cue card may NOT yet exist in the database.
        //CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC//
        boolean mustMakeNewID = false;
        long cuecard_id = (-7);//arbitrary neg number to find errors.
        boolean isOneOfTheEntitiesNew = (riddle_id.b || ink_id.b);
        if(isOneOfTheEntitiesNew){
            mustMakeNewID = true;
        }else{
            CuecardTable cc = new CuecardTable();
            cc.riddle_id = riddle_id.l;
            cc.ink_id    = ink_id.l;
            long foundID = CuecardTransUtil.findCuecardBySignature(cc);
            if(foundID <=(-1))
            {
                mustMakeNewID =true;
            }else{
                cuecard_id = foundID;
            }//
        }//
        //CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC//
        
        //Make New CueCard:
        //MNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMN//
        if(mustMakeNewID){
            CuecardTable cctab;
            cctab = CuecardTransUtil.makeCueCard(riddle_id.l, ink_id.l);
            cuecard_id = cctab.getId();
        }//
        
        if(cuecard_id <= 0){
            doError("[Something went wrong while making cuecard id]");
        }//
        //MNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMNMN//
        
        //Pack up and return the output:
        //RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR//
        LongBool results = new LongBool();
        results.b = mustMakeNewID; //was new entry made into database?
        results.l = cuecard_id;
        return results;
        //RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR//
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = CuecardPersistUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
}//CLASS::END
