package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import test.MyError;
import test.config.debug.DebugConfig;

//-----------------------------------------------------------------------------+
////Basic premise: Subdivides the range of values to choose from               |
////using recursion. Recursion stops when we have collected enough values.     |
////When no pivot element, like first iteration, easy to know what goes on     |
////left side and what goes on right.                                          |
////When there is a pivot element, the LEFT side claims ownership of it.       |
////The right side must shift it's selection over one.                         |
////We do this so there is no collision amongst sub-divided sets.              |
//                                                                             |
//     +--#2L------+--#2R--+       +--#2L------+--#2R--+                       |
//     |           |       |   |   |           |       |                       |
// +---|-ITERATION#1:LEFT--|---+---|-ITERATION#1:RIGHT-|---+                   |
// |   |           |       |   |   |           |       |   |                   |
// 1/-\2/-\	/-\	/-\2/-\	/-\2/-\1/-\2/-\	/-\	/-\2/-\	/-\2/-\1                   |
//  |0|	|1|	|2|	|3|	|4|	|5|	|6|	|7|	|8|	|9|	|A|	|B|	|C|	|D|                    |
//  \-/	\-/	\-/	\-/	\-/	\-/	\-/	\-/	\-/	\-/	\-/	\-/	\-/	\-/                    |
//                                                       ^                     |
//																									  	                           14th                  |
//-----------------------------------------------------------------------------+



/**
 *
 * Weird error: TakeCenter is NOT working here, But when I copy and paste
 * the code into https://coderpad.io/ZQNKNDPG , it works fine.
 * 
 * 
 * @author jmadison :2015.10.26 (Oct26th,Year2015,Monday)
 */
public class RandomSetUtil {
    
    static{////////////
        doStaticInit();
    }//////////////////
    
    private static void doStaticInit(){
        //Unit test forced to start the first time
        //the utility is used.
        unitTest();
    }//FUNC::END
    
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
                     
        //check inputted range. Special scenario for small ranges:
        //When there are only 3, the left gets the center, and the right
        //is pushed over onto an already taken value, and causes bug.
        //so when we get to 3 or less in our range, just linearly dump them.
        long delta = inRange[1] - inRange[0] + 1;
        if(delta <= 3){
            linearDump(inRange,values,maxLen);
            return;
        }//linear dump?
        
        if(takeFirst==TAKE_LEFT_FIRST){
            long[] rangeLFT = takeLFT(inRange, values, maxLen);
            if(values.size() >= maxLen){return;}
        
            long[] rangeRGT = takeRGT(inRange, values, maxLen);
            if(values.size() >= maxLen){return;}
            
            if(DebugConfig.isDebugBuild){
                assertNoSetCollision(rangeLFT,rangeRGT);
            }//DEBUG>
            
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
            
            if(DebugConfig.isDebugBuild){
                assertNoSetCollision(rangeLFT,rangeRGT);
            }//DEBUG>
        
            //RECURSIVE ENTRY POINT, taking RIGHT branch first.
            //Next iteration swap and take LEFT first.
            takeSides(rangeRGT, values, maxLen, TAKE_LEFT_FIRST); //<--rng RGT
            takeSides(rangeLFT, values, maxLen, TAKE_LEFT_FIRST); //<--rng LFT
        }else{
            doError("unknown take value");
        }
    }//FUNC::END
              
    /**
     * Dumps range of values into list. No scrambling or any trickery.
     * Just simple linear dump of values.
     * @param inRange :The min+max inclusive range to dump.
     * @param values  :The list of values we are building.
     * @param maxLen  :The max length (target length) 
     *                 of the list we are building.
     */
    private static void linearDump
                                (long[] inRange, List<Long> values, int maxLen){
        long d0 = inRange[0];
        long d1 = inRange[1];
        for(long i = d0; i <= d1; i++){
            if(values.size() <= maxLen){
                values.add(i);
            }else{
                //List is already filled up. Exit.
                return;
            }
        }//next i.  
    }//FUNC::END
                           
    //subdivides range and puts left-most value into list we are building.
    private static long[] takeLFT
                             (long[] inRange, List<Long> values, int maxLen){
           
        //bail if set is fully populated:
        if(values.size() >= maxLen){return inRange;}
        values.add(inRange[0]);
        
        //Cannot sub-divide any further:
        if(inRange[0]==inRange[1]){return inRange;}
                                 
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
        
        //Cannot sub-divide any further:
        if(inRange[0]==inRange[1]){return inRange;}
                                 
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
        
        long min = inRange[0];
        long max = inRange[1];
        if(min>max){doError("out of order!");}
        if(min==max){doError("no center, are same");}
        long range_inclusive = max-min+1;
        
        boolean isOdd = ((range_inclusive % 2) > 0);
        
        //output:
        long op = 0;
        
        
        if(isOdd){//<--- There IS an exact center.
            //doError("temp hack. remove");
           long perfect_center = (min+max)/2;
           double center_floored = Math.floor(perfect_center);
           if(perfect_center != center_floored){doError("didnt find center");}
           
           //if leaning LEFT, we take the exact center.
           //if leaning RIGHT, we push over to the next.
           //This is so sub-divided regions will NOT overlap:
           if(lean== LEAN_LEFT){
               op = perfect_center; //left gets bigger half.
           }else
           if(lean==LEAN_RIGHT){
               op = (perfect_center +1 );
           }else{
               doError("[unknown lean. In isOdd block]");
           }//BLOCK::END
        }else{
            //No exact center. Take to the left of center:
            double minFlt = (double)min + 0.0; //hack.
            double maxFlt = (double)max + 0.0; //hack
            double twoFlt = 2.0; //hack.
            double center = (minFlt+maxFlt)/twoFlt;
            double shifted = 0.0;
            if(lean == LEAN_LEFT){
                shifted = Math.floor(center);
            }else
            if(lean == LEAN_RIGHT){
                //shifted = Math.ceil(center);
                shifted = ceilHack(center);
            }else{
                doError("unknown lean value");
            }//BLOCK::END
            
            op = (int)shifted;
            
        }//
        
        return op;
    }//FUNC::END
    
     /** Because math.floor is not working for me.
     *  IT works on https://coderpad.io/ however...
     * @param val :The value to ceil.
     * @return :Floored value as double. **/
    public static double ceilHack(double val){
        double vfloored = (int)val; //hack off decimals.
        if(vfloored != val){
            //if value changed, it has decimals. Increment it by 1.
            return (vfloored + 1);
        }
        return val;
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
    
    /**
     * Will throw error if the two ranges overlap.
     * @param lft: The range of min-max to the LEFT of search space.
     * @param rgt: The range of min-max to the RIGHT of search space.
     */
    private static void assertNoSetCollision(long[] lft, long[] rgt){
        boolean overlaps = false;
        if(lft[0] >= rgt[0]){overlaps = true;}
        if(lft[1] >= rgt[0]){overlaps = true;}
        String msg = "";
        msg += "[sets overlap!]";
        msg += "lft[0,1]==[" + Long.toString(lft[0]) + ","
                             + Long.toString(lft[1]) + "]";
        msg += "rgt[0,1]==[" + Long.toString(rgt[0]) + ","
                             + Long.toString(rgt[1]) + "]";
        doError(msg);
        
        if(lft[1] != (rgt[0]-1) ){
            doError("expected the right and left sets to be tangental");
        }//
        
    }//FUNC::END
      
    //Unit test segment for this function:
    public static void unitTest(){
        
        //Test ceil hack with 4.5 because (1+8)/2 == 4.5
        double ch1 = ceilHack(4.5);
        if(ch1 != 5){ doError("ch1 failed");}
        
        double ch2 = ceilHack(4.111);
        if(ch2 != 5){ doError("ch2 failed");}
        
        double ch3 = ceilHack(4.999);
        if(ch3 != 5){ doError("ch3 failed");}
        
        
        long[] vals = new long[2];
        vals[0] = 1;
        vals[1] = 8;
        long cen_lft = getCenter(vals, LEAN_LEFT);
        long cen_rgt = getCenter(vals, LEAN_RIGHT);
        
        if(cen_lft != 4){doError("unit test fail. cen_lft!=4");}
        
        if(cen_rgt != 5){
            String msg = "";
            msg += "[cen_rgt==[" + Long.toString(cen_rgt) + "]]";
            msg += "[wanted:[5]]";
            doError(msg);
        }
        
        
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
