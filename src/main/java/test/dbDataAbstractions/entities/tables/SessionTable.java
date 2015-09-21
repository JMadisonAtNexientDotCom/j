package test.dbDataAbstractions.entities.tables;
//345678901234567890123456789012345678901234567890123456789012345678901234567890

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import test.dbDataAbstractions.entities.bases.BaseEntity;

/**##########################CLASS HEADER FILE##################################
//WHAT THIS CLASS DOES:
//An entity representation of the session_table.
//session_table keeps tracked of who is "logged in".
//being logged-in works by:
//1. Being a user that has access to a token that exists in both
//   the token_table and the session_table.
//
//ORIGINAL USE CASE:
//Preparation for basic login API calls:
//1. LoginAndGetTokenForSelf(userName,passWord)
//2. OpenToken(token, adminSessionToken)
//3. CloseToken(token, adminSessionToken)
//
//DESIGN NOTE (Justifications for why things are the way they are):
//    Use of milliseconds for opened_on and duration columns:
//        Standardizing time information as millisecond timestamp will make it
//        easier to work with. Date logic is weird and a pain. If we 
//        need to display the actual date later we can have a UI that  
//        converts the milliseconds to an actual date. But the priority  
//        is focused on the functionality we need to get session logic working.
//
//@author JMadison : 20XX.XX.XX_####AMPM
//@author XXXXXXXX : 20XX.XX.XX_####AMPM
//@author XXXXXXXX : 20XX.XX.XX_####AMPM 
//@author XXXXXXXX : 20XX.XX.XX_####AMPM
########10########20########30########40########50########60########70########*/
//-------0---------0---------0---------0---------0---------0---------0---------0
@Entity
@Table(name=SessionTable.TABLE_NAME)
public class SessionTable extends BaseEntity {
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME        = "session_table";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN         = "id";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String TOKEN_ID_COLUMN   = "token_id";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String OPENED_ON_COLUMN  = "opened_on";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String DURATION_COLUMN   = "duration";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String IS_ACTIVE_COLUMN  = "is_active";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String COMMENT_COLUMN    = "comment";
    
    @Id
    @Column(name=ID_COLUMN)
    private long id;
    
    /**-------------------------------------------------------------------------
     * Though this is a FORIEGN KEY taken from token_table,
     * I don't have enough experience using hibernate to be
     * certain of it's behavior. Going to keep things simple
     * and manage relationships manually with my transaction
     * utilities. For now I am mainly using hibernate to handle
     * writing SQL statements for me.
     ------------------------------------------------------------------------**/
    @Column(name=TOKEN_ID_COLUMN)
    private long token_id;
    
    @Column(name=OPENED_ON_COLUMN)
    private long opened_on;
    
    @Column(name=DURATION_COLUMN)
    private long duration;
    
    @Column(name=IS_ACTIVE_COLUMN)
    private long is_active;
    
    @Column(name=COMMENT_COLUMN)
    private String comment;

    //Boiler-plate auto-generated getters+setters for all columns:
    //GSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGS
    public long getId() {
        return id;
    }//FUNC::END

    public void setId(long id) {
        this.id = id;
    }//FUNC::END

    public long getToken_id() {
        return token_id;
    }//FUNC::END

    public void setToken_id(long token_id) {
        this.token_id = token_id;
    }//FUNC::END

    public long getOpened_on() {
        return opened_on;
    }//FUNC::END

    public void setOpened_on(long opened_on) {
        this.opened_on = opened_on;
    }//FUNC::END

    public long getDuration() {
        return duration;
    }//FUNC::END

    public void setDuration(long duration) {
        this.duration = duration;
    }//FUNC::END

    public long getIs_active() {
        return is_active;
    }//FUNC::END

    public void setIs_active(long is_active) {
        this.is_active = is_active;
    }//FUNC::END

    public String getComment() {
        return comment;
    }//FUNC::END

    public void setComment(String comment) {
        this.comment = comment;
    }//FUNC::END
    //GSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGS
    
    
}//CLASS::END
