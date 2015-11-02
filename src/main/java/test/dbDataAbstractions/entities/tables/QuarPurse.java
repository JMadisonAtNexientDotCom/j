package test.dbDataAbstractions.entities.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import test.config.constants.identifiers.TableNameReg;
import test.config.constants.identifiers.VarNameReg;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import test.dbDataAbstractions.entities.bases.PurseEntity;
import static test.dbDataAbstractions.entities.tables.KindaTable.KIND_COLUMN;

/**
 *
 * @author jmadison
 */
@Entity
@Table(name= QuarPurse.TABLE_NAME)
public class QuarPurse extends PurseEntity {
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME          = TableNameReg.QUAR_PURSE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN           = VarNameReg.ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String SLATE_ID_COLUMN     = VarNameReg.SLATE_ID;
    
    /** One of the ninja's answer slates. A collection of answer slates
     *  makes a QUAR. Which is an answer to a riddle-trial. **/
    @Column(name=SLATE_ID_COLUMN)
    public Long slate_id; //<--Public allowed.
    @Transient @JsonIgnore
    public final String SLATE_ID = SLATE_ID_COLUMN; //for API access.
    
}//CLASS::END
