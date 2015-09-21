
package test.transactions.util.forNoClearTableOwner.riddleRhyme.rhymeRiddle;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import test.MyError;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import test.dbDataAbstractions.entities.containers.BaseEntityContainer;
import test.dbDataAbstractions.entities.tables.RhymeTable;
import test.dbDataAbstractions.entities.tables.RiddleTable;
import test.transactions.util.TransUtil;

/**
 * A utility for common code between the 
 * RiddleTable and RhymeTable utility methods.
 * 
 * Original Usage:
 * Refactoring getRiddleEntityByID so that code can also be used
 * for getRhymeEntityByID
 * 
 * @author jmadison : 2015.09.11_0659PM **/
public class RRCommonCodeTransUtil {
    
    
    /** Get riddle entity or rhyme entity, using id.
     * @param tableClass: The RiddleTable class or RhymeTable class.
     * @param idValue :The ID of the riddle entity you want.
     * @return :A container that will contain the entity if one was found. **/
    public static BaseEntityContainer getTableEntityByID
                                               (Class tableClass, long idValue){
        
        //make sure we are in a transaction state:
        //and get the session object.
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();
        
        /** The column name we are querying. Actually is the entity's
         *  annotated variable name. But for ease, I make sure entity variable
         *  name exactly matches database column name. **/
        String column = "KEEPING_COMPILER_HAPPY";
        int ec = 0; //error check counter.
        Class tc = tableClass;
        if(tc == RiddleTable.class){column=RiddleTable.ID_COLUMN; ec++;}
        if(tc == RhymeTable.class ){column=RhymeTable .ID_COLUMN; ec++;}
        
        if(ec <=0){/////////////////////////////////////////////////////////////
            String em1 = "";
            String name1 = RiddleTable.class.getSimpleName();
            String name2 = RhymeTable.class.getSimpleName();
            em1+="[invalid table class supplied.]";
            em1+="[we are querying either:]";
            em1+="[1: Table Entity With Name:["+ name1 +"]]";
            em1+="[2: Table Entity With Name:["+ name2 +"]]";
            throw new MyError(em1);
        }else 
        if(ec > 1){
            String em2 = "";
            em2+="Somehow we had >1 class matches.";
            em2+="Error in this function's logic most likely.";
            em2+="Check the class references you used.";
            em2+="possible duplicates.";
            throw new MyError(em2);
        }///////////////////////////////////////////////////////////////////////

        //Transaction Logic:
        Criteria criteria = ses.createCriteria(tableClass);
        criteria.add(Restrictions.eq(column, idValue));
        BaseEntity outputEntity = (BaseEntity) criteria.uniqueResult();
        
        //package output:
        BaseEntityContainer op;
        op = BaseEntityContainer.make_NullAllowed(outputEntity);
        
        //Return the riddle:
        return op;
        
    }//FUNC::END 
}//CLASS::END
