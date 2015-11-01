package app.config.constants.signatures.paramVals;

import annotations.UniqueInstanceValue;
import annotations.UniqueStaticValue;
import annotations.UniqueValueValidator;
import annotations.Verbatim;
import annotations.VerbatimStringConst;
import annotations.VerbatimStringConstValidatorUtil;
import annotations.VerbatimValidatorUtil;
import java.util.List;
import app.MyError;

/**
 * Design note update:2015.10.29(Oct29th,Year2015)
 * Have decided to use STRINGS rather than numbers.
 * The readability of entries in the database outweighs
 * the ability to refactor and optimizations that would come
 * with using longs.
 * 
 * Design note: There are static and instance versions of these variables
 * because we also instantiate this object in order to create auto-completion
 * for the .JSP expressions in the user-interface code.
 * @author jmadison :2015.10.06
 */
public class TRIAL_STATUS_ENUMS {
    
    /** To help avoid infinite recursion. **/
    private static boolean _classValidationDone = false;
    
    static{//////////////
        doStaticInit();
    }////////////////////
    
    private static void doStaticInit(){
        //Do nothing. Validation inside static block is not working.
        
        //trying again.
        validateClass();
    }//FUNC::END
    
    /** Validates that class is setup properly.
     *  Does not seem to work if called from this class (itself) **/
    public static void validateClass(){
        //To stop validation from happening more than once.
        //Also could avoid infinite recursion.
        if(_classValidationDone){ return;}
        _classValidationDone = true;
        
        //Possible inifinte recursion:
        Class[] clazz = new Class[1];
        clazz[0] = String.class;
       
        List<Long> insts = UniqueValueValidator.validateInstanceTypes
                                        (TRIAL_STATUS_ENUMS.class, clazz);
        List<Long> stats = UniqueValueValidator.validateStaticTypes
                                        (TRIAL_STATUS_ENUMS.class, clazz);
        
        if(insts.size() != ENUM_CHECKSUM){doError("[Insts checksum fail]");}
        if(stats.size() != ENUM_CHECKSUM){doError("[Stats checksum fail]");}
        
        //validate the @VerbatimStringConst values:
        VerbatimStringConstValidatorUtil.validateAllAnnotatedFields
                                                     (TRIAL_STATUS_ENUMS.class);
        
        //UniqueValueValidator.validateStaticTypes(TRIAL_STATUS_ENUMS.class);
        //UniqueValueValidator.validateInstanceTypes(TRIAL_STATUS_ENUMS.class);
    }//FUNC::END
    
    /** Checksum set to total number of enums in this file. **/
    private final static long ENUM_CHECKSUM = 9;
    
    /** "_" at end because should match instance vars.
     *  For documentation, see instance variable version. **/
    @UniqueStaticValue
    @VerbatimStringConst(nameMod=VerbatimStringConst.TRIM_UNDERSCORES) //1
    public static final String INIT_ERROR_ =   "INIT_ERROR";
    /** "_" at end because should match instance vars.
     *  For documentation, see instance variable version. **/
    @UniqueStaticValue
    @VerbatimStringConst(nameMod=VerbatimStringConst.TRIM_UNDERSCORES) //2
    public static final String STUB_CREATED_ = "STUB_CREATED";
    /** "_" at end because should match instance vars.
     *  For documentation, see instance variable version. **/
    @UniqueStaticValue
    @VerbatimStringConst(nameMod=VerbatimStringConst.TRIM_UNDERSCORES) //3
    public static final String CREATED_LINKED_ = "CREATED_LINKED";
    /** "_" at end because should match instance vars.
     *  For documentation, see instance variable version. **/
    @UniqueStaticValue
    @VerbatimStringConst(nameMod=VerbatimStringConst.TRIM_UNDERSCORES) //4
    public static final String KNOWNOF_        = "KNOWNOF";
    /** "_" at end because should match instance vars.
     *  For documentation, see instance variable version. **/
    @UniqueStaticValue
    @VerbatimStringConst(nameMod=VerbatimStringConst.TRIM_UNDERSCORES) //5
    public static String CONFIRMED_            = "CONFIRMED";
    /** "_" at end because should match instance vars.
     *  For documentation, see instance variable version. **/
    @UniqueStaticValue
    @VerbatimStringConst(nameMod=VerbatimStringConst.TRIM_UNDERSCORES) //6
    public static final String PROGRESS_       = "PROGRESS";
    /** "_" at end because should match instance vars.
     *  For documentation, see instance variable version. **/
    @UniqueStaticValue
    @VerbatimStringConst(nameMod=VerbatimStringConst.TRIM_UNDERSCORES) //7
    public static final String EXPIRED_COMPLETED_ ="EXPIRED_COMPLETED";
    /** "_" at end because should match instance vars.
     *  For documentation, see instance variable version. **/
    @UniqueStaticValue
    @VerbatimStringConst(nameMod=VerbatimStringConst.TRIM_UNDERSCORES) //8
    public static final String EXPIRED_TIME_OUT_  ="EXPIRED_TIME_OUT";
    /** "_" at end because should match instance vars.
     *  For documentation, see instance variable version. **/
    @UniqueStaticValue
    @VerbatimStringConst(nameMod=VerbatimStringConst.TRIM_UNDERSCORES) //9
    public static final String EXPIRED_COMPROMISED_="EXPIRED_COMPROMISED";
    
    /** If zero, is initialization error. Integers initialize at zero.
     *  So we DONT want to use an enum of zero. As it could make error
     *  hard to find. **/
    @UniqueInstanceValue
    @VerbatimStringConst()
    public final String INIT_ERROR     = TRIAL_STATUS_ENUMS.INIT_ERROR_; //1
    
    /** New trial has been created. But has not been linked to ANYTHING.
     *  Not a test, not a token. It is a stub. **/
    @UniqueInstanceValue
    @VerbatimStringConst()
    public final String STUB_CREATED   = TRIAL_STATUS_ENUMS.STUB_CREATED_; //2
    
    /** New trial has been created. And the token has been linked
     *  to it. But the token has not been confirmed to be used. **/
    @UniqueInstanceValue
    @VerbatimStringConst()
    public final String CREATED_LINKED = TRIAL_STATUS_ENUMS.CREATED_LINKED_; //3
    
    /** Known of: Someone has entered the token. But has not said
     *  "YES" to the "Is this you?" confirmation page that comes after
     *  entering a token. **/
    @UniqueInstanceValue
    @VerbatimStringConst()
    public final String KNOWNOF        = TRIAL_STATUS_ENUMS.KNOWNOF_; //4
    
    /** The ninja has confirmed that the token they entered has been linked
     *  to their identity. **/
    @UniqueInstanceValue
    @VerbatimStringConst()
    public final String CONFIRMED      = TRIAL_STATUS_ENUMS.CONFIRMED_; //5
    
    /** The ninja has hit [START] to start the test. After being given
     *  a brief overview of what the test covers and the time allotted. **/
    @UniqueInstanceValue
    @VerbatimStringConst()
    public final String PROGRESS       = TRIAL_STATUS_ENUMS.PROGRESS_; //6
    
    /** Token is expired, because ninja has completed trial. **/
    @UniqueInstanceValue
    @VerbatimStringConst()                                             //7
    public final String EXPIRED_COMPLETED=TRIAL_STATUS_ENUMS.EXPIRED_COMPLETED_;
    
    /** Token is expired, because ninja has FAILED TO COMPLETE TEST WITHIN
     *  the alloted amount of time. **/
    @UniqueInstanceValue
    @VerbatimStringConst()                                             //8
    public final String EXPIRED_TIME_OUT=TRIAL_STATUS_ENUMS.EXPIRED_TIME_OUT_;
    
    /** Token is expired, because someone hit "NO" when the confirmation page
     *  came up. Meaning ANOTHER ninja has access to a token that does NOT
     *  belong to them. And they were nice enough to say, "Hey! This is not me!
     *  We cannot trust this token anymore. However, we need to give the ninja
     *  who was supposed to use this token another test. **/
    @UniqueInstanceValue
    @VerbatimStringConst()
    public final String EXPIRED_COMPROMISED =                          //9
                                        TRIAL_STATUS_ENUMS.EXPIRED_COMPROMISED_;
    
    /*--------------------------------------------------------------------------
    ** Would activating a trial with this status count as RE-activating?
    ** @param inStatus :One of the status enums.
    ** @Returns: Returns TRUE if the act of ACTIVATING a trial
    **           with this status code would be considered
    **           RE-Activation.
    -*-----------------------------------------------------------------------**/
    public static boolean isPossibleToReActivateFromThisStatus(String inStatus){
    
        String s = inStatus;
        if(null == s || s.equals("")){ doError("[s,invalid enum state!!]");}
        
        //Trial has just been created.
        if(CREATED_LINKED_.equals(s)){ return false;}
        
        //Some Ninja out there knows of this trial token's existance.
        if(KNOWNOF_.equals(s)){ return false;}
        
        //Identity confirmed, but test not started yet:
        if(CONFIRMED_.equals(s)) { return false;}
        
        ////////////////////////////////////////////////////////////////////////
        //Counts as re-activation below:RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
        if(PROGRESS_           .equals(s)){ return true;}
        if(EXPIRED_TIME_OUT_   .equals(s)){ return true;}
        if(EXPIRED_COMPLETED_  .equals(s)){ return true;}
        if(EXPIRED_COMPROMISED_.equals(s)){ return true;}
        
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


//34567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
//                                                                            80                 100