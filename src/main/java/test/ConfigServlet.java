
package test;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException; //-------------tomcat/lib/servlet-api.jar

/**
 * A servlet that should boot up before all of the other servlets and
 * do any necessary configurations!
 * @author jmadison
 */
public class ConfigServlet extends HttpServlet{
    @Override 
    public void init()throws ServletException{
        super.init();
        TestConfig.testVar01 = "INITED!!!";
    }
}
