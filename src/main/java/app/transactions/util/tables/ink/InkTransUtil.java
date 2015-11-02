package app.transactions.util.tables.ink;

import java.util.List;
import app.MyError;
import app.dbDataAbstractions.entities.bases.BaseEntity;
import app.dbDataAbstractions.entities.tables.riddleTrialStore.questionStore.InkTable;
import app.transactions.util.TransUtil;

/**
 * @author jmadison : 2015.10.26 (October26th,Year2015,Monday)
 */
public class InkTransUtil {
    
    
    /**
     * Like "getExistingTableByGroupID" but using the primary key
     * rather than the foreign group_id.
     * @param id :The primary key of the InkTable [entity/record] you want.
     * @return   :Returns the [record/enitity] with that primary key.
     *            If does NOT exist, throws an error. **/
    public static InkTable getInkTableUsingPrimaryKeyID(long id){
        
        List<BaseEntity> bel;
        bel = TransUtil.getEntitiesUsingLong
                                       (InkTable.class, InkTable.ID_COLUMN, id);
        
        InkTable op = extractTheResultFromTheListOfOneEntity(bel);
        
        if(null==op){doError("[Should not be null.K2K3N3L2]");}
        return op;
        
    }//FUNC::END
    
    /**
     * Gets the InkTable entity that has the foreign group_id referencing
     * a group of entries in the InkPurse.
     * @param inGroupID :The groupID to find entry by.
     * @return :The instance of InkTable.
     *          If NOT FOUND, will throw an error, because
     *          we assume it exists.
     */
    public static InkTable getExistingTableByGroupID(long inGroupID){
        
        List<BaseEntity> bel;
        bel = TransUtil.getEntitiesUsingLong
                            (InkTable.class, InkTable.INK_GI_COLUMN, inGroupID);
        
        InkTable op = extractTheResultFromTheListOfOneEntity(bel);
        
        if(null == op){doError("[This should never happen.K2KN3H43ILK2]");}
        return op;
        
    }//FUNC::END
    
    /**
     * Extracts an entity from list, and does some error checking.
     * @param bel :The "base entity list" containing the one entity we want.
     * @return    :The one and only InkTable [record/entity] in list. **/
    private static InkTable extractTheResultFromTheListOfOneEntity
                                                         (List<BaseEntity> bel){
        InkTable op = null;
        int num_results = bel.size();
        if(num_results < 0){
            doError("Expected record to EXIST!");
        }else
        if(num_results > 1){
            doError("Expected only one result in database.");
        }else{
            op = (InkTable)bel.get(0);
        }//
        
        if(null == op){doError("this should never happen.");}
        return op;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = InkTransUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
