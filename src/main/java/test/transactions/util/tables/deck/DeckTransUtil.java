/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.transactions.util.tables.deck;

import java.util.List;
import test.MyError;

/**
 * A utility used for transactions involving the DeckTable entity.
 * @author jmadison :2015.10.19
 */
public class DeckTransUtil {
    
    /**
     * Looks at the group of cuecard_ids and returns a valid
     * group_id if that list of cuecard_ids exist.
     * Right now:
     * 1. Order of cuecards does NOT matter for matching.
     * 2. Amount does. A list of 3 cannot match a list of 5.
     * @param cuecard_ids:The cuecard ids to find a group matching it.
     * @return :Returns >=1 if found, returns (-1) if not found.
     */
    public static long getGroupID(List<Long> cuecard_ids){
        doError("[TODO: getGroupID]");
        return (-1);
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = DeckTransUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
}//CLASS::END
