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
     * This class does NOT persist these objects.
     * That is the job of whatever is calling it.
     * We gather all the entities we need first. 
     * Then if a train wreck happens, we wont have
     * junk data persisted into our database.
     * @param numToMake :How many do you want?
     * @return :The entities requested. */
    public static List<KindaTable> makeBatchOfKindaStubs(int numToMake){
        
        TransUtil.insideTransactionCheck();
        
        List<KindaTable> op;
        op = EntityUtil.makeEntitiesFromClass(KindaTable.class, numToMake);
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
