package app.transactions.util;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import app.MyError;
import app.dbDataAbstractions.entities.bases.BaseEntity;
import app.dbDataAbstractions.entities.bases.PurseEntity;
import app.dbDataAbstractions.entities.containers.BaseEntityContainer;

/**
 * PURPOSE/PATTERN OF THIS UTILITY:
 * Provides globally scoped logical "bracket" balancing for the application.
 * Enforces a programming structure that will help find logic errors.
 * 
 * What I am trying to enforce:
 * 1. All transaction logic in app must happen between:
 *    enterTransaction() and exitTransaction() statements.
 * 2. Transactions may NOT be nested.
 * 
 * This helps detect logic errors that could not otherwise be tested for.
 * As there is a VAST SET of possible program states.
 * 
 * CONCERN:
 * This is a static utility. Because it is looking for errors in the
 * overall program logic. So far my load testing has not come up with any
 * errors due to concurrent API calls... This actually shocks me. I am hoping
 * this is the way it is by design. Because this behavior is very helpful to me.
 * Thinking there must be a new instance of TransUtil for each 
 * [session/request/connection]?
 * 
 * CONCERN-UPDATE#1:
 * I saved all my API calls to a favorites folder on my firefox browser.
 * I then selected "OPEN ALL FAVORITES IN TABS" to execute all the API calls
 * concurrently. Hackish. But I got LOTS OF ERRORS!
 * This problem is now fixed. One instance of this static utility exists
 * for each [connection/thread]
 * 
 * TransUtil stands for: "Transaction Utility"
 * A utility used to remove boiler plate code from transactions.
 * @author jmadison: 2015.09.02_0354PM **/
public class TransUtil {
    
    //ThreadLocal Core:
    //TLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTL
    //TLTLTL--------- ThreadLocal Core : START --------LTLTLTLTLTLTLTLTLTLTLTLTL
    //TLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTL
    
    /** The next thread to instantiate this utility will use this number to
     *  identify the instance of the utility. -------------------------------**/
    private static int _nextUtilityInstanceID = 0;
    
    /** One instance for all threads. Used for debugging so we can confirm
     *  that API calls are indeed using different instances of static utility
     *  core on different threads.
     * @return : An integer to use for identification -----------------------**/
    private static int getNextUtilityInstanceID(){//----------------------------
        int op = _nextUtilityInstanceID;
        _nextUtilityInstanceID ++;
        return op;
    }//-------------------------------------------------------------------------
    
    /** From my understanding, this is a collection of TransUtil_CORE, one -----
     *  for each thread. ----------------------------------------------------**/
    private static ThreadLocal<TransUtil_CORE> _core =
                                              new ThreadLocal<TransUtil_CORE>();
    
    /** If all externally ~accessable~ components need to be accessed via
     *  the getCore() function. Maybe getCore() should be called "i" for
     *  "instance core". Alternatively we could individually wrap every single
     *  method. However, that seems like more maintenance than it is worth.
     * 
     *  If utility methods are called with UtilityName.i.function(),
     *  then we know it is a ThreadLocal static utility.
     *  Sounds good to me!
     * 
     * @return  -------------------------------------------------------------**/
    public static TransUtil_CORE i(){//-----------------------------------------
        TransUtil_CORE op = _core.get();
        if(null == op){
            op = new TransUtil_CORE();
            op.utilityInstanceID = getNextUtilityInstanceID();
            _core.set( op );
        }
        return op; 
    }//------------------------------------------------------------------------- 
    
    //TLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTL
    //TLTLTL--------- ThreadLocal Core : END --------LTLTLTLTLTLTLTLTLTLTLTLTLTL
    //TLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTLTL
    
    /** We exit a transaction with this flag to tell program that
     *  we believe we have registered entities to be saved upon exiting
     *  the transaction. This is done as an error check.
     * 
     *  If we exit with EXIT_WITH_SAVE but we have not marked any entities
     *  as needing to be saved, it shows an inconsistency in our logic.
     *  Logic errors can be hard to find. So this is helpful. **/
    public static final Boolean EXIT_WITH_SAVE = true;
    
    /** The inverse of EXIT_WITH_SAVE. 
     *  We exit with this code when we have NOT marked
     *  any entities for saving upon closing of the session.
     *  For more info, see EXIT_WITH_SAVE **/
    public static final Boolean EXIT_NO_SAVING = false;
    
    /** This static utility has state information in it. I designed this
     *  intentionally for helping catch errors in the logic of a transaction.
     *  However, I expected this logic to break down if two or more API
     *  calls happen SYMULATANIOUSLY. They would both open up a session and
     *  then store that state information here in the 
     *  activeTransactionSession variable. One immediately overwriting the
     *  other and corrupting the other.
     * 
     *  But after load testing... I did not see this happen.
     *  But... I need to understand WHY this doesn't happen.
     */
    public static int debugSharedThreadCounter = 0;
    
    //WRAPPERS:
    //WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
    public static void insideTransactionCheck(){
        i().insideTransactionCheck();
    }//WRAPPER::END
    
    public static void outsideTransactionCheck(){
        i().outsideTransactionCheck();
    }//WRAPPER::END
    
    public static Session enterTransaction(){
        return i().enterTransaction();
    }//WRAPPER::END
    
    public static void exitTransaction
                            (Session ses, Boolean weHaveEntitiesThatNeedSaving){
        i().exitTransaction(ses,weHaveEntitiesThatNeedSaving);
    }//WRAPPER::END
                            
    public static void exitTransaction(Session ses){
       i().exitTransaction(ses);
    }//WRAPPER::END
                            
    public static Session getActiveTransactionSession(){
        return i().getActiveTransactionSession();
    }//WRAPPER::END
    
    public static void markEntityForSaveOnExit(BaseEntity ent){
        i().markEntityForSaveOnExit(ent);
    }//WRAPPER::END
    
    public static Criteria makeGloballyFilteredCriteria(Class tableClass){
        return i().makeGloballyFilteredCriteria(tableClass);
    }//WRAPPER::END
    
    public static void saveToGroup
                               (List<? extends PurseEntity> ents, long group_id){
        doError("Never tested. Take out this error now and test.");
        i().saveToGroup(ents,group_id);
    }//WRAPPER::END
    
    //Don't delete anything. Just overwrite data that already exists.
    //public static void markEntitiesForDeletionOnExit(List<BaseEntity> entList){
    //     i().markEntitiesForDeletionOnExit(entList);
    //}//WRAPPER::END
    
    public static long getHighestKeyInTable(Class tableClass, String keyName){
        return i().getHighestKeyInTable(tableClass, keyName);
    }//WRAPPER::END
    
    public static long getNumberOfRecordsInTable(Class tableClass){
        return i().getNumberOfRecordsInTable(tableClass);
    }//WRAPPER::END
    
    public static BaseEntityContainer getEntityFromTableUsingLong
                        (Class tableClass, String columnName, long columnValue){
        return i().getEntityFromTableUsingLong
                        (      tableClass,        columnName,      columnValue);
    }//WRAPPER::END
                        
    public static BaseEntityContainer getEntityFromTableUsingPrimaryKey
                        (Class tableClass, String columnName, long columnValue){
        return i().getEntityFromTableUsingPrimaryKey
                        (      tableClass,        columnName,      columnValue);
    }//WRAPPER::END
                        
    public static BaseEntityContainer getEntityByID
                                             (Class tableClass, long entity_id){
        return i().getEntityByID(tableClass,entity_id);
    }//WRAPPER::END                                       
                        
    public static BaseEntityContainer getRandomRecord(Class tableClass){
        return i().getRandomRecord(tableClass);
    }//WRAPPER::END   
    //WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
    
    public static BaseEntityContainer getEntityFromTableUsingUniqueString
                      (Class tableClass, String columnName, String columnValue){
        return i().getEntityFromTableUsingUniqueString
                      (      tableClass,        columnName,      columnValue);                   
    }//WRAPPER::END    
                      
    public static void deleVARCHAR
                      (Class tableClass, String columnName, String columnValue){
       i().deleVARCHAR(      tableClass,        columnName,        columnValue);
    }//WRAPPER::END
                      
    public static void deleINTEGER
                      (Class tableClass, String columnName, long columnValue){
       i().deleINTEGER(      tableClass,        columnName,      columnValue);
    }//WRAPPER::END
                      
    public static List<BaseEntity> getEntitiesUsingString
                      (Class tableClass, String columnName, String columnValue){
        return i().getEntitiesUsingString
                      (      tableClass,        columnName,        columnValue);                   
    }//WRAPPER::END
                      
    public static List<BaseEntity> getEntitiesUsingLong
                      (Class tableClass, String columnName, Long columnValue){
        return i().getEntitiesUsingLong
                      (      tableClass,        columnName,      columnValue);                   
    }//WRAPPER::END
                      
    public static List<BaseEntity> getOneEntityPerLong
                   (Class tableClass, String columnName, List<Long> colValList){
        return i().getOneEntityPerLong
                   (      tableClass,        columnName,            colValList);
    }//WRAPPER::END
                   
    public static List<BaseEntity> getEntitiesUsingRange(Class tableClass, 
                                int inclusiveIndexStart, int inclusiveIndexEnd){
            return i().getEntitiesUsingRange(tableClass,
                                         inclusiveIndexStart,inclusiveIndexEnd);
    }//WRAPPER::END
    
    public static List<Long> returnExistingPrimaryKeys
             (Class tableClass, String columnName, List<Long> possiblyValidIDs){
        return i().returnExistingPrimaryKeys
             (      tableClass,        columnName,            possiblyValidIDs);
    }//WRAPPER::END
             
    public static <T extends BaseEntity> List<T> makeStubsWithUniquePrimaryKeys
                         (Class<? extends BaseEntity> tableClass, int numStubs){
        return i().makeStubsWithUniquePrimaryKeys(    tableClass,     numStubs);
    }//WRAPPER::END
                         
    public static <T extends PurseEntity> List<T> getPursesUsingGroupID
                       (Class<? extends PurseEntity> purseClass, long group_id){
        return i().getPursesUsingGroupID(            purseClass,      group_id); 
    }//WRAPPER::END
                         
    public static void join(List<BaseEntity> from, 
                            List<BaseEntity> into, 
                            String         column){
        i().join(from,into,column);
    }//WRAPPER::END
    
    public static void link(List<BaseEntity> from, 
                            String    take_column,
                            List<BaseEntity> into, 
                            String    dest_column){
        i().link(from, take_column, into, dest_column);
    }//WRAPPER::END
    
    //TODO: MakeGroupUsingEntities
    public static <T extends PurseEntity> void makeGroup
        (Class<T> purseTable, long groupID, 
                               String foreignIDColumnName, List<Long> idValues){
        i().makeGroup(purseTable,groupID,foreignIDColumnName, idValues);
    }//WRAPPER::END
        
        
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = TransUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
                         
}//END::CLASS