package test.transactions.cargoSystem.dataTypes;

import java.util.ArrayList;
import test.MyError;

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
