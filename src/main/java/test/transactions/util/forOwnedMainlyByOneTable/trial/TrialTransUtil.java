package test.transactions.util.forOwnedMainlyByOneTable.trial;

import java.util.List;
import test.dbDataAbstractions.requestAndResponseTypes.postTypes.postResponse.Coffer;

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
        
        //STUB FOR NOW SO THAT WE CAN GO HOME AND LEAVE CODE BASE IN TACT:
        String msg ="[DispatchTokens is meerly a stub method for now]";
        Coffer op = Coffer.makeErrorCoffer(msg);
        return op;
        
    }//FUNC::END
    
}//CLASS::END
