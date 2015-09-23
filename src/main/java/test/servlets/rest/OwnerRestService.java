package test.servlets.rest;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import test.config.constants.ServletClassNames;
import test.dbDataAbstractions.entities.tables.OwnerTable;
import test.transactions.util.TransUtil;
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
    @Path("makeEntryUsing_ninja")
    public Response makeEntryUsing_ninja (
                     @DefaultValue("-1") @QueryParam("token_id") long token_id, 
                     @DefaultValue("-1") @QueryParam("ninja_id") long ninja_id){
        
        //ENTER TRANSACTION STATE:
        Session ses = TransUtil.enterTransaction();
        
        //Does ID already exist in table? If so, we have an error state:
        boolean tokenTaken = OwnerTransUtil.doesTokenExist(token_id);
        boolean tokenAvailable = (!tokenTaken);
        
        OwnerTable own;
        if(token_id >= 0 && ninja_id >= 0 && tokenAvailable){
            own = OwnerTransUtil.makeEntryUsing_ninja(token_id, ninja_id);
        }else{//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            own = new OwnerTable();
            own.setToken_id(token_id);
            own.setNinja_id(ninja_id);
            own.setIsError(true);
            String msg;
            if(tokenTaken){
                msg = "[token already claimed! M.E.U.NINJA]";
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
    @Path("makeEntryUsing_admin")
    public Response makeEntryUsing_admin (
                     @DefaultValue("-1") @QueryParam("token_id") long token_id, 
                     @DefaultValue("-1") @QueryParam("admin_id") long admin_id){
        
        //ENTER TRANSACTION STATE:
        Session ses = TransUtil.enterTransaction();
        
        //Does ID already exist in table? If so, we have an error state:
        boolean tokenTaken = OwnerTransUtil.doesTokenExist(token_id);
        boolean tokenAvailable = (!tokenTaken);
        
        OwnerTable own;
        if(token_id >= 0 && admin_id >=0 && tokenAvailable){
            own = OwnerTransUtil.makeEntryUsing_ninja(token_id, admin_id);
        }else{//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            //Error response if API fails:
            own = new OwnerTable();
            own.setToken_id(token_id);
            own.setNinja_id(admin_id);
            own.setIsError(true);
            String msg;
            if(tokenTaken){
                msg = "[token already claimed! M.E.U.ADMIN]";
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
    @Path("makeEntryUsing_random")
    public Response makeEntryUsing_random
                    (@DefaultValue("-1") @QueryParam("token_id") long token_id){
                        
        //ENTER TRANSACTION STATE:
        Session ses = TransUtil.enterTransaction();     
        
        //Does ID already exist in table? If so, we have an error state:
        boolean tokenTaken = OwnerTransUtil.doesTokenExist(token_id);
        boolean tokenAvailable = (!tokenTaken);
                        
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
                msg = "[token already claimed! M.E.U.RANDOM]";
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
  
}//CLASS::END
