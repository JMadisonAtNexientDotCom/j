package IDontKnowWhereToPutTheseClasses;

import java.io.BufferedReader;

/**
 * A simple container for BufferedReader.
 * Original Usage:
 *      To help debug a function that should return a BufferedReader.
 * @author jmadison
 */
public class ReaderContainer {
    
    public BufferedReader reader = null;
    public boolean hasReader = false;
    public String errorMessage = "NOT_SET";
    
    /** Empty constructor. **/
    public ReaderContainer(){};
    
}//CLASS::END
