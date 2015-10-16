package test.transactions.cargoSystem.dataTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * DESIGN NOTE: A DATA-STRUCT with methods for convenience+validation.
 *              
 * 
 * This is a cork board that contains JobTicket(s) that have been
 * thumb-tacked/push-pinned into the board. It is all of the work that
 * needs to be done after the cargo-ship has gathered everything it needs.
 * 
 * Imagine that the board also contains string. And that string+tacks are
 * linking together some of the jobs. Some jobs are dependent on other jobs
 * being done first. 
 * 
 * @author jmadison :2015.10.16(OCT16th,Year2015)
 */
public class JobBulletin {
    
    /** All of the jobs that need to be done on the ship. **/
    public List<JobTicket> jobs;
    
    /** Helper function to make adding to jobs easier. **/
    public void addJob(JobTicket job){
        jobs.add(job);
    }//FUNC::END
    
    /**
     * Adds an empty job ticket to the list of jobs to do,
     * and returns that job ticket so it can be further configured.
     * 
     * DESIGN NOTE:
     * Was trying to decide if function should be called
     * "addEmptyJobTicket" or "makeEmptyJobTicket" decided on
     * "add" rather than "make" because from perspective of reading code,
     * it will be more obvious that this function makes an entry into the
     * jobs list if using word "add". The RETURN value is also obvious when
     * you assign the call to a variable.
     */
    public JobTicket addEmptyJobTicket(){
        JobTicket j = new JobTicket();
        jobs.add(j);
        return j;
    }//FUNC::END
    
    public static JobBulletin make(){
        JobBulletin op = new JobBulletin();
        op.jobs = new ArrayList<JobTicket>();
        return op;
    }//FUNC::END
    
}//CLASS::END
