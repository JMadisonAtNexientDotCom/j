package frontEndBackEndIntegration.childComponents.restDoc;

/**
 *
 * @author jmadison
 */
public class RestDocument {
    
    public RestDocument(String non_inited_message_text){
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
