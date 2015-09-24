package test.servlets.rest.util;

/**
 * For testing an idea I have with merging front-end + back-end code
 * and making it refactorable.
 * @author jmadison
 */
public class RestServiceUtil {
    public static String concatStrings(String a, String b, String c){
        return (a + b + c);
    }//FUNC::END
    
    public static String getServiceURL(int restServiceID){
        return    "RAND==[" + Double.toString(Math.random()) + "]";
    }
}
