package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.servlet.ServletException; //-------------tomcat/lib/servlet-api.jar
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.HibernateUtil;
import org.hibernate.SessionFactory;

//import org.hibernate.tutorial.hbm.Event;

/**
 *
 * @author jmadison
 */
public class TestServlet extends HttpServlet{
    
    /** http://www.mkyong.com/servlet/a-simple-servlet-example-write-deploy-run/
     *  **/
    
    @Override
     public void doGet(HttpServletRequest request, 
                       HttpServletResponse response)
                throws ServletException, IOException
     {
        //test hibernate and see if you can get a simple transaction
        //to SQL database.
        TestTransaction.doTestTransaction();
         
         
            try{
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<h1>" + getTestConfigMSG() + "</h1>");
		out.println("</body>");
		out.println("</html>");	
            }catch(IOException e){
               //do nothing.
                System.out.println(e);
            }
       
     }
     
     private static String getTestConfigMSG(){
         return TestConfig.testVar01;
     }
}//CLASS::END
