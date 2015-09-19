package test.dbDataAbstractions.entities.composites;

import java.util.List;

/** A deck is a collection of CueCards. It represents a barrage of questions
 *  that the jester is about to ask the ninja. 
 * 
 *  Think of a jester with a deck of cards.
 *  But instead of playing cards, they are CueCards with riddles on them.
 * @author jmadison                                                          **/
public class Deck extends CompositeEntityBase {
    
    /** The cards in the deck that are about to 
     *  barrage(attack, mentally) the ninja. **/
    public List<CueCard> cards;
    
    //TODO: An object giving us stats on the deck.
    //      Think of it like a "magic the gathering deck"
    //      Where you can build different types of decks.
    //public Object DeckStats;
    
}//FUNC::END
