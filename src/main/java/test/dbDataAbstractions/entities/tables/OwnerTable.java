package test.dbDataAbstractions.entities.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import test.dbDataAbstractions.entities.bases.BaseEntity;

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
public class OwnerTable extends BaseEntity{
    
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
    
    @Column(name=COMMENT_COLUMN)
    private String comment;

    //Auto-Generated getters + setters:
    //GSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGS
    public long getToken_id() {
        return token_id;
    }

    public void setToken_id(long token_id) {
        this.token_id = token_id;
    }

    public long getNinja_id() {
        return ninja_id;
    }

    public void setNinja_id(long ninja_id) {
        this.ninja_id = ninja_id;
    }

    public long getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(long admin_id) {
        this.admin_id = admin_id;
    }
    
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    //GSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGS
    
     
}//CLASS::END
