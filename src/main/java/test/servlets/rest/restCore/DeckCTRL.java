package test.servlets.rest.restCore;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import test.config.constants.ServletClassNames;
import test.config.constants.identifiers.FuncNameReg;
import test.config.constants.identifiers.VarNameReg;
import test.dbDataAbstractions.entities.composites.Deck;
import test.transactions.util.TransUtil;
import test.transactions.util.tables.deck.DeckTransUtil;
import utils.JSONUtil;

/**
 * Controller for operations involving a DECK. A deck is the core contents
 * of a single riddle-trial (test). It contains all of the cuecards that the
 * Jester will use to quiz the ninja on the game show.
 * 
 * @author jmadison :2015.10.26 (Oct26th, Year 2015, Monday)
 */
@Path(ServletClassNames.DeckCTRL_MAPPING)
public class DeckCTRL extends BaseCTRL{
    
    
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
        
        if(has_err){
            Deck err_deck = Deck.makeErrorDeck(err_msg);
            return JSONUtil.compositeEntityToJSONResponse(err_deck);
        }//
        
        Session ses = TransUtil.enterTransaction();
        
        Deck trial_guts;
        trial_guts = DeckTransUtil.generateDeck
                              (card_count, number_of_choices, tru_min, tru_max);
        
        TransUtil.exitTransaction(ses);
        
        //return the deck as JSON response:
        return JSONUtil.compositeEntityToJSONResponse(trial_guts);
                
    }//FUNC::END
    
    
}//CLASS::END
