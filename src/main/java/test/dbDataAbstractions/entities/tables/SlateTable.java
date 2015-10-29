package test.dbDataAbstractions.entities.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import test.config.constants.identifiers.TableNameReg;
import test.config.constants.identifiers.VarNameReg;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import static test.dbDataAbstractions.entities.tables.KindaTable.KIND_COLUMN;

/**
 *
 * @author jmadison
 */
@Entity
@Table(name= SlateTable.TABLE_NAME)
public class SlateTable extends BaseEntity {
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME          = TableNameReg.SLATE_TABLE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN           = VarNameReg.ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String STATUS_COLUMN       = VarNameReg.STATUS;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String CUECARD_ID_COLUMN   = VarNameReg.CUECARD_ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String CHALK_ID_COLUMN     = VarNameReg.CHALK_ID;
    
    /** Because you can select for questions to:
     *  1. NOT ANSWERED: Because I don't think there is an answer.
     *  2. NOT ANSWERED: Because I skipped something I don't know.
     *  3. ANSWERED.
     *  CHALK_ID_COLUMN is only relevant if status is ANSWERED. **/
    @Column(name=STATUS_COLUMN)
    public String status; //<--Can be public
    @Transient @JsonIgnore
    public final String STATUS = STATUS_COLUMN; //for API access.
    
    /** The ID of the original cuecard that had the question. It's like taking
     *  a pop quiz in class where you write the question number and the full
     *  question on the paper before answering it.
     * 
     *  Also, since we don't care about order of questions, we need this
     *  information in order to:
     *  1. Know what this object is responding to.
     *  2. Be able to grade this response. **/
    @Column(name=CUECARD_ID_COLUMN)
    public Long cuecard_id; //<--Can be public
    @Transient @JsonIgnore
    public final String CUECARD_ID = CUECARD_ID_COLUMN; //for API access.
    
    /**
     * The actual response the ninja wrote on the slate.
     * It is highly likely that multiple people will formulate the exact
     * same responses. Thus this data should be persisted as well.
     * Could also be interesting for data mining. **/
    @Column(name=CHALK_ID_COLUMN)
    public Long chalk_id; //<--Can be public
    @Transient @JsonIgnore
    public final String CHALK_ID = CHALK_ID_COLUMN; //for API access.
    
}//CLASS::END
