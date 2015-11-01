package annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import app.MyError;
import utils.ReflectionHelperUtil;

/**
 * Scans @IndexedFunction methods, and then assembles a lookup table with them.
 * Will throw error if any of the ~indicies~ are NOT unique.
 * @author jmadison :2015.10.08
 */
public class IndexedFunctionTable {
    
    /** For code readability. **/
    private final static boolean GET_STATIC   = true;
    
    /** For code readability. **/
    private final static boolean GET_INSTANCE = true;
    
    /** After we are done adding all of the classes to scan, this is the
     *  total number of annotated functions found. **/
    private int _totalNumberOfFunctionsFoundToIndex = 0;
    
    /** This is the annotated function with the highest lookup table key found.
     *  Used to allocated the correct sized array. As well as check for holes
     *  in our lookup table. **/
    private int _maxIndexFoundWhileAddingClasses = 0;
    
    /** We might not want a function at index [0]. But we DON'T want
     *  someone to use a ridiculous index of 1,000 when there are only
     *  5 functions or something. So to prevent that, we allocate how many
     *  holes are allowed to be in our lookup table. **/
    private int _maxNumberOfHolesAllowedInArray  = 20;
    
    /** Flagged to true after class has been built. **/
    private boolean _hasBeenBuilt = false;
    
    /** Protect against adding the same class twice. **/
    private HashMap<Class,Boolean> _collisionInsurance = 
                                                   new HashMap<Class,Boolean>();
    
    /** Master list of all classes that have been added to lookup table. **/
    private List<Class> _classTable = new ArrayList<Class>();
    
    /** Master lookup table we are building. 
        Null until we know how much space we need to allocate to it.**/
    private IndexedFunctionTableEntry[] _lookupTable = null;
                                    
    /** Add a class to scan and add to the lookup table. **/
    public void addClass(Class clazz){
        
        if(_hasBeenBuilt){
            doError("[Cannot add class. Already built.]");
        }//ERROR?
        
        //Make sure we are not adding the same class twice:
        addClassToCollisionRegistry(clazz);
        _classTable.add(clazz);
  
        //WRONG!! We are indexing METHODS, not fields...
        //List<Field> fields;
        //fields = ReflectionHelperUtil.getFieldsWithAnnotation
        //                 (clazz, IndexedFunction.class,GET_STATIC,GET_INSTANCE);
        
        List<Method> methods;
        methods = ReflectionHelperUtil.getMethodsAnnotatedWith
                                                 (clazz, IndexedFunction.class);
        
        //This signifies an error in our setup, so throw error if happens.
        //Do not allow scanning of class that contains no valid information:
        if(methods.size() <= 0){
            String msg = "";
            msg+="[You added a class for scanning that contained nothing]";
            msg+="Class:[" + clazz.getCanonicalName() + "]";
            doError(msg);
        }//Error?
        
        /** index to put function at. **/
        short putDex;
        
        //Collect stats for all of the annotated fields:
        for(Method m : methods){
            _totalNumberOfFunctionsFoundToIndex++;
            
            putDex = getMethodsLookupIndex(m);
            if(putDex < 0){doError("lookup table cannot use negative index");}
            if(putDex > _maxIndexFoundWhileAddingClasses){
                _maxIndexFoundWhileAddingClasses = putDex;
            }//find max.
            
        }//Next annotation of proper type.
     
    }//FUNC::END
    
    /** Adds the class to the collision registry.
     *  Throws error if trying to add class twice. **/
    private void addClassToCollisionRegistry(Class clazz){
        if(_collisionInsurance.containsKey(clazz)){
            doError("[Attempting to add class more than once!]");
        }//Error?
        _collisionInsurance.put(clazz, true);
    }//FUNC::END
    
    /* delete later.
    ///
     * @param f: A field annotated with @IndexedFunction
     * @return : The index it wants to have within the lookup table.
     //
    private short getFieldsLookupIndex(Field f){
        //Find the index this wants to belong at:
        Annotation ann        = f.getAnnotation(IndexedFunction.class);
        IndexedFunction iFunc = (IndexedFunction)ann;
        short lookupIndex     = iFunc.key();
        return lookupIndex;
    }//FUNC::END
    */
    
    /**
     * @param m: A METHOD annotated with @IndexedFunction
     * @return : The index it wants to have within the lookup table.
     */
    private short getMethodsLookupIndex(Method m){
        //Find the index this wants to belong at:
        Annotation ann        = m.getAnnotation(IndexedFunction.class);
        IndexedFunction iFunc = (IndexedFunction)ann;
        short lookupIndex     = iFunc.key();
        return lookupIndex;
    }//FUNC::END
    
    /** It is going to be easier to scan and index after figuring out the
     *  highest value. That way we can allocate an ARRAY instead of an array
     *  list. Also, checking for collisions will be easier this way.
     */
    public void build(){
        
        if(_hasBeenBuilt){
            doError("[cannot build more than once!]");
        }//Error?
        
        throwErrorIfTooManyHolesWillBeInLookupTable();
        allocateLookupTableSpace();
        populateLookupTable();
        
        //Kill the hash map. Reliquish those resources.
        _collisionInsurance.clear();
        _collisionInsurance = null;
        _hasBeenBuilt = true;
    }//FUNC::END
    
    /** Serializes lookup table for debug printout. **/
    private String serializeLookupTable(){
        String lenStr = Integer.toString(_lookupTable.length);
        int maxValidIndex = _lookupTable.length -1 ;
        IndexedFunctionTableEntry entry;
        String msg = "";
        msg += "Length:[" + lenStr + "]";
        msg += "Values:[";
        for(int i = 0; i <= maxValidIndex; i++){
            entry = _lookupTable[i];
            msg += "#" + Integer.toString(i) + ":";
            msg += IndexedFunctionTableEntry.printForDebug( entry );
            if(i < maxValidIndex){
                msg+= ",";
            }//add comma?
        }//next i (next entry)
        
        return msg;
    }//FUNC::END
    
    /** Makes sure index you are using is valid. As well as makes sure
     *  that system is properly setup to be able to use index.
     * @param dex :The index of the function we want.
     */
    private void validateFetchingIndexWeAreAboutToUse(short dex){
        if(false == _hasBeenBuilt){
            doError("[cannot use function until setup and built.]");
        }//ERROR?
        
        //Check inputs for validity:
        if(dex<0){
            doError("inputted index should be >= 0.");
        }//ERROR?
        
        if(null == _lookupTable){
            doError("The entire lookup table is null");
        }//Uh oh...
        
        //if the index is out of bounds, do an error, but have the error
        //dump out the contents of the table for you:
        if(dex >= _lookupTable.length){
            String msg = "";
            msg += "[We are out of bounds. Serialize the table for debug]";
            msg += serializeLookupTable();
            msg += "[Serialized lookup table END]";
            doError(msg);
        }//ERROR, OUT OF BOUNDS?
    }//FUNC::END
    
    private IndexedFunctionTableEntry getContainer(short dex){
        validateFetchingIndexWeAreAboutToUse(dex);
        
        //Get everything you need to find function:
        IndexedFunctionTableEntry ent = _lookupTable[dex];
        if(null == ent){
            //This is why I'd prefer to keep as little holes in the
            //lookup table as possible.
            doError("[Index could not be resolved to a function.]");
        }//ERROR?
        
        return ent;
    }//FUNC::END
    
    public boolean getIsMethodStaticByIndex(short dex, Class[] paramTypes){
        IndexedFunctionTableEntry ent = getContainer(dex);
        return ent.isStatic;
    }//FUNC::END
    
    public Method getMethodByIndex(short dex, Class[] paramTypes){
        
        IndexedFunctionTableEntry ent = getContainer(dex);
        
        //make sure we do not have null param types:
        validateParamTypeArray(paramTypes);
        
        //Get the method:
        Method m = null;
        if(null == ent.clazz){doError("[entry stored null class ref somehow]");}
        if(null == ent.funcName){doError("[funcName is null]");}
        if("".equals(ent.funcName)){doError("[emptystring is bad funcname]");}
        try{//TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
            //m = ent.clazz.getDeclaredMethod(ent.funcName, paramTypes);
            
            if(paramTypes.length != 2){
                doError("[HACK: Only supporting 2 args. AlterCode?]");
            }//CHECK.
            
            //http://stackoverflow.com/questions/160970/
            //Think mkyong was wrong on this one and have to use positional
            //arguments. We will have to test to know for sure.
            m = ent.clazz.getDeclaredMethod(ent.funcName, paramTypes[0], 
                                                          paramTypes[1]);
     
        }catch(Exception exep){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            doError("[Method did not exist. Perhaps bad signature given?]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //Make absolutely sure method is NOT null:
        if(null==m){doError("[Method is null for some reason]");}
        
        //Return the method:
        return m;
    }//FUNC::END
    
    private static void validateParamTypeArray(Class[] paramTypes){
        if(null == paramTypes){doError("null paramTypes");}
        for(int i = 0; i < paramTypes.length; i++){
            if(null==paramTypes[i]){
                doError("NULL PARAM IN paramTypes[]");
            }//null?
        }//next i.
    }//FUNC::END
    
    /** Allocates enough slots to build the lookup table with all of the
     *  annotated functions that have been scanned. **/
    private void allocateLookupTableSpace(){
        int len      = _maxIndexFoundWhileAddingClasses+1;
        _lookupTable = new IndexedFunctionTableEntry[len];
    }//FUNC::END
    
    /** Scans through all of the classes that have been added and packs
     *  their registered functions into the lookup table. **/
    private void populateLookupTable(){
        
        if(_classTable.size() <= 0){
            String msg = "";
            msg += "[Trying to populate lookup table with empty class table]";
            msg += "[Possible errors:]";
            msg += "[1. Using build() function before adding any classes.]";
            msg += "[2. Error in this class's code.]";
            doError(msg);
        }//NOTHING TO USE!
        
        //Iterate through all classes:
        for(Class clazz : _classTable){
            scanClassAndMakeTableEntries(clazz);
        }//Next registered class.

    }//FUNC::END
    
    /** Scan a single class that has been registered and register all of
     *  it's annotated functions with this lookup table.
     * @param clazz :The class to make lookup table entries for. **/
    private void scanClassAndMakeTableEntries(Class clazz){
        IndexedFunctionTableEntry entry;
        
        /** Get all fields annotated with @IndexedFunction **/
        //List<Field> fields;
        //fields = ReflectionHelperUtil.getFieldsWithAnnotation
        //                 (clazz, IndexedFunction.class,GET_STATIC,GET_INSTANCE);
        
        List<Method> methods;
        methods = ReflectionHelperUtil.getMethodsAnnotatedWith
                                                 (clazz, IndexedFunction.class);
        
        
        /** index to put function at. **/
        short putDex;
        
        //We cannot allow this. It is indicative of error in setup and
        //should be strictly enforced that it is now allowed.
        if(methods.size() <= 0){
            String msg = "";
            msg +="Nothing to scan for this class!";
            msg +="Class:[" + clazz.getCanonicalName() + "]";
            doError(msg);
        }//Nothing in the class for you!
        
        //Collect stats for all of the annotated fields:
        for(Method m : methods){
            
            //Create entry:
            entry          = new IndexedFunctionTableEntry();
            entry.clazz    = clazz;
            entry.funcName = m.getName();
            entry.isStatic = ReflectionHelperUtil.getIsMethodStatic(m);
          
            //Get where to put entry.
            //And then put it into that position with the lookup table:
            putDex = getMethodsLookupIndex(m);
            if(putDex < 0){doError("lookup table cannot use negative index");}
            
            //Put entry where it belongs:
            _lookupTable[putDex] = entry;
            
        }//Next annotation of proper type.

    }//FUNC::END
    
    /** Will throw error if too many holes will be in the lookup table.
     *  This is to prevent someone from giving a function a ridiculous
     *  index of 2,000 when there are only ten(10) functions to register
     *  with the lookup table. **/
    private void throwErrorIfTooManyHolesWillBeInLookupTable(){
        
        //Max index + 1 == total number of allocated slots needed.
        int totalSlotsNeeded = _maxIndexFoundWhileAddingClasses + 1;
        
        if(totalSlotsNeeded < _totalNumberOfFunctionsFoundToIndex){
            doError("[We are reporting we need less that what is found.]");
        }//ERROR?
        
        int numUnAllocatedSlots = 
                         totalSlotsNeeded - _totalNumberOfFunctionsFoundToIndex;
        
        //We want to avoid un-allocated slots in our lookup table:
        if(numUnAllocatedSlots > _maxNumberOfHolesAllowedInArray){
            String msg = "[Too many holes in your lookup table!]";
            msg += "[try to use uniform enums, starting from 1 or 0.]";
            doError(msg);
        }//ERROR?
                  
    }//FUNC::END
    
    /** validates that build went properly. **/
    public void validateBuild(){
        if(null == _lookupTable){
            doError("_lookupTable is null!");
        }//
        
        if(_lookupTable.length <= 0){
            doError("_lookupTable is EMPTY!");
        }//
        
        //IGNORE COMPILER ON "advanced loop".
        //I can't be certain that such loop will not IGNORE the null entries.
        //And I want to count null entires.
        IndexedFunctionTableEntry entry;
        int holesFound = 0;
        int functionsFound = 0;
        for(int i = 0; i < _lookupTable.length; i++){
            entry = _lookupTable[i];
            if(null == entry){
                holesFound++;
            }else{
                functionsFound++;
                IndexedFunctionTableEntry.validateEntry( _lookupTable[i] );
            }
        }//next i.
        
        if(holesFound > _maxNumberOfHolesAllowedInArray){
            doError("too many holes in your lookup table");
        }//too holy.
        
        if(functionsFound <= 0){
            doError("Function table should contain at least one function");
        }//no functions.
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = IndexedFunctionTable.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
