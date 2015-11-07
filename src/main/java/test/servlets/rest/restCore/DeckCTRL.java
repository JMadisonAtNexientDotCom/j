package test.servlets.rest.restCore;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import test.config.constants.ServletClassNames;
import test.config.constants.identifiers.FuncNameReg;
import test.config.constants.identifiers.VarNameReg;
import test.dbDataAbstractions.entities.tablePojos.Deck;
import test.dbDataAbstractions.requestAndResponseTypes.PossibleErrorResponse;
import test.transactions.util.TransUtil;
import test.transactions.util.tables.deck.DeckPersistUtil;
import test.transactions.util.tables.deck.DeckTransUtil;
import utils.JSONUtil;

/**
 * Controller for operations involving a DECK. A deck is the core contents
 * of a single riddle-trial (test). It contains all of the cuecards that the
 * Jester will use to quiz the ninja on the game show.
 * 
 * @author jmadison :2015.10.26 (Oct26th, Year 2015, Monday) **/
@Path(ServletClassNames.DeckCTRL_MAPPING)
public class DeckCTRL extends BaseCTRL{
    
    /**
     * Exactly same behavior and signature as "GENERATE_DECK", however
     * this test will also PERSIST the deck into the database.
     * @param card_count        :See GENERATE_DECK
     * @param number_of_choices :See GENERATE_DECK
     * @param tru_min           :See GENERATE_DECK
     * @param tru_max           :See GENERATE_DECK
     * @return                  :See GENERATE_DECK **/
    @GET
    @Path(FuncNameReg.GENERATE_DECK_AND_PERSIST_IT)
    public Response generate_deck_and_persist_it(
                @QueryParam(VarNameReg.CARD_COUNT)        int card_count, 
                @QueryParam(VarNameReg.NUMBER_OF_CHOICES) int number_of_choices,
                @QueryParam(VarNameReg.TRU_MIN)           int tru_min,
                @QueryParam(VarNameReg.TRU_MAX)           int tru_max){
        
        //Handle Error Inputs:
        PossibleErrorResponse pos = INPUTCHECK_generate_deck
                              (card_count, number_of_choices, tru_min, tru_max);
        if(pos.exists){return pos.error;}
        
        //Enter transaction, do logic, exit transaction:
        Session ses = TransUtil.enterTransaction();
        Deck trial_guts;
        trial_guts = DeckTransUtil.generateDeck
                              (card_count, number_of_choices, tru_min, tru_max);
        //Persist deck. Must be done inside transaction state:
        DeckPersistUtil.persist(trial_guts);
        TransUtil.exitTransaction(ses);
        
        //return the deck as JSON response:
        return JSONUtil.compositeEntityToJSONResponse(trial_guts);
        
    }//FUNC::END
    
    /**
     * 
     * @param card_count :How many cuecards are in this deck?
     *                    You may get LESS in your deck than asked for
     *                    if there is NOT enough data to make that many
     *                    unique questions.
     * @param number_of_choices :How many choices (quips) should 
     *                           be on each cuecard?
     * @param tru_min :The minimum number of true answers to target.
     *                 ZERO is allowed.
     * @param tru_max :The maximum number of true answers to target.
     *                 ZERO is allowed.
     * @return :Returns a deck object that contains a collection of cuecards.
     *          Cuecards being what Jester uses to quiz the Ninja.
     */
    @GET
    @Path(FuncNameReg.GENERATE_DECK)
    public Response generate_deck(
                @QueryParam(VarNameReg.CARD_COUNT)        int card_count, 
                @QueryParam(VarNameReg.NUMBER_OF_CHOICES) int number_of_choices,
                @QueryParam(VarNameReg.TRU_MIN)           int tru_min,
                @QueryParam(VarNameReg.TRU_MAX)           int tru_max){
        
        //Handle Error inputs:
        PossibleErrorResponse pos = INPUTCHECK_generate_deck
                              (card_count, number_of_choices, tru_min, tru_max);
        if(pos.exists){return pos.error;}
        
        //Enter transaction, do logic, exit transaction:
        Session ses = TransUtil.enterTransaction();
        Deck trial_guts;
        trial_guts = DeckTransUtil.generateDeck
                              (card_count, number_of_choices, tru_min, tru_max);
        TransUtil.exitTransaction(ses);
        
        //return the deck as JSON response:
        return JSONUtil.compositeEntityToJSONResponse(trial_guts);
                
    }//FUNC::END
    
    //Checks inputs from generate_deck functions and returns an error response
    //If we find there is a problem.
    private PossibleErrorResponse INPUTCHECK_generate_deck(
                @QueryParam(VarNameReg.CARD_COUNT)        int card_count, 
                @QueryParam(VarNameReg.NUMBER_OF_CHOICES) int number_of_choices,
                @QueryParam(VarNameReg.TRU_MIN)           int tru_min,
                @QueryParam(VarNameReg.TRU_MAX)           int tru_max){
        String err_msg = "";
        boolean has_err = false;
        if(tru_min > tru_max){
            has_err =true;
            err_msg+="[tru_min > tru_max]";
        }//
        if(number_of_choices < tru_min){
            has_err = true;
            err_msg+="[Improper fraction inevitable. tru_min]";
        }//
        if(number_of_choices < tru_max){
            has_err = true;
            err_msg+="[Improper fraction inevitable. tru_max]";
        }//
        
        PossibleErrorResponse op = new PossibleErrorResponse();
        if(has_err){
            Deck err_deck = Deck.makeErrorDeck(err_msg);
            Response err = JSONUtil.compositeEntityToJSONResponse(err_deck);
            op.error  = err;
            op.exists = true;
        }//
        
        return op;
        
    }//FUNC::END
 
    
}//CLASS::END
