package frontEndBackEndIntegration.childComponents;

/**
 * The config servlet will cache text from needed files here so that
 * the java application has access to the data.
 * 
 * @author jmadison
 */
public class TextFileCacheRegistry {
    
    /** Reference this variable in .JSP file to include all of the standard
     *  CSS files used in this application. **/
     public static String INCLUDE_CSS = 
    "BACK_END_PROBLEM(NOT UI PEOPLE'S FAULT)::INCLUDE_CSS_FAILED_TO_INITIALIZE";

    /** Reference this variable in .JSP file to include all of the standard
     *  JS files used in this application. **/
     public static String INCLUDE_JS = 
     "BACK_END_PROBLEM(NOT UI PEOPLE'S FAULT)::INCLUDE_JS_FAILED_TO_INITIALIZE";
    
}//FUNC::END
