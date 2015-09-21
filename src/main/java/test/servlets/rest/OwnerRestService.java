package test.servlets.rest;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import test.dbDataAbstractions.entities.tables.OwnerTable;
import test.transactions.util.forOwnedMainlyByOneTable.owner.OwnerTransUtil;
import utils.JSONUtil;


/**
 * Wrapper for OwnerTransUtil functions.
 * @author jmadison
 */
public class OwnerRestService extends BaseRestService{
    
    @GET
    @Path("makeEntryUsing_ninja")
    public Response makeEntryUsing_ninja (
                     @DefaultValue("-1") @QueryParam("token_id") long token_id, 
                     @DefaultValue("-1") @QueryParam("ninja_id") long ninja_id){
        OwnerTable own;
        
        if(token_id >= 0 && ninja_id >= 0){
            own = OwnerTransUtil.makeEntryUsing_ninja(token_id, ninja_id);
        }else{//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            own = new OwnerTable();
            own.setToken_id(token_id);
            own.setNinja_id(ninja_id);
            own.setIsError(true);
            own.setComment("[param was either missing or invalid. M.E.U.N.]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        return JSONUtil.entityToJSONResponse(own);
    }//FUNC::END
    
    @GET
    @Path("makeEntryUsing_admin")
    public Response makeEntryUsing_admin (
                     @DefaultValue("-1") @QueryParam("token_id") long token_id, 
                     @DefaultValue("-1") @QueryParam("admin_id") long admin_id){
        OwnerTable own;
        
        if(token_id >= 0 && admin_id >=0){
            own = OwnerTransUtil.makeEntryUsing_ninja(token_id, admin_id);
        }else{//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            //Error response if API fails:
            own = new OwnerTable();
            own.setAdmin_id(admin_id);
            own.setToken_id(token_id);
            own.setIsError(true);
            own.setComment("[param was either missing or invalid. M.E.U.A.]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        return JSONUtil.entityToJSONResponse(own);
    }//FUNC::END
    
    @GET
    @Path("makeEntryUsing_random")
    public Response makeEntryUsing_random
                    (@DefaultValue("-1") @QueryParam("token_id") long token_id){
        OwnerTable own;
        
        if(token_id >= 0){
            own = OwnerTransUtil.makeEntryUsing_random(token_id);
        }else{//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            //Error response if API fails:
            own = new OwnerTable();
            own.setToken_id(token_id);
            own.setIsError(true);
            String msg = "[makeEntryUsing_random:]";
            msg+="[param was either missing or invalid. M.E.U.R.]";
            own.setComment(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        return JSONUtil.entityToJSONResponse(own);
    }//FUNC::END
    
    
}//CLASS::END
