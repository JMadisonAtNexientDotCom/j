package test.transactions.util;

import java.util.ArrayList;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import test.MyError;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import utils.HibernateUtil;


/**The instance-able core of our static TransUtil. -----------------------------
 * This is my solution to creating a static utility that has one unique
 * instance per thread.
 * 
 * @author jmadison :2015.09.16_0557PM --------------------------------------**/
public class TransUtil_CORE extends ThreadLocalUtilityBase {
     
    
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
            throw new MyError("marking entity 2X for save on exit.");
        }
        else
        {
            //If we have never seen this entity before,
            //We will add it to the list of items to be
            //Saved when we exit the transaction state.
           _saveTheseEntitiesOnExit.add(ent); 
        }
    }//FUNC::END
    
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
            throw new MyError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        
        //save,commit,close:
        //SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
        //loop through all entities marked to be saved,
        //and save them. 
        saveTheEntitiesErrorCheck(weHaveEntitiesThatNeedSaving);
        
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
        for(int i = 0; i < len; i++)
        {
            ses.save( arr.get(i) );
        }
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
            throw new MyError(msg);
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
            throw new MyError(msg);
        }
        
        //when in a transaction state, this utility should keep track of
        //the session instance this transaction state applies to:
        if(null == activeTransactionSession)
        {
            String msg2 = "";
            msg2 += "[insideTransactionCheck FAILED]";
            msg2 += "[Session reference is null for some reason.]";
            throw new MyError(msg2);
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
            throw new MyError(msg);
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
            throw new MyError(msg2);
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
                throw new MyError(msg01);
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
                throw new MyError(msg02);
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
        Criteria cri = ses.createCriteria( tableClass );
        cri = cri.setProjection(Projections.max( keyName ));
        Long boxedOutput = (Long)cri.uniqueResult();
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
        Criteria cri = ses.createCriteria( tableClass );
        cri = cri.setProjection(Projections.rowCount());
        Long boxedOutput = (Long)cri.uniqueResult();
        long unboxedOutput = (long)boxedOutput;
        
        return unboxedOutput;
        
    }//FUNC::END
}//CLASS::END
