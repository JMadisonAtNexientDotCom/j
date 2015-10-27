package test.config.constants.identifiers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import test.MyError;
import test.config.constants.identifiers.utils.StringConstantFinderUtil;

/**
 * Master registry for all of the table names.
 * @author jmadison :2015.09.30(Sept,30th,Year2015) **/
public class TableNameReg {
    
//Tables who's job it is to store the contents of a riddle-trial.
////////////////////////////////////////////////////////////////////////////////
// DECK_TABLE:                          +----------+                          //
// Wraps the CONTENTS of the deck into  |id|deck_gi|                          //
// one id reference.                    +--------+-+                          //
//                                               |                            //
// DECK_PURSE:                          +--------V-------------+              //
// The jumbled contents of a deck.      |id|group_id|cuecard_id|              //
// One unique deck within table is      +------------------|---+              //
// identified by the group_id.                             |                  //
//                                       + - - - - - - - - +                  //
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
// INK_PURSE:                           +-----V--------------+                //
// Each cue card may have n# lines of   |id|group_id|rhyme_id|                //
// "ink" on it.                         +--------------------+                //
////////////////////////////////////////////////////////////////////////////////
    public static final String DECK_TABLE    = "deck_table";
    public static final String DECK_PURSE    = "deck_purse";
    public static final String CUECARD_TABLE = "cuecard_table";
    public static final String INK_TABLE     = "ink_table";
    public static final String INK_PURSE     = "ink_purse";
    
//Tables who's job it is to store the contents of a ninja's RESPONSES
//to an entire riddle-trial:
//QUAR_TABLE:                                    +----------+                 //       
//Packages the contents of a single quar into    |id|quar_gi|                 //       
//one reference. For ease of use.                +------+---+                 //               
//                                                      |                     //       
//QUAR_PURSE:                                    +-----/|\------------+       //       
//The contents of the quars. A quar is a         |id|group_id|slate_id|       //       
//gigantic black monolith of slates.             +----------------+---+       //       
//It represents all of the answer slates the                      |           //       
//Ninja filled out to complete the riddle-trial.  +---------------+           //       
//                                                |                           //       
// CUECARD_TABLE:                                +V----------------------------+       
// An individual slate within the quar.          |id|status|cuecard_id|chalk_id|       
//                                               +------------------------+----+       
//                                                                        |   //       
//                                                +-----------------------+   //       
//                                                |                           //       
// CHALK_TABLE:                                  +V----------+                //       
// Chalk being what is written on the slate.     |id|chalk_gi|                //          
//                                               +------+----+                //       
//                                                      |                     //       
// CHALK_PURSE:                                  +-----/|\------------+       //       
// A slate can have multiple answers written on  |id|group_id|rhyme_id|       //             
// it. This table groups those answers together. +--------------------+       //      
public static final String QUAR_TABLE    = "quar_table";
public static final String QUAR_PURSE    = "quar_purse";
public static final String SLATE_TABLE   = "slate_table";
public static final String CHALK_TABLE   = "chalk_table";
public static final String CHALK_PURSE   = "chalk_purse";
    
    //Other Tables:
    public static final String GROUP_TABLE = "group_table"; //master table of group IDS.
    public static final String RIDDLE_RHYME_TRUTH_TABLE = "riddle_rhyme_truth_table";
    public static final String RIDDLE_RHYME_WRONG_TABLE = "riddle_rhyme_wrong_table";
    public static final String TRANS_TABLE   = "trans_table";
    public static final String RHYME_TABLE   = "rhyme_table";
    public static final String RIDDLE_TABLE  = "riddle_table";
    public static final String ADMIN_TABLE   = "admin_table";
    public static final String NINJA_TABLE   = "ninja_table";
    public static final String TOKEN_TABLE   = "token_table";
    public static final String SESSION_TABLE = "session_table";
    public static final String OWNER_TABLE   = "owner_table";
    public static final String TRIAL_TABLE   = "trial_table";
    public static final String KIND_TABLE    = "kind_table";
    
    /**
     * ORIGINAL USEAGE: Checking for string transposition errors where
     * string argument to function has been swapped with table name.
     * 
     * @param value :The const value we are checking for.
     * @return :returns true if the constant exists.
     */
    public static boolean contains(String value){
        return StringConstantFinderUtil.contains(TableNameReg.class, value);
    }//FUNC::END
    
}//CLASS::END
