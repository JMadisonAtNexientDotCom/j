package primitives;

/**-----------------------------------------------------------------------------
 * A string with a comment. Used so we can send back a single string as a 
 * JSON response in a standardized way within our application.
 * @author jmadison :2015.09.21_1152PM
 ----------------------------------------------------------------------------**/
public class StringWithComment extends TypeWithCommentBase{
    
    public String value;
    public StringWithComment(){
        
        //This will help us if we ever enounter empty or null strings.
        //We will know the constructor is not the responsible party.
        value = "[STRINGWITHCOMMENT:last_touched_by_constructor]";
    }//FUNC:CONSTRUCTOR:END
    
}//FUNC::END
