package frontEndBackEndIntegration.childComponents.restDoc;

import test.MyError;

/**
 * DELETE THIS!
 * 
 * Delete this. AKA: Reverse the refactor responsible for this. Did not work.
 * @author jmadison
 */
public class RestDocument {
    
    public RestDocument(String non_inited_message_text){
        throw new MyError("Delete this class from codebase");
        BASE = non_inited_message_text;
    }//Constructor!
    
    /** The REST API endpoint BASE. Does not point to an API service, but
     *  to the base of the service. Example: http://mySite/NinjaService/"
     *  Where we need to tack on a service call after!
     */
    public String BASE = "CONSTRUCTION_FAILURE";
    
    ///** Eventually, put the different service calls that can be made here. **/
    //public String DOCS = "blaBlabla";
    
}//CLASS::END
