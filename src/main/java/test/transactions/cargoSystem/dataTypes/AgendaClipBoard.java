package test.transactions.cargoSystem.dataTypes;

import java.util.ArrayList;
import java.util.List;
import test.MyError;

/**
 * An Agenda is a clipboard with a list of Order slips on it. The captain
 * of the Barge uses this in order to figure out what Port to go to next:
 * 
 * Note on naming convention:
 * Wanted to call it "Agenda.java" but then realized it would make it more
 * annoying to reference instances in code.
 * 
 * "Agenda" is the core concept.
 * "ClipBoard" is extra description that is really just there to allow us
 * to call an instance "agenda".
 * 
 * ---------------------- OVERVIEW OF SYSTEM -----------------------------------
 * /////////////////////////////////////////////////////////////////////////////
 * Agenda : Un-ordered list of items to collect. Some entries can be wired
 *          to be ~dependendant~ on other entries.
 * 
 * Cargo  : The ENTITY reference resulting from an Order on the Agenda.
 * 
 * Order  : The specs for what information you want to get from the database.
 *          The order slip ITSELF is not passed to functions, but rather the
 *          ENTIRE BARGE. The entire Barge goes into the port and is loaded
 *          with the data it needs. Some info is filled out on the order slip.
 *          (Consider it the receipt section). And then entities are packed
 *          into the Cargo containers.
 * 
 * Barge  : The ship that contains one of each:
 *          1 Agenda clip board.
 *          1 Cargo object. That contains a list of entities we found.
 *          1 Captain that initializes the cargo by looking at the
 *          agenda and ~stearing~ the barge to the correct destinations.
 * 
 * 
 * Captain or LightHouse:
 *     Captain would be instance object that is component of barge.
 *     Lighthouse would be separate object that directs barge from outside the
 *     Barge class.
 *          Steers Barge to correct locations to get needed items.
 *          Basically, our initializer manager.
 * 
 * Port: A specialized function that takes a Barge as an input and populates
 *       the correct area of Cargo and modifies correct Order slip from the
 *       Agenda. Looks through Agenda's Orders and sees which of them were
 *       asking to enter the current port. And then the current port fills
 *       out that Order slip.
 * 
 * DryDoc: Dry docs are used to build ships. This is our ship factory.
 *         Builds a Barge that has been configured to go on a journey for
 *         specific items.
 * 
 * /////////////////////////////////////////////////////////////////////////////
 * 
 * @author jmadison :2015.10.08
 * 
 */
public class AgendaClipBoard {
    
    //Different states that the agenda can be in:
    public static int STATUS_INIT_ERROR            = 0;
    public static int STATUS_CONFIGURING_AGENDA    = 1;
    public static int STATUS_CONFIGURING_COMPLETE  = 2;
    public static int STATUS_EXECUTING_ORDERS      = 3;
    public static int STATUS_ORDERS_COMPLETE       = 4;
    public static int STATUS_FAILURE               = 5;
    
    /** All of the orders that are on the clipboard. 
        Decided to make PRIVATE so that we can check for
        error of adding same order twice. **/
    private List<OrderSlip> _orders;
    
    /**
     * Adds order to collection of order slips.
     * Throws error if order is being added more than once.
     * @param order :The order to add to collection.
     */
    public void addOrder(OrderSlip order){
        if(_orders.indexOf(order) > 0 ){
            String msg = "[Attempting to add duplicate order.]";
            msg += "[AKA: Adding same order twice.]";
            doError(msg);
        }//ERROR?
        
        //If not duplicate, add to collection:
        _orders.add(order);
            
    }//FUNC::END
    
    /** Get the entire object when it SHOULD exist. **/
    public List<OrderSlip> getOrdersRef(){
        
        //It is okay if orders is empty, but not null.
        if(null == _orders){
            doError("[Tried to getOrdersRef, but it was null.]");
        }//FUNC::END
        
        return _orders;
    }//FUNC::END
    
    /** Default status is error to enforce being explicitly set by
     *  object that will be using this Agenda. **/
    public int status = STATUS_INIT_ERROR;
    
    public static AgendaClipBoard make(){
        AgendaClipBoard op = new AgendaClipBoard();
        op._orders = new ArrayList<OrderSlip>();
        op.status = STATUS_INIT_ERROR; //<--don't set yet.
        return op;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = AgendaClipBoard.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
