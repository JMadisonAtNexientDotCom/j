package primitives.endPoints;

/**
 * Represents an API endpoint. Used so the UI people have access to
 * easy auto-complete for what API calls they can make.
 * 
 * Because the SIGNATURES are different for different endpoints,
 * cannot include signatures here.
 * @author jmadison
 */
public class EndPoint {
    
    /** The servlet name. Should be the simple name of the class. **/
    public String SERVLET_NAME = "NOT_SET";
    
    /** The class represented by this API endpoint.
     *  Used for error checking the wireups. **/
    public Class SERVLET_CLASS = null;
    
    /** The fully qualified url to get to one of the member functions
     *  of the servlet referenced in this end point object. **/
    public String URL = "NOT_SET";
    
    
    
}//CLASS::END
