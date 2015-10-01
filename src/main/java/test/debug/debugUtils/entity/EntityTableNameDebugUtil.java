package test.debug.debugUtils.entity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import test.dbDataAbstractions.entities.bases.KernelEntity;
import javax.persistence.Table;
import test.MyError;

/**
 * Enforces naming convention for entities that directly map to a single table.
 * If entity class is "MyAwesomeTable.java" then the table name it references
 * should be "my_awesome_table"
 * @author jmadison :2015.09.30
 */
public class EntityTableNameDebugUtil {
    
    /** Helps give us a more informative debug message. **/
    private static Class _currentClassBeingExamined;
    
    private static final List<Class> _trackedEntityClasses = 
                                                         new ArrayList<Class>();
    
    private static boolean _hasErrors = false;
    private static final List<String> _errorLog = new ArrayList<String>();
    
    /** Add entity classes to utility BEFORE validating. **/
    public static void addAnnotatedEntityClass(Class classToTrack){
        _trackedEntityClasses.add(classToTrack);
    }//FUNC::END
    
    public static void doIntegrityCheck(){
        
        int len = _trackedEntityClasses.size();
        for(int i = 0; i < len; i++){
            _currentClassBeingExamined = _trackedEntityClasses.get(i);
            checkThatClassNameConformsToTableName( _currentClassBeingExamined );
        }//NEXT i
        
        //If any errors were collected, it is now time to crash the program
        //and summarize all the errors in a big table:
        possiblyCrashAndListProblems();
        
    }//FUNC::END
    
    private static void possiblyCrashAndListProblems(){
       
        if(false == _hasErrors){return;}//exit if no errors.
        
        String errorLogDump = "";
        errorLogDump += "ERROR IN:";
        errorLogDump += "[" + EntityTableNameDebugUtil.class + "]";
        int len = _errorLog.size();
        for(int i = 0; i < len; i++){
            errorLogDump += _errorLog.get(i);
        }//NEXT i
        
        //THROW YOUR HANDS UP IN THE AIR LIKE YOU JUST DON'T CARE!
        throw new MyError(EntityTableNameDebugUtil.class, errorLogDump);
        
    }//FUNC::END
    
    private static void checkThatClassNameConformsToTableName
                                                          (Class entity_class){
        boolean derived_from_kernel;
        
        //I ALWAYS GET THIS BACKWARDS!
        //derived_from_kernel = entity_class.isAssignableFrom(KernelEntity.class);
        derived_from_kernel = KernelEntity.class.isAssignableFrom(entity_class);
        
        if(false == derived_from_kernel){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            logBadTypeError(entity_class);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        String className = entity_class.getSimpleName();
        String tableName = getTableNameFromClass(entity_class);
        boolean conforms; //<--Conforms to convention?
        conforms = canCamelCaseExpandToUnderScoreSlug(className,tableName);
        
        if(false == conforms){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            logNonConformityError(className,tableName);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
                                                              
    }//FUNC::END
          
    /** Checks the @Table annotation on the class to get the table name --------
     *  it is mapped to.                                   ------------------**/
    private static String getTableNameFromClass(Class entity_class){
        
        Annotation[] annos = entity_class.getDeclaredAnnotations();
        
        String tableName = "";
        boolean wasTableAnnotationFound = false;
        
        for(Annotation ann : annos){
            if(ann instanceof Table){
                Table tab = (Table)ann;
                tableName = tab.name();
                wasTableAnnotationFound = true;
                break;
            }//COLUMN?
        }//NEXT FIELD
        
        if(false == wasTableAnnotationFound){
            logNoTableAnnotationFoundError(entity_class);
        }//LOG ERROR
        
        return tableName;
        
    }//FUNC::END
                                                          
    private static boolean canCamelCaseExpandToUnderScoreSlug
                                           (String className, String tableName){
                                               
        String slug;
        slug = expandCamelCaseToUnderScoreSlug(className);
        
        boolean isExpandable = ( slug.equals(tableName) );
        return isExpandable;
        
    }//FUNC::END
                                           
    private static String expandCamelCaseToUnderScoreSlug(String className){
        String slug = "";
        char cur;
        char prv = 0; //previous cur. Inited to zero to make compiler happy.
        String str;
        String chunk;
        int len = className.length();
        for(int i = 0; i < len; i++){
            cur = className.charAt(i);
            if(i!=0){
                chunk = maybeExpandChar(cur, prv);
            }else{
                str = Character.toString(cur);
                chunk = str.toLowerCase();
            }
            
            prv = cur;
            slug += chunk;
            
        }//NEXT i
        
        return slug;
    }//FUNC::END                                       
               
    /**
     * If character is UPPERCASE we want to replace it with:
     * output = UNDERSCORE + CHARACTER.
     * @param chr :A character:
     * @return :Example:
     *          If chr=="A", returns "_a"
     *          If chr=="a", returns "a"
     */
    private static String maybeExpandChar(char chr, char prv){
        
        String result;
        if( Character.isAlphabetic(chr) ){
            result = maybeExpandChar_alphabetic(chr);
        }else{
            result = maybeExpandChar_numeric(chr,prv);
        }
        return result;
        
        
    }//FUNC::END
    
    /** For handling camel case that includes numbers. If the previous
     *  character was a letter, then we consider it as a divider.
     * @param num :numeric character we are looking at.
     *             HACK: Might be a special character. But not going to
     *                   support anything that is not alpha-numeric.
     * @param prv :Previous character. A letter or number.
     * @return :Returns original character, or expansion.
     */
    private static String maybeExpandChar_numeric(char chr, char prv){
        
        String op = Character.toString(chr).toLowerCase();
        if( Character.isAlphabetic(prv)){ //previous character alphabetic?
            op = "_" + op;
        }//EXPAND with underscore?
        
        return op;
    }//FUNC::END
    
    private static String maybeExpandChar_alphabetic(char chr){
        String str = Character.toString(chr);
        String upp = str.toUpperCase();
        boolean isUpperCase = (str.equals(upp));
        if(isUpperCase){ return "_" + str.toLowerCase(); };
        return str;
    }//FUNC::END
           
    /**
     * @param className :EX: MyExampleTable
     * @param tableName :EX: my_example_table
     * NOTE: If the examples were actually used, we wouldn't have an error,
     *       because the class name is able to expand to and underscore slug.
     */
    private static void logNonConformityError
                                           (String className, String tableName){
        String msg = "";
        msg += "[NON CONFORMITY ERROR: START]";
        msg += "className:" + className + "]";
        msg += "tableName:" + tableName + "]";
        msg += "[NON CONFORMITY ERROR: END]";
        
        _hasErrors = true;
        _errorLog.add(msg);
    }//FUNC::END
                                                          
    private static void logBadTypeError(Class entity_class){
        String msg = "";
        String obj_class = entity_class.getCanonicalName();
        String need_class= KernelEntity.class.getCanonicalName();
        msg+= "[Object is NOT derived from needed base class:]";
        msg+= "[Object's Class:[" + obj_class + "]";
        msg+= "[Needed Base:[" + need_class + "]";
        
        _hasErrors = true;
        _errorLog.add(msg);
    }//FUNC::END
    
    /**
     * Means we could not find the @Table annotation at the class level.
     * Which is REQUIRED for table entities in this application.
     * @param entity_class :The class that has the error.
     */
    private static void logNoTableAnnotationFoundError(Class entity_class){
        String msg = "";
        String cName = entity_class.getCanonicalName();
        msg += "[NO_TABLE_ANNOTATION_FOUND_ERROR::START]";
        msg += "[entity class with problem:[" + cName + "]";
        msg += "[NO_TABLE_ANNOTATION_FOUND_ERROR::END]";
        
        _hasErrors = true;
        _errorLog.add(msg);
    }//FUNC::END
    
}//CLASS::END
