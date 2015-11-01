package app.config.alias;

import app.MyError;
import app.config.debug.DebugConfig;

/**
 * Display name aliases. Story name on LEFT, and serious professional name
 * on the right.
 * 
 * Why story names when it is seen as unprofessional?
 * Using vivid story analogies helps memory work incredibly.
 * I am not going to sacrifice productivity just because something is
 * social taboo.
 * 
 * EXAMPLES:
 * |-------Analogy Name-------||--------Professional Name----------|
 * Nexient Testing Service      Dojo Trial Show
 * 
 * @author jmadison
 */
public class DispAlias {
    
    private static final String[] DOJO_ALIAS    = new String[]{"Dojo"  , "Nexient"};
    private static final String[] NINJA_ALIAS   = new String[]{"Ninja" , "Candidate"};
    private static final String[] SHOW_ALIAS    = new String[]{"Show"  , "Service"};
    private static final String[] ADMIN_ALIAS   = new String[]{"Admin" , "Admin"};
    private static final String[] TRIAL_ALIAS   = new String[]{"Trial" , "Test"};
    private static final String[] RIDDLE_ALIAS  = new String[]{"Riddle", "Question"};
    private static final String[] RHYME_ALIAS   = new String[]{"Rhyme" , "Answer"};
    
    public String DOJO   = "NOT_SET";
    public String NINJA  = "NOT_SET";
    public String SHOW   = "NOT_SET";
    public String ADMIN  = "NOT_SET";
    public String TRIAL  = "NOT_SET";
    public String RIDDLE = "NOT_SET";
    public String RHYME  = "NOT_SET";
    
    public DispAlias(){
        
        int loaderIndex = (-1);
        if(DebugConfig.isDebugBuild){
            loaderIndex = 0;
        }else{
            loaderIndex = 1;
        }//BLOCK::END
        
        //debug the non-debug:
        ///if(loaderIndex == 1){ doError("Ithoughtwewereindebugmode!!");}
        
        //Load alias values based on config state:
        DOJO   = DOJO_ALIAS  [loaderIndex];
        NINJA  = NINJA_ALIAS [loaderIndex];
        SHOW   = SHOW_ALIAS  [loaderIndex];
        ADMIN  = ADMIN_ALIAS [loaderIndex];
        TRIAL  = TRIAL_ALIAS [loaderIndex];
        RIDDLE = RIDDLE_ALIAS[loaderIndex];
        RHYME  = RHYME_ALIAS [loaderIndex];
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = DispAlias.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
