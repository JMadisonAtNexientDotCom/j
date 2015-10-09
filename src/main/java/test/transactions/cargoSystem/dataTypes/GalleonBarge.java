package test.transactions.cargoSystem.dataTypes;

/**
 * What is the GalleonBarge:
 * It is the ship that makes the trips to collect the data(entities)
 * specified by the OrderSlips on the AgendaClipBoard.
 * 
 * Explanation of design choices:
 * Galleon:  A large, squarish ship used in war or to carry cargo.
 * Barge  :  A flat-bottomed boat for carrying freight, 
 *           typically on canals and rivers, either under 
 *           its own power or towed by another.
 * 
 * "GalleonBarge" is REDUNDANT. However, it allows us to refer
 *  to instances of it simply as "barge". And I find when it comes to
 *  reducing length of code lines, if you have to choose between simplified
 *  variable names or simplified class names, simplified variable names
 *  seems like the way to go. Not to mention class names are more likely
 *  to be read out of context if someone is just skimming the file system
 *  and looking at the name of the .java files.
 * 
 * OLD NOTES: PRE:2015.10.08_1019AM //OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
 * REASON THESE OLD NOTES ARE INVALID:
 *    Decided I want the conciseness in the instances more than I want
 *    the conciseness in the class names
 * OLD NOTES::BODY::START
 * The Barge is our cargo ship. I used the word "Barge" rather than
 * "Cargo Ship" because Barge is shorter and more concise. The more concise
 * I make the problem domain, the easier the code will be to read.
 * OLD NOTES::BODY::END
 * //OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
 * 
 * @author jmadison :2015.10.08_1003AM  (Year2015,October the eigth)
 */
public class GalleonBarge {
    
    //Different states the barge can be in:
    ///////////////////////////////////////////////////////
    public static int STATUS_INIT_ERROR                = 0;
    public static int STATUS_EMPTY_SHIP_READY          = 1;
    public static int STATUS_CONFIGURING_AGENDA        = 2;
    public static int STATUS_EXECUTING_AGENDA          = 4;
    public static int STATUS_TRIP_COMPLETE_CARGO_READY = 5;
    ///////////////////////////////////////////////////////
    
    //Constructor locks to prevent un-authorized access to the constructor.
    //private static boolean _constructorLock_makeNullInstance = true;
    //private static boolean _constructorLock_make = true;
  
    /** Holds all of the entities that have been collected from requests.
     *  Called cargo hold because we think of entities as exotic dinosaurs
     *  from jurassic park that are bing packed into cages and put 
     *  into the cargo hold. Dinosaurs are valuable merchandise. **/
    public CargoHold hold;
    
    /** The agenda that contains all of the orders that will be used
     *  to fill up the CargoHold. **/
    public AgendaClipBoard agenda;
    
    /** The status of the barge. **/
    public int status;
    
    private GalleonBarge(){
        //Do nothing. Just here to force the default constructor
        //to be PRIVATE and inaccessible.
        //use the .make functions on the class instead.
    }//CONSTRUCTOR
    
    /**
     * Creates a new partially configured order,
     * adds it to the current orders, and then returns
     * that order so you can further configure it.
     * @param portID :The portID that will be used to supply what we want.
     * @return       :The order, ready to be further configured.
     */
    public OrderSlip placeOrder(short portID){
       OrderSlip order = OrderSlip.makeUsingPortID(portID);
       //agenda.orders.add(order); //<--might want to use getter so we can prevent
      // return order;                //adding the same order TWICE.
       
       agenda.addOrder(order);
       return order;
       
    }//FUNC::END
    
    /** By null instance. I mean an instance where all the properties
     *  of this object are null references. You would use something like
     *  this when you want to do some manual configuration.
     * 
     *  SYNCHRONIZED TO PROTECT THE CONSTRUCTOR LOCK!
     * 
     * @return :A GalleonBarge that contains all null references.
     */
    synchronized public static GalleonBarge makeNullInstance(){
        //_constructorLock_makeNullInstance = false;
        // GalleonBarge op = new GalleonBarge(makerID);
        //_constructorLock_makeNullInstance = true;
        
        //If I can make constructors private, won't need any fancy lockers.
        GalleonBarge op = new GalleonBarge();
        return op;
    }//FUNC::END
    
    /** Make a GalleonBarge that does NOT have null references.
     *  And is ready to be populated with data.
     * @return : See above. **/
    synchronized public static GalleonBarge make(){
        //_constructorLock_make = false;
        //GalleonBarge op = new GalleonBarge(makerID);
        //_constructorLock_make = true;
        
        //If I can make constructors private, won't need any fancy lockers.
        GalleonBarge op = new GalleonBarge();
        op.hold   = CargoHold.make();
        op.agenda = AgendaClipBoard.make();
        return op;
    }//FUNC::END
    
}//CLASS::END
