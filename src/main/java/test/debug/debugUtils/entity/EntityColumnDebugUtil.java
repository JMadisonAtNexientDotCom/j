package test.debug.debugUtils.entity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import test.MyError;
import test.debug.debugUtils.entity.helpers.ErrorEntry;
import test.debug.debugUtils.entity.helpers.ErrorEntry_CONSTVAL_CASE;
import test.debug.debugUtils.entity.helpers.ErrorEntry_CONSTVAL_CHARS;
import test.debug.debugUtils.entity.helpers.ErrorEntry_STATIC_COLUMN;
import utils.ReflectionHelperUtil;
import utils.StringUtil;

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
    
    //DESIGN NOTE: 2015.10.01_0649PM:
    //List<ErrorEntry> was actually TOO MUCH STRUCTURE.
    //Just make a big string that logs all the problems as you go.
    //And one boolean to mark if there are problems.
    
    private static String _log = "";
    private static boolean _hasErrors = false;
    
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
    private static final List<ErrorEntry> _errorList = 
                                                    new ArrayList<ErrorEntry>();
    
    /** An error list of static column errors.----------------------------------
     *  Meaning: Variable annotated with @Column does not have an analogous
     *  static variable ending with _COLUMN ---------------------------------**/
    private static final List<ErrorEntry_STATIC_COLUMN> 
           _errorList_STATIC_COLUMN = new ArrayList<ErrorEntry_STATIC_COLUMN>();
    
    /** An error list of static column errors.----------------------------------
     *  Meaning: Variable annotated with @Column does not have an analogous
     *  static variable ending with _COLUMN ---------------------------------**/
    private static final List<ErrorEntry_CONSTVAL_CASE> 
           _errorList_CONSTVAL_CASE = new ArrayList<ErrorEntry_CONSTVAL_CASE>();
    
    /** An error list of static column errors.----------------------------------
     *  Meaning: Variable annotated with @Column does not have an analogous
     *  static variable ending with _COLUMN ---------------------------------**/
    private static final List<ErrorEntry_CONSTVAL_CHARS> 
         _errorList_CONSTVAL_CHARS = new ArrayList<ErrorEntry_CONSTVAL_CHARS>();
    
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
        
        /** static class variables ending with "_COLUMN" used to identify ------
         *  the column name when doing criteria search. ---------------------**/
        List<Field> theStaticColumnFields;
        
        fields = getFieldsOnClass(curClass);
        setAccessModifiersToPublic(fields);
        theStaticColumnFields = getStaticColumnFieldsFromList(fields);
        
        int len = fields.size();
        for(int i = 0; i < len; i++){
            curField = fields.get(i);
            checkField(curField, theStaticColumnFields);
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
        return ReflectionHelperUtil.getFieldsOnClass(curClass);
    }//FUNC::END
    
    /** Checks Field for @Column annotations. If exists. Makes sure ------------
     *  our rule is being enforced.
     * @param curField 
     ------------------------------------------------------------------------**/
    private static void checkField
                            (Field curField, List<Field> theStaticColumnFields){
        
        Annotation ann = curField.getAnnotation(Column.class);
        if(ann instanceof Column){
            Column col = (Column)ann;
            String columnName = col.name();
            String fieldName  = curField.getName();
            
            //do error check to make sure @Column values are REFERENCE types.
            checkFieldType(curField);
            
            //Will build errors if identifiers ending with "_id" or "_gi"
            //are not type Long 
            checkIDTypes(curField);
            
            if(notEQ(fieldName,columnName)){
                //throwColumnNamingError(fieldName,columnName);
                addError(_currentClassBeingExamined, fieldName, columnName);
            }else{
                //No error on that front. So check for another error.
                //Does this field name have an analagous "XXXXXXXXX_COLUMN"
                //that exists as a static variable on the class?
                int doesFieldNameExist;
                doesFieldNameExist = checkField_IsPairedWith_STATIC_COLUMN
                                             (fieldName, theStaticColumnFields);
                
                //If the field name does NOT exist, we will COLLECT an error because
                //our convention of making public static identifiers for the 
                //field names of variables annotated with @Column has been broken.
                if(doesFieldNameExist == (-1)){//------------------------------
                    addError_STATIC_COLUMN
                                        (_currentClassBeingExamined, fieldName);
                }else{
                    //field may exist... But does it contain correct value?
                    //Example: TOKEN_MSG_COLUMN would == "token_msg"
                    //Example: ID_COLUMN would == "id"
                    int valid_index = doesFieldNameExist;
                    Field theStaticColumn = theStaticColumnFields.get
                                                                  (valid_index);
                    boolean columnConstNameIsCorrect;
                    columnConstNameIsCorrect = staticColumnContentCheck
                                  (_currentClassBeingExamined, theStaticColumn);
                    
                    if(false == columnConstNameIsCorrect){
                        addError_CONSTVAL_CHARS(_currentClassBeingExamined, theStaticColumn);
                    }//ERROR#3: name of _COLUMN variable not reflected in it's value.
                }//ERROR#2: no corrosponding STATIC: [variable]_COLUMN 
            }//ERROR#1: field name does not match @column name.
        }//instance of?
       
    }//FUNC::END
            
    /**
     * Makes sure that column fields ending with "_id" and "_gi"
     * are Long values because they refer to primary key values. 
     * @param f 
     */
    private static void checkIDTypes(Field f){
        Class type = f.getType();
        String name = f.getName();
        if(StringUtil.endsWith(name,"_id") ||
           StringUtil.endsWith(name,"_gi") ){
            
            if(type != Long.class){
                _hasErrors = true;
                _log += "[Field:[" + name + "]must be type Long.]";
                
                if(type == long.class){
                    _log += "[You had lowercase long, you were close]";
                }//
                
                if(type == Integer.class ||
                   type == int.class){
                  _log += "[Had num type. But you specifically need Long]";
                }//
            }//Bad type?
        }//FUNC::END
    }//FUNC::END
        
    /**
     * Make sure the field annotated with @Column is a REFERENCE
     * type and not a value type. Hibernate runs into problems
     * when it tries to assign NULL to a value type or tries to
     * de-reference a value type.
     * @param f :The field we want to check the type of.
     */
    private static void checkFieldType(Field f){
        Class type = f.getType();
        
        //We only want to support REFERENCE types.
        //But going to be EXPLICIT with which ones we allow.
        //Add to this list if you get a false-positive error from this check.
        if(type == Long.class ){ return; }
        if(type == Boolean.class){return;}
        if(type == String.class) {return;}
        
        _hasErrors = true;
        String cName = _currentClassBeingExamined.getCanonicalName();
        _log += "[BAD FIELD TYPE ERROR: START]";
        _log += "Field Name:[" + f.getName() + "]";
        _log += "From Class:[" + cName + "]";
        _log += "[BAD FIELD TYPE ERROR: END]";
    }//FUNC::END
                            
    /** ------------------------------------------------------------------------
     *  Looks at a static _COLUMN variable to see if it has correct value.
     *  EXAMPLE:
     *  public static string TOKEN_MSG_COLUMN = "token_msg";
     *  The value has the _COLUMN stripped off. And it is all lowercase.
     * 
     *  value is == "token_msg" 
     * @param c :The class that this Field belongs to.
     * @param theStaticColumn :The field we are inspecting.
     * @return :True if everything checks out alright.
     ------------------------------------------------------------------------**/
    private static boolean staticColumnContentCheck
                                               (Class c, Field theStaticColumn){
        
        //Get the name of the variable, and the value it holds:
        String staticVarName  = theStaticColumn.getName();
        String staticVarValue = getStaticConstValue(theStaticColumn);
        
        //Check to make sure all characters in staticVarValue are lowercase.
        if(false == areAllCharactersLowercase(staticVarValue)){
            addError_CONSTVAL_CASE(c,theStaticColumn);
            return false;
        }//false
        
        //take the staticVarValue and append "_COLUMN" to it, then UCASE it.
        //if everything hits convention, it will now be equivalent to
        //staticVarName:
        String reconstruction = staticVarValue.toUpperCase() + "_COLUMN";
        if( notEQ(reconstruction, staticVarName)){
            return false;
        }/////////////////////////////////////////
        
        //return true, the contents of the static variable check out alright.
        return true;
        
    }//FUNC::END
                   
    /** Gets the value of a static string constant.-----------------------------
     * @param theStaticColumn :The static constant.
     * @return :The value stored inside the static constant.
     ------------------------------------------------------------------------**/
    private static String getStaticConstValue(Field theStaticColumn){
        //reference for getting value of static variable:
        //http://stackoverflow.com/questions/2685345/
        //
        //if an error happens here, we are not even going to to record it. EEEEE
        //we are just going to throw an unhandled exception right now. EEEEEEEEE
        Object obj;
        try {
            obj = theStaticColumn.get(null);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(EntityColumnDebugUtil.class.getName()).log
                                                       (Level.SEVERE, null, ex);
            String msg = "unable to retrieve value of static var. #1";
            Class  clz = EntityColumnDebugUtil.class;
            throw MyError.make(clz, msg);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(EntityColumnDebugUtil.class.getName()).log
                                                       (Level.SEVERE, null, ex);
            String msg = "unable to retrieve value of static var. #2";
            Class  clz = EntityColumnDebugUtil.class;
            throw MyError.make(clz, msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //cast the object to string:
        String staticVarValue = (String)obj;
        
        return staticVarValue;
    }//FUNC::END
                              
    
    
    /**-------------------------------------------------------------------------
     * @param value : The string we want to check for all lowercase.
     * @return      : Returns TRUE if all characters were lowercase.
     ------------------------------------------------------------------------**/
    private static boolean areAllCharactersLowercase(String value){
        String ucased = value.toLowerCase();
        return ( ucased.equals(value));
    }//FUNC::END
                      
    /**
     * Compares the current field against a selection of static fields.
     * Looks for a static field that is equal to this formula:
     * lookingFor == fieldName.toUpperCase() + "_COLUMN";
     * @param fieldName :The instance field NAME we hope has a friend.
     * @param theStaticColumnFields :The potential static fields that could
     *                               fit the naming criteria we are looing for.
     */
    private static int checkField_IsPairedWith_STATIC_COLUMN
                          (String fieldName, List<Field> theStaticColumnFields){
                  
        String lookingFor = fieldName.toUpperCase() + "_COLUMN";
        int exists_at = doesFieldNameExist(lookingFor, theStaticColumnFields);
        
        return exists_at;
                                
    }//FUNC::END
                            
    
                   
    /**-------------------------------------------------------------------------
     * Query if a field name exists in the collection:
     * @param lookingFor :The name of the field we are looking for.
     * @param theStaticColumnFields :The fields to search through.
     * @return :INDEX of ~occurance~ of Field matching search criteria.
     *          If no match found, returns (-1)
     ------------------------------------------------------------------------**/
    private static int doesFieldNameExist
                         (String lookingFor, List<Field> theStaticColumnFields){
        int results = (-1);
        Field cur;
        int len = theStaticColumnFields.size();
        for(int i = 0; i < len; i++){
                cur = theStaticColumnFields.get(i);
                if(cur.getName().equals(lookingFor)){
                    results = i;
                    break; //exit loop.
                }////////////////////////////////////
        }//NEXT i
        
        //return if field of name [lookingFor] was found:
        return results;
        
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
        throw MyError.make(EntityColumnDebugUtil.class, msg);
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
        return ReflectionHelperUtil.arrayToList_Field(fArr);
    }//FUNC::END
    
    /** If any errors were collected in our column naming conventions,----------
     *  then we want to list out those errors in a big table and
     *  throw an exception displaying that data.
     ------------------------------------------------------------------------**/
    private static void possiblyCrashAndListProblems(){
        
        //if anything is in our error list, we will crash:
        int errorAmount = getSumOfAllErrorsFromLists();
        if(errorAmount>0){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            crashAndOutput();
        }else
        if(errorAmount<=0){
            //Do nothing.
        }else{
            String msg ="We should never execute this line. hjjlsfjslfs";
            Class clz = EntityColumnDebugUtil.class;
            throw MyError.make(clz,msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
    }//FUNC::END
   
    /**-------------------------------------------------------------------------
     * Throws an error with an in depth summary of all those things you
     * did wrong in the source code.
     ------------------------------------------------------------------------**/
    private static void crashAndOutput(){
        
        //Our initial blank message:
        String msg = "";
        
        msg += _log; //append the log to the list of errors first.
                     //this log should replace the horrible mess you have
                     //below.
        
        if(_errorList.size() > 0){
            msg += ErrorEntry.add(msg, _errorList);
        }//APPEND MSG
        
        if(_errorList_STATIC_COLUMN.size() > 0){
            msg += ErrorEntry_STATIC_COLUMN.add(msg, _errorList_STATIC_COLUMN);
        }//APPEND MSG
        
        if(_errorList_CONSTVAL_CASE.size() > 0){
            msg += ErrorEntry_CONSTVAL_CASE.add(msg, _errorList_CONSTVAL_CASE);
        }//APPEND MSG
        
        if(_errorList_CONSTVAL_CHARS.size() > 0){
            msg += ErrorEntry_CONSTVAL_CHARS.add(msg, _errorList_CONSTVAL_CHARS);
        }//APPEND MSG
        
        //BUGFIX: Forgot the most important part...Throwing the error.
        throw MyError.make(EntityColumnDebugUtil.class, msg);
        
    }//FUNC::END
    
    
    
    /**-------------------------------------------------------------------------
     * Find the static member variables that end with "_COLUMN" so that we
     * can enforce this convention as well. This makes it easy for us to
     * do a criteria search.
     * @param fields :A list of fields that were collected from the class.
     * @return :The static fields in the list provided.
     -------------------------------------------------------------------------*/
    private static List<Field> getStaticColumnFieldsFromList
                                                           (List<Field> fields){
        List<Field> op = new ArrayList<Field>();
        Field cur;
        
        int len = fields.size();
        for(int i = 0; i < len; i++){
            cur = fields.get(i);
            if( getIsFieldStaticColumn(cur)){
                op.add(cur);
            }//||||||||||||||||||||||||||||||
        }//NEXT i
        
        //return the accumulated static fields ending with "_COLUMN"
        return op;
    }//FUNC::END
      
                                                           
    private static boolean getIsFieldStatic(Field f){
       return ReflectionHelperUtil.getIsFieldStatic(f);
    }//FUNC::END
     
    /** Parse the name of the field and look for "_COLUMN" at the END
     *  of it. Decided at the end because:
     *  1. Gramatically less confusing most of the time than
     *     if used as PREFIX rather than POSTFIX.
     *  2. CleanCode recommends against pre-fixing a mass amount of
     *     identifiers with the same word because it wreaks havock on
     *     your auto-complete functions.
     * @param f : The field we want to ~analize~.
     * @return 
     */
    private static boolean getIsFieldColumn(Field f){
        
        //true/false result to return. Initially FALSE.
        boolean result = false;
        
        //The name of the field we want to analize.
        String n = f.getName();
        
        //Psuedo constants to help calculations:
        String _COLUMN = "_COLUMN";
        int _COLUMN_DOT_LENGTH = _COLUMN.length();
        
        //http://stackoverflow.com/questions/1987673/substring-search-in-java
        int dexOf = n.indexOf(_COLUMN);
        if(dexOf != (-1)){
            //Only valid if "_COLUMN" appears at the END."
            int lastValidIndex  = n.length()-1;
            int indexOfLetter_N = dexOf + _COLUMN_DOT_LENGTH - 1;
            if(lastValidIndex == indexOfLetter_N){
                result = true;
            }//_COLUMN aligned at exact edge of word.
        }//_COLUMN FOUND!

        return result;
       
    }//FUNC::END
                                                           
    private static boolean getIsFieldStaticColumn(Field f){
        boolean is_Static;
        boolean is_COLUMN;
        is_Static = getIsFieldStatic(f);
        is_COLUMN = getIsFieldColumn(f);
        boolean results = (is_Static & is_COLUMN);
        return results;
    }//FUNC::END
    
    /** Gets a talley of all our problems in the source code. ------------------
     * @return :Total number of errors.
     ------------------------------------------------------------------------**/
    private static int getSumOfAllErrorsFromLists(){
        
        //TODO: Remove this function and replace with your log.
        int sum = 0;
        if(_hasErrors){ sum++;} //<--HACK for now until this function replaced.
        sum += _errorList.size();
        sum += _errorList_STATIC_COLUMN.size();
        sum += _errorList_CONSTVAL_CASE.size();
        sum += _errorList_CONSTVAL_CHARS.size();
        return sum;
    }//FUNC::END
    
    //ADD ERROR FUNCTIONS:
    //[AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][]
    //[AE]------------|       START      |--------------------------------[AE][]
    //[AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][]
    
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
    
    /** Add a broken static _COLUMN identifier error-entry to our list----------
     *  of errors to report on.
     * @param c :The class that has the error.
     * @param fieldName :The INSTANCE field name that does not have a
     *                   STATIC _COLUMN identifier variable paired with it.
     ------------------------------------------------------------------------**/
    private static void addError_STATIC_COLUMN(Class c, String fieldName){
        ErrorEntry_STATIC_COLUMN entry = new ErrorEntry_STATIC_COLUMN();
        entry.c = c;
        entry.instanceVarName = fieldName;
        _errorList_STATIC_COLUMN.add(  entry  );
    }//FUNC::END
    
    /** This error is for when the casing is incorrect in the value of ---------
     *  the static _COLUMN variable.
     * @param c :The current class being inspected, 
     *           that the variable belongs to.
     * @param theStaticColumn :The STATIC variable we are inspecting.
     ------------------------------------------------------------------------**/
    private static void addError_CONSTVAL_CASE(Class c, Field theStaticColumn){
        ErrorEntry_CONSTVAL_CASE entry = new ErrorEntry_CONSTVAL_CASE();
        entry.c = c;
        entry.fieldName  = theStaticColumn.getName();
        entry.constValue = getStaticConstValue(theStaticColumn);
        _errorList_CONSTVAL_CASE.add(  entry  );
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * Error for when the value of our static _CONST is all lowercase as
     * expected. But it doesn't correctly match the variable it corrosponds
     * to as per our convention.
     * 
     * Example:
     * -------------------------------------------------------------------------
     * public static TOKEN_MSG_COLUMN = "token_msg";
     * 
     * @Column(name="token_msg");
     * String token_msg
     * -------------------------------------------------------------------------
     * All of that must be in place for a database column to be referenced
     * in an entity in this code base. NOTE: upper-case characters are
     * NOT considered valid database column names in this code base.
     * 
     * @param c :The class that has the error.
     * @param theStaticColumn :The STATIC variable we are inspecting. 
     ------------------------------------------------------------------------**/
    private static void addError_CONSTVAL_CHARS(Class c, Field theStaticColumn){
        ErrorEntry_CONSTVAL_CHARS entry = new ErrorEntry_CONSTVAL_CHARS();
        entry.c = c;
        entry.fieldName = theStaticColumn.getName();
        entry.columnName = "!N/A!";
        _errorList_CONSTVAL_CHARS.add(  entry  );
    }//FUNC::END
    
    //[AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][]
    //[AE]------------|       END      |--------------------------------[AE][]
    //[AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][AE][]
  
}//CLASS::END
