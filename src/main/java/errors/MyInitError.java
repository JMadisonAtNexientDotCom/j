package errors;

/**
 * An error to be used when error is thrown during initialization of a class.
 * I am making this because my other error fails to show up on the stack
 * trace when it is thrown during initialization.
 * @author jmadison
 */
public class MyInitError extends NoClassDefFoundError {
    public MyInitError(String msg) {
        super(msg);
    }
}//CLASS::END
