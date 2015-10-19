package test.dbDataAbstractions.entities.tables.riddleTrialStore.questionStore;

import javax.persistence.Column;
import test.config.constants.identifiers.TableNameReg;
import test.config.constants.identifiers.VarNameReg;

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
 * This table represents the INNARDS of the deck_table.
 * 
 * A "PURSE" is a specific type of TABLE who's entries should be identified
 * by a GROUP_ID. Individual entries from a "PURSE" are not very meaningful.
 * But the COLLECTION of entries under the same group_id are.
 * 
 * 
 * @author jmadison :2015.10.19
 */
public class DeckPurse {
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME        = TableNameReg.DECK_PURSE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN         = VarNameReg.ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String GROUP_ID_COLUMN   = VarNameReg.GROUP_ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String LOCUS_COLUMN      = VarNameReg.LOCUS;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String CUECARD_ID_COLUMN = VarNameReg.CUECARD_ID;
    
    /** Reference a group_id within the master group_table **/
    @Column public Long group_id;
    
    /** The unique position of this record within the group it belongs to. **/
    @Column public Long locus;
    
    /** The contents of a single card within the deck. **/
    @Column public Long cuecard_id;
    
    
}//CLASS::END
