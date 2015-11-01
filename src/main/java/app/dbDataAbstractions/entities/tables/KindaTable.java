package app.dbDataAbstractions.entities.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import app.config.constants.identifiers.TableNameReg;
import app.config.constants.identifiers.VarNameReg;
import app.dbDataAbstractions.entities.bases.BaseEntity;
import static app.dbDataAbstractions.entities.tables.NinjaTable.NAME_COLUMN;
/**-----------------------------------------------------------------------------
 * GENERALLY:
 * Represents a test+response bundle owned by a single user.
 * 
 * ABSTRACTLY:
 * The KIND table binds together the guts of a [test/trial] to the guts
 * of a [response/set of answers] to a UNIQUE token_id.
 * 
 * NOTES:
 *    NOTE #1: Compiler may give you false warning that persistent fields
 *             need to be private. This is a lie. I think using getters+setters
 *             is total BS for this. What are you encapsulating? NOTHING.
 *             What are you cluttering? EVERYTHING.
 *
 * @author jmadison:2015.10.27(Oct27th,Year2015,Tuesday) --------------------**/

//---------------TRIAL_TABLE & KINDA_TABLE DIAGRAMS: START---------------------+
//TRIAL TABLE:                                                                 |
//kind:     A string that identifies what type of [trial/test] it is.          |
//token_id: Who owns these [tests/trial]? If you own token, you own test.      |
//status:   tells us if the test is: created,started,submitted etc.            |
//began_on: universal time in milliseconds that [trial/test] was started.      |
//ended_on: universal time in milliseconds that [trial/test] was submitted.    |
//                                                                             |
//KINDA_TABLE:                                                                 |
//kind: Same as in trial_table.                                                |
//token_id: Who owns this record? If you own the token, you own the record.    |
//challenge_id:  Points to data representing the guts of the trial.            |
//has_challenge: True if challenge_id is valid. False otherwise.               |
//effort_id:  Represents the "effort" the ninja put forth in solving the       |
//            specific challenge of the trial.                                 |
//has_effort: True if effort_id is valid.                                      |
//            Else we assume no effort has been made.                          |
//                                                                             |
//TRIAL TABLE:                                                                 |
//+------+----------+--------+----------+----------+----------+                |
//| kind | token_id | status | began_on | ended_on | allotted |                |
//+------+-----x----+--------+----------+----------+----------+                |
//             |                                                               |
//KINDA_TABLE: |                                                               |
//+------+-----x----+--------------+---------------+----------|                |
//| kind | token_id | challenge_id | has_challenge | effort_id /..             |
//+------+----------+--------------+---------------+----------|                |
//                        |-----------+-----------+-----------+                |
//                     ../ has_effort | is_graded | grade_10k |                |
//                        |-----------+-----------+-----------+                |
//                                                                             |
////WHAT I DIDN'T DO, AND WHY:XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX|
//Thought I could have trial_table use "kind_id" rather than "kind"            |
//So that it could identify the kinda_table record that way.                   |
//And then token_id would only have to appear in trial_table, not in           |
//kinda_table. And then, in the event of two candidates getting the            |
//exact same test and answering the exact same way, they could reference       |
//the EXACT SAME KIND_ID from the kinda_table.                                 |
//                                                                             |
//Problem with that:                                                           |
//1. They have to start with separate kinda_table stubs.                       |
//   So you would be doing a merge on the data.                                |
//2. If we want to add a re-take test in the future, you would have to         |
//   UN-MERGE those results. Seems like a pain to me.                          |
////XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX|
//                                                                             |
////WHAT I DID DO, AND WHY: ===================================================|
//                                                                             |
//KIND: kind is present in BOTH trial_table and kinda_table:                   |
//      It is used for redundancy. The kind value should match in both records.|
//      Also, if we want to destroy kinda_table record and create a new test   |
//      with same token, we could, since trial_table is also keeping track     |
//      of the kind.                                                           |
//                                                                             |
//TOKEN_ID: One trial_table record per token_id, one kinda_table record per    |
//          token_id. Thus, we can link/associate the information together     |
//          using the token_id. This probably also means we could just make    |
//          it one big HUGE table. But that would make the entities larger,    |
//          the transaction utilities larger, the test servlets larger... etc. |
//          This seems like a logical division the way I have partitioned the  |
//          tables.                                                            |
//                                                                             |
//MORE INFO:                                                                   |
//More on token_id:                                                            |
//Question: Why not tie to ninja_id or admin_id?                               |
//          Because Ninjas + Admins are two completely separate user types.    |
//          They are not even derived from each other. Ninjas cannot own an    |
//          account and login/password information. Ninjas can however,        |
//          own tokens.                                                        |
//                                                                             |
//          Tokens are the common currency. Whenever we have the choice of     |
//          using different foreign keys in our design, the arbitration        |
//          should be solved by using token_id, if that is one of the          |
//          choices.                                                           |
//More on status:                                                              |
//          There are other options between created and started.               |
//          But I can't remember exactly what they were called. But basically, |
//          there is another step where the user confirms the token, but has   |
//          NOT started the test yet.                                          |
//Defining "guts of challenge:                                                 |
//          Guts == all the questions on test. No meta data. Just questions.   |    
//More on effort_id:                                                           |
//          Could also think of it as                                          |
//          a "response". But response is too generic for me.                  |
//          Because response could mean "response to a single question"        |
//          or "response to a collection of questions. "effort" in this        |
//          context explicitly means "a collection of responses."              |
//More on has_challenge & has_effort:                                          |
//          Used so we can safely have null references when foreign key        |
//          does not exist. For example no challenge data or effort data will  |
//          exist when stubs are first created. When trial-questions have been |
//          generated, challenge_id will be a value >=1, and has_challenge will|
//          == true. has_effort however will remain false until ninja has      |
//          submitted all of their answers for grading. Then an effort object  |
//          will be created to reflect the ninja's responses.                  |
//==================TRIAL_TABLE & KINDA_TABLE DIAGRAMS: END====================+
@Entity
@Table(name= KindaTable.TABLE_NAME)  //<--can we replace this with TABLE_NAME ?
public class KindaTable extends BaseEntity {
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME          = TableNameReg.KINDA_TABLE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN           = VarNameReg.ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String COMMENT_COLUMN      = VarNameReg.COMMENT;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String KIND_COLUMN         = VarNameReg.KIND;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String TOKEN_ID_COLUMN     = VarNameReg.TOKEN_ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String CHALLENGE_ID_COLUMN = VarNameReg.CHALLENGE_ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String HAS_CHALLENGE_COLUMN= VarNameReg.HAS_CHALLENGE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String EFFORT_ID_COLUMN    = VarNameReg.EFFORT_ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String HAS_EFFORT_COLUMN   = VarNameReg.HAS_EFFORT;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String IS_GRADED_COLUMN    = VarNameReg.IS_GRADED;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String GRADE_10K_COLUMN    = VarNameReg.GRADE_10K;

    /** The kind of [trial/test] being taken.
     *  challenge_id and effort_id are special foreign keys.
     *  What tables they reference depends on what kind of test this is. **/
    @Column(name=KIND_COLUMN)
    public String kind; //<--NOTE:#1
    @Transient @JsonIgnore
    public final String KIND = KIND_COLUMN; //for API access.
    
    /** A unique token_id that should only appear in ONE RECORD within the
     *  kind table. It is the token associated with this test. The ninja who
     *  owns this token can take this test. **/
    @Column(name=TOKEN_ID_COLUMN)
    public Long token_id;
    @Transient @JsonIgnore
    public final String TOKEN_ID = TOKEN_ID_COLUMN; //for API access.
    
    /** A foreign key pointing to the [guts/root] of the [test/trial] given. **/
    @Column(name=CHALLENGE_ID_COLUMN)
    public Long challenge_id;
    @Transient @JsonIgnore
    public final String CHALLENGE_ID = CHALLENGE_ID_COLUMN; //for API access.
    
    /** A boolean to let us know if CHALLENGE_ID should be a valid value. **/
    @Column(name=HAS_CHALLENGE_COLUMN)
    public Boolean has_challenge;
    @Transient @JsonIgnore
    public final String HAS_CHALLENGE = HAS_CHALLENGE_COLUMN; //for API access.
    
    /** A foreign key pointing to the [guts/root] of the [response/answers] 
     *  [given/submitted] by the ninja for the [test/trial] given. **/
    @Column(name=EFFORT_ID_COLUMN)
    public Long effort_id;
    @Transient @JsonIgnore
    public final String EFFORT_ID = EFFORT_ID_COLUMN; //for API access.
    
    /** A boolean to let us know if EFFORT_ID should be a valid value. **/
    @Column(name=HAS_EFFORT_COLUMN)
    public Boolean has_effort;
    @Transient @JsonIgnore
    public final String HAS_EFFORT = HAS_EFFORT_COLUMN; //for API access.
    
    /** Boolean to let us know if this test has been graded. **/
    @Column(name=IS_GRADED_COLUMN)
    public Boolean is_graded;
    @Transient @JsonIgnore
    public final String IS_GRADED = IS_GRADED_COLUMN; //for API access.
    
    /** A normalized grade that is between 0 and 10,000.
     *  In percent, that is 0% to %100.00 with 2 significant digits. **/
    @Column(name=GRADE_10K_COLUMN)
    public Long grade_10k;
    @Transient @JsonIgnore
    public final String GRADE_10K = GRADE_10K_COLUMN; //for API access.
    
    
}//CLASS::END
