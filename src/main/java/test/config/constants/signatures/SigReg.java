package test.config.constants.signatures;

import test.config.constants.signatures.sig.UserName_PassWord;

/**
 * Signature registry for different signature types that exist in the rest
 * services.
 * 
 * Take a page out of the book from relational databases and make one
 * signature object for each unique signature that exists. These signatures
 * tell you what arguments a given rest call takes.
 * 
 * @author jmadison
 */
public class SigReg {
    public static UserName_PassWord USERNAME_PASSWORD = new UserName_PassWord();
}//CLASS::END
