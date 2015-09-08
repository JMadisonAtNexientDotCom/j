package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import test.MyError;
import test.entities.BaseEntity;

/**
 * Original use: Refactoring duplicate code that existed in both
 * my NinjaRestService.java and TokenRestService.java
 * @author jmadison **/
public class JSONUtil {
    
    /** Converts an entity to pretty-formatted JSON Response.
     *  Used for creating rest services.
     * @param ent : The entity we wish to convert to JSON
     * @return    : A 200/OK response with the 
     *              entity as JSON inside the body. **/
    public static Response entityToJSONResponse(BaseEntity ent){
        //This huge chunk of code could go in a JSON utility
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter prettyPrinter = mapper.writerWithDefaultPrettyPrinter();
        String jsonText;
        try {
            jsonText = prettyPrinter.writeValueAsString( ent );
        } catch (JsonProcessingException ex) {
            Logger.getLogger(JSONUtil.class.getName()).log(Level.SEVERE, null, ex);
            //Writing code to keep compiler happy, never a good reason.
            //This is horrible code. Now I am going to re-throw the exception I just caught.
            throw new MyError("Yeah, we are not really catching this exception. JSONUtil.java");
        }
        return Response.ok(jsonText, MediaType.APPLICATION_JSON).build();
    }//entityToJSONResponse
    
}//CLASS::END
