package test.config.constants.signatures.paramVals;

import annotations.UniqueInstanceValue;
import annotations.UniqueStaticValue;
import annotations.UniqueValueValidator;
import annotations.Verbatim;
import annotations.VerbatimValidatorUtil;
import java.util.List;
import test.MyError;
import test.config.constants.apiDocs.MasterApiDoc;
import test.config.constants.identifiers.VarNameReg;

/**
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
    
    public static void validateClass(){
        
        //To help avoid infinite recursion:
        if(_staticInitAlreadyCalled){ return;}
        _staticInitAlreadyCalled = true;
        
        VerbatimValidatorUtil.validateAnnotatedFields(TRIAL_KIND_ENUMS.class);
        
        //Possible infinite recursion:
        Class[] clazz = new Class[2];
        clazz[0] = Long.class;
        clazz[1] = long.class;
        List<Long> insts = UniqueValueValidator.validateInstanceTypes
                                        (TRIAL_KIND_ENUMS.class, clazz);
        List<Long> stats = UniqueValueValidator.validateStaticTypes
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
    private static final long ENUM_CHECKSUM = 2;
    
    @UniqueStaticValue
    public final static long INIT_FAILURE_ = 0;
    
    @UniqueStaticValue
    public final static long RIDDLE_TRIAL_ = 1;
    
    @UniqueInstanceValue
    public final long INIT_FAILURE = INIT_FAILURE_;
    @UniqueInstanceValue
    public final long RIDDLE_TRIAL = RIDDLE_TRIAL_; //<--Only supported trial type for now.
  
    
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
