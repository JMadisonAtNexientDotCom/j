package test.servlets.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import test.entities.TokenTable;
import test.transactions.token.TokenTransactionUtil;
import test.transactions.util.TransUtil;

//Attempt to convert to object to JSON:
////////////////////////////////////////////////////////////////////////////////
//http://www.mkyong.com/java/how-to-convert-java-object-to-from-json-jackson/
import java.io.File;
import java.io.IOException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
////////////////////////////////////////////////////////////////////////////////
 
@Path("/")
public class TokenRestService {
 
	@GET
	@Path("getMsg/{param}")
	public Response getMsg(@PathParam("param") String msg) {
 
		String output = "Servlet: TokenRestService : " + msg;
 
		return Response.status(200).entity(output).build();
 
	}//FUNC::END
        
        @GET
        @Path("getNextToken/{param}")
        public Response getNextToken(@PathParam("param") String msg){
            
            //message msg is discarded and not used for now.
            
            //ENTER transaction:
            Session ses = TransUtil.enterTransaction();
            
            //Transaction logic:
            TokenTable tt = TokenTransactionUtil.makeNextToken();
            TransUtil.markEntityForSaveOnExit(tt);
            
            //EXIT transaction:
            TransUtil.exitTransaction(ses, true);
            
            //String output = "NEXT TOKEN GOTTEN:[" + tt.getToken() + "]";
            //return Response.status(200).entity(output).build();
            
            //see if we can return json:
            
            
            
            //http://stackoverflow.com/questions/15375328/hibernate-entity-to-json-object-with-jackson-to-http-post
            
            //The webResource object:
            //Client client = Client.create();
            //String url = kayakoWebService.generateURL();
            //WebResource webResource = client.resource(url);
            
            //Company domainObject = companyDAO.findCompanyById(id);
            //ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
            //    .entity(domainObject, MediaType.APPLICATION_JSON)
            //    .post(ClientResponse.class);
            
            ObjectMapper mapper = new ObjectMapper();
            String jsonText = mapper.writerWithDefaultPrettyPrinter().writeValueAsString( tt );

            return Response.status(200).entity(jsonText).build();
            
            
            
            
        }//FUNC::END
}//CLASS::END