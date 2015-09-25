/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.files;

import IDontKnowWhereToPutTheseClasses.ReaderContainer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author jmadison
 */
public class StreamToTextUtil {
    
    /** Converts an InputStream into Text. **/
    public static String inputStreamToText(InputStream stream){
        
        if(null == stream){
            String msg = "stream object was null. Is relativePath correct?";
            return makeMSG(msg);
        }//Null stream?
        
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(stream));
        String docText = "Was never set";
        try{
            String newLine = System.lineSeparator();
            //To convert to string, use a string builder:
            //http://stackoverflow.com/questions/4666748/
            //                            java-how-to-read-bufferedreader-faster
            //SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
            StringBuilder builder = new StringBuilder();
            String aux;
            try{
               while ((aux = reader.readLine()) != null) {
                    builder.append(aux);
                    builder.append(newLine); //get your line endings back.
               }//NEXT
               docText = builder.toString();
            }catch(IOException e){
                docText = "getJsonText Failed! e==" + e.toString();
            }//TRY::END
            //SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
        }catch(Exception e){
            return "String builder block failed. Exception:" + e.toString();
        }
        
        return docText;
    }//FUNC::END
    
    /** Makes text meant for a non-fatal error. Meaning, we want the error
     *  message to print out somewhere without crashing the program.
     * @param msg :Unique error message.
     * @return    :The msg with information associating it with this class
     *             so we can know where the message originated from when
     *             we see it in a string somewhere.
     */
    private static String makeMSG(String msg){
        String whereAreWe = StreamToTextUtil.class.getCanonicalName();
        String op = "ERROR AT:" + whereAreWe;
        op += msg;
        return op;
    }//FUNC::END
    
}//CLASS::END
