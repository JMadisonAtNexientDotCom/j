package app.dbDataAbstractions.fracturedTypes.clientServerConversation.lectern;

import java.util.ArrayList;
import java.util.List;
import app.MyError;
import app.dbDataAbstractions.fracturedTypes.FracturedTypeBase;

/**-----------------------------------------------------------------------------
 * Represents a tiny "black board" / "chalk board" that is at the Ninja's 
 * lectern. The ninja writes on ONE of these when answering ONE of the riddles
 * on ONE of the joker's CueCard(s).
 * 
 * In more generic terms:
 * It is a questionResponse to a questionWithPossibleAnswers.
 * 
 * I am elaborating with this analogy because:
 * questionAnswer and questionResponse are TOO similar for my liking.
 * Combining the words "question" + "answer" in all these ways leads to
 * confusion for me.
 * 
 * How many variables can you use with the word "question" or "answer"
 * before you decide: "we need a new idea/word for this concept?"
 * 
 * Imagine if we didn't have a word for "cake" so we called the
 * variable: "bakedSweetDough".
 * "cake" is much more concise.
 * 
 * @author jmadison :2015.09.13----------------------------------------------**/
public class Slate extends FracturedTypeBase{
    
    /** Used to populate a slate object with error data. **/
    public static long ERROR_BAD_RIDDLE_USED_TO_MAKE_SLATE = (-999);
    
    /** Used for slates that have been configured with 100% WRONG answers.
     *  Used to debug our grading functions. **/
    public static String SLATE_DEBUG_TYPE_WRONG = 
                        "SLATE_DEBUG_TYPE_WRONG";
    
    /** Used for slates that have been configured with 100% CORRECT answers.
     *  Used to debug our grading functions. **/
    public static String SLATE_DEBUG_TYPE_TRUTH =
                        "SLATE_DEBUG_TYPE_TRUTH";
    
    /** The id of the riddle(question) that has been presented to
     *  the ninja (test taker / candidate) **/
    public long riddleID;
    
    /** The original [choices/options] that were written on the CueCard.
     *  The Jester, after reading the "Jest/title" on the CueCard, then
     *  read off the [quips/choices] the Ninja was to select from. **/
    public List<Long> originalQuips;
    
    /** A reverseable configuration hash that can be used to identify
     *  the original CueCard that this Slate is attempting to address.
     *  The originalQuips can be derived from this hash.
     *  The originalQuips exists so we can get everything working right away
     *  and deal with implementation of cueCardConfigHash at a later date. **/
    public String cueCardConfigHash;
    
    /** A selection of rhymeID(s) (answer id(s)) that the Ninja has selected.
     *  The ninja believes that any item from this list can satisfy the
     *  riddle (question) being asked. **/
    public List<Long> rhymeSelection;
    
    /** If true, the Ninja has skipped the riddle(question) **/
    public boolean skip_mysteryToMe = false;
    
    /** If true, the Ninja has NOT answered the riddle (question). -------------
     *  But not because Ninja does not know. Ninja believes there  -------------
     *  is NO CORRECT answer to the riddle(problem/question) that  -------------
     *  has been presented.                                        ----------**/
    public boolean none_noSolutionExists = false;
    
    /**
     *  Makes a blank slate object that can be serialized and sent to the
     *  Client/UI to be filled out. A SLATE is a filled out answer sheet
     *  that answers the RHYME (question) that the Jester is reading from
     *  a CueCard.
     * @param inputRiddleID:The riddleID ninja is attempting to answer.
     * @return :A blank slate that the ninja is to fill out.
     *          If the ID exists in rhymeSelection, then it is selected. **/
    public static Slate makeBlankSlate(long inputRiddleID){
        Slate op = new Slate();
        op.riddleID = inputRiddleID;
        op.originalQuips  = new ArrayList<Long>();
        op.rhymeSelection = new ArrayList<Long>();
        return op;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Throw an error if the slate does NOT conform to what a blank
     * slate should look like.
     * @param s :The slate to check.
     ------------------------------------------------------------------------**/
    public static void assertBlankSlate(Slate s){
        if(s.riddleID < 0){doError_badSlateIndex();}
        if(null==s.originalQuips ){doError("original quips was null!");}
        if(null==s.rhymeSelection){doError("rhyme selection was null!");}
        if(s.originalQuips.size() > 0){doError("org quips not empty");}
        if(s.rhymeSelection.size() > 0){doError("org quips not empty");}
        
        if(s.skip_mysteryToMe != false)
        {doError("skip should be false for blank slate");}
        
        if(s.none_noSolutionExists != false)
        {doError("no solution option should be false for blank slate");}
        
    }//FUNC::END
    
    /** Make a slate configured to bring attention to itself as an error.
     * @param inputRiddleID :The invalid riddle ID used that triggered us
     *                       to make an error response.
     * @return :A slate object configured as an error.                       **/
    public static Slate makeErrorSlate(long inputRiddleID){
        Slate op = new Slate();
        op.riddleID = ERROR_BAD_RIDDLE_USED_TO_MAKE_SLATE;
        op.originalQuips  = new ArrayList<Long>();
        op.rhymeSelection = new ArrayList<Long>();
        
        //Put riddleID 4 times in selection area to hopefully show
        //people there is an error. Also, make sure the inputted riddle
        //id is converted to negative so it is obviously invalid.
        long iri = inputRiddleID;
        long invalidRiddleID = (iri<0 ? iri : 0-iri); //MAKE NEGATIVE.
        
        op.originalQuips.add(invalidRiddleID);
        op.originalQuips.add(invalidRiddleID);
        op.originalQuips.add(invalidRiddleID);
        op.originalQuips.add(invalidRiddleID);
        
        op.rhymeSelection.add(invalidRiddleID);
        op.rhymeSelection.add(invalidRiddleID);
        op.rhymeSelection.add(invalidRiddleID);
        op.rhymeSelection.add(invalidRiddleID);
        return op;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = Slate.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
    public static void doError_badSlateIndex(){
        String msg = "Bad blank slate riddle id. Must be postive.";
        msg+="Blank slates must be titled with Riddle.";
        doError(msg);
    }//FUNC::END
    
}//CLASS::END

//NOTES:
///////////////How rhymeSelection property works: //////////////////////////////
//Originally was thinking makeBlankSlate() would have structure dynamically
//Created. But then realized my op.rhymeSelection is NOT a map<int,boolean>
//but rather a selection set where everything in the set is selected as what
//the ninja believes to be the true answer.
//
// INVALID NOTE:
// A blank slate with enough slots allocated to fit nicely in with
// the UI. AKA: UI designer does not have to create slate to
// be filled out. Rather, the SERVER handles the creation of
// this helper object.    
////////////////////////////////////////////////////////////////////////////////