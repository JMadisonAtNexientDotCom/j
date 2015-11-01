package app.transactions.cargoSystem.transactionBuilder;

import java.util.List;
import app.MyError;
import app.config.constants.identifiers.VarNameReg;
import app.config.constants.signatures.paramVals.TRIAL_KIND_ENUMS;
import app.dbDataAbstractions.entities.tables.KindaTable;
import app.dbDataAbstractions.entities.tables.NinjaTable;
import app.dbDataAbstractions.entities.tables.OwnerTable;
import app.dbDataAbstractions.entities.tables.TokenTable;
import app.dbDataAbstractions.entities.tables.TrialTable;
import app.dbDataAbstractions.entities.tables.riddleTrialStore.questionStore.DeckTable;
import app.dbDataAbstractions.requestAndResponseTypes.postTypes.postRequest.Edict;
import app.transactions.cargoSystem.dataTypes.GalleonBarge;
import app.transactions.cargoSystem.dataTypes.JobTicket;
import app.transactions.cargoSystem.dataTypes.JobTicketTypes;
import app.transactions.cargoSystem.dataTypes.OrderArg;
import app.transactions.cargoSystem.dataTypes.OrderSlip;
import app.transactions.cargoSystem.dataTypes.jobConsts.JoinOrderVars;
import app.transactions.cargoSystem.dataTypes.jobConsts.LinkOrderVars;
import app.transactions.cargoSystem.dataTypes.jobConsts.WeldJobVars;
import app.transactions.cargoSystem.ports.DeckPorts;
import app.transactions.cargoSystem.ports.KindaPorts;
import app.transactions.cargoSystem.ports.NinjaPorts;
import app.transactions.cargoSystem.ports.OwnerPorts;
import app.transactions.cargoSystem.ports.TokenPorts;
import app.transactions.cargoSystem.ports.TrialPorts;
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
        int numKindas = numNinjas; //one kinda per ninja.
        int numDecks  = numNinjas; //one deck (guts of test) per ninja.
        
        GalleonBarge barge = GalleonBarge.make();
        
        //Fill out an order for the ACTUAL ninjas.
        //If all ninjas do not exist. We will error out before any of the
        //needed data for the request is made:
        OrderSlip nin_order;
        nin_order = OrderSlip.makeUsingTable(NinjaTable.class, ids_of_ninjas);
        barge.agenda.addOrder(nin_order);
      
        //Fill out an order for tokens.
        //As many tokens as there are ninja ids.
        OrderSlip tok_order;
        tok_order = OrderSlip.makeUsingPortID(TokenPorts.MAKE_BATCH_OF_TOKENS);
        tok_order.supplier = TokenTable.class;
        tok_order.specs.add(VarNameReg.NUM_TOKENS, numTokens);
        barge.agenda.addOrder(tok_order);
        
        //Order for "kindas" the records that link tokens+tests+responses+grades
        //all-together in a nice package.
        OrderSlip knd_order;
        knd_order = OrderSlip.makeUsingPortID(KindaPorts.MAKE_BATCH_OF_KINDA_STUBS);
        knd_order.supplier = KindaTable.class;
        knd_order.specs.add(VarNameReg.NUM_KINDAS, numKindas);
        barge.agenda.addOrder(knd_order);
        
        //Order for decks. Decks are filled with cuecards. They are the
        //core of a riddle-trial.
        //Eventually we might want the test creation settings to NOT be hard
        //coded. Either have some standard test settings, or put the
        //config options in the edict. Not sure.
        OrderSlip dek_order;
        dek_order = OrderSlip.makeUsingPortID(DeckPorts.GENERATE_AND_PERSIST_DECKS);
        dek_order.supplier = DeckTable.class;
        dek_order.specs.add(VarNameReg.NUM_DECKS, numDecks);
        dek_order.specs.add(VarNameReg.CARD_COUNT,6); //<--TODO: from edict.
        dek_order.specs.add(VarNameReg.NUM_QUIPS ,4); //<--TODO: from edict.
        dek_order.specs.add(VarNameReg.TRU_MIN   ,0); //<--TODO: from edict.
        dek_order.specs.add(VarNameReg.TRU_MAX   ,4); //<--TODO: from edict.
        barge.agenda.addOrder(dek_order);
        
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
        
        
        //Welding jobs will be configured to happen AFTER orders are made.
        //And the weld-jobs will use references to the orders to complete
        //their tasks.
        //LINK STUFF!
        JobTicket tok_own; //insert token_id(s) into owner_table
        JobTicket nin_own; //insert ninja_id(s) into owner_table
        JobTicket tok_tri; //insert token_id(s) into trial_table
        JobTicket tok_knd; //insert token_id(s) into kinda_table
        JobTicket dek_knd; //insert deck_id (s) into kinda_table
        JobTicket tri_knd; //non-id join from trial_table-->kinda_table
        
        //Allocate the Welder Job Tickets:
        //naming: Structure: 
        //EX: fromTable_toTable = barge.bulletin.addEmptyJobTicket();
        tok_own = barge.bulletin.addEmptyJobTicket(); //token_id into owner.
        nin_own = barge.bulletin.addEmptyJobTicket(); //ninja_id into owner.
        tok_tri = barge.bulletin.addEmptyJobTicket(); //token_id into trial.
        tok_knd = barge.bulletin.addEmptyJobTicket(); //token_id into kinda.
        dek_knd = barge.bulletin.addEmptyJobTicket(); //deck_id  into kinda.
        tri_knd = barge.bulletin.addEmptyJobTicket(); //ANY-COL  into kinda.
        
        //Create joins in owner table:
        //OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
        tok_own.jobType = JobTicketTypes.JOIN_ORDER;
        tok_own.specs.add(JoinOrderVars .FROM_ORDER , tok_order);
        tok_own.specs.add(JoinOrderVars .INTO_ORDER , own_order);
        tok_own.specs.add(JoinOrderVars .DEST_COLUMN, OwnerTable.TOKEN_ID_COLUMN);

        nin_own.jobType = JobTicketTypes.JOIN_ORDER;
        nin_own.specs.add(JoinOrderVars .FROM_ORDER , nin_order);//ninjas
        nin_own.specs.add(JoinOrderVars .INTO_ORDER , own_order);//owners
        nin_own.specs.add(JoinOrderVars .DEST_COLUMN, OwnerTable.NINJA_ID_COLUMN);
        //OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
        
        //Create joins in trial table:
        //TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
        tok_tri.jobType   = JobTicketTypes.JOIN_ORDER;
        tok_tri.specs.add(JoinOrderVars.FROM_ORDER, tok_order);
        tok_tri.specs.add(JoinOrderVars.INTO_ORDER, tri_order);
        tok_tri.specs.add(JoinOrderVars.DEST_COLUMN,TrialTable.TOKEN_ID_COLUMN);
        //TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
        
        //Create joins in the kinda table:
        //KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK
        //token_id --> kinda_table
        tok_knd.jobType = JobTicketTypes.JOIN_ORDER;
        tok_knd.specs.add(JoinOrderVars.FROM_ORDER, tok_order);
        tok_knd.specs.add(JoinOrderVars.INTO_ORDER, knd_order);
        tok_knd.specs.add(JoinOrderVars.DEST_COLUMN,KindaTable.TOKEN_ID_COLUMN);
        //deck_id --> kinda_table
        //NOTE: deck_id is known as challenge_id when in the kinda table.
        //      This foreign key breaks convention because it's value may
        //      refer to different tables depending on what kind of test
        //      is stored in the record.
        String deckDestColumn = KindaTable.CHALLENGE_ID_COLUMN;
        dek_knd.jobType = JobTicketTypes.JOIN_ORDER;
        dek_knd.specs.add(JoinOrderVars.FROM_ORDER, dek_order);
        dek_knd.specs.add(JoinOrderVars.INTO_ORDER, knd_order);
        dek_knd.specs.add(JoinOrderVars.DEST_COLUMN,deckDestColumn);
        //
        //Make sure KindaTable has the same "kind" enum as the trial it is
        //attached to:
        String tri_take_column = TrialTable.KIND_COLUMN;
        String knd_dest_column = KindaTable.KIND_COLUMN;
        tri_knd.jobType = JobTicketTypes.LINK_ORDER;
        tri_knd.specs.add(LinkOrderVars.FROM_ORDER, dek_order);
        tri_knd.specs.add(LinkOrderVars.INTO_ORDER, knd_order);
        tri_knd.specs.add(LinkOrderVars.TAKE_COLUMN,tri_take_column);
        tri_knd.specs.add(LinkOrderVars.DEST_COLUMN,knd_dest_column);
        //KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK
        
       
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
