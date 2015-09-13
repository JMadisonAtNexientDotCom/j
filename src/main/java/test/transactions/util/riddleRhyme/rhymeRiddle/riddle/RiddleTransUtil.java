package test.transactions.util.riddleRhyme.rhymeRiddle.riddle;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import test.MyError;
import test.config.debug.DebugConfig;
import test.entities.containers.BaseEntityContainer;
import test.entities.tables.RhymeTable;
import test.entities.tables.RiddleTable;
import test.entities.tables.TokenTable;
import test.transactions.util.TransUtil;
import test.transactions.util.riddleRhyme.rhymeRiddle.RRCommonCodeTransUtil;

/**
 * A utility for transactions involving only the riddle_table.
 * @author jmadison
 */
public class RiddleTransUtil {
    
    /** Used to test if a riddle with a given ID exists. NOT meant as a 
     *  "look before you leap" to getRiddleByID() since getRiddleByID()
     *  does not error if you give it invalid ID.
     * 
     *  Original usage: To see if bad riddleID was given to rest service.
     *                  That way service can respond accordingly.
     * @param riddleID: The unique ID of the riddle you want.
     * @return        : A boolean telling us if a riddle with that idea actually
     *                  exists. TRUE==exists. FALSE==does-not-exist          **/
    public static boolean doesRiddleExist(long riddleID){
        
        //ERROR CHECK: Make sure we are inside a transaction state:
        TransUtil.insideTransactionCheck();
        
        //Logic Body:
        BaseEntityContainer bec = getRiddleByID(riddleID);
        return bec.exists;
        
    }//FUNC::END
    
    /** Get riddle entity by ID, if not found, the container will reflect that.
     * @param riddleID :The ID of the riddle entity you want.
     * @return :A container that will contain the entity if one was found. **/
    public static BaseEntityContainer getRiddleByID(long riddleID){
        
        //make sure we are in a transaction state:
        //and get the session object.
        TransUtil.insideTransactionCheck();
       
        //HeavyLifting function. Common code between RhymeTable and RiddleTable:
        BaseEntityContainer op;
        op = RRCommonCodeTransUtil.getTableEntityByID
                                                  (RiddleTable.class, riddleID);
        
        //ERROR CHECK: Make sure we have the correct entity type:
        if(op.exists)//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        {//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            if(false==(op.entity instanceof RiddleTable))
            {
                String msg="";
                msg+="[Entity is not instance of correct table:]";
                msg+="[" + RiddleTable.class.getSimpleName() + "]";
                msg+="[Error is mostly in logic of this function.]";
                msg+="[Check that the correct class references were used.]";
                throw new MyError(msg);
            }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        return op;
        
    }//FUNC::END
}//CLASS::END
