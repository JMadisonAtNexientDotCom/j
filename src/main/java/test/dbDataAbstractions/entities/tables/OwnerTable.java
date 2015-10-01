package test.dbDataAbstractions.entities.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import test.config.constants.identifiers.TableNameReg;
import test.config.constants.identifiers.VarNameReg;
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
    public static final String TABLE_NAME        = TableNameReg.OWNER_TABLE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String TOKEN_ID_COLUMN   = VarNameReg.TOKEN_ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String NINJA_ID_COLUMN   = VarNameReg.NINJA_ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ADMIN_ID_COLUMN   = VarNameReg.ADMIN_ID;
   
    /** Column name stored as string constant for easy refactoring. **/
    public static final String COMMENT_COLUMN    = VarNameReg.COMMENT;
    
    //BUG_FIX: Annotation already exists in base class...Wait... No...
    //Then again... maybe @Id should be removed? Maybe... You don't know
    //how to use foriegn keys??
    /** Primary foreign key **/
    ///@Id <---Remove this until you know how to properly do foriegn keys.
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
    
    //DELE, now in base entity.
    //@Column(name=COMMENT_COLUMN)
    //private String comment;

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
    
    //DELE, now in base entity.
    //xx public String getComment() {
    //xx     return comment;
    //xx }

    //xx public void setComment(String comment) {
    //xx     this.comment = comment;
    //xx }
    //GSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGS
    
     
}//CLASS::END
