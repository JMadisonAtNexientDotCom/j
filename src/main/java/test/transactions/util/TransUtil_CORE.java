package test.transactions.util;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import test.MyError;
import test.config.constants.identifiers.TableNameReg;
import test.config.constants.identifiers.VarNameReg;
import test.config.debug.DebugConfig;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import test.dbDataAbstractions.entities.containers.BaseEntityContainer;
import test.dbDataAbstractions.entities.tables.TransTable;
import test.globalState.SynchronizedConversationCounter;
import test.globalState.SynchronizedGlobalSaveCounter;
import test.globalState.SynchronizedLogCounter;
import utils.HibernateUtil;


/**The instance-able core of our static TransUtil. -----------------------------
 * This is my solution to creating a static utility that has one unique
 * instance per thread.
 * 
 * @author jmadison :2015.09.16_0557PM --------------------------------------**/
public class TransUtil_CORE extends ThreadLocalUtilityBase {
     
    /** Used to search for values set to true. **/
    private long TRUE_VALUE = 1;
    
    /** Variable that helps for catching errors. Only used when
     *  isSingleThreaded == true, because this debugging logic may
     *  not hold up in multi-threaded environments.
     * 
     *  Makes sure you cannot make stupid mistakes like:
     *  1. calling enterTransaction 2X in a row.
     *  2. calling exitTransaction 2X in a row.
     *  3. calling enter/exit in wrong order.
     *  4. forgetting to close a transaction.
     *  5. forgetting to open a transaction. */
    private Boolean areWeInTransaction = false;
    
    /** ID that keeps track of unique order in which this conversation was
     *  BEGAN relative to all other conversations in the app. **/
    private long _convo_open_id = (-1);
    
    /** ID that keeps track of unique order in which this conversation was
     *  [ENDED/CLOSED] relative to all other conversations in the app. **/
    private long _convo_close_id = (-1);
    
    /** The session object created when we entered a transaction state.
     *  When in a transaction state, this reference should be NON-null.
     *  When outside a transaction state, this reference should be NULL. **/
    private Session activeTransactionSession;
    
    /** Entities that have been operated on while in a transaction state.
     *  When we exit the transaction state, we will need to commit-&-save
     *  the operations done to these entities. **/
    private ArrayList<BaseEntity> _saveTheseEntitiesOnExit;
    
    /** non-static initializer **/
    public TransUtil_CORE(){////////////////////////////////////////////////////
        //allocate to zero items. Just to make sure ref is not null.
        //this array will be destroyed after exiting at transaction
        //and re-built upon entering a new transaction.
        _saveTheseEntitiesOnExit = new ArrayList<BaseEntity>(0);
    }///////////////////////////////////////////////////////////////////////////

    
    /** Gets the [Session / Transaction] we are currently in.
     *  If we are NOT in a transaction state, throws an error.
     * @return : The session representing our transaction state. **/
    public Session getActiveTransactionSession(){
        
        //Throw error if NOT inside a transaction.
        //Also will throw error if session object is null.
        insideTransactionCheck();
        
        //return the session belonging to the
        //current transaction state we are inside of:
        return activeTransactionSession;
        
    }//FUNC::END
    
    
    /** As multiple transactions are chained together, we will need
     *  to somehow gather all the information needed to finalize the
     *  bundle of transactions. When we are in a transaction state,
     *  any entities that have been operated on and need to be saved
     *  will be pushed to this queue. Queue may not be the best word.
     *  Since it suggests an implementation...
     *  How about ... instead of "pushEntityIntoSaveQueue"
     *  Something like:   "markEntityForSaveOnExit"
     *  Or perhaps longer:"markEntityForSaveOnExitTransaction"
     *  I like the former (shorter) better than the latter (longer)
     * @param ent 
     */
    public void markEntityForSaveOnExit(BaseEntity ent){
        
        //ERROR CHECK:
        //Marking entities to be saved upon EXITING a transaction
        //state should only happen while we are INSIDE a transaction state.
        insideTransactionCheck();
        
        int isInList = _saveTheseEntitiesOnExit.indexOf(ent);
        if(isInList >= 0)
        {   //This is not allowed. Would lead to saving the same entity
            //multiple times in a row. 
            //Question:Why not just exit rather than error?
            //Answer:  The stricter you are, the easier it is to find errors.
            doError("marking entity 2X for save on exit.");
        }
        else
        {
            //If we have never seen this entity before,
            //We will add it to the list of items to be
            //Saved when we exit the transaction state.
           _saveTheseEntitiesOnExit.add(ent); 
        }
    }//FUNC::END
    
    //---Decided NOT to do this. Rather than delete. We will overwrite.      ---
    //---This will de-complexify making new entries into the table a bit when---
    //---Concurrent transactions happen.                                     ---
    //public static void markEntityForDeletionOnExit(BaseEntity be){
    //    insideTransactionCheck();
    //}//FUNC::END
    //public static void markEntitiesForDeletionOnExit(List<BaseEntity> entList){
    //    insideTransactionCheck(); 
    //}//FUNC::END
    
    /** Boilerplate code for beginning a transaction.
     * @return : Returns object representing the transaction we have entered **/
    public Session enterTransaction(){
        
        //BUGFIX: At beginning of enterTransaction, do a check to make sure
        //we are not already inside a transaction. Not allowed to enterTransaction
        //twice in a row. Lack of this check may be why concurrent threads
        //were able to execute without an error showing up:
        outsideTransactionCheck();
        
        //Create new session factory:
        SessionFactory sf = HibernateUtil.getSessionFactory();
        
        //Open a session and ready it for a transaction:
        Session session   = sf.openSession();
        session.beginTransaction();
        
        //These operations must come AFTER transaction is setup:
        ///////////////////////////////////////////////////////////////////
        //before we return this session, store it in static class variable:
        activeTransactionSession = session;
        
        //give the conversation that happesn between enterTransaction() and
        //exitTransaction() a unique global ID.
        _convo_open_id = SynchronizedConversationCounter.getNextOpenID();
        
        //We now need a new list of entities for the new transaction:
        _saveTheseEntitiesOnExit = new ArrayList<BaseEntity>(0);
        
        //Error check must be BELOW assignment of activeTransactionSession
        //Checks pairing of enterTransaction with exitTransaction:
        enterExitErrorCheck(true); //<-- True for entering.
        ///////////////////////////////////////////////////////////////////
        
        //Return the session that represents our transaction we are entering:
        return session;
    }//END::FUNC
   
    /** Saves, Commits, and Closes the session.
     *  Basically, the boiler-plate code for finalizing
     *  a transaction done on an entity.
     * 
     *  DESIGN NOTE:
     *        Though the session is now STORED as a static variable inside
     *        this utility, we will NOT overload or change exitTransaction's
     *        signature to ~accomodate~ an exitTransaction() method that
     *        does NOT take a Session instance.
     * 
     *        Reasoning: The scope that ENTERED the transaction state should
     *        be the scope that EXITS the transaction state.
     * 
     * @param ses :The session object we want to perform this operation on. 
     * @param weHaveEntitiesThatNeedSaving: A redundancy to help catch errors.
     *        If the programming invoking this method fails to supply the
     *        correct boolean, it means that they have most likely made
     *        a logic-error. This required boolean is to help us catch the
     *        error NOW rather than later.
     * 
     *        If (_saveTheseEntitiesOnExit.length > 0 && 
     *            weHaveEntitiesThatNeedSaving == false)
     *        { THEN WE HAVE AN ERROR!!! }
     * 
     *        If (_saveTheseEntitiesOnExit.length == 0 &&
     *            weHaveEntitiesThatNeedSaving == true)
     *        { THEN WE HAVE AN ERROR!!! }
     *                                                                       **/
    public void exitTransaction(Session ses, Boolean weHaveEntitiesThatNeedSaving)
    {
        //BUGFIX: Make sure we are INSIDE transaction state before exiting.
        //This bugfix may lead to finding some concurrency errors.
        insideTransactionCheck();
        
        //ERRROR CHECK: Make sure session object provided is identical
        //to the one that was stored.
        if(ses != activeTransactionSession ){ //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            String msg = "";
            msg +="[Closing utility with a different session instance]";
            msg +="[Than the once used to close it. Concurrency problem]";
            msg +="[Most likely.]";
            doError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        
        //save,commit,close:
        //SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
        //loop through all entities marked to be saved,
        //and save them. 
        saveTheEntitiesErrorCheck(weHaveEntitiesThatNeedSaving);
        
        //regardless of if we have anything that needs saving,
        //we need to get the unique id for this closed conversation:
        _convo_close_id = SynchronizedConversationCounter.getNextCloseID();
        
        if(weHaveEntitiesThatNeedSaving)
        {
            saveMyEntities(ses,_saveTheseEntitiesOnExit);
        }
        ses.getTransaction().commit();
        ses.close();
        //SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
        
        //These operations must come AFTER session save,commit,close
        /////////////////////////////////////////////////////////////
        //No longer need the Session object. Nullify it.
        //THIS MUST COME BEFORE ERROR CHECK:
        activeTransactionSession = null;
        
        //Dump the entities from our no longer relevant transaction.
        _saveTheseEntitiesOnExit = null;
        
        //Checks pairing of enterTransaction with exitTransaction:
        enterExitErrorCheck(false); //<--false for exiting.
        /////////////////////////////////////////////////////////////
        
    }//END::FUNC
    
    /** Saves entities associated with the transaction we are about to close.
     *  These entities needing saving are entities that have been operated on
     *  during the course of the transaction state.
     * @param ses : The session representing our transaction state.
     * @param arr : The array of entities that need to be saved. **/
    private void saveMyEntities(Session ses, ArrayList<BaseEntity> arr){
        //loop through and save all entities:
        int len = arr.size();
        long global_save_id;
        long new_save_counter_value;
        BaseEntity bent = null;
        for(int i = 0; i < len; i++)
        {//FOR I:
            //Get entity to save:
            bent = arr.get(i);
            global_save_id = SynchronizedGlobalSaveCounter.getNextGlobalSaveID();
            bent.setGlobal_save_id(global_save_id);
            
            //incriment save counter:
            new_save_counter_value = bent.getRecord_local_save_count() + 1;
            bent.setRecord_local_save_count(new_save_counter_value);
            
            //debug:
            if(bent.getRecord_local_save_count() > 1){//EEEEEEEEEEEEEEEEEEEEEEEE
                doError("We shouldn't be saving an entity more than once");
            }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            
            //Create linear log of this edit to database.
            //Will hopefully aid in debugging concurrent transactions.
            createTransTableLogForEntityWeAreSaving(bent, ses);
          
            //Save the entity:
            ses.save( bent );
        }//NEXT I (entity)
    }//FUNC::END
    
    /**
     * Creates a "log" within a special table called the trans_table.
     * We do this so that when we run into inevitable concurrency bugs,
     * we will have some way to dissect the problem.
     * @param bent :The base entity we want to log.
     * @param ses  :The session associated with this conversation.
     */
    private void createTransTableLogForEntityWeAreSaving
                                                 (BaseEntity bent, Session ses){
        //Create a log of this in our transaction table:
        long log_count;
        TransTable log = new TransTable();
        log_count = SynchronizedLogCounter.getNextLogID();
        log.setLog_id(log_count); //<--should match primary key.
                                  //unless in production and server was
                                  //stopped and started. Then will see an
                                  //offset between the two values.
        log.setConvo_open_id (_convo_open_id);
        log.setConvo_close_id(_convo_close_id);
        log.setForeign_record_comment(bent.getComment());
        
        //SAVE HACK WORKS: Will have to keep this in mind while working on
        //the rest of this code. Hopefully this is only a problem with
        //auto-generated ID columns. For joining tables, we are going to
        //have to either impliment this hack... Or use proper hibernate
        //@JoinColumn annotations.
        ses.save(bent); //<--hack. Save it to get access to id field. Maybe?
        log.setForeign_record_id(bent.getId());
        
        log.setForeign_table_name(bent.getClass().getSimpleName() );
        ses.save( log );
    }//FUNC::END
    
    /** Throws are error if boolean does not match the 
     *  actual state of transaction. Think of it as an assert statement.
     * 
     *  Details: The transaction may or may not have entities that need
     *           to be saved upon exiting the transaction state.
     *           An error thrown by this function is indicative that
     *           the user of this utility made an incorrect assumption
     *           about the state of the application.
     * 
     *           Logic built upon false assumptions leads to hard to
     *           catch errors. Hence this method.
     * 
     * @param weHaveEntitiesThatNeedSaving: Are there entities that should be
                                            saved upon exiting the transaction
                                            state? **/
    private void saveTheEntitiesErrorCheck
        (Boolean weHaveEntitiesThatNeedSaving){
        
            
        Boolean weHaveError = false;
        String msg = "";
        int numEntitiesThatNeedSaving = _saveTheseEntitiesOnExit.size();

        if ( (numEntitiesThatNeedSaving > 0)  && 
             (weHaveEntitiesThatNeedSaving == false) )
        { 
            msg += "[saveTheEntitiesErrorCheck FAILED]";
            msg += "[REASON: you have entities that need saving.]";
            weHaveError = true;
        }

        if ( (numEntitiesThatNeedSaving == 0)  &&
            (weHaveEntitiesThatNeedSaving == true)    )
        { 
            msg += "[saveTheEntitiesErrorCheck FAILED:]";
            msg += "[REASON: you do NOT have entities that need saving.]";
            weHaveError = true;
        }

        if(weHaveError)
        {
            msg += "[Possibility #1:]";
            msg += "[Error in TransUtil.java logic]";
            msg += "[Possibility #2:]";
            msg += "[User has made incorrect assumption about the]";
            msg += "[State of their program that is utilizing this]";
            msg += "[static utility.]";
            doError(msg);
        }
    }//FUNC::EXIT
   
    /** Will crash program if we are OUTSIDE a transaction. **/
    public void insideTransactionCheck(){
        
        //TODO: Make thread safe.

        if(false == areWeInTransaction)
        {
            String msg = "";
            msg += "[insideTransactionCheck FAILED]";
            msg += "[Please check your transaction enter/exit balancing.]";
            doError(msg);
        }
        
        //when in a transaction state, this utility should keep track of
        //the session instance this transaction state applies to:
        if(null == activeTransactionSession)
        {
            String msg2 = "";
            msg2 += "[insideTransactionCheck FAILED]";
            msg2 += "[Session reference is null for some reason.]";
            doError(msg2);
        }
        
    }//FUNC::END
    
    /** Will crash program if we are INSIDE a transaction. **/
    public void outsideTransactionCheck(){
        
        //TODO: Make thread safe.

        if(true == areWeInTransaction)
        {
            String msg = "";
            msg += "[outsideTransactionCheck FAILED]";
            msg += "[Please check your transaction enter/exit balancing.]";
            msg += "[ALSO: Could be concurrency/multi-thread problem.]";
            doError(msg);
        }
        
        //For good upkeep, we should NOT be tracking a session if we are
        //NOT inside a transaction state.
        if(null != activeTransactionSession)
        {
            String msg2 = "";
            msg2 += "[insideTransactionCheck FAILED]";
            msg2 += "[Session reference should have been]";
            msg2 += "[nullifed upon exiting the transaction state.]";
            msg2 += "[ALSO: Could be concurrency/multi-thread problem.]";
            doError(msg2);
        }
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * For run-time debugging. Catching the problem with this code is
     * better than ending up with cryptic errors due to side-effects of
     * this error going un-noticed till later in program execution.
     * @param isEntering : TRUE == we are entering a transaction state
     *                     FALSE== we are exiting a transaction state
     -------------------------------------------------------------------------*/
    private void enterExitErrorCheck(Boolean isEntering){
        
        //todo: Make thread safe.
        
        if(isEntering)
        {//we are entering a transaction:
            if(areWeInTransaction) //<--already inside a transaction?
            {
                String msg01 = "";
                msg01 += "balancing broken:";
                msg01 += "ENTERING transaction multiple times in a row.";
                doError(msg01);
            }
            else
            {
                areWeInTransaction = true;
            }
        }
        else
        {//we are exiting a transaction:
            if(false == areWeInTransaction) //<--already OUTSIDE a transaction?
            {
                String msg02 = "";
                msg02 += "balancing broken:";
                msg02 += "EXITING transaction multiple times in a row.";
                doError(msg02);
            }
            else
            {
                areWeInTransaction = false;
            }
        }
    }//END::FUNC 
    
    
    /**
     * Used for primary keys. But does NOT have to be primary keys now.
     * @param tableClass : The class of the [table/entity] you want
     *                     the highest key from.
     * @param keyName    : The name of the key. Because:
     *                     1. Key is not always named "id"
     *                     2. Table may have multiple keys per record.
     * @return :Returns ZERO if there are no entries. Assumes that first
     *          key in an ascending list is #1, not #0. **/
    public long getHighestKeyInTable(Class tableClass, String keyName){
       // return getNumberOfRecordsInTable();
        
       Session ses = getActiveTransactionSession();

       //BUG FIX: Edge case of empty table:
        ////////////////////////////////////////////////////////////////////////
        //get number of entries currently in table. If there is nothing,
        //return zero: Returning zero is ASSUMING that our database starts
        //ordering primary keys at "1". Which I have observed in my MySQL
        //database. Cannot gaurantee that is the case for other databases.
        long recordCount = getNumberOfRecordsInTable(tableClass);
        if(0==recordCount){ return 0; }
        ////////////////////////////////////////////////////////////////////////
       
        //SOURCE:
        // http://stackoverflow.com/questions/3743677/
        //                          get-max-value-record-from-table-in-hibernate
        Criteria cri = ses.createCriteria( tableClass ); //<--DO NOT GLOBALLY
        cri = cri.setProjection(Projections.max( keyName ));//FILTER THIS 
        Long boxedOutput = (Long)cri.uniqueResult();        //CRITERIA.
        long unboxedOutput = (long)boxedOutput;
        
        return unboxedOutput;
            
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * FUNCTION NOT TESTED!
     * Gets the highest number of records in a given database table.
     * @param tableClass :The class of one of our entities that represents
     *                    a database table.
     * @return 
     ------------------------------------------------------------------------**/
    public long getNumberOfRecordsInTable(Class tableClass){
                     //getNumberOfRecordsInTable
        Session ses = getActiveTransactionSession();
        
        //SOURCE: How do we count rows in hibernate?
        //http://stackoverflow.com/questions/1372317/
        Criteria cri = ses.createCriteria( tableClass ); //<--DO NOT GLOBALLY
        cri = cri.setProjection(Projections.rowCount());    //FILTER THIS.
        Long boxedOutput = (Long)cri.uniqueResult();
        long unboxedOutput = (long)boxedOutput;
        
        return unboxedOutput;
        
    }//FUNC::END
    
    /**
     * Retrieve a random record from table.
     * @param tableClass : The class we want a record from.
     * @return :Container that will have a random record in it
     *          IF the table has at least one record.
     */
    public BaseEntityContainer getRandomRecord(Class tableClass){
        
        //create our output variable:
        BaseEntityContainer bec;
        
        //STEP 1: Get # of entries in the table:
        long total = getNumberOfRecordsInTable(tableClass);
        if(total <= 0){/////////////////////////////////////////////////////////
            bec = BaseEntityContainer.make_NullAllowed(null);
        }else{
            
            //Create a random index to sample:
            int randIndex;
            randIndex = (int)(Math.random() * total);
            if(randIndex == total){randIndex = 0;}
            
            //Get transaction session and do setup criteria to
            //give us the record indexed at randIndex:
            //http://www.shredzone.de/cilla/page/53/
            //                   how-to-fetch-a-random-entry-with-hibernate.html
            Criteria cri = makeGloballyFilteredCriteria(tableClass);
            cri.setFirstResult(randIndex);
            cri.setMaxResults(1);
            BaseEntity theBase = (BaseEntity)cri.uniqueResult();
            bec = BaseEntityContainer.make(theBase);
        }///////////////////////////////////////////////////////////////////////
        
        //Return a container that may or may not contain a random record:
        return bec;
        
    }//FUNC::END
    
    /** Gets an entity from a table using Long. The value is expected to be
     *  UNIQUE. If there is more than one result, that would be an error.
     * @param tableClass  :The class of the table entity.
     * @param columnName  :column of the table we are looking at.
     * @param columnValue :value column should have if 
     *                     we are to return that record.
     * @return :Returns a container that will have the entity in it IF
     *          the entity was found.
     */
    public BaseEntityContainer getEntityFromTableUsingLong
                        (Class tableClass, String columnName, long columnValue){
     
        //Error check:
        throwErrorIfClassIsNotBaseEntity(tableClass);
          
        //Logic:
        Criteria cri = makeGloballyFilteredCriteria(tableClass);
        cri.add(Restrictions.eq(columnName, columnValue));
        BaseEntity bent = (BaseEntity)cri.uniqueResult();
        BaseEntityContainer op;
        op = BaseEntityContainer.make_NullAllowed(bent);
        return op;
                            
    }//FUNC::END
       
    /**-------------------------------------------------------------------------
     * Similar to getEntityFromTableUsingLong, however will throw error
     * if multiple entries are found.
     * @param tableClass :The class of the table entity.
     * @param columnName :The column name used to find [record/entity]
     * @param columnValue:The value the column should have.
     * @return :A container that may or may not contain an entity.
     ------------------------------------------------------------------------**/
    public BaseEntityContainer getEntityFromTableUsingPrimaryKey
                        (Class tableClass, String columnName, long columnValue){
         //Error check:
        throwErrorIfClassIsNotBaseEntity(tableClass);
          
        //Core Logic:
        Criteria cri = makeGloballyFilteredCriteria(tableClass);
        cri.add(Restrictions.eq(columnName, columnValue));
        
        //Our output var:
        BaseEntityContainer op = null;
        
        //Retrieval and error checking:
        List<BaseEntity> queryResultList = cri.list();
        int listLen = queryResultList.size();
        if(listLen <= 0){
            op = BaseEntityContainer.make_NullAllowed(null);
        }else
        if(listLen == 1){
            BaseEntity uniqueResult = queryResultList.get(0);
            op = BaseEntityContainer.make( uniqueResult );
        }else{//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            String msg = "[ERROR from: getEntityFromTableUsingPrimaryKey]";
            msg += "while attempting to get an entity using a primary key";
            msg += "we ended up with multiple entries. Meaning the";
            msg += "primary key column is NOT being used as such.";
            msg += "if duplicates are allowed, use:";
            msg += "getEntityFromTableUsingLong(...)";
            msg += "but only if duplicates are allowed. Else you will";
            msg += "introduce hard-to-find bugs into the code.";
            doError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //Return the container that may or may not have
        //an entity inside of it.
        if(null==op){ doError("[should never return null. 4242345423fsdvs]"); }
        return op;         
    }//FUNC::END
                        
    public BaseEntityContainer getEntityFromTableUsingUniqueString
                      (Class tableClass, String columnName, String columnValue){
         //Error check:
        throwErrorIfClassIsNotBaseEntity(tableClass);
        
        //Core Logic:
        Criteria cri = makeGloballyFilteredCriteria(tableClass);
        cri.add(Restrictions.eq(columnName, columnValue));
        
        //Our output var:
        BaseEntityContainer op = null;
        
        //Retrieval and error checking:
        List<BaseEntity> queryResultList = cri.list();
        int listLen = queryResultList.size();
        if(listLen <= 0){
            op = BaseEntityContainer.make_NullAllowed(null);
        }else
        if(listLen == 1){
            BaseEntity uniqueResult = queryResultList.get(0);
            op = BaseEntityContainer.make( uniqueResult );
        }else{//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            String msg = "[ERROR from: getEntityFromTableUsingUniqueString]";
            msg += "while attempting to get an entity using a primary key";
            msg += "we ended up with multiple entries. Meaning the";
            msg += "primary key column is NOT being used as such.";
            msg += "if duplicates are allowed, use:";
            msg += "getEntityFromTableUsingLong(...)";
            msg += "but only if duplicates are allowed. Else you will";
            msg += "introduce hard-to-find bugs into the code.";
            doError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //Return the container that may or may not have
        //an entity inside of it.
        if(null==op){ doError("[should never return null. 644566655445]"); }
        return op;         
        
    }//FUNC::END
                        
    private void throwErrorIfClassIsNotBaseEntity(Class tableClass){
        
        TransValidateUtil.assertIsEntityClass(tableClass);
    }//FUNC::END
    
    /** Makes criteria that is pre-populated with app-global configuration.
     *  Original usage: Made so that "dele" columns set to TRUE will be
     *  ignored.
     * @return :A criteria object made from the active transaction session.
     */
    private Criteria makeGloballyFilteredCriteria(Class tableClass){
        
        //Error check:
        insideTransactionCheck();
        
        //Error check:
        throwErrorIfClassIsNotBaseEntity(tableClass);
        
        //Create criteria with some basic settings we want applied to all
        //criteria on this:
        Session ses = getActiveTransactionSession();
        Criteria cri = ses.createCriteria(tableClass);
        
        //Only ignore if DELE column is specifically marked to true.
        //If null or false, we assume NOT marked for deletion.
        //We want to pretend objects marked for deletion do not exist.
        cri.add(Restrictions.ne(VarNameReg.DELE, true));
        
        //return the criteria:
        return cri;
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Marks STRING/VARCHAR records as DELE (marked for deletion)
     * @param table    :Table to match.
     * @param colName  :Column to match.
     * @param colValue :Value of column to match.
     ------------------------------------------------------------------------**/
    public void deleVARCHAR(Class table, String colName, String colValue){
        //Error checks:
        basicErrorChecks_and_lazyFetchErrorCheck_for_STRING(table, colValue);
        
        //get the entities fitting criteria:
        boolean MARKED = false; //dont get entities already marked for deletion.
        List<BaseEntity> bel = getEntities_dele(table,colName,colValue,MARKED);
        
        //mark the entities for deletion and SAVE:
        markEntitiesForDeletionAndSave(bel);
                          
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Marks LONG/INTEGER records as DELE (marked for deletion)
     * @param table    :Table to match.
     * @param colName  :Column to match.
     * @param colValue :Value of column to match.
     ------------------------------------------------------------------------**/
    public void deleINTEGER(Class table, String colName, long colValue){
        //Error checks:
        basicErrorChecks_and_lazyFetchErrorCheck_for_LONG(table, colValue);
        
        //get the entities fitting criteria:
        boolean MARKED = false; //dont get entities already marked for deletion.
        List<BaseEntity> bel = getEntities_dele(table,colName,colValue,MARKED);
        
        //mark the entities for deletion and SAVE:
        markEntitiesForDeletionAndSave(bel);
                          
    }//FUNC::END
    
    /** 
     * Shared code for dele functions. KEEP PRIVATE!
     * NO ERROR CHECKING CODE BECAUSE SHARED INTERNAL CODE.
     * @param table    :Table class we are looking at.
     * @param colName  :Column name we are looking at.
     * @param colValue :The column values we want.
     * @param includeEntitiesAlreadyMarkedForDeletion :(self descriptive)
     */
    private List<BaseEntity> getEntities_dele(Class table, String colName, 
               Object colValue,boolean includeEntitiesAlreadyMarkedForDeletion){
        
        Session ses = getActiveTransactionSession();
        Criteria cri = ses.createCriteria(table);
        cri.add(Restrictions.eq(colName, colValue));
        
        //We might want to ignore columns already marked for
        //deletion if we are just gathering entities so we
        //can mark them as deleted.
        if(false == includeEntitiesAlreadyMarkedForDeletion){
            cri.add(Restrictions.ne(VarNameReg.DELE, TRUE_VALUE));
        }///////////////////////////////////////////////////
        
        List<BaseEntity> bel = cri.list();
        return bel;
    }//FUNC::END
    
    /**
     * Goes through list and sets "dele" values to TRUE.
     * Then does a session.save() on the entity to make sure the
     * change is reflected.
     * @param bel :List of base entities to mark for deletion.
     */
    private void markEntitiesForDeletionAndSave(List<BaseEntity> bel){
        
        long TRUE_INPUT = 1;
        Session s = getActiveTransactionSession();
        for(BaseEntity b : bel){
            b.setDele( TRUE_INPUT );
            s.save(b); //<-- so app knows about change.
        }//NEXT ENTITY
    }//FUNC::END
             
    /**-------------------------------------------------------------------------
     * Do basic boilerplate error checks AND make sure the string is not
     * empty. As this is an indicator of a problem due to lazy fetching.
     * @param tableClass: Class that should extend BaseEntity
     * @param columnValueAsString :The string to check.
     ------------------------------------------------------------------------**/
    public void basicErrorChecks_and_lazyFetchErrorCheck_for_STRING
                                 (Class tableClass, String columnValueAsString){
        basicErrorChecks(tableClass);
        
        //Debug accidential transposition of strings: //TTTTTTTTTTTTTTTTTTTTTTTT
        if(  DebugConfig.isDebugBuild && //TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
             DebugConfig.ifDebugBuild_useStrictDebugCodeThatCanBreakProduction){
            throwErrorIfValueIsTableOrVarName(columnValueAsString);
        }//TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
        
        //Is string non-null and non-empty?
        if(null == columnValueAsString){
            doError("[Null string indicative of lazy fetch error]");
        }else
        if(columnValueAsString.equals("")){
            doError("[Empty string indicative of lazy fetch error]");
        }//BLOCK::END
         
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Do basic boilerplate error checks AND make sure the NUMBER/LONG is not
     * ZERO. As this is an indicator of a problem due to lazy fetching.
     * @param tableClass: Class that should extend BaseEntity
     * @param columnValueAsLong :The long value to check.
     ------------------------------------------------------------------------**/
    private void basicErrorChecks_and_lazyFetchErrorCheck_for_LONG
                                     (Class tableClass, long columnValueAsLong){
        basicErrorChecks(tableClass);
        
        //Is string non-null and non-empty?
        if(0 == columnValueAsLong){
            doError("[ZERO long indicative of lazy fetch error]");
            //If you need zero... re-think your design to NOT need it.
            //Because finding errors due to lazyfetching is going to be
            //very important for productivity.
        }//check long ^
    }//FUNC::END
                                     
    private void basicErrorChecks(Class tableClass){
        //Are we inside a transaction?
        insideTransactionCheck();
        
        //Is tableClass a valid entity?
        throwErrorIfClassIsNotBaseEntity(tableClass);
    }//FUNC::END
           
    /**
     * Throws error if value is a table or variable name.
     * Should only be used in debug mode. Since a user could enter
     * their first name as "session_table" and crash the program.
     * @param value :The value to check.
     */
    private static void throwErrorIfValueIsTableOrVarName(String value){
        if(VarNameReg.contains(value)){
            doError("[TransUtil: Possible transposition error. VarNameReg]");
        }else
        if(TableNameReg.contains(value)){
            doError("[TransUtil: Possible transposition error. TableNameReg]");
        }//BLOCK::END
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = TransUtil_CORE.class;
        err += clazz.getSimpleName();
        err += msg;
        throw new MyError(clazz, err);
    }//FUNC::END
    
}//CLASS::END
