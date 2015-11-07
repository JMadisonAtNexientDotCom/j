/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.transactions.util.tables.deck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import primitives.LongBool;
import test.MyError;
import test.config.constants.identifiers.VarNameReg;
import test.config.debug.DebugConfig;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import test.dbDataAbstractions.entities.bases.PurseEntity;
import test.dbDataAbstractions.entities.tablePojos.CueCard;
import test.dbDataAbstractions.entities.tablePojos.Deck;
import test.dbDataAbstractions.entities.tables.RiddleTable;
import test.dbDataAbstractions.entities.tables.riddleTrialStore.questionStore.DeckPurse;
import test.dbDataAbstractions.entities.tables.riddleTrialStore.questionStore.DeckTable;
import test.transactions.cargoSystem.dataTypes.SpecialInstructionsStickyNote;
import test.transactions.util.TransUtil;
import test.transactions.util.tables.cuecard.CuecardTransUtil;
import test.transactions.util.tables.group.GroupTransUtil;
import utils.ListUtil;
import utils.RandomSetUtil;
import utils.RealNumberUtil;

/**
 * A utility used for transactions involving the DeckTable entity.
 * @author jmadison :2015.10.19
 */
public class DeckTransUtil {
    
    /**
     * Uses a populated DeckTable that was fetched from database in order
     * to find the list of Cuecards it holds.
     * @param dt :The DeckTable reference.
     * @return 
     */
    public static List<Long> getCuecardIDsUsingDeckTable(DeckTable dt){
        TransUtil.insideTransactionCheck();
        RealNumberUtil.assertGreaterThanZeroNonNull(dt.deck_gi);
        RealNumberUtil.assertGreaterThanZeroNonNull(dt.getId());
        
        long deck_gi = dt.deck_gi;
        return getCuecardIDsUsingGroupID(deck_gi);
    }//FUNC::END
    
    /**
     * 
     * @param group_id :The group_id that all retrieved records will share.
     * @return :A list of Cuecard ids.
     */
    public static List<Long> getCuecardIDsUsingGroupID(long group_id){
        TransUtil.insideTransactionCheck();
        List<DeckPurse> dp = getDeckPursesUsingGroupID(group_id);
        
        int len = dp.size();
        long cuecard_id;
        DeckPurse cur_purse;
        List<Long> op = new ArrayList<Long>();
        for(int i = 0; i < len; i++){
            cur_purse = dp.get(i);
            cuecard_id = cur_purse.cuecard_id;
            op.add(i,cuecard_id);
        }//next i.
        
        if(null==op){doError("Attempt to return null");}
        return op;
        
    }//FUNC::END
    
    /**
     * Gets all of the DeckPurse [Entity/Record](s) within DeckPurse using
     * the group_id as a handle to fetch them.
     * @param group_id :Handle used to fetch all DeckPurse(s) belonging to
     *                  that group.
     * @return :All DeckPurse records of that group_id.                      **/
    public static List<DeckPurse> getDeckPursesUsingGroupID(long group_id){
        TransUtil.insideTransactionCheck();
        
        List<DeckPurse> dp = TransUtil.getPursesUsingGroupID
                                                    (DeckPurse.class, group_id);
        if(null==dp){doError("Attempt to return null list of DeckPurse");}
        return dp;
    }//FUNC::END
    
    /**
     * Get a DeckTable [Record/Entity] using [primary key / id]
     * @param id :The [id/primary key] used to identify record.
     * @return :The populated [entity/record] with that id. **/
    public static DeckTable getDeckTableByID(long id){
        TransUtil.insideTransactionCheck();
        
        List<BaseEntity> bel = TransUtil.getEntitiesUsingLong
                                     (DeckTable.class, DeckTable.ID_COLUMN, id);
        if(bel.size() != 1){
            doError("[Should get exactly 1 entity from this call.]");
        }//error?
        
        DeckTable op = (DeckTable)bel.get(0);
        if(null==op){doError("trying to return null");}
        return op;
    }//FUNC::END
    
    /**
     * Makes a batch of DeckTable entities that correspond to persisted
     * Deck(Pojo) objects that have been generated.
     * @param specs :arguments object for the request.
     * @return 
     */
    public static List<DeckTable> generateAndPersistDecks
                                          (SpecialInstructionsStickyNote specs){
        TransUtil.insideTransactionCheck();
                                              
        //Unpack the arguments that should be there:
        Class INT = int.class;
        int num_decks = specs.getVal(VarNameReg.NUM_DECKS , INT);
        int card_count= specs.getVal(VarNameReg.CARD_COUNT, INT);
        int num_quips = specs.getVal(VarNameReg.NUM_QUIPS , INT);
        int tru_min   = specs.getVal(VarNameReg.TRU_MIN   , INT);
        int tru_max   = specs.getVal(VarNameReg.TRU_MAX   , INT);
        
        //Generate deck pojos:
        Deck curDeckPojo;
        List<Deck> pojos = new ArrayList<Deck>();
        for(int i = 0; i < num_decks; i++){
            curDeckPojo = generateDeck(card_count, num_quips, tru_min, tru_max);
            pojos.add(i, curDeckPojo); //<--preserve order!
        }//next i.
        
        //Persist decks into database:
        LongBool persistResults;
        DeckTable curDeckEntity;
        List<DeckTable> structs = new ArrayList<DeckTable>();
        for(int p = 0; p < num_decks; p++){
            curDeckPojo = pojos.get(p);
            persistResults = DeckPersistUtil.persist(curDeckPojo);
            curDeckEntity = new DeckTable();
            curDeckEntity.setId(persistResults.l);
            
            //We don't have this data. So use invalid value so that error is
            //thrown if we try to access and make use of it.
            curDeckEntity.deck_gi = new Long(-92313);
            
            //Put the deck entity into collection:
            structs.add(p, curDeckEntity);//<--preserve order!
        }//next p
        
        return structs;
        
    }//FUNC::END
   
    /**
     * Generates a deck of cuecards. Does NOT persist it.
     * A "Deck" is the Object form of our "DeckTable" Struct.
     * 
     * @param totalCards :Total number of cards in this deck.
     *                    If there is NOT enough data in the database to make
     *                    this many unique cue cards, will populate the deck
     *                    with less than you asked for.
     *                    Uniqueness in this case is determined by the
     *                    [JEST/QUESTION] titling a given cue-card, not the
     *                    overall composition of the cue-card.
     *                    E.G.: We don't want two cards asking the same question
     *                    that only vary in the choices of answers that can
     *                    be given.
     *               
     * @param numQuips   :Number of quips (choices) on each card.
     * @param minTrue    :Target MIN number of correct [quips/answers] that
     *                    can be on a single cue-card.
     * @param maxTrue    :Target MAX number of correct [quips/answers] that
     *                    can be on a single cue-card.
     * @return :Return the generated deck.
     * 
     */
    public static Deck generateDeck(int totalCards, int numQuips, int minTrue, int maxTrue){
        
        TransUtil.insideTransactionCheck();
        
        long highest_record_id = TransUtil.getHighestKeyInTable
        (RiddleTable.class, RiddleTable.ID_COLUMN);
        
        //This is assuming that there are no holes in our database.
        //If there are gaps in our primary keys, this WILL FAIL TO BE RELIABLE.
        long amt_available = highest_record_id;
        
        //We might have to return less cards in the deck than
        //we wanted if there is not enough data available:
        int actual_total_cards = totalCards;
        if(amt_available < totalCards){
            actual_total_cards = (int)amt_available;
        }
        
        //Create a set of randomized ids, in which all ids in set are unique:
        List<Long> idSet = RandomSetUtil.createRandomSetOfUniqueValues
                                     (actual_total_cards, 1, highest_record_id);
        
        List<CueCard> deckGuts = new ArrayList<CueCard>();
        
        CueCard card;
        long riddleID;
        int numberOfTruths;
        for(int i = 0; i < actual_total_cards; i++){
            riddleID = idSet.get(i);
            numberOfTruths = RandomSetUtil.getRandomIntRange(minTrue, maxTrue);
            card = CuecardTransUtil.makeFilledOutCueCard
                                    (riddleID, numQuips, numberOfTruths);
            deckGuts.add(card);
        }//next i.
        
        if(deckGuts.size() != actual_total_cards){
            doError("Deck was not made to clamped target size");
        }//error?
        
        //create our output deck:
        Deck op = new Deck();
        op.cards = deckGuts;
        
        //return the deck:
        return op;
        
    }//FUNC::END
    
    /**
     * 
     * TODO: We really need to figure out how to make this method generic.
     *       Because it is complex, and I don't want to have to write this
     *       stuff again.
     * 
     * Looks at the group of cuecard_ids and returns a valid
     * group_id if that list of cuecard_ids exist.
     * Right now:
     * 1. Order of cuecards does NOT matter for matching.
     * 2. Amount does. A list of 3 cannot match a list of 5.
     * @param cuecard_ids:The cuecard ids to find a group matching it.
     * @return :Returns >=1 if found, returns (-1) if not found.
     */
    public static long getGroupID(List<Long> cuecard_ids){
        TransUtil.insideTransactionCheck();
        
        //This check is too heavy to justify 
        //running ouside of debug mode:
        if(DebugConfig.isDebugBuild){
            ListUtil.assertAllEntriesUnique(cuecard_ids);
        }//Debug
        
        //Create a criteria search that looks only for cuecards in the
        //purse with the cuecard id's in the set:
        String column = DeckPurse.CUECARD_ID_COLUMN;
        SimpleExpression eq;         
        Disjunction ors = Restrictions.disjunction();
        for(Long id : cuecard_ids){
            eq = Restrictions.eq(column, id);
            ors.add(eq);
        }//
        
        //Add the disjunction to search criteria for the deck purse:
        Criteria c = TransUtil.makeGloballyFilteredCriteria(DeckPurse.class);
        c.add(ors);
        List<PurseEntity> pel = c.list();
        
        //Now we have all the records from different groups that could possibly
        //match the list we inputted. If any one group is 100% represented
        //and has a length exactly that of cuecard_ids.size(), then we have a
        //match. Else return (-1) for not found.
        
        //Get a unique list of all of the group ids:
        List<Long> group_ids = new ArrayList<Long>();
        for(PurseEntity p : pel){
            group_ids.add(p.group_id);
        }//
        List<Long> unique_group_ids;
        unique_group_ids = ListUtil.makeUnique(group_ids);
        
        //Siphon again, this time only letting groups with the correct
        //checksum into new collection:
        long target_checksum = cuecard_ids.size();
        List<Long> group_ids_with_valid_checksum = new ArrayList<Long>();
        for(Long gid : unique_group_ids){
            long cur_checksum = GroupTransUtil.getChecksumOfGroup(gid);
            if(cur_checksum == target_checksum){
                group_ids_with_valid_checksum.add(gid);
            }
        }//
        
        //We now have all of the group ids with matching checksums.
        //If one of these groups is present 100%, then we have a matching
        //Deck that is already in the database.
        HashMap<Long,Integer> attendance = new HashMap<Long,Integer>();
        
        //go back through original list of entities that had matching
        //cuecard ids, but this time, tally the ids:
        for(PurseEntity p : pel){
            long cur_group_id = p.group_id;
            boolean isValidGroup = 
                       (group_ids_with_valid_checksum.indexOf(cur_group_id)>=0);
            if(isValidGroup){
                if(attendance.containsKey(cur_group_id)){
                    int count = attendance.get(cur_group_id);
                    count++;
                    attendance.put(cur_group_id, count);
                }else{
                    attendance.put(cur_group_id, 1); //first entry.
                }
            }//
        }//
        
        //now that we have taken attendance of groups with valid checksum,
        //is one of those groups present it it's entirity? First do debug
        //checks.
        boolean matchFound = false;
        long matchFound_group_id = (-77);
        for (Map.Entry<Long, Integer> entry : attendance.entrySet()) {
            Long    group_id        = entry.getKey();
            Integer entries_present = entry.getValue();
            if(entries_present == target_checksum){
                if(true==matchFound){
                    doError("[Duplicates in database.]");
                }else{
                   matchFound=true; 
                   matchFound_group_id = group_id;
                }//should we error?
            }else 
            if(entries_present > target_checksum){
                doError("[Members/Matches exceeds group checksum.]");
            }//   
        }//next entry.
        
        //If match is found, return it:
        if(matchFound){ return matchFound_group_id;}
        return (-1); //<<return -1 for not found.
            
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = DeckTransUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
}//CLASS::END
