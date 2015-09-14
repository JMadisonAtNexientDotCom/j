package test.dbDataAbstractions.fracturedTypes.clientServerConversation.lectern;

import java.util.ArrayList;
import java.util.List;
import test.dbDataAbstractions.fracturedTypes.FracturedTypeBase;

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
    
    public static long ERROR_BAD_RIDDLE_USED_TO_MAKE_SLATE = (-999);
    
    /** The id of the riddle(question) that has been presented to
     *  the ninja (test taker / candidate) **/
    public long riddleID;
    
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
        op.rhymeSelection = new ArrayList<Long>();
        return op;
    }//FUNC::END
    
    /** Make a slate configured to bring attention to itself as an error.
     * @param inputRiddleID :The invalid riddle ID used that triggered us
     *                       to make an error response.
     * @return :A slate object configured as an error.                       **/
    public static Slate makeErrorSlate(long inputRiddleID){
        Slate op = new Slate();
        op.riddleID = ERROR_BAD_RIDDLE_USED_TO_MAKE_SLATE;
        op.rhymeSelection = new ArrayList<Long>();
        
        //Put riddleID 4 times in selection area to hopefully show
        //people there is an error. Also, make sure the inputted riddle
        //id is converted to negative so it is obviously invalid.
        long iri = inputRiddleID;
        long invalidRiddleID = (iri<0 ? iri : 0-iri); //MAKE NEGATIVE.
        op.rhymeSelection.add(invalidRiddleID);
        op.rhymeSelection.add(invalidRiddleID);
        op.rhymeSelection.add(invalidRiddleID);
        op.rhymeSelection.add(invalidRiddleID);
        return op;
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