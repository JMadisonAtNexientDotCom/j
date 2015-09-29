package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import primitives.BooleanWithComment;
import primitives.IntegerWithComment;
import primitives.StringWithComment;
import primitives.TypeWithCommentBase;
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
    
    /**-------------------------------------------------------------------------
     *  Used for when we are creating a JSON response.
     *  When we supply IS_AN_ERROR as parameter, the
     *  .isError flag of some of my entities will be
     *  flagged to true. Might also want to NOT sent back 200OK.
     * 
     *  Current implementations of this are used in:
     *  numberToJSONResponse
     *  booleanToJSONResponse
     *  stringToJSONResponse
     ------------------------------------------------------------------------**/
    public static final boolean IS_AN_ERROR = true;
    
    /**-------------------------------------------------------------------------
     *  This is paired with IS_AN_ERROR,and is the opposite of IS_AN_ERROR.
     *  A bit weird having ALL_IS_WELL == false... reading this would give
     *  you wrong impression.
     * 
     *  Reason for this:
     *  ALL_IS_WELL is meant to make function calls that use the
     *  hasError parameter easier on the eyes to read without having to
     *  go to the source and look at the parameter description.
     ------------------------------------------------------------------------**/
    public static final boolean ALL_IS_WELL = false;
    
    /** Converts an entity to pretty-formatted JSON Response.
     *  Used for creating rest services.
     * @param ent : The entity we wish to convert to JSON
     * @return    : A 200/OK response with the 
     *              entity as JSON inside the body. **/
    public static Response entityToJSONResponse(BaseEntity ent){
        
        if(null==ent){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            doError("null argument given to entityToJSONResponse");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        return genericObjectToJSONResponse(ent);
    }//FUNC::END
    
    public static Response fracturedTypeToJSONResponse(FracturedTypeBase frac){
        
        if(null==frac){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            doError("null arg given to fracturedTypeToJSONResponse");
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
            doError("bundleEntityToJSONResponse(null)==BAD!");
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
            doError("compositeEntityToJSONResponse(null)==BAD!");
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
        String jsonText = null;
        try {
            jsonText = prettyPrinter.writeValueAsString( obj );
        } catch (JsonProcessingException ex) {
            Logger.getLogger(JSONUtil.class.getName()).log(Level.SEVERE, null, ex);
            //Writing code to keep compiler happy, never a good reason.
            //This is horrible code. Now I am going to re-throw the exception I just caught.
            doError("Yeah, we are not really catching this exception. JSONUtil.java");
        }
        
        if(obj != null && null == jsonText){/////////////////////////
            doError("jsonText should not be null if input was not.");
        }////////////////////////////////////////////////////////////
        
        return Response.ok(jsonText, MediaType.APPLICATION_JSON).build();
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Converts a number to a JSON response.
     * @param myValue :An INTEGER that we want to convert to json.
     * @param myComment:A comment attached to the boolean. For debug.
     * @param isAnError:UI people can check explicitly to see if this response
     *                  is an error-response.
     * @return : A json response with .value set to myValue 
     ------------------------------------------------------------------------**/
    public static Response numberToJSONResponse
                             (int myValue, String myComment, boolean isAnError){
        IntegerWithComment obj = new IntegerWithComment();
        obj.value   = myValue;
        obj.comment = myComment;
        obj.isError = isAnError;
        return genericObjectToJSONResponse(obj);
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Converts a number to a JSON response.
     * @param myValue:boolean that is true or false.
     * @param myComment:A comment attached to the boolean. For debug.
     * @param isAnError:UI people can check explicitly to see if this response
     *                  is an error-response.
     * @return : A json response with .value set to myValue  
     ------------------------------------------------------------------------**/
    public static Response booleanToJSONResponse
                         (boolean myValue, String myComment, boolean isAnError){
        BooleanWithComment obj = new BooleanWithComment();
        obj.value   = myValue;
        obj.comment = myComment;
        obj.isError = isAnError;
        return genericObjectToJSONResponse(obj);
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Converts a string to a JSON response.
     * @param myValue:boolean that is true or false.
     * @param myComment:A comment attached to the boolean. For debug.
     * @param isAnError:UI people can check explicitly to see if this response
     *                  is an error-response.
     * @return : A json response with .value set to myValue  
     ------------------------------------------------------------------------**/
    public static Response stringToJSONResponse
                          (String myValue, String myComment, boolean isAnError){
        StringWithComment obj = new StringWithComment();
        obj.value   = myValue;
        obj.comment = myComment;
        obj.isError = isAnError;
        return genericObjectToJSONResponse(obj);
    }//FUNC::END
                     
    /**-------------------------------------------------------------------------
     * Converts a value-type boxed with comment/debug information
     * into a JSON response.
     * 
     * @param obj : An object extending TypeWithCommentBase
     *              Examples:
     *                  BooleanWithComment
     *                  IntegerWithComment
     *                  StringWithComment
     * @return : A json response that has serialized the input object.
     ------------------------------------------------------------------------**/
    public static Response typeWithCommentToJSONResponse
                                                      (TypeWithCommentBase obj){
                                                          
        //TODO: We might want to return something besides 200OK if the
        //input object is configured as an error. However, maybe not if
        //error response header breaks the UI. The idea is to keep the UI
        //intact even when error happens. IF the error is something the
        //back-end anticipated could happen.
        //
        //Populating the UI person's form with the words "ERROR"
        //Via the expected object CONFIGURED as an error is much more
        //friendly than giving the UI people something they do not expect
        //when they make a mistake.
        return genericObjectToJSONResponse(obj);
    }//FUNC::END
                                                      
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = JSONUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw new MyError(clazz, err);
    }//FUNC::END
    
}//CLASS::END
