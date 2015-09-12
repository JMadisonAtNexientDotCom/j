package test.entities.composites;

import java.util.ArrayList;
import java.util.List;
import test.entities.tables.RhymeTable;
import test.entities.tables.RiddleTable;

/**
 * 
 *  The talk show host is a Jester.
 *  On the Jester's game show, he asks riddles.
 *  He uses cue cards. The anatomy of a Jester's cue
 *  card is very specific:
 * 
 *  JEST: A playful inquiry in the form of a riddle.
 *        E.g, a riddle becomes a "jest" when it is in the
 *        context of being written on a Jester's cue card.
 * 
 *  QUIP: A possible clever (or not so clever) answer to the JEST.
 *        E.g, a [rhyme/answer] becomes a QUIP when it is
 *        in the context of being written on a Jester's cue card.
 * 
 *  CueCard Diagram:
 *  +----------------------------------------+
    |                                        |
    |          [A JEST Goes Here]            |
    |                                        |
    +----------------------------------------+
    +----------------------------------------+
    |                                        |
    |      1. QUIP #1                        |
    |      2. QUIP #1                        |
    |      3. QUIP #1                        |
    |      4. QUIP #1                        |
    |                                        |
    |                                        |
    +----------------------------------------+
 * 
 * 
 * 
 * Represents a single riddle that will be on a riddle-exam.
 * It is one riddle, in a package with multiple possible answers.
 * And whatever other data might be necessary to make a nice UI.
 * 
 * Naming choice: NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN
 * Originally called it "riddle and rhymes" to mean 
 * "one riddle and multiple rhymes" But if you read it too quickly,
 * it kind of sounds like "riddles and rhymes"...
 * 
 * How about:
 * 1. riddleWithRhymes
 * 2. oneRiddleAndMultipleRhymes <--sounds like it means there is more
 *                                  than one correct answer.
 * 3. riddleWithPossibleRhymes , like this. Though a bit long.
 * 4. CueCard
 *    Why use "CueCard"?
 *    Imagine that you are a contestant on a game show
 *    like "who wants to be a millionaire". The show host is not a genious.
 *    They ask you questions by reading from a CueCard that contains
 *    a RIDDLE (question) and possible RHYMES (answers) to that question.
 * 
 *    Maybe the talk show host in our case is a crazy jester in a green hat.
 *    And it is a twisted take on jeapordy. But instead of phrasing everything
 *    as a question, you must phrase it as a rhyme to make the jester happy.
 * 
 *    The words "riddle" + "rhyme" are getting thrown around too much.
 *    And we need a new different word to avoid the confusion this is causing.
 * 
 *NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN
 * 
 * @author jmadison
 */
public class CueCard {
    
    /** Riddles in the context of CueCards are known as "Jests" **/
    public RiddleTable jest;
    
    /** 
     *  Rhymes(answers) in the context of a CueCard are known as "quips"
     *  Why? Because of the naming confusion below:
     * 
     *  It is less ambigious that "rhymeChoices" and shorter than
     *  "rhymeReplyOptions" etc. **/
    public List<RhymeTable> quips;
    
    //EMPTY CONSTRUCTOR:
    public CueCard(){};
    
    /** Makes a CueCard that is populated with a Jest, but has an 
     *  empty list of Quips.
     * @param riddleToWriteDown: A riddle we want to write down onto a new
     *                           CueCard so our jester can ask it on his
     *                           game show.
     * @return : A CueCard that is ready to be populated with Quips.         **/
    public static CueCard makeCueCard_WithJest_And_EmptyQuips
                                                (RiddleTable riddleToWriteDown){
        CueCard op = new CueCard();
        op.jest = riddleToWriteDown;
        op.quips = new ArrayList<RhymeTable>();
        return op;
    }//FUNC::END
    
    /* this might just make code hard to read.
    public static RiddleAndRhymes make
                                   (RiddleTable rid, ArrayList<RhymeTable> lst){
        RiddleAndRhymes op = new RiddleAndRhymes();
        op
    }//FUNC::END
    */
    
}//CLASS::END
