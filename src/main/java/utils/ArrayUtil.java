package utils;

/**
 *
 * @author jmadison
 */
public class ArrayUtil {
    
    /**
     * Asks if array reference contains data. Allows NULL INPUTS.
     * Returns false if array is empty, or null.
     * @param <T> :The type of the array. We don't really care.
     * @param arr :The array to inspect.
     * @return :See above.
     * 
     * Original Usage: Refactored so could be used in
     * OrderSlip.java and BeaconLightHouse.java
     */
    public static <T> boolean doesArrayHaveData(T[] arr){
        if(null == arr){return false;}
        if(arr.length <= 0){return false;}
        return true;
    }//FUNC::END
    
}//CLASS::END
