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
//import org.hibernate.cfg.Configuration;

/**
 * http://stackoverflow.com/questions/18736594/location-of-hibernate-cfg-xml-in-project
 * @author jmadison
 */
public class HibernateUtil {
    
    private static Boolean _debug_hasSetupBeenCalled = false;
    private static Boolean _debug_hasStaticInitBeenCalled = false;
    private static String _debug_class_state_msg = "No msg set.";
    private static String _debug_log = "debug_log:";

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
    
    private static SessionFactory sessionFactory;


    public static SessionFactory getSessionFactory() {
        
        //Make sure getter crashes here if trying to return null.
        //If null, ask if we failed to call the static initializer:
        if(null==sessionFactory)
        {
            MyError me;
            String msg = "null==sessionFactory,";
            msg += (_debug_hasStaticInitBeenCalled ? "INIT_YES" : "NO_INIT" );
            msg += ":_debug_class_state_msg==" + _debug_class_state_msg;
            msg += "log:::::";
            msg += _debug_log;
            me = new MyError(msg);
            throw me;
        }
        
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }

    
    
    //SOURCE: http://docs.jboss.org/hibernate/orm/5.0/quickstart/html/
    //Example 4. Obtaining the org.hibernate.SessionFactory
    //JMadison note: IF session factory is set up once for an application...
    //Why is this example method non-static??? I am going to change that.
    protected static void setUp() throws Exception {
	// A SessionFactory is set up once for an application!
	final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
			.configure() // configures settings from hibernate.cfg.xml
			.build();
	try {
		sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
	}
	catch (Exception e) {
		// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
		// so destroy it manually.
		StandardServiceRegistryBuilder.destroy( registry );
	}
        
        //TEST: Make sure sessionFactory within this scope is not null: ////////
        if(null == sessionFactory)
        {
            throw new MyError("setUp() about to exit with NULL sessionFactory");
        }///////////////////////////////////////////////////////////////////////
        //TEST:
        //before we exit, test to make sure getSessionFactory() 
        //is not returning null; ///////////////////////////////////////////////
        _debug_class_state_msg = "in setUp, testing getSessionFactory";
        SessionFactory sf = getSessionFactory();
        if(null == sf){ throw new MyError("sf == null!!");}
        ////////////////////////////////////////////////////////////////////////
        
    }//FUNC:setUp:END
    
    private static void doStaticInit() throws Exception{
        
        _debug_hasStaticInitBeenCalled = true;
        _debug_hasSetupBeenCalled = false;
        setUp();
        _debug_hasSetupBeenCalled = true;
    }
    
    private static void log(String inMSG){
        _debug_log += inMSG + "\n";
    }
}//CLASS::END