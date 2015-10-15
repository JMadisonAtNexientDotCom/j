package test.transactions.cargoSystem.dataTypes;

import java.util.ArrayList;
import java.util.List;
import test.MyError;
import test.dbDataAbstractions.entities.tables.NinjaTable;
import test.transactions.cargoSystem.ports.config.NegativePorts;
import test.transactions.util.TransValidateUtil;
import utils.ArrayUtil;

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
    
    /** Entities need to be loaded into the cargo hold eventually for the
     *  order to be completed. Even if we don't use them. Also, the very act
     *  of loading entities into cargo asserts that those primary key ids
     *  are actually valid. **/
    public boolean areEntitiesLoaded = false;
    
    /** Function that I only added to help document isKeyLoaded variable,
     *  and give it a synonym. Though we could put error checking in it.
     * @return :Returns true if this order is complete. **/
    public boolean isOrderComplete(){
        return ( areKeysLoaded && areEntitiesLoaded);
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
    public SpecialInstructionsStickyNote specs = null;
    
    //Forget "hasSpecs". Specs simply always should be non-null when
    //using an order slip. And maybe the specs list is EMPTY. But don't.
    //use these paired vars. Makes things more verbose and pain.
    //public OrderArg[] specs = null;
    //public boolean hasSpecs = false;
    
   /** Throws error if dependency flags are not properly set. **/
    private static void validateDependencyFlags(OrderSlip order){
        
        boolean has = doesArrayHaveData(order.dependencies);
       
        if((false==has) && true==order.hasDependencies){
            doError("[dependency flag does not agree with data.]");
        }//
        
        if( (true==has) && false==order.hasDependencies){
            doError("[another mis-match for dependency flags]");
        }//
        
    }//FUNC::END 
    
    private static <T> boolean doesArrayHaveData(T[] arr){
        return ArrayUtil.doesArrayHaveData(arr);
    }//FUNC::END
    
    /* xxxxxxxxxxxxxxxxxxxxxxxxx
    // Throws error if SPECS flags are not properly set.//
    private static void validateSpecsFlags(OrderSlip order){
        if(null == order.specs && order.hasSpecs){
            doError("[SPECS flag does not agree with data.]");
        }//
        
        if(null != order.specs && false==order.hasSpecs){
            doError("[another mis-match for SPECS flags]");
        }//
        
        if(null == order.specs){return;}
        if(order.specs.size() <= 0){
            doError("[SPECS flagged as true. But array is empty.]");
        }//
    }//FUNC::END
    xxxxxxxxxxxxxxxxxxxxxxxxx */
    
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
    
    /** Creates an order that is configured to grab directly from a table,
     *  rather than go through a specific port.
     * @param tableEntity    :The supplier table that we grab entities from.
     * @param primaryKey_ids :The primary keys of those entities/records.
     * @return :See above. **/
    public static OrderSlip makeUsingTable
                                 (Class tableEntity, List<Long> primaryKey_ids){
    
        //basic error check inputs:
        if(null == tableEntity){doError("TableEntityIsNull");}
        if(null == primaryKey_ids){doError("nullprimaryKeyIds423432");}
        if(primaryKey_ids.isEmpty()){doError("primaryKeysAreEmpty34234");}
                                     
        OrderSlip op = new OrderSlip();
        op.supplier = tableEntity;
        op.primaryKey_ids = primaryKey_ids;
        op.areKeysLoaded  = true;
        op.specs          = SpecialInstructionsStickyNote.makeReadyFill();
        op.dependencies   = new OrderSlip[0];
        
        //We are NOT going to load using a port!
        op.portID            = NegativePorts.DO_NOT_USE;
        op.loadKeysUsingPort = false;
        
        return op;
    }//FUNC::END
    
    public static OrderSlip makeReadyToPopulateInstance(){
        OrderSlip op      = new OrderSlip();
        op.primaryKey_ids = new ArrayList<Long>();
        op.dependencies   = new OrderSlip[0];
        op.specs          = SpecialInstructionsStickyNote.makeReadyFill();
        return op;
    }//FUNC::END
    
    /** An enabler is someone who SUPPORTS a dependant. If an order has
     *  other orders it DEPENDS ON. We are going to want to fetch those orders
     *  so that we can work with them.
     * @param order :The order that is dependant on other orders.
     * @param supplierTableThisOrderIsDependantOn : Reference to actual table
     *        used to create one of the orders that our input order is dependant
     *        on. This way we can extract an enabling order from the list of
     *        order dependencies.
     *        Example: If one order in the dependency list was made using the
     *        TokenTable and another was made using the NinjaTable, we could
     *        create a reference to the TokenTable's order by:
     *        Order ninja_order = getEnabler(order, NinjaTable.class);
     * @return :Returns the correct enabling order. If order is not found,
     *          or order has no dependency list, will throw error.
     */
    public static OrderSlip getEnabler
                   (OrderSlip order, Class supplierTableThisOrderIsDependantOn){
        
        //check inputs:
        if(null == order){doError("null order input");}
        if(null == supplierTableThisOrderIsDependantOn){doError("nullClass");}
        if(false == TransValidateUtil.isEntityClass
                                        (supplierTableThisOrderIsDependantOn) ){
            doError("input class does not derive from base enitity.");
        }//Entity Class?
                       
        //does object have orders?
        if(order.hasDependencies){doError("[flagged as having no deps]");}
        if(null == order.dependencies){doError("[null dependency list]");}
        if(order.dependencies.length <= 0) {doError("[zerolen deplist]");}
        boolean orderFound = false;
        OrderSlip foundOrder = null;
                       
        for(OrderSlip o : order.dependencies){
            if(o.supplier == supplierTableThisOrderIsDependantOn){
                foundOrder = o;
                break;
            }//match?
        }//next order.
        
        if(orderFound){
            if(false == foundOrder.isOrderComplete()){
                doError("[enabler order found, but not completed!]");
            }//Order not completed?
        }else{
            doError("[Was not able to find enabler order using class!]");
        }//Found or not?
        
        return foundOrder;
                       
    }//FUNC::END
                  
    /** When the barge lands at a port. We want to validate the integrity
     *  of the order before we start filling it.
     * @param order   :The order to validate.
     * @param supplierOrNull:The supplier table that will be supplying the 
     *                 entities. If no supplier table, supply NULL. 
     *                 NULL is ALLOWED.
     */
    public static void preFillCheck(OrderSlip order, Class supplierOrNull){
        
        //might want to replace this call with check to see if order is already
        //filled. But for now, good like this:
        if(order.areEntitiesLoaded){doError("Entities already loaded");}
        
        //Make sure the port can supply the goods you are asking for:
        if(order.supplier != supplierOrNull){
            String msg = "";
            msg += "[You are at the wrong port for this order.]";
            msg += "[Possible problems:]";
            msg += "[1. ERROR in PORT FUNCTION:]";
            msg += "[Port function wrongly identifies the supplier table]";
            msg += "[That it is grabbing from.]";
            msg += "[2. ERROR in DryDock's setup of this order.";
            msg += "[The port functions are okay, but the order setup";
            msg += "[Is mentioning the wrong supplier table.";
            String sName = getNameOfPossiblyNullClass(supplierOrNull);
            String otNme= getNameOfPossiblyNullClass(order.supplier);
            msg += "supplier arg:["      + sName + "]";
            msg += "supplier on order:[" + otNme + "]";
            doError(msg);
        }//FUNC::END
    }//FUNC::END
    
    /**
     * Validate that order slip is correctly configured to fetch directly
     * from a table, rather than going to a port.
     * @param order :The order slip to validate. **/
    public static void assertIsTableOrder(OrderSlip order){
        if(null == order){doError("[null order cannot be valid.]");}
        if(order.areEntitiesLoaded){doError("already filled");}
        if(false == order.areKeysLoaded){doError("should be flagged true!");}
        if(null == order.primaryKey_ids || order.primaryKey_ids.size() <= 0){
            doError("primary keys should exist to extract from table");
        }
        if(order.portID != NegativePorts.DO_NOT_USE){
            doError("portID should be flagged as DO NOT USE!");
        }
        
        //make sure a supplier table exists, and that it is a base entity:
        if(null == order.supplier){doError("SuppliertablecannotBeNull");}
        TransValidateUtil.assertIsEntityClass(order.supplier);
        
        //specs + dependencies allowed to exist.
    }//FUNC::END
    
    private static String getNameOfPossiblyNullClass(Class clazz){
         String cName = (clazz==null?"NULL!":clazz.getCanonicalName());
         return cName;
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
