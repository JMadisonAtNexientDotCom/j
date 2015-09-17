package test.transactions.util.forCompositeEntities;

import java.util.ArrayList;
import java.util.List;
import test.MyError;
import test.dbDataAbstractions.entities.tables.RhymeTable;
import test.dbDataAbstractions.fracturedTypes.clientServerConversation.lectern.Slate;
import test.transactions.util.TransUtil;
import test.transactions.util.riddleRhyme.RiddleRhymeTransUtil;
import test.transactions.util.riddleRhyme.rhymeRiddle.riddle.RiddleTransUtil;

/**
 * A transaction utility that is responsible for managing transactions involving
 * our Slate.java objects. A slate is basically an "answer card" that the
 * ninja fills out for a single [riddle/question] that is asked by the Jester.
 * @author jmadison                                                          **/
public class SlateTransUtil {
    
    /** Creates a filled out slate (answer card) for a given riddleID.
     *  How slate is filled out depends on what constant is given to 
     *  slateDebugType.
     * @param riddleID :The riddle (question) the slate is addressing.
     * @param slateDebugType :How the [riddle/question] will be addressed.
     * Possible configurations are to: Answer 100% CORRECT or 100% WRONG.
     * Slate.SLATE_DEBUG_TYPE_TRUTH & SLATE_DEBUG_TYPE_WRONG
     * for those configurations respectively.
     * @return : A slate that has been filled out and is ready for debugging.**/
    public static Slate makeFilledOutTestSlate_COMMON
                                         (long riddleID, String slateDebugType){
    
        //make sure we are in transaction state:                                       
        TransUtil.insideTransactionCheck();

        //Route to correct configuration:
        Slate op;
        if(Slate.SLATE_DEBUG_TYPE_TRUTH.equals(slateDebugType) ){
            op = makeFilledOutTestSlate_TRUTH(riddleID);
        }else
        if(Slate.SLATE_DEBUG_TYPE_WRONG.equals(slateDebugType)){
            op = makeFilledOutTestSlate_WRONG(riddleID);
        }else{//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            op = null;
            String msg = "ERROR: Possibilities below:";
            msg+="[1:slateDebugType is UNKNOWN/INVALID]";
            msg+="[2:logic of code block is making the WRONG COMPARISONS]";
            msg+="[3:forgot to support a new slateDebugType added to system.]";
            throw new MyError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
           
        //return the results!
        return op;
        
    }//FUNC::END
                            
    /** Fills out a slate with WRONG answers.
     * @param riddleID        :see makeFilledOutTestSlate_COMMON for details.
     * @return                :see makeFilledOutTestSlate_COMMON for details.**/
    public static Slate makeFilledOutTestSlate_WRONG(long riddleID){
    
        //make sure we are in transaction state:                                       
        TransUtil.insideTransactionCheck();
        
        //LOGIC:
        long validRiddleID = RiddleTransUtil.getRiddleByID_or_Random
                                                             (riddleID).getId();
        Slate s = Slate.makeBlankSlate(validRiddleID);
        List<RhymeTable> rtList = RiddleRhymeTransUtil.getRhymesThatAre_WRONG
                                                             (validRiddleID, 4);
        s.rhymeSelection = CollectIDSFromListOfRhymeTableEntities(rtList);
        
        //Return slate populated with WRONG answers.
        return s;
        
    }//FUNC::END
             
    /** Fills out a slate with CORRECT answers.
     * @param riddleID        :see makeFilledOutTestSlate_COMMON for details.
     * @return                :see makeFilledOutTestSlate_COMMON for details.**/
    public static Slate makeFilledOutTestSlate_TRUTH (long riddleID){
    
       //make sure we are in transaction state:                                       
        TransUtil.insideTransactionCheck();
        
        //LOGIC:
        long validRiddleID = RiddleTransUtil.getRiddleByID_or_Random
                                                             (riddleID).getId();
        Slate s = Slate.makeBlankSlate(validRiddleID);
        List<RhymeTable> rtList = RiddleRhymeTransUtil.getRhymesThatAre_TRUTH
                                                             (validRiddleID, 4);
        
        //ADDED:2015.09.16_0921PM
        //Fake the original quips. Say 1 question was true, 3 were false:
        List<RhymeTable> orgQuips = 
                     RiddleRhymeTransUtil.getRhymesTrueFalse(validRiddleID,1,3);
        
        s.rhymeSelection = CollectIDSFromListOfRhymeTableEntities(rtList);
        s.originalQuips  = CollectIDSFromListOfRhymeTableEntities(orgQuips);
        
        //Return slate populated with WRONG answers.
        return s;    
        
    }//FUNC::END
    
    /**
     * Converts List of RhymeTable entities into a list of IDs.
     * @param rtList: The list of entities you want to strip the IDs out of.
     * @return :An analogous list containing the ID values of entities.      **/
    private static List<Long> CollectIDSFromListOfRhymeTableEntities
                                                      (List<RhymeTable> rtList){
        //Iterate through all, and strip out id's:
        List<Long> op = new ArrayList<Long>();
        int len = rtList.size();
        for(int i = 0; i < len; i++){
            op.add( rtList.get(i).getId() );
        }//NEXT i
        
        //Return the output:
        return op;
    }//FUNC::END
                                         
                                         
}//FUNC::END
