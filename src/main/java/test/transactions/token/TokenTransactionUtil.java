package test.transactions.token;

import test.MyError;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
//import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
//import utils.HibernateUtil;
import test.entities.TokenTable;
import test.transactions.util.TransUtil;
import test.entities.BaseEntityContainer;
import test.debug.DebugConsts;
/**
 * A utility to manage transactions done to the TokenTable.
 * This type of organization may not be needed. But I have a huntch
 * it will be helpful.
 * @author jmadison
 */
public class TokenTransactionUtil {
    
    //Code originally from:
    //http://docs.jboss.org/hibernate/orm/5.0/quickstart/html/
    //Example 5. Saving entities
    public static void doTestTransaction(){
        
        //Throw error if transaction state of application is
        //NOT currently inside a transaction.
        TransUtil.insideTransactionCheck();
        
        //I have decided that we should NOT wrap pre-made transactions
        //between TransUtil.enterTransaction() and
        //        TransUtil.exitTransaction() commands.
        //HOWEVER! We should check that the application is inside a transaction
        //state for any transaction utility method.
        
        
        //TRANSACTION LOGIC:
        TokenTable tt = new TokenTable();
        tt.setToken(DebugConsts.HARD_CODED_TOKEN);
        tt.setComment("from doTestTransaction()");
        
        
    }//FUNC::END
    
    /** Returns the highest primary key within the token_table
     *  Used so we know how to create the encryption value for
     *  the NEXT token. Since all token primary keys are ascending
     *  and sequential, we just have to add +1 to whatever value we
     *  get here, and then encrypt it.
     * @return : The max primary key currently stored in database token_table **/
    public static long getMaxTokenIndex(){
        
        //Get the transaction we are in:
        Session ses = TransUtil.getActiveTransactionSession();
        
        //BUG FIX: Edge case of empty table:
        ////////////////////////////////////////////////////////////////////////
        //get number of entries currently in table. If there is nothing,
        //return zero: Returning zero is ASSUMING that our database starts
        //ordering primary keys at "1". Which I have observed in my MySQL
        //database. Cannot gaurantee that is the case for other databases.
        long tokenRecordCount = getHighestPrimaryKeyInTokenTable();
        if(0==tokenRecordCount){ return 0; }
        ////////////////////////////////////////////////////////////////////////
        
        //Query: Get maximum value for a given column:
        //SOURCE: amacleod 's answer on:
        //http://stackoverflow.com/questions/3900105/
        Criteria c = ses.createCriteria(TokenTable.class);
        c.addOrder(Order.desc(TokenTable.COLUMN_ID));
        c.setMaxResults(1);
        TokenTable t = (TokenTable)c.uniqueResult(); //<--TokenTable should really be called TokenRecord or TokenEntry, Record is more SQL specific. Go with that.
        

        //The id stored in this entity should be the HIGHEST id that
        //currently exists in the database for the corrosponding table.
        return t.getId();
        
    }//FUNC::END
    
    /** A wrapper for getNumberOfTokensInTable() so we can
     *  make a distinction between the two operations and
     *  [codify/document] the assumption we are making.
     * @return : A long representing the highest primary key value
                 currently stored within the database's token table **/
    private static long getHighestPrimaryKeyInTokenTable(){
        return getNumberOfTokensInTable();
    }//FUNC::END
    
    /** Gets the number of records within the token table.
     *  
     *  Original Usage:
     *  To do "look-before-you-leap" check on code that would error
     *  if executed on an empty table. Specifically the function 
     *  getMaxTokenIndex
     * 
     *  synonym: "findHighestPrimaryKey", this is HOW we are using the
     *           function. However, this function only works as such
     *           IF the tokens are in order as we expect.
     * 
     * @return : The number of tokens in the table.
     */
    private static long getNumberOfTokensInTable(){
        
        Session ses = TransUtil.getActiveTransactionSession();
        
        //SOURCE: How do we count rows in hibernate?
        //http://stackoverflow.com/questions/1372317/
        Criteria cri = ses.createCriteria(TokenTable.class);
        cri = cri.setProjection(Projections.rowCount());
        Long boxedOutput = (Long)cri.uniqueResult();
        long unboxedOutput = (long)boxedOutput;
        
        return unboxedOutput;
        
    }//FUNC::END
    
    /** Creates a new token. Tokens are sequenced in ascending order.
     *  The hash of each token is actually just an encryption of
     *  the id index.
     * 
     *  CONCERN: Calling this function multiple times without saving the
     *           session could lead to [duplicate/identical] token records
     *           in our database.
     * 
     * @return : Return the new token for usage.
     */
    public static TokenTable makeNextToken(){
        
        //Get the transaction we are in:
        Session ses = TransUtil.getActiveTransactionSession();
        
        //THIS ISN'T WORKING... And it looks like it might be hard to pull off.
        //http://stackoverflow.com/questions/9046971/mysql-equivalent-of-oracles-sequence-nextval
        //
        //Get the NEXT available ID in the TokenTable:
        //http://stackoverflow.com/questions/6346450/
        //how-to-get-the-auto-increment-primary-key-value-in-mysql-using-hibernate
        /*
        String qs = "";
        qs += "select nextval('";
        qs += TokenTable.TABLE_NAME;
        qs += ".";
        qs += TokenTable.COLUMN_ID;
        qs += "')";
        Query query = ses.createSQLQuery( qs );
        
        //CORRECT: http://stackoverflow.com/questions/6346450/
        long tokenIndex = (Long) query.list().get( 0 );
        
        //WRONG:
        //convert result of query into integer:
        //long tokenIndex = Long.parseLong( query.toString() );
        
        //now that we know the next number that will be used,
        //we can encrypt the number into a hash that will be our token:
        String tokenCode = encryptIndex(tokenIndex);
        
        //create a new token entity and populate it:
        TokenTable tt = new TokenTable(); //<<--"TokenTable" is more correctly "TokenRecord" or "TokenEntry", if you want to use proper relational database names.
        tt.setId(tokenIndex);
        tt.setToken(tokenCode);
        tt.setComment("Last touched by makeNextToken() function.");
        */
        
        //try another approach:
        //when you createa new TokenTable() is the auto incriment id automatically set? Lets find out.
        TokenTable tt = new TokenTable();
        //String tokenCode = encryptIndex( tt.getId() );
        String tokenCode = encryptIndex( getMaxTokenIndex() + 1 );
        tt.setToken( tokenCode );
        tt.setComment("Query will fail if done on empty table. Fix this.");
        
        //return the populated token:
        return tt;
        
    }//FUNC::END
    
    /** Take an index and encrypt it. ENCRYPT. Not hash.
     *  The purpose of the token is to give the candidate an obfuscated
     *  key. Encryption of a primary key number will ~gaurantee~ we
     *  will never have colliding token values.
     * @param tokenIndex : The primary that:
     *                     1. represents a unique token.
     *                     2. we want to encrypt into a form
     *                        That the end-user will see.
     * @return : An encrypted version of the supplied token index.
     *           This value is what we conceptualize as our "token" **/
    private static String encryptIndex(long tokenIndex){
        
        //placeholder code for now. Also will help with debugging
        //since we will notice if encryption does not line up
        //with the primary key value.
        String op = "ENCRYPTED_TOKEN_" + Long.toString(tokenIndex);
        return op;
    }//FUNC::END
     
    /** Gets an entity representing a [record/entry] in the token table.     ***
    *** Using the token's HASH value to fetch the [record/entry].            ***
    *** @param tv : The token's value.                                       ***
    *** @return   : Returns container with found entity.                     *** 
    ***             If no match found, container is configured               ***
    ***             to reflect that. This helps us avoid returning NULL.     **/
    public static BaseEntityContainer getTokenEntityUsingTokenString(String tv){
        
        //Enter Transaction:
        //Session session = TransUtil.enterTransaction();
        
        //Test to see if we are currently in a transaction state.
        //If we are not, code will crash.
        Session session = TransUtil.getActiveTransactionSession();
        
        //Transaction Logic:
        Criteria criteria = session.createCriteria(TokenTable.class);
        criteria.add(Restrictions.eq(TokenTable.COLUMN_TOKEN, tv));

        TokenTable theToken = (TokenTable) criteria.uniqueResult();

        //DELETE THESE COMMENTS DURING CLEAN UP:
        //OLD EXPLANATION: 
        //Rather than handling exiting of transaction here,
        //we will simply build an array of entities that need to be
        //saved. When all of the transactions from different utilities
        //have finished:the entity saver queue within our main
        //1: we make a call to transaction utility to exit transaction.
        //2: Any entities that have been edited and put in our queue will
        //   be saved&commited.
        
        //DELETE THESE COMMENTS DURING CLEAN UP:
        //UPDATE: LOGIC ERROR:
        //This is wrong. Getting a token should NOT mean that we have to save
        //it. If the calling function wants to mutate the returned token and
        //save it, it is that calling function'schoice to do that:
        //XXXX if (theToken!=null) {
        //XXXX    TransUtil.markEntityForSaveOnExit(theToken);
        //XXXX }
        
        //Create output:
        BaseEntityContainer op = new BaseEntityContainer();
        if(null == theToken)
        {   //make container configured as empty:
            op.entity = null;
            op.exists = false;
        }
        else
        {   //make container configured as populated:
            op.entity = theToken;
            op.exists = true;
        }
        
        //return the container with our result of query:
        return op;
        
    }//END::FUNC
    
    public static void getAllTokens(){
        throw new MyError("TODO: getAllTokens function.");
    }
    
}//CLASS::END
