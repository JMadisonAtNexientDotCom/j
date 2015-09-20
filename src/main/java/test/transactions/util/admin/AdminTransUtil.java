package test.transactions.util.admin;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import test.dbDataAbstractions.entities.tables.AdminTable;
import test.dbDataAbstractions.entities.tables.TokenTable;
import test.transactions.util.TransUtil;

/**-----------------------------------------------------------------------------
 * Utility that handles transactions primarily involving our admin_table.
 * @author jmadison :2015.09.20_0610PM
 ----------------------------------------------------------------------------**/
public class AdminTransUtil {
    
    public static boolean loginValidate(String userName, String passWord){
        
        //check to see if we are in a transaction state:
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();

        //core logic:
        //Find the user in the table:
        
        //ucase the username, and find that user in the table:
        String lower_name = userName.toLowerCase();
        Criteria c = ses.createCriteria(AdminTable.class);
        c.add(Restrictions.eq(AdminTable.USER_NAME_COLUMN, lower_name));
        AdminTable theAdmin = (AdminTable) c.uniqueResult();
        
        //user does not exist:
        if(null == theAdmin){
            return false;
        }////////////////////
        
        //If user exists, we want to get their stored password hash in
        //the database and compare it to the hash resulting from submission.
        String actual_pass_hash = theAdmin.getPass_hash();
        String submitted_pass_hash = getHashedPassword(passWord);
        if(actual_pass_hash.equals(submitted_pass_hash)){
            return true;
        }/////////////////////////////////////////////////
        
        //return false, the password supplied was invalid.
        return false;
         
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
    
}//CLASS::END
