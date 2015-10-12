package test.transactions.cargoSystem.dataTypes;

import java.util.ArrayList;
import java.util.List;
import test.MyError;
import test.config.debug.DebugConfig;

/**
 * To be referred shorthand as "SPECS". Specs are special instructions.
 * Or rather, arguments. That are attached to the order. Visualizing it
 * as a sticky note that is attached to the order slip.
 * @author jmadison :2015.10.12
 */
public class SpecialInstructionsStickyNote {
    
    /** Visualizing this as an array of lines.
     *  Each individual note is a line on the paper with a stament like:
     * notes[0] == .varName = "ninja_id", .varValue = 5 //one note.
     */
    public List<OrderArg> notes;
    
    /**
     * Creates a new instance that is ready to be filled.
     * @return :See above.
     */
    public static SpecialInstructionsStickyNote makeReadyFill(){
        SpecialInstructionsStickyNote op = new SpecialInstructionsStickyNote();
        op.notes = new ArrayList<OrderArg>();
        return op;
    }//FUNC::END
    
    public int size(){
        if(null==notes){return 0;}
        return notes.size();
    }//Class:END
    
    /** Appends a new spec to our list of specs. Will throw error if
     *  there is a collision. (Spec already exists)
     * @param varName  :The variable name/identifier.
     * @param varValue :The value stored in the variable.
     */
    public void add(String varName, Object varValue){
        OrderArg oa = new OrderArg();
        oa.varName = varName;
        oa.varValue= varValue;
    }//FUNC::END
    
    //cant seeem to do this:
    //public <T> T getVal(String varName){
    //    Object val = getValue(varName);
    //    if(val instanceof T){
    //        
    //}//FUNC:::END
    
   
    /**
     * Get value explicitly as a long value. 
     * @param varName :The variable name to identify.
     * @return :See above.
     */
    public long getValLong(String varName){
        Object val = getValue(varName);
        
        if(val instanceof Long){
           return (Long)val;
        }//Long
        
        //handle it if it happens:
        if(val.getClass() == long.class){
            doError("[no clue how to handle this scenario]");
        }//long
        
        if(DebugConfig.isDebugBuild){
            doError("[Was not able to cast value to Long.]");
        }//
        
        return (-1);
       
    }//FUNC::END
    
    /** 
     * Retrieve that value of variable stored in the args.
     * Assumes that all entries in list have unique variable names.
     * @param varName :A string representing variable name/identifier.
     * @return :The actual value of that variable.
     * THROWS ERROR IF NOT FOUND.
     * Method should be static. Makes easier to work with.
     */
    public Object getValue(String varName){
        boolean wasFound = false;
        OrderArg cur;
        int len = notes.size();
        for(int i = 0; i < len; i++){
            cur = notes.get(i);
            if( cur.varName.equals(varName) ){
                wasFound = true;
                return cur.varValue;
            }//FOUND!
        }//Next i
        
        if(false == wasFound){
            throw new Error("[Variable was not found!]");
        }//NOT FOUND!
        
        return null;
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = SpecialInstructionsStickyNote.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
