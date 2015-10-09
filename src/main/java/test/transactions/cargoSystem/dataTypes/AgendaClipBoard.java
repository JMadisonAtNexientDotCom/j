package test.transactions.cargoSystem.dataTypes;

import java.util.ArrayList;
import java.util.List;

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
    
    /** All of the orders that are on the clipboard. **/
    public List<OrderSlip> orders = new ArrayList<OrderSlip>();
    
    /** Default status is error to enforce being explicitly set by
     *  object that will be using this Agenda. **/
    public int status = STATUS_INIT_ERROR;
    
}//CLASS::END
