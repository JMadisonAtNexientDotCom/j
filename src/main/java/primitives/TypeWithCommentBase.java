package primitives;

/**-----------------------------------------------------------------------------
 * Base class for BooleanWithComment.java and IntegerWithComment.java
 * For when we want to serialize simple primitives into JSON response.
 * 
 * The comment part is for debugging.
 * Sometimes debugability is better than optimization.
 * And I am not against live-debug information in working software.
 * 
 * Look at the Mountain Dew soda pop cans in the office. They have a color
 * swatch pallet on them. That is definitely debug information on the final
 * product.
 * 
 * @author jmadison :2015.09.20_0705PM
 ----------------------------------------------------------------------------**/
public class TypeWithCommentBase {
    public String comment = "NO_COMMENT_SET";
    
    /**-------------------------------------------------------------------------
     *  Flagged to true if the object is an error.
     *  We do this so that UI people have an easy standardized
     *  structure with which to validate response json data.
     ------------------------------------------------------------------------**/
    public boolean isError = false;
}//CLASS::END
