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
 * Twine table is used to "CAST" a collection of entries from the
 * wildc_table so that we know what the foreign_record_id values referenced
 * in the wildc_purse refer to.
 * 
 * Entries found in twine_table are specifically for binding cuecards
 * together into ordered groups. I was thinking to call it rubberband_table.
 * But I wanted to keep with  midevil theme. And rubberbands did not exist
 * back in the times of ninjas & jesters. But twine did.
 * So imagine the twine as something that bundles a sub-set of cards within
 * the deck.
 * 
 * @author jmadison:2015.11.02_0605PM (November,2nd,Year2015.6:05PM)
 */
@Entity
@Table(name =TwineTable.TABLE_NAME)
public class TwineTable extends BaseEntity{
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME           = TableNameReg.TWINE_TABLE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN            = VarNameReg.ID;
    
    //Values referencing columns used for making criteria searches:
    public static final String WILDC_ID_COLUMN = VarNameReg.WILDC_ID;
    public static final String TOKEN_ID_COLUMN = VarNameReg.TOKEN_ID;
    
    @Basic(fetch=FetchType.EAGER) //<--attempt to make not null 
    @Column(name = WILDC_ID_COLUMN)  //when getting it from database.
    public long wildc_id; //<--change to value-type.
    
    @Basic(fetch=FetchType.EAGER) //<--attempt to make not null 
    @Column(name = TOKEN_ID_COLUMN)  //when getting it from database.
    public long token_id; //<--change to value-type.
    
}//CLASS::END
