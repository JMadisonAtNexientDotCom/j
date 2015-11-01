package app.transactions.cargoSystem.dataTypes;

/**
 * Represents an argument for an Order. Used when we want to supply
 * specific data. Doing this way so we can avoid position based parameters.
 * When you have a lot of PBP, it makes code very hard to read.
 * ABBREV: PBP=="Position Based Parameters"
 * 
 * UPDATE:2015.10.16, I like this pattern and how it avoids me having
 * to use positional arguments. Lets use OrderArg array for job tickets too.
 * 
 * @author jmadison :2015.10.8
 */
public class OrderArg {
    /** Variable name referenced with this argument. 
        AKA: Identifier name. We should not have to use reflection
        to make use of this. If the design I am working with goes together
        as I imagine it. **/
    public String varName = "";
    
    /** Value to inject into the varName parameter when
        calling the port **/
    public Object varValue= null;
}//CLASS::END
