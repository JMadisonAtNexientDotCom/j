package test.dbDataAbstractions.entities.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import test.config.constants.identifiers.TableNameReg;
import test.config.constants.identifiers.VarNameReg;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import static test.dbDataAbstractions.entities.tables.NinjaTable.NAME_COLUMN;

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
@Entity
@Table(name= KindaTable.TABLE_NAME)  //<--can we replace this with TABLE_NAME ?
public class KindaTable extends BaseEntity {
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME           = TableNameReg.KIND_TABLE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN            = VarNameReg.ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String COMMENT_COLUMN       = VarNameReg.COMMENT;
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
    public String token_id;
    @Transient @JsonIgnore
    public final String TOKEN_ID = TOKEN_ID_COLUMN; //for API access.
    
    /** A foreign key pointing to the [guts/root] of the [test/trial] given. **/
    @Column(name=CHALLENGE_ID_COLUMN)
    public String challenge_id;
    @Transient @JsonIgnore
    public final String CHALLENGE_ID = CHALLENGE_ID_COLUMN; //for API access.
    
    /** A boolean to let us know if CHALLENGE_ID should be a valid value. **/
    @Column(name=HAS_CHALLENGE_COLUMN)
    public String has_challenge;
    @Transient @JsonIgnore
    public final String HAS_CHALLENGE = HAS_CHALLENGE_COLUMN; //for API access.
    
    /** A foreign key pointing to the [guts/root] of the [response/answers] 
     *  [given/submitted] by the ninja for the [test/trial] given. **/
    @Column(name=EFFORT_ID_COLUMN)
    public String effort_id;
    @Transient @JsonIgnore
    public final String EFFORT_ID = EFFORT_ID_COLUMN; //for API access.
    
    /** A boolean to let us know if EFFORT_ID should be a valid value. **/
    @Column(name=HAS_EFFORT_COLUMN)
    public String has_effort;
    @Transient @JsonIgnore
    public final String HAS_EFFORT = HAS_EFFORT_COLUMN; //for API access.
    
    /** Boolean to let us know if this test has been graded. **/
    @Column(name=IS_GRADED_COLUMN)
    public String is_graded;
    @Transient @JsonIgnore
    public final String IS_GRADED = IS_GRADED_COLUMN; //for API access.
    
    /** A normalized grade that is between 0 and 10,000.
     *  In percent, that is 0% to %100.00 with 2 significant digits. **/
    @Column(name=GRADE_10K_COLUMN)
    public String grade_10k;
    @Transient @JsonIgnore
    public final String GRADE_10K = GRADE_10K_COLUMN; //for API access.
    
    
}//CLASS::END
