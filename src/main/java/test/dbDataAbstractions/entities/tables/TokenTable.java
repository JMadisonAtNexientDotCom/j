package test.dbDataAbstractions.entities.tables;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import test.config.constants.identifiers.TableNameReg;
import test.config.constants.identifiers.VarNameReg;
/**-----------------------------------------------------------------------------
 * Table that stores the tokens.
 * Each token has a comment that is directly associated with it.
 * It is for debugging purposes ONLY. And not to be used in actual logic.
 * Though will also be printed out on our /tokenLister page.
 * @author JMadison 
 *
 * EDIT HISTORY:
 * JMadison_2015.09.03_0916AM: Refactor: hash-->token, added id field.
 *
 ----------------------------------------------------------------------------**/
@Entity
@Table(name=TokenTable.TABLE_NAME)  //<--can we replace this with TABLE_NAME ?
public class TokenTable extends BaseEntity{
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME        = TableNameReg.TOKEN_TABLE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN         = VarNameReg.ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String TOKEN_HASH_COLUMN = VarNameReg.TOKEN_HASH;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String COMMENT_COLUMN    = VarNameReg.COMMENT;
    
   /** A sequential integer used to identify the token.                      ***
   *** Internally, we should be using this id value rather than the token.   ***
   *** The token ~meerly~ exists as an ~encripted~ representation of the ID. ***
   *** So that people can't just be all like "TOKEN + 1" and access someone  ***
   *** else's data over public HTTP.                                         **/
    
    //REPLACED BY BASE_ENTITY!
 // @Id
 // @GeneratedValue(strategy = GenerationType.IDENTITY)
  //@Column(name=ID_COLUMN)
  //private long id;
   
    
   /**  The TOKEN the outside world sees.                                    ***
   ***  An ENCRYPTED version of the Id. Encrypted so that there is no        ***
   ***  chance of token collision if all Ids in the table are unique.        **/
   //@Id //<--TEMP ID till we fix bugs.
   @Column(name=TOKEN_HASH_COLUMN)
    private String token_hash;
    
    //NOW IN BASE ENTITY.
    /** A comment attached to this token entry in the token table            ***
    *** Used for debugging. Planned as ~permenant~ installment in this       ***
    *** architecture. ~Huntch~ that the maintainability gained by this       ***
    *** addition will trump the slight performance hit on the database.      **/
    //@Column(name=COMMENT_COLUMN)
    //private String comment;
    
    //now exists in base entity.
    //Is this getter+setter necessary?
    //public Long getId(){ return id;}
    //public void setId(Long id){ this.id = id;}
    
    public String getTokenHash(){ return token_hash;}
    public void setTokenHash(String value){ this.token_hash = value;}
    
    //public String getComment(){ return comment;}
    //public void setComment(String comment){ this.comment = comment;}
    
}//CLASS::END
