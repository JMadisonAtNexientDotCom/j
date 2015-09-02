package test.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Table that stores the tokens.
 * Each token has a comment that is directly associated with it.
 * It is for debugging purposes ONLY. And not to be used in actual logic.
 * Though will also be printed out on our /tokenLister page.
 * @author jmadison **/
@Entity
@Table(name="token_table")  
public class TokenTable extends BaseEntity{
    
   @Id
   @Column(name="value")
    private String value;
    
    @Column(name="comment")
    private String comment;
    
    public String getValue(){ return value;}
    public void setValue(String value){ this.value = value;}
    
    public String getComment(){ return comment;}
    public void setComment(String comment){ this.comment = comment;}
    
}//CLASS::END
