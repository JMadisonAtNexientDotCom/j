package utils;

import app.MyError;

/**
 *
 * @author jmadison
 */
public class StringUtil {
    
    public static int javascript_numspaces_per_tab = 2;
    public static int javascript_tabs_per_indent   = 1;
    
    static{////////////////
        doStaticInit();
    }/////////////////////
    
    private static void doStaticInit(){
        
        //Can you force a unit test on first use of utility?
        unitTest();
    }//FUNC::END
    
    /**
     * Returns true if the string ends with the substring provided.
     * @param str :String to check for substring at end.
     * @param sub :The substring we are looking for.
     * @return :Returns true if substring found at exact end. **/
    public static boolean endsWith(String str, String sub){
        
        int str_len = str.length();
        int sub_len = sub.length();
        
        //Cant possibly exist is substring is larger than main string:
        if(sub_len > str_len){return false;}
        
        int str_dex = str_len;
        int sub_dex = sub_len;
        while(true){
            str_dex--; //first value used should be last index of str.
            sub_dex--; //first value used should be last index of sub.
            if(sub_dex < 0){break;} //<--entire substring compared.
            if(str_dex < 0){doError("Should have been caught earlier.");}
            
            char c0 = str.charAt(str_dex);
            char c1 = str.charAt(sub_dex);
            if(c0!=c1){return false;}
        }//INF LOOP
        
        //If no mismatch found, return true.
        return true;
        
    }//FUNC::END
    
    /** Trims leading and trailing underscores. **/
    public static String trimUnderscores(String str){
        int p0 = getRunExclusionPoint_head(str,'_');
        int p1 = getRunExclusionPoint_tail(str,'_');
        return inclusiveSubString(str,p0,p1);
    }//FUNC::END
    
    /**
     * A sub-string method that INCLUDES the characters at defined start
     * and end points. Differs from default implementation of substring in java.
     * @param str:The string you want substring of.
     * @param p0 :The start index.
     * @param p1 :The end index.
     * @return   :See above. **/
    public static String inclusiveSubString(String str, int p0, int p1){
        String op = "";
        if(p0 > p1){doError("points out of order");}
        for(int i = p0; i <= p1; i++){
            op += Character.toString( str.charAt(i) );
        }//next i
        return op;
    }//FUNC::END
    
    /** Returns the index of the first NON run character on HEAD. (Leading)
     *  Example: if: str == "___HEY" and run = "_"
     *  Will return 3, for position of "H".
     * @param str :String with a run-length sequence on it.
     * @param run :The character that defines the run.
     * @return :The first character that is not part of the run. **/
    private static int getRunExclusionPoint_head(String str, char run){
        
        int max_index = str.length()-1;
        for(int i = 0; i <= max_index; i++){ //FORWARDS
            if(str.charAt(i) != run){return i;}
        }//next i
        
        doError("[String is 100% run.]");
        return(-1); //<--this line should never execute.
    }//FUNC::END
    
    /** Returns the index of the first NON run character on TAIL. (Trailing)
     *  Example: if: str == "HEY___" and run = "_"
     *  Will return 2, for position of "Y".
     * @param str :String with a run-length sequence on it.
     * @param run :The character that defines the run.
     * @return :The first character that is not part of the run. **/
    private static int getRunExclusionPoint_tail(String str, char run){
        int max_index = str.length()-1;
        for(int i = max_index; i >= 0; i--){ //BACKWARDS.
            if(str.charAt(i) != run){return i;}
        }//next i
        
        doError("[String is 100% run.]");
        return (-1); //<--this line should never execute.
    }//FUNC::END
    
    
    public static boolean canBeParsedAsWholeNumber(String str){
        int len = str.length();
        if(len <= 0){return false;}
        
        Character cur;
        for(int i = 0; i < len; i++){
            cur = str.charAt(i);
            if(true == Character.isAlphabetic(cur)){
                return false; //NOT NUMBER!
            }//RAWER
        }//NEXT i
        
        //if all characters were numbers, we will use it!
        return true;
    }//FUNC::END
    
    /**
     * Before the first character, and then immediately after each
     * NEW-LINE found, will add a tab character.
     * @param text
     * @param indentationLevel
     * @return 
     */
    public static String indentBeginningOfLines
                                            (String text, int indentationLevel){
        String ind = getIndents(indentationLevel);
        String outText = "";
        outText += ind; //first line indented no matter what.
        
     
        int maxValidIndex = text.length() - 1; //max valid index of text.
        int lastIndexOfNewLine;
        char curChar;
        for(int i = 0; i <= maxValidIndex; i++){
            lastIndexOfNewLine = isStartOfNewLineHere(text,i);
            if(lastIndexOfNewLine >= 0){ //<--never executing!!!!
                //outText += "XXXX"; //DEBUG TEST.
                outText += System.lineSeparator();
                outText += ind; //add indentation after the end of line.
                i = lastIndexOfNewLine; //skip any of the characters of
                                        //the newline sequence.
            }else{
                curChar = text.charAt(i);
                if(isNewLineChar(curChar)){
                    outText += "[BUG! Should have been caught!!]";
                }else{
                    outText += String.valueOf( text.charAt(i) );  
                }//end
                
            }//END
        }//NEXT i
        
        return outText;
        
    }//FUNC::END
                    
    /** If TRUE, returns the index of the LAST character making up the newline.
     *  That way, we can support both NL and (CR+NL) newline styles.
     * @param text  :The ext to search for newline character.
     * @param index :The index at which the newline must START.
     *               AKA: First character of newline sequence.
     * @return :Returns >= 0 if true. Negative if false.
     */
    public static int isStartOfNewLineHere(String text, int index){
        
        if(index < 0){doError("[<0 index supplied to isStartOfNewLineHere]");}
        int mi = text.length() - 1; //max index. (Last valid index of string)
        if(index > mi){doError("[index supplied >(GT) max valid index]");}
        
        boolean shouldHaveBrokenOut = false; //for debug.
        boolean hasFoundNewLine = false;
        int dexOfLastNewLineCharFound = (-888);
        int prvDex;
        int curDex = index-1; //minus one for easy loop entry.
        char curChar;
        while(true){
            if(true==shouldHaveBrokenOut){
                doError("[Break did not work as expected.]");
            }//Should have broken out?
            
            curDex++; //first used value should == index parameter. //
            
            //BAIL-OUT CHECK:
            if(curDex > mi){break;} //break out of loops if out of bounds.
            
            curChar = text.charAt(curDex);
            if(isNewLineChar(curChar)){
                dexOfLastNewLineCharFound = curDex;
                //If this is the first newLine found, we want to
                //check to make sure no newline is BEHIND US.
                if(false == hasFoundNewLine){
                   prvDex = curDex - 1; //BUG-FIX: make prv without change cur.
                   if(prvDex >= 0){
                       char characterBehindCurrent;
                       characterBehindCurrent = text.charAt(prvDex);
                       if(isNewLineChar(characterBehindCurrent) ){
                           //The character BEHIND the first new-line
                           //character found was ALSO a new-line character.
                           //this signifies we are in the MIDDLE of a newline
                           //sequence. We will NOT insert indentation between
                           //this. Return false because we are NOT at the start
                           //of a newline sequence.
                           shouldHaveBrokenOut = true;
                           dexOfLastNewLineCharFound = (-777); //found behind.
                           break; //exit loop.
                       }//IF::END
                   }//IF::END
                   hasFoundNewLine = true;
                }//FIRST FOUND
            }else{
                shouldHaveBrokenOut = true;
                break; //if not on a newline character. EXIT LOOP.
            }//BLOCK::END
        }//INF LOOP
        
        //hasFoundNewLine will be flagged to FALSE if found in middle
        //of a string of newlines. Hence why must have hasFoundNewLine==true
        //to return found index.
        //int op = (true==hasFoundNewLine ? dexOfLastNewLineCharFound : (-1) );
        
        return dexOfLastNewLineCharFound;
        
    }//FUNC::END
    
    /**
     * (Carriage Return or New Line)
     * @param c :Character in question.
     * @return :Returns TRUE if the character is a newline character.
     */
    public static boolean isNewLineChar(char c){
        final char nl = "\n".charAt(0); //new line char.
        final char cr = "\r".charAt(0); //carriage return char.
        if(nl != 10){ doError("[nl not the ascii you thought.]");}
        if(cr != 13){ doError("[cr not the ascii you thought.]");}
        return(c == nl || c == cr);
    }//FUNC::END
    
    public static String numberOfTabsToSpaces(int numTabs){
        //Assemble string of SPACES:
        String spc = " ";
        String op = "";
        int len = numTabs;
        for(int i = 0; i < len; i++){
            op += spc;
        }//NEXT i
        return op;
    }//FUNC::END
    
    public static String getIndents(int indentationLevel){
        //String tab = "\t";
        String tab = numberOfTabsToSpaces(javascript_numspaces_per_tab);
        String op = "";
       
        //Assemble string of tabs:
        int len = indentationLevel;
        for(int i = 0; i < len; i++){
            op += tab;
        }//NEXT i
        
        //return the string of tabs:
        return op;
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = StringUtil.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
    public static boolean unitTest(){
        //test isStartOfNewLineHere
        String sysNL = System.lineSeparator();
        String s01 = "ABC" + sysNL + "123";
        int result;
        result = isStartOfNewLineHere(s01,3);
        if( result < 3){
           String msg = "";
           msg +="[UNIT TEST FAIL!]";
           msg +="TEST STRING==[" + s01 + "]";
           msg +="RESULT==[" + result + "]";
           doError(msg);
        }//END
        
        return true;
        
    }//FUNC::END
    
}//CLASS::END
