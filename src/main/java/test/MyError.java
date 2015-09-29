package test;

//http://stackoverflow.com/questions/10574906/throwing-custom-exceptions-in-java

import test.debug.GlobalErrorState;

//import java.lang.RuntimeException;

public class MyError extends RuntimeException{
    public MyError(Class clazz, String msg) {
        super(msg);
        
        //Creation of error will also add it to the global error state
        //so that we can re-throw error at any time within application to
        //make absolutely sure the problem is brought to attention:
        GlobalErrorState.addError(clazz, msg);
    }///////////////CONSTRUCTOR////////////////
}//CLASS::END
