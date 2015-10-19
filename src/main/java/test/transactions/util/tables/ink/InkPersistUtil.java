package test.transactions.util.tables.ink;

import java.util.List;
import primitives.LongBool;
import test.MyError;
import test.dbDataAbstractions.entities.tables.RhymeTable;

/**
 * Utility responsible for persisting the INK on the CuecardTable entities.
 * The INK represents the answer-choices (quips/rhymes) that have been proposed
 * as possible answers to the question on the title of a given cuecard.
 * @author jmadison :2015.10.19
 */
public class InkPersistUtil {
    
    /**
     * Makes sure group of rhymes is persisted into database.
     * @param quips :The rhyme options on the cuecard.
     * @return :Returns the GROUP_ID used to identify this group of quips.
     *          If the set of quips was UNIQUE. The data is persisted
     *          and the boolean component is set to TRUE to let us know.
     */
    public static LongBool persistQuips(List<RhymeTable> quips){
        doError("[TODO: InkPersistUtil.persistQuips(quips)]");
        
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
        Class clazz = InkPersistUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
         
}//CLASS::END
