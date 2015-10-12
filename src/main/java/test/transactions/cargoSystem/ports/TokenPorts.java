package test.transactions.cargoSystem.ports;

import annotations.IndexedFunction;
import org.hibernate.Session;
import test.config.debug.DebugConfig;
import test.config.debug.DebugConfigLogger;
import test.dbDataAbstractions.entities.tables.TokenTable;
import test.transactions.cargoSystem.dataTypes.EntityCage;
import test.transactions.cargoSystem.dataTypes.GalleonBarge;
import test.transactions.cargoSystem.dataTypes.OrderSlip;
import test.transactions.cargoSystem.ports.config.MasterPortList;
import test.transactions.util.TransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.token.TokenTransUtil;

/**
 * A port that has methods that operate only on the token table.
 * But can take Barges that have other information that will be used
 * in completing the Order on the Agenda.
 * 
 * @author jmadison
 */
//@Supplier(clazz=TokenTable.clazz);
public class TokenPorts {
    
    public static final short CREATE_NEW_TOKEN       = MasterPortList.CREATE_NEW_TOKEN;
    //public static final short DEBUGGER_STUB_FUNCTION = MasterPortList.DEBUGGER_STUB_FUNCTION;
    
    @IndexedFunction(key=CREATE_NEW_TOKEN) 
    public static void create_new_token(GalleonBarge barge, OrderSlip order){
        
        //Before advice needed: Check to make sure OrderSlip matches
        //the function being called.
        
        //Core logic:
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();
        TokenTable tt = TokenTransUtil.makeNextToken();
        ses.save(tt); //<--save to force making of primary key.
        
        //Barge abstraction: (Load data onto the barge)
        //Register what you've gotten with the Hold and Order:
        EntityCage cage;
        cage = barge.hold.addCageWithOneEntity_AndAssertUnique(tt, order);
        cage.requiresSaving = true; //save this entity on exit.
        order.primaryKey_ids.add(tt.getId());
        
    }//FUNC::END
    
    //
    // * Like create_new_token, but makes multiple tokens.
    // * Uses arguments to do so.
    // * @param barge
    // * @param order 
    //
    //public static void create_token_batch(GalleonBarge barge, OrderSlip order){
    //    
    //}//FUNC::END
    
    
    /**
    @IndexedFunction(key=DEBUGGER_STUB_FUNCTION)
    public static void debugger_stub_function
                                          (GalleonBarge barge, OrderSlip order){
        if(DebugConfig.isDebugBuild){
            
            DebugConfigLogger.add("static!", "[debugger stub function()]");
        }//
        //do nothing. Just a stub to see if we can invoke without crash.
    }//FUNC::END
    **/
    
}//CLASS::END
