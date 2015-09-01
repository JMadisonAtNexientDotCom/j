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

    //Static initializer for class:
    static{/////////////////////////////////////////////////////////////////////
        try {
            doStaticInit();
        } catch (Exception ex) {
            Logger.getLogger(HibernateUtil.class.getName()).log(Level.
                                                              SEVERE, null, ex);
        }
    }///////////////////////////////////////////////////////////////////////////
    
    private static SessionFactory sessionFactory;
    
    /* This code was from another tutorial.
    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration  configuration = new Configuration().configure( "resources\\hbm.cfg.xml");
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    */

    public static SessionFactory getSessionFactory() {
        
        //Make sure getter crashes here if trying to return null.
        //If null, ask if we failed to call the static initializer:
        if(null==sessionFactory)
        {
            MyError me;
            String msg = "null==sessionFactory,";
            msg += (_debug_hasStaticInitBeenCalled ? "INIT_YES" : "NO_INIT" );
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
        
        if(null == sessionFactory)
        {
            throw new MyError("setUp() about to exit with NULL sessionFactory");
        }
        
    }//FUNC:setUp:END
    
    private static void doStaticInit() throws Exception{
        
        _debug_hasStaticInitBeenCalled = true;
        _debug_hasSetupBeenCalled = false;
        setUp();
        _debug_hasSetupBeenCalled = true;
    }
}//CLASS::END