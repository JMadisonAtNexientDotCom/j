/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import test.MyError;

/**
 * Pre:2015.09.29:
 * Probably should have multiple instances of this on different threads?
 * Or synchronize it. This doesn't seem thread safe.
 * 
 * Update:2015.09.29:
 * I should have put more comments in this class. Have little clue why
 * I have this class. Guessing it is wrapper code to hide the ugly try-catch
 * mess in here?
 * 
 * @author jmadison
 */
public class MapperUtil {
    
    private static final ObjectMapper _mapper = new ObjectMapper();
    
    public static <T> T readAsObjectOf(Class<T> clazz, String value){
                                                        
        try {//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            return _mapper.readValue(value, clazz);
        } catch (IOException ex) {
            Logger.getLogger(MapperUtil.class.getName()).log(Level.SEVERE, null, ex);
            doError("MapperUtil failed us!");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //This class should only be one line...But you know... enforced
        //try/catch......
        doError("We should never get to this line.");
        return null;
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = MapperUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw new MyError(clazz, err);
    }//FUNC::END
    
}//CLASS::END
