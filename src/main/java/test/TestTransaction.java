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
        
        LogDemo.doIt();
        
        
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session   = sf.openSession();
        session.beginTransaction();
        //session.save( new Event( "Our very first event!", new Date() ) );
        //session.save( new Event( "A follow up event", new Date() ) );
        
        //do something to an entity:
        TestTable01 tt = new TestTable01();
        tt.setToken("superToken101");
        tt.setTokenMSG("TheTokenMsg");
        
        //save changes performed on entity:
        session.save( tt );
        
        session.getTransaction().commit();
        session.close();
                
        
    }//FUNC::END
}//CLASS::END
