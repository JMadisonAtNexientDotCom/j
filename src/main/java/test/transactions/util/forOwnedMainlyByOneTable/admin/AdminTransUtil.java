package test.transactions.util.forOwnedMainlyByOneTable.admin;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import test.MyError;
import test.dbDataAbstractions.entities.containers.BaseEntityContainer;
import test.dbDataAbstractions.entities.tables.AdminTable;
import test.transactions.util.TransUtil;

//345678901234567890123456789012345678901234567890123456789012345678901234567890
/**##########################CLASS HEADER FILE##################################
//WHAT THIS CLASS DOES:
//Utility that handles transactions primarily involving our admin_table.
// 
//ORIGINAL USE CASE:
//Originally used in OwnerRestService.java
//However, it's purpose is to enable us to have admins that can log in
//and grant themselves session tokens. Ninjas (candidates) cannot do this.
//Ninjas do not even have usernames or passwords. They can only get access
//to the system when an admin grants them a token that is active in the
//session_table.
//
//DESIGN NOTE (Justifications for why things are the way they are):
//No justifications. Thinking springs Before and After advice would be
//great for this project. But too late for now. Using Jersey.
//Maybe after project is finished will convert to spring for practice?
//
//@author jmadison : 2015.09.20_0610PM -created.
//@author jmadison : 2015.09.23_0712PM -made getDoesAdminWithThisIDExist func
//@author XXXXXXXX : 20XX.XX.XX_####AMPM 
//@author XXXXXXXX : 20XX.XX.XX_####AMPM
########10########20########30########40########50########60########70########*/
//-------0---------0---------0---------0---------0---------0---------0---------0
public class AdminTransUtil {
    
    
    /**-------------------------------------------------------------------------
     * Do test to see if user exists. If we want to USE the entity
     * corresponding to the user if exists, we should use getAdminEntity()
     * instead. Since this function wraps getAdminEntity().
     * 
     * @param lowerCaseUserName :All lowercase user name.
     * @return :Returns true if user of that name exists in admin_table
     ------------------------------------------------------------------------**/
    public static boolean getDoesUserExist(String lowerCaseUserName){
        
        //check to see if we are in a transaction state:
        TransUtil.insideTransactionCheck();

        //Core logic:
        BaseEntityContainer bec = getAdminEntity(lowerCaseUserName);
        return bec.exists;
        
    }//FUNC::END
    
    /**
     * Gets if an admin with this id exist. 
     * @param admin_id :The admin ID to query.
     * @return :true if admin with this id exists.
     */
    public static boolean getDoesAdminWithThisIDExist(Long admin_id){
        
        //check to see if we are in a transaction state:
        TransUtil.insideTransactionCheck();
        
        BaseEntityContainer bec;
        bec = TransUtil.getEntityFromTableUsingPrimaryKey
                             (AdminTable.class, AdminTable.ID_COLUMN, admin_id);
        return bec.exists;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Use user-name to find admin entity in the admin_table.
     * @param userName : the all lower-case user name of this admin.
     * @return :A container that will contain the entity if the entity
     *          exists in the table.
     ------------------------------------------------------------------------**/
    public static BaseEntityContainer getAdminEntity(String userName){
        
         //ErrorCheck: Make sure userName exists in the token table.
        if(false == isAllLowercase(userName)){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            String msg = "";
            msg+="[All usernames should be stored as]";
            msg+="[lowercase in database.]";
            throw new MyError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //Error check: Make sure we are in transaction state:
        //Then fetch current session:
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();
        
        //If no error, check table to see if user exists:
        //ucase the username, and find that user in the table:
        String lower_name = userName;
        Criteria c = ses.createCriteria(AdminTable.class);
        c.add(Restrictions.eq(AdminTable.USER_NAME_COLUMN, lower_name));
        AdminTable theAdmin = (AdminTable) c.uniqueResult();
        
        // Decide what results to send back:
        //RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
        BaseEntityContainer bec = new BaseEntityContainer();
        if(null == theAdmin){
            bec.entity = null;
            bec.exists = false;
        }else
        if(null != theAdmin){
            bec.entity = theAdmin;
            bec.exists = true;
        }else{
            throw new MyError("this line should never execute.");
        }//RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
        
        //Return the container that
        //may or may not contain the entity.
        return bec;
        
    }//FUNC::END
    
    /**
     * 
     * @param userName: Case insensitive user name. Since servlet will be
     *                  directly wrapping this function.
     * 
     * @param passWord: The un-hashed password that we need to validate
     *                  for this user.
     * @return 
     */
    public static boolean loginValidate(String userName, String passWord){
        
        //check to see if we are in a transaction state:
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();

        //core logic:
        //Convert user name to lowercase.
        //Find the user in the table:
        AdminTable theAdmin;
        String lower_cased_user = userName.toLowerCase();
        BaseEntityContainer bec = getAdminEntity(lower_cased_user);
        if(false == bec.exists){////////////////////////////////////////////////
            return false; //<--unable to validate. User does not exist.
        }else
        if(true == bec.exists){
            theAdmin = (AdminTable)bec.entity;
        }else{
            throw new MyError("This line should never execute.");
        }///////////////////////////////////////////////////////////////////////
        
        //If user exists, we want to get their stored password hash in
        //the database and compare it to the hash resulting from submission.
        String pass_hash_ACTUAL    = theAdmin.getPass_hash();
        String pass_hash_SUBMITTED = getHashedPassword(passWord);
        if(pass_hash_ACTUAL.equals(pass_hash_SUBMITTED)){///////////////////////
            return true;
        }///////////////////////////////////////////////////////////////////////
        
        //return false, the password supplied was invalid.
        return false;
         
    }//FUNC::END
    
     /**
     * @return : TRUE if there is at least one entry in the admin table.
     */
    public static boolean getDoAdminsExist(){
        TransUtil.insideTransactionCheck();
        long amt =  TransUtil.getNumberOfRecordsInTable(AdminTable.class);
        return (amt > 0);
    }//FUNC::END
    
    /**
     * Get a random admin's id from table.
     * @return :id of that random admin.
     */
    public static long getRandomAdminID(){
        TransUtil.insideTransactionCheck();
        BaseEntityContainer bec;
        bec = TransUtil.getRandomRecord(AdminTable.class);
        
        long op;
        if(bec.exists){
            AdminTable admin = (AdminTable)bec.entity;
            op = admin.getId();
        }else{
            op = (-1); //return -1 for no ninja exists.
        }//xx
        
        //return output:
        return op;
    }//FUNC::END
    
    
    /** Pseuedo-hash. Stub function. Offers basic sanitization from
     *  SQL injection. But for the moment, does not actually hide your
     *  password.
     * @param passWord :Password you would like to hash.
     * @return         :Return TRUE if password is valid.
     ------------------------------------------------------------------------**/
    private static String getHashedPassword(String passWord){
        
        /** Q is for QUOTE. **/
        String Q = "\"";
        String op = passWord;
        op = op.replace("'", "q");
        op = op.replace(Q, "Q");
        op = op.replace("#", "H");
        op = op.replace("-", "D");
        op = op.replace("_", "U");
        op = op + "_hashed";
        
        //return hashed password:
        return op;
        
    }//FUNC::END
    
    /**
     * Returns true if string is all lowercase.
     * @param s0 :String to test.
     * @return :Returns TRUE if string was all lowercase.
     */
    private static boolean isAllLowercase(String s0){
        String lower = s0.toLowerCase();
        return lower.equals(s0);
    }//FUNC::END
    
}//CLASS::END
