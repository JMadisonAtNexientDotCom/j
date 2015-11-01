
package app.transactions.cargoSystem.ports;

import java.util.List;
import app.MyError;
import app.config.constants.identifiers.VarNameReg;
import app.dbDataAbstractions.entities.bases.BaseEntity;
import app.dbDataAbstractions.entities.tables.NinjaTable;
import app.transactions.cargoSystem.dataTypes.GalleonBarge;
import app.transactions.cargoSystem.dataTypes.OrderSlip;
import app.transactions.cargoSystem.ports.config.MasterPortList;
import app.transactions.util.TransUtil;
import app.transactions.util.tables.ninja.NinjaTransUtil;

/**
 * Didactic functions that take a barge+order. Used to load the barge 
 * (cargoShip) with the current order. Port is an analogy for a maritime port.
 * @author jmadison :Pre:2015.10.15 (Oct15th,Year2015)
 */
public class NinjaPorts {
    
    public static final short GET_ONE_NINJA_BY_ID = 
               MasterPortList.GET_ONE_NINJA_BY_ID; 
    
    //delete//public static final short FIND_BATCH_OF_NINJAS = 
    //delete//           MasterPortList.FIND_BATCH_OF_NINJAS;
    
    
    //Should really be using a generic order that loads from a table
    //than this function.
    /* xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    //
    // Loads the cargo ship with the ninja_ids specified on the orderslip.
    // If ANY specified ninjas do NOT exist, an error will be thrown.
    // @param barge :The cargo ship being loaded.
    // @param order :The order for ninjas being filled.
    //
    public static void find_batch_of_ninjas
                                          (GalleonBarge barge, OrderSlip order){
        preFillCheck(order);
        
        //get the ids_of_ninjas from order:
        if(null==order.primaryKey_ids){doError("[primarykeysnull]");}
        if(order.primaryKey_ids.isEmpty()){doError("[emptyprimarykeys]");}
        List<Long> ids_of_ninjas = order.primaryKey_ids;
        
        List<BaseEntity> ninjas;
        ninjas = TransUtil.getEntitiesUsingListOfLong
                        (NinjaTable.class, NinjaTable.ID_COLUMN, ids_of_ninjas);
        
        //Make sure we get back exactly the same amount we ask for. This is
        //important, because with these transactions, the data for two
        //different transactions might have their results paired up into a 
        //3rd transaction. Not getting back exactly what you have asked for
        //would wreck this pairing.
        if(ninjas.size() != ids_of_ninjas.size()){
            String msg = "";
            msg += "Output entities does not match number of inputs.";
            msg += "possible reasons:";
            msg += "1: Duplicate entries in supplied ids";
            msg += "2: Id supplied was NOT found in table.";
            doError(msg);
        }//FUNC::END
        
        //pack the resulting entities into the cargo hold:
        barge.hold.addCage(ninjas, order);
        
        //DONE!
        
    }//FUNC::END
    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx */
    
    /** Gets one ninja, using ID. Order is filled out by having a SPEC
     *  that specifies a variable ninja_id with a value of the ninja's id.
     * @param barge :The cargo-ship we are loading.
     * @param order :The order we are filling. 
     *              (Items from order loaded onto barge)
     */
    public static void get_one_ninja_by_id(GalleonBarge barge, OrderSlip order){
        
        preFillCheck(order);
        
        Long ninja_id = order.specs.getValLong(VarNameReg.NINJA_ID);
        NinjaTable ninja = NinjaTransUtil.getNinjaByID(ninja_id);
        
        //Will fill the cage in the hold, as well as the reciept:
        barge.fillOrderOfOne(order, ninja);
        
    }//FUNC::END
    
    /**
     * All of the basic checks to assert basic order integrity for an
     * order being filed with the NinjaPorts
     * @param order :The order to check/validate.
     *               Throws error if there is a problem.
     */
    private static void preFillCheck(OrderSlip order){
        assertHasCorrectSupplierAndZeroDependencies(order);
        OrderSlip.preFillCheck(order, NinjaTable.class);
    }//FUNC::END
    
    private static void assertHasCorrectSupplierAndZeroDependencies
                                                              (OrderSlip order){
        assertZeroDependencies(order);
        assertSupplierIsNinjaTable(order);
 
    }//FUNC::END
    
    private static void assertZeroDependencies(OrderSlip order){
        if(order.hasDependencies){
            doError("[This order should have zero dependencies!]");
        }//
    }//FUNC::END
    
    private static void assertSupplierIsNinjaTable(OrderSlip order){
        if(order.supplier != NinjaTable.class){
            String sName = (null==order.supplier?"NULL!":order.supplier.
                                                            getCanonicalName());
            String nTable = NinjaTable.class.getCanonicalName();
            String msg = "";
            msg += "[Wrong supplier identified in order setup].";
            msg += "supplier:[" + sName + "]";
            msg += "NinjaTable:[" + nTable + "]";
            doError(msg);
        }//Bad supplier on order??
    }//FUNC::END
    
    //THIS KIND OF FUNCTION SHOULD NOT EXIST! If loading straight from
    //a table using a list of primary key ids, we do NOT allocate a special
    //port function for that!
    //
    // Expects an order with ZERO dependencies that fetches ninja entities
    // using the ninja's id. Because orders can be dependant on other orders,
    // this function will throw error if one of the ninja ids does NOT EXIST.
    // @param barge :The barge (cargo ship) we are going to load up with ninjas.
    // @param order :The order that is specifying ninja IDS.
    //
    //public static void get_ninjas_onto_boat(GalleonBarge barge, OrderSlip order){
    //    
    //    if(order.hasDependencies){
    //        doError("Order should have zero dependencies.");
    //    }// 
    //}//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = NinjaPorts.class;
        err += clazz.getSimpleName();
        err += msg;
        throw MyError.make(clazz, err);
    }//FUNC::END
    
}//CLASS::END
