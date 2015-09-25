package frontEndBackEndIntegration.childComponents;


/**
 * Registry class used to store variable names that are used in:
 * 1. table columns.
 * 2. servlet parameters.
 * 3. UI JSP code.
 *
 * The variable names should be consistent among these.
 * So by creating constants for them, it will help enforce this.
 * 
 * DESIGN NOTE: Non-Static because we want the root front-end back end
 * class to be able to access these with dot operator.
 * 
 * Random comment: Just learned I can do this:
 *  {@value FBVarNameRegistry#TOKEN_ID}
 * In order to reference variables in documentation in a refactorable way.
 * 
 * @author jmadison
 */
public class FBVarNameRegistry {
    
    public final String TOKEN_ID = "token_id";
    public final String ADMIN_ID = "admin_id";
    public final String NINJA_ID = "ninja_id";
    
    
    
}//CLASS::END
