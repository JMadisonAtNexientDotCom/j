package test.dbDataAbstractions.entities.bases;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import static test.dbDataAbstractions.entities.tables.NinjaTable.ID_COLUMN;
import test.dbDataAbstractions.entities.tables.RiddleTable;
import test.dbDataAbstractions.entities.tables.RhymeTable;

/**
 * A common base entity representing tables that have two foreign keys.
 * 1: rhyme_id
 * 2: riddle_id
 * 
 * Original usage:
 * To consolidate common code between:
 * RiddleRhymeTruthTable.java &&
 * RiddleRhymeFalseTable.java
 * 
 * Supposidly we can get away with using @IdClass like this without using
 * another class. This looks like some trickery to me. Hope this works.
 * 
 * QUOTE:
 * SOURCE: http://stackoverflow.com/questions/3585034/
 *                                     how-to-map-a-composite-key-with-hibernate
 * There is also a Hibernate-specific solution: 
 * Map multiple properties as @Id properties without declaring 
 * an external class to be the identifier type 
 * (and use the IdClass annotation). 
 * See 5.1.2.1. Composite identifier in the Hibernate manual. 
 * –  boberj Mar 11 '14 at 19:40 
 * END QUOTE:
 * 
 * Hibernate recommends AGAINST using @IdClass, and it would make things
 * weird anyways.
 * @IdClass(RiddleRhymeJoinTableBaseEntity.class)
 * SOURCE: http://docs.jboss.org/hibernate/orm/4.3/manual/en-US/html_single/
 *                                                       #mapping-declaration-id
 * 
 * @author jmadison
 */
//

@MappedSuperclass
public class RiddleRhymeJoinTableBaseEntity extends BaseEntity implements Serializable{
    
    //NOTE: Not sure I understand joing columns or manyToOne...
    //Working mostly off this:
    //http://stackoverflow.com/questions/15426736/how-can-i-create-a-foreign-key-constraint-using-hibernate-annotations
    
    /** Column name stored as string constant for easy refactoring. **/
    public static final String RIDDLE_ID_COLUMN = "riddle_id";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String RHYME_ID_COLUMN  = "rhyme_id";
    
    @Id //COMPOSITE KEY. ID#1
    @Column(name=RIDDLE_ID_COLUMN)
    private long riddle_id;
    
    @Id //COMPOSITE KEY. ID#2
    @Column(name=RHYME_ID_COLUMN)
    private long rhyme_id;

    //Boilerplate getters and setters:
    //GSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGS
    public long getRiddleId() {
        return riddle_id;
    }

    public void setRiddleId(long riddleId) {
        this.riddle_id = riddleId;
    }

    public long getRhymeId() {
        return rhyme_id;
    }

    public void setRhymeId(long rhymeId) {
        this.rhyme_id = rhymeId;
    }
    //GSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGS
    

    //Boilerplate HashCode BS:
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public int hashCode() {
        return super.hashCode(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(Object obj) {
        //BS type checking to keep compiler  happy:
        if(obj instanceof Object){}
        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
    }
    ////////////////////////////////////////////////////////////////////////////
    
    
    
    /**
     * I don't think I understand how two primary keys are supposed to work..
     * Forget using fancy annotations you don't understand. The annotations
     * are supposed to make it easier for you. Make it work first. And then
     * refactor do it properly later. Up and running and working is first priority.
    @Id
    @ManyToOne
    @JoinColumn(name = RiddleTable.ID_COLUMN)
    private RiddleTable riddle_record;

    @Id
    @ManyToOne
    @JoinColumn(name = RhymeTable.ID_COLUMN)
    private RhymeTable rhyme_record;
    **/
    
}//CLASS::END
