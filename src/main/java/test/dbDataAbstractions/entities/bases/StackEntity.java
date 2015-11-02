package test.dbDataAbstractions.entities.bases;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import test.config.constants.identifiers.VarNameReg;


/**
 * A stack entity is like a purse entity.
 * Except, the groups represented by the stack have a LOCUS.
 * The locus gives the items of the collection in the database tables
 * an order. 
 * 
 * Original useage:
 * Making it possible to build [trial/test] 5 questions at a time dynamically.
 * 
 * @author jmadison:2015.11.02_0605PM (November,2nd,Year2015.6:05PM)
 */
public class StackEntity extends BaseEntity{
   
    //Values referencing columns used for making criteria searches:
    public static final String STACK_ID_COLUMN = VarNameReg.STACK_ID;
    public static final String LOCUS_COLUMN    = VarNameReg.LOCUS;
    
    @Basic(fetch=FetchType.EAGER) //<--attempt to make not null 
    @Column(name = STACK_ID_COLUMN)  //when getting it from database.
    public long stack_id; //<--change to value-type.
    
    @Basic(fetch=FetchType.EAGER) //<--attempt to make group_id not null 
    @Column(name = LOCUS_COLUMN)  //when getting it from database.
    public long locus; //<--change to value-type.
    
}//CLASS::END
