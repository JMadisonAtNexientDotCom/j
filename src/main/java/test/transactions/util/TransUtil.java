package test.transactions.util;

import test.MyError;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import test.entities.BaseEntity;
import utils.HibernateUtil;

/**
 * TransUtil stands for: "Transaction Utility"
 * A utility used to remove boiler plate code from transactions.
 * @author jmadison: 2015.09.02_0354PM **/
public class TransUtil {
    
    /** If the application is single threaded, then we can implement
     *  one very helpful piece of error checking:
     *  We can check for balance/pairing between
     *  entering a transaction and exiting a transaction. */
    public static final Boolean isSingleThreaded = true;
    
    /** Variable that helps for catching errors. Only used when
     *  isSingleThreaded == true, because this debugging logic may
     *  not hold up in multi-threaded environments.
     * 
     *  Makes sure you cannot make stupid mistakes like:
     *  1. calling enterTransaction 2X in a row.
     *  2. calling exitTransaction 2X in a row.
     *  3. calling enter/exit in wrong order.
     *  4. forgetting to close a transaction.
     *  5. forgetting to open a transaction. */
    private static Boolean areWeInTransaction = false;
    
    /** Boilerplate code for beginning a transaction.
     * @return : Returns object representing the transaction we have entered **/
    public static Session enterTransaction(){
        
        //Checks pairing of enterTransaction with exitTransaction:
        enterExitErrorCheck(true);
        
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
        //Checks pairing of enterTransaction with exitTransaction:
        enterExitErrorCheck(true);
        
        ses.save( ent );
        ses.getTransaction().commit();
        ses.close();
    }//END::FUNC
    
    private static void enterExitErrorCheck(Boolean isEntering){
        
        //This error check cannot be done reliably in
        //a multi-threaded environment. Exit if in multi-threaded environment.
        if(false == isSingleThreaded){ return;}
        
        if(isEntering)
        {//we are entering a transaction:
            if(areWeInTransaction) //<--already inside a transaction?
            {
                String msg01 = "";
                msg01 += "balancing broken:";
                msg01 += "ENTERING transaction multiple times in a row.";
                throw new MyError(msg01);
            }
            else
            {
                areWeInTransaction = true;
            }
        }
        else
        {//we are exiting a transaction:
            if(false == areWeInTransaction) //<--already OUTSIDE a transaction?
            {
                String msg02 = "";
                msg02 += "balancing broken:";
                msg02 += "EXITING transaction multiple times in a row.";
                throw new MyError(msg02);
            }
            else
            {
                areWeInTransaction = false;
            }
        }
    }//END::FUNC 
}//END::CLASS
