package test.transactions;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import test.MyError;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import utils.ReflectionHelperUtil;

/**
 * A reflection utility SPECIFICALLY for hibernate framework.
 * @author jmadison :Pre:2015.10.20. Maybe a few days ago?
 */
public class HibernateReflectionUtil {
    
    /**
     * A manual join-table operation. Does NOT specify a column to
     * take from, because always takes from the primary-id column.
     * @param from :[Record/table/entity] to take data from.
     * @param into :[Record/table/entity] to take data from.
     * @param dest :Column of "into" to populate with data from "from". **/
    public static void doJoin(BaseEntity from, BaseEntity into, String dest){
        long fkeyID = from.getId();//get foreign key ID.
        setEntityColumnValue(into,dest,fkeyID);
    }//FUNC::END
    
    
    /**
     * Link join, but we can take from column other than primary key column
     * @param from :[Record/table/entity] to take data from.
     * @param take :Column of "from" to take data from.
     * @param into :[Record/table/entity] to take data from.
     * @param dest :Column of "into" to populate with data from "from". **/
    public static void doLink
                   (BaseEntity from, String take, BaseEntity into, String dest){
        Object takeData = getEntityColumnValue(from,take);
        
        if(null == takeData){
            //Not allowing this because it is indicative of an error.
            doError("Linking with null columns is not allowed.");
        }//
        
        setEntityColumnValue(into,dest,takeData);
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
    }//FUNC::END
        
    /**
     * Use reflection to get the value stored in a given column on an entity.
     * @param from :The entity we want to take data from.
     * @param take :The column name of the data we want to take. **/
    public static Object getEntityColumnValue(BaseEntity from, String take){
        Field f = getDeclaredFieldUsingInstance(from, take);
        f.setAccessible(true); //<--so we can mess with PRIVATE variables.
        return getValueOfInstanceField(f,from);
    }//FUNC::END
             
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
            String msg = "";
            msg += "[NoSuchFieldException][START::]";
            msg += "[instance==[" + instance.toString() + "]]";
            msg += "[columnName==[" + columnName + "]]";
            msg += "[::END NSFE]";
            doError(msg);
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
             
    /**
     * @param f   :Field to get value from.
     * @param inst: The instance to operate on.
     * @return :Value of the field on the instance. **/
    private static Object getValueOfInstanceField(Field f, Object inst){
        boolean isFieldStatic = false;
        return ReflectionHelperUtil.getValueOfField(f, isFieldStatic, inst);
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
