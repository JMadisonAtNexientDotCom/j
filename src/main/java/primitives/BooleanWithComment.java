package primitives;

/**-----------------------------------------------------------------------------
 * Used to return a boolean to the front-end as a json response.
 * @author jmadison :2015.09.20_0714PM
 ----------------------------------------------------------------------------**/
public class BooleanWithComment extends TypeWithCommentBase {
    
    /**-------------------------------------------------------------------------
     * By strict convention, all TypeWithCommentBase objects
     *  should have a .value property.
     ------------------------------------------------------------------------**/
    public boolean value = false;
    
    //Empty Constructor:
    public BooleanWithComment(){};
}//CLASS::END
