package test.transactions.util.tables.ink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import primitives.LongBool;
import test.MyError;
import test.config.debug.DebugConfig;
import test.dbDataAbstractions.entities.EntityUtil;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import test.dbDataAbstractions.entities.tables.GroupTable;
import test.dbDataAbstractions.entities.tables.RhymeTable;
import test.dbDataAbstractions.entities.tables.riddleTrialStore.questionStore.InkPurse;
import test.transactions.util.PersistUtil;
import test.transactions.util.TransUtil;
import test.transactions.util.tables.group.GroupTransUtil;

/**
 * Utility responsible for persisting the INK on the CuecardTable entities.
 * The INK represents the answer-choices (quips/rhymes) that have been proposed
 * as possible answers to the question on the title of a given cuecard.
 * @author jmadison :2015.10.19
 */
public class InkPersistUtil {
    
    /**
     * Makes sure group of rhymes is persisted into database.
     * @param quips :The rhyme options on the cuecard.
     * @return :Returns the GROUP_ID used to identify this group of quips.
     *          If the set of quips was UNIQUE. The data is persisted
     *          and the boolean component is set to TRUE to let us know.
     */
    public static LongBool persistQuips(List<RhymeTable> quips){
        
        //Make sure all entries in input are UNIQUE, if they are not,
        //that will screw up our alogrithm. Also, while we are at it,
        //make sure none of the IDS are <= 0.
        if(DebugConfig.isDebugBuild){
            HashMap<Long,Boolean> debugCheck = new HashMap<Long,Boolean>();
            long curID;
            RhymeTable curDebugEnt;
            int lenOfInputList = quips.size();
            for(int debugDex = 0; debugDex < lenOfInputList; debugDex++){
                curDebugEnt = quips.get(debugDex);
                if(null == curDebugEnt){doError("[NullEntry,NotAllowed]");}
                curID = curDebugEnt.getId();
                if(curID <= 0){doError("[Bad ID FOUND! Group Persist func]");}
                if( debugCheck.containsKey(curID) ){
                    doError("[Duplicate IDS found! Algorithm will not work!]");
                }//
                debugCheck.put(curID, true);
            }//Next debug entity.      
        }//DEBUG::END
        
        long rhyme_id;
        InkPurse curInk;
        List<BaseEntity> pileOfMaybes = new ArrayList<BaseEntity>();
        
        /** These inkpurse objects have the SAME CHECKSUM as the total
         *  number of RhymeTable(s) in the list of entities that was
         *  supplied to this function. If all members of the group are
         *  PRESENT/FOUND, then we have data that is already persisted. **/
        List<InkPurse> checkSumCandidates = new ArrayList<InkPurse>();
        long currentCheckSum;
        
        //HashMap< GROUP_ID, Number Entities present >
        HashMap<Long,Long> groupRollCall = new HashMap<Long,Long>();
        
        int numQuips = quips.size();
        for(int i = 0; i < numQuips; i++){
            rhyme_id = quips.get(i).getId();
            List<BaseEntity> ents;
            ents = TransUtil.getEntitiesUsingLong
                           (InkPurse.class, InkPurse.RHYME_ID_COLUMN, rhyme_id);
            for(BaseEntity genericEnt : ents){
                if(null == genericEnt){
                    doError("[Why is transUtil returning null entry??]");
                }//
                
                curInk = (InkPurse)genericEnt;
                
                if(null == curInk){doError("[cast resulted in null object]");}
                
                currentCheckSum = GroupTable.getChecksumOfID(curInk.group_id);
                if(currentCheckSum == numQuips){
                    checkSumCandidates.add(curInk);
                    if(groupRollCall.containsKey(curInk.group_id)){
                        //incriment number of times found:
                        Long val = groupRollCall.get(curInk.group_id);
                        val++;
                        groupRollCall.put(curInk.group_id, val);
                    }else{
                        //Put in a 1, since first item of group_id found:
                        groupRollCall.put(curInk.group_id, new Long(1) );
                    }//
                    
                }//check sum checks out.
            }//next collected entity with COLUMN value we are looking for.
        }//Next i.
        
        //Go through the group roll call, any entry who's value in the
        //hashmap is now EXACTLY the checksum is a possible match.
        List<Long> possibleMatchingGroupIDs = new ArrayList<Long>();
        for (Map.Entry<Long, Long> entry : groupRollCall.entrySet()) {
            Long pmVal = entry.getValue();
            if(pmVal == numQuips){
                Long pmKey = entry.getKey();
                possibleMatchingGroupIDs.add(pmKey); //<--key is group_id.
            }else
            if(pmVal > numQuips){
                //Hopefully the error is in this function. Otherwise,
                //this is a pretty serious error. Means when you created a
                //group in the database, you gave it the WRONG checksum.
                //Example: 5 records with the same group id. But the group
                //id's checksum !=5 .
                doError("[The Stored checksum was found to be invalid!]");
            }//add?
        }//next entry.
        
        //Summary of what has been done so far:
        //We collected all objects with matching column values.
        //We then siphoned those down to groupIDs where all the entities
        //of that group have been collected.
        //
        //Results Expected:
        //1: If object already exists in database: 
        //   possibleMatchingGroupIDs should have EXACTLY ONE ENTRY.
        //2: If object does not already exist in database:
        //   possibleMatchingGroupIDs should have ZERO entries.
        //
        //How can I be sure of item #1?
        //If all the column values match, and all members of the group are
        //present, and the group's checksum is the size of the inputted list
        //of entities... That sounds like a match to me.
        LongBool op = null;
        int lenOfMatches = possibleMatchingGroupIDs.size();
        if(lenOfMatches==0){
            //CREATE NEW ENTRY.
            long newGroupID = GroupTransUtil.makeNewGroup
                                               ("[FromAPersistUtil]", numQuips);
            op = new LongBool();
            op.b = PersistUtil.NEW_ENTITY_MADE; //<--true for NEWLY CREATED.
            op.l = newGroupID;
            
            //We are NOT going to link to this entry from the outer container,
            //that is the job of whatever is calling this.
            //AKA: We are not going to link this PURSE entities groupID
            //     to it's corrosponding TABLE entity.
            
            //We do however, need to create all of the entries into
            //the table!
            
            List<Long> rhymeIDs = EntityUtil.StripPrimaryIDS(quips);
            TransUtil.makeGroup
               (InkPurse.class, newGroupID, InkPurse.RHYME_ID_COLUMN, rhymeIDs);
            
        }else
        if(lenOfMatches==1){
            //No new entity made:
            op = new LongBool();
            op.b = PersistUtil.NO_ENT_MADE_ALREADY_EXISTS;
            op.l = possibleMatchingGroupIDs.get(0);
        }else{
            String msg = "";
            msg += "[Problem with your algorithm. Multiple matches.]";
            msg += "[Either that, or you have duplicate groups in database.]";
            msg += "[Which is also a problem. As that is not allowed either.]";
            doError(msg);
        }//
        
        if(null == op){doError("[OpShouldNeverBeNull.234254300093]");}
        return op;
    }//FUNC::END

    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = InkPersistUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
         
}//CLASS::END
