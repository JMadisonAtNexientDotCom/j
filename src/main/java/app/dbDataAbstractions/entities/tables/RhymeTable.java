package app.dbDataAbstractions.entities.tables;

import javax.persistence.Entity;
import javax.persistence.Table;
import app.config.constants.identifiers.TableNameReg;
import app.dbDataAbstractions.entities.bases.TextTableBaseEntity;

/**
 * A "Rhyme" is an ANSWER to a Riddle.
 * @author jmadison ON: 2015.09.09_0227PM
 */
@Entity
@Table(name = RhymeTable.TABLE_NAME)
public class RhymeTable extends TextTableBaseEntity {
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME     = TableNameReg.RHYME_TABLE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN      = TextTableBaseEntity.ID_COLUMN;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String TEXT_COLUMN    = TextTableBaseEntity.TEXT_COLUMN;
    
    /** Index/ID to be used for when when we intentionally make a RhymeTable
     *  with bogus info. Negative so that it is an invalid database index.    */
    public static final long ID_MAKE_ERROR_RHYME = (-888);
    
    /** Used to create error RhymeTable that will be sent back to FRONT-END
     *  when the front-end makes a IMPROPERLY-FORMATTED http request.
     *  If the FRONT ENDS fault: We send back error objects.
     *  If BACK ENDS fault     : We throw uncaught exceptions into the backend.
     * @param errorMessage     : The error message we want displayed in 
     *                           front-end.
     * @return                 : RhymeTable object configured to display error 
     *                           message.                                    **/
    public static RhymeTable makeErrorRhyme(String errorMessage){
        RhymeTable op = new RhymeTable();
        op.setId(ID_MAKE_ERROR_RHYME);
        op.setText(errorMessage);
        return op;
    }//FUNC::END
    
  /*
  DELETE THIS CODE ONCE WE ARE CERTAIN BASE CLASS WORKS!
  //shared code between riddle+rhyme tables, can we make base class?
  ////////////////////////////////////////////////////////
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name=ID_COLUMN)
  private long id;
  
  @Column(name=TEXT_COLUMN)
  private String text;
    
  public Long getId(){ return id;}
  public void setId(Long id){ this.id = id;}
  
  public String getText(){ return text;}
  public void setText(String text){ this.text = text;}
  /////////////////////////////////////////////////////////
    */
    
}//CLASS::END
