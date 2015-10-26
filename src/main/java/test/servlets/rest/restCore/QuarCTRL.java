package test.servlets.rest.restCore;

import javax.ws.rs.Path;
import test.config.constants.ServletClassNames;

/**
 * Controller for operations involving a QUAR.
 * A Quar is a stack of answer slates. Representing an ninja's entire
 * response to a Riddle-Trial (test) it has been given.
 * Riddle-Trial data is stored in a DECK.
 * Thus, a QUAR is a ninja's response to a DECK.
 * 
 * @author jmadison :2015.10.26 (Oct26th, Year 2015, Monday)
 */
@Path(ServletClassNames.QuarCTRL_MAPPING)
public class QuarCTRL extends BaseCTRL{
    //should be able to ping now.
}//CLASS::END
