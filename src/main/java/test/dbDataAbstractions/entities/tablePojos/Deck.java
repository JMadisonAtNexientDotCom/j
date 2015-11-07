package test.dbDataAbstractions.entities.tablePojos;

import java.util.ArrayList;
import test.dbDataAbstractions.entities.bases.CompositeEntityBase;
import java.util.List;
import test.config.constants.EntityErrorCodes;
import test.dbDataAbstractions.entities.bases.ChallengeGuts;

/** A deck is a collection of CueCards. It represents a barrage of questions
 *  that the jester is about to ask the ninja. 
 * 
 *  Think of a jester with a deck of cards.
 *  But instead of playing cards, they are CueCards with riddles on them.
 * @author jmadison                                                          **/
public class Deck extends ChallengeGuts {
    
    /** The cards in the deck that are about to 
     *  barrage(attack, mentally) the ninja. **/
    public List<CueCard> cards;
    
    //TODO: An object giving us stats on the deck.
    //      Think of it like a "magic the gathering deck"
    //      Where you can build different types of decks.
    //public Object DeckStats;
    
    /**
     * Make Deck configured as error message:
     * @param msg : The error message to populate with.
     * @return    : A deck configured as error. **/
    public static Deck makeErrorDeck(String msg){
        Deck op = new Deck();
        op.cards = new ArrayList<CueCard>();
        CueCard card = CueCard.makeErrorCueCard(msg, 4);
        op.setIsError(true);
        op.setErrorCode(EntityErrorCodes.GENERIC_ERROR);
        op.cards.add(card);
        return op;
    }//FUNC::END
    
}//FUNC::END