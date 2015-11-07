package test.dbDataAbstractions.entities.tables;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import test.config.constants.identifiers.TableNameReg;
import test.config.constants.identifiers.VarNameReg;
import test.dbDataAbstractions.entities.bases.PurseEntity;

  
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
 * Bundles groups of "foreign_record_id" into clusters.
 * What table these foreign_record_id(s) are referencing depends on
 * what table on the TOP OF THE HEIRARCHY owns the entry.
 * 
 * For example: 
 * //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE//
 * If the twine_table owns a wildc_table record that owns
 * some wildc_stack records, which in turn own wildc_purse records...
 * 
 * The foreign_record_id column would mean "cuecard_id" since the twine_table
 * is used to bind cuecards within a deck into smaller ordered subsets.
 * //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE//
 * 
 * @author jmadison:2015.11.02_0605PM (November,2nd,Year2015.6:05PM)
 */
@Entity
@Table(name =StrawPurse.TABLE_NAME)
public class StrawPurse extends PurseEntity{
    
    /** Name of table this entity refers to, for easy refactoring. **/
    public static final String TABLE_NAME           = TableNameReg.STRAW_PURSE;
    /** Column name stored as string constant for easy refactoring. **/
    public static final String ID_COLUMN            = VarNameReg.ID;
    
    //Values referencing columns used for making criteria searches:
    public static final String FOREIGN_RECORD_ID_COLUMN = VarNameReg.FOREIGN_RECORD_ID;
    
    @Basic(fetch=FetchType.EAGER) //<--attempt to make group_id not null 
    @Column(name = FOREIGN_RECORD_ID_COLUMN)  //when getting it from database.
    public long foreign_record_id; //<--change to value-type.
    
}//CLASS::END
