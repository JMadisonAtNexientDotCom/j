package app.transactions.cargoSystem.ports;

import java.util.List;
import app.MyError;
import app.config.constants.identifiers.VarNameReg;
import app.dbDataAbstractions.entities.EntityUtil;
import app.dbDataAbstractions.entities.bases.BaseEntity;
import app.dbDataAbstractions.entities.tables.NinjaTable;
import app.dbDataAbstractions.entities.tables.OwnerTable;
import app.dbDataAbstractions.entities.tables.TokenTable;
import app.dbDataAbstractions.entities.tables.TrialTable;
import app.transactions.cargoSystem.dataTypes.GalleonBarge;
import app.transactions.cargoSystem.dataTypes.OrderSlip;
import app.transactions.cargoSystem.ports.config.MasterPortList;
import app.transactions.util.tables.owner.OwnerTransUtil;
import app.transactions.util.tables.trial.TrialTransUtil;

/**
 *
 * @author jmadison
 */
public class OwnerPorts {
    
    public static final short MAKE_BATCH_OF_OWNER_STUBS = 
               MasterPortList.MAKE_BATCH_OF_OWNER_STUBS;
    
    
    /**
     * Creates a batch of fresh owner records. That have not been
     * filled out. The only thing filled out being their auto-generated
     * primary keys.
     * @param barge :The cargo-ship to load up the entities onto.
     * @param order :The order slip that asked for this function to be called.
     *               We want to fill out the order slip here as a sort of 
     *               receipt, and also use it to confirm we are in the correct
     *               function.
     */
    public static void make_batch_of_owner_stubs
                                          (GalleonBarge barge, OrderSlip order){
        
         //Core logic:
        int  numOwners         = order.specs.getValInt(VarNameReg.NUM_OWNERS);
        List<OwnerTable> owners;
        owners = OwnerTransUtil.makeBatchOfOwnerStubs(numOwners);
        
        //Add to our barge (cargo ship)
       // EntityCage cage;
        //cage = barge.hold.addCage(tokens, order);
        List<BaseEntity> bel = EntityUtil.downCastEntities(owners);
        barge.fillOrder(order, bel);
        
    }//FUNC::END
    
    
   /* xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
   //
    * TODO: Should use welder-job at end of trip to do linkage between tables.
    * @param barge :The barge we are loading up
    * @param order :OrderSlip expected to have dependencies of a 
    *               TOKEN_ORDER and NINJA_ORDER.
   //
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
    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx*/
                                          
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
