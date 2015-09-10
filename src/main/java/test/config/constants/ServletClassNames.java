/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.config.constants;

import test.MyError;
import test.servlets.rest.NinjaRestService;
import test.servlets.rest.TokenRestService;
import test.servlets.rest.riddleRhyme.RiddleRhymeRestService;

/**
 * First off.... I would like to say... I am not 100% happy with this monstrosity.
 * There is probably a better way to do this.
 * 
 * In short, what this does:
 * 1. Makes refactoring of URLS to REST WEB-SERVICE APIs less prone to breakage.
 * 2. This class is also PART of what will make our api table possible.
 * 3. Finds servlet mapping collisions BEFORE Jersey has opportunity to
 *    throw you a cryptic and confusing error.
 * 
 * In an effort to make mapping easier to refactor, we are going to put a
 * master list of servlet class names here. These names will be used to build
 * our api table that keeps track of all of the URLS for our different 
 * rest-apis.
 * @author jmadison
 */
public class ServletClassNames {
    
    //There must be a better way!
    //TOKEN SERVICE:
    public static final String TokenRestService_CLASSNAME = "TokenRestService";
    public static final String TokenRestService_MAPPING   = "TokenRestService/";
    
    //NINJA SERVICE:
    public static final String NinjaRestService_CLASSNAME = "NinjaRestService";
    public static final String NinjaRestService_MAPPING   = "NinjaRestService/";
    
    //RIDDLERHYME SERVICE:
    public static final String RiddleRhymeRestService_CLASSNAME = 
                                                      "RiddleRhymeRestService";
    public static final String RiddleRhymeRestService_MAPPING   = 
                                                      "RiddleRhymeRestService/";
    
    //Static initializer.
    //I bundle it in a "doStaticInit()" function because
    //Most IDE's make it a PAIN to edit code within the
    //static-init block.
    static{doStaticInit();}
    
    //static initializer, to be invoked only once:
    private static void doStaticInit(){
        verifyCorrectMapping();
    }
    
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
        if( notEQ(TokenRestService_CLASSNAME, TokenRestService.class.getSimpleName()))
        {  mError(TokenRestService_CLASSNAME); }
        
        if( notEQ(NinjaRestService_CLASSNAME, NinjaRestService.class.getSimpleName()))
        {  mError(NinjaRestService_CLASSNAME); }
        
        if( notEQ(RiddleRhymeRestService_CLASSNAME, RiddleRhymeRestService.class.getSimpleName()))
        {  mError(RiddleRhymeRestService_CLASSNAME); }
        
        
    }//FUNC::END
    
    private static void verifyCorrectMapping_MAPPING(){
        checkMapping(TokenRestService_CLASSNAME,
                     TokenRestService_MAPPING);
        
        checkMapping(NinjaRestService_CLASSNAME,
                     NinjaRestService_MAPPING);
        
        checkMapping(RiddleRhymeRestService_CLASSNAME,
                     RiddleRhymeRestService_MAPPING);
                        
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
    private static void mError(String className){
        String msg = "";
        msg += "Mapping is incorrect for className:[" + className + "]";
        throw new MyError(msg);
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
