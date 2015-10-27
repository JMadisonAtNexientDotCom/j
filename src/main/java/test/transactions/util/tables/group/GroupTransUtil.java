package test.transactions.util.tables.group;

import java.util.List;
import org.hibernate.Session;
import test.MyError;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import test.dbDataAbstractions.entities.tables.GroupTable;
import test.transactions.util.TransUtil;

/**
 *
 * @author jmadison :2015.10.19
 */
public class GroupTransUtil {
    
    /**
     * 
     * @param name    :Name of the group. Usually table using the group.
     * @param checksum:How many records belong to the group.
     * @return        :The ID of the new group made.
     */
    public static long makeNewGroup(String name, long checksum){
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();
        
        GroupTable gt = new GroupTable();
        gt.checksum = checksum;
        gt.name     = name;
        gt.setComment("[TouchedBy: GroupTransUtil.makeNewGroup()]");
        ses.save(gt); //force update.
        //ses.flush(); <--might be needed for making multiple entries in same session.
        
        return gt.getId();
    }//FUNC::END
    
    //Returns the checksum of group.
    //If you provide a back groupID, will throw error:
    public static long getChecksumOfGroup(long groupID){
        TransUtil.insideTransactionCheck();
        Class  table = GroupTable.class;
        String column = GroupTable.ID_COLUMN;
        List<BaseEntity> bel = 
                           TransUtil.getEntitiesUsingLong(table,column,groupID);
        if(bel.size() != 1){
            String msg = "";
            msg+="[Problem with getChecksumOfGroup]";
            if(bel.size() > 1){
                msg+="[Multiple entries with same group id]";
            }else
            if(bel.size() <= 0){
                msg+="[No entries with that group id.]";
            }//
            doError(msg);
        }//
        
        GroupTable gt = (GroupTable)bel.get(0);
        long checksum = gt.checksum;
        if(checksum <= 0){
            doError("invalid checksum stored");
        }//
        
        return checksum;
            
    }//FUNC::END
    
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = GroupTransUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
