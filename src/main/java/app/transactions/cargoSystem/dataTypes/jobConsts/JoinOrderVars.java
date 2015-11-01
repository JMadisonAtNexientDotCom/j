package app.transactions.cargoSystem.dataTypes.jobConsts;

import app.dbDataAbstractions.entities.bases.BaseEntity;
import app.transactions.cargoSystem.dataTypes.OrderSlip;

/**
 * Performs join-table operation using ORDER objects as arguments.
 * 
 * Like a weld job, but takes entity cages as to-from arguments:
 * @author jmadison
 */
public class JoinOrderVars {

    //The pattern to follow, as self-documenting code:
    //Argument identifier is always a string.
    //Argument type is a class, and has same name as identifier,
    //but ends with "_TYPE"
    public static final String EXAMPLE_INTEGER_ARGUMENT = "example_integer_argument";
    public static final Class  EXAMPLE_INTEGER_ARGUMENT_TYPE= Integer.class;
    

    /** The table we want to grab the primary key from. **/
    public static final String FROM_ORDER     = "from_order";
    public static final Class  FROM_ORDER_TYPE = OrderSlip.class;
    
    /** The table we want to INSERT the primary key into, as a foreign key **/
    public static final String INTO_ORDER      = "into_order";
    public static final Class  INTO_ORDER_TYPE = OrderSlip.class;
    
    /** The destination column name. Since a table can have multiple foreign
     *  keys. We must specify this. We do NOT specify a FROM_COLUMN,
     *  because it should always be the PRIMARY KEY column, and primary key
     *  column should always be named "id" in our database. **/
    public static final String DEST_COLUMN      = "dest_column";
    public static final Class  DEST_COLUMN_TYPE = String.class;
   
}//CLASS::END
