package test.entities.composites;

import java.util.ArrayList;
import test.entities.tables.RhymeTable;
import test.entities.tables.RiddleTable;

/**
 * Represents a single riddle that will be on a riddle-exam.
 * It is one riddle, in a package with multiple possible answers.
 * And whatever other data might be necessary to make a nice UI.
 * @author jmadison
 */
public class RiddleAndRhymes {
    
    private RiddleTable riddle;
    
    /** I called it "rhymeChoiceList" instead of "rhymeChoices"
     *  because I find the addition of a single "s" to make something
     *  plural to be too subtile. **/
    private ArrayList<RhymeTable> rhymeChoiceList;
    
}//CLASS::END
