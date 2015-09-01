package test;

//http://stackoverflow.com/questions/10574906/throwing-custom-exceptions-in-java
//import java.lang.RuntimeException;

public class MyError extends RuntimeException{
    public MyError(String msg) {
        super(msg);
    }
}
