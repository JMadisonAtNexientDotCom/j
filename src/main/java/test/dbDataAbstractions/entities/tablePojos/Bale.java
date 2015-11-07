package test.dbDataAbstractions.entities.tablePojos;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a straw bale within a loft.
 * An abstraction of a data collection structure I am calling a "loft".
 * @author jmadison :2015.11.07(Nov7th,Year2015,Saturday) **/
public class Bale {
    
    //A bale contains straw. The straw has no order. But it has a grouping.
    //AKA: The Bale GROUPS the straw. But it does NOT order the straw.
    //(The ordering within this list does NOT matter)
    public List<Long> straw; //<--UN-ordered list.
    
    
     /**
     * Uniquely adds to the collection.
     * @param foreign_key_straws :The list of foreign key ids that is our
     *                            "straw" in this abstraction. **/
    public void uniqueAdd(List<Long> foreign_key_straws){
        
        //TODO: Might want to use a set eventually. For now. Stick with lists.
        //      Since that is what you are using everywhere.
        for(Long piece_of_straw : foreign_key_straws){
            if(false == straw.contains(piece_of_straw)){
                straw.add(piece_of_straw);
            }
        }//next straw.
        
    }//FUNC::END
    
    /**
     * 
     * @param initial_a_bale_worth_of_straw :Initial straw to put in bale.
     * @return :A bale with straw in it. **/
    public static Bale make(List<Long> initial_a_bale_worth_of_straw){
        Bale op = Bale.makeEmptyBale();
        op.straw.addAll(initial_a_bale_worth_of_straw);
        return op;
    }//FUNC::END
    
    
    /**
     * Makes an empty bail initialized with an empty array of straw.
     * @return :see above. **/
    public static Bale makeEmptyBale(){
        Bale op = new Bale();
        op.straw = new ArrayList<Long>();
        return op;
    }//FUNC::END
    
  
    
}//CLASS::END
