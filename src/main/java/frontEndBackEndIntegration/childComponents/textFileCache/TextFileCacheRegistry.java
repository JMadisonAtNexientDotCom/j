package frontEndBackEndIntegration.childComponents.textFileCache;

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
     
     /** Function that gives us on-resize abilities. For when the UI window
         is resized. **/
     public static String SCRIPT_BLOCK_ON_RESIZE_FUNC_FOR_ANGULAR = 
     "BACK_END_PROBLEM: Angular resize function script block failed to load";
    
}//FUNC::END
