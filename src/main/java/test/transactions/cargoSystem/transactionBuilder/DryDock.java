package test.transactions.cargoSystem.transactionBuilder;

import test.transactions.cargoSystem.dataTypes.GalleonBarge;
import test.transactions.cargoSystem.dataTypes.OrderSlip;
import test.transactions.cargoSystem.ports.TokenPorts;

/**
 * NOTE: DryDock is NOT designed for simple requests. Use transaction utilities
 *       for simple requests. Use dry dock to build more complex interrelated
 *       transactions.
 * 
 * The master object holding all of the objects that can create builders
 * for the different request types.
 * 
 * @author jmadison
 */
public class DryDock {
    
    /** Before we get too complex. Test the DryDock by re-creating previous
     *  functionality using this new pattern. If it passes, we can work up
     *  to more complex tasks.
     * @return : A barge that is configured to carry out that task
     */
    public static GalleonBarge createNewToken(){
        
        GalleonBarge barge = new GalleonBarge();
        OrderSlip order;
        order = OrderSlip.makeUsingPortID(TokenPorts.CREATE_NEW_TOKEN);
        barge.agenda.addOrder(order);
        return barge;
        
    }//FUNC::END
    
    public static GalleonBarge dispatch_trial_token_and_link_to_ninja(){
        
        GalleonBarge barge = new GalleonBarge();
        OrderSlip order;
        
        order = barge.placeOrder(TokenPorts.CREATE_NEW_TOKEN);
        order.hasDependencies = false;
        order.hasSpecs        = false;
        barge.agenda.addOrder(order);
        //order.supplier //<--do we need supplier class to make transaction?
        
       // order = OrderSlip.makeUsingPortID(SessionPorts.PUT_TOKEN_INTO_SESSION_TABLE);
       // order = OrderSlip.makeUsingPortID(TrialPorts.CREATE_NEW_TRIAL_AND_LINK_TO_TOKEN);
       // order = OrderSlip.makeUsingPortID(NinjaPorts.MAKE_NEW_NINJA);
       // order = OrderSlip.makeUsingPortID(OwnerPorts.GIVE_NINJA_OWNERSHIP_OF_TOKEN);
        //barge.agenda.orders.add(order);
        return barge;
        
    }//FUNC::END
    
}//CLASS::END
