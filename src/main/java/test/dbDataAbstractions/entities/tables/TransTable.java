package test.dbDataAbstractions.entities.tables;

import test.dbDataAbstractions.entities.bases.BaseEntity;

/**
 * A table that helps with debugging. Every time we save an entity, we will
 * make an entry into this table to help us debug.
 * @author jmadison :2015.09.30
 * 
 * Note to self: Try making changes to code in smaller steps. Else you get
 *               caught in an indefinite debugging cycle. Don't like indefinite
 *               debugging. Scary. What if the code never recovers!?
 */
public class TransTable extends BaseEntity{
    
    
    private long convo_id;
    private String table_name;
    private long mutate_id;
    private long record_id;
    private String record_comment;
    
    
    
}//CLASS::END
