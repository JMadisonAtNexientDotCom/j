package test.dbDataAbstractions.entities.tables;
//345678901234567890123456789012345678901234567890123456789012345678901234567890

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import test.config.constants.identifiers.TableNameReg;
import test.config.constants.identifiers.VarNameReg;
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
    public static final String TABLE_NAME        = TableNameReg.SESSION_TABLE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN         = VarNameReg.ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String TOKEN_ID_COLUMN   = VarNameReg.TOKEN_ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String OPENED_ON_COLUMN  = VarNameReg.OPENED_ON;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String DURATION_COLUMN   = VarNameReg.DURATION;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String IS_ACTIVE_COLUMN  = VarNameReg.IS_ACTIVE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String COMMENT_COLUMN    = VarNameReg.COMMENT;
    
    //DELETE:
    //part of base entity now.
    //@Id
    //@Column(name=ID_COLUMN)
    //private long id;
    
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
    private boolean is_active;
    
    //DELE, part of base entity now.
    //@Column(name=COMMENT_COLUMN)
    //private String comment;

    //Boiler-plate auto-generated getters+setters for all columns:
    //GSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGS
    
    //Delete. Part of base entity now.
    //xx public long getId() {
    //xx     return id;
    //xx }//FUNC::END
    //xx 
    //xx public void setId(long id) {
    //xx     this.id = id;
    //xx }//FUNC::END

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

    public boolean getIs_active() {
        return is_active;
    }//FUNC::END

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }//FUNC::END

    //Delete: Part of base entity now.
    //xx public String getComment() {
    //xx     return comment;
    //xx }//FUNC::END
    //xx 
    //xx public void setComment(String comment) {
    //xx     this.comment = comment;
    //xx }//FUNC::END
    //GSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGS
    
    
}//CLASS::END
