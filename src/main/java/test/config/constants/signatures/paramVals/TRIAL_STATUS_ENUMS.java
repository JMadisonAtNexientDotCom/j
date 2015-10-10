package test.config.constants.signatures.paramVals;

import annotations.UniqueInstanceValue;
import annotations.UniqueStaticValue;
import annotations.UniqueValueValidator;
import annotations.VerbatimValidatorUtil;
import test.MyError;

/**
 * Design note: There are static and instance versions of these variables
 * because we also instantiate this object in order to create auto-completion
 * for the .JSP expressions in the user-interface code.
 * @author jmadison :2015.10.06
 */
public class TRIAL_STATUS_ENUMS {
    
    /** To help avoid infinite recursion. **/
    private static boolean _staticInitAlreadyCalled = false;
    
    static{//////////////
        doStaticInit();
    }////////////////////
    
    private static void doStaticInit(){
        
        //To help avoid infinite recursion:
        if(_staticInitAlreadyCalled){ return;}
        _staticInitAlreadyCalled = true;
        
        //Possible inifinte recursion:
        Class[] clazz = new Class[2];
        clazz[0] = Long.class;
        clazz[1] = long.class;
        UniqueValueValidator.validateInstanceTypes
                                        (TRIAL_STATUS_ENUMS.class, clazz);
        UniqueValueValidator.validateStaticTypes
                                        (TRIAL_STATUS_ENUMS.class, clazz);
        
        //UniqueValueValidator.validateStaticTypes(TRIAL_STATUS_ENUMS.class);
        //UniqueValueValidator.validateInstanceTypes(TRIAL_STATUS_ENUMS.class);
        
    }//FUNC::END
    
    /** "_" at end because should match instance vars.
     *  For documentation, see instance variable version. **/
    @UniqueStaticValue
    public static int INIT_ERROR_          = 0;
    /** "_" at end because should match instance vars.
     *  For documentation, see instance variable version. **/
    @UniqueStaticValue
    public static int CREATED_             = 1;
    /** "_" at end because should match instance vars.
     *  For documentation, see instance variable version. **/
    @UniqueStaticValue
    public static int KNOWNOF_             = 2;
    /** "_" at end because should match instance vars.
     *  For documentation, see instance variable version. **/
    @UniqueStaticValue
    public static int CONFIRMED_           = 3;
    /** "_" at end because should match instance vars.
     *  For documentation, see instance variable version. **/
    @UniqueStaticValue
    public static int PROGRESS_            = 4;
    /** "_" at end because should match instance vars.
     *  For documentation, see instance variable version. **/
    @UniqueStaticValue
    public static int EXPIRED_COMPLETED_   = 5;
    /** "_" at end because should match instance vars.
     *  For documentation, see instance variable version. **/
    @UniqueStaticValue
    public static int EXPIRED_TIME_OUT_    = 6;
    /** "_" at end because should match instance vars.
     *  For documentation, see instance variable version. **/
    @UniqueStaticValue
    public static int EXPIRED_COMPROMISED_ = 7;
    
    /** If zero, is initialization error. Integers initialize at zero.
     *  So we DONT want to use an enum of zero. As it could make error
     *  hard to find. **/
    @UniqueInstanceValue
    public final int INIT_ERROR         = TRIAL_STATUS_ENUMS.INIT_ERROR_;
    
    /** New trial has been created. And the token has been linked
     *  to it. But the token has not been confirmed to be used. **/
    @UniqueInstanceValue
    public final int CREATED            = TRIAL_STATUS_ENUMS.CREATED_;
    
    /** Known of: Someone has entered the token. But has not said
     *  "YES" to the "Is this you?" confirmation page that comes after
     *  entering a token. **/
    @UniqueInstanceValue
    public final int KNOWNOF            = TRIAL_STATUS_ENUMS.KNOWNOF_;
    
    /** The ninja has confirmed that the token they entered has been linked
     *  to their identity. **/
    @UniqueInstanceValue
    public final int CONFIRMED          = TRIAL_STATUS_ENUMS.CONFIRMED_;
    
    /** The ninja has hit [START] to start the test. After being given
     *  a brief overview of what the test covers and the time allotted. **/
    @UniqueInstanceValue
    public final int PROGRESS           = TRIAL_STATUS_ENUMS.PROGRESS_;
    
    /** Token is expired, because ninja has completed trial. **/
    @UniqueInstanceValue
    public final int EXPIRED_COMPLETED  = TRIAL_STATUS_ENUMS.EXPIRED_COMPLETED_;
    
    /** Token is expired, because ninja has FAILED TO COMPLETE TEST WITHIN
     *  the alloted amount of time. **/
    @UniqueInstanceValue
    public final int EXPIRED_TIME_OUT   = TRIAL_STATUS_ENUMS.EXPIRED_TIME_OUT_;
    
    /** Token is expired, because someone hit "NO" when the confirmation page
     *  came up. Meaning ANOTHER ninja has access to a token that does NOT
     *  belong to them. And they were nice enough to say, "Hey! This is not me!
     *  We cannot trust this token anymore. However, we need to give the ninja
     *  who was supposed to use this token another test. **/
    @UniqueInstanceValue
    public final int EXPIRED_COMPROMISED = 
                                        TRIAL_STATUS_ENUMS.EXPIRED_COMPROMISED_;
    
    /*--------------------------------------------------------------------------
    ** Would activating a trial with this status count as RE-activating?
    ** @param inStatus :One of the status enums.
    ** @Returns: Returns TRUE if the act of ACTIVATING a trial
    **           with this status code would be considered
    **           RE-Activation.
    -*-----------------------------------------------------------------------**/
    public static boolean isPossibleToReActivateFromThisStatus(long inStatus){
    
        if(inStatus <= 0){ doError("[invalid enum state!!]");}
        
        //Trial has just been created.
        if(CREATED_  == inStatus){ return false;}
        
        //Some Ninja out there knows of this trial token's existance.
        if(KNOWNOF_  == inStatus){ return false;}
        
        //Identity confirmed, but test not started yet:
        if(CONFIRMED_==inStatus) { return false;}
        
        ////////////////////////////////////////////////////////////////////////
        //Counts as re-activation below:RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
        if(PROGRESS_           == inStatus){ return true;}
        if(EXPIRED_TIME_OUT_   == inStatus){ return true;}
        if(EXPIRED_COMPLETED_  == inStatus){ return true;}
        if(EXPIRED_COMPROMISED_== inStatus){ return true;}
        
        //If we get to here, it means that we have an unaccounted for enum:
        doError("[Unaccounted for Trial Status Enum!]");
        return false;
        
    }//FUNC::END
    
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = TRIAL_STATUS_ENUMS.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
