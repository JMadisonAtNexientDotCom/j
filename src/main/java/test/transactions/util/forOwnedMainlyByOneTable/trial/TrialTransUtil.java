package test.transactions.util.forOwnedMainlyByOneTable.trial;

import java.util.List;
import org.hibernate.Session;
import primitives.RealAndFakeIDs;
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
        
        //Step1: Make list into unique entries, then split list into TWO
        //       lists. A list of valid ninja_id (the ninja is in system)
        //       And list of invalid ninjas. (Ones not in the system.)
        RealAndFakeIDs idSplit = 
                     NinjaTransUtil.sortNinjaIDS_IntoRealAndFake(ninja_id_list);
        
        //Step2: Go through the REAL LIST. And create tests and link to tokens.
        Long cur;
        Ticket tik;
        Coffer cof = new Coffer();
        int len = idSplit.real.size();
        for(int r = 0; r < len; r++){
            cur = idSplit.real.get(r);
            tik = dispatchSingleToken( cur );
            cof.tickets.add(tik);
        }//NEXT i (real ticket)
        
        //Step3: Append FAKE/Error tickets to the end:
        int fake_len = idSplit.fake.size();
        String num;
        for(int f = 0; f < fake_len; f++){
            cur = idSplit.fake.get(f);
            num = Long.toString(cur);
            tik = Ticket.makeErrorTicket("ERROR:FAKE_NINJA#" + num);
            cof.tickets.add(tik);
        }//NEXT f (fake ticket)
        
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
        */
        
        return op;
        
    }//FUNC::END
    
    /**
     * Creates a trial and dispatches a token for that trial.
     * Associates the token with the [trial/test] and the [ninja/candidate]
     * @param inNinjaID :An id of ninja that we ASSUME EXISTS.
     * @return :Return a ticket that contains the token_hash, ninja_name,
     *          and ninja_id. Just enough info for both UI and HUMAN to confirm
     *          that the token_hash made applies to the ninja in question.
     */
    public static Ticket dispatchSingleToken(Long inNinjaID){
        
        //1: Create a new token_table record:
        //   Do session.save() on it to forc the id to auto-incriment.
        //   NOTE: This token will not exist in SESSION TABLE until the
        //   ninja has activated it by using it to start test.
        
        //2: Create a new trial_table record.
        //   Make sure ninja's ID is in the record so we known trial belongs
        //   to that ninja.
        
        //3: Pack up a Ticket object with all necessary info:
        //   You will need to fetch the Ninja entity using the ID so you
        //   can put the ninja's name into the ticket.
        
        //4: Return the ticket!
        
        
    }//FUNC::END
    
    /**
     * Take a token that is associated with a ninja+test and use it
     * to create an entry in the session table.
     * 
     * Edge cases:
     * 1: Session already exists: 
     *    Just return amount of time remaining.
     *    But do not alter session.
     * 2: Token already activated, and time has expired:
     *    Return 0 because time is up.
     * @param inTokenID :The token that should be associated with test.
     * @return:Returns the time remaining for the test that was just
     *         activated using the tokenID.
     *         IF ERROR: Returns NEGATIVE NUMBER.
     *         Errors that can happen: 
     *         1: TokenID does not exist for test. AKA: Token invalid.
     */
    public static int activateTrialToken(Long inTokenID){
        
        boolean confirmed = confirmTokenIsLinkedToTrial(inTokenID);
        if(false == confirmed){ return (-1); } //token is invalid for trial.
                                               //it is not represented anywhere
                                               //in trial table. Possibly it 
                                               //does not exist at all.
        //If the token is confirmed to exist.
        //We need to either:
        //1: POSSIBLY activate it by putting it into session table.
        //2: Return time remaining.
        Long op = activateTokenIfNotAlreadyAndReturnTimeRemainingForTrial();
        
    }//FUNC::END
    
    /**
     * Activates a token if not already activated.
     * Regardless, will return amount of time remaining for
     * the test it is associated with.
     * WILL THROW ERROR IF TOKEN IS NOT ASSOCIATED WITH ANY TRIAL.
     * WILL NOT-RE-ACTIVATE an EXPIRED token.
     * @return :Amount of time remaining in milliseconds for trial
     *          associated with inputted token_id.
     */
    public static long activateTrialTokenIfNot(long token_id){
        
        TrialTable tt = getTrialUsingTokenID(token_id);
        long status = tt.getStatus();
        
        if(0 == status){
            String msg = "[Error: ZERO IS BAD.]"
            msg += "[Lazy fetch error or forgot to init as created]";
            doError(msg);
        }else
        if(status < 0){
            doError("[How did you get NEGATIVE status code??]");
        }//BLOCK END.
        
        if(status == TRIAL_STATUS_ENUMS.CREATED_ ||
           status == TRIAL_STATUS_ENUMS.KNOWNOF_  ){
           activateTrialUsingEntity(tt);
        }//CONFIRM THE TOKEN IF IN ONE OF THE STATES COVERED ABOVE ^^
         
    }//FUNC::END
    
    /**
     * Puts token associated with Trial(test) record into the session table.
     * And flags the Trial as in progress.
     * Once this is done, the Ninja with the Trial Token will be able to
     * gain access to the Trial area and begin the battle of wits.
     * @param tt :The TrialTable instance.
     * @param allowReActivation: If true, re-activation is NOT an error.
     * @return :How much allotted time is remaining for this Trial.
     */
    public static long activateTrialUsingEntity
                                     (TrialTable tt, boolean allowReActivation){
        
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();
        long status;
        
        if(false == allowReActivation){
            status = tt.getStatus();
            if(TRIAL_STATUS_ENUMS.isPossibleToReActivateFromThisStatus(status)){
                String msg = "[Attempt to re-activate token..]";
                msg += "[Without use of allowReActivation flag.]";
                doError(msg);
            }//RE-ACTIVATION ERROR?
        }//RE-ACTIVATION not allowed?
                         
        //Set status of test to inprogress.-------------------------------------
        //Also set the time at which token was activated.-----------------------
        //----------------------------------------------------------------------
        //you need to get session object and SAVE in order to gaurantee---------
        //that the information is persisted.------------------------------------
        status = TRIAL_STATUS_ENUMS.PROGRESS_; //now in progress.
        tt.setStatus(status);
        long curTimeMs = System.currentTimeMillis();
        tt.setBegan_on( curTimeMs );
        tt.setEnded_on( curTimeMs ); //<--Better than zero.
        long allotted = tt.getAllotted();
        if(allotted <= 0){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            doError("[Allotted time should be positive milliseconds!]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        ses.save(tt);
        
        //Now that Trial is activated, we need to put the token into the
        //Session table. If trying to re-activate a session that is ACTIVELY
        //Owned by ADMIN, we will have a fatal error.
        long tokenID = tt.getToken_id();
        if(tokenID <= 0){doError("[Possible lazy fetch error]");}
        OwnerSessionTransUtil.makeOrReActivateSessionByTokenID_NINJA
                                                             (tokenID,allotted);
        
        //Return the alloted time for the trial:
        return allotted;
        
    }//FUNC::END
    
}//CLASS::END
