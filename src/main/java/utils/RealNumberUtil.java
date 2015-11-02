/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import app.MyError;

/**
 *
 * @author jmadison
 */
public class RealNumberUtil {
    
    public static void assertGreaterThanZeroNonNull(Long value){
        if(null == value){doError("invalid long");}
        if(value <= 0){doError("invalid long. <= 0");}
    }//
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = RealNumberUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
}//FUNC::END
