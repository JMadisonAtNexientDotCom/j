package test.servlets.testServlets;

import test.entities.BaseEntityContainer;
import javax.servlet.ServletException; //-------------tomcat/lib/servlet-api.jar
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import test.TestTransaction;
import test.transactions.token.TokenTransactionUtil;
import test.entities.TokenTable;


/**
 *
 * @author jmadison
 */
public class TokenHardCodedFetchTestServlet extends HttpServlet{
    

    @Override
     public void doGet(HttpServletRequest request, 
                       HttpServletResponse response)
                throws ServletException, IOException
     {
         
        String hardCodedTokenName = "superToken09";
       
        BaseEntityContainer bec;
        bec = TokenTransactionUtil.getTokenEntityUsingTokenString(hardCodedTokenName);
         
        String msg = "none set.";
        if(bec.exists)
        {
            TokenTable tt = (TokenTable)bec.entity;
            msg = tt.getComment();
        }
        else
        {
            msg = "token not found:[" + hardCodedTokenName + "]";
        }
        
	PrintWriter out = response.getWriter();
	out.println(msg);
		
     }//FUNC::END
}//CLASS::END