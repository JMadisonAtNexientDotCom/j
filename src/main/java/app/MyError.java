package app;

//http://stackoverflow.com/questions/10574906/throwing-custom-exceptions-in-java

import app.config.debug.DebugConfig;
import app.config.debug.DebugConfigLogger;
import app.debug.GlobalErrorState;

//import java.lang.RuntimeException;

public class MyError extends RuntimeException{
    
    
  
    
    /**
     * Use this to make error so we can put stuff before the super()
     * @param clazz :The class the error message originated from.
     * @param msg   :The error message to show.
     * @return      :A custom error object.
     */
    public static MyError make(Class clazz, String msg){
        
        //Append logs to end if in debug build:
        if(DebugConfig.isDebugBuild){//LLLLLLLLL
            msg += DebugConfigLogger.dumpLogs();
        }//LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL
        
        MyError op = new MyError(clazz, msg);
        return op;
        
    }//FUCN::END
   
    
    /** Made constructor private so that we can optionally tack on
     *  debug info to the end of the error message if debug build:
     * @param clazz :The class the error message originated from.
     * @param msg   :The error message to show.
     */
    private MyError(Class clazz, String msg) {
        
        super(msg);
        
        //Creation of error will also add it to the global error state
        //so that we can re-throw error at any time within application to
        //make absolutely sure the problem is brought to attention:
        GlobalErrorState.addError(clazz, msg);
    }///////////////CONSTRUCTOR////////////////
}//CLASS::END
