package test.entities.containers;
import test.MyError;
import test.entities.bases.BaseEntity;
/**
 * Say we have a function called "findEntityUsingName"
 * That takes a name. If it finds the entity, it returns an instance of
 * BaseEnity, if the entity is NOT found, it returns NULL.
 * 
 * Returning null is a BAD PRACTICE.
 * So rather than return null, we will return a container that may or may not
 * contain a BaseEntity.
 * 
 * This also gives us redundancy that will help us
 * catch null pointer exceptions:
 * ----------------------------------------------------
 * If the entity property on this container is null,
 * the .has property on this container should be false.
 * 
 * If the entity property on this container is non-null,
 * the .has property on this container should be true.
 * -----------------------------------------------------
 * 
 * @author jmadison 2015.09.02_0451PM */
public class BaseEntityContainer {
    
    public static BaseEntityContainer make(BaseEntity ent){
        
        //ERROR CHECK:
        //If input is null, throw unhandled exception:
        //This behavior is NOT allowed.
        if(null == ent){ throw new MyError("Supplied base entity was null");}
        
        //Create and configure container:
        BaseEntityContainer op = new BaseEntityContainer();
        op.entity = ent;
        op.exists = true;
        
        //return configured container:
        return op;
        
    }//END::FUNC
    
    /** Reference may or may not be null. **/
    public BaseEntity entity;
    
    /** Flag tells us if BaseEntity reference should be null or not. **/
    public Boolean    exists;
    
}//END::CLASS
