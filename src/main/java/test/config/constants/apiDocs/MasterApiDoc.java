package test.config.constants.apiDocs;

import frontEndBackEndIntegration.childComponents.ServiceURLRegistry;
import primitives.EndPoint;
import test.MyError;
import test.config.constants.ServiceUrlsInitializer;
import test.config.constants.ServletClassNames;
import test.config.constants.identifiers.FuncNameReg;
import test.config.constants.signatures.sig.CardCount_NumQuips_TruMin_TruMax_EndPoint;
import test.config.constants.signatures.sig.HttpPost_EndPoint;
import test.config.constants.signatures.sig.Id_EndPoint;
import test.config.constants.signatures.sig.Name_Phone_Email_PortfolioUrl_EndPoint;
import test.config.constants.signatures.sig.RiddleId_EndPoint;
import test.config.constants.signatures.sig.RiddleId_NumberOfChoices_NumberOfTruths_EndPoint;
import test.config.constants.signatures.sig.RiddleId_RhymeId_EndPoint;
import test.config.constants.signatures.sig.TokenId_AdminId_EndPoint;
import test.config.constants.signatures.sig.TokenId_EndPoint;
import test.config.constants.signatures.sig.TokenId_NinjaId_EndPoint;
import test.config.constants.signatures.sig.UserName_PassWord_EndPoint;
import test.config.constants.signatures.sig.Void_EndPoint;
import test.servlets.rest.AdminRestService;
import test.servlets.rest.NinjaRestService;
import test.servlets.rest.OwnerRestService;
import test.servlets.rest.TokenRestService;
import test.servlets.rest.riddleRhyme.RiddleRhymeRestService;

/**
 * 
 * QUICK_SUMMARY: ( [Q]="Quick" )
 * //[Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q]
 * 1. Gives UI programmers access to auto-complete for the services they want
 *    to call when programming their .JSP files.
 * 2. Binds FrontEnd+BackEnd together. So if back end changes the API, the
 *    .JSP files will automatically reflect that change.
 * //[Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q][Q]
 * 
 * TODO: There should be a way to check the endpoints against the actual
 *       servlet code to make sure everything checks out!
 * 
 * Experimented with a lot of different ways to access services and variable
 * names. There is definitely a balance that needs to be struck between
 * elegance and maintainability.
 * 
 * I would very much like it to be something like:
 * For api endpoints: <%= I.ADMINSERVICE.FUNCS.LOGIN_VALIDATE.ENDPOINT %>
 * For api variables: <%= I.ADMINSERVICE.FUNCS.LOGIN_VALIDATE.USER_NAME %>
 * However... Using dot (.) operators means you would need to make an analogous
 * small project of branching class files just to get access to this.
 * Super convinient. Asthetically pleasing. But not at all maintainable from
 * my attempts at doing it.
 * 
 * My thoughts: Get RID of the base service and only call the functions.
 * Then, use a flat file system. And instead of dot operators, use a combinations
 * of underscores and capitals vs lowercases.
 * 
 * Yucky, breaks a lot of clean-code rules. But is more maintainable and
 * less prone to error.
 * 
 * 
 * @author jmadison
 */
public class MasterApiDoc {
    
    private static ServiceURLRegistry R = ServiceUrlsInitializer.
                                                        getServiceURLRegistry();
    
    
    //ADMIN SERVICE:
    public UserName_PassWord_EndPoint LOGIN_VALIDATE = new UserName_PassWord_EndPoint();
    public UserName_PassWord_EndPoint LOGIN_AND_GET_TOKEN_FOR_SELF = new UserName_PassWord_EndPoint();
    
    //NINJA SERVICE
    public Name_Phone_Email_PortfolioUrl_EndPoint MAKE_NINJA_RECORD = new Name_Phone_Email_PortfolioUrl_EndPoint();
    public Id_EndPoint   GET_NINJA_BY_ID  = new Id_EndPoint();
    public Void_EndPoint GET_NEXT_NINJA = new Void_EndPoint();
    
    //OWNER SERVICE:
    public TokenId_NinjaId_EndPoint MAKE_ENTRY_USING_NINJA = new TokenId_NinjaId_EndPoint();
    public TokenId_AdminId_EndPoint MAKE_ENTRY_USING_ADMIN = new TokenId_AdminId_EndPoint();
    public TokenId_EndPoint         MAKE_ENTRY_USING_RANDOM= new TokenId_EndPoint();
    public TokenId_EndPoint         DOES_TOKEN_HAVE_OWNER  = new TokenId_EndPoint();
    public TokenId_EndPoint         GET_TOKEN_OWNER        = new TokenId_EndPoint();

    //TOKEN SERVICE:
    public Void_EndPoint GET_NEXT_TOKEN = new Void_EndPoint();
    
    //RIDDLE-RHYME SERVICE:
    public RiddleId_RhymeId_EndPoint GET_IS_CORRECT          = new RiddleId_RhymeId_EndPoint();
    public HttpPost_EndPoint GRADE_ONE_BLANK_SLATE           = new HttpPost_EndPoint();
    public RiddleId_EndPoint GET_FILLED_OUT_TEST_SLATE_TRUTH = new RiddleId_EndPoint();
    public RiddleId_EndPoint GET_FILLED_OUT_TEST_SLATE_WRONG = new RiddleId_EndPoint();
    public RiddleId_EndPoint GET_ONE_BLANK_SLATE             = new RiddleId_EndPoint();
    public Void_EndPoint     GET_ONE_RANDOM_RIDDLE           = new Void_EndPoint();
    public RiddleId_NumberOfChoices_NumberOfTruths_EndPoint MAKE_FILLED_OUT_CUE_CARD = new RiddleId_NumberOfChoices_NumberOfTruths_EndPoint();
    public CardCount_NumQuips_TruMin_TruMax_EndPoint GET_RANDOM_TRIVIA_BUNDLE = new CardCount_NumQuips_TruMin_TruMax_EndPoint();
    public HttpPost_EndPoint POST_QUAR_FOR_GRADING           = new HttpPost_EndPoint();
    public Void_EndPoint     GET_LAST_POSTED_QUAR            = new Void_EndPoint();
    
    /** current servlet name being used in the 
     * constructor/setup of document. **/
    private String _sname = "";
    
    /** current servlet class being used in constructor/setup of document. **/
    private Class  _sclass = null;
    
    /** current servlet fully qualified endpoint. Endpoint to SERVLET, not to
     *  a servlet function. Must end with a forward slash. **/
    private String _surl = "";
    
    /** When we ENTER configuration mode, we add one (+1) to this number.
     *  When we EXIT configuration mode, we subtract one (-1) from this number.
     *  When NOT in config, the value should be ZERO.
     *  When INSIDE config, the value should be ONE.
     *  Any other values suggests our balancing is OFF.
     */
    private int _enterExitBalancing = 0;
    
    /** Are we inside configuration mode? 
        AKA: Are we actively in a configuration block? **/
    private boolean _insideCFG = false;
   
    public MasterApiDoc(){
        
        String endPT;
        String cName;
        Class  clazz;
        
        //ADMIN REST SERVICE CONFIG:
        endPT = R.ADMIN;
        cName = ServletClassNames.AdminRestService_CLASSNAME;
        clazz = AdminRestService.class;
        enter_CFG(endPT, cName, clazz);
        add(LOGIN_VALIDATE, FuncNameReg.LOGIN_VALIDATE);
        add(LOGIN_AND_GET_TOKEN_FOR_SELF, FuncNameReg.LOGIN_AND_GET_TOKEN_FOR_SELF);
        exit_CFG();
        
        //NINJA REST SERVICE CONFIG:
        endPT = R.NINJA;
        cName = ServletClassNames.NinjaRestService_CLASSNAME;
        clazz = NinjaRestService.class;
        enter_CFG(endPT, cName, clazz);
        add(MAKE_NINJA_RECORD, FuncNameReg.MAKE_NINJA_RECORD);
        add(GET_NINJA_BY_ID,   FuncNameReg.GET_NINJA_BY_ID);
        add(GET_NEXT_NINJA,    FuncNameReg.GET_NEXT_NINJA);
        exit_CFG();
        
        //OWNER REST SERVICE CONFIG:
        endPT = R.OWNER;
        cName = ServletClassNames.OwnerRestService_CLASSNAME;
        clazz = OwnerRestService.class;
        enter_CFG(endPT, cName, clazz);
        add(MAKE_ENTRY_USING_NINJA ,FuncNameReg.MAKE_ENTRY_USING_NINJA);
        add(MAKE_ENTRY_USING_ADMIN ,FuncNameReg.MAKE_ENTRY_USING_ADMIN);
        add(MAKE_ENTRY_USING_RANDOM,FuncNameReg.MAKE_ENTRY_USING_RANDOM);
        add(DOES_TOKEN_HAVE_OWNER  ,FuncNameReg.DOES_TOKEN_HAVE_OWNER);
        add(GET_TOKEN_OWNER        ,FuncNameReg.GET_TOKEN_OWNER);
        exit_CFG();
        
        //TOKEN REST SERVICE CONFIG:
        endPT = R.TOKEN;
        cName = ServletClassNames.TokenRestService_CLASSNAME;
        clazz = TokenRestService.class;
        enter_CFG(endPT, cName, clazz);
        add(GET_NEXT_TOKEN, FuncNameReg.GET_NEXT_TOKEN);
        exit_CFG();
        
        //RIDDLERHYME SERVICE:
        endPT = R.RIDDLERHYME;
        cName = ServletClassNames.RiddleRhymeRestService_CLASSNAME;
        clazz = RiddleRhymeRestService.class;
        enter_CFG(endPT, cName, clazz);
        add(GET_IS_CORRECT                 , FuncNameReg.GET_IS_CORRECT                 );
        add(GRADE_ONE_BLANK_SLATE          , FuncNameReg.GRADE_ONE_BLANK_SLATE          );
        add(GET_FILLED_OUT_TEST_SLATE_TRUTH, FuncNameReg.GET_FILLED_OUT_TEST_SLATE_TRUTH);
        add(GET_FILLED_OUT_TEST_SLATE_WRONG, FuncNameReg.GET_FILLED_OUT_TEST_SLATE_WRONG);
        add(GET_ONE_BLANK_SLATE            , FuncNameReg.GET_ONE_BLANK_SLATE            );
        add(GET_ONE_RANDOM_RIDDLE          , FuncNameReg.GET_ONE_RANDOM_RIDDLE          );
        add(MAKE_FILLED_OUT_CUE_CARD       , FuncNameReg.MAKE_FILLED_OUT_CUE_CARD       );
        add(GET_RANDOM_TRIVIA_BUNDLE       , FuncNameReg.GET_RANDOM_TRIVIA_BUNDLE       );
        add(POST_QUAR_FOR_GRADING          , FuncNameReg.POST_QUAR_FOR_GRADING          );
        add(GET_LAST_POSTED_QUAR           , FuncNameReg.GET_LAST_POSTED_QUAR           );
        exit_CFG();
        
        
    }//CONSTRUCTOR
    
    private void add(EndPoint endPT, String funcName){
        if(null == endPT){//EEEEEEEEEEEEEEEEEEEEEEEEE
            doError("[Endpoint supplied was null]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //Store currently configured class and servlet name
        //for this function call:
        endPT.SERVLET_CLASS = _sclass;
        endPT.SERVLET_NAME  = _sname;
        
        //Construct servlet function endpoint:
        //BUG_FIX: Do not put "/" between variables. Because
        //the servlet url ends with "/".
        endPT.URL = _surl + funcName;
        
    }//add
    
    /** Use this when ENTERING configuration block for a single servlet. **/
    private void enter_CFG
                  (String urlToServlet, String servletName, Class servletClass){
                
        //set values to use:
        _surl   = urlToServlet; //endpoint to SERVLET but not service/func.
        _sname  = servletName;  //servlet's name.
        _sclass = servletClass; //servlet's class.
        
        if(""  ==_surl  ){ doError("[_surl empty string]"  ); }
        if(""  ==_sname ){ doError("[_sname empty string]" ); }
        if(null==_sclass){ doError("[_sclass empty string]"); }
        
        //Make sure urlToServlet is LONGER than servlet name. Servlet name
        //Should be included in the urlToServlet.
        int len_urlToServlet = urlToServlet.length();
        int len_servletName  = servletName.length();
        if(len_servletName >= len_urlToServlet){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            String msg = "[servletName is longer than urlToServlet]";
            msg += "[Did you accidentially swap/transpose the arguments?]";
            doError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //Make sure LAST charcter of urlToServlet ends with a "/":
        if('/' != urlToServlet.charAt(urlToServlet.length()-1)){//EEEEEEEEEEEEEE
            doError("[last char of url is not fwd-slash!]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //Make sure servlet class name matches the name supplied. If it
        //does not. We have an error!
        String cName = _sclass.getSimpleName();
        if(false == (cName.equals(_sname)) ){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            doError("enter_CFG::servletName != servletClass's name");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //NOTE: The lastIndexOf index represents the LAST ENTRY of the substring
        //      within the string. It is the index of the FIRST CHARACTER of
        //      that substring within the main string.
        //Useful: http://www.tutorialspoint.com/java/java_string_lastindexof.htm
        //Make sure that the URL to the servlet has the servletName at the
        //end of it's url. It not, indicates bad setup.
        int dexOf = urlToServlet.lastIndexOf(servletName);
        if(dexOf < 0){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            String msg = "[urlToServlet does not contain servletName]";
            msg += "urlToServlet==[" + urlToServlet + "]";
            msg += "servletName ==[" + servletName  + "]";
            msg += "NOTE on where to find bug:";
            msg += "If bug is NOT in this class, it could be:";
            msg += "Incorrectly mapped constants in:";
            msg += "[" + ServiceURLRegistry.class.getCanonicalName() + "]";
            msg += "If this is the case, it may be because the setup";
            msg += "for populating this code is WRONG.";
            msg += "setup happens in this class:";
            msg += "[" + ServiceUrlsInitializer.class.getCanonicalName() + "]";
            msg += "I made this mistake before by SWAPPING references where:";
            msg += "[ADMIN was paired with OWNER-serviceURL]";
            msg += "AND";
            msg += "[OWNER was paired with ADMIN-serviceURL]";
            msg += "[" + ServiceUrlsInitializer.class.getCanonicalName() + "]";
            msg += "was not able to find the error because each path was used";
            msg += "exactly once.";
            doError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        //If the servlet name is in the url to servlet, we still need to make
        //sure that it ends on the second-to-last character.
        //EXAMPLE: http://mySite/api/myService/
        //If name of service == "myService" it ends on the second-to-last char.
        //the last character being a "/" slash.
        //-1 for last index. -2 for second to last index.
        int secondToLastCharacterIndexOfURL = urlToServlet.length() -2;
        
        //Minus 1 because if servlet name were ONE letter, it would not move
        //from the dexOf it was found at.
        int lastIndexOfServletNameWithinURL = dexOf + servletName.length() - 1;
        
        if(secondToLastCharacterIndexOfURL !=
           lastIndexOfServletNameWithinURL){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            doError("[ServletNameFound, but appears in wrong position.]");
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
        _enterExitBalancing++;
        _insideCFG = true;
        if(_enterExitBalancing != 1){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            String msg = "[enter_CFG balance ERROR]";
            msg += "[[Setup/Config]Error: enter/exit balancing is off!]";
            doError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
    }//FUNC::END
    
    /** Use this when EXITING configuration block for a single servlet. **/
    private void exit_CFG(){
        
        //Only code in here is error checking to make sure our balancing in on.
        
        _enterExitBalancing--;
        _insideCFG = false;
        if(_enterExitBalancing != 0){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
            String msg = "[exit_CFG balance ERROR]";
            msg += "[[Setup/Config]Error: enter/exit balancing is off!]";
            doError(msg);
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
    }//FUNC::END
    
    
    /**-------------------------------------------------------------------------
    * Wrapper function to throw errors from this class.
    * @param msg :Specific error message.
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        err += MasterApiDoc.class.getSimpleName();
        err += msg;
        throw new MyError(err);
    }//FUNC::END
    
}//CLASS::END
