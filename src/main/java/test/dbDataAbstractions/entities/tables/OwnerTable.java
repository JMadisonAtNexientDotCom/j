package test.dbDataAbstractions.entities.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**-----------------------------------------------------------------------------
 * The owner_table is a database table that keeps track of which users
 * own which tokens.
 * 
 * If you OWN a token, and that token is ACTIVE in the session_table,
 * then you are logged in.
 * 
 * @author jmadison
 ----------------------------------------------------------------------------**/
@Entity
@Table(name= OwnerTable.TABLE_NAME)
public class OwnerTable {
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME        = "owner_table";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String TOKEN_ID_COLUMN   = "token_id";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String NINJA_ID_COLUMN   = "ninja_id";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ADMIN_ID_COLUMN   = "admin_id";
   
    /** Column name stored as string constant for easy refactoring. **/
    public static final String COMMENT_COLUMN    = "comment";
    
    /** Primary foreign key **/
    @Id
    @Column(name=TOKEN_ID_COLUMN)
    private long token_id;
    
    /** token can either be associated with
     *  ninja_id or admin_id, the other column
     *  should be negative 1 for invalid ID **/
    @Column(name=NINJA_ID_COLUMN)
    private long ninja_id;
    
    
    /** token can either be associated with
     *  ninja_id or admin_id, the other column
     *  should be negative 1 for invalid ID **/
    @Column(name=ADMIN_ID_COLUMN)
    private long admin_id;
    
    
    
    
}//CLASS::END
