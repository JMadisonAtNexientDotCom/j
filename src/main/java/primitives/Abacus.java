package primitives;

/**-----------------------------------------------------------------------------
 * An abacus represents the graded results of a Ninja's Slate.
 * Using an abacus for this concept because:
 * 1. It talleys things.
 * 2. It is mutable.
 * 3. It fits with the pre-computer theme of the analogy.
 *    And also fits very well with the Ninja part of the theme.
 * 
 * NOTE: Does not store final question scoring.
 *       Just houses all of the raw stats that will be used to
 *       calculate the grade later.
 * 
 * ORIGINAL USAGE: Created so I could implement API calls:
 *                 /GradeOneSlate & /GradeManySlates
 * 
 * @author jmadison :2015.09.16 ---------------------------------------------**/
public class Abacus {
    
    /** Total number of correct selections made by ninja
     *  for this [Riddle/Question]  -----------------------------------------**/
    public int mainSelections_truth = 0;
    
    /** Total number of selections made by ninja that were evaluated
     *  as INCORRECT/WRONG answers. **/
    public int mainSelections_wrong = 0;
    
    /** The total number of Rhymes(Answers) that were available for selection.
     *  NOT INCLUDING the wasSkipped+evadeTrick options. **/
    public int mainSelections_total = 0;
    
    /** Mark this to TRUE if the Ninja(user/candidate) skips the
     *  the Riddle(question) **/
    public boolean wasSkipped = false;
    
    /** Mark this to TRUE if the Ninja(user/candidate) selects 
     *  that there were no possible solution to the question... 
     * And they were... CORRECT! **/
    public boolean evadedTrick = false;
    
}//CLASS::END
