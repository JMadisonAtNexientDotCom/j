package test.dbDataAbstractions.entities.bases;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import test.config.constants.identifiers.VarNameReg;


/**
 * A base entity who's structure simply has a primary key id column
 * and a varchar column for text called "text".
 * 
 * Original usage:
 * Eliminating redundant code between RiddleTable.java and RhymeTable.java
 * Since both of those tables have identical structures.
 * 
 * @author jmadison
 */
@MappedSuperclass
public class TextTableBaseEntity extends BaseEntity{
    
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN              = VarNameReg.ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String TEXT_COLUMN            = VarNameReg.TEXT;
    
  //ID is now part of base entity.
  //shared code between riddle+rhyme tables, can we make base class?
  ////////////////////////////////////////////////////////
  //@Id
  //@GeneratedValue(strategy = GenerationType.IDENTITY)
  //@Column(name=ID_COLUMN)
  //private long id;
  
  @Column(name=TEXT_COLUMN)
  private String text;
    
 
  
  public String getText(){ return text;}
  public void setText(String text){ this.text = text;}
  /////////////////////////////////////////////////////////
    
}//CLASS::END
