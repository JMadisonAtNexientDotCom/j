package app.transactions.util.forCompositeEntities;

import app.dbDataAbstractions.entities.composites.CueCard;
import app.dbDataAbstractions.entities.tables.RiddleTable;
import app.transactions.util.TransUtil;
import app.transactions.util.forNoClearTableOwner.riddleRhyme.RiddleRhymeTransUtil;
import app.transactions.util.tables.cuecard.CuecardTransUtil;
import app.transactions.util.tables.riddle.RiddleTransUtil;

/**-----------------------------------------------------------------------------
 * Handles transactions involving CueCard objects.
 * @author jmadison :2015.09.18_0739PM
 ----------------------------------------------------------------------------**/
public class CuecardPojoUtil {
    
    
    /**-------------------------------------------------------------------------
     * Makes a random cue card with the number of selections asked for.
     * @param numChoicesOnCard:How many choices does the Ninja have to choose
     *                         from when answering the Riddle the Jester
     *                         presents on the game show?
     * @param targ_true: The target amount of correct answers available to
     *                   choose from off of the CueCard. May be less than
     *                   desired, but never more than desired.
     * @return: A cue card that has been populated with data.
     ------------------------------------------------------------------------**/
    public static CueCard makeRandomCueCard(int numChoicesOnCard, int targ_true){
        
        //check to see if we are in a transaction state:
        TransUtil.insideTransactionCheck();

        //core logic:
        /** op == output variable **/
        CueCard op;
        /** rid_tab   == "riddle table" **/
        RiddleTable rid_tab = RiddleTransUtil.getOneRandomRiddle();
        long riddleID = rid_tab.getId();
        op = CuecardTransUtil.makeFilledOutCueCard
                                        (riddleID, numChoicesOnCard, targ_true);

        //return output:
        return op;

    }//FUNC::END
    
    
}//CLASS::END
