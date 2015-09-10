package test.entities;

import java.io.Serializable;
import javax.persistence.MappedSuperclass;

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

