package app.dbDataAbstractions.entities.bases;

/**-----------------------------------------------------------------------------
 * A base class for all composite entities that have no direct relationship
 * within the database. I am not sure if CompositeEntityBase should extend
 * the BaseEntity class or not.
 * 
 * For now, do NOT. Since I don't understand the implications.
 * Better to be more specific and keep them separate than to accidentially
 * mix things that should not be mixed.
 * 
 * What was my definition of composite entity, and how did it differ from
 * a BUNDLE type entity?
 * 
 * COMPOSITE ENTITY: (maybe should rename to "collection entity")
 * Entity that is a collection of entities of the same TYPE.
 * 
 * BUNDLE ENTITY definition:
 * 1. an entity that stores composite entities.
 * 2. an entity that stores two or more entities of DIFFERENT types.
 * 
 * @author jmadison
 ----------------------------------------------------------------------------**/
public class CompositeEntityBase extends KernelEntity{
    
    //NOTHING IN HERE. FOR NOW.
    
}//CLASS::END
