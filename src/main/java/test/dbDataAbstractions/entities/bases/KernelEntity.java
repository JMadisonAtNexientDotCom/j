package test.dbDataAbstractions.entities.bases;

import java.io.Serializable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import test.config.constants.EntityErrorCodes;
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
    
    /** though this is a one line function. Best to use it in case we want
     *  to add error checking logic or something later. 
     * @param where_is_my_box: value to be boxed up.
     * @return : long value-type --> Long ref-type. **/
    protected long boxUpLong(long where_is_my_box){
        return new Long(where_is_my_box);
    }//rawer.
    
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
    
    
    
}//CLASS::END
