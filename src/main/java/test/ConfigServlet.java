package test;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException; //-------------tomcat/lib/servlet-api.jar

//345678901234567890123456789012345678901234567890123456789012345678901234567890
/**##########################CLASS HEADER FILE##################################
//WHAT THIS CLASS DOES:
//A servlet that should boot up before all of the other servlets and
//do any necessary configurations!
// 
//ORIGINAL USE CASE:
//Testing to see if it could be done.
//Now using to hack some initial states I want during server bootup.
//
//DESIGN NOTE (Justifications for why things are the way they are):
//No justifications. This class feels like a hack to me.
//If you have a better way, feel free to try it out.
//
//@author JMadison : 20??.??.??_????AMPM //I don't know when originally made.
//@author JMadison : 2015.09.21_1054AMPM //session_table notes.
//@author XXXXXXXX : 20XX.XX.XX_####AMPM 
//@author XXXXXXXX : 20XX.XX.XX_####AMPM
########10########20########30########40########50########60########70########*/
//-------0---------0---------0---------0---------0---------0---------0---------0
public class ConfigServlet extends HttpServlet{
    @Override 
    public void init()throws ServletException{
        super.init();
        TestConfig.testVar01 = "ConfigServlet.init() was here!";
        
        //TODO HACK:
        //Create initial session in session_table so that the table shows up.
        //For some reason. My session table does not exist. And I do not
        //know why!
        
    }//FUNC::END
}//CLASS::END
