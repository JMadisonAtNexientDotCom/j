
package test.transactions.cargoSystem.ports;

import test.MyError;
import test.config.constants.identifiers.VarNameReg;
import test.dbDataAbstractions.entities.tables.NinjaTable;
import test.transactions.cargoSystem.dataTypes.GalleonBarge;
import test.transactions.cargoSystem.dataTypes.OrderSlip;
import test.transactions.cargoSystem.ports.config.MasterPortList;
import test.transactions.util.forOwnedMainlyByOneTable.ninja.NinjaTransUtil;

/**
 *
 * @author jmadison
 */
public class NinjaPorts {
    
    public static final short GET_ONE_NINJA_BY_ID = MasterPortList.GET_ONE_NINJA_BY_ID; 
    
    /** Gets one ninja, using ID. Order is filled out by having a SPEC
     *  that specifies a variable ninja_id with a value of the ninja's id.
     * @param barge :The cargo-ship we are loading.
     * @param order :The order we are filling. 
     *              (Items from order loaded onto barge)
     */
    public static void get_one_ninja_by_id(GalleonBarge barge, OrderSlip order){
        
        if(order.hasDependencies){doError("This order should have zero dependencies!");}
        if(order.supplier != NinjaTable.class){doError("Wrong supplier identified in order setup.");}
        OrderSlip.preFillCheck(order, NinjaTable.class);
        
        Long ninja_id = order.specs.getValLong(VarNameReg.NINJA_ID);
        NinjaTable ninja = NinjaTransUtil.getNinjaByID(ninja_id);
        
        barge.hold.addCageWithOneEntity_AndAssertUnique(ninja, order);
        
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
