package utils.grading.slateGrading.rawScoreStats;

import primitives.Abacus;
import test.MyError;
import test.dbDataAbstractions.fracturedTypes.clientServerConversation.lectern.Slate;
import test.transactions.util.TransUtil;
import test.transactions.util.forNoClearTableOwner.riddleRhyme.RiddleRhymeTransUtil;

/**-----------------------------------------------------------------------------
 * The SlateClerk works for the AbacusJudge. SlateClerk is an accountant
 * slash bean-counter type. He carries around an abacus. When evaluating
 * the Ninja's slates, he takes detailed notes.
 * 
 * Every time he grades ONE slate, he gets out a NEW abacus, and records
 * the notes on that new abacus.
 * 
 * Basically, he converts a stack of Slates to a stack of Abacuses. (Abicai?)
 * @author jmadison
 ----------------------------------------------------------------------------**/
public class SlateClerk {
   
    
    /**-------------------------------------------------------------------------
     * Main job of the SlateClerk, to grade slates.
     * @param s: The Slate the Ninja filled out that now needs to be graded.
     * @return : An Abacus with a rather complex raw talley of stats.
     *           These stats will be used by the SlateJudge later. ----------**/
    public static Abacus gradeSlate(Slate s){
        
        //Grading slates requires we talk to database. So you must
        //be inside a transaction state:
        TransUtil.insideTransactionCheck();
        
        //Core Logic:
        Abacus talley = new Abacus();
        talley.wasSkipped  = false;
        talley.evadedTrick = false;
        
        //add to the abacus, how many main options/choices the Ninja had
        //to choose from. AKA: How many Quips were on the CueCard the Jester
        //read to our Ninja.
        setMainSelectionsTotal(talley, s);
      
        if(s.skip_mysteryToMe){//-----------------------------------------------
            talley.mainSelections_truth = 0;
            talley.mainSelections_wrong = 0;
            talley.wasSkipped = true;
            talley.evadedTrick= false;
        }else
        if(s.none_noSolutionExists){
            
            //Ninja has given answer proclaiming there is no solution 
            //for the Riddle (problem) that has been presented.
            //See if the Ninja just evaded a trick question:
            //------------------------------------------------------------------
            int numberOfSolutions = RiddleRhymeTransUtil.getNumberOf_TRUTH
                                                (s.riddleID, s.originalQuips);
            if(0==numberOfSolutions){
                talley.evadedTrick = true;
            }
            //------------------------------------------------------------------
            
            //Regardless, no wrong or right selections were made:
            talley.mainSelections_truth = 0;
            talley.mainSelections_wrong = 0;
            
        }else{
            //The most common use case:
            //Ninja has NOT skipped the question.
            //And Ninja has not backed down from answering the question.
            talley.mainSelections_truth = RiddleRhymeTransUtil.
                                getNumberOf_TRUTH(s.riddleID, s.rhymeSelection);
            talley.mainSelections_wrong = RiddleRhymeTransUtil.
                                getNumberOf_WRONG(s.riddleID, s.rhymeSelection);
        }//---------------------------------------------------------------------
        
        //return the abacus representing a tally/grade of the slate:
        return talley;
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Figures out the original number of (quips/choices) that were on the
     * CueCard that the Slate is attempting to answer.
     * @param talley : The abacus that is tallying up the info on the Slate.
     * @param s      : The filled out Slate that is attempting to answer a
     *                 Riddle (Question) that has been put on a CueCard.
     ------------------------------------------------------------------------**/
    private static void setMainSelectionsTotal(Abacus talley, Slate s){
        
        //This function relies on side-effects put into the slate.
        //Lets do null-checks to make sure input references exist:
        if(null==talley){throw new MyError("talley supplied cannot be null.");}
        if(null==s     ){throw new MyError("slate supplied cannot be null.");}
        
        //[mst][mst][mst][mst][mst][mst][mst][mst][mst][mst][mst][mst][mst][mst]
        int     numOriginalQuips = s.originalQuips.size();
        boolean isCueCardConfigHashValid = 
                                 validateCueCardConfigHash(s.cueCardConfigHash);
        if(numOriginalQuips <= 0 && isCueCardConfigHashValid){
            String msg = "You have neither a list of original quips";
            msg += "From the CueCard this Slate is answering.";
            msg += "Nor do you have a valid CueCardConfigHash to identify";
            msg += "The ID of the original CueCard that was asked.";
            throw new MyError(msg);
            
            //TODO: I don't think crashing is a good idea here.
            //Maybe a .tampered results flag set to true and abort
            //grading of this question?
            
        }else
        if(numOriginalQuips <= 0)
        {
            //Use the VALID hash to find out how many quips were originally
            //on the CueCard that this Slate is attempting to address.
            talley.mainSelections_total = 
                                cueCardHashToNumberOfQuips(s.cueCardConfigHash);
            if(talley.mainSelections_total <= 0){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEE
                String msg = "[talley.mainSelections_total <= 0]";
                msg+="[1:Possible Reasons:]";
                msg+="[2:cueCardConfig hash is bad.]";
                msg+="[3:improperly configured card in CUE_CARD_REPO_TABLE]";
                msg+="[4:bad-logic in cueCardHashToNumberOfQuips function]";
                throw new MyError(msg);
            }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        }else
        if(numOriginalQuips > 0){
            talley.mainSelections_total = numOriginalQuips;
        }else{
            throw new MyError("This line should never execute");
        }
        //[mst][mst][mst][mst][mst][mst][mst][mst][mst][mst][mst][mst][mst][mst]
    }//FUNC::END
    
    /** See if the cue card config hash is actually valid information-----------
     *  by checking to see if it's hash exists in the 
     *  CUE_CARD_REPO_TABLE. 
     * @param hash : A ~reversable~ hash storing configuration information
     *               on the structure of the CueCard. We do this so all
     *               CueCards in the database can be UNIQUE. 
     * @return     : Returns TRUE if the hash exists in CUE_CARD_REPO_TABLE
     ------------------------------------------------------------------------**/
    private static boolean validateCueCardConfigHash(String hash){
        
        //TODO: Logic.
        //Right now, we don't have a CUE_CARD_REPO_TABLE so we can safely
        //say all validation fails, because none of these hashes exist
        //in the table. (Because the table doesn't exist)
        return false;
    }//FUNC::END
    
    private static int cueCardHashToNumberOfQuips(String hash){
        //TODO: LOGIC!
        
        //HACK: Make sure we throw error if this function executes.
        //But make compiler unaware that return is unreachable.
        if(hash==""){
           throw new MyError("TODO! cueCardHashToNumberOfQuips"); 
        }else
        if(hash!=""){
            throw new MyError("TODO! cueCardHashToNumberOfQuips"); 
        }
        
        
        return (0-7777);
    }//FUNC::END
    
}//CLASS::END
