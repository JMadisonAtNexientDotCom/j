package test.transactions.util.forOwnedMainlyByOneTable.trial;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import primitives.RealAndFakeIDs;
import test.MyError;
import test.config.constants.signatures.paramVals.TRIAL_STATUS_ENUMS;
import test.dbDataAbstractions.entities.tables.TrialTable;
import test.dbDataAbstractions.requestAndResponseTypes.postTypes.postResponse.Coffer;
import test.dbDataAbstractions.requestAndResponseTypes.postTypes.postResponse.Ticket;
import test.transactions.util.TransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.ninja.NinjaTransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.session.SessionTransUtil;

/**
 * A utility concerned primarily with transactions involving the trial table.
 * Such as, registering a ninja to a new trial
 * @author jmadison:2015.10.04
 */
public class TrialTransUtil {
    
    /**
     * Takes an array of ninjas (identified by their id's) and
     * creates [trials/tests] that are linked to tokens for them.
     * These tokens are basically session-tokens that are linked to
     * a SINGLE test. They activate a session the first time the Ninja
     * uses it. And then expire duration_in_minutes after the Ninja
     * has first used it.
     * @param ninja_id_list :List of ninjas to all be given the same kind
     *                        of trial. (type was reserved word in SQL)
     * @param trial_kind     :The kind of trial that the ninjas will be taking.
     * @param duration_in_minutes :The alloted amount of time that will be given
     *                             to complete this test. Measured in minutes.
     * @return :Returns a Coffer object that is meant to be read by the
     *          ADMIN's UI so that the admin may tell the Ninja's what their
     *          token values are.
     */
    public static Coffer dispatchTokens(List<Long> ninja_id_list,int trial_kind,
                                                       int duration_in_minutes){
        
        doError("This is the thingy we are tyring to get working!!!");
        
        TransUtil.insideTransactionCheck();
        
        //Step1: Make list into unique entries, then split list into TWO
        //       lists. A list of valid ninja_id (the ninja is in system)
        //       And list of invalid ninjas. (Ones not in the system.)
        RealAndFakeIDs idSplit = 
                     NinjaTransUtil.sortNinjaIDS_IntoRealAndFake(ninja_id_list);
        
        //Step2: Go through the REAL LIST. And create tests and link to tokens.
        String num;
        Long cur;
        Ticket tik;
        Coffer cof = new Coffer();
        int len = idSplit.real.size();
        for(int r = 0; r < len; r++){
            cur = idSplit.real.get(r);
            num = Long.toString(cur);
            tik = Ticket.makeStubTicket("STUB:TODO_NINJA#" + num);
            cof.tickets.add(tik);
        }//NEXT i (real ticket)
        
        //Step3: Append FAKE/Error tickets to the end:
        int fake_len = idSplit.fake.size();
        for(int f = 0; f < fake_len; f++){
            cur = idSplit.fake.get(f);
            num = Long.toString(cur);
            tik = Ticket.makeErrorTicket("ERROR:FAKE_NINJA#" + num);
            cof.tickets.add(tik);
        }//NEXT f (fake ticket)
        
        //RETURN THE COFFER!
        return cof;
        
        /*
        //STUB FOR NOW SO THAT WE CAN GO HOME AND LEAVE CODE BASE IN TACT:
        String msg ="[DispatchTokens is meerly a stub method for now]";
        Coffer op = Coffer.makeErrorCoffer(msg);
        
        //fill with more test data:
        Ticket curTicket;
        int len = 10;
        for(int i = 0; i < len; i++){
            curTicket = Ticket.makeErrorTicket("test #" + Integer.toString(i));
            op.tickets.add( curTicket);
        }//NEXT i
        return op;
        */
        
    }//FUNC::END
    
    /**
     * @param num_trials :How many trial table entities should we make?
     * @param kind: The kind of trial we are making. 
     *              Basically "type" but was keyword in SQL.
     * @param allotted: The amount of time allotted for this test in 
     *                  milliseconds.
     * @return :See above.
     */
    public static List<TrialTable> makeBatchOfTrialStubs
        (int num_trials, long kind, long allotted){
        TransUtil.insideTransactionCheck();
        
        Session ses = TransUtil.getActiveTransactionSession();
        
        List<TrialTable> trials = new ArrayList<TrialTable>(num_trials);
        TrialTable cur;
        String iStr;
        for(int i = 0; i < num_trials; i++){
            iStr = Integer.toString(i);
            cur = new TrialTable();
            cur.setAllotted(allotted);
            cur.setComment("[Touched by makeBatchOfTrialStubs()]#:" + iStr);
            cur.setKind(kind);
            cur.setStatus(TRIAL_STATUS_ENUMS.STUB_CREATED_);
            //Need this to force auto-numbering of the primary keys.
            //Which will be necessary for joining columns
            ses.save(cur);
            
            //put the current trial record into collection:
            trials.set(i, cur);
        }//next i
        
        //return the list of trials.
        return trials;
        
    }//FUNC::END
    
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = TrialTransUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
  
    
}//CLASS::END
