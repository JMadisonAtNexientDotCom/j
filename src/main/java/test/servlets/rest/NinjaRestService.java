package test.servlets.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import test.transactions.util.TransUtil;

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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import test.MyError;
import test.config.constants.EntityErrorCodes;
import test.config.constants.ServletClassNames;
import test.config.constants.identifiers.FuncNameReg;
import test.config.constants.identifiers.VarNameReg;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import test.dbDataAbstractions.entities.composites.Clan;
import test.transactions.util.forOwnedMainlyByOneTable.ninja.NinjaTransUtil;
import test.dbDataAbstractions.entities.tables.NinjaTable;
import utils.JSONUtil;
import utils.StringUtil;

////////////////////////////////////////////////////////////////////////////////
@Path(ServletClassNames.NinjaRestService_MAPPING) //<--If this @Path path matches the path of 
           //   ANY OTHER JERSEY SERVLET your servlets will all fail. 
           //   Even if the full path to this servlet is unique. ARGH!!!!
public class NinjaRestService extends BaseRestService {
    
    /**
     * Gets a page of ninja entities.
     * ORIGNAL USAGE: A list of ninjas that could be selected in a UI
     * where you select a ninja from list and assign it a [trial/test]+token.
     * @param pageIndex_AsString :AS STRING so that invalid inputs will not
     *                            crash the application. Allows me to send back
     *                            a friendlier JSON error response that will
     *                            make the problem easier to find.
     * @param numResultsPerPage_AsString :AS STRING so invalid inputs will not
     *                                    crash the application. Allows me to 
     *                                    send back a friendlier JSON error 
     *                                    response that will make the problem 
     *                                    easier to find.
     * @return 
     */
    @GET
    @Path(FuncNameReg.GET_PAGE_OF_NINJAS)
    public Response getPageOfNinjas(
                    @QueryParam(VarNameReg.PAGE_INDEX)String pageIndex_AsString, 
                    @QueryParam(VarNameReg.NUM_RESULTS_PER_PAGE)
                                             String numResultsPerPage_AsString){
        
        //Error check inputs:
        boolean i0 = StringUtil.canBeParsedAsWholeNumber(pageIndex_AsString);
        boolean i1 = StringUtil.canBeParsedAsWholeNumber
                                                   (numResultsPerPage_AsString);
        boolean allInputsValid = (i0 && i1);
        if(false == allInputsValid){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            
            //Create our error object:
            NinjaTable errorNinja = new NinjaTable();
            String com = "";
            com += "PageIndex==[" + pageIndex_AsString + "]";
            com += "NumResultsPerPage==[" + numResultsPerPage_AsString + "]";
            errorNinja.setComment(com);
            errorNinja.setErrorCode(EntityErrorCodes.GARBAGE_IN_GARBAGE_OUT);
            errorNinja.setIsError(true);
            
            //Put this entity into EVERY SLOT of a list of the expected length:
            //Oh wait... we can't. That length might be BAD DATA... Just return
            //ONE.. Because we know they wanted at least one.
            List<NinjaTable> errorList = new ArrayList<NinjaTable>();
            errorList.add(errorNinja);
            
            //Pack this list into a CLAN object. (clan == group of ninjas)
            Clan clanOfErrors = Clan.makeClan(errorList, "[CLAN OF ERRORS]");
            
            //RETURN ERROR RESPONSE:
            return JSONUtil.compositeEntityToJSONResponse(clanOfErrors);
            
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
       
        //If no input errors, lets enter a transaction and return valid data:
        //ENTER TRANSACTION:
        Session ses = TransUtil.enterTransaction();
        
        //CORE LOGIC: //CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
        int pageIndex         = Integer.parseInt(pageIndex_AsString);
        int numResultsPerPage = Integer.parseInt(numResultsPerPage_AsString);
        
        //BUGFIX:YOU NEED THE STARTING NINJA INDEX!!!
        int startIndex = pageIndex * numResultsPerPage;
        //inclusive range.
        int endIndex = ((pageIndex + 1) * numResultsPerPage) - 1; 
        //|---0---||---1---||---2---| <--Page index value.
        //[ 0 , 1 ][ 2 , 3 ][ 4 , 5 ] <--result indicies. (results per page==2)
        //Last result on first page: ((0+1)*2)-1 == 1
        //Last result on last page:  ((2+1)*2)-1 == 5
        
        //Debug: Make sure we do not have invalid delta:
        int delta = endIndex - startIndex + 1;
        if(delta <= 0){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            //Clan errorClan = Clan.makeErrorClan("[BACK_END_ERROR:bad delta!]"); 
            TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
            doError("[BACK_END_ERROR:bad delta!]");
            //return JSONUtil.compositeEntityToJSONResponse(errorClan);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //Check math: Does delta get number of results you are expecting:
        if(delta != numResultsPerPage){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
            doError("[delta does not line up with results requested]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        List<BaseEntity> ninjas = TransUtil.getEntitiesUsingRange
                                        (NinjaTable.class, startIndex, endIndex);
        Clan pageOfNinjas = Clan.makeClanUsingBaseEntities
                                                       (ninjas, "PAGE RESULTS");
        
        //CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
       
        //EXIT TRANSACTION:
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
        //TEMPORARY DEBUG HACK:
        //if(pageOfNinjas.)
        
        //RETURN DATA:
        return JSONUtil.compositeEntityToJSONResponse(pageOfNinjas);
        
    }//FUNC::END
 
    @GET
    @Path(FuncNameReg.MAKE_NINJA_RECORD)
    public Response make_ninja_record(
            @QueryParam(VarNameReg.NAME)         String name ,
            @QueryParam(VarNameReg.PHONE)        String phone,
            @QueryParam(VarNameReg.EMAIL)        String email,
            @QueryParam(VarNameReg.PORTFOLIO_URL)String portfolio_url){
        
        //ENTER transaction:
        Session ses = TransUtil.enterTransaction();
        
        boolean allDigits = StringUtil.canBeParsedAsWholeNumber(phone);
        if(false == allDigits){//#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#
            NinjaTable errorNinja = new NinjaTable();
            errorNinja.setComment("error in make ninja record.");
            errorNinja.setIsError(true);
            errorNinja.setErrorCode(EntityErrorCodes.GARBAGE_IN_GARBAGE_OUT);
            errorNinja.setEmail("error@error.com");
            errorNinja.setPhone(-888);
            errorNinja.setName("Dr.Error");
            errorNinja.setPortfolioURL("www.ERROR.com");
            
            //EXIT TRANSACTION, and return error response:
            TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
            return JSONUtil.entityToJSONResponse(errorNinja);
        }//#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#E#
        
        //Transaction logic:
        //DESIGN NOTE: using phone as STRING input so that API endpoint
        //             resolves properly with bad data.
        long phoneAsLong = Long.parseLong(phone);
        NinjaTable nt = NinjaTransUtil.
                          makeNinjaRecord(name,phoneAsLong,email,portfolio_url);
        
        //Mark entity for save:
        TransUtil.markEntityForSaveOnExit(nt);
       
        //EXIT transaction, true==we have entities to save.
        TransUtil.exitTransaction(ses, TransUtil.EXIT_WITH_SAVE);
        
        //Return entity as body of 200/ok response:
        return JSONUtil.entityToJSONResponse(nt);
       
    }//FUNC::END
        
    @GET
    @Path(FuncNameReg.GET_NINJA_BY_ID)
    public Response get_ninja_by_id(@QueryParam(VarNameReg.ID) long id){
        //ENTER transaction:
        Session ses = TransUtil.enterTransaction();
        
        //Transaction logic:
        NinjaTable nt = NinjaTransUtil.getNinjaByID(id);
        
        //DO NOT MARK ENTITY FOR SAVE! 
        //Getting method is not meant to mutate.
       
        //EXIT transaction:
        //THERE ISNOTHING TO SAVE. So exit transaction with a false.
        TransUtil.exitTransaction(ses, false);
        
        //Return entity as body of 200/ok response:
        return JSONUtil.entityToJSONResponse(nt);
    }//FUNC::END
        
    @GET
    @Path(FuncNameReg.GET_NEXT_NINJA) //removed slash at end. Lets try again.
    public Response get_next_ninja(){

        //message msg is discarded and not used for now.

        //ENTER transaction:
        Session ses = TransUtil.enterTransaction();

        //Transaction logic:
        NinjaTable nt = NinjaTransUtil.makeNextNinja();
        TransUtil.markEntityForSaveOnExit(nt);

        //EXIT transaction:
        TransUtil.exitTransaction(ses, true);

        //Return entity as body of 200/ok response:
        return JSONUtil.entityToJSONResponse(nt);

    }//FUNC::END
    
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = NinjaRestService.class;
        err += clazz.getSimpleName();
        err += msg;
        throw new MyError(clazz, err);
    }//FUNC::END
    
}//CLASS::END