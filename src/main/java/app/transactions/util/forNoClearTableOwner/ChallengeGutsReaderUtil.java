package app.transactions.util.forNoClearTableOwner;

import app.MyError;
import app.config.constants.signatures.paramVals.TRIAL_KIND_ENUMS;
import app.dbDataAbstractions.entities.bases.ChallengeGuts;
import app.transactions.util.tables.deck.DeckReadUtil;
import app.transactions.util.tables.kinda.KindaReadUtil;

/**
 * Responsible for finding and de-serializing the core of a test.
 * By core, I mean guts. By guts, I mean just the questions, without any
 * attached meta data. The guts are what you need if you want to populate
 * a UI with test questions.
 * 
 * @author jmadison:2015.11.01(November,1st,Year2015.Sunday)
 */
public class ChallengeGutsReaderUtil {
    
    /**
     * 
     * @param kind_of_trial :Synonymous to "test type". Tells you what sort
     *                       of information this trial will be testing you on.
     * @param challenge_id  :A unique handle to a specific trial of the kind
     *                       specified. Will pull data from different table
     *                       depending on what kind_of_trial is.
     *                       AKA: challenge_id is only unique for a given trial
     *                       type.
     * @return 
     */
    public static ChallengeGuts findAndRead
                                      (String kind_of_trial, long challenge_id){
        
        //error check: Is trial kind valid?
        TRIAL_KIND_ENUMS.assertTrialKindIsValid(kind_of_trial);
        
        ChallengeGuts op = null;
                                 
        //Basic logic here:
        //the challenge_id is a PRIMARY FORIEGN KEY. To what table
        //depends on what kind_of_trial it is.
        String k = kind_of_trial;
        if(k.equals(TRIAL_KIND_ENUMS.RIDDLE_TRIAL_)){
            op = DeckReadUtil.readUsingPrimaryKeyID(challenge_id);
        }else{
            doError("unsupported trial kind found");
        }
               
        //return the challenge guts found:
        if(null == op){doError("[Null challenge guts being returned]");}
        return op;
                                          
    }//FUNC::END
                                      
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = ChallengeGutsReaderUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
