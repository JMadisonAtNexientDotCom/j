package test.transactions.util.tables.wildc;

import org.hibernate.Session;
import test.dbDataAbstractions.entities.tables.KindaTable;
import test.dbDataAbstractions.entities.tables.WildcTable;
import test.transactions.util.TransUtil;

/**-----------------------------------------------------------------------------
 * Transaction utility that handles transactions involving the "Wildc" table.
 * "Wildc == "wild card".
 * @author jmadison:2015.11.02_0819PM(Nov2nd,Year2015.Monday)
 ----------------------------------------------------------------------------**/
public class WildcTransUtil {
    
    /** Makes a new entity. And saves it so it's primary key is generated.**/
    public static WildcTable makeNextWildc(){
        
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();
        
        WildcTable op;
        op = new WildcTable();
        ses.save(op); //<--so primary key is generated.
        
        return op;
        
    }//FUNC::END
    
}//CLASS::END
