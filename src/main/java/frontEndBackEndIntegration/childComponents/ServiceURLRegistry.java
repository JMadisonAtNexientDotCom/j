/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontEndBackEndIntegration.childComponents;

import test.config.constants.ServiceUrls;

/**
 * Must be instantiatable so we can use as properties.
 * @author jmadison
 */
public class ServiceURLRegistry {
    public final String OWNER       = ServiceUrls.OWNER;   
    public final String ADMIN       = ServiceUrls.ADMIN;
    public final String TOKEN       = ServiceUrls.TOKEN;
    public final String FILE        = ServiceUrls.FILE;
    public final String NINJA       = ServiceUrls.NINJA;
    public final String RIDDLERHYME = ServiceUrls.RIDDLERHYME;
    public final String TRANSDEBUG  = ServiceUrls.TRANSDEBUG;
    
    //TODO: We need to be able to error check this.
    //Maybe the HASHMAP in ServiceUrls should be used for that?
    //Actually, just to a checksum check and confirm it matches the others.
    //then confirm that all paths are unique. That should be good
    //enough error checking for this class.
    
}//CLASS::END
