package test.dbDataAbstractions.entities.containers;
import test.MyError;
import test.dbDataAbstractions.entities.bases.BaseEntity;
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
    
    /** This function is used so that functions do NOT have to return null.
     *  Use this function when the BaseEntity is ALLOWED to be null. If the
     *  base entity is NOT allowed to be null, 
     *  use the standard make function. 
     * 
     *  Original Usages:
     *  1. TokenTransUtil.getTokenEntityUsingTokenString();
     *  2. RiddleTransUtil.getEntityByID();
     * 
     * @param possiblyNullEntityReference: An entity reference that 
     *                                     is allowed to be null
     * @return : A properly configured BaseEntityContainer.
     */
    public static BaseEntityContainer make_NullAllowed
                                       (BaseEntity possiblyNullEntityReference){
       BaseEntityContainer op = new BaseEntityContainer();
        if(null == possiblyNullEntityReference){   
            //make container configured as empty:
            op.entity = null;
            op.exists = false;
        }else{   
            //make container configured as populated:
            op.entity = possiblyNullEntityReference;
            op.exists = true;
        }//END-IF     
        return op;
    }//FUNC::END
    
    /** Reference may or may not be null. **/
    public BaseEntity entity;
    
    /** Flag tells us if BaseEntity reference should be null or not. **/
    public Boolean    exists;
    
}//END::CLASS