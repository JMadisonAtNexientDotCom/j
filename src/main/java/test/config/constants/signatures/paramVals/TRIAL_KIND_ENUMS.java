package test.config.constants.signatures.paramVals;

import annotations.UniqueInstanceValue;
import annotations.UniqueStaticValue;
import annotations.UniqueValueValidator;
import annotations.Verbatim;
import annotations.VerbatimValidatorUtil;
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
        UniqueValueValidator.validateInstanceTypes
                                        (TRIAL_STATUS_ENUMS.class, clazz);
        UniqueValueValidator.validateStaticTypes
                                        (TRIAL_STATUS_ENUMS.class, clazz);
     
        //UniqueValueValidator.validateStaticTypes(TRIAL_STATUS_ENUMS.class);
        //UniqueValueValidator.validateInstanceTypes(TRIAL_STATUS_ENUMS.class);
    }//FUNC::END
    
    @UniqueStaticValue
    public final static long INIT_FAILURE_ = 0;
    
    @UniqueStaticValue
    public final static long RIDDLE_TRIAL_ = 1;
    
    @UniqueInstanceValue
    public final long INIT_FAILURE = INIT_FAILURE_;
    @UniqueInstanceValue
    public final long RIDDLE_TRIAL = RIDDLE_TRIAL_; //<--Only supported trial type for now.
  
}//CLASS::END
