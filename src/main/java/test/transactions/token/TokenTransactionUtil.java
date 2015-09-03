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
        
        //ENTER TRANSACTION:
        Session ses = TransUtil.enterTransaction();
        
        //TRANSACTION LOGIC:
        TokenTable tt = new TokenTable();
        tt.setToken("!!!TestToken!!!");
        tt.setComment("from doTestTransaction()");
        
        //EXIT TRANSACTION:
        TransUtil.exitTransaction(ses,tt);
        
    }//FUNC::END
    
     
    /** Gets an entity representing a [record/entry] in the token table.     ***
    *** Using the token's HASH value to fetch the [record/entry].            ***
    *** @param tv : The token's value.                                       ***
    *** @return   : Returns container with found entity.                     *** 
    ***             If no match found, container is configured               ***
    ***             to reflect that. This helps us avoid returning NULL.     **/
    public static BaseEntityContainer getTokenEntityUsingTokenString(String tv){
        
        //Enter Transaction:
        Session session = TransUtil.enterTransaction();
        
        //Transaction Logic:
        Criteria criteria = session.createCriteria(TokenTable.class);
        criteria.add(Restrictions.eq(TokenTable.VARNAME_TOKEN, tv));

        TokenTable theToken = (TokenTable) criteria.uniqueResult();

        //Decide how we are going to exit the transaction,
        //based on if the token was null or not:
        if (theToken!=null) {
            TransUtil.exitTransaction(session, theToken);
        }
        else
        {    //exits a transaction where the entity was null:
             TransUtil.nullExit(session,theToken);
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
