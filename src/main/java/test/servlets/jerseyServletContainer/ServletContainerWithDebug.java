
package test.servlets.jerseyServletContainer;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import java.io.IOException;
import java.net.URI;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import test.debug.GlobalErrorState;

/**
 * A servlet container that is built upon the Jersey Servlet Container.
 * This is my way of creating "before" advice that will check the global
 * error state of the application every time a request is made. If anything
 * is in the global error state, the app will crash.
 * @author jmadison
 */
public class ServletContainerWithDebug extends ServletContainer {
    
    //The service method from WebComponent.java which ServletContainer.java
    //extends.
    @Override
    public int service(URI baseUri, URI requestUri, HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {
       
        //INTERCEPT:
        beforeAdvice();
        
        return super.service(baseUri, requestUri, request, response);
    }//FUNC::END
    
    @Override
    public void service(ServletRequest request, ServletResponse response) 
                                          throws ServletException, IOException {
        
        //INTERCEPT:
        beforeAdvice();
        
       super.service(request, response);
    }//FUNC::END
    
    @Override
    public void service(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {
        
        //INTERCEPT:
        beforeAdvice();
        
        super.service(request, response);
    }//FUNC::END
    
    /**
     * Causes servlet to crash if anything stored in the global error state.
     * Done to make absolutely sure program will not run with errors.
     */
    private void beforeAdvice(){////////////
        GlobalErrorState.throwIfHasErrors();
    }///////////////////////////////////////
    
}//FUNC::END
