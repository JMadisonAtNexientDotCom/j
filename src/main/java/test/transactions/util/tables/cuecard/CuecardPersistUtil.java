package test.transactions.util.tables.cuecard;

import primitives.LongBool;
import test.MyError;
import test.dbDataAbstractions.entities.composites.CueCard;
import test.dbDataAbstractions.entities.tables.riddleTrialStore.questionStore.CuecardTable;
import test.transactions.util.tables.ink.InkPersistUtil;
import test.transactions.util.tables.rhyme.RhymePersistUtil;
import test.transactions.util.tables.riddle.RiddlePersistUtil;

/**
 * Utility used to persist cuecard pojo objects into cuecardTable structs.
 * @author jmadison
 */
public class CuecardPersistUtil {
    
    /** Makes sure card is represented in database. **/
    public static LongBool persist(CueCard card){
        LongBool riddle_id = RiddlePersistUtil.persist( card.jest );
        LongBool ink_id    = InkPersistUtil.persistQuips(card.quips );
        
        boolean mustMakeNewID = false;
        long cuecard_id = (-7);//arbitrary neg number to find errors.
        boolean isOneOfTheEntitiesNew = (riddle_id.b || ink_id.b);
        if(isOneOfTheEntitiesNew){
            mustMakeNewID = true;
        }else{
            CuecardTable cc = new CuecardTable();
            cc.riddle_id = riddle_id.l;
            cc.ink_id    = ink_id.l;
            long foundID = CuecardTransUtil.findCuecard(cc);
            if(foundID <=(-1))
            {
                mustMakeNewID =true;
            }else{
                cuecard_id = foundID;
            }//
        }//
        
        if(mustMakeNewID){
            CuecardTable cctab;
            cctab = CuecardTransUtil.makeCueCard(riddle_id.l, ink_id.l);
            cuecard_id = cctab.getId();
        }//
        
        if(cuecard_id <= 0){
            doError("[Something went wrong while making cuecard id]");
        }//
        
        //Pack up the output:
        LongBool results = new LongBool();
        results.b = mustMakeNewID; //was new entry made into database?
        results.l = cuecard_id;
        return results;
        
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
