package test.dbDataAbstractions.entities.bases;

import java.io.Serializable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import test.config.constants.identifiers.VarNameReg;

/**
 * The BASE class of my base entity. I needed something a bit more cut up.
 * Because of the logging+debug info I put in the base entity.
 * That information should not be put into the trans table.
 * 
 * So, the trans table will be a KERNEL entity unlike all the others that
 * will be a base entity.
 * 
 * ORIGINAL REASON FOR MAKING THIS CLASS:
 * trans_table that is used to log transactions does not use the logging
 * info from base entity because it itself is logging info.
 * Left me with many un-filled columns that make it look unprofessional.
 * Not to mention distracting and confusing.
 * 
 * @author jmadison :2015.09.30
 */
@MappedSuperclass
public class KernelEntity implements Serializable{
    
    
    
    /**-------------------------------------------------------------------------
     *  Unboxes a long. Null input is considered ZERO.
     *  Using this because hibernate will init some new
     *  columns as NULL and then when you try to incriment
     *  them, all hell breaks looks with the dreaded
     *  null pointer exception.
     * 
     * @param help_iam_in_a_box :long value to save from box.
     * @return :The inputted REF-TYPE:Long (or null) 
     *          converted to VALUE-TYPE:long.
     ------------------------------------------------------------------------**/
    protected long unBoxLong(Long help_iam_in_a_box){
        
        if(null == help_iam_in_a_box){
            return (new Long(0)).longValue();
        }//000000000000000000000000000000000
        
        //If not null, unbox as usual:
        return help_iam_in_a_box.longValue();
    }//FUNC::END
    
    /**
     * UnBox a boolean. So hibernate doesn't crash on you!
     * @param bool_in_a_box :The boolean in a box.
     * @return :The boolean taken out of the Boolean.
     */
    protected boolean unBoxBool(Boolean bool_in_a_box){
        if(null == bool_in_a_box){
            return false; //null is FALSE!
        }//000000000000000000000000000000000
        
        //If not null, unbox as usual:
        return bool_in_a_box.booleanValue();
    }//FUNC::END
    
     //Deciding on some required fields for entities:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name=VarNameReg.ID) // unique = true, nullable = false)
    //@Access(AccessType.PROPERTY) <--Don't use this either.
    //@Access(AccessType.FIELD) <---take this off. You don't know what you are doing.
    private Long id; //<--hibernate wants REF-TYPES.
    public long getId()
    { 
        return unBoxLong( this.id);
    }
    
    /** We use setId when configuring entities as error responses.
     *  Other than that, setId should not be used. As it is auto generated
     *  primary key used by hibernate.
     * @param id :The ID value to set.
     */
    public void setId(long id){ this.id = id;}
    
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
        //To change body of generated methods, choose Tools | Templates.
        return super.hashCode(); 
    }
    ////////////////////////////////////////////////////////////////////////////
    
    
}//CLASS::END
