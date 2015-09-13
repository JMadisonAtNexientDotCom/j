package test.dbDataAbstractions.entities.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import test.dbDataAbstractions.entities.bases.TextTableBaseEntity;

/**
 * A riddle is a question that must be answered with a rhyme.
 * @author jmadison ON: 2015.09.09_0227PM
 */
@Entity
@Table(name=RiddleTable.TABLE_NAME)
public class RiddleTable extends TextTableBaseEntity {
     /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME             = "riddle_table";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN              = TextTableBaseEntity.ID_COLUMN;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String TEXT_COLUMN            = TextTableBaseEntity.TEXT_COLUMN;
    
    /** An invalid index (hence negative) used to identify error objects
     *  that have been intentionally made by the programmer of this application.
     */
    public static final long ID_MAKE_ERROR_RIDDLE = (-777);
    
    /** 
     *  Makes a RiddleTable instance that is populated with bogus data that
     *  will help us catch errors in the program if we see it show up on 
     *  on the front-end.
     * 
     *  DESIGN NOTE:
     *  Is static method of RiddleTable rather than in the RiddleTransUtil
     *  because it does NOT require us to be in a transaction session. 
     * 
     * @param errorMessage :The error message we want shown on the UI front end.
     *                      Of the application. The error message we want to
     *                      use to populate this RiddleTable instance.
     * @return : A RiddleTable populated with bogus data and an error message. 
                                                                             **/
    public static RiddleTable makeErrorRiddle(String errorMessage){
        RiddleTable op = new RiddleTable();
        op.setId(ID_MAKE_ERROR_RIDDLE); //set id to invalid value.
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
