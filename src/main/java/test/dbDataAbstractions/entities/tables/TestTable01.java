package test.dbDataAbstractions.entities.tables;

import test.dbDataAbstractions.entities.bases.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**-----------------------------------------------------------------------------
 * Some test code I did while I was learning.
 * http://www.roseindia.net/hibernate/hibernate4.3/Hibernate-4-hello-world-example.shtml
 * @author jmadison
 ----------------------------------------------------------------------------**/
@Entity
@Table(name="test_table_01")  
public class TestTable01 extends BaseEntity{
    
    @Id
    @Column(name="token")
    private String token;
    
    @Column(name="token_msg")
    private String token_msg;
    
    public String getToken(){ return token;}
    public void setToken(String token){ this.token = token;}
    
    public String getTokenMSG(){ return token_msg;}
    public void setTokenMSG(String tokenMSG){ this.token_msg = tokenMSG;}
    
}//CLASS::END






