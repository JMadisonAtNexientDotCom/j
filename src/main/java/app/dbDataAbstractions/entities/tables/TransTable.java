package app.dbDataAbstractions.entities.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import app.config.constants.identifiers.TableNameReg;
import app.config.constants.identifiers.VarNameReg;
import app.dbDataAbstractions.entities.bases.BaseEntity;
import app.dbDataAbstractions.entities.bases.KernelEntity;

/**
 * A table that helps with debugging. Every time we save an entity, we will
 * make an entry into this table to help us debug.
 * @author jmadison :2015.09.30
 * 
 * Note to self: Try making changes to code in smaller steps. Else you get
 *               caught in an indefinite debugging cycle. Don't like indefinite
 *               debugging. Scary. What if the code never recovers!?
 */
@Entity
@Table(name= TransTable.TABLE_NAME) 
public class TransTable extends KernelEntity{
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME           = TableNameReg.TRANS_TABLE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN            = VarNameReg.ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String COMMENT_COLUMN       = VarNameReg.COMMENT;
    
    //COLUMNS UNIQUE TO THIS TABLE://///////////////////////////////////////////
    /** Column name stored as string constant for easy refactoring. **/
    public static final String CONVO_OPEN_ID_COLUMN          = VarNameReg.CONVO_OPEN_ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String CONVO_CLOSE_ID_COLUMN         = VarNameReg.CONVO_CLOSE_ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String LOG_ID_COLUMN                 = VarNameReg.LOG_ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String FOREIGN_TABLE_NAME_COLUMN     = VarNameReg.FOREIGN_TABLE_NAME;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String FOREIGN_RECORD_ID_COLUMN      = VarNameReg.FOREIGN_RECORD_ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String FOREIGN_RECORD_COMMENT_COLUMN = VarNameReg.FOREIGN_RECORD_COMMENT;
    ////////////////////////////////////////////////////////////////////////////
    
    /**-------------------------------------------------------------------------
     *  ID telling us what [conversation/session] this transaction was part of.
     *  The word "session" not to be confused with a session in the session
     *  table. This is why I state, "Conversation". A conversation being
     *  all of the transactions done by a single [thread/connection].
     *  NOT counting transactions occuring from thread pooling
     *  (re-using the connection as if it were a new connection)
     ------------------------------------------------------------------------**/
    @Column(name=CONVO_OPEN_ID_COLUMN)
    private Long convo_open_id;
    
    /**
     * Like convo_open_id, but this value is retrieved and set
     * when the conversation comes to a close. **/
    @Column(name=CONVO_CLOSE_ID_COLUMN)
    private Long convo_close_id;
    
    /** The name of the table involved in the transaction that has just been
     *  saved to the database. **/
    @Column(name=FOREIGN_TABLE_NAME_COLUMN)
    private String foreign_table_name;
    
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
    @Column(name=LOG_ID_COLUMN)
    private Long log_id;
    
    /** The record_id from the table of table_name that is represented
     *  by this transaction log we are making. **/
    @Column(name=FOREIGN_RECORD_ID_COLUMN)
    private Long foreign_record_id;
    
    /** A copy of the the original comment column from the
     *  record of record_id in the table of table_name **/
    @Column(name=FOREIGN_RECORD_COMMENT_COLUMN)
    private String foreign_record_comment;
    
    //Generated getters+setters:
    //GSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGS

    public long getConvo_open_id() {
        return this.unBoxLong(convo_open_id);
    }

    public void setConvo_open_id(long convo_open_id) {
        this.convo_open_id = new Long(convo_open_id);
    }

    public long getConvo_close_id() {
        return this.unBoxLong(convo_close_id);
    }

    public void setConvo_close_id(long convo_close_id) {
        this.convo_close_id = new Long(convo_close_id);
    }
   

    public String getForeign_table_name() {
        return foreign_table_name;
    }

    public void setForeign_table_name(String table_name) {
        this.foreign_table_name = table_name;
    }

    public long getLog_id() {
        return this.unBoxLong(log_id);
    }

    public void setLog_id(long log_id) {
        this.log_id = new Long(log_id);
    }

    public long getForeign_record_id() {
        return this.unBoxLong(foreign_record_id);
    }

    public void setForeign_record_id(long record_id) {
        this.foreign_record_id = new Long(record_id);
    }

    public String getForeign_record_comment() {
        return foreign_record_comment;
    }

    public void setForeign_record_comment(String record_comment) {
        this.foreign_record_comment = record_comment;
    }
    //GSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGS
    
}//CLASS::END
