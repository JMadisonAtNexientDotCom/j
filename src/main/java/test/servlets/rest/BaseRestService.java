package test.servlets.rest;
/**-----------------------------------------------------------------------------
 * A base rest service from which all other rest services will be derived.
 * Reason:
 * So we can setup new rest servlets in smaller incriments.
 * When creating a new servlet, we can make it a STUB that extends
 * BaseRestService. We can then use the ping methods on our new stub servlet
 * to make sure it is wired in correctly. Once we know the servlet is configured
 * correctly, we can go on writing more code.
 * 
 * //My design philosophy here:
 * /////////////////////////////////////////////////////////////////////////////
 * 1. Small change. 
 * 2. Small commit. 
 * 3. Test to make sure still working.
 * Repeat in smallest possible steps that will result in UNBROKEN product.
 * Planning out the increments to your final goal is a good idea.
 * /////////////////////////////////////////////////////////////////////////////
 * 
 * @author jmadison ---------------------------------------------------------**/
public class BaseRestService {
    
}//CLASS::END
