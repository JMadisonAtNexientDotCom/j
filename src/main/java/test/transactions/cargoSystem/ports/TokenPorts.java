package test.transactions.cargoSystem.ports;

import annotations.IndexedFunction;
import org.hibernate.Session;
import test.dbDataAbstractions.entities.tables.TokenTable;
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
    
    public static final short CREATE_NEW_TOKEN = MasterPortList.CREATE_NEW_TOKEN;
    
    @IndexedFunction(key=CREATE_NEW_TOKEN) 
    public static void create_new_token(GalleonBarge barge, OrderSlip order){
        
        //Before advice needed: Check to make sure OrderSlip matches
        //the function being called.
        
        //Core logic:
        Session ses = TransUtil.getActiveTransactionSession();
        TokenTable tt = TokenTransUtil.makeNextToken();
        ses.save(tt); //<--save to force making of primary key.
        
        //Barge abstraction: (Load data onto the barge)
        //Register what you've gotten with the Hold and Order:
        boolean saveOnExit = true;
        barge.hold.addCageWithOneEntity_AndAssertUnique(tt, order, saveOnExit);
        order.primaryKey_ids.add(tt.getId());
        
    }//FUNC::END
    
    
}//CLASS::END