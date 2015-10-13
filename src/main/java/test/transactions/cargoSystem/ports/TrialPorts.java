package test.transactions.cargoSystem.ports;

import java.util.List;
import test.config.constants.identifiers.VarNameReg;
import test.dbDataAbstractions.entities.EntityUtil;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import test.dbDataAbstractions.entities.tables.TokenTable;
import test.dbDataAbstractions.entities.tables.TrialTable;
import test.transactions.cargoSystem.dataTypes.GalleonBarge;
import test.transactions.cargoSystem.dataTypes.OrderSlip;
import test.transactions.cargoSystem.ports.config.MasterPortList;
import test.transactions.util.forOwnedMainlyByOneTable.token.TokenTransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.trial.TrialTransUtil;

/**
 *
 * @author jmadison
 */
public class TrialPorts {
    public static final short MAKE_BATCH_OF_TRIAL_STUBS = 
               MasterPortList.MAKE_BATCH_OF_TRIAL_STUBS;
    
    
    public static void make_batch_of_trial_stubs
                                          (GalleonBarge barge, OrderSlip order){
        
         //Core logic:
        int  numTrials         = order.specs.getValInt(VarNameReg.NUM_TRIALS);
        int  kindOfTrial       = order.specs.getValInt(VarNameReg.KIND);
        long allottedMillisecs = order.specs.getValLong(VarNameReg.ALLOTTED);
        List<TrialTable> trials;
        trials = TrialTransUtil.makeBatchOfTrialStubs
                                    (numTrials, kindOfTrial, allottedMillisecs);
        
        //Add to our barge (cargo ship)
       // EntityCage cage;
        //cage = barge.hold.addCage(tokens, order);
        List<BaseEntity> bel = EntityUtil.downCastEntities(trials);
        barge.fillOrder(order, bel);
        
    }//FUNC::END
}//CLASS::END
