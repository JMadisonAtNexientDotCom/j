/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.dbDataAbstractions.entities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import test.MyError;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import test.transactions.HibernateReflectionUtil;
import test.transactions.util.TransUtil;

/**
 *
 * @author jmadison :Pre:2015.10.20(Oct20th,Year2015)
 */
public class EntityUtil {
   
    
    //Wrapper for function I could not find.
    //I looked here first. So put a wrapper here because I was very close
    //to re-writing this function because I did not know it existed.
    //For more info on it, look into the wrapped function.
    public static void setField
                              (BaseEntity ent, String columnName, Object value){
        HibernateReflectionUtil.setEntityColumnValue(ent, columnName, value);
    }//FUCN::END
    
    
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
     * 
     * UPDATE:2015.10.20: Changed signature from List<BaseEntity> to 
     *        List<? extends BaseEntity> so we don't have to cast.
     */
    public static List<Long> StripPrimaryIDS(List<? extends BaseEntity> entities){
        
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
            if(curID <= 0){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
                doEntityLazyFetchError(curEnt);
            }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            
            
            op.add(curID);
        }//next i
        
        return op;
        
    }//FUNC::END
    
    /**
     * Added this error to help me trace root of problem:
     * @param <T> :Type that is BaseEntity or higher.
     * @param curEnt :Actual entity to output as having error. **/
    public static <T extends BaseEntity> void doEntityLazyFetchError(T curEnt){
        long id = curEnt.getId();
        Class clazz = curEnt.getClass();
        String entity_name = clazz.getCanonicalName();
        String msg = "";
        msg+= "[EntityUtil Detects Possible Lazy Fetch Error: START]";
        msg+= "[ID==[" + Long.toString(id) + "]]";
        msg+= "[ENTITY CLASS:[" + entity_name + "]]";
        doError(msg);
    }//FUNC::END
    
    /** Makes sure the data is correctly paired. We want the original primary
     *  keys to match up with the produced entities so that we can make
     *  assumptions in calculations later that may only use the keys list
     *  rather than the actual entities.
     * @param ents :The entities in a list.
     * @param keys :The primary ids of each entity in the other list.
     */
    public static void assertEntitiesLineUpWithPrimaryKeys
                             (List<? extends BaseEntity> ents, List<Long> keys){
        if(null == ents || null == keys){
            doError("[Null_Lists_Are_Not_Allowed]");
        }
        
        //Do not allows this, because empty list is sign of error:
        if(ents.isEmpty() || keys.isEmpty()){
            doError("[Empty_Lists_Are_Not_Allowed]");
        }
        int ent_len = ents.size();
        int key_len = keys.size();
        if(ent_len != key_len){doError("[CannotLineUp.DifferentSizes]");}
        
        //Iterate though lists and make sure data matches up:
        for(int i = 0; i < ent_len; i++){
            BaseEntity cur_ent = ents.get(i);
            long ent_id = cur_ent.getId();
            long key_id = keys.get(i);
            if(ent_id != key_id){
                doError("EntityIDMisMatchFound");
            }//
        }//next i.
        
    }//FUNC::END
                             
    /**
     * 
     * @param <T> :The type of entity we want to create a new instance of.
     * @param entClass :The actual reference to the type using class ref.
     * @return    :A new instance of the entity.
     * 
     * Design note: This was the FIRST place I looked for this function when
     * I thought "I think I already made this function before".
     * HibernateReflectionUtil was the 2ND place. Rather than ~accidentially~
     * duplicate code as this code base grows, I would rather just make
     * wrapper functions for where I expect functions to be.
     * 
     * Not elegant. But less-bad than code duplication.
     * In a perfect world, we would just know where everything is.
     * 
     */
    public static <T extends BaseEntity> BaseEntity makeEntityFromClass
                                                            (Class<T> entClass){
        return HibernateReflectionUtil.makeEntityFromClass(entClass);
    }//FUNC::END
             
    /**
     * Like makeEntityFromClass, but returns a [batch/set/list] of them.
     * @param <T>         :Type of entity.
     * @param entClass    :Reference to the entity class to instantiate.
     * @param amountToMake:How many entities do you want?
     * @return :The entities requested. Does NOT persist them. **/
    public static <T extends BaseEntity> List<T> makeEntitiesFromClass
                                          (Class<T> entClass, int amountToMake){
        List<T> op = new ArrayList<T>();
        BaseEntity new_ent;
    
        for(int i = 0; i < amountToMake; i++){
            new_ent = makeEntityFromClass(entClass);
            op.add((T)new_ent); //Why did I have to cast?
        }//FUNC::END
        
        return op;
    }//FUNC::END
          
    /**
     * Exact same behavior as makeEntitiesFromClass, however it also persists
     * the entities into database so that you can make practical use of the
     * primary key value of the entity.
     * @param <T>         :See makeEntitiesFromClass
     * @param entClass    :See makeEntitiesFromClass
     * @param amountToMake:See makeEntitiesFromClass
     * @return            :See makeEntitiesFromClass **/                
    public static <T extends BaseEntity> List<T> 
                                                makeEntitiesFromClass_PersistIDS
                                          (Class<T> entClass, int amountToMake){
        //Make sure we are inside transaction state,
        //then get session object so we can persist entity.
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();
                  
        //Make batch of entities, then persist them all in
        //the same order that they were made.
        List<T> lst = makeEntitiesFromClass(entClass, amountToMake); 
        T cur_ent;
        for(int i = 0; i < amountToMake; i++){
            cur_ent = lst.get(i);
            ses.save(cur_ent); //<--persist so it can have primary key.
        }//next i.
        
        //return the list of persisted entities:
        return lst;
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
