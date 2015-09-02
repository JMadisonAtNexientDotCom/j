package test.transactions.token;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateUtil;
import test.entities.TokenTable;
import test.transactions.util.TransUtil;
/**
 * A utility to manage transactions done to the TokenTable.
 * This type of organization may not be needed. But I have a huntch
 * it will be helpful.
 * @author jmadison
 */
public class TokenTransactionUtil {
    
    //Code originally from:
    //http://docs.jboss.org/hibernate/orm/5.0/quickstart/html/
    //Example 5. Saving entities
    public static void doTestTransaction(){
        
      
        //ENTER TRANSACTION:
        Session ses = TransUtil.enterTransaction();
        
        //TRANSACTION LOGIC:
        TokenTable tt = new TokenTable();
        tt.setValue("superToken101");
        tt.setComment("TheTokenMsg");
        
        //EXIT TRANSACTION:
        TransUtil.exitTransaction(ses,tt);
        
        
    }//FUNC::END
}//CLASS::END
