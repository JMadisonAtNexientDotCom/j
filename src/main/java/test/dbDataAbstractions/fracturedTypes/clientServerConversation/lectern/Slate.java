package test.dbDataAbstractions.fracturedTypes.clientServerConversation.lectern;

import java.util.List;

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
public class Slate {
    
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
    
}//CLASS::END
