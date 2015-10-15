/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.dbDataAbstractions.entities;

import java.util.ArrayList;
import java.util.List;
import test.MyError;
import test.dbDataAbstractions.entities.bases.BaseEntity;

/**
 *
 * @author jmadison
 */
public class EntityUtil {
   
    /** Creates a list where all entities have been downcasted to base entity**/
    public static List<BaseEntity> downCastEntities
                                              (List<? extends BaseEntity> ents){
        List<BaseEntity> op = new ArrayList<BaseEntity>();
        int len = ents.size();
        BaseEntity curBase;
        for(int i = 0; i < len; i++){
            curBase = (BaseEntity)ents.get(i);
            op.add(curBase);
        }//next i
        
        return op;
    }//FUNC::END
            
    /**  **/
    /**
     * Takes base entities and UPCASTS them to their actual type.
     * @param <T> :The type we want to cast to.
     * @param ents:List of generic base entities.
     * 
     * //Maybe not needed? Delete if you read this later.
     * @param upcastType:The type we want to cast to.
     *        Class<T> upcastType
     * 
     * @return 
     */
    public static <T> List<T> upCastEntities
        (List<? extends BaseEntity> ents){
              
        List<T> op = new ArrayList<T>();
        int len = ents.size();
        T cur_specific_entity_type;
        for(int i = 0; i < len; i++){
            cur_specific_entity_type = (T)ents.get(i); //cast to type.
            op.add(cur_specific_entity_type);
        }//next i
        
        return op;                        
                                
    }//FUNC::END
    
    /** I swear I have made something like this before. Not happy that I am
     *  duplicating code possibly.
     * @param entities :The entities to collect IDS from.
     *  A list of IDS from the entities. Preserves the original order.
     *  Considered error if passed null or empty list.
     * @return: See above.
     */
    public static List<Long> StripPrimaryIDS(List<BaseEntity> entities){
        
        if(null == entities || entities.isEmpty()){
            doError("Not allowed!");
        }//Error!
        
        List<Long> op = new ArrayList<Long>();
        
        int len = entities.size();
        BaseEntity curEnt;
        long curID;
        for(int i = 0; i < len; i++){
            curEnt = entities.get(i);
            curID = curEnt.getId();
            if(curID <= 0){doError("possible lazy fetch error");}
            op.add(curID);
        }//next i
        
        return op;
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = EntityUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
