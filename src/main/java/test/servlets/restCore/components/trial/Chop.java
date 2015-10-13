package test.servlets.restCore.components.trial;

import annotations.PairedStaticFunction;
import annotations.Verbatim;
import primitives.RealAndFakeIDs;
import test.config.constants.identifiers.FuncNameReg;
import test.dbDataAbstractions.requestAndResponseTypes.postTypes.postRequest.Edict;
import test.servlets.restCore.components.admin.*;
import test.transactions.util.forOwnedMainlyByOneTable.ninja.NinjaTransUtil;

/**-----------------------------------------------------------------------------
 * Process:
 * BOOK -- Convert request into an object. "BOOK" the request.
 * CHOP -- Chop booked request into valid and invalid requests. Two halves.
 * FILL -- Fills the INVALID booked order, and the VALID booked order.
 * JOIN -- Joins the invalid+valid back together into one output object.
 *
 * @author jmadison :2015.10.13
 ----------------------------------------------------------------------------**/
public class Chop {
    
    /**
     * Job of chopping is to SPLIT the request/input object in HALF.
     * Where one half is valid input. And the other input is garbage input.
     * @param ed :The Edit to chop in half.
     * @return   :Edict[0] == good info.
     *            Edict[1] == bad  info.
     *            If good info does not exist, slot [0] will be null.
     */
    @PairedStaticFunction //All classes scanned should have func with this name
    @Verbatim( name=FuncNameReg.DISPATCH_TOKENS ) //refactor easier.
    public static Edict[] dispatch_tokens(Edict ed){
        
        Edict[] op = new Edict[2];
        op[0] = null; //good slot.
        op[1] = null; //bad slot.
        
        ed.comment = "[Touched by Chop.dispatch_tokens]";
        
        if(null == ed.ninja_id_list){
            ed.isError = true;
            ed.comment += "[Null ninja id list]";
        }else
        if(ed.ninja_id_list.isEmpty()){
            ed.isError = true;
            ed.comment += "[Empty ninja id list]";
        }//
        
        //possibly convert to error if invalid input:
        if(ed.duration_in_minutes <= 0){
            ed.isError = true;
            ed.comment += "[Bad duration]";
        }//
        
        if(ed.trial_kind <= 0){
            ed.isError = true;
            ed.comment += "[bad trial kind]";
        }//
        
        //If the entire object is an error, there is
        //nothing good we can extract!
        if(ed.isError){
            op[0] = null; //nothing good.
            op[1] = ed;   //something bad.
            return op;
        }//
        
        //Try and split the information into good and bad:
        RealAndFakeIDs rf;
        rf = NinjaTransUtil.sortNinjaIDS_IntoRealAndFake(ed.ninja_id_list);
        op[0] = Edict.clone(ed);
        op[0].ninja_id_list = rf.real;
        
        op[1] = Edict.clone(ed);
        op[1].ninja_id_list = rf.fake;
        
        return op;
            
    }//FUNC::END
    
}//CLASS::END
