package test.transactions.util.tables.riddle;

import primitives.LongBool;
import test.MyError;
import test.dbDataAbstractions.entities.tables.RiddleTable;

/**
 * Utility responsible for persisting RiddleTable entities into database.
 * @author jmadison:2014.10.19
 */
public class RiddlePersistUtil {
    
    /**
     * Persists the riddle object into the database.
     * @param riddle :The configured riddle to persist.
     * @return :Returns a LongBool who's long value is the ID of
     *          the RiddleTable record within the database.
     *          If the riddle you supplied to the function did
     *          NOT previously exist, the output's boolean is
     *          flagged to TRUE.
     */
    public static LongBool persist(RiddleTable riddle){
        doError("[TODO: RiddlePersistUtil.persist(riddle)]");
        
        LongBool TODO = new LongBool();
        TODO.l = (-1); //negative is invalid output.
        TODO.b = true;
        return TODO;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = RiddlePersistUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
