package utils;

/**
 *
 * @author jmadison
 */
public class StringUtil {
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
}//CLASS::END
