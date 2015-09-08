package test.servlets.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
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
import test.config.constants.ServletClassNames;
import test.transactions.ninja.NinjaTransactionUtil;
import test.entities.NinjaTable;

////////////////////////////////////////////////////////////////////////////////
@Path(ServletClassNames.NinjaRestService_MAPPING) //<--If this @Path path matches the path of 
           //   ANY OTHER JERSEY SERVLET your servlets will all fail. 
           //   Even if the full path to this servlet is unique. ARGH!!!!
public class NinjaRestService {
 
	@GET
	@Path("getMsg/{param}")
	public Response getMsg(@PathParam("param") String msg) {
 
		String output = "Servlet: NinjaRestService : " + msg;
 
		return Response.status(200).entity(output).build();
 
	}//FUNC::END
        
    
        
          @GET
          @Path("getNextNinja") //removed slash at end. Lets try again.
          public Response getNextToken(@QueryParam("msg") int msg){
            
            //message msg is discarded and not used for now.
            
            //ENTER transaction:
            Session ses = TransUtil.enterTransaction();
            
            //Transaction logic:
            NinjaTable nt = NinjaTransactionUtil.makeNextNinja();
            TransUtil.markEntityForSaveOnExit(nt);
            
            //EXIT transaction:
            TransUtil.exitTransaction(ses, true);
            
            //This huge chunk of code could go in a JSON utility
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter prettyPrinter = mapper.writerWithDefaultPrettyPrinter();
            String jsonText;
            try {
                jsonText = prettyPrinter.writeValueAsString( nt );
            } catch (JsonProcessingException ex) {
                Logger.getLogger(NinjaRestService.class.getName()).log(Level.SEVERE, null, ex);
                //Writing code to keep compiler happy, never a good reason.
                //This is horrible code. Now I am going to re-throw the exception I just caught.
                throw new MyError("Yeah, we are not really catching this exception. NinjaRestService.java");
            }
            return Response.ok(jsonText, MediaType.APPLICATION_JSON).build();
            
            
            
        }//FUNC::END
}//CLASS::END