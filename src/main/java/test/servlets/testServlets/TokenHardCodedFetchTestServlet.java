package test.servlets.testServlets;

import test.dbDataAbstractions.entities.containers.BaseEntityContainer;
import javax.servlet.ServletException; //-------------tomcat/lib/servlet-api.jar
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import test.TestTransaction;
import test.transactions.util.forOwnedMainlyByOneTable.token.TokenTransUtil;
import test.dbDataAbstractions.entities.tables.TokenTable;
import test.transactions.util.TransUtil;
import org.hibernate.Session;
import test.debug.DebugConsts;

/**
 * A hard-coded fetch of a token. One of the tiny wins needed to work up
 * to an awesome app. Much like scaffolding, this method may become useless
 * once the app is finished.
 * @author jmadison **/
public class TokenHardCodedFetchTestServlet extends HttpServlet{
    

    @Override
     public void doGet(HttpServletRequest request, 
                       HttpServletResponse response)
                throws ServletException, IOException
     {
         
        //Enter a transaction state:
        Session ses = TransUtil.enterTransaction();
         
        //perform transaction:
        String hardCodedTokenName = DebugConsts.HARD_CODED_TOKEN;
        BaseEntityContainer bec;
        bec = TokenTransUtil.getTokenEntityUsingTokenString(hardCodedTokenName);
        
        //exit the transaction state, no saving is required:
        TransUtil.exitTransaction(ses,false);
         
        //Display message to user:
        ////////////////////////////////////////////////////////////
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
        ////////////////////////////////////////////////////////////	
     }//FUNC::END
}//CLASS::END