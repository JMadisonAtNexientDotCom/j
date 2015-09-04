package test.servlets.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
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


////////////////////////////////////////////////////////////////////////////////
 
//@Path("/")
public class TokenRestService {
 
	@GET
	@Path("getMsg/{param}")
	public Response getMsg(@PathParam("param") String msg) {
 
		String output = "Servlet: TokenRestService : " + msg;
 
		return Response.status(200).entity(output).build();
 
	}//FUNC::END
        
        //@GET
        //@Path("getNextToken/{param}")
        //public Response getNextToken(@PathParam("param") String msg){
        
          @GET
          @Path("getNextToken") //removed slash at end. Lets try again.
          public Response getNextToken(@QueryParam("msg") int msg){
            
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
            
            
            
            
           
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter prettyPrinter = mapper.writerWithDefaultPrettyPrinter();
            String jsonText;
            try {
                jsonText = prettyPrinter.writeValueAsString( tt );
            } catch (JsonProcessingException ex) {
                Logger.getLogger(TokenRestService.class.getName()).log(Level.SEVERE, null, ex);
                //Writing code to keep compiler happy, never a good reason.
                //This is horrible code. Now I am going to re-throw the exception I just caught.
                throw new MyError("Yeah, we are not really catching this exception. TokenRestService.java");
            }
            return Response.ok(jsonText, MediaType.APPLICATION_JSON).build();
            
            
            
        }//FUNC::END
}//CLASS::END