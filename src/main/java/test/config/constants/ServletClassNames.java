package test.config.constants;

import java.util.HashMap;
import java.util.Map;
import test.MyError;
import test.debug.GlobalErrorState;
import test.servlets.rest.restCore.AdminCTRL;
import test.servlets.rest.restCore.NinjaCTRL;
import test.servlets.rest.restCore.OwnerCTRL;
import test.servlets.rest.restCore.TokenCTRL;
import test.servlets.rest.restCore.TrialCTRL;
import test.servlets.rest.other.debug.TransDebugCTRL;
import test.servlets.rest.restCore.RiddleRhymeCTRL;
import test.servlets.rest.other.utilityServlets.FileContentFetcher;
import test.servlets.rest.restCore.ChalkCTRL;
import test.servlets.rest.restCore.CuecardCTRL;
import test.servlets.rest.restCore.DeckCTRL;
import test.servlets.rest.restCore.GroupCTRL;
import test.servlets.rest.restCore.InkCTRL;
import test.servlets.rest.restCore.KindaCTRL;
import test.servlets.rest.restCore.QuarCTRL;
import test.servlets.rest.restCore.SlateCTRL;
import test.servlets.rest.restCore.StackCTRL;
import test.servlets.rest.restCore.TwineCTRL;
import test.servlets.rest.restCore.LoftCTRL;
//345678901234567890123456789012345678901234567890123456789012345678901234567890
/**##########################CLASS HEADER FILE##################################
//WHAT THIS CLASS DOES:
// 1. Makes refactoring of URLS to REST WEB-SERVICE APIs less prone to breakage.
//     In an effort to make mapping easier to refactor, we are going to put a
//     master list of servlet class names here. These names will be used to 
//     build our api table that keeps track of all of the URLS for our
//     different rest-apis.
// 2. This class is also PART of what will make our api table possible.
// 3. Finds servlet mapping collisions BEFORE Jersey has opportunity to
//    throw you a cryptic and confusing error.
// 
//ORIGINAL USE CASE:
//I noticed inconsisencies in the url used to call my webservice and
//the method name in the actual servlet. Wanted to force them to be consisent
//so that we would.
//1. Have less to remember.
//2. Front-End + Back-End people will use same termonology for things.
//3. I would eventually like to enforce the same strictness of the
//   parameters of HTTP-GET requests.
//
//DESIGN NOTE (Justifications for why things are the way they are):
//I am not 100% happy with this monstrosity.
//There is probably a better way to do this.
//
//@author JMadison : 2015.??.??_????AMPM -don't remember when I first made this.
//@author JMadison : 2015.09.22_1020AM   -updating header.
//@author XXXXXXXX : 20XX.XX.XX_####AMPM 
//@author XXXXXXXX : 20XX.XX.XX_####AMPM
########10########20########30########40########50########60########70########*/
//-------0---------0---------0---------0---------0---------0---------0---------0
public class ServletClassNames {
    
    //There must be a better way!
    
    //SWT == "stack,wild,twine" these controllers were added in order to test
    //the tables that will give us the ability to dynamically generate
    //[trial/tests] 5 [riddles/questions] at a time. ADDED:2015.11.02(Nov11,2nd)
    //SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-//
    //STACK SERVICE:
    public static final String StackCTRL_CLASSNAME = "StackCTRL";
    public static final String StackCTRL_MAPPING   = "StackCTRL/";
    
    //WILDC SERVICE:
    public static final String LoftCTRL_CLASSNAME = "LoftCTRL";
    public static final String LoftCTRL_MAPPING   = "LoftCTRL/";
    
    //TWINE SERVICE:
    public static final String TwineCTRL_CLASSNAME = "TwineCTRL";
    public static final String TwineCTRL_MAPPING   = "TwineCTRL/";
    //SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-SWT-//
    
    //KINDA SERVICE: Joins tests+responses into graded package.
    public static final String KindaCTRL_CLASSNAME = "KindaCTRL";
    public static final String KindaCTRL_MAPPING   = "KindaCTRL/";
    
    //Quar,Slate,Chalk tables are what is necessary to persist
    //the structure of a complete response to a Riddle-Trial (test):
    //-------------------------------------------------------------------------+
    //QUAR SERVICE:
    public static final String QuarCTRL_CLASSNAME = "QuarCTRL";
    public static final String QuarCTRL_MAPPING   = "QuarCTRL/";
    
    //SLATE SERVICE:Deck
    public static final String SlateCTRL_CLASSNAME = "SlateCTRL";
    public static final String SlateCTRL_MAPPING   = "SlateCTRL/";
    
    //CHALK SERVICE:Deck
    public static final String ChalkCTRL_CLASSNAME = "ChalkCTRL";
    public static final String ChalkCTRL_MAPPING   = "ChalkCTRL/";
    //-------------------------------------------------------------------------+
    
    //Deck,Cuecard,Ink tables are what is necessary to persist
    //the structure of a generated Riddle-Trial (test):
    //-------------------------------------------------------------------------+
    //DECK SERVICE:
    public static final String DeckCTRL_CLASSNAME    = "DeckCTRL";
    public static final String DeckCTRL_MAPPING      = "DeckCTRL/";
    
    //CUECARD SERVICE:
    public static final String CuecardCTRL_CLASSNAME = "CuecardCTRL";
    public static final String CuecardCTRL_MAPPING   = "CuecardCTRL/";
    
    //INK SERVICE:
    public static final String InkCTRL_CLASSNAME     = "InkCTRL";
    public static final String InkCTRL_MAPPING       = "InkCTRL/";
    //-------------------------------------------------------------------------+
    
    //GROUP SERVICE:
    public static final String GroupCTRL_CLASSNAME = "GroupCTRL";
    public static final String GroupCTRL_MAPPING   = "GroupCTRL/";
    
    //TOKEN SERVICE:
    public static final String TokenCTRL_CLASSNAME = "TokenCTRL";
    public static final String TokenCTRL_MAPPING   = "TokenCTRL/";
    
    //TRIAL SERVICE:
    public static final String TrialCTRL_CLASSNAME = "TrialCTRL";
    public static final String TrialCTRL_MAPPING   = "TrialCTRL/";
    
    //NINJA SERVICE:
    public static final String NinjaCTRL_CLASSNAME = "NinjaCTRL";
    public static final String NinjaCTRL_MAPPING   = "NinjaCTRL/";
    
    //RIDDLERHYME SERVICE:
    public static final String RiddleRhymeCTRL_CLASSNAME = 
                                                      "RiddleRhymeCTRL";
    public static final String RiddleRhymeCTRL_MAPPING   = 
                                                      "RiddleRhymeCTRL/";
    
    //TRANSACTION DEBUGGER SERVICE:
    public static final String TransDebugCTRL_CLASSNAME = 
                                                       "TransDebugCTRL";
    public static final String TransDebugCTRL_MAPPING = 
                                                       "TransDebugCTRL/";
    
    //ADMIN SERVICE:
    public static final String AdminCTRL_CLASSNAME = "AdminCTRL";
    public static final String AdminCTRL_MAPPING   = "AdminCTRL/";
    
    //OWNER SERVICE: (for the token ownership table known as "owner_table" )
    public static final String OwnerCTRL_CLASSNAME = "OwnerCTRL";
    public static final String OwnerCTRL_MAPPING   = "OwnerCTRL/"; 
    
    //Originally made so that .JSP files can inject .HTML and .CSS files
    //on the server into themselves. Originally so we could use JSP
    //expressions like <%= I.INCLUDE_CSS %> to include all the CSS libs
    //we need.
    public static final String FileContentFetcher_CLASSNAME = 
                                                          "FileContentFetcher";
    public static final String FileContentFetcher_MAPPING = 
                                                          "FileContentFetcher/";
    
    /**-------------------------------------------------------------------------
     *  Keeps track of how many times was called. Possible that
     *  we may end up with errors if this error check utility is
     *  NOT synchronized.
     ------------------------------------------------------------------------**/
    private static int _times_called_CHECK_CLASSNAME = 0;
    
    /**-------------------------------------------------------------------------
     *  Keeps track of how many times was called. Possible that
     *  we may end up with errors if this error check utility is
     *  NOT synchronized.
     ------------------------------------------------------------------------**/
    private static int _times_called_CHECK_MAPPING   = 0;
    
    /** used to make sure every time we check the mapping for CLASSNAME,
     *  the first arguments for all the cumulative calls are all UNIQUE.
     *  AKA: If we check mapping for "DOG" more than once, then we have error.
        Multiple identical inputs are indicator that we mapped something wrong.
    -------------------------------------------------------------------------**/
    private static final Map<String,Integer> _vcm_dict_CLASSNAME_fromVar
                                                = new HashMap<String,Integer>();
    
    /** used to make sure every time we check the mapping for CLASSNAME,
     *  the 2ND arguments for all the cumulative calls are all UNIQUE.
     *  AKA: If we check mapping for "DOG" more than once, then we have error. 
        Multiple identical inputs are indicator that we mapped something wrong.
    -------------------------------------------------------------------------**/
    private static final Map<String,Integer> _vcm_dict_CLASSNAME_fromReflect 
                                                = new HashMap<String,Integer>();
    
    //Static initializer.
    //I bundle it in a "doStaticInit()" function because
    //Most IDE's make it a PAIN to edit code within the
    //static-init block.
    static{doStaticInit();}
    
    //static initializer, to be invoked only once:
    private static void doStaticInit(){
        verifyCorrectMapping();
    }
    
    
    
    /**
     * Get the number of unique apis that are mapped in this class.
     * @return :# of unique apis.
     * 
     * NOTE: implimentation is kind of hackish. Expects that any API
     *       represented in here has also had it's mapping checked.
     * 
     */
    public static int getNumberOfMappings(){
        verifyMappingChecksWereEvenlyPaired();
        return _times_called_CHECK_MAPPING;
    }//FUNC::END
    
    private static void verifyCorrectMapping(){
        
        //Verify that no two servlet names are identical.
        //I doubt this error check will ever find an error, since the
        //Names used are class names. 
        //And if there was a collision due to bad mapping, 
        //verifyCorrectMapping_CLASSNAME or verifyCorrectMapping_MAPPING
        //would find it first...
        //Oh... There is your answer. To get the most benifit out of this
        //error check, put BEFORE _CLASSNAME and _MAPPING error checks.
        verifyAllNamesAreUnique();
        
        //Verify that _CLASSNAME vars have same name 
        //as the classes they are standing in for.
        verifyCorrectMapping_CLASSNAME();
        
        //Verify all _MAPPING vars are simply the _CLASSNAME + "/" character.
        verifyCorrectMapping_MAPPING();
        
        //Make sure verifications of CLASSNAME and MAPPING resulted in
        //evenly paired calls. If this does not happen it signifies:
        //1: Possible improperly set-up error checks.
        //2: non-synchronized access to debuggin utility.
        verifyMappingChecksWereEvenlyPaired();
      
    }//FUNC::END
    
    /** ------------ Throws error if this check fails. --------------------- **/
    private static void verifyMappingChecksWereEvenlyPaired(){
        int v0 = _times_called_CHECK_CLASSNAME;
        int v1 = _times_called_CHECK_MAPPING;
        if(v0 != v1){//////////////////////////////////////////////////////
            Class clazz = ServletClassNames.class;
            String eMsg = "[mapping checks were not evenly paired.]";
            throw MyError.make(clazz, eMsg);
        }//////////////////////////////////////////////////////////////////
    }//FUNC::END
    
    /** If any constant is identical to any other constant,
     *  we will throw an error. As this means there is bad
     *  mapping configuration in this class. **/
    private static void verifyAllNamesAreUnique(){
        
        //TODO: The actual logic. Going to skip this for now
        //because it is a NICE TO HAVE. But not vital.
        //As the other error checking will find it.
        //Also, adding this just makes more code to maintain.
        
        //ArrayList<String> arr = new ArrayList<String>();
        //check(arr, TokenRestService)
    }//FUNC::END
    
    private static void verifyCorrectMapping_CLASSNAME(){
        
        //vcm_cn == "verify correct mapping _ class name"
        //but we wnat to keep it short to help keep line length down.
        
        /** The actual class name to compare with our mapping constants.--------
         *  to make sure they match! ----------------------------------------**/
        String clazName;
        
        clazName =         StackCTRL.class.getSimpleName();
        if(         vcm_cn(StackCTRL_CLASSNAME, clazName))
        {   doMappingError(StackCTRL_CLASSNAME); }
        
        clazName =         LoftCTRL.class.getSimpleName();
        if(         vcm_cn(LoftCTRL_CLASSNAME, clazName))
        {   doMappingError(LoftCTRL_CLASSNAME); }
        
        clazName =         TwineCTRL.class.getSimpleName();
        if(         vcm_cn(TwineCTRL_CLASSNAME, clazName))
        {   doMappingError(TwineCTRL_CLASSNAME); }
        
        clazName =         KindaCTRL.class.getSimpleName();
        if(         vcm_cn(KindaCTRL_CLASSNAME, clazName))
        {   doMappingError(KindaCTRL_CLASSNAME); }
        
        clazName =         QuarCTRL.class.getSimpleName();
        if(         vcm_cn(QuarCTRL_CLASSNAME, clazName))
        {   doMappingError(QuarCTRL_CLASSNAME); }
        
        clazName =         SlateCTRL.class.getSimpleName();
        if(         vcm_cn(SlateCTRL_CLASSNAME, clazName))
        {   doMappingError(SlateCTRL_CLASSNAME); }
        
        clazName =         ChalkCTRL.class.getSimpleName();
        if(         vcm_cn(ChalkCTRL_CLASSNAME, clazName))
        {   doMappingError(ChalkCTRL_CLASSNAME); }
        
        clazName =         DeckCTRL.class.getSimpleName();
        if(         vcm_cn(DeckCTRL_CLASSNAME, clazName))
        {   doMappingError(DeckCTRL_CLASSNAME); }
        
        clazName =         CuecardCTRL.class.getSimpleName();
        if(         vcm_cn(CuecardCTRL_CLASSNAME, clazName))
        {   doMappingError(CuecardCTRL_CLASSNAME); }
        
        clazName =         InkCTRL.class.getSimpleName();
        if(         vcm_cn(InkCTRL_CLASSNAME, clazName))
        {   doMappingError(InkCTRL_CLASSNAME); }
        
        clazName =         GroupCTRL.class.getSimpleName();
        if(         vcm_cn(GroupCTRL_CLASSNAME, clazName))
        {   doMappingError(GroupCTRL_CLASSNAME); }
        
        clazName =         TokenCTRL.class.getSimpleName();
        if(         vcm_cn(TokenCTRL_CLASSNAME, clazName))
        {   doMappingError(TokenCTRL_CLASSNAME); }
        
        clazName =         TrialCTRL.class.getSimpleName();
        if(         vcm_cn(TrialCTRL_CLASSNAME, clazName))
        {   doMappingError(TrialCTRL_CLASSNAME); }
        
        clazName =         NinjaCTRL.class.getSimpleName();
        if(         vcm_cn(NinjaCTRL_CLASSNAME, clazName))
        {   doMappingError(NinjaCTRL_CLASSNAME); }
        
        clazName =         RiddleRhymeCTRL.class.getSimpleName();
        if(         vcm_cn(RiddleRhymeCTRL_CLASSNAME, clazName))
        {   doMappingError(RiddleRhymeCTRL_CLASSNAME); }
        
        clazName =         TransDebugCTRL.class.getSimpleName();
        if(         vcm_cn(TransDebugCTRL_CLASSNAME, clazName))
        {   doMappingError(TransDebugCTRL_CLASSNAME); }
        
        clazName =         AdminCTRL.class.getSimpleName();
        if(         vcm_cn(AdminCTRL_CLASSNAME, clazName))
        {   doMappingError(AdminCTRL_CLASSNAME); }
        
        clazName =         OwnerCTRL.class.getSimpleName();
        if(         vcm_cn(OwnerCTRL_CLASSNAME, clazName))
        {   doMappingError(OwnerCTRL_CLASSNAME); }
        
        clazName =         FileContentFetcher.class.getSimpleName();
        if(         vcm_cn(FileContentFetcher_CLASSNAME, clazName))
        {   doMappingError(FileContentFetcher_CLASSNAME); }
          
    }//FUNC::END
    
    private static void verifyCorrectMapping_MAPPING(){
        
        checkMapping(StackCTRL_CLASSNAME,
                     StackCTRL_MAPPING);
        
        checkMapping(LoftCTRL_CLASSNAME,
                     LoftCTRL_MAPPING);
        
        checkMapping(TwineCTRL_CLASSNAME,
                     TwineCTRL_MAPPING);
        
        checkMapping(KindaCTRL_CLASSNAME,
                     KindaCTRL_MAPPING);
        
        checkMapping(QuarCTRL_CLASSNAME,
                     QuarCTRL_MAPPING);
        
        checkMapping(SlateCTRL_CLASSNAME,
                     SlateCTRL_MAPPING);
        
        checkMapping(ChalkCTRL_CLASSNAME,
                     ChalkCTRL_MAPPING);
        
        checkMapping(DeckCTRL_CLASSNAME,
                     DeckCTRL_MAPPING);
        
        checkMapping(CuecardCTRL_CLASSNAME,
                     CuecardCTRL_MAPPING);
        
        checkMapping(InkCTRL_CLASSNAME,
                     InkCTRL_MAPPING);
        
        checkMapping(GroupCTRL_CLASSNAME,
                     GroupCTRL_MAPPING);
        
        checkMapping(TokenCTRL_CLASSNAME,
                     TokenCTRL_MAPPING);
        
        checkMapping(TrialCTRL_CLASSNAME,
                     TrialCTRL_MAPPING);
        
        checkMapping(NinjaCTRL_CLASSNAME,
                     NinjaCTRL_MAPPING);
        
        checkMapping(RiddleRhymeCTRL_CLASSNAME,
                     RiddleRhymeCTRL_MAPPING);
        
        checkMapping(TransDebugCTRL_CLASSNAME,
                     TransDebugCTRL_MAPPING);
        
        checkMapping(AdminCTRL_CLASSNAME,
                     AdminCTRL_MAPPING);
        
        checkMapping(OwnerCTRL_CLASSNAME,
                     OwnerCTRL_MAPPING);
        
        checkMapping(FileContentFetcher_CLASSNAME,
                     FileContentFetcher_MAPPING);
                        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     *  Wrapper so we can count how many times we checked the
     *  classname mapping in verifyCorrectMapping_CLASSNAME function.
     *  Our CLASSNAME and MAPPING verifications should have identical
     *  number of calls in them when we are validating.
     * @param classNameFromVariable   :CLASSNAME as string.
     * @param classNameFromReflection :CLASSNAME as string.
     * @return   : results of notEQ
     ------------------------------------------------------------------------**/
    private static Boolean vcm_cn
                  (String classNameFromVariable,String classNameFromReflect){
        _times_called_CHECK_CLASSNAME++;
        
        //add classNameFromReflection into hashmap.
        //add classNameFromVariable into another hashmap.
        //Do this so we can make sure each class name is only checked ONCE.
        //Being checked more than once is indication of error:
        if(_vcm_dict_CLASSNAME_fromVar.containsKey(classNameFromVariable)){
           doTooManyChecksError_CLASSNAME_fromVar(classNameFromVariable);
        }//ERROR?
        
        if(_vcm_dict_CLASSNAME_fromReflect.containsKey(classNameFromReflect)){
           doTooManyChecksError_CLASSNAME_fromReflect(classNameFromReflect);
        }//ERROR?
        
        //Add both of the keys to dictionary:
        _vcm_dict_CLASSNAME_fromVar    .put(classNameFromVariable,1);
        _vcm_dict_CLASSNAME_fromReflect.put(classNameFromReflect,1);
        
        
        return notEQ(classNameFromVariable,classNameFromReflect);
    }//FUNC::END
    
    /** All _MAPPING vars should be _CLASSNAME + "/" 
     *  Enforcing this so it is easier to refactor REST API URLS. **/
    private static void checkMapping(String className, String mapping){
        if( notEQ(className + "/", mapping))
        {
            String msg = "";
            msg += "[MAPPING ERROR IN ServletClassNames.java]";
            msg += "[className:[" + className + "]]";
            msg += "[mapping  :[" + mapping + "]]";
        }
        
        _times_called_CHECK_MAPPING++;
        
    }//FUNC::END
    
    /** 
     *  mError == "mapping error"
     *  Function was originally called "ThrowMappingError" but broke convention
     *  because it is more important that variables line up in
     *  our verifyCorrectMapping_CLASSNAME function.
     * 
     *  Through error showing which class name has a constant variable
     *  that does not match the name of the class it is supposed to be
     *  associated with.
     * @param className :A string constant that should be IDENTICAL to the
     *                   name of the class.
     */
    private static void doMappingError(String className){
        String msg = "";
        msg += "Mapping is incorrect for className:[" + className + "]";
        //GlobalErrorState.addError(ServletClassNames.class, msg);
        throw MyError.make(ServletClassNames.class, msg);
    }//FUNC::END
    
    private static void doTooManyChecksError_CLASSNAME_fromVar
                                                             (String className){
        String msg = "";
        msg += "1ST ARG ERROR:";
        msg += "We found multiple classname mapping checks for the class:";
        msg += className;
        msg += "[within the FIRST ARGUMENT of our mapping checks.];";
        msg += "[this is indicative that there ]";
        msg += "[is a mapping mismatch in our setup.]";
        throw MyError.make(ServletClassNames.class, msg);
    }//FUNC::END
                                                             
    private static void doTooManyChecksError_CLASSNAME_fromReflect
                                                             (String className){
        String msg = "";
        msg += "2ND ARG ERROR:";
        msg += "We found multiple classname mapping checks for the class:";
        msg += className;
        msg += "[within the SECOND (2ND) ARGUMENT of our mapping checks.];";
        msg += "[this is indicative that there ]";
        msg += "[is a mapping mismatch in our setup.]";
        throw MyError.make(ServletClassNames.class,msg);
    }//FUNC::END
    
    /** Returns TRUE if the two strings compared are NOT equal. **/
    private static Boolean notEQ(String s0, String s1){
        if(s0.equals(s1)){ return false;}
        return true;
    }//FUNC::END
    
    //as much as I want to shorten line length...
    //Below is a bit too experimental for now. WIll take some research.
    //get code WORKING first.
    //
    ///** Code snippet to reduce line lengths in verifyCorrectMapping function. **/
    ///private static String getName(Class inClassYouWantToGetTheNameOf){
    //    inClassYouWantTheNameOf.class.getSimpleName())
    //}
    
}//CLASS::END
