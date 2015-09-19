package test.transactions.util.forBundleEntities;

import primitives.Synopsis;
import test.MyError;
import test.dbDataAbstractions.bundleTypes.TriviaBundle;
import test.dbDataAbstractions.entities.composites.CueCard;
import test.dbDataAbstractions.fracturedTypes.clientServerConversation.lectern.Slate;
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
        bund.synopsis = Synopsis.makeDefault();
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
        if(null == bund      ){doError("BUNDLE is null" );}else
        if(null == bund.deck ){doError("null DECK made!");}else
        if(null == bund.quar ){doError("null QUAR made!");}else
        if(null == bund.synopsis){doError("synnop..NULL");}
        
        //Check-Sum:
        int deck_len = bund.deck.cards.size();
        int quar_len = bund.quar.slates.size();
        if(deck_len != quar_len ){ doError("deck_len != quar_len"); }
        if(deck_len != cardCount){ doError("deck not to len-specs");}
        
        //Check to make sure that the "original quips" in our slates
        //matches the quips that were in the cards.
        CueCard cur_ccard;
        Slate   cur_slate;
        int len = cardCount;
        for(int i = 0; i < len; i++){
            cur_ccard = bund.deck.cards.get(i);
            cur_slate = bund.quar.slates.get(i);
            validateCueCardQuipsMatchSlate(cur_ccard, cur_slate);
        }//NEXT i
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Makes sure that the blank slate has it's original quips selection
     * matching the CueCard it ~corrosponds~ to.
     * We want the slate to include what the original options were because
     * what WRONG options were presented along with the slated
     * riddle (question) could influence how difficult the question is.
     * @param c :c is for CueCard. The reads riddles from CueCards.
     *           One riddle per CueCard.
     * @param s :s is for Slate. The slate the Ninja uses to write down
     *           rhymes(answers) for a single riddle(question) that the
     *           Joker reads from slate.
     ------------------------------------------------------------------------**/
    public static void validateCueCardQuipsMatchSlate(CueCard c, Slate s){
        if(null == c.quips){doError("quips of cue card are null");}
        if(null == s.originalQuips){doError("originalQuips of Slate are null");}
        int a = c.quips.size();
        int b = s.originalQuips.size();
        if(a != b){doError("both quip arrays should be same length!");}
        
        //If we can make it this far, we can check to make sure they all match.
        //Note: ORDER MATTERS.
        long cueCard_quipID;
        long slate_quipID;
        int len = a; //<-- number of quips in both arrays.
        for(int i = 0; i < len; i++){
            cueCard_quipID = c.quips.get(i).getId();
            slate_quipID   = s.originalQuips.get(i);
            if(cueCard_quipID != slate_quipID){
                doError("[cueCard_quipID != slate_quipID]");
            }//////////////////////////////////
        }//NEXT i
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
