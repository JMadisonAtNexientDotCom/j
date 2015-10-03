package test.dbDataAbstractions.entities.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import test.config.constants.identifiers.TableNameReg;
import test.config.constants.identifiers.VarNameReg;
import test.dbDataAbstractions.entities.bases.BaseEntity;


/**
 *
 * @author jmadison
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
    private Long kind; //<--"type" is reserved word in SQL. So used "kind"
    
    /** What is the state of the test?
     *  At time of writing:
     *  1. CREATED_INITIALIZED
     *  2. BEGAN_OPENED
     *  3. ENDED_CLOSED
     *  4. GRADED_FINALIZED **/
    @Column(name=STATUS_COLUMN)
    private Long status; //<--"state" is reserved word in SQL. So used "status"
    
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

    public long getKind() {
        return this.unBoxLong( kind );
    }//FUNC::END

    public long getStatus() {
        return this.unBoxLong( status );
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

    public void setKind(long kind) {
        this.kind = this.boxUpLong( kind );
    }//FUNC::END

    public void setStatus(long status) {
        this.status = this.boxUpLong( status );
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
