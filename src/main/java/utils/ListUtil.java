package utils;

import java.util.ArrayList;
import java.util.List;
import test.MyError;

/**
 *
 * @author jmadison
 */
public class ListUtil {
    
    /** Sifts through array list of entries and makes sure all occurences.
     *  are UNIQUE.
     * @param inList :The list to sift.
     * @return :A copy of list with all entries unique.
     */
    public static List<Long> makeUnique(List<Long> inList ){
        List<Long> op = new ArrayList<Long>();
        
        Long cur;
        int len = inList.size();
        for(int i = 0; i < len; i++){
            cur = inList.get(i);
            if(op.indexOf(cur) <=(-1)){
                op.add(cur);
            }//Unique add.
        }//NEXT i
        
        return op;
        
    }//FUNC::END
    
    public static String printLongs(List<Long> inList){
        
        if(null == inList){doError("[Input to printLongs was null]");}
        
        String op = "";
        op += "[";
        for(Long entry : inList){
            op += Long.toString( entry );
            op += ","; //<--extra on end. Quick+dirty. Okay for human eyes.
        }//next
        op += "]";
        return op;
    }//FUNC::END
    
    
    // delete this. Unable to get template version to work.
    // /** Serialize list for DEBUGGING. 
     //  *  Human readable, not machine readable. **/
    //public static <T> String print(List<T> inList){
    //    String op = "";
    //    op += "[";
    //    for(T entry : inList){
    //        op += entry.toString();
    //        op += ","; //<--extra on end. Quick+dirty. Okay for human eyes.
    //    }//next
     //   op += "]";
    //    return op;
    //}//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = ListUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
