package test.transactions.cargoSystem.dataTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * DESIGN: Primarily a DATA STRUCT, not object. Any non-static methods on this
 *         class should primarily be for convinience.
 *         Maybe have a "Maintenance Crew" that looks at job tickets and
 *         carries them out?
 *         
 * 
 * Represents a "job" that needs to be done on the ship AFTER all of the
 * orders have been completed and the ship is full of cargo.
 * 
 * Original Job: A weld job. AKA: Join table commands.
 * Signature for command: weld(fromTable,toTable,toColumn)
 * Takes primary key out of fromTable, and inserts it into toTable under
 * the needed column name.
 * 
 * @author jmadison :2015.10.16 (OCT16TH,Year2015) Clean code says you shouldn't
 *                              use author tags because they become out of date.
 *                              In 5 years, don't message me about this code.
 *                              Otherwise: (586)214-3958, If I am alive, this
 *                              will be my phone number.
 */
public class JobTicket {
    
    /** The type of job to do. **/
    public String jobType = JobTicketTypes.NOT_SET;
    
    /** "blockers" taken from scrum. These are other jobs that must be
     *  done FIRST before this job can be done. It is a dependency list. **/
    public List<JobTicket> blockers = new ArrayList<JobTicket>();
    
    /** The arguments used to fufill this job ticket.
     *  Should never be null. Empty is okay. **/
    public SpecialInstructionsStickyNote specs = 
                                            new SpecialInstructionsStickyNote();
    
}//CLASS::JobTicket
