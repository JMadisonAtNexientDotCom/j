package test.transactions.cargoSystem.dataTypes;


import java.util.ArrayList;
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
    public ArrayList<BaseEntity> merchandise;
    
    /** The original order slip that was used 
     *  to create this batch of entities. **/
    public OrderSlip receipt;
    
    /** If we are just fetching entities for reference, and don't need to
     *  save them in the database, this should be FALSE.
     *  If entities have been edited and need to be saved in database,
     *  this should be flagged as TRUE.
     **/
    public boolean requiresSaving = false;
    
    
}//CLASS::END
