package app.config.constants;

/**
 * 
 * Reason for making this class:
 * Found out that /resources/json in my project ends up getting mapped to:
 * ROOT.war/WEB-INF/classes/json. This seems like something that could change
 * depending on deployment settings.
 * 
 * Any other relative resource folders I need to access contents from
 * the context of deployed ROOT.war file will go here...
 * 
 * 
 * @author jmadison
 */
public class ResourceRelativeFolderPaths {
    
    // Relative paths must start with a "/" !!!!!!!!  //
     
    /**
     * Were our CSS and JS libraries we want to inject into the
     * header (or wherever you think is appropriate) of our
     * .JSP files.
     * 
     * NOTE: If you move the libInjectHTML folder from resources/libInjectHTML
     *       It will break the code. Refactor carefully!
     */
    public static final String HTML_INJECT = "/WEB-INF/classes/libInjectHTML";
    
}//CLASS::END
