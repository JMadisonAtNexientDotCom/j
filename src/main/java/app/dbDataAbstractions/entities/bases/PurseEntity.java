package app.dbDataAbstractions.entities.bases;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import app.config.constants.identifiers.VarNameReg;

/**
 * A purse entity is a special type of table that is used to store a heap
 * of records that have group_id associations. Individual records from these
 * tables are not of much value. For the most part, we want to treat all
 * records with the SAME GROUP ID within these tables as ONE OBJECT.
 * @author jmadison :2015.10.20 (October Twentieth, Year 2015);
 * 
 * DESIGN NOTE:
 * Been making new entities WITHOUT getters and setters.
 * Because getters and setters are:
 * 1. Unecessary boiler plate.
 * 2. If you screw them up, they mess with your serialization.
 *    You must follow camelCase convention when making getters+setters.
 *    But I've been using under_scores since they correspond to table names.
 *    I end up making really weird camelCase_under_score hibrid getter setter
 *    names just for the code to work properly.
 * 
 * Also: This resource says getters+setters are old-school and
 *       using public variable is allowed by hibernate and good practice.
 *       https://netbeans.org/bugzilla/show_bug.cgi?id=225766
 * 
 */
@MappedSuperclass
public class PurseEntity extends BaseEntity{
    
    public static final String GROUP_ID_COLUMN = VarNameReg.GROUP_ID;
    
    ///If you see compiler warning:
    //"Instance variable for a persistent attribute must not be public"
    //The compiler is lying to you. JPA2 standards allow for this.
    //And hibernate supports it.
    // http://stackoverflow.com/questions/5602908/
    //                    jpa-which-should-i-use-basicoptional-or-columnnullable
    @Basic(fetch=FetchType.EAGER) //<--attempt to make group_id not null 
    @Column(name = GROUP_ID_COLUMN)  //when getting it from database.
    public long group_id; //<--change to value-type to enforce nullable==false?
    
}//CLASS::END
