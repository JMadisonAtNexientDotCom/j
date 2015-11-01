package app.servlets.rest.restCore;

import com.fasterxml.jackson.core.JsonProcessingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import app.dbDataAbstractions.entities.tables.TokenTable;
import app.transactions.util.tables.token.TokenTransUtil;
import app.transactions.util.TransUtil;

//Attempt to convert to object to JSON:
////////////////////////////////////////////////////////////////////////////////
//http://www.mkyong.com/java/how-to-convert-java-object-to-from-json-jackson/
import java.io.File;
import java.io.IOException;
//import org.codehaus.jackson.JsonGenerationException;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.map.ObjectMapper;
//import com.fasterXML
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import app.MyError;
import app.debug.DebugConsts;
import app.config.constants.ServletClassNames;
import app.config.constants.identifiers.FuncNameReg;
import app.transactions.cargoSystem.dataTypes.EntityCage;
import app.transactions.cargoSystem.dataTypes.GalleonBarge;
import app.transactions.cargoSystem.transactionBuilder.DryDock;
import utils.JSONUtil;


////////////////////////////////////////////////////////////////////////////////

@Path( ServletClassNames.TokenCTRL_MAPPING )
 //@Path(TokenRestService.class.getSimpleName() + "/")
//@Path("TokenRestService/") //<--By matching class name, we assure not servlet mapping collision.
public class TokenCTRL extends BaseCTRL {
 
    public static final String CLASS_MAPPING = TokenCTRL.class.getSimpleName() + "/";
    
    
        /** A simple stub function not meant to accomplish anything except
         *  NOT CRASH. We can't program more logic until we figure out what
         *  is going wrong with dynamic method invocation.
         */
        /*
        @GET
        @Path(FuncNameReg.DEBUGGER_STUB_FUNCTION)
        public Response debugger_stub_function(){
            //ENTER transaction:
            Session ses = TransUtil.enterTransaction();
            
            GalleonBarge barge = DryDock.debugger_stub_function();
            barge.embark();
            
            String str = JSONUtil.serializeObj_NoNULL(barge);
            Response r = JSONUtil.stringToJSONResponse(str,"hi",false);
            return r;
        }//FUNC::END
        */
            
   
        //This version is GUTTED and replaced with some experimental
        //code to do the same thing.
        @GET
        @Path(FuncNameReg.GET_NEXT_TOKEN) 
        public Response get_next_token(){
            
            //ENTER transaction:
            Session ses = TransUtil.enterTransaction();
            
            //Transaction logic:
            TokenTable tt;
            GalleonBarge barge;
            EntityCage cage;
            barge = DryDock.createNewToken();
            barge.embark();
            
            //Fetching info after barge has embarked:
            cage = barge.hold.getCageUsingSupplier(TokenTable.class);
            tt = (TokenTable)cage.merchandise.get(0);
            
            //Will have to exit transaction session without knowing
            //if saving is needed:
            
            //EXIT transaction:
            TransUtil.exitTransaction(ses);
            
            //Return entity as body of 200/ok response:
            return JSONUtil.entityToJSONResponse(tt);
            
        }//FUNC::END
    
    
        /**
        @GET
        @Path(FuncNameReg.GET_NEXT_TOKEN) //removed slash at end. Lets try again.
        public Response get_next_token(){
            
            //ENTER transaction:
            Session ses = TransUtil.enterTransaction();
            
            //Transaction logic:
            TokenTable tt = TokenTransUtil.makeNextToken();
            TransUtil.markEntityForSaveOnExit(tt);
            
            //EXIT transaction:
            TransUtil.exitTransaction(ses, true);
            
            //Return entity as body of 200/ok response:
            return JSONUtil.entityToJSONResponse(tt);
            
        }//FUNC::END
        **/
          
          
}//CLASS::END