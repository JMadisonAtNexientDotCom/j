/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
         
            try{
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<h1>Hello Servlet Get</h1>");
		out.println("</body>");
		out.println("</html>");	
            }catch(IOException e){
               //do nothing.
                System.out.println(e);
            }
       
     }
}
