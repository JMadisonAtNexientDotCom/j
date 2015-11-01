package app.transactions.cargoSystem.ports;

import annotations.IndexedFunction;
import java.util.List;
import org.hibernate.Session;
import app.config.constants.identifiers.VarNameReg;
import app.config.debug.DebugConfig;
import app.config.debug.DebugConfigLogger;
import app.dbDataAbstractions.entities.EntityUtil;
import app.dbDataAbstractions.entities.bases.BaseEntity;
import app.dbDataAbstractions.entities.tables.TokenTable;
import app.transactions.cargoSystem.dataTypes.EntityCage;
import app.transactions.cargoSystem.dataTypes.GalleonBarge;
import app.transactions.cargoSystem.dataTypes.OrderSlip;
import app.transactions.cargoSystem.ports.config.MasterPortList;
import app.transactions.util.TransUtil;
import app.transactions.util.tables.token.TokenTransUtil;

/**
 * A port that has methods that operate only on the token table.
 * But can take Barges that have other information that will be used
 * in completing the Order on the Agenda.
 * 
 * @author jmadison
 */
//@Supplier(clazz=TokenTable.clazz);
public class TokenPorts {
    
    public static final short CREATE_NEW_TOKEN       = 
               MasterPortList.CREATE_NEW_TOKEN;
    
    public static final short MAKE_BATCH_OF_TOKENS   = 
               MasterPortList.MAKE_BATCH_OF_TOKENS;
    
    @IndexedFunction(key=MAKE_BATCH_OF_TOKENS)
    public static void make_batch_of_tokens
                                          (GalleonBarge barge, OrderSlip order){
        
        //Core logic:
        int numTokens = order.specs.getValInt(VarNameReg.NUM_TOKENS);
        List<TokenTable> tokens;
        tokens = TokenTransUtil.makeBatchOfTokens(numTokens);
        
        //Add to our barge (cargo ship)
       // EntityCage cage;
        //cage = barge.hold.addCage(tokens, order);
        List<BaseEntity> bel = EntityUtil.downCastEntities(tokens);
        barge.fillOrder(order, bel);
        
    }//FUNC::END
    
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
