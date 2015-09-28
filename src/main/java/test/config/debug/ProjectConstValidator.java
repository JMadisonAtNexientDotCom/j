package test.config.debug;

import test.config.constants.identifiers.FuncNameReg;
import test.config.constants.identifiers.VarNameReg;
import utils.generalDebugUtils.ConstNameRegDebugUtil;

/**
 * From experimentation, does not look like static registry classes are
 * able to validate themselves during initialization. So this class will
 * do it!
 * @author jmadison
 */
public class ProjectConstValidator {
    
    public static void validate(){
        ConstNameRegDebugUtil.validateStaticVars(VarNameReg.class);
        ConstNameRegDebugUtil.validateStaticVars(FuncNameReg.class);
    }//FUNC::END
}//CLASS::END
