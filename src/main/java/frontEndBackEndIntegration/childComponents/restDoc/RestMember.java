package frontEndBackEndIntegration.childComponents.restDoc;

/**
 * Documentation that contains all the info you would want in order to
 * INVOKE a member function from a specific rest service.
 * 
 * Example, if you have a service called "NinjaRestService"
 * Then you would have:
 * A RestMember instance for...
 * calling: NinjaRestService/MakeNinja
 * calling: NinjaRestService/KillNinja 
 * calling: NinjaRestService/GiveNinjaAMission
 * This documentation object is used to allow our UI people to have
 * something to grab onto and use in the .JSP files.
 * @author jmadison
 */
public class RestMember {
    public String funcName = "MakeNinja";
    
}//CLASS::END
