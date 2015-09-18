package test.dbDataAbstractions.entities.tables;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * Table that stores the tokens.
 * Each token has a comment that is directly associated with it.
 * It is for debugging purposes ONLY. And not to be used in actual logic.
 * Though will also be printed out on our /tokenLister page.
 * @author JMadison 
 *
 * EDIT HISTORY:
 * JMadison_2015.09.03_0916AM: Refactor: hash-->token, added id field.
 *
 **/
@Entity
@Table(name="token_table")  //<--can we replace this with TABLE_NAME ?
public class TokenTable extends BaseEntity{
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME     = "token_table";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN      = "ID";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String TOKEN_COLUMN   = "token";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String COMMENT_COLUMN = "comment";
    
   /** A sequential integer used to identify the token.                      ***
   *** Internally, we should be using this id value rather than the token.   ***
   *** The token ~meerly~ exists as an ~encripted~ representation of the ID. ***
   *** So that people can't just be all like "TOKEN + 1" and access someone  ***
   *** else's data over public HTTP.                                         **/
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name=ID_COLUMN)
  private long ID;
   
    
   /**  The TOKEN the outside world sees.                                    ***
   ***  An ENCRYPTED version of the Id. Encrypted so that there is no        ***
   ***  chance of token collision if all Ids in the table are unique.        **/
   //@Id //<--TEMP ID till we fix bugs.
   @Column(name=TOKEN_COLUMN)
    private String token;
    
    /** A comment attached to this token entry in the token table            ***
    *** Used for debugging. Planned as ~permenant~ installment in this       ***
    *** architecture. ~Huntch~ that the maintainability gained by this       ***
    *** addition will trump the slight performance hit on the database.      **/
    @Column(name=COMMENT_COLUMN)
    private String comment;
    
    //Is this getter+setter necessary?
    public Long getId(){ return ID;}
    public void setId(Long id){ this.ID = id;}
    
    public String getToken(){ return token;}
    public void setToken(String value){ this.token = value;}
    
    public String getComment(){ return comment;}
    public void setComment(String comment){ this.comment = comment;}
    
}//CLASS::END
