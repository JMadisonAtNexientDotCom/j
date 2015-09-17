/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;
import java.io.File;
import java.net.URI;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import test.MyError;
//import org.hibernate.cfg.Configuration;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import org.hibernate.boot.Metadata;
import test.dbDataAbstractions.entities.tables.NinjaTable;
import test.dbDataAbstractions.entities.tables.RhymeTable;
import test.dbDataAbstractions.entities.tables.RiddleRhymeTruthTable;
import test.dbDataAbstractions.entities.tables.RiddleRhymeWrongTable;
import test.dbDataAbstractions.entities.tables.RiddleTable;
import test.dbDataAbstractions.entities.tables.TestTable01;
import test.dbDataAbstractions.entities.tables.TokenTable;




/**
 * JMADISON NOTE: When I first deploy the app, and make multiple API calls
 * it seems like the import.sql is being imported MULTIPLE times.
 * Is that because this class needs to be synchronized so only one thread
 * can access it at a time?
 * 
 * 
 * 
 * http://stackoverflow.com/questions/18736594/location-of-hibernate-cfg-xml-in-project
 * @author jmadison
 */
public class HibernateUtil {
    
    //declare all static variables BEFORE the static initializer.
    //will this help?
    private static Boolean _debug_hasSetupBeenCalled = false;
   // private static Boolean _debug_hasStaticInitBeenCalled = false;
    private static String _debug_class_state_msg = "No msg set.";
    private static String _debug_log = "debug_log:";
    private static SessionFactory _sessionFactory    = null;
    private static Boolean        _hasSessionFactory = false;

    /*
    Thinking it is a a bad idea to try to access hibernate configuration within
    static initializer.
    //Static initializer for class:
    static{/////////////////////////////////////////////////////////////////////
        log("entering static{} initializer block");
        try {
            log("about to try: doStaticInit()");
            doStaticInit();
        } catch (Exception ex) {
            log("failed to doStaticInit, catching exception");
            Logger.getLogger(HibernateUtil.class.getName()).log(Level.
                                                              SEVERE, null, ex);
        }
        log("exiting static{} initializer block");
    }///////////////////////////////////////////////////////////////////////////
    */
   


    public static SessionFactory getSessionFactory() {
        
        if(false == _hasSessionFactory)
        { 
            //try{ setUp();}catch(Exception e){System.out.println(e);}
            setUp();
        }
        
        //Causes infinite recursion.
        //testSessionFactoryReferenceIntegrity();
        
        //Make sure getter crashes here if trying to return null.
        //If null, ask if we failed to call the static initializer:
        if(null==_sessionFactory)
        {
            MyError me;
            String msg = "getSessionFactory() FAILED! :: null==_sessionFactory,";
            msg += ("_hasSessionFactory ==" + _hasSessionFactory);
            //msg += (_debug_hasStaticInitBeenCalled ? "INIT_YES" : "NO_INIT" );
            msg += ":_debug_class_state_msg==" + _debug_class_state_msg;
            msg += "log:::::";
            msg += _debug_log;
            me = new MyError(msg);
            throw me;
        }
        
        return _sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }

    /** This way on stack overflow seems more sensible. **/
    /*
    //Have NOT found a way to validate the configuration path. So put
    //resources folder  under java folder as a huntch. Then will just try
    //my other setUp() method and put hbm.cfg.xml at root of project or
    //root of WEB-INF 
    //http://stackoverflow.com/questions/4934330/org-hibernate-hibernateexception-hibernate-cfg-xml-not-found
    protected static void setUp(){
         Configuration configuration = new Configuration();
         configuration.configure("resources//hbm.cfg.xml");
         //configuration.configure("sfdsfdsfdsf//sfdjslfj//hbm.cfg.xml");
         StandardServiceRegistryBuilder ssrb = 
            new StandardServiceRegistryBuilder().applySettings(configuration
                                                              .getProperties());
         _sessionFactory = configuration.buildSessionFactory(ssrb.build());
         
         if(null != _sessionFactory)
         {
             _hasSessionFactory = true;
             throw new MyError("session factory is null, abc");
         }
         else
         {
             _hasSessionFactory = false;
         }
    }//setUp
    */
    
    //SOURCE: http://docs.jboss.org/hibernate/orm/5.0/quickstart/html/
    //Example 4. Obtaining the org.hibernate.SessionFactory
    //JMadison note: IF session factory is set up once for an application...
    //Why is this example method non-static??? I am going to change that.
    protected static void setUp() {
	// A SessionFactory is set up once for an application!
        
        //Configuring using a FILE REFERENCE looks like a good idea to me.
        //Will probably allow me to know if the reference is bad.
        //http://stackoverflow.com/questions/20063330/how-to-load-hibernate-cfg-xml-from-different-location
        log("about to set file reference:");
       // File f = new File("D:\\fax\\hibernate.cfg.xml");
        // File f = new File("..\\resources\\hbm.cfg.xml");
        Path relativePath = FileSystems.getDefault().
                                            getPath("webapps", "hbm.cfg.xml");
        Path absPath = relativePath.toAbsolutePath();
        String absolutePathAsString = absPath.toString();
        File f = new File(absolutePathAsString);
        
        //hack, try using relative path string:
        //Not possible....
        
        //hack... Uri? Why not.
        URI uriPath = absPath.toUri();
        String absURIAsString = uriPath.toString();
        
        //hack: try hand-made-relative path:
        //String sep = System.getProperty("file.separator"); //<--http://stackoverflow.com/questions/19762169/forward-slash-or-backslash
        String handMadeRelativePath = "webapps/hbm.cfg.xml";
        
        if(false == f.exists())
        { 
            log("file reference is INVALID PATH");
            throw new MyError("Path is invalid!:" +
                    "absolutePathAsString==[" + absolutePathAsString + "]");
        }
        log("file reference for hbm.cfg.xml was valid!");
        
        //HACK: Throw error no matter what to see what that absolute path is!!
        if(false)
        {
            throw new MyError("Path is VALID!:" +
                    "absolutePathAsString==[" + absolutePathAsString + "]");
        }
        
        //Bug fix maybe? Gave .configure an absolute file PATH-STRING rather
        //than a file object. But why would .configure not complainin the first place
        //if it was given bad data? Not sure.
	final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
			.configure(handMadeRelativePath) // configures settings from hibernate.cfg.xml
			.build();
        
         MetadataSources mds = new MetadataSources(registry);
         addAnnotations(mds);
         Metadata md = mds.buildMetadata();
         _sessionFactory = md.buildSessionFactory();
         
         //UDPATE: Add annotation configuration here?
      
         
         
         _hasSessionFactory = true;
        
        /*
	try {
		//_sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
                MetadataSources mds = new MetadataSources(registry);
                Metadata md = mds.buildMetadata();
                _sessionFactory = md.buildSessionFactory();
                _hasSessionFactory = true;
	}
	catch (Exception e) {
		// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
		// so destroy it manually.
                _hasSessionFactory = false;
                _sessionFactory    = null;
		StandardServiceRegistryBuilder.destroy( registry );
                throw new MyError("setUp failed!");
	}
        */
        
        //test is okay here maybe?
        //NO,not okay here. since the test can call setup.
        //testSessionFactoryReferenceIntegrity();
        
    }//FUNC:setUp:END
    
    /** This function replaces the entity mappings that were in-----------------
     *  hbm.cfg.xml.
     *  Example of such xml mapping:
     *  <mapping class="com.richCompany.moneyApp.Billing"/>
     * @param mds : The MetadataSources that replaces the old
     *              AnnotationConfiguration object in previous
     *              versions of hibernate. ----------------------------------**/
    private static void addAnnotations(MetadataSources mds){
        mds.addAnnotatedClass(TestTable01.class );
        mds.addAnnotatedClass(TokenTable .class );
        mds.addAnnotatedClass(NinjaTable .class );
        mds.addAnnotatedClass(RiddleTable.class );
        mds.addAnnotatedClass(RhymeTable .class );
        mds.addAnnotatedClass(RiddleRhymeTruthTable.class);
        mds.addAnnotatedClass(RiddleRhymeWrongTable.class);
    }//FUNC::END
    
    /** Checks to make sure the _has variable matches the reference var. **/
    private static void throwErrorIfBadSessionFactoryRef(){
        if( (true==_hasSessionFactory) && (null == _sessionFactory))
        {
            throw new MyError("_sessionFactory==null, yet _hasSessionFactory==true");
        }else
        if( (false==_hasSessionFactory) && (null != _sessionFactory))
        {
             throw new MyError("_sessionFactory!=null, but _hasSessionFactory==false");
        }
    }//FUNC::END
   

    
    /*
    //thinking this was a bad idea:
    private static void doStaticInit() throws Exception{
        
        _debug_hasStaticInitBeenCalled = true;
        _debug_hasSetupBeenCalled = false;
        setUp();
        _debug_hasSetupBeenCalled = true;
    }
    */
    
    private static void log(String inMSG){
        _debug_log += inMSG + "\n";
    }
}//CLASS::END