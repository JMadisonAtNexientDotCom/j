package test.transactions.cargoSystem.managerTypes;

import java.util.List;
import test.MyError;
import test.transactions.cargoSystem.dataTypes.AgendaClipBoard;
import test.transactions.cargoSystem.dataTypes.CargoHold;
import test.transactions.cargoSystem.dataTypes.GalleonBarge;
import test.transactions.cargoSystem.dataTypes.OrderSlip;
import test.transactions.util.TransUtil;

/**
 * CAPTAIN is the object that guides the GalleonBarge (cargo ship)
 * to all of the destinations it needs to be, in the correct order.
 * 
 * In less poetic terms: It is an initializer manager.
 * It manages what functions need to be called in what order in order
 * to fufill a group of requests with complex interdependencies.
 * (AKA: Our AgendaClipBoard)
 * 
 * Captain is a merchant, and also a captain. Hence why we call
 * this person the "Merchant Captain". Also, by giving more descriptive
 * class name, we can just refer to instances as "Captain".
 * 
 * @author jmadison
 */
public class MerchantCaptain {
    
    /** In case you forget to set an enum value and it default initializes to
     *  zero. We want to catch that and not think the zero was intentional. **/
    int INITIALIZATION_ERROR = 0;
    
    /** This enum flag is used to mock the execution of all orders on the
     *  agenda. This makes it possible to catch errors in the dependency
     *  setup BEFORE executing the real code. If your Agenda is invalid,
        you don't halt the program in the middle of a complex set of
        interrelated transactions. **/
    int MOCK_EXECUTION = 1;
    
    /** Flag with cause ACTUAL execution of the orders within the agenda. **/
    int DO_IT_FOR_REAL = 2;
    
    /** Captain has reference to captain's ship. So that
     *  captain has access to the OrderSlips on the AgendaClipBoard **/
    public GalleonBarge barge;
    
    /**  Main job of the captain. This is how we resolve all of our OrderSlip
     *   objects on the AgendaClipBoard into actual entities and 
     *   primary_keys.**/
    public void fetchOrders(){
       
        cleanBarge(); //<--empties or replaces containers we will be filling.
        preSailValidation();
        
        //mock first to check for any problems with
        //the dependency setup of the orders.
        mockOrderCompletion(); 
        
        //If dependencies check out as valid, we run
        //the same dependency resolution code as before, but
        //this time we actually resolve the function handles and
        //work with real data. We could have cached results from
        //mockOrderCompletion() and then had a function run off of that.
        //However, that would have two draw backs:
        //1: This function would be dependant on the debugging mock function.
        //2: We would have to support and duplicate a moderately complex
        //   initializer loop structure that exists in mockOrderCompletion.
        //SUMMARY:
        //We would symultaniously create dependencies AND re-use LESS code.
        executeAllOrdersOnTheAgenda();
        
    }//FUNC::END
    
    /** Executes all of the orders in the correct order based on the
     *  dependencies between the orders.
     */
    private void executeAllOrdersOnTheAgenda(){
        
        //Make sure we are in a transaction state before executing.
        //I am NOT going to have any checks for being inside a transaction
        //State for any of the port functions. We are trying to go with
        //a slightly different design pattern.
        TransUtil.insideTransactionCheck();
        
        //Do all the transactions:
        executeOrMock_AllOrders(DO_IT_FOR_REAL);
        
    }//FUNC::END
    
     /**
     * A test that PRETENDS to execute the orders.
     * Used to make sure that all of the dependencies are setup
     * correctly. If unable to resolve all orders, it signifies a
     * problem in our order dependencies. (Most likely a dependency loop.)
     */
    private void mockOrderCompletion(){
        executeOrMock_AllOrders(MOCK_EXECUTION);
    }//FUNC::END
    
    /**
     * 
     * @param execType :
     *        MOCK_EXECUTION: Will mock up execution, but not really do it.
     *                        This is for testing that all the dependencies
     *                        are wired up correctly before doing real
     *                        execution.
     *       DO_IT_FOR_REAL : Will NOT mock the execution. Will really
     *                        Execute all orders on the agenda.
     */
    private void executeOrMock_AllOrders(int execType){
        //Figure out how many orders there are. We want
        //one reciept for each order. Once order list is
        //filled, we are done.
        List<OrderSlip> orders = barge.agenda.getOrdersRef();
        int numberOfOrders     = orders.size();
        int maxValidOrderIndex = numberOfOrders - 1;
        
        /** Use an array of actual order references as receipts.
         *  This will make dependency checking easier
         *  than if we were just to use the index at which the order
         *  appears in barge.agenda.orders list. **/
        OrderSlip[] orderReceipts= new OrderSlip[numberOfOrders];
        
        /** Used to track number of orders [filled/complete], also used
         *  to establish the index at which to put the next receipt
         *  within orderReceipts **/
        int numOrdersFilled= 0;
        
        //Create inf loop and try to resolve one order each
        //iteration of that loop until all orders are filled.
        /**ord == current order index. **/
        int ord;
        /** current order object. **/
        OrderSlip curOrder;
        boolean hasEverythingItNeeds;
        
        /** Represents how many Orders were successfully resolved in
         *  one complete cycle of the inner loop. If this number ends up
         *  as ZERO after the loop executes, we have an error. 
            This algorithm for dependency resolution should allow at least
            one Order to be resolved each cycle of the inner loop. **/
        int numberOfOrdersSuccessfullyResolvedThisIteration;
        
        while(true){
            //On outer loop. 
            //Reset number of orders successfully resolved to zero.
            numberOfOrdersSuccessfullyResolvedThisIteration = 0;
           
            //See if you should bail out because all orders are complete:
            if(numOrdersFilled == numberOfOrders){
                //ERROR CHECK: If this error check fails, it could mean
                //that you have multiple references to the same Order object
                //within your AgendaClipBoard.
                verifyRecieptsMatchOrders(orderReceipts,orders); 
                return; //exit function. All orders complete.
            }else
            if(numOrdersFilled >  numberOfOrders){
                doError("[order overflow.]");
                return; //CYA: exit function. Error happened.
                        //probably will never get to that line. But just
                        //in case.
            }//BLOCK::END
          
            ord = (-1); //prime for loop entry.
            while(true){
                ord++;//inc & bail out when out of bounds.
                if(ord > maxValidOrderIndex){break;} 
                
                //Only check NULL orders that have not been filled yet:
                if(null == orderReceipts[ord]){ 
                    curOrder = orders.get(ord);
                    hasEverythingItNeeds = areDependenciesForThisOrderMet
                                                      (curOrder, orderReceipts);
                    
                    //If we order has everything it needs to complete, 
                    //mock it's completion:
                    //nix that. MAYBE mock it's completion. Depending on if
                    //shouldWeDoItForReal is true or false.
                    if(hasEverythingItNeeds){
                        numberOfOrdersSuccessfullyResolvedThisIteration++;
                        orderReceipts[ord] = executeOrMock(curOrder, execType);
                        //orderReceipts[ord] = curOrder;
                        numOrdersFilled++;
                    }//MOCK, or get for real.
                }//NEXT.
            }//INF LOOP
            
            //If you made it through the inner loop and NOTHING was resolved:  
            if(numberOfOrdersSuccessfullyResolvedThisIteration <= 0){
                String exe = Integer.toString(execType);
                String msg = "[DEPENDENCIES NOT WIRED CORRECTLY.]";
                msg+="[execType enum:[" + exe + "]]";
                msg+="[Possible solutions/reasons for problem]";
                msg+="[1: Dependencies not set up properly.]";
                msg+="[(A dependency loop probably.)]";
                msg+="[2: Zero items on the agenda.]";
                msg+="[3: Forgot to tally when completed Order on agenda.]";
                msg+="[4: Completed all orders, but this error check got]";
                msg+="[executed before function was able to exit.]";
                doError(msg);
            }//ERROR CHECK END.
            
        }//INF LOOP
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
     * If mocking, code short circuits and returns original input as output.
     * If NOT mocking, code actually ~fufils~ the order before
     * returning it.
     * 
     * NOTE: This function, when being ran in WITHOUT the mock execution type.
     *       Expects that all dependencies have been properly resolved.
     *       If you give it code with a dependency loop, it will NOT pre-check
     *       it. Hence why this method is PRIVATE and not public.
     *       Exposing this method publically could open you up to a scenario
     *       where AGENDAS with errors in them execute half way through before 
     *       throwing a fatal error. If this happens in production, you are
     *       going to end up with bad fragments of data in your database.
     * 
     * @param curOrder     :The current order to execute.
     * @param execType:Are we mocking execution, or doing it for real?
     * @return :Returns the original curOrder that was inputted.
     ------------------------------------------------------------------------**/
    private OrderSlip executeOrMock(OrderSlip curOrder, int execType){
    
        if(DO_IT_FOR_REAL==execType){
            BeaconLightHouse.guideShipToPort(barge,curOrder);
        }else
        if(MOCK_EXECUTION==execType){
            //Do nothing.
        }else
        if(INITIALIZATION_ERROR==execType){
            doError("[execType:INIT_ERROR, meaning you forgot to set it]");
        }else{
            doError("[unknown execType enum]");
        }//BLOCK::END
        
        //Return the original order input.
        //Which will have been populated with info if
        //we are not using mock-execution execType mode.
        return curOrder;
        
    }//FUNC::END

    
    /** 
     *  Empties or replaces containers we will be filling:
     *  
     *  In case the barge has been used by a previous owner.
     *  AKA: Used in a previous transaction with the database.
     *  We are going to want to rid ourselves of data that may
     *  have been left over. **/
    private void cleanBarge(){
        if(null==barge){
            doError("[No barge exists for crew to clean.]");
        }//NULL BARGE.
        
        barge.hold  = CargoHold.make();
        
        //NOTE: does not change status of barge. Because that status
        //should have been set before you called "fetchOrders()".
        
    }//FUNC::END
    
    /** Will throw error if not able to start. **/
    private void preSailValidation(){
        
        //Barge is never allowed to be null when validating.
        if(null == barge){
            doError("[Captain has no barge. Cannot set sail.]");
        }//NULL BARGE.
        
        if(null == barge.agenda){
            doError("[Agenda is null! No orders to carry out.]");
        }//Agenda NOT allowed to be null.
        
        List<OrderSlip> orders = barge.agenda.getOrdersRef();
        if(null == orders){
            doError("[No orders. The orders object is null.]");
        }//NULL orders.
        
        if(orders.size() <= 0){
            doError("[Need at least one order to justify sailing this ship]");
        }//Nothing to collect problem.
        
        if(barge.agenda.status != AgendaClipBoard.STATUS_CONFIGURING_COMPLETE){
            String msg ="";
            msg += "[Agenda is NOT flagged as ready]";
            msg += "[Think of it like, ]";
            msg += "[the agenda has not been signed-off on (approved).]";
            msg += "[hich means that it may not be fully configured.]";
            msg += "[Maybe you forgot to flag the agenda as configured after]";
            msg += "[You finished configuring it?]";
            msg += "[Maybe you are re-fetching an agenda that has already]";
            msg += "[Been completed by the captian?]";
            doError(msg);
        }//Agenda status error.
        
        if(null == barge.hold){
            doError("[we need a hold that is ready to be filled!]");
        }//FUNC::END
        
        if(barge.hold.isFilled != false){
            String msg = "";
            msg += "Hold already filled?";
            msg += "Perhaps this boat has already sailed?";
            doError(msg);
        }//Hold already filled?
        
        if(null == barge.hold.cages){
            doError("[We need an EMPTY cages array.]");
        }//No cages ref?
        
        if(barge.hold.cages.isEmpty() != true){
            doError("[Should have zero cargo at beginning of trip.]");
        }//FUNC::END
        
        
    }//FUNC::END
    
    /**
     * After an AgendaClipBoard has been resolved, we want to make sure the
     * receipts match up with the original orders. If they do not, then we
     * have an error in the code.
     * @param receipts :An ARRAY of receipts. Should be same length as the
     *                  orders array. Receipts contain the same references
     *                  as orders, only in a different order. The order of
     *                  entries in receipts is based on the order in which
     *                  the orders were [initialized/resolved]
     * @param orders   :The actual orders that you wanted to resolve.
     */
    private void verifyRecieptsMatchOrders
                                 (OrderSlip[] receipts, List<OrderSlip> orders){
        
        int len00 = receipts.length;
        int len01 = orders.size();
        if(len00 != len01){
            doError("[CheckSumError:Should be 1 receipt for each order.]");
        }//CheckSumError.
        int maxValidOrderIndex = len00-1;                       
        int ir; //index of reciept.
        int oi; //index of order.
        OrderSlip curReceipt;
        int foundIndexOfOrder;
        for(ir = 0; ir <=maxValidOrderIndex; ir++){
            curReceipt = receipts[ir];
            if(null==curReceipt){doError("[null==curReceipt]");}
            foundIndexOfOrder = orders.indexOf( curReceipt );
            if(foundIndexOfOrder <= (-1)){
                doError("[receipt could not be resolved to an order]");
            }//END
        }//NEXT i
        
    }//FUNC::END
                                 
    /**
     * For a given order on the agenda, are the dependencies met that would
     * allow for this order to be completed?
 
     * @param order  :The current order you wish to assess the viability of.
     *                Do you have everything you need in order to make this
     *                order?
     * 
     * @param receipts :References of Orders that have already been completed.
     *                  If all of the order's dependencies are present within
     *                  the receipts array, then the order can be filled.
     * 
     *                  NULL ENTRIES ARE ALLOWED. But should be isolated to the
     *                  tail. This is because we are using the receipts object
     *                  WHILE IT IS BEING BUILT.
     * 
     * @return :Return TRUE if order is able to be executed.
     *          Return FALSE if order is NOT able to be executed.
     */
    private boolean areDependenciesForThisOrderMet
                                      (OrderSlip order, OrderSlip[] receipts){
                                          
        int numDep = order.getNumberOfDependencies(); 
        if(0==numDep){return true;} //no dependencies. So no checks needed.
                                    //you can fufill this order.
                                          
        int maxRecieptIndex = getLastNonNullIndex(receipts);
        
        //Default of boolean LOWERCASE "b" is FALSE:
        //Every time a dependency is FOUND, a TRUE is put
        //into this array where that dependency occurs in
        //the order object. If, after exiting, the dependencies
        //check list is full, then all dependencies have been met.
        boolean[] dependenciesCheckList = new boolean[numDep];
        
        
        OrderSlip receipt;
        for(int i = 0; i <= maxRecieptIndex; i++){
            receipt = receipts[i];
            
            int foundIndex = OrderSlip.getIndexOf(receipts, receipt);
            if( foundIndex >= 0){
               dependenciesCheckList[foundIndex] = true;
            }//Mark dependency as found.
            
        }//NEXT i
        
        //If all items have been checked off on our dependency
        //check list, then the dependencies for this order
        //have been met:
        return areAllEntriesTrue(dependenciesCheckList);
        
    }//FUNC::END
                                      
    private boolean areAllEntriesTrue(boolean[] arr){
       int len = arr.length;
        for(int i = 0; i < len; i++){
            if(false==arr[i]){return false;}
        }//NEXT i
        return true;
    }//FUNC::END
                     
    /**
     * Finds the last non null index of the array.
     * @param receipts :The array of receipts to check.
     * @return :Returns >= 0 if found contiguous non-null elements.
     *          Returns (-1) if no non-null elements found.
     *          Throws Error: If non-contiguous section of elements found.
     *                        OR: non-contiguous section does not start at [0]
     */
    private int getLastNonNullIndex(OrderSlip[] receipts){
        int maxIndexOfArray = receipts.length;
        boolean nullPreviouslyFound = false;
        int lastNonNullIndex = (-1);
        for(int i = 0; i < maxIndexOfArray; i++){
            if(null != receipts[i]){
                if(nullPreviouslyFound){
                    String msg = "[null slots should be isolated]";
                    msg += "[in one chunk at tail.]";
                    doError(msg);
                }else{
                    lastNonNullIndex = i;
                }//END
            }else
            if(null == receipts[i]){
                nullPreviouslyFound = true;
            }else{
                doError("This should be dead code.");
            }//BLOCK::END 
        }//NEXT i
        
        return lastNonNullIndex;
        
    }//FUNC::END
    
    /**-------------------------------------------------------------------------
    -*- Wrapper function to throw errors from this class.   --------------------
    -*- @param msg :Specific error message.                 --------------------
    -------------------------------------------------------------------------**/
    private static void doError(String msg){
        String err = "ERROR INSIDE:";
        Class clazz = MerchantCaptain.class;
        err += clazz.getSimpleName();
        err += msg;
        throw new MyError(clazz, err);
    }//FUNC::END
    
}//CLASS::END
