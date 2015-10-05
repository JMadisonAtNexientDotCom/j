package test.dbDataAbstractions.requestAndResponseTypes.postTypes.postResponse;

import java.lang.reflect.Array;
import java.util.ArrayList;
import primitives.TypeWithCommentBase;
import test.config.constants.EntityErrorCodes;

/**
 * *****************************************************************************
 * DEFINE: Coffer:
 * a strongbox or small chest for holding valuables.
 * synonyms:	strongbox, money box, cashbox, money chest, treasure chest, safe;
 * *****************************************************************************
 * 
 * It's size is SMALL. Which means this word makes more sense than "safe".
 * The Coffer is a response to an Edict.
 * A coffer contains TOKENS that will be distributed to the ninjas so that
 * they can begin their trials.
 * 
 * Tokens are kind of like Willy-Wanka's golden tickets. They are needed
 * in order to gain access.
 * 
 * @author jmadison
 */
public class Coffer extends PostResponseType{
    
    public ArrayList<Ticket> tickets = new ArrayList<Ticket>();
    
    /**
     * Make a coffer that is an ERROR MESSAGE.
     * Original usage: Stubbing TrialTransUtil.dispatchTokens() method in 
     *                 way that will allow me to still test the API call on the
     *                 UI side of things.
     * @param msg :A specific error message to help person reading it fix the
     *             bug in the program.
     * @return : A coffer object configured as an error.
     */
    public static Coffer makeErrorCoffer(String msg){
        
        //Create base object, configure as error:
        Coffer op = new Coffer();
        op.tickets = new ArrayList<Ticket>();
        op.comment   = msg;
        op.errorCode = EntityErrorCodes.GENERIC_ERROR;
        op.isError   = true;
    
        //Make one entry that is an ERROR TICKET.
        //So that any display code displaying tickets will show errors.
        Ticket errorTicket = Ticket.makeErrorTicket(msg);
        op.tickets.add( errorTicket);
        
        //return the Coffer that has been configured as an error:
        return op;
        
    }//FUNC::END
    
}//CLASS::END
