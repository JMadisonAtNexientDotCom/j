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
 * The ChalkTable is a helper table that helps give us one ID to manage
 * a collection of records existing in the ChalkPurse.
 * @author jmadison :2015.10.27(Oct27th,Year2015,TUESDAY.7:32PM) **/
@Entity
@Table(name= ChalkTable.TABLE_NAME)
public class ChalkTable extends BaseEntity {
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME          = TableNameReg.CHALK_TABLE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN           = VarNameReg.ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String CHALK_GI_COLUMN     = VarNameReg.CHALK_GI;
    
    /** The group of records that this record is referencing. **/
    @Column(name=CHALK_GI_COLUMN)
    public String chalk_gi; //<--NOTE:#1
    @Transient @JsonIgnore
    public final String CHALK_GI = CHALK_GI_COLUMN; //for API access.
    
}//CLASS::END
