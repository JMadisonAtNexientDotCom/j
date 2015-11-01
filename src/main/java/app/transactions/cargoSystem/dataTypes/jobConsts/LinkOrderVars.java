package app.transactions.cargoSystem.dataTypes.jobConsts;

import app.transactions.cargoSystem.dataTypes.OrderSlip;

/**
 * Identical to JOIN_ORDER, except can TAKE from a column
 * other than the primary id column.
 * @author jmadison :2015.10.29(Oct29th,year2015)
 */
public class LinkOrderVars {
    
    //THESE ARE THE TWO VARIABLES THAT DIFFERENTIATE LINK ORDER FROM JOIN ORDER:
    public static final String TAKE_COLUMN      = "take_column";
    public static final Class  TAKE_COLUMN_TYPE = String.class;
    
     /** The table we want to grab the primary key from. **/
    public static final String FROM_ORDER      = JoinOrderVars.FROM_ORDER;
    public static final Class  FROM_ORDER_TYPE = JoinOrderVars.FROM_ORDER_TYPE;
    
    /** The table we want to INSERT the primary key into, as a foreign key **/
    public static final String INTO_ORDER      = JoinOrderVars.INTO_ORDER;
    public static final Class  INTO_ORDER_TYPE = JoinOrderVars.INTO_ORDER_TYPE;
    
    /** The destination column name. **/
    public static final String DEST_COLUMN      = JoinOrderVars.DEST_COLUMN;
    public static final Class  DEST_COLUMN_TYPE = JoinOrderVars.DEST_COLUMN_TYPE;
    
}//CLASS::END
