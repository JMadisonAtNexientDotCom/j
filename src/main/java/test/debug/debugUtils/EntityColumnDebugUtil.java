package test.debug.debugUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import test.MyError;
import test.debug.debugUtils.helpers.ErrorEntry;

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
 * @author jmadison ON:2015.09.17_0313PM
 ----------------------------------------------------------------------------**/
public class EntityColumnDebugUtil {
    
    /** A list of all the Entity Classes that we want to check the annotations
     *  for when we do our integrity check. ---------------------------------**/
    private static final List<Class> _trackedEntityClasses = 
                                                         new ArrayList<Class>();
    
    /** Helps give us a more informative debug message. **/
    private static Class _currentClassBeingExamined;
    
    /** Rather than crash on first error found, scan everything and collect-----
     *  all errors. That way we don't have to:
     *  1. re-deploy the server...
     *  2. fix ONE error.
     *  3. re-deploy the server...
     *  4. fix ONE error.
     *  5. re-deploy the server...
     *  6. fix ONE error.
     *  ETC. (An insight I learned through experience.)
     ------------------------------------------------------------------------**/
    private static List<ErrorEntry> _errorList = new ArrayList<ErrorEntry>();
    
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
        
        int len = _trackedEntityClasses.size();
        for(int i = 0; i < len; i++){
            _currentClassBeingExamined = _trackedEntityClasses.get(i);
            checkColumnAnnotationsOnClass( _currentClassBeingExamined );
        }//NEXT i
        
        //If any errors were collected, it is now time to crash the program
        //and summarize all the errors in a big table:
        possiblyCrashAndListProblems();
        
    }//FUNC::END
    
    /** Check the @Column annotations on the class and make sure they match-----
     *  the field name they are being applied to.
     * @param curClass 
     ------------------------------------------------------------------------**/
    private static void checkColumnAnnotationsOnClass(Class curClass){
        
        List<Field> fields;
        Field curField;
        
        fields = getFieldsOnClass(curClass);
        setAccessModifiersToPublic(fields);
        
        int len = fields.size();
        for(int i = 0; i < len; i++){
            curField = fields.get(i);
            checkField(curField);
        }//NEXT i
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Seems like it might be a good idea. Else problems might arise
     * when I am trying to work with them.
     * http://stackoverflow.com/questions/2989560/
     *                         how-to-get-the-fields-in-an-object-via-reflection
     * @param fields : The list of Field(s) you want to set the access on.
     ------------------------------------------------------------------------**/
    private static void setAccessModifiersToPublic(List<Field> fields){
        
        Field currentField;
        int len = fields.size();
        for(int i = 0; i < len; i++){
            currentField = fields.get(i);
            currentField.setAccessible(true);
        }//NEXT i
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Get all of the fields that are on the class.
     * @param curClass :The class we want fields off of.
     * @return :The fields. Un-tampered with.
     *          You might want to tamper with them and edit the write access.
     *          That way you can access private fields.
     ------------------------------------------------------------------------**/
    private static List<Field> getFieldsOnClass(Class curClass){
        Field[] fArr = curClass.getDeclaredFields();
        List<Field> op = arrayToList_Field(fArr);
        return op;
    }//FUNC::END
    
    /** Checks Field for @Column annotations. If exists. Makes sure ------------
     *  our rule is being enforced.
     * @param curField 
     ------------------------------------------------------------------------**/
    private static void checkField(Field curField){
        
        Annotation ann = curField.getAnnotation(Column.class);
        if(ann instanceof Column){
            Column col = (Column)ann;
            String columnName = col.name();
            String fieldName  = curField.getName();
            if(notEQ(fieldName,columnName)){
                //throwColumnNamingError(fieldName,columnName);
                addError(_currentClassBeingExamined, fieldName, columnName);
            }
        }//instance of?
       
    }//FUNC::END
    
    /** Throws an error to alert the programmer that they have broken our
     *  convention. Doing this will allow us to catch mistakes that could
     *  over time completely de-rail the [structure/design] that was ~initially~
     *  laid out.
     * @param fieldName :Name of the field in your java class.
     * @param columnName:The .name property used in @Column notation applied
                         to that field within your java class. --------------**/
    private static void throwColumnNamingError
                                          (String fieldName, String columnName){
        String msg = "";
        msg+="[BROKEN ENFORCED CONVENTION ERROR::]";
        msg+="CLASS:[" + _currentClassBeingExamined.getCanonicalName() + "]";
        msg+="["+ fieldName + "]!=[" + columnName + "]";
        msg+="Bad variable/field name:[" + fieldName + "]";
        msg+="To make criteria queries less error prone";
        msg+="And to make sure we don't have two variables for ONE concept";
        msg+="I have decided that the @Column(name='my_variable') must";
        msg+="Match the variable it is being applied to.";
        msg+="In your case:";
        msg+="Since your COLUMN name is:";
        msg+="@Column(name=" + "'" +columnName+ "'" + ")";
        msg+="The variable name should be:";
        msg+="[" + columnName + "]";
        msg+="[In short: Variable name should be identical to column name.]";
        throw new MyError(msg);
    }//FUNC::END
  
    /** Returns TRUE if the two strings are not equivalent in value. **/
    private static boolean notEQ(String s0, String s1){
        boolean equivalence = s0.equals(s1);
        return (!equivalence);
    }//FUNC::END
    
    /**
     * Helper Function:Converts an array of fields into a LIST of fields. 
     * @param fArr :The array of Field(s) you want to convert to List. **/
    private static List<Field> arrayToList_Field(Field[] fArr){
        
        Field current_field;
        ArrayList<Field> myList = new ArrayList<Field>();
        
        int len = fArr.length;
        for(int i = 0; i < len; i++){
            current_field = fArr[i];
            myList.add(current_field);
        }//NEXT i
        
        return myList;
    }//FUNC::END
    
    /** If any errors were collected in our column naming conventions,----------
     *  then we want to list out those errors in a big table and
     *  throw an exception displaying that data.
     ------------------------------------------------------------------------**/
    private static void possiblyCrashAndListProblems(){
        
        //if anything is in our error list, we will crash:
        int errorAmount = _errorList.size();
        if(errorAmount>0){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            crashAndOutput();
        }else
        if(errorAmount<=0){
            //Do nothing.
        }else{
            throw new MyError("We should never execute this line. hjjlsfjslfs");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
    }//FUNC::END
    
    /** Appends an error to our list of errors. Will spit out all errors--------
     *  after all annotated classes have been examined.
     * @param c         :c is for Class. The class that contains error.
     * @param fieldName :the filedName/variableName with problem.
     * @param columnName:the @Column(name=??????) name used.
     ------------------------------------------------------------------------**/
    private static void addError(Class c, String fieldName, String columnName ){
        
        //Create error entry:
        ErrorEntry ee = new ErrorEntry();
        ee.c = c;
        ee.fieldName = fieldName;
        ee.columnName= columnName;
        
        //Add it to our master list:
        _errorList.add(ee);
        
    }//FUNC::END
    
    private static void crashAndOutput(){
        
        String nl = System.lineSeparator();
        String msg = "";
        ErrorEntry cur;
        msg+="|--Class Name:--||--Variable Name:--||--Column Name:--|" + nl;
        
        int len = _errorList.size();
        for(int i = 0; i < len; i++){
            cur = _errorList.get(i);
            msg+=makeErrorRecord(cur);
        }//NEXT i
    }//FUNC::END
    
    /** Serializes one line/row of one of the errors we have.-------------------
     *  So that we can build our ascii table of errors to output
     *  as error message.
     * @param cur : The current error to serialize into a line of text.
     * @return    : Serialized ErrorEntry
     ------------------------------------------------------------------------**/
    private static String makeErrorRecord(ErrorEntry cur){
        String msg = "";
        msg+="[" + cur.c.getCanonicalName() + "]";
        msg+="[" + cur.fieldName + "]";
        msg+="[" + cur.columnName + "]";
        msg+= System.lineSeparator();
        return msg;
    }//FUNC::END
  
}//CLASS::END
