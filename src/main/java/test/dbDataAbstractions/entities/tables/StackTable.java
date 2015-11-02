package test.dbDataAbstractions.entities.tables;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
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
public class StackTable extends BaseEntity{
    
    //Values referencing columns used for making criteria searches:
    public static final String CHECKSUM_COLUMN = VarNameReg.CHECKSUM;
    
    @Basic(fetch=FetchType.EAGER) //<--attempt to make group_id not null 
    @Column(name = CHECKSUM_COLUMN)  //when getting it from database.
    public long checksum; //<--change to value-type.
    
}//CLASS::END
