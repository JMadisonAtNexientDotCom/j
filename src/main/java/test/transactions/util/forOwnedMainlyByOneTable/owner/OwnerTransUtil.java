package test.transactions.util.forOwnedMainlyByOneTable.owner;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import test.MyError;
import test.config.constants.DatabaseConsts;
import test.config.debug.DebugConfig;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import test.dbDataAbstractions.entities.containers.BaseEntityContainer;
import test.dbDataAbstractions.entities.tables.AdminTable;
import test.dbDataAbstractions.entities.tables.NinjaTable;
import test.dbDataAbstractions.entities.tables.OwnerTable;
import test.dbDataAbstractions.entities.tables.TokenTable;
import test.debug.debugUtils.table.TableDebugUtil;
import test.transactions.util.TransUtil;
import test.transactions.util.TransValidateUtil;
import test.transactions.util.forNoClearTableOwner.AdminTokenTransUtil;
import test.transactions.util.forNoClearTableOwner.OwnerTokenTransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.admin.AdminTransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.ninja.NinjaTransUtil;

//345678901234567890123456789012345678901234567890123456789012345678901234567890
/**##########################CLASS HEADER FILE##################################
//WHAT THIS CLASS DOES:
//Handles transactions mainly involving the owner_table.
// Owner table being a join-table that joins:
// 1. token_id  (token_table.id)
// 2. ninja_id  (ninja_table.id)
// 3. admin_id  (admin_table.id) 
// 
//ORIGINAL USE CASE:
//  A .html app that tested it. At time of writing this, I do not know
//  how this will plug into the structure of the overall program. Just
//  know it is vital to creating sessions in the session table that are
//  associated with a token that is in turn associated with a Ninja Or Admin.
//  HOWEVER: I am currently testing this code in an app that has 4 buttons:
//          Button   #1  [ Make Token]
//          Button   #2  [ Randomly Assign Token To Ninja Or Admin]
//          Button   #3  [ Does Token Have Owner? ]
//          Button   #4  [ Find Owner of Token    ]
//
//DESIGN NOTE:
//In final production, this utility should NOT be exposed to rest.
//However, in dev mode, we may want this exposed to rest to test it out.
//
//@author JMadison : 2015.??.??_????AMPM
//@author JMadison : 2015.09.23_0545PM  --Testing OwnerRestService.java
//@author XXXXXXXX : 20XX.XX.XX_####AMPM 
//@author XXXXXXXX : 20XX.XX.XX_####AMPM
########10########20########30########40########50########60########70########*/
//-------0---------0---------0---------0---------0---------0---------0---------0
public class OwnerTransUtil {
    
    /**-------------------------------------------------------------------------
     *  RESERVE 0 for un-used, because (-1) is INVALID FOR UNSIGNED DATA!
     * 
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
    private static long UNUSED_ID = DatabaseConsts.UNUSED_ID;
    
    /**
     * 
     * @param ownerRecords :list of owner records to extract tokenIDs from.
     *                      Will be casted from BaseEntity to OwnerTable inside.
     * @param deleOwnerRecords :If true, mark entities used in extraction for
     *                          deletion.
     * @return Returns a collection of only tokenIDS.
     */
    public static List<Long> extractTokenIDSFromOwnerTableEntities
                      (List<BaseEntity> ownerRecords, boolean deleOwnerRecords){
                              
        //Make sure you are in a transaction state.
        //Then get transaction session:
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();
                              
        //Core extraction logic:
        Long curTokenID;
        OwnerTable curOwnerRecord;
        List<Long> hordeOfTokenIDS = new ArrayList<Long>();
        for(BaseEntity bEnt : ownerRecords){
            curOwnerRecord = (OwnerTable)bEnt;
            curTokenID = curOwnerRecord.getToken_id();
            if(curTokenID<=0){doError("[lazy init error. Token Id???]");}
            
            //add the token to collection to return:
            hordeOfTokenIDS.add(curTokenID);
            
            if(deleOwnerRecords){//[DELE][DELE][DELE][DELE][DELE][DELE][DELE]]
                curOwnerRecord.setDele(true);
                ses.save( curOwnerRecord );
            }//[DELE][DELE][DELE][DELE][DELE][DELE][DELE][DELE][DELE][DELE]]]]]]
        }//Next entry.
        
        //A list with length of zero is acceptable here.
        return hordeOfTokenIDS;
    }//FUNC::END
    
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
       if(token_id <= (-1)){ doError("[[NEGATIVE token_id supplied!]]");}                 
       if(false == xor_id(ninja_id, admin_id)){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
           String msg = "";
           msg += "[XOR_ID ERROR:]";
           msg += "[Must supply ninja_id or admin_id, but not both!]";
           msg += "[The other ID should be == UNUSED_ID (should be (-1)]";
           doError(msg);
       }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
               
       //Non-Niave error checks:
       //Make sure supplied id's are in their respective tables!
       TransValidateUtil.idShouldExistIfPositive
                             (TokenTable.class, TokenTable.ID_COLUMN, token_id);
       TransValidateUtil.idShouldExistIfPositive
                             (NinjaTable.class, NinjaTable.ID_COLUMN, ninja_id);
       TransValidateUtil.idShouldExistIfPositive
                             (AdminTable.class, AdminTable.ID_COLUMN, admin_id);
       
       //DEBUG::DEBUG::DEBUG::DEBUG::DEBUG::DEBUG::DEBUG::DEBUG::DEBUG::DEBUG:::
       //Check integrity of OwnerTable before we edit it:
       //I cannot justify this error check running in production.
       //It is too heavy.
       if(DebugConfig.isDebugBuild){//DEBUG::DEBUG::DEBUG::DEBUG::DEBUG::DEBUG::
            assertAllTokensAreUniqueInTable();
       }//DEBUG::DEBUG::DEBUG::DEBUG::DEBUG::DEBUG::DEBUG::DEBUG::DEBUG::DEBUG::
       
       //See if exact entry already exists. If it does, we will create
       //and send back an error object. With comment telling us that object
       //already exists. Why error instead of ignore? Because the design I have
       //in place expects that we will have an entity to save if one already
       //exists:
       boolean isDuplicate = doesRecordExist(token_id,ninja_id,admin_id);
       
       //If exact entry does not already exist:
       //Make sure we are not using a token already in the table:
       boolean tokenAlreadyExists = doesTokenExist(token_id);
       if(tokenAlreadyExists){//////////////////////////////////////////////////
           //Within this utility, this is a more serious error. 
           //More serious than a duplicate entry.
           //
           //Throw an uncaught exception. It would be less serious IF
           //we KNEW this call was made from a servlet API.
           //So we should have servlet API also check for this and return less
           //serious error that doesn't crash system.
           String tID = Long.toString(token_id);
           doError("[Token of ID AlreadyExists! TokenID==]"+ tID);
       }////////////////////////////////////////////////////////////////////////
                
       //Actually make an entry:
       OwnerTable theJoin = new OwnerTable();
       theJoin.setToken_id(token_id);
       theJoin.setNinja_id(ninja_id);
       theJoin.setAdmin_id(admin_id);
       
       //Decide if theJoin should be saved to database:
       //SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
       if(false==isDuplicate){
            //save new entry:
            TransUtil.markEntityForSaveOnExit(theJoin);
       }else{
           //NOT SURE IF THIS BELONGS HERE.
           //Might only belong on the API layer. Not down into
           //the logic of our actual transaction utilites.
           //-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
           //do NOT save duplicates to database.
           //mark them as error without crashing.
           theJoin.setIsError(true);
       }//SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
       
       //Return the entry we made in case we want to operate on it.
       //Not sure why we would though... What can you really do by
       //immediately returning the entry? Besides... causing an error
       //in the program by meddling with it??
       return theJoin; //<--Seriously might want to consider returning void.
       
    }//FUNC::END
          
    /**
     * Check to see if a record exists. 
     * Used to prevent duplicate record insertion.
     * @param token_id
     * @param ninja_id
     * @param admin_id 
     * @return : Returns true if record exists. All records should be unique.
     */
    public static boolean doesRecordExist
                                  (long token_id, long ninja_id, long admin_id){
        boolean results = false;                        
                                      
        TransUtil.insideTransactionCheck();
        Session ses = TransUtil.getActiveTransactionSession();
        Criteria c = ses.createCriteria(OwnerTable.class);
        c.add(Restrictions.eq(OwnerTable.TOKEN_ID_COLUMN, token_id));
        c.add(Restrictions.eq(OwnerTable.NINJA_ID_COLUMN, ninja_id));
        c.add(Restrictions.eq(OwnerTable.ADMIN_ID_COLUMN, admin_id));
        List<OwnerTable> oneItemMax = c.list();
        int numOwners = oneItemMax.size();
        if(numOwners <= 0){ results = false;}else
        if(numOwners == 1){ results = true; }else
        if(numOwners >  1){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            String msg = "[When calling doesRecordExist()]";
            msg+= "[We found multiple identical records]";
            msg+= "[This cannot be allowed, because the token column]";
            msg+= "[is a primary key. It is a primary key because only]";
            msg+= "[one user can claim ownership to a given token.]";
            msg+= "[In other words: Token cannot have multiple owners]";
            doError(msg);
        }else{
            doError("[This line should never execute. drexst3242]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //Return results, was the record found?
        return results;
        
    }//FUNC::END
     
    /**
     * Used to prevent assigning a token to someone when it already has
     * an owner. Also used to just, validate you are working with a token
     * that has an owner.
     * 
     * Note: Proving token exists does NOT prove it has an owner!
     * 
     * @param token_id 
     * @return : Returns true if the token exists in table.
     *           Token should be a PRIMARY key.
     */
    public static boolean doesTokenExist(long token_id){
        
        //Error Checking:
        TransUtil.insideTransactionCheck();
        
        //Logic:
        BaseEntityContainer bec;
        bec = TransUtil.getEntityFromTableUsingPrimaryKey
                       (OwnerTable.class, OwnerTable.TOKEN_ID_COLUMN, token_id);
        return bec.exists;
    }//FUNC::END
    
    //we've managed to make two functions that seem identical.
    ////////////////////////////////////////////////////////////////////////////
    //222222222222222222222222222222222222222222222222222222222222222222222222//
    ////////////////////////////////////////////////////////////////////////////
    /**
     * UDPATE! What si the difference between this function and
     *         isTokenOwned function?
     * 
     * Does the token that may or may not be in the table
     * have an owner?
     * @param token_id :The token ID to use to find if owner exists.
     * @return         :Returns true if token has owner associated with it.
     */
    public static boolean isTokenIDOwned(long token_id){
        
        //Error Checking:
        TransUtil.insideTransactionCheck();
        
        //Involves owner_table + token_table, code belongs in
        //OwnerTokenTransUtil.java
        return OwnerTokenTransUtil.isTokenIDOwned(token_id);
    }//FUNC::END
    
    /**
     * Returns TRUE if the token is owned by an ADMIN or NINJA
     * @param token_id :id of token we want to find ownership of.
     * @return :Returns TRUE if token has an owner.
     */                         
    public static boolean isTokenOwned(long token_id){
        TransUtil.insideTransactionCheck();
        BaseEntityContainer bec = getTokenOwner(token_id);
        return bec.exists;
    }//FUNC::END
    ////////////////////////////////////////////////////////////////////////////
    //222222222222222222222222222222222222222222222222222222222222222222222222//
    ////////////////////////////////////////////////////////////////////////////
       
    /** Program will crash if this is not true. **/
    private static void assertAllTokensAreUniqueInTable(){
        Class theTableClass= OwnerTable.class;
        String tokenColumn = OwnerTable.TOKEN_ID_COLUMN;
        TableDebugUtil.assertUniqueColumn(theTableClass,tokenColumn);
    }//FUNC::END
                
    
    
    /**
     * Returns a container that will have the owner of the token
     * if their is an owner for the token.
     * @param token_id : id of token we want to find ownership of.
     * @return :Returns a container. If has owner, the container
     *          will contain the entity that owns this token.
     * 
     *          RETURNS OWNER table instance. NOT ninja or admin.
     */
    public static BaseEntityContainer getTokenOwner(long token_id){
        BaseEntityContainer bec;
        bec = TransUtil.getEntityFromTableUsingLong
                       (OwnerTable.class, OwnerTable.TOKEN_ID_COLUMN, token_id);
        
        //Verify that a ninja or admin has been set to owning this token.
        if(bec.exists){
            OwnerTable owner = (OwnerTable)bec.entity;
            long admin_id = owner.getAdmin_id();
            long ninja_id = owner.getNinja_id();
            boolean oneOrTheOther = xor_id(admin_id, ninja_id);
            if(false == oneOrTheOther){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
                String msg = "[getTokenOwner returned bad results:]";
                msg += "[It is alright for getTokenOwner to return]";
                msg += "[an empty container. But it is not alright]";
                msg += "[to return an improperly configured entity]";
                msg += "[Possible cause of error:]";
                msg += "[1: token has ZERO owners. But exists in table.]";
                msg += "[   We probably should not allow token with zero ]";
                msg += "[   owners to be in table. But I wouldn't consider ]";
                msg += "[   it an error to avoid error, just use ]";
                msg += "[   doesTokenHaveOwner() to look before you leap.]";
                msg += "[2: token has MORE THAN ONE OWNER.]";
                msg += "[   Maybe failed switch of ownership?]";
                msg += "[   Hopefully NOT a synchronicity problem]";
                doError(msg);
            }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        }//bec.exists?
        
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
        
        boolean admin_already_exists = OwnerTransUtil.doesAdminExist(admin_id);
        if(admin_already_exists){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            String msg = "";
            msg += "[Cannot make entry like this.]";
            msg += "[Admin only allowed in table once.]";
            msg += "Try using from class:";
            msg += AdminTokenTransUtil.class.getCanonicalName();
            msg += "[function called: linkAdminToNewToken()]";
            doError(msg);
        }//Already exists? EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        return makeEntry(token_id, UNUSED_ID, admin_id);
    }//FUNC::END
    
    /*
     * @param admin_id :The id of the admin.
     * @return : Returns TRUE if the admin exists in the table.
     */
    public static boolean doesAdminExist(long admin_id){
        BaseEntityContainer bec;
        bec = TransUtil.getEntityFromTableUsingLong
                       (OwnerTable.class, OwnerTable.ADMIN_ID_COLUMN, admin_id);
        return (bec.exists);
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
            doError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        OwnerTable own=null;
        if(userCode == NINJA_USER_CODE){
           long ninja_id = NinjaTransUtil.getRandomNinjaID();
           own = makeEntryUsing_ninja(token_id, ninja_id); 
        }else
        if(userCode == ADMIN_USER_CODE){
            long admin_id = AdminTransUtil.getRandomAdminID();
            own = makeEntryUsing_admin(token_id, admin_id);
        }else{
            doError("This error should have been caught earlier.");
        }
        
        //NOTE: We do NOT have to mark own for save, because
        //that is handled by the makeEntryUsing_.... functions we are
        //wrapping in this function. Calling it twice == error.
        
        if(null==own){doError("[own is null. This is not acceptable]");}
        
        //return the owner table that has the entry:
        return own;
        
    }//FUNC::END
    
    /*
    --This is going to lead to concurrency problems.                          --
    --A more appropriate solution I think is to OVERWRITE entries that are    --
    --no longer valid. For example, since the admin can only have ONE entry   --
    --In the session table, we should overwrite the admin's entry. When admin --
    --Gets a new token.                                                       --
     X Removes tokens associated with a given admin from the owner table.
     X Only tokens that are currently owned are allowed in this table.
     X @param admin_id :The id of the admin.
     X
    public static void reliquishOwnershipOfAllTokens_ADMIN(long admin_id){
        List<OwnerTable> deleteThese = getRecordsBelongingToAdmin(admin_id);
        
        //DO NOT clear entities before deleting them.
        //That will just mess up the query that searches for them!
        //(Unless you can delete by ID)
        TransUtil.markEntitiesForDeletionOnExit( deleteThese );
    }//FUNC::END
    */
   
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = OwnerTransUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
    /** A XOR of id_00 and id_01, where ONE must have a
     *  valid id that is >UNUSED_ID and the other must have
     *  an INVALID_ID that is <=UNUSED_ID
     * @param id_00 :1ST ID
     * @param id_01 :2ND ID
     ------------------------------------------------------------------------**/
    private static boolean xor_id(long id_00, long id_01){
        if(id_00 > UNUSED_ID && id_01 <=UNUSED_ID){ return true;}
        if(id_00 <=UNUSED_ID && id_01 > UNUSED_ID){ return true;}
        return false;
    }//FUNC::END
      
}//CLASS::END
