package primitives;

import test.config.constants.EntityErrorCodes;

/**
 * So that my non-entity types can also have .isError and .errorCode properties.
 * @author jmadison
 */
public class Flaggable {
    /**-------------------------------------------------------------------------
     *  Flagged to true if the object is an error.
     *  We do this so that UI people have an easy standardized
     *  structure with which to validate response json data.
     ------------------------------------------------------------------------**/
    public boolean isError   = false;
    public String  errorCode = EntityErrorCodes.NONE_SET;
}//CLASS::END
