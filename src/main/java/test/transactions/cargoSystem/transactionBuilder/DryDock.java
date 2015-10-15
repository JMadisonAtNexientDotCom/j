package test.transactions.cargoSystem.transactionBuilder;

import java.util.List;
import test.MyError;
import test.config.constants.identifiers.VarNameReg;
import test.config.constants.signatures.paramVals.TRIAL_KIND_ENUMS;
import test.dbDataAbstractions.entities.tables.NinjaTable;
import test.dbDataAbstractions.entities.tables.OwnerTable;
import test.dbDataAbstractions.entities.tables.TokenTable;
import test.dbDataAbstractions.entities.tables.TrialTable;
import test.dbDataAbstractions.requestAndResponseTypes.postTypes.postRequest.Edict;
import test.transactions.cargoSystem.dataTypes.GalleonBarge;
import test.transactions.cargoSystem.dataTypes.OrderArg;
import test.transactions.cargoSystem.dataTypes.OrderSlip;
import test.transactions.cargoSystem.ports.NinjaPorts;
import test.transactions.cargoSystem.ports.OwnerPorts;
import test.transactions.cargoSystem.ports.TokenPorts;
import test.transactions.cargoSystem.ports.TrialPorts;
import utils.TimeMathUtil;

/**
 * SIMPLE SUMMARY:
 * DryDock builds a cargo-ship (barge) that has been configured to carry out
 * a rather complex transaction involving multiple tables.
 *
 * NOTE: DryDock is NOT designed for simple requests. Use transaction utilities
 *       for simple requests. Use dry dock to build more complex interrelated
 *       transactions.
 * 
 * The master object holding all of the objects that can create builders
 * for the different request types.
 * 
 * Why the word DryDock?
 * A dry dock is where ships are built. It is basically a ShipBuilder.
 * But I wanted a more concise word for that. I want the main players of
 * the architecture to have strong+concise+distinguishable names.
 * 
 * The DESCRIPTIVENESS of the identifiers in the main architecture should
 * not come from having a SuperLongDescriptiveNameThatTellsUsExactlyWhatItDoes
 * but rather by using a unique word who's definition fits into a larger 
 * analogy.
 * 
 * Basically: I am using elaborative encoding techniques 
 * to help the maintainability of the system. A bit experimental. But after
 * memorizing 40 digits of pi in under 1 hour... And being able to keep it in
 * memory for a few months... I think I am onto something.
 * 
 * @author jmadison :2015.10.09
 * @author jmadison :2015.10.12 -got working. Testing more simple methods.
 */
public class DryDock {
    
    /**
    public static GalleonBarge debugger_stub_function(){
        GalleonBarge barge = GalleonBarge.make();
        OrderSlip order;
        order = OrderSlip.makeUsingPortID(TokenPorts.DEBUGGER_STUB_FUNCTION);
        barge.agenda.addOrder(order);
        return barge;
    }//FUNC::END
    **/
    
    /** Before we get too complex. Test the DryDock by re-creating previous
     *  functionality using this new pattern. If it passes, we can work up
     *  to more complex tasks.
     * @return : A barge that is configured to carry out that task
     */
    public static GalleonBarge createNewToken(){
        
        GalleonBarge barge = GalleonBarge.make();
        OrderSlip order;
        order = OrderSlip.makeUsingPortID(TokenPorts.CREATE_NEW_TOKEN);
        order.supplier = TokenTable.class; //<--TODO: make automaticly supplied.
        barge.agenda.addOrder(order);
        return barge;
        
    }//FUNC::END
    
   
    
    /**
     *  Will create trials, for each ninja id specified. And output
     *  the corrosponding token_ids that will be used for access.
     * @param ids_of_ninjas :List of ninjas.
     * @return :Returns a ship ready to take on the task it has been
     *          configured for.
     */
    public static GalleonBarge dispatch_trials(Edict theEdict){
        
        if(null == theEdict){doError("inputted edict is null");}
        List<Long> ids_of_ninjas = theEdict.ninja_id_list;
        
        //Error check inputs:
        if(null == ids_of_ninjas){doError("null ninjas input");}
        if(ids_of_ninjas.isEmpty()){doError("ninja id list is empty");}
        
        //get number of ninjas in list:
        int numNinjas = ids_of_ninjas.size();
        int numTokens = numNinjas; //one token per ninja.
        int numTrials = numNinjas; //one trial per ninja.
        int numOwners = numNinjas; //one owner per ninja.
        
        GalleonBarge barge = GalleonBarge.make();
        
        //Fill out an order for the ACTUAL ninjas.
        //If all ninjas do not exist. We will error out before any of the
        //needed data for the request is made:
        OrderSlip nin_order;
        nin_order = OrderSlip.makeUsingPortID(NinjaPorts.FIND_BATCH_OF_NINJAS);
        nin_order.supplier = NinjaTable.class;
        nin_order.specs.add(VarNameReg.NUM_NINJAS, numNinjas);
        barge.agenda.addOrder(nin_order);
      
        //Fill out an order for tokens.
        //As many tokens as there are ninja ids.
        OrderSlip tok_order;
        tok_order = OrderSlip.makeUsingPortID(TokenPorts.MAKE_BATCH_OF_TOKENS);
        tok_order.supplier = TokenTable.class;
        tok_order.specs.add(VarNameReg.NUM_TOKENS, numTokens);
        barge.agenda.addOrder(tok_order);
        
        //Create Trials, one per ninja:
        //No dependencies!
        OrderSlip tri_order;
        tri_order = OrderSlip.makeUsingPortID(TrialPorts.MAKE_BATCH_OF_TRIAL_STUBS);
        tri_order.supplier = TrialTable.class;
        tri_order.specs.add (VarNameReg.KIND, theEdict.trial_kind);
        tri_order.specs.add (VarNameReg.NUM_TRIALS, numTrials);
        int  minutes   = (int)(theEdict.duration_in_minutes);
        long millisecs = TimeMathUtil.minutesToMS(minutes);
        tri_order.specs.add (VarNameReg.ALLOTTED, millisecs);
        barge.agenda.addOrder(tri_order);
        
        //Make sure each ninja owns one of the tokens, and thus, owns the
        //trial that is associated with that token:
        OrderSlip own_order;
        own_order = OrderSlip.makeUsingPortID(OwnerPorts.MAKE_BATCH_OF_OWNER_STUBS);
        own_order.supplier = OwnerTable.class;
        own_order.specs.add(VarNameReg.NUM_OWNERS, numOwners);
        barge.agenda.addOrder(own_order);
        
        /*
        //Welding jobs will be configured to happen AFTER orders are made.
        //And the weld-jobs will use references to the orders to complete
        //their tasks.
        //LINK STUFF!
        WelderJobTicket tok_own;//insert token_id(s) into owner_table
        WelderJobTicket nin_own; //insert ninja_id(s) into owner_table
        WelderJobTicket tok_tri; //insert token_id(s) into trial_table
        
        //Allocate the Welder Job Tickets:
        tok_own = barge.jobs.makeEmptyWeldJobTicket();
        nin_own = barge.jobs.makeEmptyWeldJobTicket();
        tok_tri = barge.jobs.makeEmptyWeldJobTicket();
        
        //Create joins in owner table:
        //OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
        tok_own.fromOrder = tok_order;//tokens
        tok_own.destOrder = own_order;//owners
        tok_own.destColumn= OwnerTable.TOKEN_ID_COLUMN;
        
        nin_own.fromOrder = nin_order;//ninjas
        nin_own.destOrder = own_order;//owners
        nin_own.destColumn= OwnerTable.NINJA_ID_COLUMN;
        //OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
        
        //Create joins in trial table:
        //TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
        tok_tri.fromOrder = tok_order;
        tok_tri.destOrder = tri_order;
        tok_tri.destColumn= TrialTable.TOKEN_ID_COLUMN;
        //TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
        */
     
        
       
        return barge;
    }//FUNC::END
    
    
    
    /** Testing DryDoc by re-creating previous functionality using the
     *  new cargo-system pattern. Need all the individual pieces to pass
     *  before I can confidentially build up to:
     *  "dispatch_trial_token_and_link_to_one_ninja" function.
     * @param ninja_id :The id of the ninja we want to fetch.
     * @return :A ninja entity with an id == ninja_id.
     */
    public static GalleonBarge getNinjaByID(Long ninja_id){
        GalleonBarge barge = GalleonBarge.make();
        OrderSlip order;
        order = OrderSlip.makeUsingPortID(NinjaPorts.GET_ONE_NINJA_BY_ID);
        order.supplier = NinjaTable.class;
        order.specs.add(VarNameReg.NINJA_ID, ninja_id);
        barge.agenda.addOrder(order);
        return barge;
    }//FUNC::END
    
    /*
    public static GalleonBarge dispatch_trial_token_and_link_to_one_ninja
                                                                 (long ninjaID){
        
        GalleonBarge barge = GalleonBarge.make();
        OrderSlip order;
        
        //Make a new token:
        order = barge.placeOrder(TokenPorts.CREATE_NEW_TOKEN);
        order.supplier        = TokenTable.class;
        order.hasDependencies = false;
        barge.agenda.addOrder(order);
        
        //Get a ninja, using ninja's ID:
        //Note: Operation that returns a SINGLE ENTITY always should
        //      operate using SPECS rather than loading primary keys in list.
        order = barge.placeOrder(NinjaPorts.GET_ONE_NINJA_BY_ID);
        order.supplier        = NinjaTable.class;
        order.specs.add(VarNameReg.NINJA_ID,ninjaID);
        barge.agenda.addOrder(order);
        
        
        
       // order = OrderSlip.makeUsingPortID(SessionPorts.PUT_TOKEN_INTO_SESSION_TABLE);
       // order = OrderSlip.makeUsingPortID(TrialPorts.CREATE_NEW_TRIAL_AND_LINK_TO_TOKEN);
       // order = OrderSlip.makeUsingPortID(NinjaPorts.MAKE_NEW_NINJA);
       // order = OrderSlip.makeUsingPortID(OwnerPorts.GIVE_NINJA_OWNERSHIP_OF_TOKEN);
        //barge.agenda.orders.add(order);
        return barge;
        
    }//FUNC::END
    */
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = DryDock.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END                                                       
                                                                 
}//CLASS::END
