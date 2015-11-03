package test.transactions.util.tables.stack;

import org.hibernate.Session;
import test.dbDataAbstractions.entities.tables.KindaTable;
import test.dbDataAbstractions.entities.tables.StackTable;
import test.transactions.util.TransUtil;

/**-----------------------------------------------------------------------------
 * Handles transactions involving the StackTable.
 * The StackTable is similiar to the GroupTable, however, it's collections
 * have a "stack_id" AND "locus" the locus gives an order to the object
 * within the stack.
 * 
 * Original reason for creating this class:
 * Order in which we create sub-groups of cuecards will matter if we are
 * to dynamically build tests. Rather than re-structure the decks,
 * we will simply ADD these groups as auxillary meta-data.
 * 
 * @author jmadison:2015.11.02_0819PM(Nov2nd,Year2015.Monday)
 ----------------------------------------------------------------------------**/
public class StackTransUtil {
    
    /** Makes a new entity. And saves it so it's primary key is generated.**/
    public static StackTable makeNextStack(){
        
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();
        
        StackTable op;
        op = new StackTable();
        ses.save(op); //<--so primary key is generated.
        
        return op;
        
    }//FUNC::END
    
}//CLASS::END
