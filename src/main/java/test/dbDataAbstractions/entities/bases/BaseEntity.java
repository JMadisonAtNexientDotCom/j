package test.dbDataAbstractions.entities.bases;

import java.io.Serializable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

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
  
  
 * 
 * 
 * 
 * @author jmadison */
@MappedSuperclass
public class BaseEntity implements Serializable{

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
    
    
    //We don't need an errorMessage field. The comment property that
    //I use on my entities should handle communication of what the error
    //happens to be.
    //XXX-dont-use-XXX//public String  errorMessage = "NOT_AN_ERROR";
    
    //Boilerplate implementation of Serializable interface:
    //Usage: Keep compiler happy.
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean equals(Object obj) {
        //BS type checking to keep compiler  happy:
        if(obj instanceof Object){}
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode(); //To change body of generated methods, choose Tools | Templates.
    }
    ////////////////////////////////////////////////////////////////////////////
   
    //This is completely empty for the moment.
    //I only want a "BaseEntity" class so that I can
    //Have stricter typing with my TransactionUtil.
    
    
    
}

