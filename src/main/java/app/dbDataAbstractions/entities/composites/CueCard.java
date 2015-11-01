package app.dbDataAbstractions.entities.composites;

import app.dbDataAbstractions.entities.bases.CompositeEntityBase;
import java.util.ArrayList;
import java.util.List;
import app.MyError;
import app.dbDataAbstractions.entities.tables.RhymeTable;
import app.dbDataAbstractions.entities.tables.RiddleTable;

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
public class CueCard extends CompositeEntityBase{
    
    /** Riddles in the context of CueCards are known as "Jests" **/
    public RiddleTable jest;
    
    /** 
     *  Rhymes(answers) in the context of a CueCard are known as "quips"
     *  Why? Because of the naming confusion below:
     * 
     *  It is less ambiguous that "rhymeChoices" and shorter than
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
                                                            
    /**-------------------------------------------------------------------------
     *  Extracts the ID values from our quips.
     *  Will throw an error if the quip list is null or empty.
     *  Because the only time we want to be extracting data from
     *  this list is when there is data present to extract.
     * 
     *  Original use:
     *  Creating properly partially-filled-out Slates for the Ninja.
     *  This ~expidites~ the slate filling out process and makes it easier
     *  on our UI programmers.
     * 
     * @param c :The CueCard to extract quipIDsfrom.
     * @return  :A list of quipsIDs in SAME ORDER as the
     *           quips they were extracted from.
     ------------------------------------------------------------------------**/                                          
    public static List<Long> extractQuipIDs(CueCard c){
        
        //ERROR CHECK INPUT:
        if(null==c){doError("cue card supplied was null!");}
        if(null==c.quips){doError("quips list was null!");}
        if(c.quips.size() <= 0){doError("quips was EMPTY!");}
        
        //Create our output:
        RhymeTable cur_quip;
        int len = c.quips.size();
        List<Long> op = new ArrayList<Long>(len);
        for(int i = 0; i < len; i++){            
            cur_quip = c.quips.get(i);
            //op.set(i, cur_quip.getId() ); <-- cant get this to work.
            op.add(i, cur_quip.getId() ); //<--oooh.. overloaded add.
        }//NEXT i
        
        //Return output: Extracted IDS:
        return op;
        
    }//FUNC::END
                      
    /**
     * Creates a CueCard populated with an error message. Used to let the 
     * USER-INTERFACE people know they did something wrong with their 
     * restful get http request.
     * 
     * IMPLEMENTATION:
     * We want the ERROR JSON response to have the EXACT SAME DATA FORMAT
     * as if it were a valid response. So we don't have to code handling for
     * another data format in the UI.
     * 
     * REASONING:
     * Taking this logic for Valve and the Half-Life hammer editor.
     * If there was an error in rendering a model in the game, the model
     * was simply replaced with a model that said "ERROR".
     * 
     * @param errorMessage : The error message to display.
     * @return  **/
    public static CueCard makeErrorCueCard
                                       (String errorMessage, int numberOfQuips){
        CueCard op = new CueCard();
        op.jest  = RiddleTable.makeErrorRiddle(errorMessage);
        op.quips = new ArrayList<RhymeTable>();
        RhymeTable currentEntry;
        for(int i = 0; i < numberOfQuips; i++){
            currentEntry = RhymeTable.makeErrorRhyme(errorMessage);
            op.quips.add(currentEntry);
        }//NEXT I
        
        //return the CueCard configured as an Error message.
        return op;
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    * Wrapper function to throw errors from this class.
    * @param msg :Specific error message.
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
       String err = "ERROR INSIDE:";
       err += CueCard.class.getSimpleName();
       err += msg;
       throw MyError.make(CueCard.class, err);
    }//FUNC::END
    
}//CLASS::END
