package test.config.constants;

/**
 * Error codes to specify exactly what the problem is when returning back a
 * JSON response. Using this because I now realize that we might want to
 * return errors for expected behaviors like... failing to give you a session
 * token because you supplied bad information. 
 * 
 * @author jmadison :2015.09.29
 */
public class EntityErrorCodes {
    
    /** Error because access is denied. Example is if you try to get
     *  a session token with invalid credentials, it will spit back and
     *  error with this this code. **/
    public static final String ACCESS_DENIED = "access_denied";
    
    /** Admin by that user name does not exist. **/
    public static final String ADMIN_404 = "admin_404";
    
    /** Error produced by the application. Not working as expected. **/
    public static final String FATAL_APP_BUG = "fatal_app_bug";
    
    /** Used for when no error exists, or when error exists, but
     *  no one has bothered to specify the details of the error. **/
    public static final String NONE_SET      = "none_set";
    
}//CLASS::END
