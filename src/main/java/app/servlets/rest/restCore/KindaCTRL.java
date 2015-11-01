package app.servlets.rest.restCore;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import app.config.constants.ServletClassNames;
import app.config.constants.identifiers.FuncNameReg;
import app.dbDataAbstractions.entities.tables.KindaTable;
import app.dbDataAbstractions.entities.tables.NinjaTable;
import app.transactions.util.TransUtil;
import app.transactions.util.tables.kinda.KindaTransUtil;
import app.transactions.util.tables.ninja.NinjaTransUtil;
import utils.JSONUtil;

/**
 *
 * @author jmadison:2015.10.27(Oct27th,Year2015.Tuesday)
 */
@Path(ServletClassNames.KindaCTRL_MAPPING)
public class KindaCTRL extends BaseCTRL {

    @GET
    @Path(FuncNameReg.MAKE_NEXT_KINDA) //removed slash at end. Lets try again.
    public Response make_next_kinda(){
        
         //ENTER transaction:
        Session ses = TransUtil.enterTransaction();

        //Transaction logic:
        KindaTable kt = KindaTransUtil.makeNextKinda();
        TransUtil.markEntityForSaveOnExit(kt);

        //EXIT transaction:
        TransUtil.exitTransaction(ses, true);

        //Return entity as body of 200/ok response:
        return JSONUtil.entityToJSONResponse(kt);
        
    }//FUNC::END
    
}//CLASS::END
