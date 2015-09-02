/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateUtil;
import test.entities.TestTable01;


/**
 *
 * @author jmadison
 */
public class TestTransaction {
    
    //Code originally from:
    //http://docs.jboss.org/hibernate/orm/5.0/quickstart/html/
    //Example 5. Saving entities
    public static void doTestTransaction(){
        
        //http://wiki.apache.org/tomcat/FAQ/Logging#Q6
        //System.out.println("Is this a tomcat log?");
        //LogDemo.doIt();
        
        //Attempt to throw custom error:
        //MyError me = new MyError("Oh noooo!!");
        //throw me;
        
        
        SessionFactory sf = HibernateUtil.getSessionFactory();
        
        //just uncommented this all.
        Session session   = sf.openSession();
        session.beginTransaction();
        //session.save( new Event( "Our very first event!", new Date() ) );
        //session.save( new Event( "A follow up event", new Date() ) );
        
        //do something to an entity:
        TestTable01 tt = new TestTable01();
        tt.setToken("superToken02");
        tt.setTokenMSG("The message. TWO");
        
        //save changes performed on entity:
        session.save( tt );
        
        session.getTransaction().commit();
        session.close();
        
        
    }//FUNC::END
}//CLASS::END
