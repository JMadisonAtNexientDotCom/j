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
import test.dbDataAbstractions.requestAndResponseTypes.postTypes.postResponse.PostResponseType;

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
    
    public static Response postResponseToJSONResponse(PostResponseType prt){
        if(null==prt){
            doError("[Null arg given to postResponseToJSONResponse]");
        }/////////////
        
        return genericObjectToJSONResponse(prt);
        
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
        String jsonText;
        jsonText = serializeObj_NullOKAY(obj);
        return Response.ok(jsonText, MediaType.APPLICATION_JSON).build();
    }//FUNC::END
    
    /**
     * I think it is a bit of an anti-pattern to make a new serialize
     * function for everything you want to serialize, just for the sake
     * of type safety. I am going to use this function for testing.
     * As sometimes I will want to serialize ~miscellanious~ objects
     * that I can't justify making a specific function for.
     * @param obj :The object to serialize.
     * @return    :A 200/ok with serialized object in body.
     */
    public static Response whateverToJsonResponse(Object obj){
        return genericObjectToJSONResponse(obj);
    }//FUNC::END
    
    /** Wrapper for core serialization code that will throw error
     *  if you give it a null object. If you are NOT expecting null,
     *  you should be using the NoNULL version that will check for mistakes.
     * @param obj :The object to serialize.
     * @return :A serialized JSON object.
     */
    public static String serializeObj_NoNULL(Object obj){
        if(null == obj){doError("[Null supplied to serializeObj_NoNULL]");}
        return serializeObject_PRIVATE(obj);
    }//FUNC::END
    
    /**
     * This object is allowed to take null for an input.
     * @param obj :The object to serialize. IS OKAY if the object is null.
     * @return    :Json text representing that object.
     */
    public static String serializeObj_NullOKAY(Object obj){
        return serializeObject_PRIVATE(obj);
    }//FUNC::END
    
    /**
     * INTERNAL/PRIVATE core code for serializeObject. Should not be
     * publicly exposed. Hence why _PRIVATE added. Yes, I have a private
     * modifier. But I want to make the point that this is private. There
     * are two perfectly valid publicly exposed wrappers that can be used.
     * Please don't publicly expose this and wreck my pattern.
     * @param obj:The object to serialize.
     * @return :A Json object representing the object inputted.
     */
    private static String serializeObject_PRIVATE(Object obj){
        //This huge chunk of code could go in a JSON utility
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter prettyPrinter = mapper.writerWithDefaultPrettyPrinter();
        String jsonText = null;
        try {
            jsonText = prettyPrinter.writeValueAsString( obj );
        } catch (JsonProcessingException ex) {//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            Logger.getLogger(JSONUtil.class.getName()).log(Level.SEVERE, null, ex);
            //Writing code to keep compiler happy, never a good reason.
            //This is horrible code. Now I am going to re-throw the exception 
            //I just caught.
            String msg = "";
            msg += "[Yeah, we are not really catching this exception.]";
            msg += "[FROM EXPERIENCE: Why you may have gotten this error:]";
            msg += "[If your getters+setters do NOT follow the correct naming]";
            msg += "[convention, you might get errors here.]";
            msg += "[use camelCase getters and setters.]";
            if(null == obj){ msg+= "[input object was null!]";}
            if(null != obj){ msg+= "[obj==[" + obj.toString() + "]]";}
            doError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        if(obj != null && null == jsonText){/////////////////////////
            doError("jsonText should not be null if input was not.");
        }////////////////////////////////////////////////////////////
        
        return jsonText;
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
                         
    /** For cases when we can't return a data structure populated as error,
     *  return a generic error response.
     * @msg    :An error message you would like to put in comment area.
     * @return :A boolean with comment object flagged as error. **/
    public static Response makeGenericErrorResponse(String msg){
        return booleanToJSONResponse(false,msg,true);
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
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
