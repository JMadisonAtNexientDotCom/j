package app.transactions.util.tables.cuecard;

import app.MyError;
import app.dbDataAbstractions.entities.composites.CueCard;
import app.dbDataAbstractions.entities.tables.RhymeTable;
import app.dbDataAbstractions.entities.tables.riddleTrialStore.questionStore.CuecardTable;
import app.transactions.util.tables.ink.InkReadUtil;
import app.transactions.util.tables.riddle.RiddleTransUtil;
import java.util.List;

/**
 * Class responsible for READING CueCardTable(s) or cuecard_id(s)
 * and turning them into CueCard Pojo objects. 
 * 
 * This class does the INVERSE of CueCardPersistUtil.
 * 
 * @author jmadison:2015.11.01(November,1st,Year2015.Sunday)
 */
public class CuecardReadUtil {
    
    /**
     * Reads a CueCard from the database using cuecard_id as handle to find
     * the correct persisted data.
     * @param cuecard_id :The primary key of the CuecardTable [entity/record]
     * @return :Returns instantiated pojo version of record.
     */
    public static CueCard readUsingPrimaryKeyID(long cuecard_id){
        
        //Use the cuecard_id to find the actual cuecard table instance:
        CuecardTable ct = CuecardTransUtil.getCuecardTableByID(cuecard_id);
        return readUsingCuecardTable(ct);
        
    }//FUNC::END
    
    /**
     * Uses a CuecardTable [entity/record] to read into a CueCard pojo.
     * @param ct :The CuecardTable to read from.
     * @return   :The pojo version of the CuecardTable. **/
    public static CueCard readUsingCuecardTable(CuecardTable ct){
        
        //Error checks:
        if(null==ct){doError("[Null cuecard input.]");}
        if(ct.ink_id <= 0){doError("[Bad ink_id to read from. Lazy fetch?]");}
        if(ct.riddle_id <= 0){doError("[Bad riddle_id to read from.]");}
        
        CueCard op = new CueCard();
        op.jest = RiddleTransUtil.getRiddleTableByID(ct.riddle_id);
        op.quips= InkReadUtil.readQuipsUsingInkTablePrimaryKeyID(ct.ink_id);
        return op;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = CuecardReadUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
