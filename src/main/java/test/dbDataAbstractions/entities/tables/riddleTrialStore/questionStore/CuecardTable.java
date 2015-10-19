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
*  DETAILS on CUECARD_TABLE:
*  Contains a single riddle-trial test question. But it's not just a question,
*  it contains the possible answers as well. The answers presented will affect
*  how people make their decisions. And since Drew would like to datamine
*  this eventually, we need to take that into consideration.
* 
*  Note that "cuecard" is being treated as a single word in order to keep
*  my convention of each table being a 1-word name for simplicity.
* 
* 
* @author jmadison :2015.10.19
**/
public class CuecardTable {
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME        = TableNameReg.CUECARD_TABLE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN         = VarNameReg.ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String RIDDLE_ID_COLUMN  = VarNameReg.RIDDLE_ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String INK_ID_COLUMN     = VarNameReg.INK_ID;
    
    /** The unique riddle that titles this cue card. 
     *  AKA: The question being asked. **/
    @Column(name=RIDDLE_ID_COLUMN) public Long riddle_id;
    
    /** The group of quips to select from. 0, 1 or more of these could be
     *  a correct answer to the jest (riddle_id) being asked. 
        Quip = A possible answer to question, in the context of a cuecard.
        Jest = A riddle/question being asked, in context of a cuecard. **/
    @Column(name=INK_ID_COLUMN) public Long ink_id;
}//CLASS::END
