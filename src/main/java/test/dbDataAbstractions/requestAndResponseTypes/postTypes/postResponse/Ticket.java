package test.dbDataAbstractions.requestAndResponseTypes.postTypes.postResponse;

import test.config.constants.identifiers.VarNameReg;
import annotations.Verbatim;
import annotations.VerbatimValidatorUtil;
import primitives.Flaggable;
import primitives.TypeWithCommentBase;
import test.config.constants.EntityErrorCodes;

/**
 * A ticket is a piece of card-stock with the Ninja's name on it and
 * and a Trial-Token taped to it.
 * 
 * It is like a ticket for a movie, gameshow, or concert.
 * It has enough information so that both the Ninja and the
 * establishment (game show box office) know enough to identify the Ninja.
 * AKA: Ninja can identify themselves from the ticket.
 *      Show  workers can identify ninja from ticket, and will know where
 *            that Ninja needs to go in order to complete their trial.
 * 
 * @author jmadison
 */
public class Ticket extends TypeWithCommentBase{
    
    static{////////
        doStaticInit();
    }//////////////
    
    private static void doStaticInit(){
        VerbatimValidatorUtil.validateAnnotatedFields(Ticket.class);
    }//FUNC::END
    
    /** The HASH of the token that now belongs to the ninja.
     *  NOT THE ID!!!! The id's are for internal/back-end database use only. **/
    @Verbatim( name=VarNameReg.TOKEN_HASH)
    public String token_hash   = "TOKEN_HASH_NOT_SET";
    
    /** The ID of the ninja within the system. **/
    @Verbatim( name=VarNameReg.NINJA_ID)
    public long ninja_id   = (-1);
    
    /** We don't want all of the ninja's info. Just enough for
     *  simple confirmation of ownership that will be easy for
     *  our UI/FRONT-END to use. It would be annoying if they got back
     *  a token, but then had to make another service call so that they
     *  could figure out the display name of the ninja from the ninja's ID# **/
    @Verbatim( name=VarNameReg.NINJA_NAME)
    public String ninja_name = "NINJA_NAME_NOT_SET";
    
    /**
     * Make a ticket configured as an error.
     * Will help UI people spot problems easier if we return expected
     * data structure, but flagged as an error.
     * @param msg :The error message we wish to convey.
     * @return 
     */
    public static Ticket makeErrorTicket(String msg){
        Ticket op    = new Ticket();
        op.comment   = msg;
        op.isError   = true;
        op.errorCode = EntityErrorCodes.GENERIC_ERROR;
        return op;
    }//FUNC::END
    
}//CLASS::END
