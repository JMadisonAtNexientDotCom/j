package test.transactions.util;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import test.MyError;
import test.dbDataAbstractions.entities.bases.BaseEntity;

/**
 * Error checking utility that is used for validation error-checking in
 * transactions. When validation fails, error will be thrown.
 * @author jmadison
 */
public class TransValidateUtil {
    
    /**
     * Check table to see if the id exists.
     * @param tableEntityClass:Class of entity used to identify table.
     * @columnName: Name of column we are checking for longValue existance.
     * @param longValue : Value that should be in column of table.
     *                    BUT: Only validated if number provided is >=0
     */
    public static void longValueShouldExistIfNonNegative
                    (Class tableEntityClass, String columnName, long longValue){
                        
        //Make sure we are in a transaction state:
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();

        if(longValue <(-1)){////////////////////////////////////////////////////
            doError("If you want to avoid check, must supply (-1)");
        }///////////////////////////////////////////////////////////////////////
        
        //Ignore check if (-1) supplied.
        if(longValue == (-1)){return;}
        
        //else, value is >= 0, and we need to make sure entry exists:
        Class tec = tableEntityClass; 
        boolean isEntityClass = is_B_BaseClass_of_C(BaseEntity.class, tec);
        if(false == isEntityClass){/////////////////////////////////////////////
            String msg = "[[Class supplied was not entity.]]";
            String className = tableEntityClass.getCanonicalName();
            msg+="Class used:";
            msg+="[" + className + "]";
            doError(msg);
        }///////////////////////////////////////////////////////////////////////
               
        Criteria c = ses.createCriteria(tableEntityClass);
        c.add(Restrictions.eq(columnName, longValue));
        BaseEntity table = (BaseEntity) c.uniqueResult();
        if(null == table){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            String tableName = tableEntityClass.getCanonicalName();
            String msg = "The entry did not exist in the table!";
            msg+="tableClass:[" + tableName + "]";
            msg+="columnName:[" + columnName+ "]";
            msg+="longValue:[" + Long.toString(longValue) + "]";
            doError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
    }//FUNC::END
        
    /**
     * A wrapper for longValueShouldExistIfNonNegative 
     * that exists only for code readability.
     * @param tableEntityClass :Class of entity used to identify table.
     * @param columnName       :Column name to check for id.
     * @param id               :The ID to check for. If (-1), check
     *                          is ignored. if <(-1) error thrown.
     */
    public static void idShouldExistIfNonNegative
                    (Class tableEntityClass, String columnName, long id){

        TransUtil.insideTransactionCheck();
        longValueShouldExistIfNonNegative(tableEntityClass, columnName, id);
                        
    }//FUNC::END
     
    /**
     * Found isAssignableFrom to be a bit confusing to comprehend.
     * So I wrapped it in grammer that is more understandable for
     * what I am checking for.
     * SOURCE: http://stackoverflow.com/questions/3949260/
     *                           java-class-isinstance-vs-class-isassignablefrom
     * @param b :The class that is the base-class.
     * @param c :The class that extends base-class b.
     * @return  :Returns true if this relationship exists.
     */
    private static boolean is_B_BaseClass_of_C(Class b, Class c){
        return b.isAssignableFrom(c);
    }//FUNC::END
                    
    /**-------------------------------------------------------------------------
     * Wrapper function to throw errors from this class.
     * @param msg :Specific error message.
     ------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        err += TransValidateUtil.class.getSimpleName();
        err += msg;
        throw new MyError(err);
    }//FUNC::END
        
}//CLASS::END
