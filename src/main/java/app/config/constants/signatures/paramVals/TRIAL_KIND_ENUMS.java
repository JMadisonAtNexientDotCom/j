package app.config.constants.signatures.paramVals;

import annotations.UniqueInstanceValue;
import annotations.UniqueStaticValue;
import annotations.UniqueValueValidator;
import annotations.Verbatim;
import annotations.VerbatimValidatorUtil;
import java.util.List;
import app.MyError;
import app.config.constants.apiDocs.MasterApiDoc;
import app.config.constants.identifiers.VarNameReg;

/**
 * NOTE: Decided to use STRINGS as enums. Because more human readable.
 *       And we don't need to worry about optimizations.
 * 
 * 
 * Valid arguments values for TRIAL_KIND parameter.
 * Should be of type long since used as keys in database.
 * @author jmadison :2015.10.14: (October 14th, Year 2015, Wednesday)
 */
@Verbatim(name=VarNameReg.TRIAL_KIND + "_ENUMS")
public class TRIAL_KIND_ENUMS {
    
    /** To help avoid infinite recursion. **/
    private static boolean _staticInitAlreadyCalled = false;
    
    static{////////
        doStaticInit();
    }//////////////
    
    private static void doStaticInit(){
        //validate might need to be done OUTSIDE of class.
    }//
    
    /** Returns true if the string represents a valid enum. **/
    public static boolean isEnumValid(String enumString){
        
        if(TRIAL_KIND_ENUMS.RIDDLE_TRIAL_.equals(enumString)){
            return true;
        }//End cases.
        
        return false;
    }//FUNC::END
    
    public static void validateClass(){
        
        //To help avoid infinite recursion:
        if(_staticInitAlreadyCalled){ return;}
        _staticInitAlreadyCalled = true;
        
        VerbatimValidatorUtil.validateAnnotatedFields(TRIAL_KIND_ENUMS.class);
        
        //Possible infinite recursion:
        Class[] clazz = new Class[1];
        clazz[0] = String.class;
   
        List<String> insts = UniqueValueValidator.validateInstanceTypes
                                        (TRIAL_KIND_ENUMS.class, clazz);
        List<String> stats = UniqueValueValidator.validateStaticTypes
                                        (TRIAL_KIND_ENUMS.class, clazz);
        
        if(insts.isEmpty() || stats.isEmpty()){
            doError("[Nothing collected!!! But validation passed... Hmm..]");
        }//Error
        
        if(insts.size() != stats.size()){
            doError("[Should have same amount of instance as static vars.]");
        }//Error.
        
        if(stats.size() != ENUM_CHECKSUM){
            String msg = "";
            msg += "[CHECKSUM OFF! Update checksum, or fix types of enums.]";
            msg += "Num insts:[" + Integer.toString(insts.size()) + "]";
            msg += "Num stats:[" + Integer.toString(stats.size()) + "]";
            doError(msg);
        }//Error
     
        //UniqueValueValidator.validateStaticTypes(TRIAL_STATUS_ENUMS.class);
        //UniqueValueValidator.validateInstanceTypes(TRIAL_STATUS_ENUMS.class);
    }//FUNC::END
    
    /** Checksum telling us how many enums should be in this class. **/
    private static final long ENUM_CHECKSUM = 3;
    
    
    //These enums are NOT considered valid.
    //Only used in error objects and non-initialized data:
    ////////////////////////////////////////////////////////////////////////////
    @UniqueStaticValue
    public final static String INIT_FAILURE_ = 
                              "init_failure";
    
    @UniqueStaticValue
    public final static String DO_NOT_PERSIST_ = 
                              "do_not_persist";
    ////////////////////////////////////////////////////////////////////////////
    
    @UniqueStaticValue
    public final static String RIDDLE_TRIAL_ = 
                              "riddle_trial";
    
    @UniqueInstanceValue
    public final String INIT_FAILURE   = INIT_FAILURE_;
    @UniqueInstanceValue
    public final String DO_NOT_PERSIST = DO_NOT_PERSIST_;
    @UniqueInstanceValue
    public final String RIDDLE_TRIAL   = RIDDLE_TRIAL_; //<--Only supported trial type for now.
    
    public static void assertTrialKindIsValid(String kind_of_trial){
        String k = kind_of_trial;
        if(k.equals(RIDDLE_TRIAL_)){return;}
        
        String msg = "[Kind of trial found is not valid.]";
        msg+="[kind_of_trial==[" + kind_of_trial + "]]";
        doError(msg);
    }//FUNC::END
  
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = TRIAL_KIND_ENUMS.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
