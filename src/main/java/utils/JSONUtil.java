package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import primitives.IntegerWithComment;
import test.MyError;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import test.dbDataAbstractions.entities.bases.BundleEntityBase;
import test.dbDataAbstractions.entities.bases.CompositeEntityBase;
import test.dbDataAbstractions.fracturedTypes.FracturedTypeBase;

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
        
        if(null==ent){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            throw new MyError("null argument given to entityToJSONResponse");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        return genericObjectToJSONResponse(ent);
    }//FUNC::END
    
    public static Response fracturedTypeToJSONResponse(FracturedTypeBase frac){
        
        if(null==frac){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            throw new MyError("null arg given to fracturedTypeToJSONResponse");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        return genericObjectToJSONResponse(frac);
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Converts a bundle entity
     * (an entity that bundles multiple entity of different types.)
     * into a JSON response.
     * @param be :Our bundle entity.
     * @return   :A serialized JSON response.
     ------------------------------------------------------------------------**/
    public static Response bundleEntityToJSONResponse(BundleEntityBase be){
        if(null==be){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            throw new MyError("bundleEntityToJSONResponse(null)==BAD!");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE                                                  
                                                                                                       
        return genericObjectToJSONResponse(be);
    }//FUNC::END
    
    /** Converts a composite entity 
     * (an entity with no direct database table representation)
     *  into a JSON Response. Used for creating rest services.
     * 
     * @param ce : The composite entity we want to convert to JSON.
     * @return   : A 200/OK response with the entity asJSON inside the body. **/
    public static Response compositeEntityToJSONResponse
                                                       (CompositeEntityBase ce){
                                                           
        if(null==ce){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            throw new MyError("compositeEntityToJSONResponse(null)==BAD!");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE                                                  
                                                                                                       
        return genericObjectToJSONResponse(ce);
    }//FUNC::END
    
    /** Made this PRIVATE because I would like to use strict typing in project.
     *  However, internally, the core implementation uses generic objects.
     * @param obj :The object you want to convert to JSON response.
     * @return    :A JSON response. **/
    private static Response genericObjectToJSONResponse(Object obj){
        //This huge chunk of code could go in a JSON utility
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter prettyPrinter = mapper.writerWithDefaultPrettyPrinter();
        String jsonText;
        try {
            jsonText = prettyPrinter.writeValueAsString( obj );
        } catch (JsonProcessingException ex) {
            Logger.getLogger(JSONUtil.class.getName()).log(Level.SEVERE, null, ex);
            //Writing code to keep compiler happy, never a good reason.
            //This is horrible code. Now I am going to re-throw the exception I just caught.
            throw new MyError("Yeah, we are not really catching this exception. JSONUtil.java");
        }
        return Response.ok(jsonText, MediaType.APPLICATION_JSON).build();
    }//FUNC::END
    
    /**
     * Converts a number to a JSON response.
     * @param myValue
     * @param myComment
     * @return sdfjlsdjflsdjfljds */
    public static Response numberToJSONResponse(int myValue, String myComment){
        IntegerWithComment obj = new IntegerWithComment();
        obj.value   = myValue;
        obj.comment = myComment;
        return genericObjectToJSONResponse(obj);
    }//FUNC::END
    
}//CLASS::END
