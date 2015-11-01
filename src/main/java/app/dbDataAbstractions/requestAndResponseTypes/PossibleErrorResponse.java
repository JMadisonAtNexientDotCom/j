package app.dbDataAbstractions.requestAndResponseTypes;

import javax.ws.rs.core.Response;

/**
 * Packs up a response object into a container.
 * Same pattern as a Java optional object.
 * @author jmadison :2015.10.27(Oct27th,Year2015)
 */
public class PossibleErrorResponse {
    
    /** Stores error response **/
    public Response error  = null;
    
    /** Does error exist? (aka, not null) **/
    public boolean  exists = false;
}//CLASS::END
