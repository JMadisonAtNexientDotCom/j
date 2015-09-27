package test.servlets.rest;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import primitives.BooleanWithComment;
import test.config.constants.ServletClassNames;
import test.config.constants.identifiers.FuncNameReg;
import test.config.constants.identifiers.VarNameReg;
import test.dbDataAbstractions.entities.containers.BaseEntityContainer;
import test.dbDataAbstractions.entities.tables.OwnerTable;
import test.transactions.util.TransUtil;
import test.transactions.util.forNoClearTableOwner.OwnerTokenTransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.admin.AdminTransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.ninja.NinjaTransUtil;
import test.transactions.util.forOwnedMainlyByOneTable.owner.OwnerTransUtil;
import utils.JSONUtil;

//345678901234567890123456789012345678901234567890123456789012345678901234567890
/**##########################CLASS HEADER FILE##################################
//WHAT THIS CLASS DOES:
//Wrapper for OwnerTransUtil functions
// 
//ORIGINAL USE CASE:
//First step in creating the ability to have sessions and log-in + log-out
//is for admins+ninjas to be able to own tokens.
//In this context, token_table tokens become access tokens when associated
//with a given Ninja or Admin.
//
//DESIGN NOTE (Justifications for why things are the way they are):
//We have Admins and Ninjas rather than just "users" where some users
//also happen to be admins because Ninjas do NOT:
//1. Have user names.
//2. Have a password.
//Ninja's are basically invited into the system for a temporary amount of
//time in order to perform a Trial (test). After that, they should no longer
//have access to the system.
//3. Ninja IDs and Admin IDs being their own tables makes it a lot cleaner
//   to look at in PHPMyAdmin. I like the consistency.
//
//@author JMadison : 2015.09.21_????AMPM -95% of code written.
//@author XXXXXXXX : 2015.09.22_1010AM   -preparing for testing.
//@author XXXXXXXX : 20XX.XX.XX_####AMPM 
//@author XXXXXXXX : 20XX.XX.XX_####AMPM
########10########20########30########40########50########60########70########*/
//-------0---------0---------0---------0---------0---------0---------0---------0

@Path(ServletClassNames.OwnerRestService_MAPPING)
public class OwnerRestService extends BaseRestService{
    
    @GET
    @Path(FuncNameReg.MAKE_ENTRY_USING_NINJA)
    public Response make_entry_using_ninja (
            @DefaultValue("-1") @QueryParam(VarNameReg.TOKEN_ID) long token_id, 
            @DefaultValue("-1") @QueryParam(VarNameReg.NINJA_ID) long ninja_id){
        
        //ENTER TRANSACTION STATE:
        Session ses = TransUtil.enterTransaction();
        
        OwnerTable own;
        
        boolean ninjaExists = 
                           NinjaTransUtil.getDoesNinjaWithThisIDExist(ninja_id);
        if(false == ninjaExists){/////////////////////////////////////////
            own = new OwnerTable();
            own.setToken_id(token_id);
            own.setNinja_id(ninja_id);
            own.setIsError(true);
            own.setComment("MEU.NINJA: Ninja of this Id does not exist");
            TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
            return JSONUtil.entityToJSONResponse(own);
        }///////////////////////////////////////////////////////////////////////
        
        //Does ID already exist in table? If so, we have an error state:
        //boolean tokenTaken = OwnerTransUtil.doesTokenExist(token_id);
        //boolean tokenAvailable = (!tokenTaken);
        BooleanWithComment tokenStatus = 
             OwnerTokenTransUtil.isTokenAbleToBeEnteredIntoOwnerTable(token_id);
        boolean tokenAvailable = tokenStatus.value;
        boolean tokenTaken =   (!tokenAvailable);
        
        if(token_id >= 0 && ninja_id >= 0 && tokenAvailable){
            own = OwnerTransUtil.makeEntryUsing_ninja(token_id, ninja_id);
        }else{//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            own = new OwnerTable();
            own.setToken_id(token_id);
            own.setNinja_id(ninja_id);
            own.setIsError(true);
            String msg;
            if(tokenTaken){
                msg = "[M.E.U.NINJA]" + tokenStatus.comment;
            }else{
                msg = "[param was either missing or invalid. M.E.U.NINJA]";
            }//IF::END
            //set error message to be read in comments of entity.
            own.setComment(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
       
        //EXIT TRANSACTION STATE:
        ////We just send them back to UI/FrontEnd to notify them.
        boolean doWeHaveAnEntityToSave = (false == own.getIsError() );
        TransUtil.exitTransaction(ses, doWeHaveAnEntityToSave);
        
        //RETURN RESPONSE:
        return JSONUtil.entityToJSONResponse(own);
    }//FUNC::END
    
    @GET
    @Path(FuncNameReg.MAKE_ENTRY_USING_ADMIN)
    public Response make_entry_using_admin (
            @DefaultValue("-1") @QueryParam(VarNameReg.TOKEN_ID) long token_id, 
            @DefaultValue("-1") @QueryParam(VarNameReg.ADMIN_ID) long admin_id){
        
        //ENTER TRANSACTION STATE:
        Session ses = TransUtil.enterTransaction();
        
        OwnerTable own;
        
        boolean adminExists = 
                           AdminTransUtil.getDoesAdminWithThisIDExist(admin_id);
        if(false == adminExists){/////////////////////////////////////////
            own = new OwnerTable();
            own.setToken_id(token_id);
            own.setAdmin_id(admin_id);
            own.setIsError(true);
            own.setComment("MEU.ADMIN: Admin of this ID does not exist.");
            TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
            return JSONUtil.entityToJSONResponse(own);
        }///////////////////////////////////////////////////////////////////////
        
        //Does ID already exist in table? If so, we have an error state:
        //boolean tokenTaken = OwnerTransUtil.doesTokenExist(token_id);
        //boolean tokenAvailable = (!tokenTaken);
        BooleanWithComment tokenStatus = 
             OwnerTokenTransUtil.isTokenAbleToBeEnteredIntoOwnerTable(token_id);
        boolean tokenAvailable = tokenStatus.value;
        boolean tokenTaken =   (!tokenAvailable);
        
        if(token_id >= 0 && admin_id >=0 && tokenAvailable){
            own = OwnerTransUtil.makeEntryUsing_admin(token_id, admin_id);
        }else{//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            //Error response if API fails:
            own = new OwnerTable();
            own.setToken_id(token_id);
            own.setAdmin_id(admin_id);
            own.setIsError(true);
            String msg;
            if(tokenTaken){
                msg = "[M.E.U.ADMIN]" + tokenStatus.comment;
            }else{
                msg = "[param was either missing or invalid. M.E.U.ADMIN]";
            }//IF::END
            //set error message to be read in comments of entity.
            own.setComment(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //EXIT TRANSACTION STATE:
        //We do NOT save entities to database if they are errors.
        //We just send them back to UI/FrontEnd to notify them.
        boolean doWeHaveAnEntityToSave = (false == own.getIsError() );
        TransUtil.exitTransaction(ses, doWeHaveAnEntityToSave);
        
        //RETURN RESPONSE:
        return JSONUtil.entityToJSONResponse(own);
    }//FUNC::END
    
    @GET
    @Path(FuncNameReg.MAKE_ENTRY_USING_RANDOM)
    public Response make_entry_using_random
           (@DefaultValue("-1") @QueryParam(VarNameReg.TOKEN_ID) long token_id){
                        
        //ENTER TRANSACTION STATE:
        Session ses = TransUtil.enterTransaction();     
        
        //Does ID already exist in table? If so, we have an error state:
        //boolean tokenTaken = OwnerTransUtil.doesTokenExist(token_id);
        //boolean tokenAvailable = (!tokenTaken);
        BooleanWithComment tokenStatus = 
             OwnerTokenTransUtil.isTokenAbleToBeEnteredIntoOwnerTable(token_id);
        boolean tokenAvailable = tokenStatus.value;
        boolean tokenTaken =   (!tokenAvailable);
                        
        OwnerTable own;
        if(token_id >= 0 && tokenAvailable){
            own = OwnerTransUtil.makeEntryUsing_random(token_id);
        }else{//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            //Error response if API fails:
            own = new OwnerTable();
            own.setNinja_id(-1337);
            own.setAdmin_id(-1337);
            own.setIsError(true);
            String msg;
            if(tokenTaken){
                msg = "[M.E.U.RANDOM]" + tokenStatus.comment;
            }else{
                msg = "[param was either missing or invalid. M.E.U.RANDOM]";
            }//IF::END
            //set error message to be read in comments of entity.
            own.setComment(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //EXIT TRANSACTION STATE:
        //We do NOT save entities to database if they are errors.
        //We just send them back to UI/FrontEnd to notify them.
        boolean doWeHaveAnEntityToSave = (false == own.getIsError() );
        TransUtil.exitTransaction(ses, doWeHaveAnEntityToSave);
        
        //RETURN RESPONSE:
        return JSONUtil.entityToJSONResponse(own);
    }//FUNC::END
                    
    @GET
    @Path(FuncNameReg.DOES_TOKEN_HAVE_OWNER)
    public Response does_token_have_owner
           (@DefaultValue("-1") @QueryParam(VarNameReg.TOKEN_ID) long token_id){
        
        //ENTER TRANSACTION STATE:
        Session ses = TransUtil.enterTransaction();
        
        //LOGIC:
        boolean results = OwnerTokenTransUtil.doesTokenHaveOwner(token_id);
        Response op;
        op = JSONUtil.booleanToJSONResponse
                                        (results, "doesTokenHaveOwner?", false);
        
        //EXIT TRANSACTION STATE:
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
        return op;
    }//FUNC::END
    
    @GET
    @Path(FuncNameReg.GET_TOKEN_OWNER)
    public Response get_token_owner
                    (@DefaultValue("-1") @QueryParam(VarNameReg.TOKEN_ID) long token_id){
        
        //ENTER TRANSACTION STATE:
        Session ses = TransUtil.enterTransaction();
        
        //LOGIC:
        BaseEntityContainer bec;
        bec = OwnerTransUtil.getTokenOwner(token_id);
        OwnerTable own;
        if(bec.exists){
            own = (OwnerTable)bec.entity;
        }else{//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            own = new OwnerTable();
            own.setIsError(true);
            own.setComment("no owner for this token!");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //EXIT TRANSACTION STATE:
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        return JSONUtil.entityToJSONResponse(own);
    }//FUNC::END
  
}//CLASS::END
