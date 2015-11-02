package test.dbDataAbstractions.entities.bases;

/**
 * Used for POJO object. NOT for TABLE-ENTITY objects.
 * This base class has no functionality.
 * It is simply used for strict typing.
 * If a POJO extends this, we know it is the challenge question guts
 * of a [trial/test]. No meta data. Just the questions.
 * 
 * If you want information to populate a test within a UI on a web page,
 * you want one of these instances.
 * 
 * @author jmadison :2015.10.28
 */
public class ChallengeGuts extends CompositeEntityBase {
    
    //Nothing here. Just a formality so we can identify the object.
    //Since the guts of the challenge may vary depending on what type
    //of [trial/test] is being taken.
    
}//CLASS::END
