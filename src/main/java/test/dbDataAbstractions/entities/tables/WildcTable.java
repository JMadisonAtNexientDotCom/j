package test.dbDataAbstractions.entities.tables;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
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
public class WildcTable extends BaseEntity {
    
    //Values referencing columns used for making criteria searches:
    public static final String WILDC_SI_COLUMN = VarNameReg.WILDC_SI;
    
    @Basic(fetch=FetchType.EAGER) //<--attempt to make not null 
    @Column(name = WILDC_SI_COLUMN)  //when getting it from database.
    public long wildc_si; //<--change to value-type.
    
}//CLASS::END
