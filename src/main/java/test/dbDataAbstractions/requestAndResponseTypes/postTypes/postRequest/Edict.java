
package test.dbDataAbstractions.requestAndResponseTypes.postTypes.postRequest;

import annotations.Verbatim;
import annotations.VerbatimValidatorUtil;
import java.util.ArrayList;
import java.util.List;
import primitives.BooleanWithComment;
import test.MyError;
import test.config.constants.EntityErrorCodes;
import test.config.constants.identifiers.VarNameReg;
import test.config.constants.signatures.paramVals.TRIAL_KIND_ENUMS;
import utils.JSONUtil;
import utils.MapperUtil;

/**
 * *****************************************************************************
 * DEFINE: Edict:
 * "An official order or proclamation issued by a person in authority."
 * *****************************************************************************
 * 
 * An edict is an order from the administrator to dispatch tokens for
 * ninjas. The number values must be of type "long" in order to avoid
 * loss of precision. Since these values are used as database record values.
 * 
 * 
 * @author jmadison :2015.10.04
 */
public class Edict extends PostRequestType {
    
    static{////////
        doStaticInit();
    }//////////////
    
    private static void doStaticInit(){
        VerbatimValidatorUtil.validateAnnotatedFields(Edict.class);
    }//FUNC::END
    
    
    /** The id's of the ninjas we would like to create [trial/tests]
     *  for and dispatch tokens for. Tokens being used to access the
     *  [trial/tests] **/
    @Verbatim(name=VarNameReg.NINJA_ID_LIST)
    public List<Long> ninja_id_list      = new ArrayList<Long>();
    
    /** The enum representing the kind of test to be taken. **/
    @Verbatim(name=VarNameReg.TRIAL_KIND)
    public String   trial_kind      = TRIAL_KIND_ENUMS.INIT_FAILURE_;
    
    /** The time alloted for our Ninjas (Candidates) to complete the 
     *  test once it has began. **/
    @Verbatim(name=VarNameReg.DURATION_IN_MINUTES)
    public long   duration_in_minutes = (-1); //MUST BE LONG
    
    /**
     * Validate object without crashing.
     * @param ed :The Edict to evaluate integrity of
     *            as a valid input.
     * @return: Returns a TRUE if valid. False if not.
     */
    public static BooleanWithComment validate(Edict ed){
        BooleanWithComment stat = new BooleanWithComment();
        stat.comment = "";
        stat.value = true; //<--valid unless invalidated by bad data found.
        if(null == ed.ninja_id_list){
            stat.comment += "[Edict was null.]";
            stat.value    = false;
            return stat;
        }else
        if(null != ed.ninja_id_list){
            if(ed.duration_in_minutes <= 0)
                       { stat.value = false; stat.comment = "[duration <= 0]"; }
            if(ed.ninja_id_list.size()<= 0)
                       { stat.value = false; stat.comment = "[list.size()<=0]";}
            if(TRIAL_KIND_ENUMS.isEnumValid(ed.trial_kind))
                      {stat.value = false; stat.comment="[invalid trial kind]";}
        }else{
           doError("This line should be dead code");
        }//BLOCK::END
        
        //return the status. If invalidated. Will contain message.
        //If valid, comment field will be empty string.
        return stat;
        
    }//FUNC::END
    
    /** Deep copy is easier to pull off. Because we can simply serialize
     *  and de-serialize it. So go with that.
     * @param ed :The edict to copy.
     * @return :A deep-copy/clone of object.
     */
    public static Edict clone(Edict ed){
      
        String tex = JSONUtil.serializeObj_NoNULL(ed);
        return (Edict)MapperUtil.readAsObjectOf(Edict.class, tex);
        
    }//FUNC::END
    
    public static Edict makeErrorEdict(String msg){
        Edict op = new Edict();
        op.isError             = true;
        op.comment             = msg;
        op.errorCode           = EntityErrorCodes.GENERIC_ERROR;
        op.ninja_id_list       = new ArrayList<Long>();
        op.duration_in_minutes = (-12);
        op.trial_kind          = TRIAL_KIND_ENUMS.DO_NOT_PERSIST_;
        return op;
    }//FUNC::END
    
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = Edict.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
