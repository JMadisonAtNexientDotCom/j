package test.dbDataAbstractions.bundleTypes;

import java.util.List;
import primitives.Synopsis;
import test.dbDataAbstractions.entities.bases.BundleEntityBase;
import test.dbDataAbstractions.entities.tablePojos.CueCard;
import test.dbDataAbstractions.entities.tablePojos.Deck;
import test.dbDataAbstractions.entities.tablePojos.Quar;
import test.dbDataAbstractions.fracturedTypes.clientServerConversation.lectern.Slate;

/**-------------------------HEADER START----------------------------------------

////////////////////////////////////////////////////////////////////////////////
                                                                              //
[ - - - - - - TRIVIA BUNDLE  - - - - - - ]   DEFINITIONS:                     //
+########################################+   TRIVA BUNDLE: An package         //
#  [    DECK      ]     [    QUAR     ]  #   containing all the information   //
#  +--------------+     +-------------+  #   necessary for the Jester & Ninja //
#  |              |     |             |  #   to play the Riddle Triva game.   //
#  | +----------+ |     | +---------+ |  #                                    //
#  | | CUE CARD | | <-> | |  SLATE  | |  #   DECK: A collection of CueCards.  //
#  | +----------+ |     | +---------+ |  #   The the Joker reads from.        //
#  |              |     |             |  #                                    //
#  | +----------+ |     | +---------+ |  #   QUAR: A collection of Slates.    //
#  | | CUE CARD | | <-> | |  SLATE  | |  #   That the Ninja holds onto.       //
#  | +----------+ |     | +---------+ |  #                                    //
#  |              |     |             |  #   CUE CARD: A card with a Riddle on//
#  | +----------+ |     | +---------+ |  #   it and some proposed solutions.  //
#  | | CUE CARD | | <-> | |  SLATE  | |  #                                    //
#  | +----------+ |     | +---------+ |  #   SLATE: A tiny chalk board that   //
#  |              |     |             |  #   the ninja uses to answer ONE     //
#  +--------------+     +-------------+  #   riddle. Why Ninja doesn't just   //
#                                        #   use a slip of paper for each     //
+########################################+   question, I do not know.         //
                                                                              //
Why the word "QUAR":                                                          //
There was no concise word for "Quarry Stone". Though the term                 //
Quarry Stone Cores, and Slabs came up. "QuarryStoneCore" is too verbose.      //
"Slab" does not represent the correct shape.                                  //
Definition: A rectangular prism of smooth quarry stone, that is then          //
cut into bread-shaped slices called SLATES.                                   //
                                                                              //
////////////////////////////////////////////////////////////////////////////////


  @author jmadison: ON:2015.09.18_0448PM
  
  
-------------------------------HEADER END------------------------------------**/
public class TriviaBundle extends BundleEntityBase{
    
    /** A Summary of what this TrivaBundle is about.----------------------------
     *  Stores information like:
     *  [Game Type / Test Type]
     *  ...Whatever other header-style information
     *     should be associated with a set of [Riddles/Questions]
     ------------------------------------------------------------------------**/
    public Synopsis synopsis;
    
    /** The deck of CueCards the Jester reads from. **/
    public Deck deck;
    
    /** A gigantic rectangular pillar of smooth black stone. -------------------
     *  That the Ninja promptly slices into tiny sheets with
     *  their mystically sharp Ninja sword.
     * 
     *  An impractical way to create sheets to write on.
     *  But just let him be awesome okay? -----------------------------------**/
    public Quar quar;
    
}//CLASS::END
