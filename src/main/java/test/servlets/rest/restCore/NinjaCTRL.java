package test.servlets.rest.restCore;

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
import test.dbDataAbstractions.entities.containers.BaseEntityContainer;
import test.transactions.util.forOwnedMainlyByOneTable.ninja.NinjaTransUtil;
import test.dbDataAbstractions.entities.tables.NinjaTable;
import test.dbDataAbstractions.entities.tables.TokenTable;
import test.transactions.cargoSystem.dataTypes.EntityCage;
import test.transactions.cargoSystem.dataTypes.GalleonBarge;
import test.transactions.cargoSystem.transactionBuilder.DryDock;
import utils.JSONUtil;
import utils.StringUtil;

////////////////////////////////////////////////////////////////////////////////
@Path(ServletClassNames.NinjaCTRL_MAPPING) //<--If this @Path path matches the path of 
           //   ANY OTHER JERSEY SERVLET your servlets will all fail. 
           //   Even if the full path to this servlet is unique. ARGH!!!!
public class NinjaCTRL extends BaseCTRL {
    
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
        
        //Inputs may be valid numbers. But perhaps one of those numbers is
        //negative? Or asking for ZERO results per page?
        boolean badIn = false; //do we have bad inputs from API user?
        String bim = ""; //bad input error message.
        if(pageIndex < 0){ badIn=true; bim+="[negativePageIndex]";}
        if(numResultsPerPage <= 0){badIn=true; bim+="[numResultsPerPage<=0]";}
        if(badIn){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            Clan badInputErrorPageResults = Clan.makeErrorClan(bim);
            badInputErrorPageResults.setErrorCode
                                      (EntityErrorCodes.GARBAGE_IN_GARBAGE_OUT);
            //Exit transaction state before returning the error:
            doExit_getPageOfNinjas(ses, TransUtil.EXIT_NO_SAVING);
            return JSONUtil.compositeEntityToJSONResponse
                                                     (badInputErrorPageResults);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
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
            doExit_getPageOfNinjas(ses, TransUtil.EXIT_NO_SAVING);
            doError("[BACK_END_ERROR:bad delta!]");
            return null;
            //return JSONUtil.compositeEntityToJSONResponse(errorClan);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //Check math: Does delta get number of results you are expecting:
        if(delta != numResultsPerPage){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            doExit_getPageOfNinjas(ses, TransUtil.EXIT_NO_SAVING);
            doError("[delta does not line up with results requested]");
            return null;
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        List<BaseEntity> ninjas = TransUtil.getEntitiesUsingRange
                                        (NinjaTable.class, startIndex, endIndex);
        Clan pageOfNinjas = Clan.makeClanUsingBaseEntities
                                                       (ninjas, "PAGE RESULTS");
        
        //CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
       
        //EXIT TRANSACTION:
        doExit_getPageOfNinjas(ses, TransUtil.EXIT_NO_SAVING);
        
        //If pageOfNinjas is NOT an error, but there are no members,
        //put an "out of bounds" ninja into the list to let user of API
        //know you have gotten to the end.
        if(false  == pageOfNinjas.getIsError() 
                                            && 0 ==pageOfNinjas.members.size()){
            NinjaTable oobNinja = new NinjaTable();
            oobNinja.setName("[END OF LIST]");
            oobNinja.setEmail("[END OF LIST]");
            oobNinja.setPhone(-989898);
            oobNinja.setPortfolio_url("[END OF LIST]");
            oobNinja.setIsError(true);
            pageOfNinjas.members.add(oobNinja);
        }else{ //OUT OF BOUNDS POSITIVE ^^^
            //If all is well, lets give the clan display name something that
            //is informative. How about the current page number?
            pageOfNinjas.displayName = "PG#" + pageIndex_AsString;
        }//BLOCK::END
        
        //RETURN DATA:
        return JSONUtil.compositeEntityToJSONResponse(pageOfNinjas);
        
    }//FUNC::END
    
    /** Wrapper for transaction exiting so that balancing is preserved
     *  when searching for usages. Doing this as an attempt to make it easier
     *  to debug the pairing of these commands.
     * @param inShouldSave :Should save on exit?
     */
    private static void doExit_getPageOfNinjas
                                            (Session ses, Boolean inShouldSave){
        TransUtil.exitTransaction(ses, inShouldSave);
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
            errorNinja.setPortfolio_url("www.ERROR.com");
            
            //EXIT TRANSACTION, and return error response:
            makeNinjaRecord_exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
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
        makeNinjaRecord_exitTransaction(ses, TransUtil.EXIT_WITH_SAVE);
        
        //Return entity as body of 200/ok response:
        return JSONUtil.entityToJSONResponse(nt);
       
    }//FUNC::END
    
    /**
     * Wrapper to maintain bracket balancing of enter/exit calls.
     * @param ses   :Transaction session to exit.
     * @param doSave:Do we have entities that need saving?
     */
    private static void makeNinjaRecord_exitTransaction
                                                  (Session ses, boolean doSave){
        TransUtil.exitTransaction(ses, doSave);
    }//FUNC::END
                                                  
    @GET
    @Path(FuncNameReg.GET_NINJA_BY_TOKEN_HASH)
    public Response get_ninja_by_token_hash
                         (@QueryParam(VarNameReg.TOKEN_HASH) String token_hash){
        //ENTER TRANSACTION:
        Session ses = TransUtil.enterTransaction();
        
        //Transaction logic:
        BaseEntityContainer bec;
        bec = NinjaTransUtil.getNinjaByTokenHash(token_hash);
        
        //If container is empty, we will want to make an error response:
        NinjaTable ninja;
        if(false == bec.exists){
            ninja = new NinjaTable();
            String msg = "ERROR no ninja with that token hash";
            ninja.setName(msg);
            ninja.setEmail("ERRORatERRORdotCOM");
            ninja.setId(-777);
            ninja.setPhone(-987654321);
            ninja.setPortfolio_url("wwwdotERRORdotcom");
        }else{
            ninja = (NinjaTable)bec.entity;
        }//exists?
        
        //EXIT TRANSACTION:
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
        //Return result:
        return JSONUtil.entityToJSONResponse(ninja);
                             
    }//FUNC::END
        
    /** A more complicated implementation of get_ninja_by_id that is just
     *  meant to make sure that the cargoSystem is working for this type of
     *  request.
     * @param id :The ID of the ninja you want.
     * @return   :Returns the ninja entity.
     */                                        
    @GET
    @Path(FuncNameReg.GET_NINJA_BY_ID)
    public Response get_ninja_by_id(@QueryParam(VarNameReg.ID) long id){
            //ENTER transaction:
            Session ses = TransUtil.enterTransaction();
            
            //Transaction logic:
            NinjaTable nt;
            GalleonBarge barge;
            EntityCage cage;
            barge = DryDock.getNinjaByID(id);
            barge.embark();
            
            //Fetching info after barge has embarked:
            cage = barge.hold.getCageUsingSupplier(NinjaTable.class);
            nt = (NinjaTable)cage.merchandise.get(0);
            
            //Will have to exit transaction session without knowing
            //if saving is needed:
            
            //EXIT transaction:
            TransUtil.exitTransaction(ses);
            
            //Return entity as body of 200/ok response:
            return JSONUtil.entityToJSONResponse(nt);
    }//FUNC::END
                                                  
    /*xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
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
    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx*/
        
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
        Class clazz = NinjaCTRL.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END