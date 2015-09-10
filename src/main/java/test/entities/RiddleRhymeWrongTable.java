package test.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity for our table that explicity marks 
 * riddle+rhyme (question+answer) pairs as WRONG.
 * @author jmadison **/
@Entity
@Table(name= RiddleRhymeWrongTable.TABLE_NAME)
public class RiddleRhymeWrongTable extends RiddleRhymeJoinTableBaseEntity{
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME       = "riddle_rhyme_wrong_table";
    
    /** Column name stored as string constant for easy refactoring. 
        COMPILER WARNING: 
        "Field hides another Field" is LIE because this is static.**/
    public static final String RIDDLE_ID_COLUMN =
        RiddleRhymeJoinTableBaseEntity.RIDDLE_ID_COLUMN;
    
    /** Column name stored as string constant for easy refactoring. 
        COMPILER WARNING: 
        "Field hides another Field" is LIE because this is static.**/
    public static final String RHYME_ID_COLUMN  = 
        RiddleRhymeJoinTableBaseEntity.RHYME_ID_COLUMN;
    
}//CLASS::END
