package app.debug.debugUtils.tempDataStore;

import app.dbDataAbstractions.entities.composites.Quar;

/**-----------------------------------------------------------------------------
 * A temporary class for developing HTTP POST service.
 * Should not be referenced when this project is in final production.
 * 
 * Usage: 
 *        1:
 *        When HTTP POST, put the data here in one variable corrosponding
 *        to the HTTP POST url.
 * 
 *        2:
 *        Use a corrosponding HTTP GET to get the data that was stored during
 *        the HTTP POST.
 * 
 * Why this is hackish:
 * There is only one static variable for storing results of HTTP POST.
 * So you will run into a lot of trouble if multiple people are trying to
 * test the same service call. Good quick solution for figuring things out
 * however.
 * 
 * 
 * @author jmadison :2015.09.19_0916PM
 ----------------------------------------------------------------------------**/
public class TempServiceDataUtil {
    
    
    public static Quar theQuar = null;
    public static int gradedQuarScore = 99;
    
    
}//CLASS::END
