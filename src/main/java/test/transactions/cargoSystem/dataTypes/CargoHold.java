package test.transactions.cargoSystem.dataTypes;

import java.util.ArrayList;

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
    
    /** make an empty cargo hold that is ready to be filled. **/
    public static CargoHold make(){
        /** op=="output" **/
        CargoHold op = new CargoHold();
        op.cages     = new ArrayList<EntityCage>();
        op.isFilled  = false;
        return op;
    }//FUNC::END
    
}//CLASS::END
