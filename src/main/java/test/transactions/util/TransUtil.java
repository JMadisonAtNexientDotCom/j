package test.transactions.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import test.entities.BaseEntity;
import utils.HibernateUtil;

/**
 * TransUtil stands for: "Transaction Utility"
 * A utility used to remove boiler plate code from transactions.
 * @author jmadison: 2015.09.02_0354PM **/
public class TransUtil {
    
    /** Boilerplate code for beginning a transaction.
     * @return : Returns object representing the transaction we have entered **/
    public static Session enterTransaction(){
        
        //Create new session factory:
        SessionFactory sf = HibernateUtil.getSessionFactory();
        
        //Open a session and ready it for a transaction:
        Session session   = sf.openSession();
        session.beginTransaction();
        
        //Return the session that represents our transaction we are entering:
        return session;
    }//END::FUNC
    
    
    
    /** Saves, Commits, and Closes the session.
     *  Basically, the boiler-plate code for finalizing
     *  a transaction done on an entity.
     * @param ses :The session object we want to perform this operation on. 
     * @param ent :The entity object we want to perform this operation on. **/
    public static void exitTransaction(Session ses, BaseEntity ent)
    {
        ses.save( ent );
        ses.getTransaction().commit();
        ses.close();
    }//END::FUNC
}//END::CLASS
