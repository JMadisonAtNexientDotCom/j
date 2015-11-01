package app.config.debug;

import app.config.constants.EntityErrorCodes;
import app.config.constants.identifiers.FuncNameReg;
import app.config.constants.identifiers.TableNameReg;
import app.config.constants.identifiers.VarNameReg;
import utils.generalDebugUtils.ConstNameRegDebugUtil;

/**
 * NOTE: Even though checking in CONFIG servlet... Errors are very hard to
 *       notice when they happen. Even though I am throwing them without
 *       handling them... Maybe have a GLOBAL ERROR STATE that will check
 *       flag and if that flag is TRUE the program will crash?
 *       So there are multiple chances to notice problem.
 * 
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
        boolean t3;
        boolean t4;
        t1 = ConstNameRegDebugUtil.validateStaticVars(VarNameReg.class,er);
        t2 = ConstNameRegDebugUtil.validateStaticVars(FuncNameReg.class,er);
        t3 = ConstNameRegDebugUtil.validateStaticVars(EntityErrorCodes.class, er);
        t4 = ConstNameRegDebugUtil.validateStaticVars(TableNameReg.class, er);
  
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
        if(false == t3){return false;}
        if(false == t4){return false;}
        return true;
        
    }//FUNC::END
}//CLASS::END
