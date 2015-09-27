package test.config.constants.apiDocs;

import frontEndBackEndIntegration.childComponents.ServiceURLRegistry;
import test.config.constants.ServiceUrlsInitializer;
import test.config.constants.ServletClassNames;
import test.config.constants.identifiers.FuncNameRegistry;
import test.config.constants.identifiers.VarNameReg;
import test.config.constants.signatures.SigReg;
import test.config.constants.signatures.sig.UserName_PassWord_EndPoint;
import test.servlets.rest.AdminRestService;

/**
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
    public UserName_PassWord_EndPoint LOGIN_VALIDATE;
    public UserName_PassWord_EndPoint LOGIN_AND_GET_TOKEN_FOR_SELF;
   
    public MasterApiDoc(){
        
        //ADMIN REST SERVICE CONFIG:
        String servletname_ADMIN = ServletClassNames.AdminRestService_CLASSNAME;
        Class servletclass_ADMIN = AdminRestService.class;
        LOGIN_VALIDATE.SERVLET_NAME = servletname_ADMIN;
        LOGIN_VALIDATE.SERVLET_CLASS= servletclass_ADMIN;
        LOGIN_VALIDATE.URL = R.ADMIN + "/" + FuncNameRegistry.LOGIN_VALIDATE;
        
        LOGIN_AND_GET_TOKEN_FOR_SELF.SERVLET_NAME = servletname_ADMIN;
        LOGIN_AND_GET_TOKEN_FOR_SELF.SERVLET_CLASS= servletclass_ADMIN;
        LOGIN_AND_GET_TOKEN_FOR_SELF.URL = R.ADMIN + "/" + FuncNameRegistry.
                                                   LOGIN_AND_GET_TOKEN_FOR_SELF;
        
        
        
    }//CONSTRUCTOR
   
    
}//CLASS::END
