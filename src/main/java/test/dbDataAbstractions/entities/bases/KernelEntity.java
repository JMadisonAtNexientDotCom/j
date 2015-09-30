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
    
     //Deciding on some required fields for entities:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name=VarNameReg.ID) // unique = true, nullable = false)
    //@Access(AccessType.PROPERTY)
    @Access(AccessType.FIELD)
    private long id;
    public long getId(){ return this.id;}
    
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
