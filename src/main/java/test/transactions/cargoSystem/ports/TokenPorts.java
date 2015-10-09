package test.transactions.cargoSystem.ports;

import annotations.IndexedFunction;
import test.transactions.cargoSystem.dataTypes.GalleonBarge;
import test.transactions.cargoSystem.dataTypes.OrderSlip;
import test.transactions.cargoSystem.ports.config.MasterPortList;

/**
 * A port that has methods that operate only on the token table.
 * But can take Barges that have other information that will be used
 * in completing the Order on the Agenda.
 * 
 * @author jmadison
 */
public class TokenPorts {
    
    public static final int CREATE_NEW_TOKEN = MasterPortList.CREATE_NEW_TOKEN;
    
    @IndexedFunction(key=CREATE_NEW_TOKEN) 
    public static void create_new_token(GalleonBarge barge, OrderSlip order){
        
    }//FUNC::END
    
    
}//CLASS::END
