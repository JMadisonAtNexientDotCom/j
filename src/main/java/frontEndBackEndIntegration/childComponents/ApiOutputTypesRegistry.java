package frontEndBackEndIntegration.childComponents;

import app.dbDataAbstractions.entities.bases.BaseEntity;
import app.dbDataAbstractions.entities.composites.Clan;
import app.dbDataAbstractions.entities.tables.NinjaTable;

/**
 * A registry holding a null reference to every type that can be
 * outputted by api calls. Allows us to have auto-completion reference
 * to data used by UI.
 * @author jmadison :2015.10.14
 */
public class ApiOutputTypesRegistry {
    //public final Clan CLAN = null;
    public final NinjaTable NINJA = new NinjaTable(); 
    public final BaseEntity ENTITY= new BaseEntity();
}//CLASS::END
