package test.dbDataAbstractions.entities.entityHelpers;

/**
 * Used as helper for OwnerTokenTransUtil.java so we can see who owns token.
 * @author jmadison
 */
public class WhoOwnsToken {
    //NOTE: Not including one for BOTH own or no one owns. 
    //Because those are errors.
    
    /** Means ninja user owns this token. **/
    public static int NINJA_OWNS     = 1;
    
    /** Means admin user owns this token. **/
    public static int ADMIN_OWNS     = 2;
    
    /** Not found in token_table, so should not be in owner table either. **/
    public static int DOES_NOT_EXIST_IN_TOKEN_TABLE =3;
    
    /** Implication: Exists in token table, but not in the owner table. **/
    public static int DOES_NOT_EXIST_IN_OWNER_TABLE =4;
    
    /** Zero means error. Should never return zero.
     *  Using zero because ints are always initialized to zero. **/
    public static int ERROR_ENUM     = 0;
    
    /** value set to enum that tells us who owns the token. **/
    ///public int value = 0;
    
}//CLASS::END
