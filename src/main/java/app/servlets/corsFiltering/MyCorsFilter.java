package app.servlets.corsFiltering;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Adding a cors filter to allow "cross origin resource sharing".
 * IT WORKS!
 * SRC URL: http://stackoverflow.com/questions/30974730/
 *                                                   jersey-cross-domain-request
 * @author jmadison: 2015.09.15_1104AM**/
public class MyCorsFilter implements Filter {

    public MyCorsFilter() { }

    @Override
    public void init(FilterConfig fConfig) throws ServletException { }

    @Override
    public void destroy() { }

    @Override
    public void doFilter
        (ServletRequest request, ServletResponse response, FilterChain chain) 
                                         throws IOException, ServletException  {
        String accessSlug = "Access-Control-Allow-Origin";
      ((HttpServletResponse)response).addHeader(accessSlug, "*");
        
        //MIGHT NOT BE NEEDED!!! I think I forgot to push to server before
        //I tried my HTTP POST.
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //Added to make HTTP POST work:
        //http://www.codingpedia.org/ama/how-to-add-cors-support-on-the-server-side-in-java-with-jersey/
        String methodSlug = "Access-Control-Allow-Methods";
        String allowedMethods = "GET, POST, DELETE, PUT";
        ((HttpServletResponse)response).addHeader(methodSlug, allowedMethods);
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        
        //Fixing this error:
        //Request header field Content-Type is not allowed by Access-Control-Allow-Headers
        String headerSlug = "Access-Control-Allow-Headers";
        String allowedHeaders = "Content-Type";
        ((HttpServletResponse)response).addHeader(headerSlug, allowedHeaders);
     
        //Huntch that this belongs below all of the header setting:
        chain.doFilter(request, response);
      
    }//FUNC::END
}//CLASS::END
