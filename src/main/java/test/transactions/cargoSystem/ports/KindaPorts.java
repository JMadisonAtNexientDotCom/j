package test.transactions.cargoSystem.ports;

import annotations.IndexedFunction;
import java.util.List;
import test.config.constants.identifiers.VarNameReg;
import test.dbDataAbstractions.entities.EntityUtil;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import test.dbDataAbstractions.entities.tables.KindaTable;
import test.dbDataAbstractions.entities.tables.riddleTrialStore.questionStore.DeckTable;
import test.transactions.cargoSystem.dataTypes.GalleonBarge;
import test.transactions.cargoSystem.dataTypes.OrderSlip;
import test.transactions.cargoSystem.ports.config.MasterPortList;
import test.transactions.util.tables.deck.DeckTransUtil;
import test.transactions.util.tables.kinda.KindaTransUtil;

/**
 *
 * @author jmadison:2015.10.27(Oct27th,Year2015.Tuesday)
 */
public class KindaPorts {
    
    public static final short MAKE_BATCH_OF_KINDA_STUBS = 
               MasterPortList.MAKE_BATCH_OF_KINDA_STUBS; 
    
    @IndexedFunction(key=MAKE_BATCH_OF_KINDA_STUBS)
    public static void make_batch_of_kinda_stubs
                                          (GalleonBarge barge, OrderSlip order){
        
        //Core logic:
        int numKindas = order.specs.getValInt(VarNameReg.NUM_KINDAS);
        List<KindaTable> kindas;
        kindas = KindaTransUtil.makeBatchOfKindaStubs(numKindas);
        
        //Add to our barge (cargo ship)
        List<BaseEntity> bel = EntityUtil.downCastEntities(kindas);
        barge.fillOrder(order, bel);
        
    }//FUNC::END
    
}//CLASS::END
