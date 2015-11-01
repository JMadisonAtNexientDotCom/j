package app.debug;

/**
 * Made my original error object log itself with the global error state.
 * Because of that, I cannot use that error object within the global error
 * state. If we did, it would cause an infinite recursion of logging itself.
 * @author jmadison
 */
public class GlobalErrorStateError extends RuntimeException{
    public GlobalErrorStateError(String msg){
        super(msg);
    }
}
