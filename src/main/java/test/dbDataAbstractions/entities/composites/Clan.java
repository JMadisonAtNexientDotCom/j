package test.dbDataAbstractions.entities.composites;

import java.util.List;
import test.MyError;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import test.dbDataAbstractions.entities.bases.CompositeEntityBase;
import test.dbDataAbstractions.entities.tables.NinjaTable;

/**
 * A group of Ninjas could be known as a Clan. So rather than calling it a
 * "listOfNinjas" which is overly verbose. I would like to be concise and
 * call it a "Clan".
 * 
 * @author jmadison:2015.10.03
 */
public class Clan extends CompositeEntityBase {
    
    /** Depending on what this list of ninjas represents, we may want to
     *  give it a different display name. For example, if it is a results list
     *  of "page 1 of 4" we might want it's display name to be "page 1 of 4" **/
    public String displayName = "NOT_SET";
    
    /** Members of the clan. **/
    public List<NinjaTable> members;
    
    /**
     * A maker function with basic error checking.
     * @param inMembers :Members to add to clan.
     * @param inDisplayName :The display name for this clan.
     * @return :A Clan of ninjas with a display name.
     */
    public static Clan makeClan
                             (List<NinjaTable> inMembers, String inDisplayName){
        if(null == inMembers){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            doError("[inMembers may NOT be null.]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        if(null == inDisplayName || "".equals(inDisplayName)){//EEEEEEEEEEEEEEEE
            doError("[Use something! Blank or null string not acceptable]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        Clan op = new Clan();
        op.displayName = inDisplayName;
        op.members     = inMembers; //<--list allowed to be empty.
        
        //return the Clan:
        return op;
    }//FUNC::END
    
    /**
     * Wrapper function used to handle when you have a list that has
     * not been casted into NinjaTable type.
     * @param genericMembers :Entries that are currently downcasted.
     * @param inDisplayName  :Display name to give this collection.
     * @return :A clan of ninjas with a display name.
     */
    public static Clan makeClanUsingBaseEntities
                        (List<BaseEntity> genericMembers, String inDisplayName){
        
        //Error check: Check for null input. Because we don't want weird
        //error from seeing null casted into list.
        if(null==genericMembers){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            doError("[Attempt to cast null list of generic members]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
              
        //Cast generic input into it's true type:
        List<NinjaTable> membersAsNinjas = 
                                      (List<NinjaTable>)(List<?>)genericMembers;
        
        //Call the function we are wrapping and return results:
        return makeClan(membersAsNinjas, inDisplayName);
    }//FUNC::END
                             
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = Clan.class;
        err += clazz.getSimpleName();
        err += msg;
        throw new MyError(clazz, err);
    }//FUNC::END
    
}//CLASS::END
