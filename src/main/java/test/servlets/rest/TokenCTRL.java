package test.servlets.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import test.dbDataAbstractions.entities.tables.TokenTable;
import test.transactions.util.forOwnedMainlyByOneTable.token.TokenTransUtil;
import test.transactions.util.TransUtil;

//Attempt to convert to object to JSON:
////////////////////////////////////////////////////////////////////////////////
//http://www.mkyong.com/java/how-to-convert-java-object-to-from-json-jackson/
import java.io.File;
import java.io.IOException;
//import org.codehaus.jackson.JsonGenerationException;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.map.ObjectMapper;
//import com.fasterXML
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import test.MyError;
import test.debug.DebugConsts;
import test.config.constants.ServletClassNames;
import test.config.constants.identifiers.FuncNameReg;
import utils.JSONUtil;


////////////////////////////////////////////////////////////////////////////////

@Path( ServletClassNames.TokenCTRL_MAPPING )
 //@Path(TokenRestService.class.getSimpleName() + "/")
//@Path("TokenRestService/") //<--By matching class name, we assure not servlet mapping collision.
public class TokenCTRL extends BaseCTRL {
 
    public static final String CLASS_MAPPING = TokenCTRL.class.getSimpleName() + "/";
   
          @GET
          @Path(FuncNameReg.GET_NEXT_TOKEN) //removed slash at end. Lets try again.
          public Response get_next_token(){
            
            //ENTER transaction:
            Session ses = TransUtil.enterTransaction();
            
            //Transaction logic:
            TokenTable tt = TokenTransUtil.makeNextToken();
            TransUtil.markEntityForSaveOnExit(tt);
            
            //EXIT transaction:
            TransUtil.exitTransaction(ses, true);
            
            //Return entity as body of 200/ok response:
            return JSONUtil.entityToJSONResponse(tt);
            
        }//FUNC::END
}//CLASS::END