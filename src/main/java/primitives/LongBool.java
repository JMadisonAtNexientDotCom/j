package primitives;

import java.util.ArrayList;
import java.util.List;

/**
 * A long with a boolean, the meaning is up to you.
 * @author jmadison:2015.10.19
 */
public class LongBool {
    public long    l = 0;
    public boolean b = false;
    
    /** Strips the booleans off and converts to array of long. **/
    public static List<Long> stripOutLongs(List<LongBool> theList){
        List<Long> op = new ArrayList<Long>();
        int len = theList.size();
        for(int i = 0; i < len; i++){
            op.add(i, theList.get(i).l);
        }//next i.
        
        return op;
    }//FUNC::END
    
}//CLASS::END
