/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import test.MyError;
import org.hibernate.cfg.Configuration;
//import org.hibernate.cfg.Configuration;

/**
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
            try{ setUp();}catch(Exception e){System.out.println(e);}
        }
        testSessionFactoryReferenceIntegrity();
        
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
    protected static void setUp(){
         Configuration configuration = new Configuration();
         configuration.configure("resources//hbm.cfg.xml");
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
    
    //SOURCE: http://docs.jboss.org/hibernate/orm/5.0/quickstart/html/
    //Example 4. Obtaining the org.hibernate.SessionFactory
    //JMadison note: IF session factory is set up once for an application...
    //Why is this example method non-static??? I am going to change that.
    protected static void setUpOld() throws Exception {
	// A SessionFactory is set up once for an application!
        
	final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
			.configure("resources//hbm.cfg.xml") // configures settings from hibernate.cfg.xml
			.build();
	try {
		_sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
                _hasSessionFactory = true;
	}
	catch (Exception e) {
		// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
		// so destroy it manually.
                _hasSessionFactory = false;
                _sessionFactory    = null;
		StandardServiceRegistryBuilder.destroy( registry );
                
	}
        
        testSessionFactoryReferenceIntegrity();
        
    }//FUNC:setUp:END
   
    /** TEST FUNCTION: Will crash on error. **/
    private static void testSessionFactoryReferenceIntegrity(){
        
        //This guard is here because setUp() is allowed to fail.
        //If setup fails, _hasSessionFactory == false, and setUp()
        //Will be called again when trying to use the getSessionFactory()
        //method.
        if(_hasSessionFactory)
        {
            //TEST: Make sure _sessionFactory within this scope is not null: ///
            if(null == _sessionFactory)
            {
                throw new MyError("setUp() about to exit with +" + 
                                                        "NULL _sessionFactory");
            }///////////////////////////////////////////////////////////////////
            //TEST:
            //before we exit, test to make sure getSessionFactory() 
            //is not returning null; ///////////////////////////////////////////
            _debug_class_state_msg = "in setUp, testing getSessionFactory";
            SessionFactory sf = getSessionFactory();
            if(null == sf){ throw new MyError("sf == null!!");}
            ////////////////////////////////////////////////////////////////////
        }//_hasSessionFactory?
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