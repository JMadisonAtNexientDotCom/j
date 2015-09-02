package test.entities;

import javax.persistence.MappedSuperclass;

/**
 * A base entity class from which all other entities are derived.
 * Will allow us to better manage any boiler plate code that involves entities.
 * @author jmadison */
@MappedSuperclass
public class BaseEntity {
   
    //This is completely empty for the moment.
    //I only want a "BaseEntity" class so that I can
    //Have stricter typing with my TransactionUtil.
}

