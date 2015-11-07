package test.servlets.rest.restCore;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import test.config.constants.ServletClassNames;
import test.config.constants.identifiers.FuncNameReg;
import test.dbDataAbstractions.entities.tables.KindaTable;
import test.dbDataAbstractions.entities.tables.LoftTable;
import test.transactions.util.TransUtil;
import test.transactions.util.tables.wildc.LoftTU;
import utils.JSONUtil;

  
////////////////// LOFT ABSTRACTION DOCUMENTATION START ////////////////////////
/*
Diagram relevant enough to these tables to be included in the header file:
1: twine_table
2: loft_table
3: bale_stack
4: straw_purse
       	__________________________________________________________________
       /:::::::::::::THE LOFT: (AHK Shortcut===[?lofttable]:::::::::::::::\
      /                                                                    \
     /  +--loft_table records:--------------------------------------------+ ]
    /   |           [0]                   [1]                   [2]       | ]
   /    \--> __[ bale_si ]__ ===== __[ bale_si ]__ ===== __[ bale_si ]__<-/ ]
  /         /               \     /               \     /               \   ]
 [ --+------->+----bale---+  \   /  +----bale---+  \   /  +----bale---+  \  ]
 [   |     |  | [ straw ] |  |   |  | [ straw ] |  |   |  | [ straw ] |  |  ]
 [   |     |  | [ straw ] |  |   |  | [ straw ] |  |   |  | [ straw ] |  |  ]
 [   |     |  | [ straw ] |  |   |  | [ straw ] |  |   |  | [ straw ] |  |  ]
 [   |     |  +-----------+  |   |  +-----------+  |   |  +-----------+  |  ]
 [   |     |                 |   |                 |   |                 |  ]
 [ bale_   |  +----bale---+  |   |  +----bale---+  |   |  +----bale---+  |  ]
 [ stack   |  | [ straw ] |  |   |  | [ straw ] |  |   |  | [ straw ] |  |  ]
 [ records |  | [ straw ] |  |   |  | [ straw ] |  |   |  | [ straw ] |  |  ]
 [   |     |  | [ straw ] |  |   |  | [ straw ] |  |   |  | [ straw ] |  |  ]
 [   |     |  +-----------+  |   |  +-----------+  |   |  +-----------+  |  ]
 [   |     |                 |   |                 |   |                 |  ]
 [   |     |  +----bale---+  |   |  +----bale---+  |   |  +----bale---+  |  ]
 [   |     |  | [ straw ] |  |   |  | [ straw ] |  |   |  | [ straw ] |  |  ]
 [   |     |  | [ straw ] |  |   |  | [ straw ] |  |   |  | [ straw ] |  |  ]
 [   |     |  | [ straw ] |  |   |  | [ straw ] |  |   |  | [ straw ] |  |  ]
 [ --+------->+-----------+  /   \  +-----------+  /   \  +-----------+  /  ]
 [          \_______________/     \_______________/     \_______________/   ]
 +==========================================================================+
             +----+----------+----------+  KEY:============================||
twine_table: | id | loft_id  | token_id |  fk    : short for "foreign key" ||
             +----+--|-------+----------+  col   : short for "column"      ||
                     |                     wno: short for "with name of"   ||
                  +--V-+----------+        ?_id : fk to col wno "id"       ||
 loft_table:      | id |  bale_si |        ?_si : fk to col wno "stack_id" ||
                  +----+----|-----+        ?_gi : fk to col wno "group_id" ||
                            |              ================================||
              +----------+--V-+-------+----------+
 bale_stack:  | stack_id | id | locus | straw_gi |
              +-----|----+----+-------+-----|----+
                    |                       |
                    |             +----+---/|\----+-------------------+
straw_purse:        |             | id | group_id | foreign_record_id |
                    |             +----+--|-------+-----------------|-+
                    |                     |                         |
                    | stack_table:        | group_table:            |
group_table:     +--V-+----------+     +--V-+----------+            |
    AND          | id | checksum |     | id | checksum |            |
stack_table:     +----+----------+     +----+----------+            |
                                                                    |
                                            +-----------+--------+--V-+
cuecard_table                               | riddle_id | ink_id | id |
                                            +-----------+--------+----+

The straw bail is my data structure for storing an array of groups.
Each array of groups is known as a "loft". Because straw is stored
in a loft.

NOTE: XXXX_stack table is only required to have "stack_id" and "locus_id"
columns. The foreign_record_id only exists because this stack happens to be
stacking GROUPS/BUNDLES of cuecards or other information fitting the same
structure.

CAUTION: If you find ids are not working, or use of entities is crashing:
Remove these lines from EntityColumnDebugUtil.java .checkFiledType(...)
1: if(type == long.class){return;}
2: if(type == boolean.class){return;}
Pretty sure they will work. But I've had problems in past.
I'd prefer to work with such values if possible.
*/
////////////////// LOFT ABSTRACTION DOCUMENTATION END   ////////////////////////

/**
 *
 * @author jmadison :2015.11.02_0739PM(Nov11th,Year2015.Monday)
 */
@Path(ServletClassNames.LoftCTRL_MAPPING) 
public class LoftCTRL extends BaseCTRL{
    
    /**-------------------------------------------------------------------------
     * Used to test that table has been wired up to hibernate correctly.
     * 
     * Original use of this controller:
     * To test making of new record. May or may not be used in
     * actual development. Just here to make sure we can
     * read+write to table without errors.
     * @return : An unpopulated stub. EXCEPT FOR THE PRIMARY KEY. We set that.
     -------------------------------------------------------------------------*/  
    @GET
    @Path(FuncNameReg.MAKE_NEXT_LOFT)
    public Response make_next_loft(){
        
         //ENTER transaction:
        Session ses = TransUtil.enterTransaction();

        //Transaction logic:
        LoftTable wc = LoftTU.makeNextLoft();
        ses.save(wc); //<--force primary key to generate.

        //EXIT transaction:
        TransUtil.exitTransaction(ses, true);

        //Return entity as body of 200/ok response:
        return JSONUtil.entityToJSONResponse(wc);
        
    }//FUNC::END
        
}//CLASS::END
