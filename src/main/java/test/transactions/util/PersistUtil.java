package test.transactions.util;

import org.hibernate.Session;
import primitives.LongBool;
import test.MyError;
import test.dbDataAbstractions.entities.bases.BaseEntity;

/**
 * Generic persistence utility for any common boiler plate I find in my
 * particular persistence utilities.
 * 
 * ONLY FOR PERSISTING! Not for READING.
 * Reading being like persisting, but in the other direction.
 * Serialize --is to--> De-Serialize
 * as
 * Persist   --is to--> Read
 * 
 * @author jmadison :2015.10.20
 */
public class PersistUtil {
    
    public static boolean NEW_ENTITY_MADE = true;
    public static boolean NO_ENT_MADE_ALREADY_EXISTS = false;
    
    /** Create a response that lets us know entity is NEW.
     *  This version will SAVE the base entity with the ID.
     * @param  entity_id :The ID of the entity.
     * @param  bent : The actual entity.
     * @return : A LongBool configured to let us know entity is new. **/
    public static LongBool entityIsNew(long entity_id, BaseEntity bent){
        
        //Not a valid id!
        if(entity_id <= 0){
            doError("[Trying to make output with <= 0 entity id value.]");
        }//Error
        
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();
        
        //Set id of new entity and persist it by saving it:
        bent.setId(entity_id);
        ses.save(bent);
        
        //Create output:
        LongBool op = new LongBool();
        op.b = NEW_ENTITY_MADE; //<-- true means new entity made.
        op.l = entity_id; //<--id of new entity made.
        return op;
        
    }//FUNC::END
    
    /**
     * Since entity already exists, all we need to do is make
     * the response. No persisting of duplicate data.
     * @param entity_id :Id of entity that already exists in database.
     * @return :LongBool configured to let us know entity already exists. **/
    public static LongBool entityAlreadyExists(long entity_id){
        
        //Not a valid id!
        if(entity_id <= 0){
            doError("[entity_id<=0, entityAlreadyExists() func]");
        }//Error
        
        //Create output:
        LongBool op = new LongBool();
        op.b = NO_ENT_MADE_ALREADY_EXISTS;//<--Meansalready existed in database.
        op.l = entity_id; //<--id of new entity made.
        return op;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = PersistUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
