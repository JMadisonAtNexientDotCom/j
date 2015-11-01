package app.transactions.cargoSystem.ports;

import java.util.List;
import app.config.constants.identifiers.VarNameReg;
import app.dbDataAbstractions.entities.EntityUtil;
import app.dbDataAbstractions.entities.bases.BaseEntity;
import app.dbDataAbstractions.entities.tables.TokenTable;
import app.dbDataAbstractions.entities.tables.TrialTable;
import app.transactions.cargoSystem.dataTypes.GalleonBarge;
import app.transactions.cargoSystem.dataTypes.OrderSlip;
import app.transactions.cargoSystem.ports.config.MasterPortList;
import app.transactions.util.tables.token.TokenTransUtil;
import app.transactions.util.tables.trial.TrialTransUtil;

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
        int    numTrials         = order.specs.getValInt (VarNameReg.NUM_TRIALS);
        String kindOfTrial       = order.specs.getValStr(VarNameReg.KIND);
        long   allottedMillisecs = order.specs.getValLong(VarNameReg.ALLOTTED);
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
