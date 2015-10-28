package test.transactions.util.tables.kinda;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import test.dbDataAbstractions.entities.EntityUtil;
import test.dbDataAbstractions.entities.tables.KindaTable;
import test.transactions.util.TransUtil;

/**
 * 
 * @author jmadison:2015.10.27(Oct27th,Year2015.Tuesday)
 */
public class KindaTransUtil {
    
    /**
     * Create a batch of KindaTable record stubs.
     * 
     * THIS CLASS DOES PERSIST THE OBJECTS MADE!
     * I didn't want to, because that means if something goes wrong, we will
     * have junk data in the database. But we need to work with the primary 
     * keys, so they must be persisted.
     * 
     * @param numToMake :How many do you want?
     * @return :The entities requested. */
    public static List<KindaTable> makeBatchOfKindaStubs(int numToMake){
        
        TransUtil.insideTransactionCheck();
        List<KindaTable> op;
        op = EntityUtil.makeEntitiesFromClass_PersistIDS
                                                  (KindaTable.class, numToMake);
        return op;
    }//FUNC::END
    
    /** Makes a new entity. And saves it so it's primary key is generated.**/
    public static KindaTable makeNextKinda(){
        
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();
        
        KindaTable op;
        op = new KindaTable();
        ses.save(op); //<--so primary key is generated.
        
        return op;
        
    }//FUNC::END

}//CLASS::END
