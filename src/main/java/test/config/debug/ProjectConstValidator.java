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
    
   
    
    /**
     * Validates our constants and makes sure the names of our constants
     * actually match the VALUES of our constants.
     * EXAMPLE: public static final String SOME_CONST = "some_const";
     * @return : Returns TRUE if everything is alright.
     */
    public static boolean validate(){
        boolean er = false;
        boolean t1;
        boolean t2;
        t1 = ConstNameRegDebugUtil.validateStaticVars(VarNameReg.class,er);
        t2 = ConstNameRegDebugUtil.validateStaticVars(FuncNameReg.class,er);
  
        /*XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        I would like to wreck these values on error. But they
        need to be FINAL and thus immutable.
        if(false == t1){//WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
            VarNameReg.wreckIt("[ProjectConstValidator::ERROR]");
        }//WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
        
        if(false == t2){//WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
            FuncNameReg.wreckIt("[ProjectConstValidator::ERROR]");
        }//WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
        XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX*/
        
        if(false == t1){return false;}
        if(false == t2){return false;}
        return true;
        
    }//FUNC::END
}//CLASS::END
