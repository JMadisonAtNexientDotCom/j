package app.dbDataAbstractions.fracturedTypes;

/**
 * A fractured type has no direct representation in the database.
 * It is smaller than a table entity that has a [ 1 to 1 ] relationship.
 * Fractured types have a [ <1 to 1 ] relationship with the database.
 * AKA: 0.5 to 1, 0.25 to 1. ETC.
 * They represent a fractional selection of data from the table.
 * Such as, maybe the id of a record in the RiddleTable. But not the text
 * of that record as well.
 * 
 * Feel like hibernate probably has a better way to handle this.
 * Because this is probably what oneToMany, ManyToMany, etc mappings are
 * used for. However, the goal is to get this DONE+WORKING.
 * 
 * If the architecture confuses me, then it fails to serve it's purpose.
 * Which is to make my life easier. Possibly AFTER I have finished this
 * project I will have enough experience+insight to fully utilize hibernate
 * and spring.
 * 
 * @author jmadison                                                          **/
public class FracturedTypeBase {
    
    //Nothing in here. Just declared for type saftey.
    
}//CLASS::END
