package test.transactions.cargoSystem.dataTypes;

import java.util.ArrayList;
import java.util.List;
import test.MyError;
import test.transactions.util.TransValidateUtil;

/**
 * Main container that contains all of the EntityCages.
 * Contains all of the entities that have been collected from the orders.
 * 
 * In contrast to filled out OrderSlip(s), that contain only the
 * primary key id's of the information.
 * 
 * @author jmadison : 2015.10.08
 */
public class CargoHold {
    
    /** Contains all of our entities collected from the orders. **/
    public ArrayList<EntityCage> cages = null;
    
    /** Set to true when all orders from the AgendaClipBoard have been
     *  made. */
    public boolean isFilled = false;
    
    /**
     * In this context, an OrderSlip becomes used as a receipt
     * once the order has been fufilled.
     * @param receipt :The original order used to get this information.
     * @return        :Returns the entity cage associated with order.
     *                 Throws error if unable to.
     */
    public EntityCage getCageUsingReceipt(OrderSlip receipt){
        if(null == cages){doError("[Cages are null!]");}
        if(null == receipt){doError("[receipt is null!]");}
        
        for(EntityCage c : cages){
            if(c.receipt == receipt){
                return c;
            }//CAGE FOUND!
        }//Next cage.
        
        doError("[Cage was not found by using receipt]");
        return null;
        
    }//FUNC::END
    
    /** Returns all of the entity cages that were supplied from
     *  a given table in the database.
     * 
     *  EXPECTS 2 or more! PLURAL! Will throw error if not.
     *  If you expected only ONE, then use getSupplierCage.
     * 
     * @param supplierTable :The class representing a single database table.
     * @return :A list of cages that fit the criteria.
     */
    public List<EntityCage> allCagesBySupplier(Class supplierTable){
        List<EntityCage> op = getAllCagesLinkedToSupplier(supplierTable);
        if(op.size() < 2){
            String msg ="";
            msg += "[Command expected 2 or more results.]";
            msg += "[If you were expecting 1, use getCageUsingSupplier.]";
            msg += "[If you were expecting 0, DON'T CALL ANYTHING.]";
            msg += "[If you don't know what to expect...]";
            msg += "[...you are doing it wrong.]";
            doError(msg);
        }//ERROR!
        
        return op;
    }//FUNC::END
    
    /** Expects there to be ONE entitycage registered to the supplier.
     *  If more, we have error.
     * @param supplierTable
     * @return 
     */
    public EntityCage getCageUsingSupplier(Class supplierTable){
        List<EntityCage> op = getAllCagesLinkedToSupplier(supplierTable);
        if(op.size() != 1){
            String msg ="";
            msg += "[Command expected EXACTLY ONE(1) result.]";
            msg += "[If you were expecting 2+, use allCagesBySupplier.]";
            msg += "[If you were expecting 0, DON'T CALL ANYTHING.]";
            msg += "[If you don't know what to expect...]";
            msg += "[...you are doing it wrong.]";
            doError(msg);
        }//ERROR!
        
        //Take the one and only result and return it:
        return op.get(0);
        
    }//FUNC::END
    
    /**
     * Core function used by getSupplierCage and getCagesUsingSupplier
     * @param supplierTable :The entity class representing a single table.
     * @return :Returns a list with length 0 to whatever of cages that
     *          are registered with that table.
     */
    private List<EntityCage> getAllCagesLinkedToSupplier(Class supplierTable){
        
        TransValidateUtil.assertIsEntityClass(supplierTable);
        if(null == cages){doError("[Cages are null. Cannot iterate over.]");}
        
        //This is an error, because user of system should have expectation
        //of what they are getting.
        if(cages.size() <= 0){
            doError("[No cages to search! Cages list is empty.]");
            return null;
        }//ERROR END.
        
        List<EntityCage> op = new ArrayList<EntityCage>();
        for(EntityCage ec : cages){
            
            if(null == ec.entityClass){
                doError("[No entityClass was stored with this cage!]");
                return null;
            }//End Func.
            
            if(ec.entityClass == supplierTable){
                op.add(ec);
            }//Valid entry found!
        }//Next cage.
        
        //ZERO IS AN ERROR. When calling, we expect there to be at least
        //one item. If you DON'T KNOW if there are any items... You are
        //using the architecture improperly. This check is here because
        //throwing errors that signify the programmer doesn't know what they
        //are doing is an EXCELLENT OPPROTUNITY. Because logic errors are some
        //of the most sinister and hardest to find errors in code.
        //And ideally we want our error checking to do things that the compiler
        //CANNOT do because the compiler is not HUMAN and has no insights into
        //the larger system.
        if(op.size() <= 0){
            String msg = "";
            msg += "[Entity cages were found.]";
            msg += "[But none registered to the supplier you specified.]";
            doError(msg);
        }//NO SUPPLIERS FOUND
            
        return op;
 
    }//FUNC::END
    
    /** make an empty cargo hold that is ready to be filled. **/
    public static CargoHold make(){
        /** op=="output" **/
        CargoHold op = new CargoHold();
        op.cages     = new ArrayList<EntityCage>();
        op.isFilled  = false;
        return op;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = CargoHold.class;
        err += clazz.getSimpleName();
        err += msg;
        throw new MyError(clazz, err);
    }//FUNC::END
    
}//CLASS::END
