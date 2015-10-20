package test.transactions;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import test.MyError;
import test.dbDataAbstractions.entities.bases.BaseEntity;

/**
 * A reflection utility SPECIFICALLY for hibernate framework.
 * @author jmadison :Pre:2015.10.20. Maybe a few days ago?
 */
public class HibernateReflectionUtil {
    
    public static void doJoin(BaseEntity from, BaseEntity to, String dest){
        long fkeyID = from.getId();//get foreign key ID.
        setEntityColumnValue(to,dest,fkeyID);
    }//FUNC::END
    
    /**
     * Use reflection to set entity column value.
     * Original usage: Creating join-table operations.
     * @param to        :The entity to set value into.
     * @param columnName:The name of the column.
     * @param value     :The value to set into column/entity property
     */
    public static void setEntityColumnValue
                               (BaseEntity to, String columnName, Object value){
        Field f = getDeclaredFieldUsingInstance(to,columnName);
        f.setAccessible(true); //<--so we can mess with PRIVATE variables.
        setValueOfInstanceField(f,to,value);
    }//FUCN::END
             
    /** wrapper mainly to keep required try-catch out of my core logic. **/
    private static Field getDeclaredFieldUsingInstance
                                           (Object instance, String columnName){
                                               
        Field f = null; //<--output variable.
        //Try-catch enforced by compiler.
        //Consider failure a fatal error / bug in code that needs
        //to have the root problem immediately fixed.
        try {//TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
            f = instance.getClass().getDeclaredField(columnName);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(HibernateReflectionUtil.class.getName()).log
                                                       (Level.SEVERE, null, ex);
            doError("[NoSuchFieldException]");
        } catch (SecurityException ex) {
            doError("[SecurityException]");
            Logger.getLogger(HibernateReflectionUtil.class.getName()).log
                                                       (Level.SEVERE, null, ex);
        }//END TRY //TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
        
        if(null == f){doError("[Trying to return null field]");}
        return f;
    }//FUNC::END
                               
    /** wrapper mainly to keep required try-catch out of my core logic. **/
    private static void setValueOfInstanceField
                                            (Field f, Object obj, Object value){
        if(null == f || null == obj || null == value){
            doError("[Null not allowed for any of these inputs]");
        }//
        
        try {
            f.set(obj, value);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(HibernateReflectionUtil.class.getName()).log
                                                       (Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(HibernateReflectionUtil.class.getName()).log
                                                       (Level.SEVERE, null, ex);
        }
    }//FUNC::END
                                            
    public static <T extends BaseEntity> BaseEntity makeEntityFromClass
                                                            (Class<T> entClass){
        T obj = null;
        
        //Boiler plate BS try-catch:
        try {
            obj = (T)(entClass.newInstance());
        } catch (InstantiationException ex) {
            Logger.getLogger(HibernateReflectionUtil.class.getName()).log
                                                       (Level.SEVERE, null, ex);
            doError("[Address this now. Find the source of error. 3242332]");
        } catch (IllegalAccessException ex) {
            Logger.getLogger(HibernateReflectionUtil.class.getName()).
                                                    log(Level.SEVERE, null, ex);
            doError("[Address this now. Find the source of error. 7434546]");
        }//meow
        
        if(null == obj){doError("[How did this happen??42342]");}
        return obj;
    }//FUNC::END
                                           
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = HibernateReflectionUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
    
}//CLASS::END
