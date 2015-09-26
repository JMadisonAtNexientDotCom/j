package test.servlets.rest;

import test.servlets.rest.admin.docs.AdminServiceDoc;

/**
 * A master registry for all of the rest services.
 * The REST services wire into this.
 * The JSP  pages wire into this.
 * This is how we get the FRONT-END + BACK-END to agree on the service calls.
 * @author jmadison
 */
public class MasterServiceDoc {
    public static final AdminServiceDoc ADMIN = new AdminServiceDoc();
}//CLASS_END
