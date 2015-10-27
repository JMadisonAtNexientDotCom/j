package test.transactions.util.tables.deck;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import primitives.LongBool;
import test.MyError;
import test.dbDataAbstractions.entities.EntityUtil;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import test.dbDataAbstractions.entities.composites.CueCard;
import test.dbDataAbstractions.entities.composites.Deck;
import test.dbDataAbstractions.entities.tables.riddleTrialStore.questionStore.DeckPurse;
import test.dbDataAbstractions.entities.tables.riddleTrialStore.questionStore.DeckTable;
import test.transactions.util.TransUtil;
import test.transactions.util.tables.cuecard.CuecardPersistUtil;
import test.transactions.util.tables.group.GroupTransUtil;

/**
 * Utility used for persistence conversions:
 * Deck.java (Pojo)---> DeckTable.java (Table)
 * 
 * @author jmadison:2015.10.19
 */
public class DeckPersistUtil {
    
    /**
     * Takes the POJO version of DECK and converts it to
     * the STRUCT/ENTITY/DATA version of DECK.
     * @param pojo :The pojo to convert to struct.
     * @return :Returns the ID of the DeckTable entity persisted with
     *          a boolean to tell us if the entity was newly added to the
     *          database or if it previously existed.
     */
    public static LongBool persist(Deck pojo){
        
        TransUtil.insideTransactionCheck();
        
        LongBool finalResults = new LongBool();
        
        LongBool groupPersistResults;
        groupPersistResults = persistPurse(pojo.cards);
        boolean needToMakeNewEntry = groupPersistResults.b;
        if(needToMakeNewEntry){
            
            //Create new deck table and put the group ID
            //from the previous persist call into the 
            //group index (gi) column:
            DeckTable dt = new DeckTable();
            dt.deck_gi = groupPersistResults.l;
            Session ses = TransUtil.getActiveTransactionSession();
            ses.save(dt); //<--save to give us an id!
            
            //populate final results:
            finalResults.b = true; //<--new data was put into database.
            finalResults.l = dt.getId();
        }else{
            //If the deck already has an exact match in the database,
            //we need to find that entry in the database so we can return
            //the ID of that record:
            Class  table   = DeckTable.class;
            String column  = DeckTable.DECK_GI_COLUMN;
            long   group_id= groupPersistResults.l;
            List<BaseEntity> bel;
            bel = TransUtil.getEntitiesUsingLong(table,column,group_id);
            if(bel.size()!=1){
                doError("[only one entity with that group id should exist!]");
            }//
            
            finalResults.b = false; //<--new data was NOT put into database.
            finalResults.l = bel.get(0).getId();
        }//FUNC::END
        
        return finalResults;
    }//FUNC::END
    
    private static LongBool persistPurse(List<CueCard> cards){
        
        TransUtil.insideTransactionCheck();
        
        //Persist each an every cue card:
        List<LongBool> perCards = new ArrayList<LongBool>();
        int num_cards = cards.size();
        for(int i = 0; i < num_cards; i++){
            CueCard cur_card         = cards.get(i);
            LongBool persisted_card  = CuecardPersistUtil.persist(cur_card);
            perCards.add(persisted_card);
        }//Next i.
        
        //Only set to true if this cluster of CueCards represents a
        //combination of cards that has never existed in the database before.
        boolean newGroupIdNeeded = false;
        
        //CreateOrGet CueCard entities. If any of the entities are NEW,
        //then we know we need a new deck_purse. If any of the entities
        //are in a different order than whatever they match, then we also
        //need a new group ID.
        for(LongBool lb : perCards){
            boolean newEntryMadeInDataBase = lb.b;
            if(newEntryMadeInDataBase){
                newGroupIdNeeded = true;
                break;
            }//
        }//next i.
        
        //NOTE: We are NOT worrying about the order of questions.
        //AKA:
        ////////////////////////////   ////////////////////////////  
        //What color is the sky?  //   //What color is the sky?  //
        //[1]: RED                //===//[1]: BLUE               //
        //[2]: GREEN              //===//[2]: RED                //
        //[3]: BLUE               //   //[3]: GREEN              //
        ////////////////////////////   ////////////////////////////
        
        long groupIDToUse = 0;
        if(false == newGroupIdNeeded){
           List<Long> card_ids = LongBool.stripOutLongs(perCards);
           groupIDToUse = DeckTransUtil.getGroupID(card_ids);
           if(groupIDToUse <= (-1)){ newGroupIdNeeded = true;}
        }//
        
        if(newGroupIdNeeded){
            groupIDToUse = GroupTransUtil.makeNewGroup
                                                ("Testing123", perCards.size());
        }//
        
        LongBool results = new LongBool();
        results.b = newGroupIdNeeded; //was new object persisted?
        results.l = groupIDToUse;
        return results;

    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = DeckPersistUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
