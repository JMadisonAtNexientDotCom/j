
package test.dbDataAbstractions.requestAndResponseTypes.postTypes.postRequest;

import annotations.Verbatim;
import annotations.VerbatimValidatorUtil;
import java.util.ArrayList;
import java.util.List;
import test.config.constants.identifiers.VarNameReg;

/**
 * *****************************************************************************
 * DEFINE: Edict:
 * "An official order or proclamation issued by a person in authority."
 * *****************************************************************************
 * 
 * An edict is an order from the administrator to dispatch tokens for
 * ninjas.
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
    public int   trial_kind          = (-1);
    
    /** The time alloted for our Ninjas (Candidates) to complete the 
     *  test once it has began. **/
    @Verbatim(name=VarNameReg.DURATION_IN_MINUTES)
    public int   duration_in_minutes = (-1);
    
}//CLASS::END
