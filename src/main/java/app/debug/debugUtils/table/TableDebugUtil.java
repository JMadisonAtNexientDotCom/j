package app.debug.debugUtils.table;

//345678901234567890123456789012345678901234567890123456789012345678901234567890

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import app.MyError;
import app.config.debug.DebugConfig;
import app.transactions.util.TransUtil;
import app.transactions.util.TransValidateUtil;

/**##########################CLASS HEADER FILE##################################
//WHAT THIS CLASS DOES:
//Uses entity CLASSES to debug information stored in the database tables.
//NOT used to debug individual records.
//
//ORIGINAL USE CASE:
//OwnerTransUtil.assertAllTokensAreUniqueInTable()
//wraps function in here to check that the unique column is indeed unique.
//
//DESIGN NOTE (Justifications for why things are the way they are):
//"table" and "entity" are kind of a blurry line in hibernate.
//This utility debugs operations on my table entities that are on 
//the CLASS level and treating the entity class as a table of multiple
//records.
//
//@author JMadison : 2015.09.22_0130PM
//@author XXXXXXXX : 20XX.XX.XX_####AMPM
//@author XXXXXXXX : 20XX.XX.XX_####AMPM 
//@author XXXXXXXX : 20XX.XX.XX_####AMPM
########10########20########30########40########50########60########70########*/
//-------0---------0---------0---------0---------0---------0---------0---------0
public class TableDebugUtil {
    
   
    
    /**-------------------------------------------------------------------------
     * Assert that the column is UNIQUE!
     * If not, program will crash.
     * @param table     :The table entity we want to check for duplicate values.
     * @param columnName:The column we want to assert has all unique values.
     ------------------------------------------------------------------------**/
    public static void assertUniqueColumn(Class table, String columnName){
        
        //Make sure debug mode is on:
        if(false == DebugConfig.isDebugBuild){ //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            String msg ="[assertUniqueColumn is a heavy debug function that]";
            msg += "[should only be used when in debug build.]";
            msg += "[please surround call with: ]";
            msg += "[if(DebugConfig.isDebugBuild){}]";
            doError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //Make sure we are in a transaction state:
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();
        
        //Make sure we are working with correct type:
        TransValidateUtil.assertIsEntityClass(table);
        
        //http://stackoverflow.com/questions/12501228/
        //hibernate-criteria-to-return-a-list-that-contains-values-from-a-column
        Criteria c = ses.createCriteria(table);
        c.setProjection(Projections.property(columnName));
        
        List<String> columnValues = c.list();
        Map<String,Integer> theMap = new HashMap<String, Integer>();
        
        Object objectType;
        String cur;
        int len = columnValues.size();
        for(int i = 0; i < len; i++){
            objectType = columnValues.get(i);
            cur = convertObjectTypeToString(objectType);
            
            if( theMap.containsKey(cur))
            {//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
                String msg = "[Duplicate Column Value found!]";
                msg+= "TableClass=="      + table.getCanonicalName();
                msg+= "ColumnName=="      + columnName;
                msg+= "Duplicate Value==" + cur;
                doError(msg);
            }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            
            //Insert entry:
            //If we need it: Talley all entries and throw summary.
            //Might need if we want to catch ALL the errors in one check
            //Rather than crash on the first duplicate column value found.
            theMap.put(cur, 1);
            
        }//NEXT i
        
    }//FUNC::END
    
    /**
     * Convert a generic object into a string so we can put it in a hash
     * map. Original usage: seeing if column contains all unique entries.
     * @param oType :object type to convert to string.
     * @return :The object type converted to string.
     */
    public static String convertObjectTypeToString(Object oType){
        if(oType instanceof Long){ 
            return Long.toString( (Long)oType );
        }else
        if(oType instanceof String){ 
            return (String)oType;
        }else{
            doError("[Niave Convert Fail: not Long and not String]");
        }//IF::END
        
       return "[ERROR:FUNCTION_SHOULD_NOT_RETURN_THIS_STRING]";
    }//FUNC::END
    
    
   /**--------------------------------------------------------------------------
   * Wrapper function to throw errors from this class.
   * @param msg :Specific error message.
   --------------------------------------------------------------------------**/
   private static void doError(String msg){
       String err = "ERROR INSIDE:";
       err += TableDebugUtil.class.getSimpleName();
       err += msg;
       throw MyError.make(TableDebugUtil.class, err);
   }//FUNC::END
    
}//CLASS::END
