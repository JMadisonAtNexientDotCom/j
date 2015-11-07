package test.dbDataAbstractions.entities.tablePojos;

import java.util.ArrayList;
import java.util.List;
import test.dbDataAbstractions.entities.bases.CompositeEntityBase;
import test.dbDataAbstractions.fracturedTypes.clientServerConversation.lectern.Slate;

/**-----------------------------------------------------------------------------
 * A Quar is a tall black rectangular prism of fine stone.
 * That is sliced into chalk board slates. And used to answer
 * questions presented on CueCard(s).
 * 
 * @author jmadison: 2015.09.18_0823PM
 -----------------------------------------------------------------------------*/
public class Quar extends CompositeEntityBase{
    public List<Slate> slates;
    
    /**-------------------------------------------------------------------------
     * Creates an empty Quar object that is ready to be populated.
     * @return :A quar with a non-null slates variable.
     ------------------------------------------------------------------------**/
    public static Quar makeEmptyInstance(){
        Quar op = new Quar();
        op.slates = new ArrayList<Slate>();
        return op;
    }//FUNC::END
    
}//CLASS::END
