package test.transactions.util.tables.twine;

import org.hibernate.Session;
import test.dbDataAbstractions.entities.tables.KindaTable;
import test.dbDataAbstractions.entities.tables.TwineTable;
import test.transactions.util.TransUtil;

/**-----------------------------------------------------------------------------
 * Handles transactions involving TwineTable.
 * TwineTable is used to create ordered sub-groups of cuecards within a deck.
 * This was added to address the ability to dynamically build a test 5
 * questions at a time.
 * 
 * @author jmadison:2015.11.02_0819PM(Nov2nd,Year2015.Monday)
 ----------------------------------------------------------------------------**/
public class TwineTransUtil {
    
    /** Makes a new entity. And saves it so it's primary key is generated.**/
    public static TwineTable makeNextTwine(){
        
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();
        
        TwineTable op;
        op = new TwineTable();
        ses.save(op); //<--so primary key is generated.
        
        return op;
        
    }//FUNC::END
    
}//CLASS::END
