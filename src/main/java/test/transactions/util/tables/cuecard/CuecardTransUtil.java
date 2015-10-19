package test.transactions.util.tables.cuecard;

import test.MyError;
import test.dbDataAbstractions.entities.tables.riddleTrialStore.questionStore.CuecardTable;

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
