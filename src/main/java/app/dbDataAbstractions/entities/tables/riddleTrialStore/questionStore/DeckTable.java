package app.dbDataAbstractions.entities.tables.riddleTrialStore.questionStore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import app.config.constants.identifiers.TableNameReg;
import app.config.constants.identifiers.VarNameReg;
import app.dbDataAbstractions.entities.bases.BaseEntity;

/**
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
 * 
 * The Deck_Table represents the MASS/CORE of a riddle-trial.
 * The Deck_Table holds references to unique Decks of CueCards.
 * 
 * @author jmadison :2015.10.19
 * 
 * NOTE: Try to see if we can do this WITHOUT getters&setters.
 *       They are just boilerplate pains as far as I am concerned.
 *       If we find out we NEED getters+setters to get this code to
 *       work, then we will add them.
 * 
 *       But for the most part, I want my table-entities to be DATA-OBJECTS.
 *       (Objects with no functionality,)
 *       (and with their internal structure exposed)
 */
@Entity
@Table(name= DeckTable.TABLE_NAME)  
public class DeckTable extends BaseEntity{
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME           = TableNameReg.DECK_TABLE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN            = VarNameReg.ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String DECK_GI_COLUMN       = VarNameReg.DECK_GI;
    
    
     /**Foreign GROUP-id referencing the group_id column within DeckPurse**/
    @Column(name=DECK_GI_COLUMN) public Long deck_gi;
    
    
}//CLASS::END
