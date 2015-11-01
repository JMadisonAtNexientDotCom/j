package app.transactions.util.tables.ninja;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import primitives.RealAndFakeIDs;
import app.dbDataAbstractions.entities.tables.NinjaTable;
import app.transactions.util.TransUtil;
import app.MyError;
import app.dbDataAbstractions.entities.bases.BaseEntity;
import app.dbDataAbstractions.entities.containers.BaseEntityContainer;
import app.dbDataAbstractions.entities.tables.OwnerTable;
import app.dbDataAbstractions.entities.tables.TokenTable;
import app.transactions.util.forNoClearTableOwner.OwnerTokenTransUtil;
import app.transactions.util.tables.owner.OwnerTransUtil;
import app.transactions.util.tables.token.TokenTransUtil;
import utils.ListUtil;
/**
 * Handles [transactions/operations] involving the ninja table.
 * AKA: Our table full of identification info for every ninja that
 * wishes to participate in trials for a chance to become a member of the clan.
 * @author jmadison
 */
public class NinjaTransUtil {
    
    /**
     * Sort unsanitized list with possible duplicate entries into two
     * unique lists. One with VALID ids existing in the database. The other
     * with INVALID ids that do not exist in the database.
     * @inputIDs: Id's that may or may not exist within the ninja table.
     * @return :Return what we just said!
     */
    public static RealAndFakeIDs sortNinjaIDS_IntoRealAndFake
                                                     (List<Long> inputIDs){
        List<Long> uniqueIDs = ListUtil.makeUnique(inputIDs);
        
        List<Long> fake = new ArrayList<Long>();
        List<Long> real = TransUtil.returnExistingPrimaryKeys
                            (NinjaTable.class, NinjaTable.ID_COLUMN, uniqueIDs);
        Long curUnique;
        int len = uniqueIDs.size();
        for(int i = 0; i < len; i++){
            curUnique = uniqueIDs.get(i);
            if(real.indexOf(curUnique) <= (-1)){
                fake.add(curUnique); //unique, but FAKE id.
            }//unique add.
        }//NEXT i
        
        //Pack our real IDs and fake IDs into output array:
        RealAndFakeIDs op = RealAndFakeIDs.make(real,fake);
        return op;
        
    }//FUNC::END
    
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
    
    /** Get a page of ninjas so that our ADMIN can select ninja from list
     *  and assign a test to them. 
     * @param pageIndex :The page number we want to return. Starts at...
     *                    ZERO.
     * @param numberOfResultsPerPage :How many results should be on each page?
     * @return :Return a list of NinjaTable objects.
     */
    public static List<NinjaTable> getPageOfNinjas
                                (int pageIndex, int numberOfResultsPerPage){
                                    
        //Make sure we are in a transaction state:
        TransUtil.insideTransactionCheck();
        
        //Indicies have +1 because we are assuming the first ninja in the
        //table has an ID of ONE (not zero).
        int inclusiveStartingIndex = 1 + (pageIndex * numberOfResultsPerPage); 
        //minus one because if we return only ONE result, the beginning and end
        //inclusive indicies will be IDENTICAL numbers.
        int inclusiveEndingIndex = 
                            inclusiveStartingIndex + numberOfResultsPerPage - 1;
        
        //Get the generic results:
        List<BaseEntity> genericList;
        genericList = TransUtil.getEntitiesUsingRange(NinjaTable.class, 
                                 inclusiveStartingIndex, inclusiveEndingIndex );
        
        //Cast those results to Ninja Type:
        //Probably a better way to do this.
        //http://stackoverflow.com/questions/
        //     933447/how-do-you-cast-a-list-of-supertypes-to-a-list-of-subtypes
        List<NinjaTable> pageOfNinjas = (List<NinjaTable>)(List<?>)genericList;
        
        //Returning an empty list is acceptable. Returning NULL is not.
        if(null == pageOfNinjas){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            doError("[pageOfNinjas can have len==0, but never null]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        return pageOfNinjas;
        
    }//FUNC::END
    
    /**
     * Gets if a NINJA with this id exist. 
     * @param ninja_id :The NINJA ID to query.
     * @return :true if NINJA with this id exists.
     */
    public static boolean getDoesNinjaWithThisIDExist(long ninja_id){
        //check to see if we are in a transaction state:
        TransUtil.insideTransactionCheck();
        
        BaseEntityContainer bec;
        bec = TransUtil.getEntityFromTableUsingPrimaryKey
                             (NinjaTable.class, NinjaTable.ID_COLUMN, ninja_id);
        return bec.exists;
    }//FUNC::END
    
    /**
     * Finds the ninja by searching for the token-hash value.
     * Original Use: Asking the [candidate/ninja] who just entered the
     *               token to confirm their identity. AKA: Make sure
     *               token is actually linked to the person about to
     *               take the [trial/test]
     * @param token_hash :String of characters, representing an obfuscated
     *                    token id value.
     * @return :Returns a ninja in the container if hash is valid.
     *          Returns empty container if hash is invalid.
     */
    public static BaseEntityContainer getNinjaByTokenHash(String token_hash){
        
        BaseEntityContainer bec_own;
        bec_own = OwnerTokenTransUtil.getOwnerByTokenHash(token_hash);
        if(false == bec_own.exists){
            return BaseEntityContainer.make_NullAllowed(null);
        }//
        
        //If we have an owner table, we can use it to get the ninja instance
        //if the token is owned by a ninja:
        OwnerTable own = (OwnerTable)bec_own.entity;
        long ninja_id = own.getNinja_id();
        if(ninja_id <= 0){
            return BaseEntityContainer.make_NullAllowed(null);
        }//Zero or less means NOT owned by ninja.
        
        //If owned by ninja, use ninjaID to get actual ninja,
        //then pack that into a container and return it:
        NinjaTable nin = NinjaTransUtil.getNinjaByID(ninja_id);
        if(null == nin){doError("[OPNINJAshouldNOTbeNull]");}
        BaseEntityContainer ninja_pod = BaseEntityContainer.make(nin);
        return ninja_pod;
        
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
        NinjaTable nt = null;
        
        //TODO: Is there a way to check if ninja of given ID exists and
        //return negative one (-1) if it does not exist??
        
        try {
            Session ses = TransUtil.getActiveTransactionSession();
            nt =  (NinjaTable) ses.get(NinjaTable.class, nid);
        } catch (Exception e) {
            //Not really catching exception. Just making compiler happy.
            doError(e.toString() );
        }
        
        if(null==nt){doError("getNinjaByID has returned null");}
        
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
     * @return : TRUE if there is at least one entry in the ninja_table
     */
    public static boolean getDoNinjasExist(){
        TransUtil.insideTransactionCheck();
        long amt =  TransUtil.getNumberOfRecordsInTable(NinjaTable.class);
        return (amt > 0);
    }//FUNC::END
    
    /**
     * Get a random ninja's id from table.
     * @return :id of that random ninja.
     */
    public static long getRandomNinjaID(){
        TransUtil.insideTransactionCheck();
        BaseEntityContainer bec;
        bec = TransUtil.getRandomRecord(NinjaTable.class);
        
        long op;
        if(bec.exists){
            NinjaTable ninja = (NinjaTable)bec.entity;
            op = ninja.getId();
        }else{
            op = (-1); //return -1 for no ninja exists.
        }
        
        //return output:
        return op;
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
        ninja.setPortfolio_url(portfolio_url);
        
        //Set debug comment:
        ninja.setComment("last touched by setNinjaBasicInfo()");
        
        //return the ninja!
        return ninja;
            
    }//FUNC::END
        
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = NinjaTransUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
