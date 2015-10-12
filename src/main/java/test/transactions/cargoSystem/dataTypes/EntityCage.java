package test.transactions.cargoSystem.dataTypes;


import java.util.ArrayList;
import java.util.List;
import test.dbDataAbstractions.entities.bases.BaseEntity;


/**
 * 
 * Calling it a EntityCage because the cages can be "unlocked" with a key.
 * Because: The Orders get a PRIMARY KEY back when they are filled out.
 * And those primary keys corresponds to entities stored in the EntityCage.
 * 
 * @author jmadison :2015.10.08
 */
public class EntityCage {
    /** Must be an object that EXTENDS base entity.
     *  Cannot be JUST A BASE-ENTITY. Must extend. **/
    public Class entityClass;
    
    /**
     * Think of the entities as live animals in cages
     * that are merchandise for sale. A bit morbid of
     * an analogy, but I think it will be helpful to remember. **/
    public List<BaseEntity> merchandise;
    
    /** The original order slip that was used 
     *  to create this batch of entities. **/
    public OrderSlip receipt;
    
    /** 
     *  Default to TRUE because it is better to mistakenly SAVE an entity
     *  that has NOT changed than to mistakenly NOT-SAVE an entity
     *  that HAS-CHANGED.
     * 
     *  If we are just fetching entities for reference, and don't need to
     *  save them in the database, this should be FALSE.
     *  If entities have been edited and need to be saved in database,
     *  this should be flagged as TRUE.
     **/
    public boolean requiresSaving = true; //<--DEFAULT TRUE.
    
    /**-------------------------------------------------------------------------
     * Maker function that will make sure all properties from resulting
     * construction are non-null.
     * @param inEntityClass :The entity class that all entities within cage
     *                       should belong to.
     * @param inReceipt     :The original OrderSlip used to gather these
     *                       entities. Used as a receipt.
     * @return :See above.
     ------------------------------------------------------------------------**/
    public static EntityCage make(Class inEntityClass, OrderSlip inReceipt){
        EntityCage op  = new EntityCage();
        op.entityClass = inEntityClass;
        op.receipt     = inReceipt;
        op.merchandise = new ArrayList<BaseEntity>();
        return op;
    }//FUNC::END
    
    
}//CLASS::END
