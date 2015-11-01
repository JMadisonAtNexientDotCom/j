package frontEndBackEndIntegration.childComponents;

import app.config.constants.signatures.paramVals.TRIAL_KIND_ENUMS;

/**
 * A registry holding parameter ENUMS and other valid values that can
 * be supplied to API calls.
 * @author jmadison :2015.10.05
 */
public class ApiParamValuesRegistry {
    
    public ApiParamValuesRegistry(){
        //Check may not be able to work within static initializer:
        TRIAL_KIND_ENUMS.validateClass();
    }//FUNC::END
    
    public TRIAL_KIND_ENUMS TRIAL_KIND = new TRIAL_KIND_ENUMS(); 
    
}//CLASS::END
