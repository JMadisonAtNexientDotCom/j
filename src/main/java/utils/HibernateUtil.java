package utils;

import java.io.File;
import java.net.URI;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import test.MyError;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import org.hibernate.boot.Metadata;
import test.dbDataAbstractions.entities.tables.AdminTable;
import test.dbDataAbstractions.entities.tables.NinjaTable;
import test.dbDataAbstractions.entities.tables.OwnerTable;
import test.dbDataAbstractions.entities.tables.RhymeTable;
import test.dbDataAbstractions.entities.tables.RiddleRhymeTruthTable;
import test.dbDataAbstractions.entities.tables.RiddleRhymeWrongTable;
import test.dbDataAbstractions.entities.tables.RiddleTable;
import test.dbDataAbstractions.entities.tables.SessionTable;
import test.dbDataAbstractions.entities.tables.TestTable01;
import test.dbDataAbstractions.entities.tables.TokenTable;
import test.debug.debugUtils.entity.EntityColumnDebugUtil;

/**-----------------------------------------------------------------------------
 * JMADISON NOTE: When I first deploy the app, and make multiple API calls
 * it seems like the import.sql is being imported MULTIPLE times.
 * Is that because this class needs to be synchronized so only one thread
 * can access it at a time?
 * 
 * http://stackoverflow.com/questions/18736594/
 *                                      location-of-hibernate-cfg-xml-in-project
 * @author jmadison : DAY/TIME unknown.
 * @author jmadison : UPDATED: 2015.09.17_0247PM
 ----------------------------------------------------------------------------**/
public class HibernateUtil {
    
    //declare all static variables BEFORE the static initializer.
    //will this help?
    private static Boolean _debug_hasSetupBeenCalled = false;
   // private static Boolean _debug_hasStaticInitBeenCalled = false;
    private static String _debug_class_state_msg = "No msg set.";
    private static String _debug_log = "debug_log:";
    private static SessionFactory _sessionFactory    = null;
    private static Boolean        _hasSessionFactory = false;
    
    /**-------------------------------------------------------------------------
     * Why synchronized? By JMadison:
     * -------------------------------------------------------------------------
     * When the application server first starts up, it is LAZY initialized
     * upon the first API call (or other request) to the application.
     * 
     * IF concurrent requests occur before the app has been initialized.
     * The lazy initialization will happen multiple times, at the same time,
     * on different threads.
     * 
     * How do I know this? Saved multiple API urls to firefox favorites.
     * Then selected to open them ALL AT ONCE right after I restarted the
     * server. The problem with import.sql could then be seen in PHPMyAdmin.
     * 
     * This causes import.sql to be invoked multiple times.
     * -------------------------------------------------------------------------
     * 
     * 
     * @return : A session factory. That handles making of sessions when
     *           new request comes in to the server.
     ------------------------------------------------------------------------**/
    public synchronized static SessionFactory getSessionFactory() {
        
        //Variable integrity check:
        throwErrorIfBadSessionFactoryRef();
        
        if(false == _hasSessionFactory)
        { 
            //try{ setUp();}catch(Exception e){System.out.println(e);}
            setUp();
        }
        
        //Make sure getter crashes here if trying to return null.
        //If null, ask if we failed to call the static initializer:
        if(null==_sessionFactory)
        {//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            MyError me;
            String msg = "getSessionFactory() FAILED! :: null==_sessionFactory,";
            msg += ("_hasSessionFactory ==" + _hasSessionFactory);
            //msg += (_debug_hasStaticInitBeenCalled ? "INIT_YES" : "NO_INIT" );
            msg += ":_debug_class_state_msg==" + _debug_class_state_msg;
            msg += "log:::::";
            msg += _debug_log;
            me = new MyError(msg);
            throw me;
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //Variable integrity check:
        throwErrorIfBadSessionFactoryRef();
        
        return _sessionFactory;
    }//FUNC::END

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }//FUNC::END

    
    
    //SOURCE: http://docs.jboss.org/hibernate/orm/5.0/quickstart/html/
    //Example 4. Obtaining the org.hibernate.SessionFactory
    //JMadison note: IF session factory is set up once for an application...
    //Why is this example method non-static??? I am going to change that.
    protected static void setUp() {
	// A SessionFactory is set up once for an application!
        
        //Configuring using a FILE REFERENCE looks like a good idea to me.
        //Will probably allow me to know if the reference is bad.
        //http://stackoverflow.com/questions/20063330/
        //                 how-to-load-hibernate-cfg-xml-from-different-location
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
        //than a file object. But why would .configure not complainin the first 
        //place if it was given bad data? Not sure.
        //.configure : configures settings from hibernate.cfg.xml
	final StandardServiceRegistry registry = 
            new StandardServiceRegistryBuilder().configure(handMadeRelativePath)
            .build();
        
        MetadataSources mds = new MetadataSources(registry);

        annotationConfigurationStep(mds);

        Metadata md = mds.buildMetadata();
        _sessionFactory = md.buildSessionFactory();
        _hasSessionFactory = true;
        
    }//FUNC:setUp:END
    
    /** Links the annotations to hibernate. (or rather, prepares)---------------
     *  And then debugs to make sure our @Column annotations fit the
     *  convention of variable name must match column name.
     * @param mds :Object to apply described operation to.
     ------------------------------------------------------------------------**/
    private static void annotationConfigurationStep(MetadataSources mds){
        
        //Register annotations with HIBERNATE and our DEBUGGER utility:
        addAnnotations(mds);
        
        //Use debug utility to make sure everything is setup correctly:
        EntityColumnDebugUtil.doIntegrityCheck();
    }//FUNC::END
    
    /** This function replaces the entity mappings that were in-----------------
     *  hbm.cfg.xml.
     *  Example of such xml mapping:
     *  <mapping class="com.richCompany.moneyApp.Billing"/>
     * @param mds : The MetadataSources that replaces the old
     *              AnnotationConfiguration object in previous
     *              versions of hibernate. ----------------------------------**/
    private static void addAnnotations(MetadataSources mds){
        addAnnoEntity(mds, TestTable01.class );
        addAnnoEntity(mds, TokenTable .class );
        addAnnoEntity(mds, NinjaTable .class );
        addAnnoEntity(mds, RiddleTable.class );
        addAnnoEntity(mds, RhymeTable .class );
        addAnnoEntity(mds, RiddleRhymeTruthTable.class);
        addAnnoEntity(mds, RiddleRhymeWrongTable.class);
        addAnnoEntity(mds, AdminTable.class);
        addAnnoEntity(mds, SessionTable.class);
        addAnnoEntity(mds, OwnerTable.class);
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Adds annotated entity class to MetadataSources, but ALSO
     * registers that class with my debug code so that I can make sure that
     * this constraint is enforced: 
     * 
     * Column annotation name used == the actual column name in database.
     * Variable name used == the actual column in database.
     * Have everything MATCH EXACTLY. This makes criteria queries much
     * easier to pull off.
     * @Column(name="column_name") private long column_name;
     *
     * @param mds :The object we are injecting with annotation config info.
     * @param annotatedEntityClass :The class to inject into mds.
     ------------------------------------------------------------------------**/
    private static void addAnnoEntity
                              (MetadataSources mds, Class annotatedEntityClass){
        mds.addAnnotatedClass(annotatedEntityClass);
        EntityColumnDebugUtil.addAnnotatedEntityClass(annotatedEntityClass);
    }//FUNC::END
    
    /** Checks to make sure the _has variable matches the reference var. **/
    private static void throwErrorIfBadSessionFactoryRef(){
        if( (true==_hasSessionFactory) && (null == _sessionFactory))//----------
        {
            String e1;
            e1 = "_sessionFactory==null, yet _hasSessionFactory==true";
            throw new MyError(e1);
        }else
        if( (false==_hasSessionFactory) && (null != _sessionFactory))
        {
            String e2;
            e2 = "_sessionFactory!=null, but _hasSessionFactory==false";
            throw new MyError(e2);
        }//---------------------------------------------------------------------
    }//FUNC::END
    
    private static void log(String inMSG){
        _debug_log += inMSG + "\n";
    }
}//CLASS::END