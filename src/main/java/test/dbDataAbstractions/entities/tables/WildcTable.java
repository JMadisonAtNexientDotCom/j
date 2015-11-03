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
 *
 * The WildcTable is used to give an easy-to-use single-id,
 * (the wildc_si) to the entries within the wildc_stack.
 * 
 * This allows us to refer to a [collection/stack] of records by a single
 * primary-key id, rather than referring to it by a group_id.
 * 
 * @author jmadison:2015.11.02_0605PM (November,2nd,Year2015.6:05PM)
 */
@Entity
@Table(name =WildcTable.TABLE_NAME)
public class WildcTable extends BaseEntity {
    
     /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME           = TableNameReg.WILDC_TABLE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN            = VarNameReg.ID;
    
    //Values referencing columns used for making criteria searches:
    public static final String WILDC_SI_COLUMN = VarNameReg.WILDC_SI;
    
    @Basic(fetch=FetchType.EAGER) //<--attempt to make not null 
    @Column(name = WILDC_SI_COLUMN)  //when getting it from database.
    public long wildc_si; //<--change to value-type.
    
}//CLASS::END
