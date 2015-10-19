package test.transactions.util.tables.group;

import org.hibernate.Session;
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
}//CLASS::END
