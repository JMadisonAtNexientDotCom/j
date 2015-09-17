package test.debug.debugUtils;

import java.util.ArrayList;
import java.util.List;
import test.MyError;

/**-----------------------------------------------------------------------------
 * This is a utility used to enforce a convention of mine.
 * The more strict your code is, the less errors you have.
 * And the easier it is to find errors.
 * 
 * Convention:
 * If entity variable is mapping to a database column, the variable
 * name should be IDENTICAL to the database column name.
 * 
 * Example:
 * @Column(name="match_column");
 * long match_column;
 * 
 * Reason for this enforced convention:
 * 1. Makes criteria searches easier.
 * 2. Rather have ONE NAME for the exact same data set.
 * 3. When reading code, makes it obvious when variable is referring to a data
 *    base column because we have this_style_of_variable rather than
 *    thisStyleOfVariable
 * 
 * @author jmadison
 ----------------------------------------------------------------------------**/
public class EntityColumnDebugUtil {
    
    /** A list of all the Entity Classes that we want to check the annotations
     *  for when we do our integrity check. ---------------------------------**/
    private static final List<Class> _trackedEntityClasses = 
                                                         new ArrayList<Class>();
    
    /**-------------------------------------------------------------------------
     * Add an entity class to our list. We will debug the @Column annotations
     * on this entity at a later date.
     * @param classToTrack:The class we want to debug @Column annotations on.
     ------------------------------------------------------------------------**/
    public static void addAnnotatedEntityClass(Class classToTrack){
        _trackedEntityClasses.add(classToTrack);
    }//FUNC::END
    
    /** Checks the @Column annotations on all of the classes in our-------------
     *  _trackedEntityClasses and makes sure they conform to the
     *  convention of [column identifier text] == [variable identifier text]
     ------------------------------------------------------------------------**/
    public static void doIntegrityCheck(){
        
        throw new MyError("TODO: integrity check!");
        
        
    }//FUNC::END
    
    
    
    
    
}//CLASS::END
