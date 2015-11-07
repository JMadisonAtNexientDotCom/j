package test.dbDataAbstractions.entities.tables;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import test.config.constants.identifiers.TableNameReg;
import test.config.constants.identifiers.VarNameReg;
import test.dbDataAbstractions.entities.bases.PurseEntity;

/**
 * Bundles groups of "foreign_record_id" into clusters.
 * What table these foreign_record_id(s) are referencing depends on
 * what table on the TOP OF THE HEIRARCHY owns the entry.
 * 
 * For example: 
 * //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE//
 * If the twine_table owns a wildc_table record that owns
 * some wildc_stack records, which in turn own wildc_purse records...
 * 
 * The foreign_record_id column would mean "cuecard_id" since the twine_table
 * is used to bind cuecards within a deck into smaller ordered subsets.
 * //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE//
 * 
 * @author jmadison:2015.11.02_0605PM (November,2nd,Year2015.6:05PM)
 */
@Entity
@Table(name =WildcPurse.TABLE_NAME)
public class WildcPurse extends PurseEntity{
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME           = TableNameReg.WILDC_PURSE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN            = VarNameReg.ID;
    
    //Values referencing columns used for making criteria searches:
    public static final String FOREIGN_RECORD_ID_COLUMN = VarNameReg.FOREIGN_RECORD_ID;
    
    @Basic(fetch=FetchType.EAGER) //<--attempt to make group_id not null 
    @Column(name = FOREIGN_RECORD_ID_COLUMN)  //when getting it from database.
    public long foreign_record_id; //<--change to value-type.
    
}//CLASS::END