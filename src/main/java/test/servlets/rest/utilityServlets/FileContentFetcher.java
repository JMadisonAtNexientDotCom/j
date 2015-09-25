package test.servlets.rest.utilityServlets;

import IDontKnowWhereToPutTheseClasses.ReaderContainer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import test.MyError;
import test.config.constants.ResourceRelativeFolderPaths;
import test.config.constants.ServletClassNames;
import test.servlets.rest.BaseRestService;
import utils.JSONUtil;
//import ntsResponseMachine.MyError;
//import ntsResponseMachine.configClasses.ResourceRelativeFolderPaths;
//import ntsResponseMachine.containers.ReaderContainer;

/**-----------------------------------------------------------------------------
 * A base rest service from which all other rest services will be derived.
 * Reason:
 * So we can setup new rest servlets in smaller incriments.
 * When creating a new servlet, we can make it a STUB that extends
 * BaseRestService. We can then use the ping methods on our new stub servlet
 * to make sure it is wired in correctly. Once we know the servlet is configured
 * correctly, we can go on writing more code.
 * 
 * //My design philosophy here:
 * /////////////////////////////////////////////////////////////////////////////
 * 1. Small change. 
 * 2. Small commit. 
 * 3. Test to make sure still working.
 * Repeat in smallest possible steps that will result in UNBROKEN product.
 * Planning out the increments to your final goal is a good idea.
 * /////////////////////////////////////////////////////////////////////////////
 * 
 * Debug notes:
 * This error may lead you to believe it is an entity serialization issue on
 * a JSON response from a servlet. But this actually happened to me because I
 * left off the @QueryParam annotations on the servlet function.
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * HTTP Status 415 - Unsupported Media Type
   type Status report
   message: Unsupported Media Type
   description: The server refused this request because the request entity is 
   *            in a format not supported by the requested resource for the 
   *            requested method.
   !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * 
 * @author jmadison ---------------------------------------------------------**/
@Path(ServletClassNames.FileContentFetcher_MAPPING)
public class FileContentFetcher extends BaseRestService {  
//Jersey servlet does NOT extend HttpServlet
    
     
    /** Necessary for getting ServletContext, which enables us to fetch files
    that end up inside ROOT.war during deployment.
    SOURCE: http://jersey.576304.n2.nabble.com/
    Accessing-ServletContext-or-ServletConfig-from-a-Provider-td2448963.html **/
    @Context
    public ServletContext restServiceContext;
    
    /** Hack: Trying to get X-Forwarded-For header
     *  Maybe this can be done if using @Context in BASE CLASS. **/
    @Context
    public HttpServletRequest servletRequest;
    
    
    /** No constructor needed. Leave blank. If this were an HttpServlet that
     *  was NOT a Jersey servlet, we might have to use super.init() in the
     *  constructor. 
     * http://www.coderanch.com/t/362020/
     *                        Servlets/java/getServletContext-returning-null **/
    public FileContentFetcher(){}
    
    
    
    
    /** Attempts to get a reader that will read the file referenced by the
     *  inputted path. Class meticulously dissects the process to give you
     *  a specific message about what went wrong (if something goes wrong)
     *  and sends that message back without crashing the program.
     * 
     *  If problem occurs. The .exist property of container will be false.
     * @param path : The relative file path of the file you want to read.
     * @return     : A container that may or may-not contain a reader object
     *               that is ready to read from the file pointed to by path. **/
    protected ReaderContainer tryToGetReader(String path){
        
        //We are going to take this code. And de-condense it so we can return
        //what goes wrong in the process from this function:
        //----------------------------------------------------------------------
        //http://stackoverflow.com/questions/17382775/
        //                     opening-a-resource-file-in-a-servlet-on-openshift
        //InputStream inputStream = this.getServletConfig().getServletContext()
        //                                           .getResourceAsStream(path);
        //reader = new BufferedReader(new InputStreamReader(inputStream));
        //----------------------------------------------------------------------
        
        ReaderContainer op = new ReaderContainer();
        
        ServletContext ctx = this.restServiceContext;
        if(null == ctx){
            op.errorMessage = "this.attemptToStoreContext is null";
            return op;
        }
     
        InputStream stream = ctx.getResourceAsStream(path);
        if(null == stream){
            String msg = "ctx.getResourceAsStream(path) returned null";
            msg += "path==[" + path + "]";
            op.errorMessage = msg;
            return op;
        }
        
        BufferedReader reader;
        try{
            reader = new BufferedReader(new InputStreamReader(stream));
        }catch(Exception e){
            String msg = "creating new BufferedReader failed.";
            msg += "error message:" + e.toString();
            op.errorMessage = msg;
            return op;
        }
        
        
        //If everything goes alright, return populated container:
        //Compiler now says this is an unnecessary test for null.
        //But without this wrapper the build fails because:
        //ERROR: variable reader may have not been initialized.
        if(null == reader){
            throw new MyError("How on earth did our reader become null?");
        }
        else
        {
            op.hasReader = true;
            op.reader    = reader;
        }
        
        return op;
        
    }//FUNC::END
    
  
    
    /**
     * Gets the HTML file we want to inject into our .jsp page.
     * @param fileName
     * @return 
     */
    /*
    @GET
    @Path("getHTMLFileToInject")
    protected Response getHTMLFileToInject(
            @QueryParam("fileName") String fileName){
     */   
    /** is it possible to make this servlet for internal use only? **/
    protected String getHTMLFileToInject(String fileName){   
        //resources/json ends up in:
        //ROOT.war/WEB-INF/classes/json/v1/test/
        String path = ResourceRelativeFolderPaths.HTML_INJECT +"/"+ fileName;
        String sourceText = getFileText(path);
        
        //Return sourceText in response object:
        //Response op;
        //op = JSONUtil.stringToJSONResponse(sourceText,"success!",false);
        //return op;
        
        return sourceText;
    }//FUNC::END
    
    /**
     * Gets contents of a file specified.
     * @param path :Fully qualified path to a file you want.
     * @return : A string with the file contents in it.
     */
    protected String getFileText(String path){
        String outputText = "Was never set";
        ReaderContainer readCon;
        BufferedReader reader;
        String newLine = System.lineSeparator();
        
        readCon = tryToGetReader(path);
        if(false==readCon.hasReader){
            return "readCon.errorMessage==" + readCon.errorMessage;
        }else{
            reader = readCon.reader;
        }
        
        try{
            //To convert to string, use a string builder:
            //http://stackoverflow.com/questions/4666748/
            //                            java-how-to-read-bufferedreader-faster
            //SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
            StringBuilder builder = new StringBuilder();
            String aux;
            try{
               while ((aux = reader.readLine()) != null) {
                    builder.append(aux);
                    builder.append(newLine); //get your line endings back.
               }//NEXT
               outputText = builder.toString();
            }catch(IOException e){
                outputText = "getJsonText Failed! e==" + e.toString();
            }//TRY::END
            //SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
        }catch(Exception e){
            return "String builder block failed. Exception:" + e.toString();
        }
        
        //Return the text. 
        //In the same format as the original file we read from:
        return outputText;
    }//FUNC::END
    
}//CLASS::END
