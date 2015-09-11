package test.entities.tables;
import test.entities.bases.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Represents the ninja(s) that are registered in the system.
 * In order to get hired (brought into the clan) the ninja must pass
 * a trial (test) administered by a sensei (teacher/recruiter) 
 * @author jmadison on 2015.09.04_0507PM                          **/
@Entity
@Table(name= NinjaTable.TABLE_NAME)  //<--can we replace this with TABLE_NAME ?
public class NinjaTable extends BaseEntity {
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME             = "ninja_table";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String COLUMN_ID              = "id";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String COLUMN_NAME            = "name";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String COLUMN_PHONE           = "phone";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String COLUMN_EMAIL           = "email";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String COLUMN_PORTFOLIO_URL   = "portfolio_url";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String COLUMN_COMMENT         = "comment";
    
    
  /** named "idOfNinja" instead of simply "id" because I am trying to
   *  track down a bug that seems to be involved with the name "id"
   *  colliding in my token table and ninja table. 
   * 
   *  Called "idOfNinja" rather than "ninjaID" because postfixes are
   *  more friendly with intellisense than prefixes.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name=COLUMN_ID)
  private long idOfNinja;
  
  @Column(name=COLUMN_NAME)
  private String name;
  
  //If you do HTTP request to set a phone number that is too long.
  //The response will turn into a 404. Made long instead of int for this reason.
  @Column(name=COLUMN_PHONE)
  private long phone;
  
  @Column(name=COLUMN_EMAIL)
  private String email;
  
  @Column(name=COLUMN_PORTFOLIO_URL)
  private String portfolio_url;
  
  @Column(name=COLUMN_COMMENT)
  private String comment;
  

  //Getters and setters:
  //GSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGS
  public Long getIdOfNinja(){ return idOfNinja;}
  public void setIdOfNinja(Long idOfNinja){ this.idOfNinja = idOfNinja;}
  
  public String getName(){ return name;}
  public void setName(String name){ this.name = name;}
  
  public long getPhone(){ return phone;}
  public void setPhone(long phone){ this.phone = phone;}
  
  public String getEmail(){ return email;}
  public void setEmail(String email){ this.email = email;}
  
  public String getPortfolioURL(){ return portfolio_url;}
  public void setPortfolioURL(String portfolio_url)
  { this.portfolio_url = portfolio_url;}
  
  public String getComment(){ return comment;}
  public void setComment(String comment){ this.comment = comment;}
  //GSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGSGS
    
}//CLASS::END
