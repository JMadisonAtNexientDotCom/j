/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontEndBackEndIntegration.childComponents.libInject;

import test.servlets.rest.utilityServlets.FileContentFetcher;

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
        String tags = "";
        tags +=  "<!-- todo, JS importing code -->";
    
        
        return tags;
    }//FUNC::END
    
}//CLASS::END
