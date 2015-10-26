/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.transactions.util.tables.deck;

import java.util.ArrayList;
import java.util.List;
import test.MyError;
import test.dbDataAbstractions.entities.composites.CueCard;
import test.dbDataAbstractions.entities.composites.Deck;
import test.dbDataAbstractions.entities.tables.RiddleTable;
import test.transactions.util.TransUtil;
import test.transactions.util.tables.cuecard.CuecardTransUtil;
import utils.RandomSetUtil;

/**
 * A utility used for transactions involving the DeckTable entity.
 * @author jmadison :2015.10.19
 */
public class DeckTransUtil {
    
   
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
     * Looks at the group of cuecard_ids and returns a valid
     * group_id if that list of cuecard_ids exist.
     * Right now:
     * 1. Order of cuecards does NOT matter for matching.
     * 2. Amount does. A list of 3 cannot match a list of 5.
     * @param cuecard_ids:The cuecard ids to find a group matching it.
     * @return :Returns >=1 if found, returns (-1) if not found.
     */
    public static long getGroupID(List<Long> cuecard_ids){
        doError("[TODO: getGroupID]");
        return (-1);
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
