package test.dbDataAbstractions.entities.tables.riddleTrialStore.questionStore;
////////////////////////////////////////////////////////////////////////////////
//TABLE: Each entry is a meaningful datapoint.                                //

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import test.config.constants.identifiers.TableNameReg;
import test.config.constants.identifiers.VarNameReg;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import test.dbDataAbstractions.entities.bases.PurseEntity;
import javax.persistence.FetchType;  

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
 * INK_PURSE DETAILS:
 * Represents a group of quips on a cuecard.
 * (Quips are rhymes/answers in the context of a cuecard)
 * For each set of entries with the SAME group_id, there should be
 * a UNIQUE locus value that tells us what order it should appear on the
 * cuecard. This is important because the ORDER in which you propose answers
 * will have an effect on how the question is answered. And thus, needs to be
 * taken into account if we wish to datamine test results for statistics.
 * @author jmadison :2015.10.19
 */
@Entity
@Table(name= InkPurse.TABLE_NAME)  //<--can we replace this with TABLE_NAME ?
public class InkPurse extends PurseEntity {
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME        = TableNameReg.INK_PURSE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN         = VarNameReg.ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String LOCUS_COLUMN      = VarNameReg.LOCUS;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String RHYME_ID_COLUMN   = VarNameReg.RHYME_ID;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String GROUP_ID_COLUMN   = VarNameReg.GROUP_ID;
    
    //DELETE: not wanted.
    /** The position of this record within the group_id it belongs to. **/
    //@Column(name = LOCUS_COLUMN) public Long locus;
    
    //DELETE: now inherited.
    /** The group of [rhymes/quips/answers] that this record belongs to. **/
    //@Column(name = GROUP_ID_COLUMN) public Long group_id;
    
    // http://stackoverflow.com/questions/5602908/
    //                    jpa-which-should-i-use-basicoptional-or-columnnullable
    /** The actual [rhyme/quip/answer] for this record of the group. **/
    @Basic(fetch=FetchType.EAGER)
    @Column(name = RHYME_ID_COLUMN) public Long rhyme_id;
    
}//CLASS::END
