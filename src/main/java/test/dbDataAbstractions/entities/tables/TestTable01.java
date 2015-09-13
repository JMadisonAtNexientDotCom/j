package test.dbDataAbstractions.entities.tables;

//import org.hibernate.annotations.DynamicUpdate;

import test.dbDataAbstractions.entities.bases.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 *http://www.roseindia.net/hibernate/hibernate4.3/Hibernate-4-hello-world-example.shtml
 * @author jmadison
 */
//
//@DynamicUpdate
@Entity
@Table(name="test_table_01")  
public class TestTable01 extends BaseEntity{
    
   @Id
   @Column(name="token")
    private String token;
    
    @Column(name="token_msg")
    private String tokenMSG;
    
    public String getToken(){ return token;}
    public void setToken(String token){ this.token = token;}
    
    public String getTokenMSG(){ return tokenMSG;}
    public void setTokenMSG(String tokenMSG){ this.tokenMSG = tokenMSG;}
    
}//CLASS::END






