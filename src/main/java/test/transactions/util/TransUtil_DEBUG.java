package test.transactions.util;

import java.util.List;
import test.MyError;
import test.dbDataAbstractions.entities.bases.BaseEntity;

/**
 * Experimenting with separation of concerns with error checking.
 * Will contain same function names as TransUtil.java, but will be used
 * to DEBUG the inputs.
 * @author jmadison
 */
public class TransUtil_DEBUG {
    
    public static void join(List< BaseEntity> from, 
                            List< BaseEntity> to, 
                            String column){
        
        /* DELETE THIS BLOCK
        if(null==ids || null == from || null == to || null == column){
            doError("[Null:ids||from||to||column]");
        }//
        if(ids.isEmpty()){doError("[trying join with empty list.]");}
        */
      
        if(column.equals("")){doError("[empty string not allowed for column]");}
        if(column.toLowerCase().equals(column)){
            //do nothing. Good!
        }else{
            doError("[Make column name 100% lowercase. Enforced convention.]");
        }//BLOCK::END
        
        if(from.size() != to.size()){
            doError("[Entities Must be evenly paired.]");
        }//
        
        if(from.isEmpty() || to.isEmpty()){
            doError("[neither list can be empty]");
        }//
        
        //check all IDS and make sure none are ZERO.
        int len = to.size();
        for(int i = 0; i < len; i++){
            //Class<? extends BaseEntity> f = from.get(i);
            //Class<? extends BaseEntity> t = to.get(i);
            BaseEntity f = from.get(i);
            BaseEntity t = to.get(i);
            if(null == f){doError("[From entry null]");}
            if(null == t){doError("[To entry null]");}
            long theID = f.getId();
            if(theID <= 0){doError("[invalid primary key ID]");}
        }//next id value.
        
        
        /* DELETE THIS BLOCK
        //check all IDS and make sure none are ZERO.
        int len = ids.size();
        for(int i = 0; i < len; i++){
            Long cur = ids.get(i);
            if(null == cur){doError("[Null val found in ids!]");}
            if(0 == cur){
                doError("[Zero forbidden and indicative of lazy fetch error]");
            }//
            if(cur < 0){
                doError("[NegForbidden. Maybe got from using error object?]");
            }//
        }//next id value.
        */
        
    }//WRAPPER::END   
    
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = TransUtil_DEBUG.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS:END
