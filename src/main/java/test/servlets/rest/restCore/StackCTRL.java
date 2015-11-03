package test.servlets.rest.restCore;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import test.config.constants.ServletClassNames;
import test.config.constants.identifiers.FuncNameReg;
import test.dbDataAbstractions.entities.tables.KindaTable;
import test.dbDataAbstractions.entities.tables.StackTable;
import test.transactions.util.TransUtil;
import test.transactions.util.tables.stack.StackTransUtil;
import utils.JSONUtil;


/**
 * Used to test that StackTable can be read and written to.
 * 
 * @author jmadison :2015.11.02_0739PM(Nov11th,Year2015.Monday)
 */
@Path(ServletClassNames.StackCTRL_MAPPING) 
public class StackCTRL extends BaseCTRL{
    
    
    /**-------------------------------------------------------------------------
     * Used to test that table has been wired up to hibernate correctly.
     * 
     * Original use of this controller:
     * To test making of new record. May or may not be used in
     * actual development. Just here to make sure we can
     * read+write to table without errors.
     * @return : An unpopulated stub. EXCEPT FOR THE PRIMARY KEY. We set that.
     -------------------------------------------------------------------------*/  
    @GET
    @Path(FuncNameReg.MAKE_NEXT_STACK)
    public Response make_next_stack(){
        
         //ENTER transaction:
        Session ses = TransUtil.enterTransaction();

        //Transaction logic:
        StackTable stk = StackTransUtil.makeNextStack();
        ses.save(stk); //<--force primary key to generate.

        //EXIT transaction:
        TransUtil.exitTransaction(ses, true);

        //Return entity as body of 200/ok response:
        return JSONUtil.entityToJSONResponse(stk);
        
    }//FUNC::END
        
}//CLASS::END

    

