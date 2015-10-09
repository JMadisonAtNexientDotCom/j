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
    
}//CLASS::END
