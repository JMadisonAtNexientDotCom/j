package test.dbDataAbstractions.entities.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import test.dbDataAbstractions.entities.bases.BaseEntity;

/**
 * The administrator table. Some operations will require admin access.
 * Like creating new tokens and Ninjas (candidates).
 * @author jmadison :2015.09.20_0627PM
 */
@Entity
@Table(name= AdminTable.TABLE_NAME)
public class AdminTable extends BaseEntity {
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME          = "admin_table";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN           = "id";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String USER_NAME_COLUMN    = "user_name";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String PASS_HASH_COLUMN    = "pass_hash";
    
    /** Unique ID of the admin user. **/
    @Id
    @Column(name=ID_COLUMN)
    private long id;
    
    /** Case-Insensitive user-name. **/
    @Column(name=USER_NAME_COLUMN)
    private String user_name;
    
    /**-------------------------------------------------------------------------
     *  Case-Sensitive hash of user's password.
     *  We store the hash, rather than the actual password,
     *  in case security is compromised and there is a database
     *  dump of the admin_table. 
     ------------------------------------------------------------------------**/
    @Column(name=PASS_HASH_COLUMN)
    private String pass_hash;

    
    //Getters and setters:
    //GSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGS
    /**------------------------------------------------------------------------- 
     *  We only have a getter. NO SETTER for the ID.
     *  Since it is primary key and should not be tampered with.
     * @return :The ID of the [admin/user]:
     ------------------------------------------------------------------------**/
    public long getId() {
        return id;
    }//FUNC::END

    public String getUser_name() {
        return user_name;
    }//FUNC::END

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }//FUNC::END

    public String getPass_hash() {
        return pass_hash;
    }//FUNC::END

    public void setPass_hash(String pass_hash) {
        this.pass_hash = pass_hash;
    }//FUNC::END
    //GSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGS

}//CLASS::END
