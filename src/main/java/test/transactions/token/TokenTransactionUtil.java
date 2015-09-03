package test.transactions.token;

import test.MyError;
import org.hibernate.Criteria;
import org.hibernate.Session;
//import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
//import utils.HibernateUtil;
import test.entities.TokenTable;
import test.transactions.util.TransUtil;
import test.entities.BaseEntityContainer;
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
        
        //Throw error if transaction state of application is
        //NOT currently inside a transaction.
        TransUtil.insideTransactionCheck();
        
        //I have decided that we should NOT wrap pre-made transactions
        //between TransUtil.enterTransaction() and
        //        TransUtil.exitTransaction() commands.
        //HOWEVER! We should check that the application is inside a transaction
        //state for any transaction utility method.
        
        
        //TRANSACTION LOGIC:
        TokenTable tt = new TokenTable();
        tt.setToken("!!!TestToken!!!");
        tt.setComment("from doTestTransaction()");
        
        
    }//FUNC::END
    
     
    /** Gets an entity representing a [record/entry] in the token table.     ***
    *** Using the token's HASH value to fetch the [record/entry].            ***
    *** @param tv : The token's value.                                       ***
    *** @return   : Returns container with found entity.                     *** 
    ***             If no match found, container is configured               ***
    ***             to reflect that. This helps us avoid returning NULL.     **/
    public static BaseEntityContainer getTokenEntityUsingTokenString(String tv){
        
        //Enter Transaction:
        //Session session = TransUtil.enterTransaction();
        
        //Test to see if we are currently in a transaction state.
        //If we are not, code will crash.
        Session session = TransUtil.getActiveTransactionSession();
        
        //Transaction Logic:
        Criteria criteria = session.createCriteria(TokenTable.class);
        criteria.add(Restrictions.eq(TokenTable.COLUMN_TOKEN, tv));

        TokenTable theToken = (TokenTable) criteria.uniqueResult();

        
        //Rather than handling exiting of transaction here,
        //we will simply build an array of entities that need to be
        //saved. When all of the transactions from different utilities
        //have finished:the entity saver queue within our main
        //1: we make a call to transaction utility to exit transaction.
        //2: Any entities that have been edited and put in our queue will
        //   be saved&commited.
        if (theToken!=null) {
            TransUtil.markEntityForSaveOnExit(theToken);
        }
        
        //Create output:
        BaseEntityContainer op = new BaseEntityContainer();
        if(null == theToken)
        {   //make container configured as empty:
            op.entity = null;
            op.exists = false;
        }
        else
        {   //make container configured as populated:
            op.entity = theToken;
            op.exists = true;
        }
        
        //return the container with our result of query:
        return op;
        
    }//END::FUNC
    
    public static void getAllTokens(){
        throw new MyError("TODO: getAllTokens function.");
    }
    
}//CLASS::END
