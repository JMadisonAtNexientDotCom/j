package primitives.endPoints;

import test.MyError;
import utils.JSONUtil;
import utils.StringUtil;

/**
 * An endpoint for HTTP_POST. Has information in it that will make it
 * easier for UI people to make HTTP post calls.
 * @author jmadison
 */
public abstract class PostEndPoint extends EndPoint{
   
    
    
    /**
     * Embeds the schema used for REQUESTS as JAVASCRIPT variable declaration
     * for an angular controller. Used so that UI people do not have to download
     * the schema via HTTP-GET request just so they can fill it out and make
     * and HTTP-POST request.
     * @param embedIdentifierToUse :The identifier of the javaSCRIPT variable
     *                              to set the embedded schema equal to.
     * @return :JAVASCRIPT as Text, that == @param CHARLES
     */
    abstract public String EMBED_REQUEST_SCHEMA(String embedIdentifierToUse);
    
    /**
     * OVERLOADED METHOD: Allows you to specify the indentation level of the
     * block of embedded code.
     * @param embedIdentifierToUse :Identifier name used for javaSCRIPT
     *                              variable name.
     * @param indentationLevel     :How many tabs should be applied to properly
     *                              indent this block?
     * @return :Embedded schema instance with nice indentation.
     */
    abstract public String EMBED_REQUEST_SCHEMA
                            (String embedIdentifierToUse, int indentationLevel);
    
    /**
     * Helper function that EMBED_REQUEST_SCHEMA will use.
     * @param clazz :Reference to class that takes ZERO PARAMETERS for
     *               constructor. Will make a new instance of object using this.
     * @param embedIdentifierToUse :The variable name we would like to set
     *                              this instance equal to when embedded into
     *                              the javascript file. Probably will want
     *                              this value to be something like:
     *                              "$scope.someVarName"
     * @param indentationLevel: How many tabs should the embedded block
     *                          of code be embedded?
     * @return :Serialized instance that can be directly embedded into
     *          javascript.
     */
    public static String embedEmptyClassInstanceInJavaScript
               (Class clazz, String embedIdentifierToUse, int indentationLevel){
        Object inst = null;
        try{
           inst = clazz.newInstance(); 
        }catch(Exception exep){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            String msg = "";
            msg += "[Unable to embed instance.]";
            msg += "[Possibly this instance does not have...]";
            msg += "[...a zero parameter constructor?]";
            doError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //Serialize the class instance:
        if(null == inst){doError("[How did instance get null??]");}
        String serializedINST = JSONUtil.serializeObj_NoNULL(inst);
        
        String indented_instance = "";
        if(indentationLevel > 0){
            indented_instance = indentBeginningOfLines
                                             (serializedINST, indentationLevel);
        }else{
            indented_instance = serializedINST;
        }//BLOCK::END
        
        //Pack the serialized JSON instance into valid javascript,
        //With some helpful comments so we can identify it in the .JSP
        //page source: /////////////////////////////////////////////////////////
        String ind = getIndents(indentationLevel);
        String nl = System.lineSeparator();
        String javascript = "";
        //NOTE: First indent skipped because the <%= JSP EXPRESSION %>
        //      occurs at the correctly indented location.
        javascript += ""  + "///// EMBEDDED REQUEST SCHEMA :: START /////" + nl;
        javascript += ind + embedIdentifierToUse + " = " + nl;
        javascript += indented_instance + ";" + nl;
        javascript += ind + "///// EMBEDDED REQUEST SCHEMA :: END   /////"; 
        ////////////////////////////////////////////////////////////////////////
        
        //return the javascript with embedded schema:
        return javascript;
        
    }//FUNC::END
           
    /** Gets indents to be used in indenting a line of code. **/
    public static String getIndents(int indentationLevel){
        return StringUtil.getIndents(indentationLevel);
    }//FUNC::END
    
    /**
     * Used to indent a block of serialized text.
     * Makes embedding a bit less ugly.
     * @param text :The serialized text to indent.
     * @param indentationLevel :The indentation level. (Number of tabs)
     * @return :Text block but indented.
     */
    public static String indentBeginningOfLines
                                            (String text, int indentationLevel){
        return StringUtil.indentBeginningOfLines(text, indentationLevel);
    }//FUNC::END
    
    
                                     
                                     
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = PostEndPoint.class;
        err += clazz.getSimpleName();
        err += msg;
        throw new MyError(clazz, err);
    }//FUNC::END
                                     
}//CLASS::END
