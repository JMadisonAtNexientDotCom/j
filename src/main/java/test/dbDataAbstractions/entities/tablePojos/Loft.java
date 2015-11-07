package test.dbDataAbstractions.entities.tablePojos;

import java.util.ArrayList;
import java.util.List;
import test.MyError;
import test.config.debug.DebugConfig;

/**
 * Pojo version of LoftTable.
 * A generic data structure used to store ordered clusters of foreign key ids.
 * @author jmadison :2015.11.07 (Nov7th,Year2015,Saturday) **/
public class Loft {
    
    //List of straw bales that this loft is collecting.
    public List<Bale> stackOfBales; //bale vs bales == too similar of names.
    
    /**
     * Make a new loft and initialize it with it's first bale
     * @param bale_of_ids :Cluster of foreign key ids.
     * @return : See above. **/
    public static Loft makeEmptyLoft(){
        Loft op = new Loft();
        op.stackOfBales = new ArrayList<Bale>();
        return op;
    }//
    
    /**
     * Push another bale onto the end of the loft.
     * @param bale_of_ids_as_straw :The ids used to create a new bail to 
     *                              push onto the loft.                      **/
    public void pushBaleUsingStraw(List<Long> bale_of_ids_as_straw){
        //Convert input to bale;
        Bale theBale = Bale.make(bale_of_ids_as_straw);
        pushBaleUsingBale( theBale );
    }//FUNC::END
    
    /**
     * Push another bale onto the end of the loft.
     * @param bale_of_ids :Bale of straw. (unordered collection of ids)      **/
    public void pushBaleUsingBale(Bale bale_of_ids){
        
        //This is a bit heavy of a check, so only do in debug build:
        if(DebugConfig.isDebugBuild){
            Loft.assertBaleIsUnique(this, bale_of_ids);
        }//Debug?
        
        this.stackOfBales.add(bale_of_ids);
    }//FUNC::END
    
    //
    /**
     * Throws error if bale arrangement already exists.
     * Note: Order of straw within bale does not matter.
     * @param theLoft
     * @param theBale 
     */
    private static void assertBaleIsUnique(Loft theLoft, Bale theBale){
        if( doesBaleExistInLoft(theLoft, theBale) ){
            doError("[The bale of straw already exist in the loft]");
        }    
    }//FUNC::END
    
    /**Checks to see if theBale is already within theLoft.
     * @param theLoft : A stack of straw bales.
     * @param theBale : The straw bale that we want to check for.
     *                  The order of the straw within a bale does not
     *                  matter. **/
    private static boolean doesBaleExistInLoft(Loft theLoft, Bale theBale){
        for(Bale b : theLoft.stackOfBales){
            if(areBalesIdentical(b, theBale) ){
                return true;
            }
        }//next bail.
        
        //Bail does NOT exist in the loft.
        return false;
        
    }//FUNC::END
    
    //Returns TRUE if bale are identical.
    private static boolean areBalesIdentical(Bale b0, Bale b1){
        boolean results = true;
        for(Long s0 : b0.straw){
            if(b1.straw.contains(s0)){
                //do nothing. Still identical.
            }else{
                results = false; //NOT identical.
                break;
            }
        }//next piece of straw.
        
        return results;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = Loft.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//Class::END
