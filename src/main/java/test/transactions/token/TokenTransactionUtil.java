package test.transactions.token;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import utils.HibernateUtil;
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
        tt.setValue("!!!TestToken!!!");
        tt.setComment("from doTestTransaction()");
        
        //EXIT TRANSACTION:
        TransUtil.exitTransaction(ses,tt);
        
    }//FUNC::END
    
     
    /** Gets an entity representing a [record/entry] in the token table.     ---
    --- Using the token's value to fetch the [record/entry].                 ---
    --- @param tv : The token's value.                                       **/
    public static BaseEntityContainer getTokenEntityUsingTokenValue(String tv){
        
        //Enter Transaction:
        Session session = TransUtil.enterTransaction();
        
        //Transaction Logic:
        Criteria criteria = session.createCriteria(Employee.class);
        criteria.add(Restrictions.eq("id", employeeId));

        Employee employee = (Employee) criteria.uniqueResult();

        if (employee!=null) {
                System.out.println("Employee found:");
                System.out.println(employee.getId() + " - " + employee.getName());
        }
        
        //Exit Transaction:
        TransUtil.exitTransaction(session, ent);
    }
    
    public static void getAllTokens(){
        throw new MyError("TODO: getAllTokens function.")
    }
    
}//CLASS::END
