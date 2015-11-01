package app.transactions.cargoSystem.dataTypes.jobConsts;

import app.dbDataAbstractions.entities.bases.BaseEntity;

/**
 * Stores argument variables for completing a weld-job:
 * @author jmadison :2015.10.16(October16th,Year2015)
 */
public class WeldJobVars {
    
    //The pattern to follow, as self-documenting code:
    //Argument identifier is always a string.
    //Argument type is a class, and has same name as identifier,
    //but ends with "_TYPE"
    public static final String EXAMPLE_INTEGER_ARGUMENT = "example_integer_argument";
    public static final Class  EXAMPLE_INTEGER_ARGUMENT_TYPE= Integer.class;
    
   //DELETE THIS COMMENTED OUT BLOCK:
   //List of all the IDS/primary key values from from-table to use://
   // public static final String KEYS      = "keys";
   // public static final Class  KEYS_TYPE = Long.class; //boxed longs.
    
    //NOTE: Not able to make TYPE == List<BaseEntity>.class...
    //      So make sure type checking has provisions for knowing data
    //      might be inside a list.
    
    /** The table we want to grab the primary key from. **/
    public static final String FROM_TABLE      = "from_table";
    public static final Class  FROM_TABLE_TYPE = BaseEntity.class;
    
    /** The table we want to INSERT the primary key into, as a foreign key **/
    public static final String INTO_TABLE      = "into_table";
    public static final Class  INTO_TABLE_TYPE = BaseEntity.class;
    
    /** The destination column name. Since a table can have multiple foreign
     *  keys. We must specify this. We do NOT specify a FROM_COLUMN,
     *  because it should always be the PRIMARY KEY column, and primary key
     *  column should always be named "id" in our database. **/
    public static final String DEST_COLUMN      = "dest_column";
    public static final Class  DEST_COLUMN_TYPE = String.class;
    
}//CLASS::END
