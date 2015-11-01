package app.transactions.cargoSystem.ports;

import annotations.IndexedFunction;
import java.util.List;
import app.config.constants.identifiers.VarNameReg;
import app.dbDataAbstractions.entities.EntityUtil;
import app.dbDataAbstractions.entities.bases.BaseEntity;
import app.dbDataAbstractions.entities.tables.TokenTable;
import app.dbDataAbstractions.entities.tables.riddleTrialStore.questionStore.DeckTable;
import app.transactions.cargoSystem.dataTypes.GalleonBarge;
import app.transactions.cargoSystem.dataTypes.OrderSlip;
import static app.transactions.cargoSystem.ports.TokenPorts.MAKE_BATCH_OF_TOKENS;
import app.transactions.cargoSystem.ports.config.MasterPortList;
import app.transactions.util.tables.deck.DeckTransUtil;
import app.transactions.util.tables.token.TokenTransUtil;

/**
 *
 * @author jmadison:2015.10.27(Oct27th,Year2015.Tuesday)
 */
public class DeckPorts {
    
    public static final short GENERATE_AND_PERSIST_DECKS = 
               MasterPortList.GENERATE_AND_PERSIST_DECKS; 
    
    @IndexedFunction(key=GENERATE_AND_PERSIST_DECKS)
    public static void generate_and_persist_decks
                                          (GalleonBarge barge, OrderSlip order){
        
        //Core logic:
        //int numDecks = order.specs.getValInt(VarNameReg.NUM_DECKS);
        List<DeckTable> decks;
        decks = DeckTransUtil.generateAndPersistDecks(order.specs);
        
        //Add to our barge (cargo ship)
        List<BaseEntity> bel = EntityUtil.downCastEntities(decks);
        barge.fillOrder(order, bel);
        
    }//FUNC::END
}//CLASS::END
