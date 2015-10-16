package test.transactions.cargoSystem.dataTypes;

/**
 * Enumeration class holding the types of jobs that can be done.
 * @author jmadison
 */
public class JobTicketTypes {
    //DESIGN NOTE:
    //Decided to use STRING constants rather than ENUM constants because:
    //1: These values WONT be ending up in database.
    //2: Strings are easier to debug because can be read.
    //3: Integer Enums == Pointless optimization for bulky enterprise software.
    
    /** Zero means we forgot to initialize. **/
    public static final String NOT_SET = "not_set";
    
    /** A join table operation is requested. 
     *  Visualized as a welder welding together different entitie's cages **/
    public static final String WELD_JOB   = "weld_job";
    
    /**
     * Does join-table operation, using two orders as inputs rather
     * than the table names. Is an abstract wrapper for standard join
     * table operation WELD_JOB.
     */
    public static final String JOIN_ORDER = "join_order";
}//CLASS::END
