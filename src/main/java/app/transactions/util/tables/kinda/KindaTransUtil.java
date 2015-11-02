package app.transactions.util.tables.kinda;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import app.MyError;
import app.dbDataAbstractions.entities.EntityUtil;
import app.dbDataAbstractions.entities.bases.BaseEntity;
import app.dbDataAbstractions.entities.bases.ChallengeGuts;
import app.dbDataAbstractions.entities.containers.BaseEntityContainer;
import app.dbDataAbstractions.entities.tables.KindaTable;
import app.transactions.util.TransUtil;
import app.transactions.util.forNoClearTableOwner.ChallengeGutsReaderUtil;

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
        
        String kind_of_trial = kt.kind;
        long challenge_id    = kt.challenge_id;
        if(null==kind_of_trial || kind_of_trial.equals("")){
            doError("invalid kind found");
        }
        if(challenge_id <= 0){doError("invalid challenge id. Lazy fetch?");}
        
        
        //doError("Make the class below that is commented out");
        ChallengeGuts op;
        op = ChallengeGutsReaderUtil.findAndRead(kind_of_trial, challenge_id);
        if(null==op){doError("[challenge guts null]");}
        return op;
        
        //STEP1: Get the correct challenge guts tableEntity:
        
        //STEP2: read from database using entity.
        //       (read == inverse of persisting data)
        //       (Kind of like de-serializing. But not completely analagous.)
    }//FUNC::END
    
    /**
     * Returns a BaseEntityContainer that may or may not contain entity
     * if the token_id exists in the table.
     * @param token_id :Token_id foreign key to identify record with.
     * @return :A single entity. Kinda table should only have one record
     *          with that token_id. If more, that is an error.
     */
    public static BaseEntityContainer getKindaUsingTokenID(long token_id){
        
        List<BaseEntity> bel = TransUtil.getEntitiesUsingLong
                       (KindaTable.class, KindaTable.TOKEN_ID_COLUMN, token_id);
        
        int bel_size = bel.size();
        if(bel_size > 1){
            doError("[Only one kinda record per token_id allowed!]");
        }else
        if(1==bel_size){
            return BaseEntityContainer.make(bel.get(0));
        }else
        if(0==bel_size){
            return BaseEntityContainer.makeEmpty();
        }else{
            doError("[This should be dead code]");
        }//FUNC::END
        
        //We should never get to this line of execution:
        doError("[This should also be dead code]");
        return null;

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
