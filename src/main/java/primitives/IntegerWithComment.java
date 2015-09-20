package primitives;

/**
 * Used as a return type for a json response.
 * The comment is for debugging purposes.
 * We live in an age where the overhead associated with such programming
 * style is minimal compared to the benefits you can gain from it.
 * @author jmadison
 */
public class IntegerWithComment extends TypeWithCommentBase{
    public int value = 0;
    
    //Empty Constructor:
    public IntegerWithComment(){};

    /*
    NOT NECESSARY TO SERIALIZE! NOW I KNOW!
    //Boiler-Plate getters and setters. May be necessary for JSONUtil.java
    //to serialize it!
    ////////////////////////////////////////////////////////////////////////////
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    ////////////////////////////////////////////////////////////////////////////
    */
    
    
}//CLASS::END
