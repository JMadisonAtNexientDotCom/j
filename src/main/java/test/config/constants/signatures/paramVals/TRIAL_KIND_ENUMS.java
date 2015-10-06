package test.config.constants.signatures.paramVals;

import annotations.Verbatim;
import annotations.VerbatimValidatorUtil;
import test.config.constants.apiDocs.MasterApiDoc;
import test.config.constants.identifiers.VarNameReg;

/**
 * Valid arguments values for TRIAL_KIND parameter.
 * @author jmadison
 */
@Verbatim(name=VarNameReg.TRIAL_KIND + "_ENUMS")
public class TRIAL_KIND_ENUMS {
    
    static{////////
        doStaticInit();
    }//////////////
    
    private static void doStaticInit(){
        VerbatimValidatorUtil.validateAnnotatedFields(TRIAL_KIND_ENUMS.class);
    }//FUNC::END
    
    public int INIT_FAILURE = 0;
    public int RIDDLE_TRIAL = 1; //<--Only supported trial type for now.
  
}//CLASS::END
