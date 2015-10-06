package primitives;

import java.util.ArrayList;
import java.util.List;
import test.MyError;

/**
 * Separate ID's into real+fake.
 * Original usage: Splitting inputting batched request into the valid and
 * invalid. If Ninja bob wants ninja sam, ninja carl, and magical non-existant
 * figment of imagination imaginary dragon on his ninja team... Well bob's
 * request is only 1/3 incorrect. We should not be throwing an error that
 * says the entire request is bogus.
 * 
 * @author jmadison :2015.10.06
 */
public class RealAndFakeIDs {
    
    /** Unique list of REAL ids. Meaning the IDs were found in whatever
     *  table we were querying. **/
    public List<Long> real = new ArrayList<Long>();
    
    /** Unique list of FAKE ids. MEaning the IDs were NOT found in whatever
     *  table we were querying. **/
    public List<Long> fake = new ArrayList<Long>();
    
    public static RealAndFakeIDs make (List<Long>inReal, List<Long>inFake){
                                     
        //Do error check for possible things that could go wrong:
        if(null   == inReal){doError("inReal should not be null");}
        if(null   == inFake){doError("inFake should not be null");}
        if(inReal == inFake){doError("inReal==inFake. Same Reference.");}
        
        RealAndFakeIDs op = new RealAndFakeIDs();
        op.real = inReal;
        op.fake = inFake;
        
        return op;
        
    }//FUNC::END
                                 
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = RealAndFakeIDs.class;
        err += clazz.getSimpleName();
        err += msg;
        throw new MyError(clazz, err);
    }//FUNC::END

}//CLASS::END
