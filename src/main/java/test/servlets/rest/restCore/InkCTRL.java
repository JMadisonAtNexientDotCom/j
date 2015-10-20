package test.servlets.rest.restCore;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import primitives.LongBool;
import test.config.constants.ServletClassNames;
import test.config.constants.identifiers.FuncNameReg;
import test.config.constants.identifiers.VarNameReg;
import test.dbDataAbstractions.entities.tables.RhymeTable;
import test.transactions.util.tables.ink.InkPersistUtil;
import utils.JSONUtil;

/**
 * This controller is mainly for testing methods involving the
 * ink_table and ink_purse database tables.
 * I don't imagine I will ever need it for actual production.
 * @author jmadison:2015.10.20(Oct20th,Year2015)
 */
@Path(ServletClassNames.InkCTRL_MAPPING)
public class InkCTRL extends BaseCTRL{
    
    
    @GET
    @Path(FuncNameReg.PERSIST_GROUP_OF_1_QUIP_TEST)
    public Response persist_group_of_1_quip_test(
             @QueryParam(VarNameReg.Q1_ID)long q1_id){
        
        List<Long> ids = new ArrayList<Long>();
        ids.add(q1_id);
        return convertAndRespond(ids);
    }//FUNC::END
    
    @GET
    @Path(FuncNameReg.PERSIST_GROUP_OF_2_QUIP_TEST)
    public Response persist_group_of_2_quip_test(
            @QueryParam(VarNameReg.Q1_ID)long q1_id,
            @QueryParam(VarNameReg.Q2_ID)long q2_id){
        
        List<Long> ids = new ArrayList<Long>();
        ids.add(q1_id);
        ids.add(q2_id);
        return convertAndRespond(ids);
    }//FUNC::END
    
    @GET
    @Path(FuncNameReg.PERSIST_GROUP_OF_3_QUIP_TEST)
    public Response persist_group_of_3_quip_test(
            @QueryParam(VarNameReg.Q1_ID)long q1_id,
            @QueryParam(VarNameReg.Q2_ID)long q2_id,
            @QueryParam(VarNameReg.Q3_ID)long q3_id){
        
        List<Long> ids = new ArrayList<Long>();
        ids.add(q1_id);
        ids.add(q2_id);
        ids.add(q3_id);
        return convertAndRespond(ids);
    }//FUNC::END
    
    @GET
    @Path(FuncNameReg.PERSIST_GROUP_OF_4_QUIP_TEST)
    public Response persist_group_of_4_quip_test(
            @QueryParam(VarNameReg.Q1_ID)long q1_id,
            @QueryParam(VarNameReg.Q2_ID)long q2_id,
            @QueryParam(VarNameReg.Q3_ID)long q3_id,
            @QueryParam(VarNameReg.Q4_ID)long q4_id){
        
        List<Long> ids = new ArrayList<Long>();
        ids.add(q1_id);
        ids.add(q2_id);
        ids.add(q3_id);
        ids.add(q4_id);
        return convertAndRespond(ids);
    }//FUNC::END
    
    //Shared function amongs by persistence tests//
    public Response convertAndRespond(List<Long> ids){
        List<RhymeTable> rhymes = convertToRhymes(ids);
        LongBool lb = InkPersistUtil.persistQuips(rhymes);
        return JSONUtil.whateverToJsonResponse(lb);
    }//FUNC::END
    
    /**
     * Takes a list of rhyme_ids and converts them into new entities
     * that are NOT persisted into the database.
     * @param ids :List of rhyme_id.
     * @return :List of RhymeTable **/
    private static List<RhymeTable> convertToRhymes(List<Long>ids){
        List<RhymeTable> op = new ArrayList<RhymeTable>();
        RhymeTable curRhyme;
        for(Long l : ids){
            curRhyme = new RhymeTable();
            curRhyme.setId(l);
            curRhyme.setComment("[Touched by testing method.]");
            curRhyme.setText("[Testing:]" + Long.toString(l));
            op.add(curRhyme);
        }//next long
        
        //return output:
        return op;
    }//FUNC::END
    
}//CLASS::END
