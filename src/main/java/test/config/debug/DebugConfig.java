package test.config.debug;

/**
 * When in debug mode. More information may travel around than is needed in
 * order to help find problems. I honestly think the project should always run
 * in debug mode. Because it will make life easier when things go wrong.
 * But put this flag here because:
 * Without debug mode:
 * 1. Performance benefits.
 * 2. Hackers won't get comment information if they snoop JSON responses.
 * 
 * With debug mode:
 * 1. Easier to develop.
 * 2. Easier to debug.
 * 
 * @author jmadison 2015.??.??_####AMPM
 * @author jmadison 2015.10.01_1121AM  -added production breaking debug opt.
 
 **/
public class DebugConfig {
    
    /** Have we built in debug mode? **/
    public static final boolean isDebugBuild = true;
    
    /**-------------------------------------------------------------------------
     *  Allows debug code to run that WILL break production.
     *  Other debug code may be a performance hit, but this debug code could
     *  cause fatal crash.
     * 
     *  Example: Not allowing values in tables to be variable names or
     *  table names. So if user enters their name as "session_table" that app
     *  will crash. Good for debug and checking for transposition of strings.
     *  But not good for final production.
     ------------------------------------------------------------------------**/
    public static final boolean 
            ifDebugBuild_useStrictDebugCodeThatCanBreakProduction = 
            (true & isDebugBuild);
    
}//CLASS::END
