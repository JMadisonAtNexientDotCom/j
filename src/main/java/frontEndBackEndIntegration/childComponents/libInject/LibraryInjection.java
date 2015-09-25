/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontEndBackEndIntegration.childComponents.libInject;

//import test.servlets.rest.utilityServlets.FileContentFetcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import test.config.constants.ResourceRelativeFolderPaths;


/**
 *
 * @author jmadison
 */
public class LibraryInjection {
   
    /** Hack to get resource.
     *  http://stackoverflow.com/questions/2797162/getresourceasstream-is-always-returning-null
     */
    ///@Context public static ServletContext servletContext;
    
    /**
     * Retrieves library tags used by the project. Uses ABSOLUTE PATHS
     * to all of the libraries so that this call can work anywhere.
     * @return :A series of formatted css library tags for HTML/JSP docs
     */
    public static String getLibTagsCSS(){
        String tags = "";
        tags +=  "<!-- todo, CSS importing code -->";
        return tags;
    }//FUNC::END
    
    /**
     * Retrieves library tags used by the project. Uses ABSOLUTE PATHS
     * to all of the libraries so that this call can work anywhere.
     * @return :A series of formatted javascript library tags for HTML/JSP docs
     */
    public static String getLibTagsJS(){
        //String tags = "";
        //tags +=  "<!-- todo, JS importing code -->";
        
        String relativeFolder = ResourceRelativeFolderPaths.HTML_INJECT;
        String relativePath = relativeFolder + "/" + "JSLibs.html";
        return getResourceAsString(relativePath);
        
        
        //return "TESTING: getLibTagsJS()";
    }//FUNC::END
    
    /**
     * Converts a resource from your resources folder into a string so that
     * you can use it somewhere. Originally used to inject .css and .html
     * imports into .jsp files.
     * 
     * @param relativePath :A relative path that terminates at a file name.
     *                      Example: /resources/myFiles/file.html
     * @return :The file as a string.
     */
    private static String getResourceAsString(String relativePath){
        InputStream stream;
        
        //stream = LibraryInjection.class.getResourceAsStream(relativePath);
        
        //Another attempt to get resource:
        //http://stackoverflow.com/questions/1108434/howto-load-a-resource-from-web-inf-directory-of-a-web-archive
        //stream = LibraryInjection.class.getClass().getClassLoader().getResourceAsStream(relativePath);
        
        /*
        if(null == servletContext){
            String msg = "Servlet context was null";
            return makeMSG(msg);
        }//Null servlet context?
        stream = servletContext.getResourceAsStream(relativePath);
        */
        
        //http://stackoverflow.com/questions/2797162/getresourceasstream-is-always-returning-null
        //stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(relativePath);
        
        String localhostname;
        try{
            localhostname = InetAddress.getLocalHost().getHostName();
            return("LocalHostName==" + localhostname);
        }catch(Exception e){
            return makeMSG("Failed to get localhost name");
        }
        
        
        
        /*
        if(null == stream){
            String msg = "stream object was null. Is relativePath correct?";
            msg+= "relative path used:[" + relativePath + "]";
            return makeMSG(msg);
        }//Null stream?
        
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(stream));
        String docText = "Was never set";
        try{
            String newLine = System.lineSeparator();
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
               docText = builder.toString();
            }catch(IOException e){
                docText = "getJsonText Failed! e==" + e.toString();
            }//TRY::END
            //SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
        }catch(Exception e){
            return "String builder block failed. Exception:" + e.toString();
        }
        
        return docText;
        */
    }//getResourceAsString
    
    /** Makes text meant for a non-fatal error. Meaning, we want the error
     *  message to print out somewhere without crashing the program.
     * @param msg :Unique error message.
     * @return    :The msg with information associating it with this class
     *             so we can know where the message originated from when
     *             we see it in a string somewhere.
     */
    private static String makeMSG(String msg){
        String whereAreWe = LibraryInjection.class.getCanonicalName();
        String op = "ERROR AT:" + whereAreWe;
        op += msg;
        return op;
    }//FUNC::END
    
}//CLASS::END
