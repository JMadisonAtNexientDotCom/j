package test.transactions.util;

import java.util.List;
import test.MyError;
import test.dbDataAbstractions.entities.bases.BaseEntity;

/**
 * Experimenting with separation of concerns with error checking.
 * Will contain same function names as TransUtil.java, but will be used
 * to DEBUG the inputs.
 * @author jmadison: Original date unknown.
 * @author jmadison: 2015.10.29(Oct29th,Year2015,Thursday)
 */
public class TransUtil_DEBUG {
    
    public static void link(List< BaseEntity> from,
                            String take,
                            List<BaseEntity> into,
                            String dest){
        join(from,into,dest);
        
        //Check variables not covered by join check:
        checkColumnName(take);
    
    }//FUNC::END
    
    private static void checkColumnName(String column){
        if(column.equals("")){doError("[empty string not allowed for column]");}
        if(column.toLowerCase().equals(column)){
            //do nothing. Good!
        }else{
            doError("[Make column name 100% lowercase. Enforced convention.]");
        }//BLOCK::END
    }//FUNC::END
    
    public static void join(List< BaseEntity> from, 
                            List< BaseEntity> to, 
                            String column){
        
        checkColumnName(column);
        
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
