package test.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import test.entities.TextTableBaseEntity;

/**
 * A "Rhyme" is an ANSWER to a Riddle.
 * @author jmadison ON: 2015.09.09_0227PM
 */
@Entity
@Table(name = RhymeTable.TABLE_NAME)
public class RhymeTable extends TextTableBaseEntity {
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME             = "rhyme_table";
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN              = TextTableBaseEntity.ID_COLUMN;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String TEXT_COLUMN            = TextTableBaseEntity.TEXT_COLUMN;
    
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