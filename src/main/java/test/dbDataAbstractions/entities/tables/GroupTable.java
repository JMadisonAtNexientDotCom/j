package test.dbDataAbstractions.entities.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import test.config.constants.identifiers.TableNameReg;
import test.config.constants.identifiers.VarNameReg;
import test.dbDataAbstractions.entities.bases.BaseEntity;

/**
 * A master table of group ids.
 * Used so that we can group items within the database together.
 * @author jmadison :2015.10.19
 */
@Entity
@Table(name= GroupTable.TABLE_NAME)  //<--can we replace this with TABLE_NAME ?
public class GroupTable extends BaseEntity{
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME           = TableNameReg.GROUP_TABLE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN            = VarNameReg.ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String CHECKSUM_COLUMN      = VarNameReg.CHECKSUM;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String NAME_COLUMN          = VarNameReg.NAME;
    
    /** A checksum for how many entries should belong to this group. **/
    @Column(name=CHECKSUM_COLUMN) public Long checksum;
    
    /** The name of the group. By convention, this should be the table
     *  the group is from. However, it might just end up being used for
     *  debugging. **/
    @Column(name=NAME_COLUMN) public String name;
    
    
    /**Makes a new entity configured as an error response.
     * @return :See above. **/
    public static GroupTable makeErrorTable(){
        GroupTable op = new GroupTable();
        op.checksum = new Long(-777);
        op.name     = "[GROUPTABLE::ERROR_GROUP::]";
        op.setComment("[Touched by GroupTable.makeErrorTable]");
        return op;
    }//FUNC::END
    
}//CLASS::END
