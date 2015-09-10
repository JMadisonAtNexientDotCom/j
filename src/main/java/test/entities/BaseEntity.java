package test.entities;

import java.io.Serializable;
import javax.persistence.MappedSuperclass;

/**
 * A base entity class from which all other entities are derived.
 * Will allow us to better manage any boiler plate code that involves entities.
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

