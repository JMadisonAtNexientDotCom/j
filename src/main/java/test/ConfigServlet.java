package test;

import frontEndBackEndIntegration.I;
import java.io.InputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException; //-------------tomcat/lib/servlet-api.jar

import test.config.constants.ResourceRelativeFolderPaths;
import utils.files.FileToTextUtil;

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
//NEW USE CASE: 2015.09.25:
//Caching resources that should be available to me in WEB-INF folder.
//I can get the resources from a servlet... But not otherwise.
//Spent whole day looking for help and tried a few things.
////stream = servletContext.getResourceAsStream(relativePath);
////stream = LibraryInjection.class.getClass().getClassLoader().getResourceAsStream(relativePath);
////stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(relativePath);
//There were even people claiming that this is the way to do it if your
//resources are packed in a .WAR file. But doesn't work for me. Could be
//a tomcat security problem? I could use the openshift data directory. But
//that would make the app dependent on running on an openshift server.
//SO: For my classes that need to INJECT HTML into JSP files... This servlet
//    will boot up and INJECT the HTML into the registry classes, where it will
//    then be available.
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
        
        //The vital piece that gives us access to WEB-INF:
        ServletContext ctx = this.getServletContext();
        
        //Inject our front-end-back-end-integration utility. IIIIIIIIIIIIIIIIIII
        //I == INTEGRATION. //IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
        String HTML_INJECT = ResourceRelativeFolderPaths.HTML_INJECT;
        String path_css = HTML_INJECT + "/" + "CSSLibs.html";
        String path_js  = HTML_INJECT + "/" + "JSLibs.html";

        String css = FileToTextUtil.getUsingServletContext(path_css, ctx);
        String js  = FileToTextUtil.getUsingServletContext(path_js, ctx);
        I.INCLUDE_CSS = css;
        I.INCLUDE_JS  = js;
        //IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
        
        //TODO HACK:
        //Create initial session in session_table so that the table shows up.
        //For some reason. My session table does not exist. And I do not
        //know why!
        
    }//FUNC::END
}//CLASS::END
