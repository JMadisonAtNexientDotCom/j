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
 * @author jmadison **/
public class DebugConfig {
    
    /** Have we built in debug mode? **/
    public static final boolean isDebugBuild = true;
    
}//CLASS::END
