package app.dbDataAbstractions.entities.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import app.config.constants.identifiers.TableNameReg;
import app.config.constants.identifiers.VarNameReg;
import app.dbDataAbstractions.entities.bases.PurseEntity;
import static app.dbDataAbstractions.entities.tables.KindaTable.KIND_COLUMN;

/**
 *
 * @author jmadison
 */
@Entity
@Table(name= ChalkPurse.TABLE_NAME)
public class ChalkPurse extends PurseEntity {
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME          = TableNameReg.CHALK_PURSE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN           = VarNameReg.ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String RHYME_ID_COLUMN     = VarNameReg.RHYME_ID;
    
    
    /**Think of this as a [rhyme/answer] scribbled in chalk.
     * There can be multiple answers written on a slate. **/
    @Column(name=RHYME_ID_COLUMN)
    public Long rhyme_id; //<--Public is allowed.
    @Transient @JsonIgnore
    public final String RHYME_ID = RHYME_ID_COLUMN; //for API access.
    
}//CLASS::END
