package test.dbDataAbstractions.entities.tables;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import test.config.constants.identifiers.TableNameReg;
import test.config.constants.identifiers.VarNameReg;
import test.dbDataAbstractions.entities.bases.BaseEntity;

/**
 * Note: The StackTable itself is NOT a StackEntity. StackEntities refer
 * to collections that make use of the ids within the StackTable.
 * 
 * Original usage:
 * Enabling dynamic [test/trial] creation 5 cuecards at a time.
 * 
 * @author jmadison:2015.11.02_0605PM (November,2nd,Year2015.6:05PM)
 */
@Entity
@Table(name =StackTable.TABLE_NAME)
public class StackTable extends BaseEntity{
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME           = TableNameReg.STACK_TABLE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN            = VarNameReg.ID;
    
    //Values referencing columns used for making criteria searches:
    public static final String CHECKSUM_COLUMN = VarNameReg.CHECKSUM;
    
    @Basic(fetch=FetchType.EAGER) //<--attempt to make group_id not null 
    @Column(name = CHECKSUM_COLUMN)  //when getting it from database.
    public long checksum; //<--change to value-type.
    
}//CLASS::END
