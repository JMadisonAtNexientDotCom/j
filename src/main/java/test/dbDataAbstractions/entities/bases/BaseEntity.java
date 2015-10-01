package test.dbDataAbstractions.entities.bases;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import test.MyError;
import test.config.constants.EntityErrorCodes;
import test.config.constants.identifiers.VarNameReg;

/**
 * A base entity class from which all other entities are derived.
 * Will allow us to better manage any boiler plate code that involves entities.
 * 
 * DESIGN SPECS FOR ENTITIES:
 * public getters + setters should be CAMEL CASE.
 * private variables should be IDENTICAL to what they are in the the table they
 * represent. This simplifies the creation of queries. Because HIBERNATE
 * looks at the ACTUAL private variable name using reflection rather than
 * the @Column mapping.
 * 
 * Example Case to explain better: 
 * /////////////////////////////////////////////////////////////////////////////
 * 
 *  ------------------RiddleRhymeTransUtil_Truth.java : Relevant Data:----------
    // public static final String RIDDLE_ID_COLUMN = "riddle_id";
    // public static final String RHYME_ID_COLUMN  = "rhyme_id";
    
    //@Id //COMPOSITE KEY. ID#1
    //@Column(name=RIDDLE_ID_COLUMN)
    //private long riddle_id;
    
    //@Id //COMPOSITE KEY. ID#2
    //@Column(name=RHYME_ID_COLUMN)
    //private long rhyme_id;
  
    //-------------RiddleRhymeTransUtil_Truth.java : Relevant Data--------------
    //Criteria c = ses.createCriteria(RiddleRhymeTruthTable.class);
    //c.add(Restrictions.eq(RiddleRhymeTruthTable.RIDDLE_ID_COLUMN,riddleID));
    //c.add(Restrictions.eq(RiddleRhymeTruthTable.RHYME_ID_COLUMN ,rhymeID ));
    //List results = c.list();

  The first argument of Restrictions.eq() call wants the entity's private
  variable name. NOT the getter/setter. NOT the table column name in database.
  If I could use reflection to get private var... That might be the way to go...
  But a better solution I think is just to have riddle_id be the name of the
  variable in both the database table's column, and in the entity.
    
  //////////////////////////////////////////////////////////////////////////////
  
  The first piece of criteria actually wants the name of the ENTITIY's private
  variable of that name. NOT the column name in the database. To simplify
  this, the private variable of the entity and the column name in database
  should have IDENTICAL NAMES.
  
  DESIGN NOTE: JMADISON: 2015.09.30
               I've never done any of this before. Making a lot of mistakes.
               I cannot accurately predict what I will need for this.. And
               rather than expend too much effort hypothesizing about what
               problems I am going to encounter, I want to make a system where
               I have a lot of insight as to what is going on. As such,
               I have decided that DELE, COMMENT, TRANS_ID, SAVE_COUNTER
               will be required of all base entities. This will give me a
               structure that will hopefully help me find problems.
 * 
 * 
 * 
 * @author jmadison */
@MappedSuperclass
public class BaseEntity extends KernelEntity{
    
   
    /** DELE != DELETE. DELE == To mark something for deletion.
     *  I was thinking about the word "kill" but "dele" is more descriptive
     *  and more proper grammar for what I intend to communicate.
     * 
     *  Rather than deleting data, we just mark it as "dele" and the data is
     *  ignored. Objects marked as "dele" should not be taken into consideration
     *  when validating the integrity of a database.
     */
    @Column(name=VarNameReg.DELE) //, nullable = true)
    private boolean dele;

    public boolean getDele() {
        return dele;
    }

    public void setDele(boolean dele) {
        this.dele = dele;
    }
    
    
    /** A comment column where you can put a comment to help with debug.
     *  REQUIRED for base entity. But not required to use it. 
     *
     **/
    @Column(name=VarNameReg.COMMENT)
    private String comment;
    public void setComment(String inComment){
        this.comment = inComment;
    }//FUNC::END
    public String getComment(){ return this.comment;}
    
    /** 
     *  COMMENT: 2015.09.30_0326PM
     *  It think "GLOBAL_SAVE_ID" is better than mutate id...
     *  It is more descriptive of situation.
     * 
     *  OLD COMMENT: PRE: 2015.09.30:
     *  A global and unique transaction id that is unique across all tables.
     *  This transaction ID is updated whenever a new record is made, or a
     *  record is overwritten/updated. The trans_id does NOT change from
     *  simply viewing the record. 
     *
     **/
    @Column(name=VarNameReg.GLOBAL_SAVE_ID)
    private long global_save_id;
    
    public long getGlobal_save_id() {
        return global_save_id;
    }

    public void setGlobal_save_id(long save_id) {
        this.global_save_id = save_id;
    }
    
    
    
    
    
    /** For debugging. See how many times a record has been saved.
     *  For the design I am going for, this should always be ONE.
     *  Because we create new records rather than overwrite.
     *  And we mark the old records for deletion.
     * 
     *  This way, we have a sort of log going where we can look into the
     *  sql database tables and see if anything is amiss. 
     *
     **/
    @Column(name=VarNameReg.RECORD_LOCAL_SAVE_COUNT)
    private long record_local_save_count;

    public long getRecord_local_save_count() {
        return record_local_save_count;
    }

    public void setRecord_local_save_count(long save_count) {
        this.record_local_save_count = save_count;
    }
    
    

    /**-------------------------------------------------------------------------
     * Used to let UI people know if the response sent back is an 
     * error-response. Rather than change the STRUCTURE of information
     * sent back, we simple edit these flags.
     * 
     * NOTE ON COMPILER WARNING:
     * WARNING: http://forums.netbeans.org/topic53754.html
     * 
     * //http://forums.netbeans.org/topic53754.html
     * Okay, it seems while Hibernate allows it, JPA explicitly forbids this:
     * JPA 2.0 "2.2 Persistent Fields and Properties" (PDF p22): 
     * 
     * JMadison Note:
     * My understanding: You want to access entity properties via
     * getters and setters in case object gets wrapped into a proxy.
     * 
     * DESIGN PROBLEM:
     * I want this to be serialized. But NOT show up in SQL table.
     * NICE! It works this way. Meaning when UI people pull down the JSON
     * they will have the .isError property, but when data is packed into
     * database table, that .isError will be ignored.
     * 
     * 
     ------------------------------------------------------------------------**/
    @Transient
    private boolean isError      = false;
    public void setIsError(boolean tf){
        isError = tf;
    }//FUNC::END
    public boolean getIsError(){
        return isError;
    }//FUNC::END
    
    //Added so that it matches our TypeWithComment base class.
    @Transient
    private String errorCode = EntityErrorCodes.NONE_SET;
    public void setErrorCode(String errCode){
        errorCode = errCode;
    }//FUNC::END
    public String getErrorCode(){
        return errorCode;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = BaseEntity.class;
        err += clazz.getSimpleName();
        err += msg;
        throw new MyError(clazz, err);
    }//FUNC::END
    
}//CLASS::END

