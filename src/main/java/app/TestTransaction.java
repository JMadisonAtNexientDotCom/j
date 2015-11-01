package app;
import org.hibernate.Session;
import app.dbDataAbstractions.entities.tables.TokenTable;
import app.transactions.util.TransUtil;
import app.debug.DebugConsts;

 /** A test transaction. Just used in development. --*
 *-- Will not be used in final deployment.         --*
 *-- @author jmadison :2015.09.02_0424PM           **/
public class TestTransaction {
    
    //Code originally from:
    //http://docs.jboss.org/hibernate/orm/5.0/quickstart/html/
    //Example 5. Saving entities
    public static void doTestTransaction(){
        
        //Enter Transaction:
        Session ses = TransUtil.enterTransaction();
        
        //Transaction Logic:
        TokenTable ent = new TokenTable();
        ent.setToken_hash(DebugConsts.HARD_CODED_TOKEN);
        ent.setComment("!!The message. TEN!!");
        TransUtil.markEntityForSaveOnExit(ent);
        
        //Exit Transaction:
        TransUtil.exitTransaction(ses,true);
         
    }//FUNC::END
}//CLASS::END
