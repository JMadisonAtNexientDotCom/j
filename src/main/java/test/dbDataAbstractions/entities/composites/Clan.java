package test.dbDataAbstractions.entities.composites;

import java.util.ArrayList;
import java.util.List;
import test.MyError;
import test.config.constants.EntityErrorCodes;
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
        
        //Error check:  Make sure casting works as expected:
        if(null==genericMembers ){doError("[NullNotExpected 424234234]");}//EEEE
        if(null==membersAsNinjas){doError("[NullNotExpected 998787345]");}//EEEE
        int lenBefore = genericMembers.size();
        int lenAfter  = membersAsNinjas.size();
        if(lenBefore != lenAfter){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            doError("[List cast did not work as expected]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //Call the function we are wrapping and return results:
        return makeClan(membersAsNinjas, inDisplayName);
    }//FUNC::END
                        
    public static Clan makeErrorClan(String errorMSG){
        //Create our error object:
        NinjaTable errorNinja = new NinjaTable();
        String com = "[Touched by Clan.makeErrorClan]";
        errorNinja.setComment(com);
        errorNinja.setName(errorMSG);
        errorNinja.setEmail(errorMSG);
        errorNinja.setPortfolio_url(errorMSG);
        errorNinja.setPhone(-999);
        errorNinja.setErrorCode(EntityErrorCodes.GENERIC_ERROR);
        errorNinja.setIsError(true);

        //Put this entity into EVERY SLOT of a list of the expected length:
        //Oh wait... we can't. That length might be BAD DATA... Just return
        //ONE.. Because we know they wanted at least one.
        List<NinjaTable> errorNinjas = new ArrayList<NinjaTable>();
        errorNinjas.add(errorNinja);

        //Pack this list into a CLAN object. (clan == group of ninjas)
        Clan clanOfErrors = Clan.makeClan(errorNinjas,errorMSG);
        clanOfErrors.setIsError(true);
        clanOfErrors.setErrorCode(EntityErrorCodes.GENERIC_ERROR);
        
        //return the error object:
        return clanOfErrors;
        
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
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
