package test.servlets.rest.restCore;

import javax.ws.rs.Path;
import test.config.constants.ServletClassNames;

/**
 * Controller for operations involving a DECK. A deck is the core contents
 * of a single riddle-trial (test). It contains all of the cuecards that the
 * Jester will use to quiz the ninja on the game show.
 * 
 * @author jmadison :2015.10.26 (Oct26th, Year 2015, Monday)
 */
@Path(ServletClassNames.DeckCTRL_MAPPING)
public class DeckCTRL extends BaseCTRL{
    //should be able to ping now.
}//CLASS::END
