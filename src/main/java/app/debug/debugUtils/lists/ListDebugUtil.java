package app.debug.debugUtils.lists;

import java.util.List;
import app.MyError;

/**-----------------------------------------------------------------------------
 * 
 * DESIGN NOTE: This would be a nice utility. Unfortunately I am not able
 * do sent a list of List<SomeType> to a function expecting List<Object>
 * Which I find ridiculous since polymorphism should allow for it.
 *
 * Has different checks that, if not true, will throw error.
 * @author jmadison :2015.09.19_0235PM
 ----------------------------------------------------------------------------**/
public class ListDebugUtil {
    
    /**-------------------------------------------------------------------------
     * Throws error if either list is null.
     * Throws error is lists are not identical in length.
     * @param a   :List a.
     * @param b   :List b.
     * @param c   :Calling class.
     * @param msg :Error message to display if problem occurs.
     ------------------------------------------------------------------------**/
    public static void equalAndNonNull
                          (List<Object> a, List<Object> b, Class c, String msg){
        
        if(null==a){doError("["+ msg + "]-->[a==null]", c);}
        if(null==b){doError("["+ msg + "]-->[b==null]", c);}
        areEqual(a,b,c,msg);
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Throws error if list are not identical in length.
     * @param a   :List a.
     * @param b   :List b.
     * @param c   :Calling class.
     * @param msg :Error message to display if problem occurs.
     ------------------------------------------------------------------------**/
    public static void areEqual
                          (List<Object> a, List<Object> b, Class c, String msg){
        if(a.size() != b.size()){
            doError(msg,c);
        }////////////////////////
    }//FUNC::END
                          
                          
    /**-------------------------------------------------------------------------
    * Wrapper function to throw errors from this class.
    * @param msg :Specific error message.
    -------------------------------------------------------------------------**/
    private static void doError(String msg, Class c){
       String err = "List Debug Util MSG:";
       err += "DEBUG UTIL CLASS:[" + ListDebugUtil.class.getSimpleName() + "]";
       err += "Class Error Check Originated From:";
       err += "[" + c.getCanonicalName() + "]";
       err += msg;
       throw MyError.make(ListDebugUtil.class, err);
    }//FUNC::END                 
                          
}//CLASS::END
