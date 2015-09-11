package test.transactions.util.riddleRhyme.riddle;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import test.MyError;
import test.config.debug.DebugConfig;
import test.entities.containers.BaseEntityContainer;
import test.entities.tables.RiddleTable;
import test.entities.tables.TokenTable;
import test.transactions.util.TransUtil;

/**
 * A utility for transactions involving only the riddle_table.
 * @author jmadison
 */
public class RiddleTransUtil {
    
    /** Get riddle entity by ID, if not found, the container will reflect that.
     * @param riddleID :The ID of the riddle entity you want.
     * @return :A container that will contain the entity if one was found. **/
    public static BaseEntityContainer getRiddleEntityByID(long riddleID){
        
        //make sure we are in a transaction state:
        //and get the session object.
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();
        
        //Transaction Logic:
        Criteria criteria = ses.createCriteria(RiddleTable.class);
        criteria.add(Restrictions.eq(RiddleTable.ID_COLUMN, riddleID));
        RiddleTable theRiddle = (RiddleTable) criteria.uniqueResult();
        
        //package output:
        BaseEntityContainer op;
        op = BaseEntityContainer.make_NullAllowed(theRiddle);
        
        //Return the riddle:
        return op;
        
    }//FUNC::END
    
}//CLASS::END
