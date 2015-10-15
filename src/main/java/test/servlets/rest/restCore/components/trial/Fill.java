package test.servlets.rest.restCore.components.trial;

import annotations.PairedStaticFunction;
import annotations.Verbatim;
import java.util.ArrayList;
import java.util.List;
import test.MyError;
import test.config.constants.identifiers.FuncNameReg;
import test.dbDataAbstractions.entities.tables.NinjaTable;
import test.dbDataAbstractions.entities.tables.TokenTable;
import test.dbDataAbstractions.requestAndResponseTypes.postTypes.postRequest.Edict;
import test.dbDataAbstractions.requestAndResponseTypes.postTypes.postResponse.Coffer;
import test.dbDataAbstractions.requestAndResponseTypes.postTypes.postResponse.Ticket;
import test.transactions.cargoSystem.dataTypes.EntityCage;
import test.transactions.cargoSystem.dataTypes.GalleonBarge;
import test.transactions.cargoSystem.transactionBuilder.DryDock;

/**-----------------------------------------------------------------------------
 * Process:
 * BOOK -- Convert request into an object. "BOOK" the request.
 * CHOP -- Chop booked request into valid and invalid requests. Two halves.
 * FILL -- Fills the INVALID booked order, and the VALID booked order.
 * JOIN -- Joins the invalid+valid back together into one output object.
 *
 * @author jmadison :2015.10.13
 ----------------------------------------------------------------------------**/
public class Fill {
    
    /**
     * Takes the two edicts, and completes their orders, returning two coffers.
     * If a slot is NULL, process is ignored. Only non-error slot (1st slot)
     * is allowed to be null.
     * @param eds:The edicts to fetch info for.
     * @return 
     */
    @PairedStaticFunction //All classes scanned should have func with this name
    @Verbatim( name=FuncNameReg.DISPATCH_TOKENS ) //refactor easier.
    public static Coffer[] dispatch_tokens(Edict[] eds){
        
        if(null == eds[0] && null == eds[1]){doError("both cannot be null");}
        Coffer[] op = new Coffer[2];
        op[0] = dispatch_tokens_process_valid( eds[0] ); //null input allowed.
        op[1] = dispatch_tokens_process_bogus( eds[1] ); //null input allowed.
        return op;
        
    }//FUNC::END
    
    /** Process VALID request. **/
    private static Coffer dispatch_tokens_process_valid( Edict ed){
        
        if(ed==null){return null;}

        //Delete this block of comments later:
        //TODO: Actual logic:
        //Coffer op = Coffer.makeStubCofferUsingEdict(ed);
        //return op;
        
        //Create a cargo-ship configured to carry out the order,
        //Then instruct ship to carry out order:
        GalleonBarge barge = DryDock.dispatch_trials(ed);
        barge.embark();
        EntityCage nin_cage = barge.hold.getCageUsingSupplier(NinjaTable.class);
        EntityCage tok_cage = barge.hold.getCageUsingSupplier(TokenTable.class);
        List<NinjaTable> ninjas = nin_cage.getMerchandise();
        List<TokenTable> tokens = tok_cage.getMerchandise();
        if(ninjas.size() != tokens.size()){
            doError("Orders should have been of equal sizes!");
        }//Error!
        
        //Extract contents of order and pack into a coffer:
        int len = ninjas.size();
        Coffer op = new Coffer();
        op.comment = "Touched by Fill.dispatch_tokens_process_valid";
        op.tickets = new ArrayList<Ticket>(len);
        for(int i = 0; i < len; i++){
            NinjaTable nin = ninjas.get(i);
            TokenTable tok = tokens.get(i);
            
            //populate ticket:
            Ticket tik     = new Ticket();
            tik.ninja_id   = nin.getId();
            tik.ninja_name = nin.getName();
            tik.token_hash = tok.getToken_hash();
            
            //validate and set ticket:
            Ticket.validateNaive(tik);
            op.tickets.set(i, tik);
        }//next i
        
        //return output coffer:
        return op;
        
    }//FUNC::END
    
    /** Process BOGUS request. **/
    private static Coffer dispatch_tokens_process_bogus( Edict ed){
        
        if(ed==null){return null;}

        String msg = "";
        msg += "[dis...process_bogus]";
        msg += ed.comment;
        Coffer op = Coffer.makeErrorCoffer(msg);
        op.errorCode = ed.errorCode;
        return op;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = Fill.class;
        err += clazz.getCanonicalName();//NEED CANONICAL NAME FOR THIS!
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
