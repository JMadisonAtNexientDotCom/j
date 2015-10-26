package test.servlets.rest.restCore;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import test.MyError;
import test.config.constants.ServletClassNames;
import test.config.constants.identifiers.FuncNameReg;
import test.config.constants.identifiers.VarNameReg;
import test.dbDataAbstractions.entities.bases.CompositeEntityBase;
import test.dbDataAbstractions.entities.composites.CueCard;
import test.transactions.util.TransUtil;
import test.transactions.util.forNoClearTableOwner.riddleRhyme.RiddleRhymeTransUtil;
import test.transactions.util.tables.cuecard.CuecardTransUtil;
import test.transactions.util.tables.riddle.RiddleTransUtil;
import utils.JSONUtil;

/**
 * Rest service for making and testing cuecards.
 * CuecardTable.java == Struct/Entity version of cuecard.
 * Cuecard.java      == object version of cue-card
 * 
 * @author jmadison :2015.10.26
 */
@Path(ServletClassNames.CuecardCTRL_MAPPING)
public class CuecardCTRL extends BaseCTRL{
    
    @GET
    @Path(FuncNameReg.MAKE_FILLED_OUT_CUE_CARD_AND_PERSIST_IT)
    public Response make_filled_out_cue_card_and_persist_it(
                @QueryParam(VarNameReg.RIDDLE_ID)         long riddle_id, 
                @QueryParam(VarNameReg.NUMBER_OF_CHOICES) int number_of_choices,
                @QueryParam(VarNameReg.NUMBER_OF_TRUTHS)  int number_of_truths){
        doError("TODO!....");
        return null; //todo.
    }//FUNC::END
    
    @GET
    @Path(FuncNameReg.MAKE_FILLED_OUT_CUE_CARD)
    public Response make_filled_out_cue_card(
                @QueryParam(VarNameReg.RIDDLE_ID)         long riddle_id, 
                @QueryParam(VarNameReg.NUMBER_OF_CHOICES) int number_of_choices,
                @QueryParam(VarNameReg.NUMBER_OF_TRUTHS)  int number_of_truths){
        
        CueCard c = PRIVATE_make_filled_out_cue_card
                               (riddle_id, number_of_choices, number_of_truths);
        
        //Create our response:
        CompositeEntityBase ceb = (CompositeEntityBase)c;
        return JSONUtil.compositeEntityToJSONResponse(ceb);
    
    }//FUNC::END
    
    private CueCard PRIVATE_make_filled_out_cue_card(
                @QueryParam(VarNameReg.RIDDLE_ID)         long riddle_id, 
                @QueryParam(VarNameReg.NUMBER_OF_CHOICES) int number_of_choices,
                @QueryParam(VarNameReg.NUMBER_OF_TRUTHS)  int number_of_truths){
        //Enter transaction state:
        Session ses = TransUtil.enterTransaction();
        
        //Use utility to get what you want:
        CueCard c;
        
        //Handle Error checking more gracefully on REST servlet by returning
        //An "Error Cue Card" Still respond with 200/OK. But the information
        //Sent back will indicate an error on the non-server side.
        if(number_of_choices < 1){
            String msg = "[makeFilledOutCueCard needs at least one choice!]";
            c = CueCard.makeErrorCueCard(msg, number_of_choices);
        }else
        if(number_of_truths < 0){
            String msg = "[CAN have zero truths, but NEVER negative!!]";
            c = CueCard.makeErrorCueCard(msg, number_of_choices);
        }else
        if(false == RiddleTransUtil.doesRiddleExist(riddle_id)){
            String msg = "[riddle of that id does not exist in database]:";
            msg+= "id==" + riddle_id +"]";
            if(riddle_id < 0){ msg+= "[(riddle ID is NEGATIVE)]";}
            c = CueCard.makeErrorCueCard(msg, number_of_choices);
        }else
        if(number_of_truths > number_of_choices)
        {
            String msg = "[numberOfTruths>numberOfChoices]";
            c = CueCard.makeErrorCueCard(msg, number_of_choices);
        }else{
            //Our non-error case. Note that ZERO number of truths is allowed.
            c = CuecardTransUtil.makeFilledOutCueCard
                                    (riddle_id, number_of_choices, number_of_truths);
        }//BLOCK::END
        
        //EXIT TRANSACTION!!!
        TransUtil.exitTransaction(ses, TransUtil.EXIT_NO_SAVING);
        
        //return the cuecard:
        return c;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = CuecardCTRL.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
