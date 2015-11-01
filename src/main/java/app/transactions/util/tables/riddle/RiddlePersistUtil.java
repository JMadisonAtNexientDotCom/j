package app.transactions.util.tables.riddle;

import java.util.List;
import org.hibernate.Session;
import primitives.LongBool;
import app.MyError;
import app.dbDataAbstractions.entities.bases.BaseEntity;
import app.dbDataAbstractions.entities.containers.BaseEntityContainer;
import app.dbDataAbstractions.entities.tables.RiddleTable;
import app.transactions.util.PersistUtil;
import app.transactions.util.TransUtil;

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
        
        TransUtil.insideTransactionCheck();
        
        LongBool op=null; //<--output variable.
        long riddle_id = riddle.getId();
        
        //Only if the riddle_id is set, do we want to check and see if
        //the object already exist in database:
        if(riddle_id > 0){
            BaseEntityContainer bec;
            bec = TransUtil.getEntityByID(RiddleTable.class, riddle_id);
            if(bec.exists){
                op = PersistUtil.entityAlreadyExists(riddle_id);
                return op;
            }//
        }//non-zero id?
        
        //Though ID did not exist, it is still possible that we used
        //The same information in this riddle as another one. Look for
        //Collision:
        String text = riddle.getText();
        List<BaseEntity> entList;
        entList = TransUtil.getEntitiesUsingString
                             (RiddleTable.class, RiddleTable.TEXT_COLUMN, text);
        if(entList.isEmpty()){
            op = PersistUtil.entityIsNew(riddle_id, riddle);
        }else
        if(entList.size() > 1){
            doError("[Colliding entries fround in riddle table.]");
        }else{
            long found_id = entList.get(0).getId();
            op = PersistUtil.entityAlreadyExists(found_id);
        }//BLOCK::END
        
        //Return the results of persistence.
        if(null==op){doError("[How did this get null??]");}
        if(op.l <= 0){doError("[Invalid output id!]");}
        return op;
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
