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
import test.transactions.util.TransUtil;


/**
 *
 * @author jmadison
 */
public class TestTransaction {
    
    //Code originally from:
    //http://docs.jboss.org/hibernate/orm/5.0/quickstart/html/
    //Example 5. Saving entities
    public static void doTestTransaction(){
        
       //REFACTORING TO USE TransUtil
       
       // SessionFactory sf = HibernateUtil.getSessionFactory();
       // Session session   = sf.openSession();
       // session.beginTransaction();
        Session ses = TransUtil.enterTransaction();
        
        //do something to an entity:
        TestTable01 ent = new TestTable01();
        ent.setToken("superToken03");
        ent.setTokenMSG("The message. THREE");
        
        //save changes performed on entity:
       // session.save( tt );
       // session.getTransaction().commit();
        //session.close();
        TransUtil.exitTransaction(ses,ent);
        
        
    }//FUNC::END
}//CLASS::END
