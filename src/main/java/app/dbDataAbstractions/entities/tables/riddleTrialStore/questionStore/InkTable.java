package app.dbDataAbstractions.entities.tables.riddleTrialStore.questionStore;

////////////////////////////////////////////////////////////////////////////////

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import app.config.constants.identifiers.TableNameReg;
import app.config.constants.identifiers.VarNameReg;
import app.dbDataAbstractions.entities.bases.BaseEntity;

//TABLE: Each entry is a meaningful datapoint.                                //
//PURSE: A table where entries are only meaningful as a cluster               //
//       of records with same group id. Thinking of it as a jumbled           //
//       mess inside a purse, where you have to manually sort though it       //
//       and collect what you want.                                           //
////////////////////////////////////////////////////////////////////////////////
// DECK_TABLE:                          +----------+                          //
// Wraps the CONTENTS of the deck into  |id|deck_gi|                          //
// one id reference.                    +--------+-+                          //
//                                               |                            //
// DECK_PURSE:                          +--------V-------------------+        //
// The jumbled contents of a deck.      |id|group_id|locus|cuecard_id|        //
// One unique deck within table is      +-----------------------|----+        //
// identified by the group_id.                                  |             //
//                                       + - - - - - - - - - - -+             //
//                                       |                                    //
// CUECARD_TABLE:                       +V------------------+                 //
// Represents a single interview        |id|riddle_id|ink_id|                 //
// question asked by the joker to       +----------------|--+                 //
// the ninja.                                            |                    //
//                                       + - - - - - - - +                    //
//                                       |                                    //
// INK_TABLE:                           +V--------+                           //
// Represents [QUIPS/RHYMES/ANSWERS]    |id|ink_gi|                           //
// Ninja can select.                    +-----|---+                           //
//                                            |                               //
// INK_PURSE:                           +-----V--------------------+          //
// Each cue card may have n# lines of   |id|group_id|locus|rhyme_id|          //
// "ink" on it.                         +--------------------------+          //
////////////////////////////////////////////////////////////////////////////////
/**
 * INK_TABLE DETAILS:
 * The word "ink" is used to describe the "INK" used to print out the
 * possible choices on the cue card. Possible choices, when on a cue-card
 * are known as "quips". Quips are RHYMES from the riddle table, but in
 * the context of a cuecard.
 * 
 * The ink table stores references to GROUPS of [quips/RHYMES/answers]
 * that are stored witin the ink_purse.
 * 
 * 
 * 
 * @author jmadison :2015.10.19:
 */
@Entity
@Table(name= InkTable.TABLE_NAME)
public class InkTable extends BaseEntity{
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME = TableNameReg.INK_TABLE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN  = VarNameReg.ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String INK_GI_COLUMN     = VarNameReg.INK_GI;
    
    /** Foreign key to group_id column within the ink_purse **/
    @Column(name = INK_GI_COLUMN) public Long ink_gi;
    
}//CLASS::END
