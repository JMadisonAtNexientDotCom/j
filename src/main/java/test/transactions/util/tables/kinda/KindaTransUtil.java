package test.transactions.util.tables.kinda;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import test.MyError;
import test.dbDataAbstractions.entities.EntityUtil;
import test.dbDataAbstractions.entities.bases.ChallengeGuts;
import test.dbDataAbstractions.entities.containers.BaseEntityContainer;
import test.dbDataAbstractions.entities.tables.KindaTable;
import test.transactions.util.TransUtil;

/**
 * 
 * @author jmadison:2015.10.27(Oct27th,Year2015.Tuesday)
 */
public class KindaTransUtil {
    
    /**
     * Create a batch of KindaTable record stubs.
     * 
     * THIS CLASS DOES PERSIST THE OBJECTS MADE!
     * I didn't want to, because that means if something goes wrong, we will
     * have junk data in the database. But we need to work with the primary 
     * keys, so they must be persisted.
     * 
     * @param numToMake :How many do you want?
     * @return :The entities requested. */
    public static List<KindaTable> makeBatchOfKindaStubs(int numToMake){
        
        TransUtil.insideTransactionCheck();
        List<KindaTable> op;
        op = EntityUtil.makeEntitiesFromClass_PersistIDS
                                                  (KindaTable.class, numToMake);
        return op;
    }//FUNC::END
    
    /** Makes a new entity. And saves it so it's primary key is generated.**/
    public static KindaTable makeNextKinda(){
        
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();
        
        KindaTable op;
        op = new KindaTable();
        ses.save(op); //<--so primary key is generated.
        
        return op;
        
    }//FUNC::END
    
    /**
     * Gets the guts of trial/test 's challenge questions so that you may use
     * the information to populate a front-end UI.
     * @param kt :The KindaTable entity used to find the ChallengeGuts.
     * @return   :A ChallengeGuts pojo representing the information 
     *            persisted in the database. **/
    public static ChallengeGuts getChallengeGutsUsingKindaTable(KindaTable kt){
        
        //Stubbing methods for now. Because I want to work on UI stuff.
        doError("TODO: Make getChallengeGutsUsingKindaTable");
        return new ChallengeGuts();
        
    }//FUNC::END
    
    public static BaseEntityContainer getKindaUsingTokenID(long token_id){
        
        //Stubbing methods for now. Because I want to work on UI stuff.
        doError("TODO: getKindaUsingTokenID ");
        return new BaseEntityContainer();
    }//FUNC::END

    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = KindaTransUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
    
}//CLASS::END
