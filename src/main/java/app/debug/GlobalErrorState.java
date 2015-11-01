package app.debug;

import java.util.ArrayList;
import java.util.List;
import primitives.ClassWithComment;
import frontEndBackEndIntegration.I;

/**
 * A global error state for when something goes wrong.
 * Why? Because I found that some uncaught exceptions I threw EXPLICITLY
 * myself did not show up as response from server.
 * 
 * This is usually true of errors during initialization/setup.
 * 
 * To solve this, we can log problems here and then re-throw and
 * error from here if any errors exist in this global error state.
 * 
 * @author jmadison
 */
public class GlobalErrorState {
    
    private static List<ClassWithComment> _errors = 
                                              new ArrayList<ClassWithComment>();
    /**
     * Add an error to the global error state.
     * @param clazz :The class that originated the error.
     * @param msg   :The custom error message to help us fix error.
     */
    public static void addError(Class clazz, String msg){
        ClassWithComment er = new ClassWithComment();
        er.value  = clazz;
        er.comment = msg;
        
        /**---------------------------------------------------------------------
        Why {@value #isError} != true ?
        isError means the ACTUAL object is configured as an error.
        By configured as an error, 
        I mean "configured with corrupted data to draw attention to itself"
        Though this object is logging an error, this object itself is 
        NOT an error. -------------------------------------------------------**/
        er.isError = false;
        
        //add to our list:
        _errors.add(er);
        
        //This worked decently... Null pointer exception definitely got my
        //attention... But see if you can throw a more specific error.
        //----------------------------------------------------------------------
        //KILL I: De-reference I.API so that .JSP pages crash.
        //I.API_WAS_SET_TO_NULL_TO_BRING_ATTENTION_TO_ERRORS = true;
        //I.API = null;
        //----------------------------------------------------------------------
        
    }//FUNC::END
    
    public static boolean doErrorsExist(){
        return (_errors.size() > 0);
    }//FUNC::END
    
    /** If any errors are stored in the global error state, it will
     *  throw errors.
     */
    public static void throwIfHasErrors(){
        if(_errors.size() > 0){
            generateErrorMessageAndThrow();
        }//////////////////////
    }//FUNC::END
    
    private static void generateErrorMessageAndThrow(){
        String errorMsg = getLog();
        throw new GlobalErrorStateError(errorMsg);
    }//FUNC::END
    
    public static String getLog(){
        
        //Make error message header:
        String msg = "";
        msg+= GlobalErrorState.class.getCanonicalName();
        msg+= "[has errors registred with it!]";
        msg+= "[LOG:]";

        //Go through all of the logged errors and create entries:
        ClassWithComment cur;
        String           log;
        int len = _errors.size();
        for(int i = 0; i < len; i++){
            cur = _errors.get(i);
            log = makeLogEntry(cur);
            msg+=log;
        }//NEXT i
        
        //return the entire textualized error
        //for our human to read.
        return msg;
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Converts Class with comment into textualized error message.
     * @param err: The object representing a logged error.
     * @return   : An message a human can read and use to fix
     *             the problem with. ----------------------------------------**/
    private static String makeLogEntry(ClassWithComment err){
        String op = "";
        op+="(("; //<--visually group class+error in output.
        op+="[Class:][" + err.value.getCanonicalName() + "]";
        op+="[Error:][" + err.comment + "]";
        op+="))"; //<--visually group class+error in output.
        return op;
    }//FUNC::END
   
}//CLASS::END
