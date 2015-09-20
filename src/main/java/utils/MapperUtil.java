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
 * Probably should have multiple instances of this on different threads?
 * Or synchronize it. This doesn't seem thread safe.
 * @author jmadison
 */
public class MapperUtil {
    
    private static final ObjectMapper _mapper = new ObjectMapper();
    
    public static <T> T readAsObjectOf(Class<T> clazz, String value){
                                                        
        try {//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            return _mapper.readValue(value, clazz);
        } catch (IOException ex) {
            Logger.getLogger(MapperUtil.class.getName()).log(Level.SEVERE, null, ex);
            throw new MyError("MapperUtil failed us!");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
    }//FUNC::END
}//CLASS::END
