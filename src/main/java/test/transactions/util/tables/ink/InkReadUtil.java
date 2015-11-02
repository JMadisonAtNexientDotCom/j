package test.transactions.util.tables.ink;

import test.MyError;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import test.dbDataAbstractions.entities.tables.RhymeTable;
import test.dbDataAbstractions.entities.tables.riddleTrialStore.questionStore.InkPurse;
import test.dbDataAbstractions.entities.tables.riddleTrialStore.questionStore.InkTable;
import test.transactions.util.TransUtil;
import test.transactions.util.tables.rhyme.RhymeTransUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jmadison :2015.11.01(November,1st,Year2015.Sunday)
 */
public class InkReadUtil {
    
    /**
     * Uses the InkTable primary key to find
     * @param ink_id :The primary key of the InkTable
     * @return :A list of Quips representing the [Rhymes/Answers]
     *          on a single cuecard.
     */
    public static List<RhymeTable> readQuipsUsingInkTablePrimaryKeyID
                                                                  (long ink_id){
        if(ink_id <= 0){doError("[invalid ink_id supplied. Lazy fetch?]");}
        
        //get the actual group id:
        InkTable itab = InkTransUtil.getInkTableUsingPrimaryKeyID(ink_id);
        if(null == itab){doError("[itab is null]");}
        
        return readQuipsUsingInkTable(itab);
        
    }//FUNC::END
                   
    /**
     * Use a populated InkTable instance to retrieve the list of RhymeTable
     * objects it is grouping.
     * @param itab :The InkTable [Entity/Record]
     * @return :A list of RymeTable, known as "Quips" in the context of
     *          being on a cuecard.
     */
    public static List<RhymeTable> readQuipsUsingInkTable(InkTable itab){
        if(itab.ink_gi <= 0){doError("[invalid group id. Lazy fetch?]");}
        
        //Fetch all of the entities that have been clustered into the ink purse:
        //List<BaseEntity> bel = TransUtil.getEntitiesUsingLong
        //                (InkPurse.class, InkPurse.GROUP_ID_COLUMN, itab.ink_gi);
        
       List<InkPurse> lip =  TransUtil.getPursesUsingGroupID
                                                  (InkPurse.class, itab.ink_gi);
        
       //go through each InkPurse, and use it to find the correct RhymeTable.
       //Put them into a list, and return the list.
       InkPurse cur_purse;
       long rhyme_id;
       RhymeTable cur_rhyme;
       int num_riddles = lip.size();
       if(num_riddles <= 0){doError("Group is somehow empty");}
       List<RhymeTable> op = new ArrayList<RhymeTable>();
       for(int i = 0; i < num_riddles; i++){
           cur_purse = lip.get(i);
           if(cur_purse.group_id != itab.ink_gi){
               doError("[Data mismatch! GroupID does not match parent.]");
           }//
           rhyme_id = cur_purse.rhyme_id;
           cur_rhyme = RhymeTransUtil.getRhymeTableByID(rhyme_id);
           
           //add the rhyme found:
           op.add(i, cur_rhyme);
           
       }//Get next rhyme.
       
       //return the list!
       if(op.size() <= 0){doError("[List should not be empty!]");}
       return op;
       
    }//FUNC::END
                     
    
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = InkReadUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
                                                                  
}//CLASS::END
