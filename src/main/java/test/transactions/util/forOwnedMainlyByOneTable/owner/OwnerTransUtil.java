package test.transactions.util.forOwnedMainlyByOneTable.owner;

import test.MyError;
import test.dbDataAbstractions.entities.containers.BaseEntityContainer;
import test.dbDataAbstractions.entities.tables.AdminTable;
import test.dbDataAbstractions.entities.tables.NinjaTable;
import test.dbDataAbstractions.entities.tables.OwnerTable;
import test.dbDataAbstractions.entities.tables.TokenTable;
import test.transactions.util.TransUtil;
import test.transactions.util.TransValidateUtil;
import test.transactions.util.forOwnedMainlyByOneTable.admin.AdminTransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.ninja.NinjaTransUtil;

/**
 * NOTE: In final production, this utility should NOT be exposed to rest.
 * However, in dev mode, we may want this exposed to rest to test it out.
 * 
 * Handles transactions mainly involving the owner_table.
 * Owner table being a join-table that joins:
 * 1. token_id  (token_table.id)
 * 2. ninja_id  (ninja_table.id)
 * 3. admin_id  (admin_table.id)
 * @author jmadison
 */
public class OwnerTransUtil {
    
    /**-------------------------------------------------------------------------
     *  Owner table can link token+ninja or token+admin, but should
     *  not do a 3-way binding. So the field that is NOT binded should
     *  have this NEGATIVE value in it. This negative value [says/means],
     *  "This foreign-key was not used to bind. And this is intentional."
     * 
     *  DESIGN QUESTIONS:
     *  //DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD
     *  QUESTION:
     *  Should ninja's ID's and admin's ID's really be separate sets?
     *  Wouldn't it be a better design to have users and some of those
     *  users are also admins?
     *  Like a isadmin_table that if your userID is in it, you are an admin?
     *  
     *  ANSWER:
     *  ANSWER_PART_01:
     *  I am honestly not sure.
     *  However, the current design is made so that ADMIN(S) 
     *  can have login info. But NINJA(S) cannot.
     *  Hence why the admin_table is called "admin_table" and not "user_table"
     *  --
     *  ANSWER_PART_02:
     *  When Drew explained the gauntlet to me. I got the impression that
     *  candidates do not actually have username+login credentials in the
     *  traditional sense. Rather they login with access tokens that must
     *  be explicitly allocated by an admin. 
     * (AKA: Recruiter or other nexient employee with admin account)
     ------------------------------------------------------------------------**/
    private static long UNUSED_ID = (-1);
    
    /**-------------------------------------------------------------------------
     * Make an entry into join table represented by this class.
     * Because these are all FORIEGN KEYS, they must be proven to exist
     * in their corresponding tables before we make entry.
     * @param token_id :REQUIRED.
     * @param ninja_id :REQUIRED: ninja_id XOR admin_id   (one,but not both)
     * @param admin_id :REQUIRED: ninja_id XOR admin_id   (one,but not both)
     * @return : Returns the entity representing this join.
     *           Also registers it as needed to be saved when
     *           the session closes.
     ------------------------------------------------------------------------**/
    public static OwnerTable makeEntry
                                  (long token_id, long ninja_id, long admin_id){
            
       //Error if not in transaction state:
       TransUtil.insideTransactionCheck();
                                      
       //Niave error checks of inputs:
       //Make sure only ninja_id or admin_id was specified.
       if(token_id < 0){ doError("negative token_id supplied");}                 
       if(false == xor_id(ninja_id, admin_id)){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
           String msg = "";
           msg += "[XOR_ID ERROR:]";
           msg += "[Must supply ninja_id or admin_id, but not both!]";
           msg += "[The other ID should be == UNUSED_ID (should be (-1)]";
           doError(msg);
       }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
               
       //Non-Niave error checks:
       //Make sure supplied id's are in their respective tables!
       TransValidateUtil.idShouldExistIfNonNegative
                             (TokenTable.class, TokenTable.ID_COLUMN, token_id);
       TransValidateUtil.idShouldExistIfNonNegative
                             (NinjaTable.class, NinjaTable.ID_COLUMN, ninja_id);
       TransValidateUtil.idShouldExistIfNonNegative
                             (AdminTable.class, AdminTable.ID_COLUMN, admin_id);
                
       //Actually make an entry:
       OwnerTable theJoin = new OwnerTable();
       theJoin.setToken_id(token_id);
       theJoin.setNinja_id(ninja_id);
       theJoin.setAdmin_id(admin_id);
       
       //Queue up this entry to be saved by hibernate when
       //we exit the transaction:
       TransUtil.markEntityForSaveOnExit(theJoin);
       
       //Return the entry we made in case we want to operate on it.
       //Not sure why we would though... What can you really do by
       //immediately returning the entry? Besides... causing an error
       //in the program by meddling with it??
       return theJoin; //<--Seriously might want to consider returning void.
       
    }//FUNC::END
                
    /**
     * Returns TRUE if the token is owned by an ADMIN or NINJA
     * @param token_id :id of token we want to find ownership of.
     * @return :Returns TRUE if token has an owner.
     */                         
    private static boolean isTokenOwned(long token_id){
        TransUtil.insideTransactionCheck();
        BaseEntityContainer bec = getTokenOwner(token_id);
        return bec.exists;
    }//FUNC::END
    
    /**
     * Returns a container that will have the owner of the token
     * if their is an owner for the token.
     * @param token_id : id of token we want to find ownership of.
     * @return :Returns a container. If has owner, the container
     *          will contain the entity that owns this token.
     */
    private static BaseEntityContainer getTokenOwner(long token_id){
        BaseEntityContainer bec;
        bec = TransUtil.getEntityFromTableUsingLong
                       (OwnerTable.class, OwnerTable.TOKEN_ID_COLUMN, token_id);
        return bec;
    }//FUNC::END
                
    /**-------------------------------------------------------------------------
     * Make entry using only ninja's id.
     * @param token_id :token_table.id to join.
     * @param ninja_id :ninja_table.id to join.
     * @return : Returns the entity representing this join.
     *           Also registers it as needed to be saved when
     *           the session closes.
     ------------------------------------------------------------------------**/
    public static OwnerTable makeEntryUsing_ninja(long token_id, long ninja_id){
        
        TransUtil.insideTransactionCheck();
        return makeEntry(token_id, ninja_id, UNUSED_ID);
        
    }//FUNC::END
    
    /**
     * Make entry using only admin's id.
     * @param token_id : token_table.id to join.
     * @param admin_id : admin_table.id to join.
     * @return : Returns the entity representing this join.
     *           Also registers it as needed to be saved when
     *           the session closes.
     */
    public static OwnerTable makeEntryUsing_admin(long token_id, long admin_id){
        TransUtil.insideTransactionCheck();
        return makeEntry(token_id, UNUSED_ID, admin_id);
    }//FUNC::END
    
    /**
     * Randomly assigns token ownership to a Ninja or Admin.
     * Should it be an error if it takes the token from someone else?
     * YES: Because it is MAKING an entry. Not EDITING an entry.
     * @param token_id :The id of the token we want to assign to someone.
     * @return :The OwnerTable entity that gives us the record resulting
     *          from the join.
     */
    public static OwnerTable makeEntryUsing_random(long token_id){
        
        //Before we can randomly assign a token to a ninja or
        //an admin, we need to make sure ninjas and admins exist.
        boolean hasNinjas = NinjaTransUtil.getDoNinjasExist();
        boolean hasAdmins = AdminTransUtil.getDoAdminsExist();
        int userCode = (-77);
        int NINJA_USER_CODE = 1;
        int ADMIN_USER_CODE = 2;
        int NOONE_USER_CODE = 3;
        
        //Create random choices to choose from:
        //[RC][RC][RC][RC][RC][RC][RC][RC][RC][RC][RC][RC][RC][RC][RC][RC][RC]]]
        if(hasNinjas && hasAdmins){ //ninjas + admins exist in system.
            double flip = Math.random() * 100;
            userCode = (flip>50?NINJA_USER_CODE : ADMIN_USER_CODE);
        }else
        if(hasNinjas){ //only ninjas exist in system.
            userCode = NINJA_USER_CODE;
        }else
        if(hasAdmins){ //only admins exist in system.
            userCode = ADMIN_USER_CODE;
        }else{
            userCode = NOONE_USER_CODE;
        }//[RC][RC][RC][RC][RC][RC][RC][RC][RC][RC][RC][RC][RC][RC][RC][RC][RC]]
        
        //now that we know who we are going to assign to, do assigning.
        //special case if no one to assign to.
        if(userCode == NOONE_USER_CODE){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            //Decided to throw error. Because otherwise it would mean
            //this function sometimes can have NO ENTITIES needed to be saved.
            //And that would wreck my error checking designs.
            String msg = "No users exist which to assign ownership of token";
            throw new MyError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        OwnerTable own;
        if(userCode == NINJA_USER_CODE){
           long ninja_id = NinjaTransUtil.getRandomNinjaID();
           own = makeEntryUsing_ninja(token_id, ninja_id); 
        }else
        if(userCode == ADMIN_USER_CODE){
            long admin_id = AdminTransUtil.getRandomAdminID();
            own = makeEntryUsing_admin(token_id, admin_id);
        }else{
            throw new MyError("This error should have been caught earlier.");
        }
        
        //NOTE: We do NOT have to mark own for save, because
        //that is handled by the makeEntryUsing_.... functions we are
        //wrapping in this function. Calling it twice == error.
        
        //return the owner table that has the entry:
        return own;
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    * Wrapper function to throw errors from this class.
    * @param msg :Specific error message.
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
       String err = "ERROR INSIDE:";
       err += OwnerTransUtil.class.getSimpleName();
       err += msg;
       throw new MyError(err);
    }//FUNC::END
    
    /** A XOR of id_00 and id_01, where TRUE is anything
     *  that is ZERO or POSITIVE. Zero == true because
     *  0 is a valid id for a database. (Though not used by convention it seems)
     * @param id_00 :1ST ID
     * @param id_01 :2ND ID
     */
    private static boolean xor_id(long id_00, long id_01){
        if(id_00 >= 0 && id_01 < 0){ return true;}
        if(id_00 <  0 && id_01 >=0){ return true;}
        return false;
    }//FUNC::END
    
}//CLASS::END