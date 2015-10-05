package test.config.constants.signatures.sig;

import primitives.endPoints.PostEndPoint;
import test.dbDataAbstractions.requestAndResponseTypes.postTypes.postRequest.Edict;
import utils.JSONUtil;

/**
 * First time I have done an endpoint for a POST call instead of a GET call.
 * Experimenting with a way that UI people can pull down and embed the
 * schema into the javascript document. I think it is going to work out
 * pretty well.
 * 
 * I like this method because it will mean:
 * 1. We will NOT have to make an HTTP-GET call to pull down an empty
 *    PostRequestType just so we can fill it out.
 * 
 * 2. The compiled .JSP file will be able to be read more easily. Because
 *    the schema information will be available inside of it.
 * 
 * 3. The compiled .JSP file will still run as a normal HTML page.
 *  
 * 4. Point #1, but another point on that.
 *    UI people don't have to program ANOTHER http GET request just so they
 *    can use the MAIN http POST request that is featured on the .jsp page.
 * 
 * @author jmadison :2015.10.05:
 */
public class NinjaIdList_TrialKind_DurationInMinutes_EndPoint extends PostEndPoint{
    
    /** Specifically call the signature POSTARG so that it is not
     *  confused with HTTPGET signatures and it will be more obvious
     *  to UI people how it needs to be used.
     */
    public NinjaIdList_TrialKind_DurationInMinutes POSTARG = 
       new NinjaIdList_TrialKind_DurationInMinutes();
    
    /** Object that represents the data that needs to be sent via HTTP-POST **/
    private final Edict SCHEMA_INSTANCE = new Edict();
    
    /**
     * Returns and empty instance of this signature. So that
     * the UI people can use it to send post requests.
     * @param schemaVarName: A variable name that will be used to identify
     *                       the schema.
     * @return : JAVASCRIPT. Returns javascript that sets a variable
     *           who's identifier == schemaVarName.
     *           And who's content == the serialized SCHEMA_TYPE.
     */
    @Override
    public String EMBED_REQUEST_SCHEMA(String schemaVarName){
        String jText = JSONUtil.serializeObj_NoNULL(SCHEMA_INSTANCE);
        return jText;
    }//FUNC::END
    
}//CLASS::END
