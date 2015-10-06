package utils;

import java.util.ArrayList;
import java.util.List;

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
    
}//CLASS::END
