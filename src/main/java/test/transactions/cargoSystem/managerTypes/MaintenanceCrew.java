package test.transactions.cargoSystem.managerTypes;

import java.util.List;
import test.MyError;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import test.transactions.cargoSystem.dataTypes.EntityCage;
import test.transactions.cargoSystem.dataTypes.GalleonBarge;
import test.transactions.cargoSystem.dataTypes.JobTicket;
import test.transactions.cargoSystem.dataTypes.JobTicketTypes;
import test.transactions.cargoSystem.dataTypes.OrderSlip;
import test.transactions.cargoSystem.dataTypes.jobConsts.JoinOrderVars;
import test.transactions.cargoSystem.dataTypes.jobConsts.LinkOrderVars;
import test.transactions.cargoSystem.dataTypes.jobConsts.WeldJobVars;
import test.transactions.util.TransUtil;

/**
 * The MaintenanceCrew fufills the requests on the job tickets present on the
 * bulliten board. The maintance crew does all of their work AFTER the ship
 * has fufilled all of it's orders.
 * 
 * DESIGN NOTE: Personification of organization:
 *              Objects      == ANIMATE/LIVING things.
 *              DATA-STRUCTS == IN-ANIMATE THINGS.
 * 
 *              Objects HIDE DATA but can perform functions 
 *              (People have secrets and things they keep to themselves)
 *              
 *              For our cargoSystem analogy,
 *              The GalleonBarge(Cargo Ship) represents a hybrid-object.
 *              Because it performs functions and gathers data. And
 *              OBJECTS/PEOPLE like the captain + crew can live on the ship.
 * 
 * @author jmadison :2015.10.16 (OCT16TH,Year2015) Clean code says you shouldn't
 *                              use author tags because they become out of date.
 *                              In 5 years, don't message me about this code.
 *                              Otherwise: (586)214-3958, If I am alive, this
 *                              will be my phone number.
 */
public class MaintenanceCrew {
    
    /**The boat the maintenance crew belongs to. **/
    public GalleonBarge barge;
    
    /** Will go through all of the job tickets on the barge
     *  and complete them. **/
    public void work(){
        
        
        for(JobTicket j : barge.bulletin.jobs){
            
            //TODO: Factor in dependencies. No support now. 
            //Just throw errors if they exist.
            if(null  == j.blockers){doError("[Can be empty, but not null.]");}
            if(false == j.blockers.isEmpty()){
                doError("[TODO: Code support for dependencies]");
            }//Error.
            
            doJob(j);
            
        }//NEXT j
    }//FUNC::END
    
    /**
     * PRIVATE because jobs may have dependencies. You don't tell the crew
     * WHEN to do their jobs, you just tell them what you want done.
     * AKA: The crew works on a commission basis and is NOT micro-managed.
     * Management trusts that the crew will do their work.
     * @param j :The job ticket to complete.
     */
    private void doJob(JobTicket j){
        String job = j.jobType;
        if(job.equals(JobTicketTypes.WELD_JOB)){
            doWeldJob(j);
        }else
        if(job.equals(JobTicketTypes.JOIN_ORDER)){
            doJoinOrder(j);
        }else
        if(job.equals(JobTicketTypes.LINK_ORDER)){
            doLinkOrder(j);
        }else
        if(job.equals(JobTicketTypes.NOT_SET)){
            doError("[Job ticket was not filled out with job type]");
        }else{
            doError("[Unknown job type constant]");
        }//
    }//FUNC::END
    
    private void doJoinOrder(JobTicket j){
        
        //Extract arguments:
        OrderSlip from   = j.specs.getVal(JoinOrderVars.FROM_ORDER, 
                                          JoinOrderVars.FROM_ORDER_TYPE);
        OrderSlip into   = j.specs.getVal(JoinOrderVars.INTO_ORDER, 
                                          JoinOrderVars.INTO_ORDER_TYPE);
        String  column   = j.specs.getVal(JoinOrderVars.DEST_COLUMN, 
                                          JoinOrderVars.DEST_COLUMN_TYPE);
        
        //Retrieve cages using orders:
        EntityCage cage_from = barge.hold.getCageUsingReceipt(from);
        EntityCage cage_into = barge.hold.getCageUsingReceipt(into);
        
        //Get entity lists from cages:
        List<BaseEntity> records_from = cage_from.getMerchandise();
        List<BaseEntity> records_into = cage_into.getMerchandise();
        
        //Perform Join:
        TransUtil.join(records_from, records_into, column);
        
    }//FUNC::END
    
    /**
     * Like a join order, but can take from an arbitrary column
     * rather than only from the primary id column.
     * @param j 
     */
    private void doLinkOrder(JobTicket j){
        
        //Extract arguments:
        OrderSlip fromVar= j.specs.getVal(LinkOrderVars.FROM_ORDER, 
                                          LinkOrderVars.FROM_ORDER_TYPE);
        OrderSlip intoVar= j.specs.getVal(LinkOrderVars.INTO_ORDER, 
                                          LinkOrderVars.INTO_ORDER_TYPE);
        String  take     = j.specs.getVal(LinkOrderVars.TAKE_COLUMN, 
                                          LinkOrderVars.TAKE_COLUMN_TYPE);
        String  dest     = j.specs.getVal(LinkOrderVars.DEST_COLUMN, 
                                          LinkOrderVars.DEST_COLUMN_TYPE);
        
        //Retrieve cages using orders:
        EntityCage cage_from = barge.hold.getCageUsingReceipt(fromVar);
        EntityCage cage_into = barge.hold.getCageUsingReceipt(intoVar);
        
        //Get entity lists from cages:
        List<BaseEntity> from = cage_from.getMerchandise();
        List<BaseEntity> into = cage_into.getMerchandise();
        
        //Perform Link:
        TransUtil.link(from, take, into, dest);
        
    }//FUNC::END
    
    /** A weld job is basically a join-table operation. **/
    private void doWeldJob(JobTicket j){
        
        //Get params for weld job:
        List<BaseEntity> from = j.specs.getList(WeldJobVars.FROM_TABLE, 
                                                WeldJobVars.FROM_TABLE_TYPE);
        List<BaseEntity> into = j.specs.getList(WeldJobVars.INTO_TABLE, 
                                                WeldJobVars.INTO_TABLE_TYPE);
        String column         = j.specs.getVal (WeldJobVars.DEST_COLUMN, 
                                                WeldJobVars.DEST_COLUMN_TYPE);
        
        //Use transaction utility to perform the join:
        TransUtil.join(from, into, column);
        
    }//FUNC::END
    
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = MaintenanceCrew.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
}//CLASS::END
