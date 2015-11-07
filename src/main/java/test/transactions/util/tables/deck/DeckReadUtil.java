package test.transactions.util.tables.deck;

import test.MyError;
import test.dbDataAbstractions.entities.tablePojos.CueCard;
import test.dbDataAbstractions.entities.tablePojos.Deck;
import test.dbDataAbstractions.entities.tables.riddleTrialStore.questionStore.DeckTable;
import test.transactions.util.tables.cuecard.CuecardReadUtil;
import java.util.ArrayList;
import java.util.List;
import utils.RealNumberUtil;

/**
 * Reading == Taking persistent data and converting it into a pojo.
 * 
 * Reading: DeckTable.java (DataBase Table) --> Deck.java (Pojo Version)
 * @author jmadison :2015.10.19
 */
public class DeckReadUtil {
    
    /**
     * Gets a deck of cue-cards by pulling out persisted data from database.
     * If the primary key does NOT exist in the database, that is a fatal error.
     * @param id :The primary key of the deckTable entry.
     * @return :A deck pojo
     */
    public static final Deck readUsingPrimaryKeyID(long id){
        
        //Get the deck record:
        DeckTable dt = DeckTransUtil.getDeckTableByID(id);
        
        //Read the DeckTable into a Deck Pojo:
        return readUsingDeckTable(dt);
        
    }//FUNC::END
    
    /**
     * 
     * @param dt :The DeckTable entity we want to read (inverse of persist)
     * @return : A Deck that can be serialized and shot down to the UI.*/
    public static final Deck readUsingDeckTable(DeckTable dt){
        
        if(null==dt){doError("deck table instance null");}
        if(dt.deck_gi <= 0){doError("invalid deck_gi");}
        
        //Make blank instance of Deck and recursively populate it:
        Deck out_deck = new Deck();
        List<Long> cuecard_ids = DeckTransUtil.getCuecardIDsUsingDeckTable(dt);
                                                
        //For each cuecard_id, we want to make an entry into the deck:
        out_deck.cards = new ArrayList<CueCard>();
        int num_cuecards = cuecard_ids.size();
        long cur_id;
        CueCard cur_card;
        for(int i = 0; i < num_cuecards; i++){
            cur_id = cuecard_ids.get(i);
            RealNumberUtil.assertGreaterThanZeroNonNull(cur_id);
            cur_card = CuecardReadUtil.readUsingPrimaryKeyID(cur_id);
            out_deck.cards.add(i,cur_card);
        }//next i.
        
        //Return the populated deck:
        return out_deck;
        
    }//FUNC::END
    
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = DeckReadUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
