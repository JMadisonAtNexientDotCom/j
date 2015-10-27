package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import test.MyError;
import test.config.debug.DebugConfig;

/**
 *
 * @author jmadison :2015.10.26 (Oct26th,Year2015,Monday)
 */
public class RandomSetUtil {
    
    private static int LEAN_LEFT = (-1);
    private static int LEAN_RIGHT= (1);
    
    private static int TAKE_LEFT_FIRST = (-1);
    private static int TAKE_RIGHT_FIRST= (1);
    
    /** Creates a randomized set of unique values.
     *  Original usage: Creating randomized cuecards for deck.
     *                  I.E: Making randomized test questions for test.
     * 
     * @param numEntries :How many entries in our set?
     * @param rangeMin   :The min value, INCLUSIVE to pick from.
     * @param rangeMax   :The max value, INCLUSIVE to pick from.
     * @return :A set of random numbers within the inclusive range provided.
     */
    public static List<Long> createRandomSetOfUniqueValues
                                (int numEntries, long rangeMin, long rangeMax){
           
        //Error check inputs:
        //EIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEI//
        if(rangeMin > rangeMax){
            doError("min should be less than or equal to max");
        }//Error?
                                     
        long deltaInclusiveRangeAvailable = (rangeMax-rangeMin)+1;
        if(deltaInclusiveRangeAvailable < numEntries){
            //There pool of values is too small to make it possible
            //to return a set of all unique values of the length you asked for.
            String msg = "";
            msg += "[You pigeon holed yourself. Set too small.]";
            msg += "numEntries==[" + Integer.toString(numEntries) + "]";
            msg += "rangeMin==[" + Long.toString(rangeMin) + "]";
            msg += "rangeMax==[" + Long.toString(rangeMax) + "]";
            doError(msg);
        }//Error?
        //EIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEIEI//
        
        ArrayList<Long> values = new ArrayList<Long>(numEntries);

        long[] initialRange = new long[2];
        initialRange[0] = rangeMin;
        initialRange[1] = rangeMax;

        //Begin recursive grabbing of values:
        //Arbitrarily pick which side you wish to take first:
        takeSides(initialRange, values, numEntries, TAKE_RIGHT_FIRST);

        //If in debug mode, we are going to use a hashmap to guarantee
        //that all of the values are unique:
        if(DebugConfig.isDebugBuild){
          assertAllValuesUnique(values);
        }//DEBUG::END
        
        if(values.size() != numEntries){
            doError("resulting output is not correct length");
        }//error?

        //return the values.
        //Should be random enough looking for now.
        //The next step of process would be to shuffle the values in
        //place, if you ever get around to it.
        return values;

    }//FUNC::END
                               
    /**
     * Recursively sub-divides range of values and samples them.
     * Filling them into the values array provided. Recursion ends
     * when values array has been fully populated.
     * @param inRange   :inclusive range of values to choose from.
     * @param values    :The set of "random" values we are building.
     * @param maxLen    :The max # of items in the set. Our target length.
     * @param takeFirst :Take the RIGHT or LEFT partition first? **/
    private static void takeSides
                 (long[] inRange, List<Long> values, int maxLen, int takeFirst){
        
        if(takeFirst==TAKE_LEFT_FIRST){
            long[] rangeLFT = takeLFT(inRange, values, maxLen);
            if(values.size() >= maxLen){return;}
        
            long[] rangeRGT = takeRGT(inRange, values, maxLen);
            if(values.size() >= maxLen){return;}
            
            //RECURSIVE ENTRY POINT, taking LEFT branch first.
            //Next iteration swap and take RIGHT first.
            takeSides(rangeLFT, values, maxLen, TAKE_RIGHT_FIRST); //<--rng LFT
            takeSides(rangeRGT, values, maxLen, TAKE_RIGHT_FIRST); //<--rng RGT
        }else
        if(takeFirst==TAKE_RIGHT_FIRST){
            long[] rangeRGT = takeRGT(inRange, values, maxLen);
            if(values.size() >= maxLen){return;}
            
            long[] rangeLFT = takeLFT(inRange, values, maxLen);
            if(values.size() >= maxLen){return;}
        
            //RECURSIVE ENTRY POINT, taking RIGHT branch first.
            //Next iteration swap and take LEFT first.
            takeSides(rangeRGT, values, maxLen, TAKE_LEFT_FIRST); //<--rng RGT
            takeSides(rangeLFT, values, maxLen, TAKE_LEFT_FIRST); //<--rng LFT
        }else{
            doError("unknown take value");
        }
    }//FUNC::END
                           
    //subdivides range and puts left-most value into list we are building.
    private static long[] takeLFT
                             (long[] inRange, List<Long> values, int maxLen){
           
        //bail if set is fully populated:
        if(values.size() >= maxLen){return inRange;}
        values.add(inRange[0]);
                                 
        long min = inRange[0] + 1; //move min values UP towards center.
        long max = getCenter(inRange,LEAN_LEFT);
        
        //if min==max, bail out, you have converged.
        //possibly collect value that you have converged on:
        if(max==min){
            if(values.size() < maxLen){
                values.add(max);
            }//
            return inRange;
        }//
        
        //formulate output:
        long[] op = new long[2];
        op[0] = min;
        op[1] = max;
        
        return op;      
    }//FUNC::END
    
    //subdivides range and puts right-most value into list we are building.
    private static long[] takeRGT
                             (long[] inRange, List<Long> values, int maxLen){
                                 
        //bail if set is fully populated:
        if(values.size() >= maxLen){return inRange;}    
        values.add(inRange[1]);
                                 
        long max= inRange[1] - 1; //move max value DOWN towards center.
        long min= getCenter(inRange,LEAN_RIGHT);
        
        //if min==max, bail out, you have converged.
        //possibly collect value that you have converged on:
        if(max==min){
            if(values.size() < maxLen){
                values.add(max);
            }//
            return inRange;
        }//
        
        //formulate output:
        long[] op = new long[2];
        op[0] = min;
        op[1] = max;
       
        return op;
    }//FUNC::END
    
    /**
     * Finds the center of an inclusive range of numbers.
     * @param inRange :the range to find the center of.
     * @param lean    :Which way to lean if there is no absolute center
     *                 because the number is even.
     * 
     */
    private static long getCenter(long[] inRange, int lean){
        double center = (inRange[0] + inRange[1]) / 2;
        long op = 0;
        if(lean == LEAN_LEFT){
            op = (long)Math.floor(center);
        }else
        if(lean == LEAN_RIGHT){
            op = (long)Math.ceil(center);
        }else{
            doError("unknown lean value");
        }//BLOCK::END
        
        return op;
    }//FUNC::END
    
    //Get a random integer within the provided inclusive range:
    public static int getRandomIntRange(int minInclusive, int maxInclusive){
        
        int delta = maxInclusive-minInclusive;
        if(delta < 0){doError("min should be greater than max");}
        double deltaPlusOne = delta + 1;
        double offset = (Math.random() * deltaPlusOne);
        double offset_floored = Math.floor(offset);
        int os = (int)offset_floored;
        int op = (minInclusive + os);
        
        if(op < minInclusive || op > maxInclusive){
            doError("random value generated is out of range");
        }//Error?
        
        return op;
        
    }//FUNC::END
    
    //Will throw error if all values are not unique:
    private static void assertAllValuesUnique(List<Long> values){
        HashMap<Long,Boolean> map = new HashMap<Long,Boolean>();
        
        for(long key : values){
            if(map.containsKey(key)){
                String msg = "";
                msg += "[not all values were unique::START]";
                msg += ListUtil.printLongs(values);
                msg += "[not all values were unique::START]";
                doError(msg);
            }//
            map.put(key, true);
        }//next i
    }//FUNC::END
               
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = RandomSetUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
                             
}//CLASS::END
