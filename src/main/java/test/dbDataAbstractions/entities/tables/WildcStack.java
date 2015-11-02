package test.dbDataAbstractions.entities.tables;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import test.config.constants.identifiers.VarNameReg;
import test.dbDataAbstractions.entities.bases.StackEntity;

/**
 * 
 * The WildcStack is responsible for grouping clusters of records
 * in an ORDERED manner. The stack_id is similar to the group_id used
 * in purseTable entities. Except that when you have a record with a stack_id,
 * it must also have a "locus" so we know the order of the record within
 * the stack collection.
 * 
 * NOTE: stack_id and locus_id inherited from StackEntity class.
 * @author jmadison:2015.11.02_0605PM (November,2nd,Year2015.6:05PM)
 */
public class WildcStack extends StackEntity{
    
    //Values referencing columns used for making criteria searches:
    public static final String WILDC_GI_COLUMN = VarNameReg.WILDC_GI;
    
    @Basic(fetch=FetchType.EAGER) //<--attempt to make group_id not null 
    @Column(name = WILDC_GI_COLUMN)  //when getting it from database.
    public long wildc_gi; //<--change to value-type.
    
}//CLASS::END
