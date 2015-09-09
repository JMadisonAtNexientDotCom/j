package test.servlets.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import test.transactions.util.ninja.NinjaTransUtil;
import test.entities.NinjaTable;
import utils.JSONUtil;

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
    @Path("makeNinjaRecord")
    public Response makeNinjaRecord(
            @QueryParam("name")         String name ,
            @QueryParam("phone")        long   phone,
            @QueryParam("email")        String email,
            @QueryParam("portfolioURL") String portfolioURL){
        
        //ENTER transaction:
        Session ses = TransUtil.enterTransaction();
        
        //Transaction logic:
        NinjaTable nt = NinjaTransUtil.
                                makeNinjaRecord(name,phone,email,portfolioURL);
        
        //Mark entity for save:
        TransUtil.markEntityForSaveOnExit(nt);
       
        //EXIT transaction, true==we have entities to save.
        TransUtil.exitTransaction(ses, TransUtil.EXIT_WITH_SAVE);
        
        //Return entity as body of 200/ok response:
        return JSONUtil.entityToJSONResponse(nt);
       
    }//FUNC::END
        
    @GET
    @Path("getNinjaByID")
    public Response getNinjaByID(@QueryParam("id") long nid){
        //ENTER transaction:
        Session ses = TransUtil.enterTransaction();
        
        //Transaction logic:
        NinjaTable nt = NinjaTransUtil.getNinjaByID( nid );
        
        //DO NOT MARK ENTITY FOR SAVE! 
        //Getting method is not meant to mutate.
       
        //EXIT transaction:
        //THERE ISNOTHING TO SAVE. So exit transaction with a false.
        TransUtil.exitTransaction(ses, false);
        
        //Return entity as body of 200/ok response:
        return JSONUtil.entityToJSONResponse(nt);
    }//FUNC::END
        
    @GET
    @Path("getNextNinja") //removed slash at end. Lets try again.
    public Response getNextNinja(@QueryParam("msg") int msg){

        //message msg is discarded and not used for now.

        //ENTER transaction:
        Session ses = TransUtil.enterTransaction();

        //Transaction logic:
        NinjaTable nt = NinjaTransUtil.makeNextNinja();
        TransUtil.markEntityForSaveOnExit(nt);

        //EXIT transaction:
        TransUtil.exitTransaction(ses, true);

        //Return entity as body of 200/ok response:
        return JSONUtil.entityToJSONResponse(nt);

    }//FUNC::END
}//CLASS::END