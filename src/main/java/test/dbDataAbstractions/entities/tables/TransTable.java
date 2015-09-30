package test.dbDataAbstractions.entities.tables;

import test.dbDataAbstractions.entities.bases.BaseEntity;

/**
 * A table that helps with debugging. Every time we save an entity, we will
 * make an entry into this table to help us debug.
 * @author jmadison :2015.09.30
 * 
 * Note to self: Try making changes to code in smaller steps. Else you get
 *               caught in an indefinite debugging cycle. Don't like indefinite
 *               debugging. Scary. What if the code never recovers!?
 */
public class TransTable extends BaseEntity{
    
    /**-------------------------------------------------------------------------
     *  ID telling us what [conversation/session] this transaction was part of.
     *  The word "session" not to be confused with a session in the session
     *  table. This is why I state, "Conversation". A conversation being
     *  all of the transactions done by a single [thread/connection].
     *  NOT counting transactions occuring from thread pooling
     *  (re-using the connection as if it were a new connection)
     ------------------------------------------------------------------------**/
    private long convo_id;
    
    /** The name of the table involved in the transaction that has just been
     *  saved to the database. **/
    private String table_name;
    
    /** 
     *  NEW NOTES: 2014.09.30:
     *  Calling "log_id" because we want a unique ID put into the log every
     *  time an entry is made. Ideally should be identically to our primary
     *  id key. But could be offset if we boot up the app without dropping
     *  all of the tables.
     * 
     *  OLD NOTES: (Pre 2015.09.30)
     *  Keeps track of the exact order of our transactions. Called
     *  "mutate_id" rather than "transaction_id" because it is only counting
     *  transactions that [EDIT/CHANGE/ALTER] information in the database. **/
    private long log_id;
    
    /** The record_id from the table of table_name that is represented
     *  by this transaction log we are making. **/
    private long record_id;
    
    /** A copy of the the original comment column from the
     *  record of record_id in the table of table_name **/
    private String record_comment;
    
    //GSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGS
    
    
    //GSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGS
    
    
}//CLASS::END
