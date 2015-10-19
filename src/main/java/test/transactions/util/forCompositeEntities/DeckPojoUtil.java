package test.transactions.util.forCompositeEntities;

import java.util.ArrayList;
import test.MyError;
import test.dbDataAbstractions.entities.composites.CueCard;
import test.dbDataAbstractions.entities.composites.Deck;
import test.transactions.util.TransUtil;

/**-----------------------------------------------------------------------------
 * 
 * DESIGN NOTE:
 * Why considered a composite entity and not a bundle entity?
 * NOT a bundle type because a deck is a collection of uniform data.
 * A deck represents a collection of CueCards.
 * 
 * 
 * @author jmadison: 2015.09.18_0710PM
 ----------------------------------------------------------------------------**/
public class DeckPojoUtil {
    
    /**-------------------------------------------------------------------------
     * Creates a random deck of CueCards 
     * CueCards == (Cards with riddle (question) -&- possible answers (rhymes))
     * 
     * @param cardCount: How many CueCards should be in this deck?
     * @param numQuips : How many quips on each cue card?
     * @param truMIN   : Min target for number of correct [quips/choices].
     * @param truMAX   : Max target for number of correct [quips/choices].
     * @return :A deck of CueCards that the Joker can use on game show
     *          to quiz the Ninja.
     ------------------------------------------------------------------------**/
    public static Deck getRandomDeckOfCueCards
                      (int cardCount, int numQuips, int truMIN, int truMAX){
        //check to see if we are in a transaction state:
        TransUtil.insideTransactionCheck();
        
        //Input debug logic:
        if(truMIN <        0){ err("cannot have negative truth answers.");}
        if(truMAX <        0){ err("cannot have negative wrong answers.");}
        if(truMAX < truMIN  ){ err("trueMAX > trueMIN");}
        if(truMAX > numQuips){ err("max cannot exceed num choices(quips)");}
       
        //core logic:
        //Make an empty deck and populate it.
        Deck dek = new Deck();
        dek.cards = new ArrayList<CueCard>();
        
        //Build deck in loop:
        CueCard card;
        int numTru;
        int len = cardCount;
        for(int i = 0; i < len; i++){
            numTru = getRandRange(truMIN, truMAX);
            card = CuecardPojoUtil.makeRandomCueCard(numQuips, numTru);
            dek.cards.add(card);
        }//NEXT i
        
        //Error check the deck:
        if(dek.cards.size() != cardCount){
            err("deck was not made to correct cardCount specs!");
        }//Error Check

        //return output deck of cue cards.
        return dek;
        
    }//FUNC::END
                                
    /**-------------------------------------------------------------------------
     * get random number starting at min and ending at max
     * INCLUSIVE RANGE.
     * @param min :min possible value.
     * @param max :max possible value.
     * @return: random number in inclusive range min-max.
     ------------------------------------------------------------------------**/
    public static int getRandRange(int min, int max){
        return min + (int)(Math.random() * (max-min));
    }//FUNC::END
                      
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void err(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = DeckPojoUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END


