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
import test.config.constants.ResourceRelativeFolderPaths;


/**
 *
 * @author jmadison
 */
public class LibraryInjection {
   
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
        InputStream stream;
        stream = LibraryInjection.class.getResourceAsStream(relativePath);
        
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
    }//FUNC::END
    
}//CLASS::END
