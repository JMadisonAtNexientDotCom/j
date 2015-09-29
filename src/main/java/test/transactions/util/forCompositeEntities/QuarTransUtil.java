package test.transactions.util.forCompositeEntities;

import test.MyError;
import test.config.debug.DebugConfig;
import test.dbDataAbstractions.entities.composites.CueCard;
import test.dbDataAbstractions.entities.composites.Deck;
import test.dbDataAbstractions.entities.composites.Quar;
import test.dbDataAbstractions.fracturedTypes.clientServerConversation.lectern.Slate;
import test.debug.debugUtils.lists.ListDebugUtil;
import test.transactions.util.TransUtil;
import test.transactions.util.forBundleEntities.TriviaBundleTransUtil;

/**-----------------------------------------------------------------------------
 * 
 * DESIGN NOTE:
 * Why considered a composite entity and not a bundle entity?
 * NOT a bundle type because a QUAR is a collection of uniform data.
 * A QUAR represents a collection of Slates. 
 * (Answer Sheets Filled out by the Ninja)
 * 
 * 
 * @author jmadison: 2015.09.18_0710PM
 ----------------------------------------------------------------------------**/
public class QuarTransUtil {
    
    
    /**-------------------------------------------------------------------------
     * Makes blank slates that are ready to be filled out by Ninja
     * @param d :The deck of CueCards to make blanks slates for.
     * @return: A Quar object containing all of the slates.
     ------------------------------------------------------------------------**/
    public static Quar makeBlankSlatesForDeck(Deck d){
        
        //check to see if we are in a transaction state.
        //Also make sure input is not null.
        TransUtil.insideTransactionCheck();
        if(null==d){doError("d was null!");}

        //core logic:-----------------------------------------------------------
        /** op == "output variable" **/
        Quar op = Quar.makeEmptyInstance();
        CueCard cur_card;
        long    cur_riddleID;
        Slate   cur_slate;
        int len = d.cards.size();
        for(int i = 0; i < len; i++){
            cur_card = d.cards.get(i);
            cur_riddleID = cur_card.jest.getId();
            cur_slate = Slate.makeBlankSlate( cur_riddleID );
            
            //Possibly heavy error checks should 
            //be wrapped in optional compile:
            if(DebugConfig.isDebugBuild){
                Slate.assertBlankSlate( cur_slate );
            }////////////////////////////
            
            //Slate needs to be partially filled out for convinience.
            //Ninja should not be touching this information. And later
            //We will want integrity checks for it.
            cur_slate.originalQuips = CueCard.extractQuipIDs(cur_card);
            
            op.slates.add(cur_slate);
        }//NEXT i
        //----------------------------------------------------------------------
        
        //Error checks:
        if(op.slates.size() != d.cards.size()){
            doError("slates.size != cards.size");
        }//////////////////////////////////////
        
        //confirm that slates's original-quips match up with our deck:
        //That would need to be handled in a CueCard-Slate utility
        //since it involves both. The check in TrivaBundleTransUtil
        //should be enough.

        //return output:
        return op;
      
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = QuarTransUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw new MyError(clazz, err);
    }//FUNC::END
    
}//CLASS::END
