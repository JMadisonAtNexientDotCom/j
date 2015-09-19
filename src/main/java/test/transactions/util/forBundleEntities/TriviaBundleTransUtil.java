package test.transactions.util.forBundleEntities;

import test.MyError;
import test.dbDataAbstractions.bundleTypes.TriviaBundle;
import test.transactions.util.forCompositeEntities.DeckTransUtil;
import test.transactions.util.forCompositeEntities.QuarTransUtil;

/**-----------------------------------------------------------------------------
 * A transaction utility for trivia bundles.
 * A trivia bundle is my story analogy for a single test.
 * @author jmadison :2015.09.18 at some time during the night.
 * @author jmadison :2015.09.19_0258PM Now ready for live test.
 ----------------------------------------------------------------------------**/
public class TriviaBundleTransUtil {
    /**-------------------------------------------------------------------------
     *  Creates a TrivaBundle on the fly. The UI people can pull this down,-----
     *  and fill out the QUAR (collection of answer slates) and then
     *  HTTP post the QUAR back to the server for grading.
     * @param cardCount:Number of CueCards in the deck.
     *                  Also == number of slates, because slates are
     *                  paired 1:1 with CueCards.
     * @param numQuips :How many Quips are on each CueCard?
     *                  Quips == Rhymes(answers/replies) to choose from.
     * @param truMIN   :Minimum number of valid Quips we want to target
     *                  when populating CueCard.
     * @param truMAX   :Maximum number of valid Quips we want to target
     *                  when populating CueCard.
     * @return: A bundle of CueCard(s) and Slate(s) in a nice package.
     *          The Joker now has material for their show.
     ------------------------------------------------------------------------**/
    public static TriviaBundle getRandomTrivaBundle
                          (int cardCount, int numQuips, int truMIN, int truMAX){
        TriviaBundle bund = new TriviaBundle();
        
        //Populate our triva bundle:
        bund.synopsis.overview = "Made using getRandomTrivaBundle()";
        bund.deck  = DeckTransUtil.getRandomDeckOfCueCards
                               (cardCount, numQuips, truMIN, truMAX);
        bund.quar = QuarTransUtil.makeBlankSlatesForDeck(bund.deck);
        
        //Do some basic integrity checking on the bundle:
        validateBundle(bund, cardCount);
        
        //Return the triva bundle:
        return bund;
        
    }//FUNC::END
                   
    /**-------------------------------------------------------------------------
     * Throws error if we detect lack of integrity in the Trivia Pack.
     * @param bund :Trivia bundle we are debugging.
     * @param cardCount :How many CueCards does this trivia bundle contain?
     ------------------------------------------------------------------------**/
    private static void validateBundle(TriviaBundle bund, int cardCount){
        
        //Basic error checking on triva bundle:
        if(null == bund      ){doError("BUNDLE is null"); }else
        if(null == bund.deck ){doError("null DECK made!");}else
        if(null == bund.quar ){doError("null QUAR made!");}
        
        //Check-Sum:
        int deck_len = bund.deck.cards.size();
        int quar_len = bund.quar.slates.size();
        if(deck_len != quar_len ){ doError("deck_len != quar_len"); }
        if(deck_len != cardCount){ doError("deck not to len-specs");}
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Wrapper function to throw errors from this class.
     * @param msg :Specific error message.
     ------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        err += TriviaBundleTransUtil.class.getSimpleName();
        err += msg;
        throw new MyError(err);
    }//FUNC::END
    
}//CLASS::END
