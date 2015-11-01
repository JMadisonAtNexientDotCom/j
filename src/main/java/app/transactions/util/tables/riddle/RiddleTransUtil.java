package app.transactions.util.tables.riddle;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import app.MyError;
import app.config.debug.DebugConfig;
import app.dbDataAbstractions.entities.containers.BaseEntityContainer;
import app.dbDataAbstractions.entities.tables.RhymeTable;
import app.dbDataAbstractions.entities.tables.RiddleTable;
import app.dbDataAbstractions.entities.tables.TokenTable;
import app.transactions.util.TransUtil;
import app.transactions.util.forNoClearTableOwner.riddleRhyme.rhymeRiddle.RRCommonCodeTransUtil;

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
    
    /** Returns RANDOM riddle if riddleID supplied is negative.
     *  Returns riddle of a given ID if riddleID is >= 0.
     *  Will throw error if >=0 value is not found in table.
     * @param riddleID : The ID of the riddle.
     * @return :A RiddleTable entity representing the riddleID. **/
    public static RiddleTable getRiddleByID_or_Random(long riddleID){
        
        //ERROR CHECK: Are we inside a transaction state?
        TransUtil.insideTransactionCheck();
        
        RiddleTable op = null;
        if(riddleID < 0){//-----------------------------------------------------
            op = getOneRandomRiddle();
        }else
        if(riddleID >= 0){
            boolean doesExist = doesRiddleExist(riddleID);
            if(false==doesExist){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
                String msg = "[getRiddleByID_or_Random asked for.]";
                msg+="[A non-existant riddle of ]";
                msg+="[ID:" + Long.toString(riddleID) + "]";
                doError(msg);
            }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            BaseEntityContainer con = getRiddleByID(riddleID);
            op = (RiddleTable)con.entity;
        }else{
            //if you get here. Logic of block is messed up.
            doError("This should be an unreachable statement.");
        }//---------------------------------------------------------------------
        
        //Null output is considered an error:
        if(null==op){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            String m2 = "[op should not be null if ]";
            m2+="[we get to this line of execution.]";
            doError(m2);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //return the output:
        return op;
        
    }//FUNC::END
    
    /**
     * Return a random riddle. Should NOT return null. I am not going to
     * return a BaseEntityContainer just in case the database has ZERO
     * riddles in it. A database with zero riddles in it is an ERROR
     * as far as I am concerned.
     * @return : ONE random riddle (question) **/
    public static RiddleTable getOneRandomRiddle(){
        
        //Make sure we are in a transaction state:
        //And get the session object:
        TransUtil.insideTransactionCheck();
        
        //Figure out max riddle index, then get a random riddle
        //in the inclusive range of 1-to-maxRiddleIndex
        long maxRiddleID = getMaxRiddleID();
        double randPercentage = Math.random();
        long randomIndex = (long)(maxRiddleID * randPercentage);
        if(0==randomIndex){randomIndex = 1;} 
        BaseEntityContainer boxedOutput = getRiddleByID(randomIndex);
        RiddleTable op = null;
        if(false == boxedOutput.exists)
        {//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            String msg = "";
            msg+="[getOneRandomRiddle pulled a non-existant riddleID somehow.]";
            msg+="[Possible problem:]";
            msg+="[1. Inclusive index grabbing formula is incorrect.]";
            msg+="[2. Holes exist in your data. Example: A set: [1,50,100]]";
            msg+="[Has a max index of 100, but 2 very large holes exist in]";
            msg+="[The set of data, seeing that only 3 entries exist.]";
            doError("");
        }else{//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            op = (RiddleTable)boxedOutput.entity;
        }//BLOCK::END
        
        if(null==boxedOutput.entity){ //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
                String m2= "Entity Integrity Problem in getOneRandomRiddle()";
                doError(m2);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //return our random riddle:
        if(null == op){doError("output should never be null on this line");}
        return op;
        
    }//FUNC::END
    
    /** Queries database and gets you the max riddle [id/index].
     *  indeces of the riddles should be SEQUENTIAL. So this information
     *  should also tell you how many riddles there are. But since there
     *  is ~uncertanty~ if the first entry is "1" or "0" I think it is better
     *  to get the max index than the count.
     * @return :The highest indexed riddle (question) in the database.      **/
    public static long getMaxRiddleID(){
        
        //Make sure we are in a transaction state:
        //And get the session object:
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();
        
        //Transaction logic:
        //http://stackoverflow.com/questions/3743677/get-max-value-record-from-table-in-hibernate
        Criteria c = ses.createCriteria(RiddleTable.class);
        c.setProjection(Projections.max(RiddleTable.ID_COLUMN));
        Long boxedLong = (Long)c.uniqueResult();
        long op = boxedLong; //<--- [auto/implicitly] unboxes?
        
        //return result:
        return op;
        
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
                doError(msg);
            }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        return op;
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = RiddleTransUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
    
}//CLASS::END
