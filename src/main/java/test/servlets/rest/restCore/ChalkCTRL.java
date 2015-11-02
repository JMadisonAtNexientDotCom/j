package test.servlets.rest.restCore;

import javax.ws.rs.Path;
import test.config.constants.ServletClassNames;

/**
 * Controller for operations involving CHALK.
 * Chalk is what appears on a single slate that the ninja fills out.
 * You could think of one line of chalk on a slate as
 * "one answer the ninja has scribed onto the slate."
 * 
 * @author jmadison :2015.10.26 (Oct26th, Year 2015, Monday)
 */
@Path(ServletClassNames.ChalkCTRL_MAPPING)
public class ChalkCTRL extends BaseCTRL{
    //should be able to ping now.
}//CLASS::END
