package frontEndBackEndIntegration.childComponents;

import test.dbDataAbstractions.entities.composites.Clan;
import test.dbDataAbstractions.entities.tables.NinjaTable;

/**
 * A registry holding a null reference to every type that can be
 * outputted by api calls. Allows us to have auto-completion reference
 * to data used by UI.
 * @author jmadison :2015.10.14
 */
public class ApiOutputTypesRegistry {
    public final Clan CLAN = null;
    public final NinjaTable NINJA = null;
}//CLASS::END