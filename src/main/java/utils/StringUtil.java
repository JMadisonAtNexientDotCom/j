package utils;

import test.MyError;

/**
 *
 * @author jmadison
 */
public class StringUtil {
    
    
    static{////////////////
        doStaticInit();
    }/////////////////////
    
    private static void doStaticInit(){
        
        //Can you force a unit test on first use of utility?
        unitTest();
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
        
        boolean hasFoundNewLine = false;
        int dexOfLastNewLineCharFound = (-1);
        int prvDex;
        int curDex = index-1; //minus one for easy loop entry.
        char curChar;
        while(true){
            curDex++; //first used value should == index parameter. //
            
            //BAIL-OUT CHECK:
            if(curDex > mi){break;} //break out of loops if out of bounds.
            
            curChar = text.charAt(curDex);
            if(isNewLineChar(curChar)){
                dexOfLastNewLineCharFound = curDex;
                //If this is the first newLine found, we want to
                //check to make sure no newline is BEHIND US.
                if(false == hasFoundNewLine){
                   prvDex = curDex--;
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
                           break; //exit loop.
                       }//IF::END
                   }//IF::END
                   hasFoundNewLine = true;
                }//FIRST FOUND
            }else{
                break; //if not on a newline character. EXIT LOOP.
            }//BLOCK::END
        }//INF LOOP
        
        //hasFoundNewLine will be flagged to FALSE if found in middle
        //of a string of newlines. Hence why must have hasFoundNewLine==true
        //to return found index.
        int op = (true==hasFoundNewLine ? dexOfLastNewLineCharFound : (-1) );
        return op;
        
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
    
    public static String getIndents(int indentationLevel){
        String tab = "\t";
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
        throw new MyError(clazz, err);
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
