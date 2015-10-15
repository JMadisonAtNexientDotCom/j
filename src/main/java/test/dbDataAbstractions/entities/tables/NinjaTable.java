package test.dbDataAbstractions.entities.tables;
import com.fasterxml.jackson.annotation.JsonIgnore;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import test.config.constants.identifiers.TableNameReg;
import test.config.constants.identifiers.VarNameReg;

/**-----------------------------------------------------------------------------
 * Represents the ninja(s) that are registered in the system.
 * In order to get hired (brought into the clan) the ninja must pass
 * a trial (test) administered by a sensei (teacher/recruiter) 
 * @author jmadison on 2015.09.04_0507PM-------------------------------------**/
@Entity
@Table(name= NinjaTable.TABLE_NAME)  //<--can we replace this with TABLE_NAME ?
public class NinjaTable extends BaseEntity {
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME           = TableNameReg.NINJA_TABLE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN            = VarNameReg.ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String NAME_COLUMN          = VarNameReg.NAME;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String PHONE_COLUMN         = VarNameReg.PHONE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String EMAIL_COLUMN         = VarNameReg.EMAIL;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String PORTFOLIO_URL_COLUMN = VarNameReg.PORTFOLIO_URL;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String COMMENT_COLUMN       = VarNameReg.COMMENT;
    
    
    //DELE, now in base entity.
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
  /** named "idOfNinja" instead of simply "id" because I am trying to
   *  track down a bug that seems to be involved with the name "id"
   *  colliding in my token table and ninja table. 
   * 
   *  Called "idOfNinja" rather than "ninjaID" because postfixes are
   *  more friendly with intellisense than prefixes.
   */
  //@Id
  //@GeneratedValue(strategy = GenerationType.IDENTITY)
  //@Column(name=ID_COLUMN)
  //private long id;
     //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
  
  @Column(name=NAME_COLUMN)
  private String name;
  @Transient
  @JsonIgnore
  public final String NAME = NAME_COLUMN; //for API access.
  
  //If you do HTTP request to set a phone number that is too long.
  //The response will turn into a 404. Made long instead of int for this reason.
  @Column(name=PHONE_COLUMN)
  private Long  phone;
  @Transient
  @JsonIgnore
  public final String PHONE = PHONE_COLUMN; //for API access
  
  @Column(name=EMAIL_COLUMN)
  private String email;
  @Transient
  @JsonIgnore
  public final String EMAIL = EMAIL_COLUMN; //for API access
  
  @Column(name=PORTFOLIO_URL_COLUMN)
  private String portfolio_url;
  @Transient
  @JsonIgnore
  public final String PORTFOLIO_URL = PORTFOLIO_URL_COLUMN; //for API access
  
  //DELE, now in base entity.
  //xx @Column(name=COMMENT_COLUMN)
  //xx private String comment;
  

  //Getters and setters:
  //GSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGS
  
  //DELE: now in base entity.
  //xx public Long getIdOfNinja(){ return id;}
  //xx public void setIdOfNinja(Long idOfNinja){ this.id = idOfNinja;}
  
  public String getName(){ return name;}
  public void setName(String name){ this.name = name;}
  
  public long getPhone()
  { 
      return this.unBoxLong(phone);
  }
  public void setPhone(long phone){ 
      this.phone = new Long(phone);
  }
  
  public String getEmail(){ return email;}
  public void setEmail(String email){ this.email = email;}
  
  public String getPortfolio_url(){ return portfolio_url;}
  public void setPortfolio_url(String portfolio_url)
  { this.portfolio_url = portfolio_url;}
  
  //DELE: Now in base entity.
  //xx public String getComment(){ return comment;}
  //xx public void setComment(String comment){ this.comment = comment;}
  //GSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGS
    
}//CLASS::END
