package test.dbDataAbstractions.entities.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import test.config.constants.identifiers.TableNameReg;
import test.config.constants.identifiers.VarNameReg;
import test.dbDataAbstractions.entities.bases.BaseEntity;

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

/**
 *
 * @author jmadison : Unknown Start date.
 * @author jmadison : 2015.10.28(Oct28th,Year2015,Wednesday)
 *                  - added diagrams to header.
 */
@Entity
@Table(name= TrialTable.TABLE_NAME) 
public class TrialTable extends BaseEntity {
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME     = TableNameReg.TRIAL_TABLE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN      = VarNameReg.ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String COMMENT_COLUMN = VarNameReg.COMMENT;
    
    //COLUMNS UNIQUE TO THIS TABLE://///////////////////////////////////////////
    /** Column name stored as string constant for easy refactoring. **/
    public static final String TOKEN_ID_COLUMN = VarNameReg.TOKEN_ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String KIND_COLUMN     = VarNameReg.KIND;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String STATUS_COLUMN   = VarNameReg.STATUS;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String BEGAN_ON_COLUMN = VarNameReg.BEGAN_ON;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ENDED_ON_COLUMN = VarNameReg.ENDED_ON;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ALLOTTED_COLUMN = VarNameReg.ALLOTTED;
    ////////////////////////////////////////////////////////////////////////////
    
    /** Token that OWNS this trial. Token CANNOT own more than one trial.
     *  Because tokens given to Ninjas are basically used as access tokens
     *  to access a specific test that the Admin has dispatched for them.**/
    @Column(name=TOKEN_ID_COLUMN)
    private Long token_id;
    
    /** The [type/kind] of trail. Bow+Arrow test? Java Test? Caligraphy test?
        Being facetious. But hopefully that illustrates the context/usage. **/
    @Column(name=KIND_COLUMN)
    private String kind; //<--"type" is reserved word in SQL. So used "kind"
    
    /** What is the state of the test?
     *  At time of writing:
     *  1. CREATED_INITIALIZED
     *  2. BEGAN_OPENED
     *  3. ENDED_CLOSED
     *  4. GRADED_FINALIZED **/
    @Column(name=STATUS_COLUMN)
    private String status; //<--"state" is reserved word in SQL. So used "status"
    
    /** 
     *  Time in milliseconds that [trial/test] was started.
     *  Don't want this to be grammatically confused with the state of "began".
     *  so appending the word "on". **/
    @Column(name=BEGAN_ON_COLUMN)
    private Long began_on;
    
    /** 
     *  Time in milliseconds that [trial/test] was [terminated/submitted].
     *  Don't want this to be grammatically confused with the state of "began".
     *  so appending the word "on". **/
    @Column(name=ENDED_ON_COLUMN)
    private Long ended_on;
    
    /** The amount of allotted time for this test. 
        NOTE: "allotted is spelled with TWO "L" and TWO "T" **/
    @Column(name=ALLOTTED_COLUMN)
    private Long allotted;
    
    //DESIGN NOTE: private vars are ref types. While getters and setters are
    //             value types. Done to avoid problems with hibernate's
    //             boxing and unboxing of values.
    
    //GETTERS//GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG
    public long getToken_id() {
        return this.unBoxLong(token_id );
    }//FUNC::END

    public String getKind() {
        return kind;
    }//FUNC::END

    public String getStatus() {
        return this.status;
    }//FUNC::END

    public long getBegan_on() {
        return this.unBoxLong( began_on );
    }//FUNC::END

    public long getEnded_on() {
        return this.unBoxLong( ended_on );
    }//FUNC::END

    public long getAllotted() {
        return this.unBoxLong( allotted );
    }//FUNC::END
    //END OF GETTERS//GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG
    
    //SETTERS//SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
     public void setToken_id(long token_id) {
        this.token_id = this.boxUpLong( token_id );
    }//FUNC::END

    public void setKind(String kind) {
        this.kind = kind;
    }//FUNC::END

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBegan_on(long began_on) {
        this.began_on = this.boxUpLong( began_on );
    }//FUNC::END

    public void setEnded_on(long ended_on) {
        this.ended_on = this.boxUpLong( ended_on );
    }//FUNC::END

    public void setAllotted(long allotted) {
        this.allotted = this.boxUpLong( allotted );
    }//FUNC::END
    //END OF SETTERS//SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS

}//CLASS::END
