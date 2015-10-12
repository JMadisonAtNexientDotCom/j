package test.transactions.cargoSystem.dataTypes;

import java.util.ArrayList;
import java.util.List;
import test.MyError;

/**
 * An OrderSlip is a yellow slip of carbon paper that has order information
 * on it. It describes the data we want, as well as where to get it. And
 * also records a receipt of that data.
 * 
 * Note on name:
 * Called "OrderSlip". The core concept is "Order".
 * The "Slip" part really only exists so we can refer to instances
 * of this object as "order" with minimal confusion.
 * I am not a fan of the whole: "variable name identical to class name except
 * for the case" thing. Example: Session session = new Session(); YUCK.
 * 
 * @author jmadison :2015.10.08
 */
public class OrderSlip {
    
    public static final long KEY_ID_READY_FOR_LOAD = (-1);
    public static final long KEY_ID_HAS_INIT_ERROR = 0;
    
    public static final short NO_PORT = (-2);
    public static final short PORT_HANDLE_NOT_INITIALIZED = 0;
    
    /**
     * What port has the entity merchandise we want for this order?
     * AKA: A handle to the function used to evaluate this order. **/
    public short portID = PORT_HANDLE_NOT_INITIALIZED;
    
    /** The Class of the table entity that will supply the data.
        Decided this makes more sense than a var called "key_name" or
        "column_name". What keys and columns to work with really depend
        on the portName (function handle) **/
    public Class supplier;
    
    /**
     * This is the PRIMARY KEY of the item in the database that we have
     * gotten from this order. All ports should give you this.
     */
    public List<Long> primaryKey_ids;
    
    /** Should be we load the primary key value using a port? **/
    public boolean loadKeysUsingPort = false;
    
    /** Is they primary key loaded? AKA: Is this order complete?
     *  When creating a bunch of order slips, there could be one order
     *  on which all other orders are dependant on. That order might
     *  come with INJECTED/LOADED keys. So it does not need to go to a port
     *  to get the keys.
     */
    public boolean areKeysLoaded = false;
    
    /** Function that I only added to help document isKeyLoaded variable,
     *  and give it a synonym. Though we could put error checking in it.
     * @return :Returns true if this order is complete. **/
    public boolean isOrderComplete(){
        return areKeysLoaded;
    }//FUNC::END
    
    //uncessarry and will just needlessly complexify things.
    //if order object is in agenda, we use it. End of story.
    //no pretending it doesn't exist. We are not going to
    //be recycling and pooling these objects at the moment.
    //xx///public boolean exists = false;
    
    /**
     * References OTHER ORDERS that must be completed before
     * this order can be made. A dependency is basically a special
     * order argument. The primary key of the completed OrderSlip will
     * be used to satisfy the inputs of the port.
     */
    public OrderSlip[] dependencies = null;
    public boolean hasDependencies = false;
    
    public int getNumberOfDependencies(){
        validateDependencyFlags(this);
        
        //now count number of dependencys.
        if(false==hasDependencies){return 0;}
        return dependencies.length;
        
    }//FUNC::END
    
    /** Specifications/extra detailed instructions for
     *  what you want from the port.
     * 
     *  Imagine that portName = "PIZZA_HUT_PORT";
     *  Going to this port without any special instructions gets
     *  you a pepperoni pizza. But if you specify specific special
     *  instructions, you could get pineapple pizza.
     */
    public OrderArg[] specs = null;
    public boolean hasSpecs = false;
    
   /** Throws error if dependency flags are not properly set. **/
    private static void validateDependencyFlags(OrderSlip order){
        if(null == order.dependencies && order.hasDependencies){
            doError("[dependency flag does not agree with data.]");
        }//
        
        if(null != order.dependencies && false==order.hasDependencies){
            doError("[another mis-match for dependency flags]");
        }//
        
        if(null == order.dependencies){return;}
        if(order.dependencies.length <= 0){
            doError("[Dependencies flagged as true. But array is empty.]");
        }//
    }//FUNC::END
    
    /** Throws error if SPECS flags are not properly set. **/
    private static void validateSpecsFlags(OrderSlip order){
        if(null == order.specs && order.hasSpecs){
            doError("[SPECS flag does not agree with data.]");
        }//
        
        if(null != order.specs && false==order.hasSpecs){
            doError("[another mis-match for SPECS flags]");
        }//
        
        if(null == order.specs){return;}
        if(order.specs.length <= 0){
            doError("[SPECS flagged as true. But array is empty.]");
        }//
    }//FUNC::END
    
    /**
     * @param orders:Array of orders
     * @param receipt:The particular order you want to find.
     * @return :Index of receipt within orders.
     *          If not found, returns (-1);
     */
    public static int getIndexOf(OrderSlip[] orders, OrderSlip receipt){
        OrderSlip cur;

        int len = orders.length;
        for(int i = 0; i < len; i++){
            cur = orders[i];
            //ITEM FOUND AT INDEX I:
            if(cur==receipt){return i;}
        }//NEXT i
        
        //Item not found:
        return (-1);
    }//FUNC::END
    
    /**
     * Will get you started and reduce a bit of boilerplate code.
     * But you'll still have to do more config to get the results you want.
     * @param inPortID :Function handle for what you want to achieve.
     * @return         :An order configured to use the portID specified
     *                  to fufill the order.
     */
    public static OrderSlip makeUsingPortID(short inPortID){
        OrderSlip op = makeReadyToPopulateInstance();
        op.portID            = inPortID;
        op.loadKeysUsingPort = true;
        
        //to get this to work, you may have to edit the lookup table.
        //op.supplier          = resolvePortIDToSupplyingTableEntity(inPortID);
        return op;
    }//FUNC::END
    
    public static OrderSlip makeReadyToPopulateInstance(){
        OrderSlip op = new OrderSlip();
        op.primaryKey_ids = new ArrayList<Long>();
        op.dependencies   = new OrderSlip[0];
        op.specs          = new OrderArg[0];
        return op;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = OrderSlip.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
    
}//CLASS::END
