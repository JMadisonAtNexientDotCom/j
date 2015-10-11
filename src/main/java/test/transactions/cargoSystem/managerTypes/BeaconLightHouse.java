package test.transactions.cargoSystem.managerTypes;

import annotations.IndexedFunctionTable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import test.MyError;
import test.dbDataAbstractions.entities.bases.BaseEntity;
import test.transactions.cargoSystem.dataTypes.EntityCage;
import test.transactions.cargoSystem.dataTypes.GalleonBarge;
import test.transactions.cargoSystem.dataTypes.OrderSlip;
import test.transactions.cargoSystem.ports.TokenPorts;
import test.transactions.cargoSystem.ports.config.MasterPortList;

/**
 * This is a static registry class that helps resolve the different
 * port names. Because we cannot pass functions as variables in java,
 * we need this object to register the function handle names referenced
 * by the "port name" field of our OrderSlip objects.
 * 
 * NOTE: Not quite sure how I will implement this.
 *       Just have a basic idea of how it will work as of the
 *       time of writing this documentation.
 * 
 * @author jmadison :2014.10.08_0900PM
 */
public class BeaconLightHouse {
    
    /**Parameter signature that all ports take. **/
    private static Class[] _paramTypes;
    
    static{//////////////
        doStaticInit();
    }////////////////////
    
    private static IndexedFunctionTable _ports;
    
    private static void doStaticInit(){
        setup();
    }//FUNC::END
    
    /** Sets up our lookup tables so that the beacon light house can
     *  direct the Barge to the correct functions. **/
    private static void setup(){
        
        //Create the signature used to fetch the
        //methods from the lookup table:
        _paramTypes = new Class[2];
        _paramTypes[0] = GalleonBarge.class;
        _paramTypes[1] = OrderSlip.class;
        
        //Create new indexed function scanner
        //And register all the annotated classes:
        //Adding will also cause scanning.
        _ports = new IndexedFunctionTable();
        _ports.addClass(TokenPorts.class);
        
        //Build after all has been added:
        _ports.build();
        
        //Validate that after building, there are no null entries in the table:
        _ports.validateBuild();
        
    }//FUNC::END
    
    /** Manages the resolution of a given OrderSlip
     *  by looking at the portName (program handle) and routing to
     *  the correct port for transactions to take place on the ship.
     * @param barge :The cargo-ship we are loading with data.
     * @param order :The current order the captain of the ship wants
     *               ~fufilled~. Example: Go to treasure island and get
     *               5 pieces of gold.
     *               AKA: Get 5 new tokens out of the token table.
     */
    public static void guideShipToPort(GalleonBarge barge, OrderSlip order){
        
        validateIncommingShip(barge,order);
        
        //Regardless of if we are loading entities using a port,
        //we need to check the validity of how the order has been setup:
        validateKeyLoadingConfigOnOrder(order);
        
        //NOTE:If the Order has been pre-loaded with IDS, then we DO NOT
        //use a port function to fetch the data. We just get it directly
        //from the table using the transaction utility, querying for the
        //primary keys:
        if(false == order.loadKeysUsingPort){
            loadBargeUsingTable(barge,order);
        }else
        if(true == order.loadKeysUsingPort){
            loadBargeUsingPort(barge,order);
        }else{
            doError("[This should be line of dead code.]");
        }//BLOCK::END
        
        //Has order been fufilled, and fufilled properly?
        //validate that entities from the order have been collected
        //and that they have been collected PROPERLY.
        validateCollectedMerchandise(barge,order);
 
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     *  Do basic validation on an ~incoming~ ship that wants to have an
     *  order completed.
     * @param barge :The ship that is ~fufilling~ orders on the Agenda.
     * @param order :The current order on the AgendaClipBoard that is
     *               up for ~fufillment~.
     ------------------------------------------------------------------------**/
    private static void validateIncommingShip
                                          (GalleonBarge barge, OrderSlip order){
        //Error check inputs:
        if(null == barge){doError("[input barge is null]");}
        if(null == order){doError("[input order is null]");}
        if(null == barge.hold){doError("[cargo hold is null]");}
        if(barge.hold.isFilled){
            String msg = "[hold should not be filled if you are still]";
            msg += "[on quest to fufil your agenda.]";
            doError(msg);
        }//ERROR?
    }//FUNC::END
    
    private static void validateCollectedMerchandise
                                          (GalleonBarge barge, OrderSlip order){
        //Todo: Validate that for every deposited primary key, there is
        //also the corresponding entity.
        EntityCage cage = barge.hold.getCageUsingReceipt(order);
        if(cage.merchandise.size() != order.primaryKey_ids.size()){
            String msg = "";
            msg+="[Keys did not evenly pair with actual entities.]";
            msg+="[This is a requirement!]";
           doError(msg);
        }//Checksum error.
        
        //If checksum error passes, we want to make sure the POSITIONS
        //all match up.
        long id_from_key_list;
        BaseEntity be;
        long id_from_caged_entity;
        int len = cage.merchandise.size();
        for(int i = 0; i < len; i++){
            id_from_key_list = order.primaryKey_ids.get(i);
            be = cage.merchandise.get(i);
            
            //Make sure entity is of the correct type:
            if(be.getClass() != cage.entityClass){
                String msg = "";
                msg += "The wrong type of entity is in this cage";
                msg += "Analogous to putting a CAT into the DOG kennel";
                doError(msg);
            }//Wrong entity stored?
            
            id_from_caged_entity = be.getId();
            if(id_from_key_list != id_from_caged_entity){
                String msg = "";
                msg += "IDs must be paired.";
                msg += "possible reasons for error:";
                msg += "1: All keys have a match, but the keys are shuffled";
                msg += "and do not line up with the entity array paired with";
                msg += "2: You are looking at WRONG collection of entities.";
                doError(msg);
            }//Pairing error?   
        }//NEXT i
    }//FUNC::END
    
    /**For an order that is NOT configured to use a port function to get
     * the requested merchandise(entities), we want to do a straight pull
     * from the supplier table, using the primary key ids that have been
     * specified on the order slip. **/
    private static void loadBargeUsingTable
                                          (GalleonBarge barge, OrderSlip order){
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     *  This function loads up the ship(barge) with the requested order.
     *  Using a PORT FUNCTION to do the work. 
     * @param barge :The ship ~travelling~ around and collecting the cargo
     *               on the AgendaClipBoard containing all of the orders.
     * @param order :The current order on the agenda that is being ~fufilled~.
     ------------------------------------------------------------------------**/
    private static void loadBargeUsingPort(GalleonBarge barge, OrderSlip order){
        //Confirm that the portID is valid:
        MasterPortList.validatePortID(order.portID);
        
        //Get the correct port from the OrderSlip,
        //This is basically, the destination where we will find what
        //We need to fufill the order. Example: Maybe it is an order
        //For getting BLUE Giraffees. And the order.portID points to a port
        //that holds the function "getGiraffeesByColor(...)"
        Method m = _ports.getMethodByIndex(order.portID, _paramTypes);
        boolean isStatic = _ports.getIsMethodStaticByIndex
                                                    (order.portID, _paramTypes);
        
        //SOMETHING I DID NOT TRY.. Perhaps the method itself is... null?
        if(null == m){doError("Method returned was null.");}
        
        
        //DEBUG: Make sure _paramTypes is NOT NULL.
        if(null == _paramTypes){doError("[params null]");}
        if(_paramTypes.length != 2){doError("[param len]");}
        if(_paramTypes[0] != GalleonBarge.class){doError("[wrong 1st param]");}
        if(_paramTypes[1] != OrderSlip.class){doError("[wrong 2nd param]");}
        
        //DEBUG: Grasping at straws. Make sure inputs are NOT NULL.
        if(null == barge ||
           null == order  ){
            doError("needed input is null");
        }//ERRRRRRR
        
        String msg = ""; //message string for if error happens.
        
        //Try catches, stupid. Don't run your program in
        //a broken state. Ever. The longer you put off fixing an error,
        //the longer it takes to fix. An error that would take 1 hour to fix
        //will take 24hours to fix if you put it off for 3 months.
        try{//TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
            //m.invoke(m, _paramTypes[0], _paramTypes[1]); 
            //m.invoke(m, _paramTypes); //<--try this way?
            //m.invoke(m, barge, order); //<--wow... I was stupid.
            //m.invoke(barge,order); //<--....really stupid.
            
            //READ THE DOCUMENTATION:
            //https://docs.oracle.com/javase/tutorial/reflect/member/
            //                                             methodInvocation.html
            // (If the method is static, the first argument should be null.) 
            if(isStatic){ 
                //m.invoke(null, _paramTypes[0], _paramTypes[1]);
                //m.invoke(null, (Object)_paramTypes);
                //m.invoke(null, (Object[])_paramTypes);
                m.invoke(null, _paramTypes[0], _paramTypes[1]);
                
            }else{
                //Put msg += incase this error is hidden by the try catch.
                msg += "[NOT DESIGNED TO HANDLE NON-STATIC METHODS.]";
                doError("[Not designed to handle non-static methods.]");
            }//FUNC::END  
        }catch(InvocationTargetException ite){
          
            msg += "[InvocationTargetException:]";
            msg += ite.getMessage();
            msg += "[End of invocationTargetException details]";
          
        }catch(IllegalArgumentException illegal){
            
            msg += "[Illegal argument exception happened]";
            msg += "[MSG[" + illegal.getMessage() + "]MSG]";
            
        }catch(Exception exep){//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        
            msg += "[Exception was NOT type InvocationTargetException]";
            
        }finally{
            
            String mName = m.toGenericString();
            msg += "[Failed to invoke method!]";
            if(false == isStatic){
                msg+="[NOT DESIGNED TO HANDLE NON-STATIC METHODS!]";
            }//not static?
            msg += "[METHOD INFO:: START]";
            msg += mName;
            msg += "[METHOD INFO:: END]";
            doError(msg);  
            
        }//EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
    }//FUNC::END
    
    /**
     * Make sure the configuration of the keys is correct before we try
     * and satisfy the order. Keys either come pre-loaded and are used to
     * fetch entities, or keys don't exist and the port loads both the
     * key and the entities.
     * 
     *              |--loadKeysUsingPort=TRUE--|--loadKeysUsingPort==FALSE--|
     * INITIAL KEYS:|       NULL/EMPTY         |      1 or more primary keys|
     * INITIAL ENTITIES:    NONE IN CARGO      |     NONE IN CARGO          |
     * ----------------------------------------------------------------------
     * ENTITIES ON  |     Entities in cargo, and one primary key loaded
     * ORDER COMPLETE:    into order for each entity.
     * ----------------------------------------------------------------------
     * 
     * 
     * @param order :The order we want to ~analyse~ the integrity of before
     *               carrying it out.
     */
    private static void validateKeyLoadingConfigOnOrder(OrderSlip order){
        
        if(order.isOrderComplete()){
            doError("[Order has already been completed]");
        }//FUNC::END
        
        //Configured to fetch primary keys and entities
        //using a port function?
        if(true==order.loadKeysUsingPort){
            if(null != order.primaryKey_ids){
                if(order.primaryKey_ids.size() >= 0){
                    String msg = "";
                    msg += "[we are configured to fetch keys using port.]";
                    msg += "[But already has come pre-loaded with keys.]";
                    doError(msg);
                }//Has keys already loaded?
            }//keys object is NOT null.
        }//should we load keys into order using a port function?
        
        //Are we pre-loading the primary keys in order to fetch entities
        //from the table using straight-up keys? If this is the case,
        //there should be ZERO SPECS in this order. As it is being used
        //as a sort of constant.
        if(false == order.loadKeysUsingPort){
      
            //If flagged as preloading, there must actually be keys!
            if(null == order.primaryKey_ids){
                doError("[no loaded primary key list. Null.]");
            }//
            if(order.primaryKey_ids.size() <= 0){
                doError("[No primary key list. EMPTY.]");
            }//
            
            //Design question: 
            //What should we do if primary key asked for
            //ends up NOT existing? I think we should just throw an error.
            //But is there any situtation where we have to get something
            //we don't know exists by primary key?
            //I don't think there should ever be that scenario within this
            //system. The dependencies are wired up. And if information we
            //ask for does not exist, the kind of breaks the entire system.
            
        }//PRE-LOADED KEYS?
        
    }//FUNC::END
    
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = BeaconLightHouse.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
