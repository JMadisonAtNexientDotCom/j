package test.transactions.cargoSystem.managerTypes;

import annotations.IndexedFunctionTable;
import java.lang.reflect.Method;
import test.MyError;
import test.transactions.cargoSystem.dataTypes.EntityCage;
import test.transactions.cargoSystem.dataTypes.GalleonBarge;
import test.transactions.cargoSystem.dataTypes.OrderSlip;
import test.transactions.cargoSystem.ports.TokenPorts;

/**
 * This is a static registry class that helps resolve the different
 * port names. Because we cannot pass functions as variables in java,
 * we need this object to register the function handle names referenced
 * by the "port name" field of our OrderSlip objects.
 * 
 * NOTE: Not quite sure how I will implement this.
 *       Just have a basic idea of how it will work as of the
 *       time of writing this documentation.
 * 
 * @author jmadison :2014.10.08_0900PM
 */
public class BeaconLightHouse {
    
    /**Parameter signature that all ports take. **/
    private static Class[] _paramTypes;
    
    static{//////////////
        doStaticInit();
    }////////////////////
    
    private static IndexedFunctionTable _ports;
    
    private static void doStaticInit(){
        setup();
    }//FUNC::END
    
    /** Sets up our lookup tables so that the beacon light house can
     *  direct the Barge to the correct functions. **/
    private static void setup(){
        
        //Create the signature used to fetch the
        //methods from the lookup table:
        _paramTypes = new Class[2];
        _paramTypes[0] = GalleonBarge.class;
        _paramTypes[1] = OrderSlip.class;
        
        //Create new indexed function scanner
        //And register all the annotated classes:
        //Adding will also cause scanning.
        _ports = new IndexedFunctionTable();
        _ports.addClass(TokenPorts.class);
        
        //Build after all has been added:
        _ports.build();
        
    }//FUNC::END
    
    /** Manages the resolution of a given OrderSlip
     *  by looking at the portName (program handle) and routing to
     *  the correct port for transactions to take place on the ship.
     * @param barge :The cargo-ship we are loading with data.
     * @param order :The current order the captain of the ship wants
     *               ~fufilled~. Example: Go to treasure island and get
     *               5 pieces of gold.
     *               AKA: Get 5 new tokens out of the token table.
     */
    public static void guideShipToPort(GalleonBarge barge, OrderSlip order){
        
        //Error check inputs:
        if(null == barge){doError("[input barge is null]");}
        if(null == order){doError("[input order is null]");}
        if(null == barge.hold){doError("[cargo hold is null]");}
        if(barge.hold.isFilled){
            String msg = "[hold should not be filled if you are still]";
            msg += "[on quest to fufil your agenda.]";
            doError(msg);
        }//ERROR?
        
        //Get the correct port from the OrderSlip,
        //This is basically, the destination where we will find what
        //We need to fufill the order. Example: Maybe it is an order
        //For getting BLUE Giraffees. And the order.portID points to a port
        //that holds the function "getGiraffeesByColor(...)"
        Method m = _ports.getMethodByIndex(order.portID, _paramTypes);
        
        //Try catches, stupid. Don't run your program in
        //a broken state. Ever. The longer you put off fixing an error,
        //the longer it takes to fix. An error that would take 1 hour to fix
        //will take 24hours to fix if you put it off for 3 months.
        try{//TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
           m.invoke(m, _paramTypes[0], _paramTypes[1]); 
        }catch(Exception exep){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            doError("[Failed to invoke method!]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //Todo: Validate that for every deposited primary key, there is
        //also the corresponding entity.
        EntityCage cage = barge.hold.getCageUsingReceipt(order);
        if(cage.merchandise.size() != order.primaryKey_ids.size()){
            String msg = "";
            msg+="[Keys did not evenly pair with actual entities.]";
            msg+="[This is a requirement!]";
           doError(msg);
        }//Checksum error.
        
    }//FUNC::END
    
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = BeaconLightHouse.class;
        err += clazz.getSimpleName();
        err += msg;
        throw new MyError(clazz, err);
    }//FUNC::END
    
}//CLASS::END
