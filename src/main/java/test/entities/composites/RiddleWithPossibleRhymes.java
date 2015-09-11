package test.entities.composites;

import java.util.ArrayList;
import test.entities.tables.RhymeTable;
import test.entities.tables.RiddleTable;

/**
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
 *NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN
 * 
 * @author jmadison
 */
public class RiddleWithPossibleRhymes {
    
    public RiddleTable riddle;
    
    /** I called it "rhymeChoiceList" instead of "rhymeChoices"
     *  because I find the addition of a single "s" to make something
     *  plural to be too subtile. **/
    public ArrayList<RhymeTable> rhymeChoiceList;
    
    //EMPTY CONSTRUCTOR:
    public RiddleWithPossibleRhymes(){};
    
    public static RiddleWithPossibleRhymes makeRiddleWithEmptyRhymeChoiceList
                                                              (RiddleTable rid){
        RiddleWithPossibleRhymes op = new RiddleWithPossibleRhymes();
        op.riddle = rid;
        op.rhymeChoiceList = new ArrayList<RhymeTable>();
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
