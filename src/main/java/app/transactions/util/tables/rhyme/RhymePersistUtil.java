package app.transactions.util.tables.rhyme;

import primitives.LongBool;
import app.MyError;
import app.dbDataAbstractions.entities.tables.RhymeTable;

/**
 * Utility responsible for persisting RhymeTable entities into database.
 * @author jmadison :2015.10.19
 */
public class RhymePersistUtil {
    public static LongBool persist(RhymeTable rhyme){
        doError("[TODO: RhymePersistUtil.persist(rhyme)]");
        
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
        Class clazz = RhymePersistUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
}//CLASS::END
