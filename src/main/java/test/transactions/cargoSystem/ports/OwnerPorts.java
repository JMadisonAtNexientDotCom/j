package test.transactions.cargoSystem.ports;

import java.util.List;
import test.MyError;
import test.dbDataAbstractions.entities.tables.NinjaTable;
import test.dbDataAbstractions.entities.tables.TokenTable;
import test.transactions.cargoSystem.dataTypes.GalleonBarge;
import test.transactions.cargoSystem.dataTypes.OrderSlip;
import test.transactions.cargoSystem.ports.config.MasterPortList;
import test.transactions.util.forOwnedMainlyByOneTable.owner.OwnerTransUtil;

/**
 *
 * @author jmadison
 */
public class OwnerPorts {
    
   /**
    * 
    * @param barge :The barge we are loading up
    * @param order :OrderSlip expected to have dependencies of a 
    *               TOKEN_ORDER and NINJA_ORDER.
    */
   public static void give_ninja_ownership_of_token
                                          (GalleonBarge barge, OrderSlip order){
       
        //Get the orders that this order depends on!
        OrderSlip token_order = OrderSlip.getEnabler(order, TokenTable.class);
        OrderSlip ninja_order = OrderSlip.getEnabler(order, NinjaTable.class);
        
        //now use owner table to put this token into 
        //the table under the ninja's id:
        List<Long> token_ids = token_order.primaryKey_ids;
        List<Long> ninja_ids = ninja_order.primaryKey_ids;
        
        //Error checks:
        if(token_ids.size() <= 0){ doError("[no token ids!]");}
        if(ninja_ids.size() <= 0){ doError("[no ninja ids!]");}
        if(token_ids.size() != ninja_ids.size()){doError("[unevenly paired!]");}
        
        //Go through the owner table, and link each ninja_id to token_id:
        int len = ninja_ids.size();
        for(int id = 0; id < len; id++){
            long t_id = token_ids.get(id);
            long n_id = ninja_ids.get(id);
            OwnerTransUtil.makeEntryUsing_ninja(t_id, n_id);
        }//Next id.
        
   }//FUNC::END
                                          
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = OwnerPorts.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
