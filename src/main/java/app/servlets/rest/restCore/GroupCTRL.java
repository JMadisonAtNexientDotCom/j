package app.servlets.rest.restCore;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import app.config.constants.ServletClassNames;
import app.config.constants.identifiers.FuncNameReg;
import app.config.constants.identifiers.VarNameReg;
import app.dbDataAbstractions.entities.tables.GroupTable;
import app.transactions.util.TransUtil;
import utils.JSONUtil;
import utils.StringUtil;

/**
 *
 * @author jmadison :2015.10.19
 */
@Path(ServletClassNames.GroupCTRL_MAPPING) 
public class GroupCTRL extends BaseCTRL{
    
    /**
     * Makes a new group in the group table.
     * The group table manages ALL GROUP IDS amongst all of the
     * tables. Two group IDS of the same value should NOT exist.
     * Even across separate tables.
     * @param name :The name of the group. Should be the name of the
     *                   table that the group exists in.
     * @param checksum  :A checksum telling us how many records exist in
     *                   this group.
     * @return :A serialized representation of the GroupTable entity. **/
    @GET
    @Path(FuncNameReg.MAKE_NEW_GROUP)
    public Response makeNewGroup(
            @QueryParam(VarNameReg.NAME)     String name, 
            @QueryParam(VarNameReg.CHECKSUM) String checksum){
        
        //Make sure checksum can be represented as long:
        boolean canParse = StringUtil.canBeParsedAsWholeNumber(checksum);
        if(false == canParse){
            GroupTable errorTable = GroupTable.makeErrorTable();
            errorTable.setComment("[UNPARSEABLE STRING]");
            return JSONUtil.entityToJSONResponse(errorTable);
        }//
        
        //Make sure valid name supplied:
        if(null==name || name.equals("")){
            GroupTable alsoError = GroupTable.makeErrorTable();
            alsoError.setComment("[bad group name supplied]");
            return JSONUtil.entityToJSONResponse(alsoError);
        }//
        
        long checksumAsLong = Long.parseLong(checksum);
        
        //Enter Transaction State:
        Session ses = TransUtil.enterTransaction();
      
        //LOGIC:
        GroupTable gt = new GroupTable();
        gt.name = name;
        gt.checksum = checksumAsLong;
        ses.save(gt);
        
        //Exit transaction state:
        TransUtil.exitTransaction(ses);
        
        //convert entity to response:
        Response res = JSONUtil.entityToJSONResponse(gt);
        return res;
     
    }//FUNC::END
        
}//CLASS::END
