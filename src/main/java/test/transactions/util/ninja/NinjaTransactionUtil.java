package test.transactions.util.ninja;

import org.hibernate.Session;
import test.entities.NinjaTable;
import test.transactions.util.TransUtil;
import test.MyError;
/**
 * Handles [transactions/operations] involving the ninja table.
 * AKA: Our table full of identification info for every ninja that
 * wishes to participate in trials for a chance to become a member of the clan.
 * @author jmadison
 */
public class NinjaTransactionUtil {
    
    /** Makes a new ninja [record/entry] in the ninja table.
     *  But it is up to other logic to populate the record
     *  with information relevant to this new ninja.
     * @return  **/
    public static NinjaTable makeNextNinja(){
        
        //Make sure we are in a transaction state if we are doing this!
        TransUtil.insideTransactionCheck();
        
        //Logic Body:
        NinjaTable nt = new NinjaTable();
        nt.setComment("touched by makeNextNinja()");
        
        //return the newly constructed ninja:
        return nt;   
           
    }//FUNC::END
    
    /**
     * Retrieve the ninja entity using ID:
     * @param nid :The ninja id we want to use.
     * @return 
     */
    
    public static NinjaTable getNinjaByID(long nid){
        
        //ErrorCheck: Are we in a transaction state?
        TransUtil.insideTransactionCheck();
        
        //our output variable:
        NinjaTable nt;
        
        //TODO: Is there a way to check if ninja of given ID exists and
        //return negative one (-1) if it does not exist??
        
        try {
            Session ses = TransUtil.getActiveTransactionSession();
            nt =  (NinjaTable) ses.get(NinjaTable.class, nid);
        } catch (Exception e) {
            //Not really catching exception. Just making compiler happy.
            throw new MyError(e.toString() );
        }
        
        //return our ninja output variable:
        return nt;
        
    }//FUNC::END
    
    /**
     * Makes a new [record/entry] in database's 
     * ninja table and populates that [record/entry]
     * @param name           :for info, see: setNinjaBasicInfo
     * @param phone          :for info, see: setNinjaBasicInfo
     * @param email          :for info, see: setNinjaBasicInfo
     * @param portfolio_url  :for info, see: setNinjaBasicInfo
     * @return A new ninja that has just had all of it's basic info set. **/
    public static NinjaTable makeNinjaRecord
        (String name, long phone, String email,String portfolio_url){
        //Make sure we are in a transaction state if we are doing this!
        TransUtil.insideTransactionCheck();
        
        //Create a new ninja:
        NinjaTable n = makeNextNinja();
        
        //Populate ninja with basic info:
        setNinjaBasicInfo(n, name,phone,email,portfolio_url);
        
        //Set debug comment:
        n.setComment("last touched by makeNinjaRecord");
        
        //Return the ninja:
        return n;
            
    }//FUNC::END
            
            
    
    /**
     * Set the basic info for the ninja.
     * Name, phone, email, portfolio url. Basic stuff.
     * Ideally: Any info that is stored in a given row of the ninja table.
     * @param ninja:N is for Ninja.
     * @param name :Full name of our ninja. Each name separated by spaces.
     *              I do this because full name is "JOHN MARK ISAAC MADISON"
     *              So first+middle+last name fields to me seem stupid.
     *              We can parse the information if need be. Or have a 
     *              FirstName + FullName field. But for now,
     *              lets keep it simple and just have one name field.
     * @param phone :Phone number of our ninja. Most ninjas have cell phones.
     * @param portfolio_url :Portfolio or resume detailing ninjas exploits.
     * @param email :Ninja's email. Ninjas like to respond to email because
     *               it is quite and cell phone calls give away their position.
     * @return                                                               **/
    public static NinjaTable setNinjaBasicInfo
        (NinjaTable ninja, String name, long phone, String email, 
                                                          String portfolio_url){
        
        //Make sure we are in a transaction state if we are doing this!
        TransUtil.insideTransactionCheck();
            
        //SET THE INFO FOR NINJA:
        ninja.setName(name);
        ninja.setPhone(phone);
        ninja.setEmail(email);
        ninja.setPortfolioURL(portfolio_url);
        
        //Set debug comment:
        ninja.setComment("last touched by setNinjaBasicInfo()");
        
        //return the ninja!
        return ninja;
            
    }//FUNC::END
    
}//CLASS::END
