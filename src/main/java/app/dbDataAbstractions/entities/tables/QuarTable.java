package app.dbDataAbstractions.entities.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import app.config.constants.identifiers.TableNameReg;
import app.config.constants.identifiers.VarNameReg;
import app.dbDataAbstractions.entities.bases.BaseEntity;
import static app.dbDataAbstractions.entities.tables.KindaTable.KIND_COLUMN;

/**
 *
 * @author jmadison
 */
@Entity
@Table(name= QuarTable.TABLE_NAME)
public class QuarTable extends BaseEntity {
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME          = TableNameReg.QUAR_TABLE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN           = VarNameReg.ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String QUAR_GI_COLUMN      = VarNameReg.QUAR_GI;
    
    /** Identifies a group of slates within quar purse that make up
     *  the composition of a single QUAR object. **/
    @Column(name=QUAR_GI_COLUMN)
    public Long quar_gi; //<--Public Allowed
    @Transient @JsonIgnore
    public final String QUAR_GI = QUAR_GI_COLUMN; //for API access.
    
}//CLASS::END
